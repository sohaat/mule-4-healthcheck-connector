package com.florencenext.connectors.healthcheck.internal.operations;

import com.florencenext.connectors.healthcheck.api.model.entities.Healtcheck;
import com.florencenext.connectors.healthcheck.api.model.entities.ServiceStatus;
import com.florencenext.connectors.healthcheck.api.model.entities.ServiceType;
import com.florencenext.connectors.healthcheck.api.model.errors.ExtensionErrorTypes;
import com.florencenext.connectors.healthcheck.api.parameter.ExternalConfigRef;
import com.florencenext.connectors.healthcheck.internal.providers.ExtensionErrorProviders;
import com.mule.extensions.amqp.api.config.ConsumerAckMode;
import com.mule.extensions.amqp.api.message.AmqpMessageBuilder;
import com.mule.extensions.amqp.api.message.AmqpProperties;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.client.DefaultOperationParameters;
import org.mule.runtime.extension.api.client.ExtensionsClient;
import org.mule.runtime.extension.api.client.OperationParameters;
import org.mule.runtime.extension.api.exception.ModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jms.JMSException;

import static com.florencenext.connectors.healthcheck.internal.helper.ErrorFormatterHelper.createErrorStringFromException;
import static org.mule.runtime.api.metadata.DataType.JSON_STRING;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.DEFAULT_TAB;


public class AmqpOperations {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AmqpOperations.class);
	private static final String amqpExtension = "AMQP";
	private static final String amqpConsumeOperation = "consume";
	private static final String amqpPublishOperation = "publish";
	private static ServiceType type = ServiceType.DB;
	private ServiceStatus status = ServiceStatus.UNHEALTHY;
	
	
	@Inject
	ExtensionsClient extensionsClient;

	@ParameterGroup(name = "Service to perform Healtcheck",showInDsl = true)
	@DisplayName("AMQP Service to perform Healtcheck")
	@Placement(tab = DEFAULT_TAB, order = 1)
	private ExternalConfigRef externalConfigRef;
	
    @Parameter
    @Optional
    @Summary("Name of the target exchange")
    @DisplayName("Exchange Destination")
    private String exchangeDestination;

	@Parameter
	@Optional
	@Summary("Name of the target queue destination")
	@DisplayName("Queue Destination")
	private String queueDestination;
	
	/***amqp healthcheck for external systems****/
	@MediaType(value = MediaType.APPLICATION_JSON, strict = false)
	@Alias("AMQP")
	@Throws(ExtensionErrorProviders.class)
	@Summary("Test an amqp service")
	public Healtcheck getAmqpHealthCheck() {

	String serviceName = externalConfigRef.getName();
	String configRef = 	externalConfigRef.getConfigurationName();
	String errorString = null;
	
	
	String destExchange = this.exchangeDestination;
	String destQueue = this.queueDestination;
	long startTime = 0, elapsedTime = 0;
	
	try {
		//publish
		OperationParameters parameters = DefaultOperationParameters.builder().configName(configRef)
				.addParameter("exchangeName", destExchange)
				.addParameter("messageBuilder", AmqpMessageBuilder.class, DefaultOperationParameters.builder()
						.addParameter("body", new TypedValue<>("test body", JSON_STRING))
						.addParameter("properties", new AmqpProperties()))
				.build();					
		extensionsClient.execute(amqpExtension, amqpPublishOperation, parameters);									
		LOGGER.debug("amqp publish executed!");

		//consume
		OperationParameters conParameters = DefaultOperationParameters.builder().configName(configRef)
				.addParameter("queueName", destQueue)
				.addParameter("ackMode", ConsumerAckMode.IMMEDIATE)
				.build();
	
		startTime = System.currentTimeMillis();											
		extensionsClient.execute(amqpExtension, amqpConsumeOperation, conParameters);			
		elapsedTime = System.currentTimeMillis() - startTime;
		
		LOGGER.debug("amqp consume executed!");
		status = ServiceStatus.HEALTHY;

	} catch (Exception e) {
		status = ServiceStatus.UNHEALTHY;
		elapsedTime = System.currentTimeMillis() - startTime;
		errorString = createErrorStringFromException(e);
		LOGGER.error("Error: " + e.getMessage());
		e.printStackTrace();
	}
	return new Healtcheck(serviceName, ServiceType.AMQP, status,(Integer) Math.toIntExact(elapsedTime),errorString);
 }
	
}

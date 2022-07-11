package com.florencenext.connectors.healthcheck.internal.operations;

import com.florencenext.connectors.healthcheck.api.model.entities.Healtcheck;
import com.florencenext.connectors.healthcheck.api.model.entities.HealthcheckError;
import com.florencenext.connectors.healthcheck.api.model.entities.ServiceStatus;
import com.florencenext.connectors.healthcheck.internal.configurations.HealtcheckConnectorConfig;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.florencenext.connectors.healthcheck.internal.helper.HealtcheckNormalizer.normalizeHealthCheck;
import static org.mule.runtime.api.meta.ExpressionSupport.REQUIRED;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class HealthCheckOperations {

	private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckOperations.class);


	/***heartbeat***/
	@MediaType(value = "application/java", strict = false)
	@Alias("CreateHealthcheckObject")
	public Healtcheck healthcheck(@Config HealtcheckConnectorConfig c,
								  @DisplayName("Dependencies")
								  @Expression(REQUIRED)
								  @Optional() List<Healtcheck> services){

		Healtcheck outputHealtcheck = new Healtcheck(
				c.getApplicationName(),
				c.getApplicationType(),
				ServiceStatus.HEALTHY);
		Integer outputTime = 0;

		if(!(services == null || services.size() == 0)) {

//
			for (Healtcheck dep : services) {
				LOGGER.debug("Computing dependency:"+dep);
				normalizeHealthCheck(dep);

				outputTime = dep.getTime() > outputTime ?  dep.getTime() : outputTime;

				if (!isHcHealty(dep)) {
					/**
					 * An error occured in one of the dependencies
					 * **/
					outputHealtcheck.setStatus(ServiceStatus.UNHEALTHY);
					LOGGER.debug("Bubbling up of dependency errors");
					for (HealthcheckError innerDepError : dep.getDependenciesErrors()) {
						outputHealtcheck.addHealtcheckError(innerDepError);
					}
					if(!(dep.getError() == null) && !dep.getError().isEmpty()){
						outputHealtcheck.addHealtcheckError(dep.getName(),dep.getError());
					}
				}
				outputHealtcheck.addDependency(dep);
			}
		}
		LOGGER.debug("FINAL HEALTCHECK:"+outputHealtcheck);
		outputHealtcheck.setTime(outputTime);
		return outputHealtcheck;
	}

	private boolean isHcHealty(Healtcheck hc){
		return (hc.getStatus() == ServiceStatus.HEALTHY) &&
				(hc.getDependenciesErrors() == null || hc.getDependenciesErrors().size() == 0 || hc.getDependenciesErrors() == null) &&
				(hc.getError() == null || hc.getError().isEmpty());
	}
}

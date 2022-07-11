package com.florencenext.connectors.healthcheck.api.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Healtcheck implements Serializable {


    private static final long serialVersionUID = -1854339524993191727L;

    private String name;
    private ServiceType type;
    private ServiceStatus status;
    @JsonProperty(required = false)
    private Integer time;
    @JsonProperty(required = false)
    private String error;
    @JsonProperty(required = false)
    private List<Healtcheck> dependencies;
    @JsonProperty(required = false)
    private List<HealthcheckError> dependenciesErrors;


    public void setName(String name) {
        this.name = name;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setDependencies(List<Healtcheck> dependencies) {
        this.dependencies = dependencies;
    }

    public void setDependenciesErrors(List<HealthcheckError> dependenciesErrors) {
        this.dependenciesErrors = dependenciesErrors;
    }

    public Healtcheck() {}

    public Healtcheck(String name,ServiceType type) {
        this.name = name;
        this.type = type;
        this.dependencies = new ArrayList<Healtcheck>();
        this.dependenciesErrors = new ArrayList<HealthcheckError>();
    }

    public Healtcheck(String name,ServiceStatus status,String error) {
        this.name = name;
        this.status = status;
        this.error = error;
        this.dependencies = new ArrayList<Healtcheck>();
        this.dependenciesErrors = new ArrayList<HealthcheckError>();
    }

    public Healtcheck(String name, ServiceType type, ServiceStatus status) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.dependencies = new ArrayList<Healtcheck>();
        this.dependenciesErrors = new ArrayList<HealthcheckError>();
    }
    public Healtcheck(String name, ServiceType type, ServiceStatus status, Integer time, String error) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.time = time;
        this.error = error;
    }

    public Healtcheck(String name, ServiceType type, ServiceStatus status, String error) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.time = 0;
        this.error = error;
        this.dependencies = new ArrayList<Healtcheck>();
        this.dependenciesErrors = new ArrayList<HealthcheckError>();
    }

    public String getName() {
        return name;
    }

    public ServiceType getType() {
        return type;
    }

    public ServiceStatus getStatus() {
        return status;
    }

    public Integer getTime() {
        return time;
    }

    public String getError() {
        return error;
    }

    public List<Healtcheck> getDependencies() {
        return dependencies;
    }

    public List<HealthcheckError> getDependenciesErrors() {
        return dependenciesErrors;
    }

    public void addDependency(Healtcheck dependency) {
        this.dependencies.add(dependency);
    }
    public void addHealtcheckError(HealthcheckError error) {
        this.dependenciesErrors.add(error);
    }

    public void addHealtcheckError(String serviceName,String error) {
        //To be removed this array
        this.dependenciesErrors.add(new HealthcheckError(serviceName,error));
    }

    @Override
    public String toString() {
        return "Healtcheck{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", time=" + time +
                ", error='" + error + '\'' +
                ", dependencies=" + dependencies +
                ", dependencyErrors=" + dependenciesErrors +
                '}';
    }


}

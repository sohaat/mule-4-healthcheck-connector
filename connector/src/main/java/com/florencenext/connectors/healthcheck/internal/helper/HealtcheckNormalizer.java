package com.florencenext.connectors.healthcheck.internal.helper;

import com.florencenext.connectors.healthcheck.api.model.entities.Healtcheck;
import com.florencenext.connectors.healthcheck.api.model.entities.HealthcheckError;

import java.util.ArrayList;

public class HealtcheckNormalizer {


    public static void normalizeHealthCheck(Healtcheck hc){

        if (hc.getTime() == null) hc.setTime(0);
        if (hc.getDependenciesErrors() == null) hc.setDependenciesErrors(new ArrayList<HealthcheckError>());

        if (hc.getDependencies() == null) {
            hc.setDependencies(new ArrayList<Healtcheck>());
        }
        else {
            for(Healtcheck dep: hc.getDependencies()){
                normalizeHealthCheck(dep);
            }
        }

    }
}

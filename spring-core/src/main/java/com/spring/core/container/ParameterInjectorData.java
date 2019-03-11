package com.spring.core.container;

import java.lang.reflect.Parameter;


public class ParameterInjectorData extends AbstractInjectorData {
    private Parameter parameter;

    public ParameterInjectorData(String defalultName, String refName, Parameter parameter) {
        super(defalultName, refName);
        this.parameter = parameter;
    }

    @Override
    public Class<?> getType() {
        return parameter.getType();
    }

}

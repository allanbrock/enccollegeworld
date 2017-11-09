package com.endicott.edu.application;
// Created by abrocken on 8/28/2017.

import java.util.Set;
import javax.ws.rs.core.Application;
import java.util.HashSet;

public class CustomApplication extends Application
    {
        //Add Service APIs
        @Override
        public Set<Class<?>> getClasses()
        {
            Set<Class<?>> resources = new HashSet<Class<?>>();

            //Manually adding MOXyJSONFeature
            resources.add(org.glassfish.jersey.moxy.json.MoxyJsonFeature.class);

            //Configure Moxy behavior
            resources.add(JsonMoxyConfigurationContextResolver.class);

            return resources;
        }
    }


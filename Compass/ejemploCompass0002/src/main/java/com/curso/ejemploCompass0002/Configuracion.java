/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploCompass0002;

import com.curso.ejemploCompass0002.modelo.Tweet;
import javax.annotation.PreDestroy;
import org.compass.core.Compass;
import org.compass.core.config.CompassConfiguration;
import org.compass.spring.LocalCompassSessionBean;
import org.compass.spring.support.CompassContextBeanPostProcessor;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author usuario
 */
@Configuration
public class Configuracion {

    private Compass compass;

    @Bean
    public Compass daIgual() {
        CompassConfiguration config = new CompassConfiguration().configure().addClass(Tweet.class);
        compass = config.buildCompass();
        compass.getSearchEngineIndexManager().deleteIndex();
        compass.getSearchEngineIndexManager().createIndex();
        return compass;
    }

    @PreDestroy
    private void limpiar() {
        compass.close();
    }

    @Bean(autowire = Autowire.BY_TYPE)
    public LocalCompassSessionBean daIgual2() {
        return new LocalCompassSessionBean();
    }

    @Bean
    public CompassContextBeanPostProcessor daIgual3() {
        return new CompassContextBeanPostProcessor();
    }
}

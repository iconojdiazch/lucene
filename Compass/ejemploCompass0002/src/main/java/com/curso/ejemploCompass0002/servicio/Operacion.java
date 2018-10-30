/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploCompass0002.servicio;

import org.compass.core.CompassSession;

/**
 *
 * @author iconotc
 * @param <T>
 */
@FunctionalInterface
public interface Operacion<T> {

    T ejecutar(final CompassSession s);
}

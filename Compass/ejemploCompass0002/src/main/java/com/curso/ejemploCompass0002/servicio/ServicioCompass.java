/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploCompass0002.servicio;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compass.core.Compass;
import org.compass.core.CompassException;
import org.compass.core.CompassSession;
import org.springframework.stereotype.Service;

/**
 *
 * @author iconotc
 */
@Service
public class ServicioCompass {

    private static final Logger LOG = Logger.getLogger(ServicioCompass.class.getName());

    private final Compass compass;

    public ServicioCompass(Compass compass) {
        this.compass = compass;
    }

    public <T> Optional<T> ejecutarEnTransaccion(final Operacion<T> op) {
        Optional<T> resultado = Optional.empty();
        CompassSession sesion = null;
        try {
            sesion = compass.openSession();
            sesion.beginTransaction();
            resultado = Optional.of(op.ejecutar(sesion));
            sesion.commit();
        } catch (CompassException e) {
            if (sesion != null) {
                sesion.rollback();
            }
            LOG.log(Level.SEVERE, "Ha ocurrido un error {0}", e);
        } finally {
            if (sesion != null) {
                sesion.close();
            }
        }
        return resultado;
    }
}

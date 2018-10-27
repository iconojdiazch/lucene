/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.plantillapractica;

import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.compass.core.CompassTransaction;
import org.compass.core.config.CompassConfiguration;

/**
 *
 * @author usuario
 */
// Necesitamos un Compass y un CompassTemplate
public class Principal {

    private static final Logger LOG = Logger.getLogger(Principal.class.getName());

    private Compass compass;
    private CompassTemplate compassTemplate;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Principal p = new Principal();
        p.inicializarCompass();
        try {
            Properties props = p.cargarPropiedades();
            p.indexarPropiedades(props);
            p.buscarPropiedades("clave:mercurio").forEach(out::println);
            p.buscarPropiedades("clave:* valor:*gaseoso*").forEach(out::println);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error en main", ex);
        } finally {
            p.cerrar();
        }
    }

    // Hay que crear una CompassConfiguration
    // Construir el objeto Compass que declaramos previamente
    // Decidir si borramos el índice existente, si lo hubiera
    // Crear un índice si es necesario
    // Instanciar un CompassTemplate
    private void inicializarCompass() {
    }

    // Cerrar el objeto compass
    private void cerrar() {

    }

    private Properties cargarPropiedades() throws IOException {
        Properties properties;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("planetas.properties")) {
            properties = new Properties();
            properties.load(is);
        }
        return properties;
    }

    // Obtener una Sesión y una transacción
    // Convertir los datos del objeto Properties en instancias de la clase Propiedad
    // Guardar los objetos en el índice
    // Hacer persistentes los cambios en el índice y cerrar la sesión  
    private void indexarPropiedades(Properties props) {

    }

    // Mediante el compassTemplate, ejecutar la consulta creando
    // un CompassCallback<List<Propiedad>>
    // Usar la sesón para obtener las sugerencias encontradas, método find
    // Introducir los objetos en una List<Propiedad> y devolverla
    private List<Propiedad> buscarPropiedades(String consulta) {
        return null;
    }
}

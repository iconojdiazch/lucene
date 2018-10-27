/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.practicaposiblesolucion;

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

    private void inicializarCompass() {
        CompassConfiguration config = new CompassConfiguration().configure().addClass(Propiedad.class);
        compass = config.buildCompass();
        compass.getSearchEngineIndexManager().deleteIndex();
        compass.getSearchEngineIndexManager().createIndex();
        compassTemplate = new CompassTemplate(compass);
    }

    private void cerrar() {
        compass.close();
    }

    private Properties cargarPropiedades() throws IOException {
        Properties properties;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("planetas.properties")) {
            properties = new Properties();
            properties.load(is);
        }
        return properties;
    }

    private void indexarPropiedades(Properties props) {
        CompassSession session = compass.openSession();
        CompassTransaction tx = session.beginTransaction();
        AtomicLong contador = new AtomicLong(1L);
        props.forEach(
                (clave, valor)
                -> session.save(new Propiedad(contador.incrementAndGet(), (String) clave, (String) valor))
        );
        tx.commit();
        session.close();
    }

    private List<Propiedad> buscarPropiedades(String consulta) {
        return compassTemplate.execute((CompassSession session) -> {
            List<Propiedad> r = new ArrayList<>();
            CompassHits hits = session.find(consulta);
            LOG.log(Level.INFO, "Hits: {0}", hits.length());
            for (int i = 0; i < hits.length(); i++) {
                r.add((Propiedad) hits.data(i));
            }
            return r;
        });
    }
}

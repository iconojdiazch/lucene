/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemplocompass0001;

import static java.lang.System.out;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import java.util.logging.Logger;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassCallbackWithoutResult;
import org.compass.core.CompassException;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.core.CompassTemplate;
import org.compass.core.CompassTransaction;
import org.compass.core.Resource;
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
        p.testPlanetas(p);
        p.testPersonas(p);
    }

    private void testPersonas(Principal p) {
        p.inicializarCompass();
        p.indexarPersona();
        Persona persona = p.buscarPersonaPorId(10);
        LOG.info(persona.toString());
        String consulta = "nombre:cualquiera AND nombreAficion:tenis OR nombreInteres:Juego*";
        p.buscarPersonasUsandoCompassCallback(consulta).forEach(out::println);
        p.cerrar();
    }

    private Persona buscarPersonaPorId(Integer id) {
        Persona p;
        CompassSession session = compass.openSession();
        CompassTransaction tx = session.beginTransaction();
        p = session.load(Persona.class, id);
        tx.commit();
        session.close();
        return p;
    }

    private List<Persona> buscarPersonasUsandoCompassCallback(String consulta) {
        return compassTemplate.execute(new CompassCallback<List<Persona>>() {
            @Override
            public List<Persona> doInCompass(CompassSession session) throws CompassException {
                List<Persona> r = new ArrayList<>();
                CompassHits sugerencias = session.find(consulta);
                System.out.println("Se han encontrado [" + sugerencias.getLength() + "] sugerencias para [" + consulta + "]");
                for (int i = 0; i < sugerencias.getLength(); i++) {
                    r.add((Persona) sugerencias.data(i));
                }
                return r;
            }
        });
    }

    private void testPlanetas(Principal p) {
        p.inicializarCompass();
        p.indexarPlanetas(p.crearObjetosPlaneta());
        p.buscarPlanetasUsandoCompassCallbackWithoutResult("nombre:* AND tipo:rocoso AND -marte");
        p.buscarPlanetasUsandoCompassCallback("nombre:* AND tipo:rocoso AND -marte").forEach(out::println);
        LOG.info("Buscando el planeta mercurio".toUpperCase());
        Planeta mercurio = p.buscarPlanetaPorId(10);
        System.out.println("::::::" + mercurio);
        LOG.info("Actualizando el planeta mercurio".toUpperCase());
        mercurio = p.actualizarPlanetaPorId(10);
        System.out.println("::::::" + mercurio);
        LOG.info("borrando el planeta mercurio".toUpperCase());
        p.borrarPlanetaPorId(10);
        p.cerrar();
    }

    private void inicializarCompass() {
        LOG.info("Inicializando Compass".toUpperCase());
//        CompassConfiguration config = new CompassConfiguration().configure().addClass(Planeta.class);
//        CompassConfiguration config = new CompassConfiguration().configure("/compass.cfg.hsqldb.xml").addClass(Planeta.class).addClass(Persona.class).addClass(Aficion.class).addClass(Interes.class);
        CompassConfiguration config = new CompassConfiguration().configure("/compass.cfg.derby.xml").addClass(Planeta.class).addClass(Persona.class).addClass(Aficion.class).addClass(Interes.class);
        compass = config.buildCompass();
        compass.getSearchEngineIndexManager().deleteIndex();
        compass.getSearchEngineIndexManager().createIndex();
        compassTemplate = new CompassTemplate(compass);
        LOG.info("Fin de la inicialización".toUpperCase());
    }

    private void cerrar() {
        compass.close();
        LOG.info("Compass cerrado".toUpperCase());
    }

    private List<Planeta> crearObjetosPlaneta() {
        List<Planeta> planetas = new ArrayList<>();
        Planeta planeta = new Planeta();
        planeta.setCodigo(10);
        planeta.setNombre("Mercurio");
        planeta.setDiametro(4879);
        planeta.setTipo("rocoso");
        planeta.setSignificado("mensajero de los dioses");
        planetas.add(planeta);
        return planetas;
    }

    private List<Planeta> indexarPlanetas(List<Planeta> planetas) {
        CompassSession session = compass.openSession();
        CompassTransaction tx = session.beginTransaction();
        planetas.forEach(session::save);
        tx.commit();
        session.close();
        return planetas;
    }

    private void mostrar(CompassHits hits, int hitNumber) {
        Planeta planeta = (Planeta) hits.data(hitNumber);
        Resource resource = hits.resource(hitNumber);
        System.out.println("ALIAS [" + resource.getAlias() + "] ID [" + planeta.getCodigo() + "] SCORE ["
                + hits.score(hitNumber) + "]");
        System.out.println("El planeta es:" + planeta);
    }

    private void buscarPlanetasUsandoCompassCallbackWithoutResult(String consulta) {

        compassTemplate.execute(new CompassCallbackWithoutResult() {
            @Override
            protected void doInCompassWithoutResult(CompassSession session) throws CompassException {
                CompassHits sugerencias = session.find(consulta);
                System.out.println("Se han encontrado [" + sugerencias.getLength() + "] sugerencias para [" + consulta + "]");
                System.out.println("Mostrando los datos encontrados...".toUpperCase());
                for (int i = 0; i < sugerencias.getLength(); i++) {
                    mostrar(sugerencias, i);
                }
                sugerencias.close();
            }
        });
    }

    private List<Planeta> buscarPlanetasUsandoCompassCallback(String consulta) {
        return compassTemplate.execute(new CompassCallback<List<Planeta>>() {
            @Override
            public List<Planeta> doInCompass(CompassSession session) throws CompassException {
                List<Planeta> r = new ArrayList<>();
                CompassHits sugerencias = session.find(consulta);
                System.out.println("Se han encontrado [" + sugerencias.getLength() + "] sugerencias para [" + consulta + "]");
                for (int i = 0; i < sugerencias.getLength(); i++) {
                    r.add((Planeta) sugerencias.data(i));
                }
                return r;
            }
        });
    }

    private Planeta buscarPlanetaPorId(Integer id) {
        Planeta p;
        CompassSession session = compass.openSession();
        CompassTransaction tx = session.beginTransaction();
        p = session.load(Planeta.class, id);
        tx.commit();
        session.close();
        return p;
    }

    private void borrarPlanetaPorId(Integer id) {
        CompassSession session = compass.openSession();
        CompassTransaction tx = session.beginTransaction();
        session.delete(Planeta.class, id);
        try {
            session.load(Planeta.class, id);
        } catch (CompassException e) {
            System.out.println("::::::Error: " + e);
        }
        tx.commit();
        session.close();
    }

    private Planeta actualizarPlanetaPorId(Integer id) {
        Planeta p;
        CompassSession session = compass.openSession();
        CompassTransaction tx = session.beginTransaction();
        p = session.load(Planeta.class, id);
        p.setDiametro(0L);
        session.save(p);
        tx.commit();
        session.close();
        return p;
    }

    private void indexarPersona() {
        CompassSession session = compass.openSession();
        CompassTransaction tx = session.beginTransaction();
        Persona p = new Persona(10L, "cualquiera");
        p.setAficion(new Aficion("tenis"));
        p.setIntereses(asList(new Interes(20L, "Informática"), new Interes(30L, "Juego de Tronos")));
        session.save(p);
        tx.commit();
        session.close();
    }
}

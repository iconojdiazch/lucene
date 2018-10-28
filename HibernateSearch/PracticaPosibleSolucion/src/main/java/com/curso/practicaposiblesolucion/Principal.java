/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.practicaposiblesolucion;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

/**
 *
 * @author usuario
 */
public class Principal {

    private static final Logger LOG = Logger.getLogger(Principal.class.getName());
    private SessionFactory sf;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Principal p = new Principal();
        p.inicializarHibernate();
        p.inicializarBd();
        p.mostrarObjetos();
        p.crearIndice();
        final String consulta = "nombre:Un* calle:Una*";
        final String[] campos = {"nombre", "direccion.calle"};
        List<Persona> l = p.buscar(consulta, campos);
        LOG.log(Level.INFO, "Encontradas {0} personas", l.size());
        l.forEach(out::println);
        p.usarProyecciones();
        p.cerrar();
    }

    private void inicializarHibernate() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            LOG.info("Factoría de sesiones creada");
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            LOG.log(Level.SEVERE, "Error irrecuperable: {0}", e);
            System.exit(0);
        }
    }

    private void cerrar() {
        sf.close();
        LOG.info("Factoría de sesiones cerrada");
    }

    private void inicializarBd() {
        Session s = sf.getCurrentSession();
        s.getTransaction().begin();
        s.save(new Persona("Un nombre cualquiera", new Direccion("Una calle cualquiera")));
        s.getTransaction().commit();
    }

    private void mostrarObjetos() {
        Session s = sf.getCurrentSession();
        s.getTransaction().begin();
        List<Persona> list = s.createQuery("select p from Persona p", Persona.class).list();
        s.getTransaction().commit();
        list.forEach(out::println);
    }

    private void crearIndice() {
        Session s = sf.getCurrentSession();
        FullTextSession fts = Search.getFullTextSession(s);
        try {
            fts.createIndexer().startAndWait();
            LOG.info("Índice (re)creado");
        } catch (InterruptedException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    private List<Persona> buscar(String consulta, String[] campos) {
        List<Persona> personas = new ArrayList<>();
        FullTextSession fts = null;
        try {
            Session s = sf.getCurrentSession();
            fts = Search.getFullTextSession(s);
            fts.getTransaction().begin();
            MultiFieldQueryParser parser = new MultiFieldQueryParser(campos,
                    new StandardAnalyzer());
            org.apache.lucene.search.Query query = parser.parse(consulta);
            FullTextQuery hibQuery = fts.createFullTextQuery(query, Persona.class);
            personas = hibQuery.list();
            fts.getTransaction().commit();
        } catch (ParseException | HibernateException ex) {
            if (fts != null) {
                fts.getTransaction().rollback();
            }
            LOG.log(Level.SEVERE, "Ha ocurrido un error {0}", ex);
        }
        return personas;
    }

    private void usarProyecciones() {
        try {
            final String consulta = "nombre:Un* calle:Una*";
            final String[] campos = {"nombre", "direccion.calle"};
            FullTextSession fts = null;
            Session s = sf.getCurrentSession();
            fts = Search.getFullTextSession(s);
            fts.getTransaction().begin();
            MultiFieldQueryParser parser = new MultiFieldQueryParser(campos,
                    new StandardAnalyzer());
            org.apache.lucene.search.Query query = parser.parse(consulta);
            FullTextQuery hibQuery = fts.createFullTextQuery(query, Persona.class);
            hibQuery.setProjection(
                    FullTextQuery.SCORE,
                    FullTextQuery.THIS,
                    "direccion.calle");
            List list = hibQuery.list();
            fts.getTransaction().commit();
            Object[] resultado = (Object[]) list.get(0);
            float score = (float) resultado[0];
            Persona persona = (Persona) resultado[1];
            String calle = (String) resultado[2];
            System.out.printf("Score:%f\nPersona: %s\nCalle:%s\n", score, persona, calle);
        } catch (ParseException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }
}

/*
org.hibernate.search.FullTextQuery query =
    s.createFullTextQuery(luceneQuery, Book.class);
query.setProjection(
    FullTextQuery.SCORE,
    FullTextQuery.THIS,
    "mainAuthor.name" );
List results = query.list();
Object[] firstResult = (Object[]) results.get(0);
float score = firstResult[0];
Book book = firstResult[1];
String authorName = firstResult[2];
 */

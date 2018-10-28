/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.hibernatesearch0002.util;

import com.curso.hibernatesearch0002.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

/**
 *
 * @author usuario
 */
public class SearchUtil implements LogUtil {

    public static void crearIndice(Optional<SessionFactory> sf) {
        sf
                .map(SessionFactory::getCurrentSession)
                .map(Search::getFullTextSession)
                .map(FullTextSession::createIndexer)
                .ifPresent(
                        massIndexer -> {
                            try {
                                massIndexer.startAndWait();
                            } catch (InterruptedException ex) {
                                LOG.log(Level.SEVERE, "Error al crear el índice", ex);
                            }
                        }
                );
    }

    public static void buscarSinQueryBuilber(Optional<SessionFactory> sf, final String consulta, final String[] campos) {
        LOG.info("Buscando sin emplear QueryBuilder".toUpperCase());
        sf.map(
                sesion -> buscar(sesion, consulta, campos)
        ).ifPresent(libros -> LOG.log(Level.INFO, "Encontrados {0} libro(s) que verifican {1}", new Object[]{libros.size(), consulta}));
    }

    public static void buscarConQueryBuilder(Optional<SessionFactory> sf, final String consulta) {
        LOG.info("Buscando con QueryBuilder".toUpperCase());
        sf.map(
                sesion -> buscarQueryBuilder(sesion, consulta)
        ).ifPresent(libros -> LOG.log(Level.INFO, "Encontrados {0} libro(s) que verifican {1}", new Object[]{libros.size(), consulta}));
    }

    private static List<Book> buscar(SessionFactory sessionFactory, String consulta, String[] campos) {
        List<Book> libros = new ArrayList<>();
        FullTextSession fts = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            fts = Search.getFullTextSession(session);
            fts.getTransaction().begin();
            MultiFieldQueryParser parser = new MultiFieldQueryParser(campos,
                    new StandardAnalyzer());
            org.apache.lucene.search.Query query = parser.parse(consulta);
            FullTextQuery hibQuery = fts.createFullTextQuery(query, Book.class);
            libros = hibQuery.list();
            fts.getTransaction().commit();
        } catch (ParseException | HibernateException ex) {
            if (fts != null) {
                fts.getTransaction().rollback();
            }
            LOG.log(Level.SEVERE, "Ha ocurrido un error {0}", ex);
        }
        return libros;
    }

    private static List<Book> buscarQueryBuilder(SessionFactory sessionFactory, String consulta) {
        List<Book> libros = new ArrayList<>();
        FullTextSession fts = null;
        try {
            Session session = sessionFactory.getCurrentSession();
            fts = Search.getFullTextSession(session);
            QueryBuilder b = fts.getSearchFactory().buildQueryBuilder().forEntity(Book.class).get();
            fts.getTransaction().begin();
            org.apache.lucene.search.Query query = b.keyword().onFields("title", "subtitle", "authors.name").matching(consulta).createQuery();
            FullTextQuery hibQuery = fts.createFullTextQuery(query, Book.class);
            libros = hibQuery.list();
            fts.getTransaction().commit();
        } catch (HibernateException ex) {
            if (fts != null) {
                fts.getTransaction().rollback();
            }
            LOG.log(Level.SEVERE, "Ha ocurrido un error {0}", ex);
        }
        return libros;
    }
}

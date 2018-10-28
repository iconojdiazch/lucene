/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.hibernatesearch0002.util;

import com.curso.hibernatesearch0002.Author;
import com.curso.hibernatesearch0002.Book;
import static java.util.Arrays.asList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import static java.util.stream.Collectors.toSet;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 *
 * @author usuario
 */
public class HibernateUtil implements LogUtil {

    private static Optional<SessionFactory> sessionFactory = Optional.empty();

    static {
        inicializarHibernate();
    }

    public static Optional<SessionFactory> getSessionFactory() {
        return sessionFactory;
    }

    private static Optional<SessionFactory> inicializarHibernate() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = Optional.ofNullable(new MetadataSources(registry).buildMetadata().buildSessionFactory());
            LOG.info(":::Factoría de sesiones creada".toUpperCase());
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            LOG.log(Level.SEVERE, "Error irrecuperable: {0}", e);
            System.exit(0);
        }
        return sessionFactory;
    }

    public static void cerrar() {
        sessionFactory.ifPresent(
                s -> {
                    s.close();
                    LOG.info("Factoría de sesiones cerrada".toUpperCase());
                }
        );
    }

    public static Optional<Book> crearObjetosPersistentes(Optional<SessionFactory> sf) {
        return sf.map(SessionFactory::getCurrentSession).map(
                s -> {
                    Set<Author> autores = asList("Miguel", "Cervantes", "Saavedra")
                            .stream()
                            .map(Author::new)
                            .collect(toSet());
                    Book b = new Book("El Quijote", "El ingenioso hidalgo de la Mancha", new Date());
                    b.setAuthors(autores);
                    s.getTransaction().begin();
                    s.save(b);
                    s.getTransaction().commit();
                    return b;
                }
        );
    }

    public static void mostrarTodosLosObjetos(Optional<SessionFactory> sf) {
        sf.map(SessionFactory::getCurrentSession).map(
                s -> {
                    s.getTransaction().begin();
                    List<Book> l = s.createQuery("select distinct b from Book b join fetch b.authors", Book.class).list();
                    s.getTransaction().commit();
                    return l;
                }
        ).ifPresent(book -> LOG.info(book.toString().toUpperCase()));
    }
}

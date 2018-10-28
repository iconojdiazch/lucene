/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.plantillapractica;

import static java.lang.System.out;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

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
        final String consulta = "";
        p.buscar(consulta);
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
        LOG.info("Crear índice pendiente");
    }

    private void buscar(String consulta) {
        LOG.info("Buscar en el índice pendiente");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistema;

import beans.Usuario;
import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.hibernate.CacheMode;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

/**
 *
 * @author Chema
 */
public class Principal {

    private static final Logger LOG = Logger.getLogger(Principal.class.getName());
    private SessionFactory sessionFactory;
    private static final String DIRECTORIO = "indices-search";

    public static void main(String[] args) {
        Principal p = new Principal();
        try {
            p.borrarDirectorio(new File(DIRECTORIO));
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Ha ocurrido un error al borrar el directorio", ex);
        }
        p.validarUsuario();
        p.inicializarHibernate();
        p.crearUsuarios(1000);
        p.indexarUsuarios();
        p.buscarUsuariosConAficion("montañismo");
        p.mostrarUsuarios(10);
        p.cerrar();
    }

    private void inicializarHibernate() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            LOG.log(Level.SEVERE, "Error irrecuperable: {0}", e);
            System.exit(0);
        }
    }

    private void cerrar() {
        sessionFactory.close();
        System.out.println("Factoría de sesiones cerrada");
    }

    void borrarDirectorio(File directorio) throws IOException {
        FileUtils.deleteDirectory(directorio);
        LOG.log(Level.INFO, "Directorio {0} borrado", directorio.getName());
    }

    /**
     * @param args the command line arguments
     */
    private void buscarUsuariosConAficion(String aficion) {
        FullTextSession fts = null;
        try {
            System.out.println("Buscando usuarios aficionados al " + aficion);
            final long a = System.currentTimeMillis();
            Session session = sessionFactory.getCurrentSession();
            fts = Search.getFullTextSession(session);
            fts.getTransaction().begin();
            MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"aficiones"},
                    new StandardAnalyzer());
            org.apache.lucene.search.Query query = parser.parse(aficion);
            FullTextQuery hibQuery = fts.createFullTextQuery(query, Usuario.class);
            List<Usuario> usuarios = hibQuery.list();
            System.out.format("Encontrados %d usuarios aficionados al %s%n", usuarios.size(), aficion);
            fts.getTransaction().commit();
            long b = System.currentTimeMillis();
            System.out.println("Fin de la transacción. Tiempo empleado en milisegundos " + (b - a));
        } catch (ParseException | HibernateException ex) {
            if (fts != null) {
                fts.getTransaction().rollback();
            }
            LOG.log(Level.SEVERE, "Ha ocurrido un error {0}", ex);
        }
    }

    private void crearUsuarios(Integer cuantos) {
        Session session = null;
        Usuario usuario;
        System.out.format("Creando %d usuarios%n", cuantos);
        try {
            final long a = System.currentTimeMillis();
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            for (int i = 0; i < cuantos; i++) {
                usuario = new Usuario("Uno" + i, "clave " + i, "abcd" + i);
                usuario.setCorreo("xyz@abc.com");
                if (i % 10 == 0) {
                    usuario.setAficiones("fútbol, baloncesto, tenis, natación");
                } else {
                    usuario.setAficiones("fútbol, baloncesto, tenis, montañismo");
                }
                session.save(usuario);
                if (i % 40 == 0) {
                    session.flush();
                    session.clear();
                    System.out.println("Usuarios insertados " + i);
                }
            }
            long b = System.currentTimeMillis();
            System.out.println("Fin de la inserción. Tiempo empleado en milisegundos " + (b - a));
            session.getTransaction().commit();
            b = System.currentTimeMillis();
            System.out.println("Fin de la transacción. Tiempo (inserción + commit) empleado en milisegundos " + (b - a));
        } catch (HibernateException ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            LOG.log(Level.SEVERE, "Ha ocurrido un error {0}", ex);
        }
    }

    private void indexarUsuarios() {
        Session session = null;
        FullTextSession fts;
        int contador = 0;
        System.out.println("Comenzando la construcción del índice...");
        long a = System.currentTimeMillis();
        try {
            session = sessionFactory.getCurrentSession();
            fts = Search.getFullTextSession(session);
            fts.getTransaction().begin();
            try (ScrollableResults usuarios = session.createQuery("select u from Usuario u", Usuario.class).setCacheMode(CacheMode.IGNORE).scroll()) {
                while (usuarios.next()) {
                    Usuario u = (Usuario) usuarios.get(0);
                    fts.index(u);
                    session.evict(u);
                    if (++contador % 100 == 0) {
                        System.out.println("Usuarios indexados " + contador);
                    }
                }
            }
            fts.getTransaction().commit();
            long b = System.currentTimeMillis();
            System.out.println("Fin de la transacción. Tiempo empleado en milisegundos " + (b - a));
            System.out.format("Indexados %d usuarios%n", contador);
        } catch (HibernateException ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            LOG.log(Level.SEVERE, "Ha ocurrido un error {0}", ex);
        }
    }

    private void mostrarUsuarios(Integer maximo) {
        Session session = null;
        try {
            session = sessionFactory.getCurrentSession();
            session.getTransaction().begin();
            Query<Usuario> query = session.createQuery("select u from Usuario u where u.aficiones like '%montañismo%'", Usuario.class);
            query.setMaxResults(maximo);
            List<Usuario> usuarios = query.list();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios que mostrar");
            } else {
                usuarios.forEach(out::println);
            }
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            LOG.log(Level.SEVERE, "Ha ocurrido un error {0}", ex);
        }
    }

    private void validarUsuario() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Usuario usuario = new Usuario();
        Set<ConstraintViolation<Usuario>> errores = validator.validate(usuario);
        for (ConstraintViolation<Usuario> error : errores) {
            System.out.format("Error: %s. Mensaje: %s.%n", error.getInvalidValue(), error.getMessage());
        }

        usuario = new Usuario("uno", "dos", "abc");
        usuario.setCorreo("xyz");
        errores = validator.validate(usuario);
        for (ConstraintViolation<Usuario> error : errores) {
            System.out.format("Error: %s. Mensaje: %s.%n", error.getInvalidValue(), error.getMessage());
        }
    }
}

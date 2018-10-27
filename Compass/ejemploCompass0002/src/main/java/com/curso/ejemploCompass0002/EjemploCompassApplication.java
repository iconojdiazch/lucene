package com.curso.ejemploCompass0002;

import com.curso.ejemploCompass0002.modelo.Tweet;
import com.curso.ejemploCompass0002.repos.TweetRepository;
import static java.util.Arrays.asList;
import java.util.List;
import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
import org.compass.spring.LocalCompassSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EjemploCompassApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(EjemploCompassApplication.class, args);
    }

    @Autowired
    private TweetRepository repo;

    @Autowired
    private Compass compass;
    
    @Override
    public void run(String... args) throws Exception {
        asList("uno","dos","tres").stream().map(t -> new Tweet(t)).forEach(repo::save);
        List<Tweet> findAll = repo.findAll();
        System.out.println("::::::Indexando");  
        CompassSession sesion = compass.openSession();
        sesion.beginTransaction();
        findAll.forEach(sesion::save);        
        CompassHits find = sesion.find("text:*");
        System.out.println("::::::Hits: " + find.length());
        sesion.commit();        
        sesion.close();
    }
}

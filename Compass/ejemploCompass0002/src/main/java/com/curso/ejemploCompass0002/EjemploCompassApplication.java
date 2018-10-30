package com.curso.ejemploCompass0002;

import com.curso.ejemploCompass0002.modelo.Tweet;
import com.curso.ejemploCompass0002.repos.TweetRepository;
import com.curso.ejemploCompass0002.servicio.ServicioCompass;
import java.util.stream.Stream;
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
    private ServicioCompass sc;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("::::::Indexando");
        Integer cuantosHits = sc.ejecutarEnTransaccion(
                sesion -> {
                    Stream
                            .of("uno", "dos", "tres")
                            .map(Tweet::new)
                            .map(repo::save)
                            .forEach(sesion::save);
                    return sesion.find("text:*").length();
                }
        ).orElse(0);
        System.out.println("::::::Hits: " + cuantosHits);
    }
}

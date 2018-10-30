/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploCompass0002.controladores;

import com.curso.ejemploCompass0002.servicio.Operacion;
import com.curso.ejemploCompass0002.servicio.ServicioCompass;
import com.curso.ejemploCompass0002.modelo.Tweet;
import com.curso.ejemploCompass0002.repos.TweetRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.compass.core.CompassHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author usuario
 */
@RestController
public class TweetController {

    private static final Logger LOG = Logger.getLogger(TweetController.class.getName());

    private final TweetRepository repo;

    public TweetController(TweetRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String inicio() {
        return "Ejemplo CRUD con JPA y Spring Boot";
    }

    @Autowired
    private ServicioCompass sc;

    private final Operacion<List<Tweet>> todos = s -> {
        List<Tweet> l = new ArrayList<>();
        CompassHits hits = s.find("text:*");
        System.out.println("::::::Hits: " + hits.length());
        for (int i = 0; i < hits.length(); i++) {
            l.add((Tweet) hits.data(i));
        }
        return l;
    };

    @GetMapping("/tweetscompass")
    public List<Tweet> tweetsDesdeIndice() {
        return sc.ejecutarEnTransaccion(todos).orElse(Collections.emptyList());
    }

    @GetMapping("/tweets")
    public List<Tweet> tweets() {
        return repo.findAll();
    }

    @PostMapping("/tweets")
    public Tweet crearTweet(@Valid @RequestBody Tweet tweet) {
        LOG.log(Level.INFO, "Desde Compass en método crearTweet: {0}", tweet.toString());
        Tweet tw = repo.save(tweet);
        return sc.ejecutarEnTransaccion(
                s -> {
                    s.save(tw);
                    return tw;
                }
        ).orElse(new Tweet("No existe"));
    }

    @GetMapping("/tweets/{id}")
    public Tweet tweetById(@PathVariable("id") Long id) {
        Tweet tw = sc.ejecutarEnTransaccion(
                s -> s.load(Tweet.class, id)
        ).orElse(new Tweet("No existe"));
        LOG.log(Level.INFO, "Desde Compass en método tweetById: {0}", tw.toString());
        return repo.findById(id).orElse(new Tweet("No existe"));
    }

    @PutMapping("/tweets/{id}")
    public Tweet actualizarTweet(@PathVariable("id") Long id, @Valid @RequestBody Tweet tweet) {
        Tweet tw = repo.findById(id).map(
                t -> {
                    t.setText(tweet.getText());
                    return t;
                }
        ).map(repo::save).orElse(new Tweet("No existe"));
        sc.ejecutarEnTransaccion(
                s -> {
                    s.save(tw);
                    return tw;
                }
        );
        LOG.log(Level.INFO, "Desde Compass en método actualizarTweet: {0}", tw.toString());
        return tw;
    }

    @DeleteMapping("/tweets/{id}")
    public void borrarTweet(@PathVariable("id") Long id) {
        sc.ejecutarEnTransaccion(
                s -> {
                    s.delete(Tweet.class, id);
                    return id;
                }
        );
        LOG.info("Desde Compass en método borrarTweet");
        repo.findById(id).ifPresent(repo::delete);
    }
}

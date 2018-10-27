/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploCompass0002.controladores;

import com.curso.ejemploCompass0002.modelo.Tweet;
import com.curso.ejemploCompass0002.repos.TweetRepository;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.compass.core.Compass;
import org.compass.core.CompassHits;
import org.compass.core.CompassSession;
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

    private final TweetRepository repo;

    public TweetController(TweetRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public String inicio() {
        return "Ejemplo CRUD con JPA y Spring Boot";
    }

    
    @Autowired
    private Compass compass;
    
    @GetMapping("/tweetscompass")    
    public List<Tweet> tweetsDesdeIndice() {
        List<Tweet> l = new ArrayList<>();
        CompassSession sesion = compass.openSession();
        sesion.beginTransaction();
        CompassHits hits = sesion.find("text:*");
        System.out.println("::::::Hits: " + hits.length());
        for (int i = 0; i < hits.length(); i++) {
            l.add((Tweet) hits.data(i));
        }
        sesion.commit();
        sesion.close();
        return l;
    }
    
    @GetMapping("/tweets")
    public List<Tweet> tweets() {
        return repo.findAll();
    }

    @PostMapping("/tweets")
    public Tweet crearTweet(@Valid @RequestBody Tweet tweet) {
        return repo.save(tweet);
    }

    @GetMapping("/tweets/{id}")
    public Tweet tweetById(@PathVariable("id") Long id) {
        return repo.findById(id).orElse(new Tweet("No existe"));
    }

    @PutMapping("/tweets/{id}")
    public Tweet actualizarTweet(@PathVariable("id") Long id, @Valid @RequestBody Tweet tweet) {
        return repo.findById(id).map(
                t -> {
                    t.setText(tweet.getText());
                    return t;
                }
        ).map(repo::save).orElse(new Tweet("No existe"));
    }

    @DeleteMapping("/tweets/{id}")
    public void borrarTweet(@PathVariable("id") Long id) {
        repo.findById(id).ifPresent(repo::delete);
    }
}

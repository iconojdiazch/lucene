/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploCompass0002.repos;

import com.curso.ejemploCompass0002.modelo.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author usuario
 */
public interface TweetRepository extends JpaRepository<Tweet, Long> {

}

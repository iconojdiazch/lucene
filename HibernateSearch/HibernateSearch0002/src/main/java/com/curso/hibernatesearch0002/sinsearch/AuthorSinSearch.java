/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.hibernatesearch0002.sinsearch;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author usuario
 */
@Entity
public class AuthorSinSearch {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    public AuthorSinSearch() {
    }

}

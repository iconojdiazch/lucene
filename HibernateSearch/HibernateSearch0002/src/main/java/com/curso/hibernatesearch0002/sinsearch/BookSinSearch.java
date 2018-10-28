/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.hibernatesearch0002.sinsearch;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author usuario
 */
@Entity
public class BookSinSearch {

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private String subtitle;

    @ManyToMany
    private Set<AuthorSinSearch> authors = new HashSet<AuthorSinSearch>();

    private Date publicationDate;

    public BookSinSearch() {
    }
}

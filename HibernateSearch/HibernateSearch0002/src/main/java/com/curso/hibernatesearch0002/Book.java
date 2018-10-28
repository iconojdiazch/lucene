/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.hibernatesearch0002;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author usuario
 */
@Entity
@Indexed
public class Book {

    @Id
    @GeneratedValue
    private Integer id;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String title;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    private String subtitle;

    @Field(index = Index.YES, analyze = Analyze.NO, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date publicationDate;

//    @IndexedEmbedded
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Author> authors = new HashSet<Author>();

    public Book() {
    }

    public Book(String title, String subtitle, Date publicationDate) {
        this.title = title;
        this.subtitle = subtitle;
        this.publicationDate = publicationDate;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", subtitle=" + subtitle + ", publicationDate=" + publicationDate + ", authors=" + authors + '}';
    }

}

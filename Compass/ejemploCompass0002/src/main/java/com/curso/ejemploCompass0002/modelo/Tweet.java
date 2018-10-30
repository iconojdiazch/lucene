/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploCompass0002.modelo;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

/**
 *
 * @author usuario
 */
@Entity
@Searchable
public class Tweet {

    @SearchableId
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Size(max = 140)
    @SearchableProperty
    private String text;

    @NotNull
    @SearchableProperty
    private Date createdAt = new Date();

    public Tweet() {
    }

    public Tweet(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Tweet{" + "id=" + id + ", text=" + text + ", createdAt=" + createdAt + '}';
    }

}

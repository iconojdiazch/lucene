/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemplocompass0001;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

/**
 *
 * @author usuario
 */
@Searchable
public class Interes {

    @SearchableId
    private Long id;
    @SearchableProperty
    private String nombreInteres;

    public Interes() {
    }

    public Interes(Long id, String nombre) {
        this.id = id;
        this.nombreInteres = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreInteres() {
        return nombreInteres;
    }

    public void setNombreInteres(String nombreInteres) {
        this.nombreInteres = nombreInteres;
    }

    @Override
    public String toString() {
        return "Interes{" + "id=" + id + ", nombre=" + nombreInteres + '}';
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemplocompass0001;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableProperty;

/**
 *
 * @author usuario
 */
@Searchable(root = false)
public class Aficion {

    @SearchableProperty
    private String nombreAficion;

    public Aficion() {
    }

    public Aficion(String nombre) {
        this.nombreAficion = nombre;
    }

    public String getNombreAficion() {
        return nombreAficion;
    }

    public void setNombreAficion(String nombreAficion) {
        this.nombreAficion = nombreAficion;
    }

    @Override
    public String toString() {
        return "Aficion{" + "nombre=" + nombreAficion + '}';
    }

}

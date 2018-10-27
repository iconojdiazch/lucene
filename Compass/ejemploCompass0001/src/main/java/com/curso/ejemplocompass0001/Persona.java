/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemplocompass0001;

import java.util.List;
import org.compass.annotations.Cascade;
import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableComponent;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;
import org.compass.annotations.SearchableReference;

/**
 *
 * @author usuario
 */
@Searchable
public class Persona {

    @SearchableId
    private Long id;
    @SearchableProperty
    private String nombre;
    @SearchableComponent
    private Aficion aficion;

    @SearchableReference(cascade = Cascade.ALL)
    private List<Interes> intereses;

    public Persona() {
    }

    public Persona(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Aficion getAficion() {
        return aficion;
    }

    public void setAficion(Aficion aficion) {
        this.aficion = aficion;
    }

    public List<Interes> getIntereses() {
        return intereses;
    }

    public void setIntereses(List<Interes> intereses) {
        this.intereses = intereses;
    }

    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", nombre=" + nombre + ", aficion=" + aficion + ", intereses=" + intereses + '}';
    }
}

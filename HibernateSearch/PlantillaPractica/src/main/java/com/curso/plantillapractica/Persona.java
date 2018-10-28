/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.plantillapractica;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author usuario
 */
@Entity
public class Persona implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String nombre;
    @Embedded // Esta anotación no es necesaria
    private Direccion direccion;

    public Persona() {
    }

    public Persona(String nombre, Direccion direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", nombre=" + nombre + ", direccion=" + direccion + '}';
    }
    
}

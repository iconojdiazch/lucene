/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.practicaposiblesolucion;

import java.io.Serializable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 *
 * @author usuario
 */
@Entity
@Indexed
public class Persona implements Serializable {

    @Id
    @GeneratedValue
    @DocumentId
    private Long id;
    @Field
    private String nombre;

    @IndexedEmbedded
    @Embedded //Esta anotación no es necesaria
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

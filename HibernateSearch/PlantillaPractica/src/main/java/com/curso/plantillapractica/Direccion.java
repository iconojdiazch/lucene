/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.plantillapractica;

import javax.persistence.Embeddable;

/**
 *
 * @author usuario
 */
@Embeddable
public class Direccion {
    private String calle;

    public Direccion() {
    }

    public Direccion(String nombre) {
        this.calle = nombre;
    }

    @Override
    public String toString() {
        return "Direccion{" + "calle=" + calle + '}';
    }
    
}

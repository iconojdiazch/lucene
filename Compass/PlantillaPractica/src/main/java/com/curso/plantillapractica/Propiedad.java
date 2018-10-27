/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.plantillapractica;

/**
 *
 * @author usuario
 */
// Incluir las anotaciones cd Compass correspondientes
public class Propiedad {   
    private Long id;    
    private String clave;    
    private String valor;

    public Propiedad() {
    }

    public Propiedad(Long id, String clave, String valor) {
        this.id = id;
        this.clave = clave;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Propiedad{" + "id=" + id + ", clave=" + clave + ", valor=" + valor + '}';
    }
    
}

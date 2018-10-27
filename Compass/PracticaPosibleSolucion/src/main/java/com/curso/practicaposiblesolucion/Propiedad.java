/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.practicaposiblesolucion;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;
import org.compass.annotations.SearchableProperty;

/**
 *
 * @author usuario
 */
@Searchable
public class Propiedad {
    @SearchableId
    private Long id;
    @SearchableProperty
    private String clave;
    @SearchableProperty
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

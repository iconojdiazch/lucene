/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.practicaposiblesolucion;

import javax.persistence.Embeddable;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

/**
 *
 * @author usuario
 */
@Embeddable
public class Direccion {

    @Field(store = Store.YES) // Necesario si queremos usarlo en, por ejemplo, proyecciones
    private String calle;

    public Direccion() {
    }

    public Direccion(String calle) {
        this.calle = calle;
    }

    @Override
    public String toString() {
        return "Direccion{" + "calle=" + calle + '}';
    }

}

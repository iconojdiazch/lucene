/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploLucene0001;

/**
 *
 * @author usuario
 */
public interface Constantes {

    String[][] librosEnInglés = {
        {"Lucene in Action", "193398817"},
        {"Lucene for Dummies", "55320055Z"},
        {"Managing Gigabytes", "55063554A"},
        {"The Art of Computer Science", "9900333X"}
    };
    String[][] librosEnCastellano = {
        {"Lucene en Acción", "193398817"},
        {"Lucene para principiantes", "55320055Z"},
        {"Gestión de Gigabytes", "55063554A"},
        {"El arte de la computación", "9900333X"}
    };

    public enum Idiomas {
        CASTELLANO, INGLÉS
    }
}

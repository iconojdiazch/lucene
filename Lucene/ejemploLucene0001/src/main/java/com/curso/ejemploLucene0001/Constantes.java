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

    String[][] librosEnIngl�s = {
        {"Lucene in Action", "193398817"},
        {"Lucene for Dummies", "55320055Z"},
        {"Managing Gigabytes", "55063554A"},
        {"The Art of Computer Science", "9900333X"}
    };
    String[][] librosEnCastellano = {
        {"Lucene en Acci�n", "193398817"},
        {"Lucene para principiantes", "55320055Z"},
        {"Gesti�n de Gigabytes", "55063554A"},
        {"El arte de la computaci�n", "9900333X"}
    };

    public enum Idiomas {
        CASTELLANO, INGL�S
    }
}

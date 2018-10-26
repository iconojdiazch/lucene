/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploLucene0001;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author usuario
 */
@Data
@AllArgsConstructor
public class Libro {
    private String titulo;
    private String isbn;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploLucene0001;

import java.io.IOException;
import static java.util.Arrays.stream;
import java.util.stream.Stream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author usuario
 */
public class Util implements Constantes {

    public Stream<Libro> crearLibros(Idiomas idioma) {
        String[][] libros = idioma == Idiomas.CASTELLANO ? librosEnCastellano : librosEnInglés;
        return stream(libros).map(s -> new Libro(s[0], s[1]));
    }
   
    public void nuevoDocumento(IndexWriter w, Libro libro) {
        try {
            Document doc = new Document();
            doc.add(new TextField("titulo", libro.getTitulo(), Field.Store.YES));
            doc.add(new StringField("isbn", libro.getIsbn(), Field.Store.YES));
            w.addDocument(doc);
        } catch (IOException ex) {
            throw new RuntimeException("Error en nuevoDocumento", ex);
        }
    }
}

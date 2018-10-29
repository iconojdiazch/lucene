/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.plantillapractica;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.ScoreDoc;

/**
 *
 * @author usuario
 */
public class Principal {

    private static final Logger LOG = Logger.getLogger(Principal.class.getName());
    private static final String LUCENEINDEX = "lucene-index";
    private static final String TEXTOS = "textos";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        FileUtils.deleteDirectory(new File(LUCENEINDEX));
        Principal p = new Principal();
        try (IndexWriter w = p.crearIndice()) {
            p.indexarArchivos(TEXTOS, w);
        }
        ScoreDoc[] docs = p.realizarBusqueda("Acosta");
//        ScoreDoc[] docs = p.realizarBusqueda("Acosta~");
        LOG.log(Level.INFO, "Encontradas {0} sugerencias.", docs.length);
        p.mostarDocs(docs);
        LOG.info("Fin");
    }

    private IndexWriter crearIndice() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void indexarArchivos(String textos, IndexWriter w) throws IOException {
        File[] lista = new File(textos).listFiles();
        for (File f : lista) {
            String contenido = FileUtils.readFileToString(f, "UTF-8");
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ScoreDoc[] realizarBusqueda(String termino) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void mostarDocs(ScoreDoc[] docs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

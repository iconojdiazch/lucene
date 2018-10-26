/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.practicaposiblesolucion;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

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
     */
    public static void main(String[] args) throws IOException, ParseException {
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

    private IndexWriter crearIndice() throws IOException {
        SpanishAnalyzer analizador = new SpanishAnalyzer();
        FSDirectory index = FSDirectory.open(new File(LUCENEINDEX).toPath());
        IndexWriterConfig config = new IndexWriterConfig(analizador);
        return new IndexWriter(index, config);
    }

    private Document nuevoDocumento(String contenido, String nombreArchivo) {
        Document doc = new Document();
        doc.add(new StringField("nombre", nombreArchivo, Field.Store.YES));
        doc.add(new TextField("cuerpo", contenido, Field.Store.NO));
        return doc;
    }

    private IndexWriter indexarArchivos(String textos, IndexWriter w) throws IOException {
        File[] lista = new File(textos).listFiles();
        for (File f : lista) {
            String contenido = FileUtils.readFileToString(f, "UTF-8");
            Document doc = nuevoDocumento(contenido, f.getPath());
            w.addDocument(doc);
        }
        return w;
    }

    private ScoreDoc[] realizarBusqueda(String termino) throws ParseException, IOException {        
        Query q = new QueryParser("cuerpo", new SpanishAnalyzer()).parse(termino);
        TopDocs documentos;
        try (DirectoryReader lector = DirectoryReader.open(FSDirectory.open(new File("lucene-index").toPath()))) {
            IndexSearcher buscador = new IndexSearcher(lector);
            documentos = buscador.search(q, 10);
        }
        return documentos.scoreDocs;
    }

    private void mostarDocs(ScoreDoc[] docs) throws IOException {
        try (DirectoryReader lector = DirectoryReader.open(FSDirectory.open(new File("lucene-index").toPath()))) {
            IndexSearcher buscador = new IndexSearcher(lector);
            for (ScoreDoc s : docs) {
                Document d = buscador.doc(s.doc);
                LOG.info(String.format("El texto se encuentra en el archivo: %s.%n", d.get("nombre")));
            }
        }
    }
}

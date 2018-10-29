package com.curso.ejemploLucene0002;

import java.io.File;
import java.io.FileInputStream;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Principal {

    private static final Logger LOG = Logger.getLogger(Principal.class.getName());
    private static final String LUCENEINDEX = "lucene-index";

    public static void main(String[] args) throws IOException, ParseException {
        SpanishAnalyzer analizador = new SpanishAnalyzer();

        FileUtils.deleteDirectory(new File(LUCENEINDEX));
        FSDirectory index = FSDirectory.open(new File(LUCENEINDEX).toPath());

        IndexWriterConfig config = new IndexWriterConfig(analizador);
        Principal p = new Principal();
        try (IndexWriter w = new IndexWriter(index, config)) {
            File[] lista = new File("pdfs").listFiles();
            for (File f : lista) {
                String contenido;
                PDDocument doc;
                System.out.println("Indexando: " + f.getPath());
                doc = PDDocument.load(f);
                PDFTextStripper stripper = new PDFTextStripper();
                stripper.setLineSeparator("\n");
                stripper.setStartPage(1);
                stripper.setEndPage(5);
                contenido = stripper.getText(doc);
                doc.close();
                p.nuevoDocumento(w, contenido, f.getPath());
            }
            LectorHtml lectorHtml = new LectorHtml();
            lista = new File("htmls").listFiles();
            for (File f : lista) {
                System.out.println("Indexando: " + f.getPath());
                try (InputStream stream = new FileInputStream(f)) {
                    Document doc = lectorHtml.obtenerDocumento(stream,f.getPath());
                    w.addDocument(doc);
                }
            }
        }
        String textoConsulta = "ejemplo";
        Query q = new QueryParser("cuerpo", analizador).parse(textoConsulta);
        Integer resultadosPorPagina = 10;

        DirectoryReader lector = DirectoryReader.open(index);
        IndexSearcher buscador = new IndexSearcher(lector);
        TopDocs documentos = buscador.search(q, resultadosPorPagina);

        ScoreDoc[] sugerencias = documentos.scoreDocs;
        LOG.log(Level.INFO, "Encontradas {0} sugerencias.", sugerencias.length);
        for (ScoreDoc s : sugerencias) {
            Document doc = buscador.doc(s.doc);
            LOG.info(String.format("El texto se encuentra en el archivo: %s.%n", doc.get("nombre")));
        }
        LOG.info("Fin");
    }

    public void nuevoDocumento(IndexWriter w, String contenido, String nombreArchivo) {
        try {
            Document doc = new Document();
            doc.add(new StringField("nombre", nombreArchivo, Field.Store.YES));
            doc.add(new TextField("cuerpo", contenido, Field.Store.NO));
            w.addDocument(doc);
        } catch (IOException ex) {
            throw new RuntimeException("Error en nuevoDocumento", ex);
        }
    }
}

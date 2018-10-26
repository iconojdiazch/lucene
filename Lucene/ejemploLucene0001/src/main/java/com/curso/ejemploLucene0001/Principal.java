package com.curso.ejemploLucene0001;

import com.curso.ejemploLucene0001.Constantes.Idiomas;
import io.vavr.control.Try;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;

import java.io.IOException;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import java.util.logging.Logger;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

public class Principal {

    private static final Logger LOG = Logger.getLogger(Principal.class.getName());

    public static void main(String[] args) throws IOException, ParseException {
        Principal p = new Principal();
        //Paso 1: Indexar
        StandardAnalyzer analizador = new StandardAnalyzer();
//        SpanishAnalyzer analizador = new SpanishAnalyzer();

        RAMDirectory index = new RAMDirectory();
//        FileUtils.deleteDirectory(new File("lucene-index"));
//        FSDirectory index = FSDirectory.open(new File("lucene-index").toPath());
        IndexWriterConfig config = new IndexWriterConfig(analizador);
        //Paso 2: Añadir documentos
        Util util = new Util();
        try (IndexWriter w = new IndexWriter(index, config)) {
            util.crearLibros(Idiomas.INGLÉS).forEach(
                    libro -> util.nuevoDocumento(w, libro)
            );
        }
        //Paso 3: Crear consultas
        String textoConsulta = "lucene";
//        String textoConsulta = "acción";
        Query q = new QueryParser("titulo", analizador).parse(textoConsulta);
        //Paso 4: Buscar
        Integer resultadosPorPagina = 10;
        DirectoryReader lector = DirectoryReader.open(index);
        IndexSearcher buscador = new IndexSearcher(lector);
        TopDocs docs = buscador.search(q, resultadosPorPagina);
        ScoreDoc[] sugerencias = docs.scoreDocs;
        //Paso 5: Mostrar resultados
        LOG.info(String.format("Encontradas %d sugerencias.%n", sugerencias.length));
        stream(sugerencias).map(
                d -> Try.of(() -> buscador.doc(d.doc))
        ).flatMap(
                t -> Stream.of(t.getOrElse(Document::new))
        ).forEach(
                d -> LOG.info(String.format("Isbn: %s. Título: %s%n", d.get("isbn"), d.get("titulo")))
        );
    }
}

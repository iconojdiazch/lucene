package com.curso.ejemploLucene0001;

import io.vavr.control.Try;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class Principal {

    private static final Logger LOG = Logger.getLogger(Principal.class.getName());

    public static void main(String[] args) {
        try {
            Principal p = new Principal();
            //Paso 1: Index
            StandardAnalyzer analyzer = new StandardAnalyzer();
            RAMDirectory index = new RAMDirectory();
//            FSDirectory index = FSDirectory.open(new File("lucene-index").toPath());
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            
            IndexWriter w = new IndexWriter(index, config);
            p.addDoc(w, "Lucene in Action", "193398817");
            p.addDoc(w, "Lucene for Dummies", "55320055Z");
            p.addDoc(w, "Managing Gigabytes", "55063554A");
            p.addDoc(w, "The Art of Computer Science", "9900333X");
            w.close();
            //Paso 2: Query
            String queryStr = "lucene";
            Query q = new QueryParser("title", analyzer).parse(queryStr);
            //Paso 3: Search
            Integer hitsPerPage = 10;
            DirectoryReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs docs = searcher.search(q, hitsPerPage);
            ScoreDoc[] hits = docs.scoreDocs;
            //Paso 4: Display
            LOG.info(String.format("Found %d hits.%n", hits.length));
            stream(hits).map(
                    d -> Try.of(() -> searcher.doc(d.doc))
            ).flatMap(
                    t -> Stream.of(t.getOrElse(Document::new))
            ).forEach(
                    d -> LOG.info(String.format("Isbn: %s. Title: %s%n", d.get("isbn"), d.get("title")))
            );
        } catch (IOException | ParseException ex) {
            LOG.log(Level.SEVERE, "Ha ocurrido un error", ex);
        }
    }

    private void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
}

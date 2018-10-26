/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.ejemploLucene0002;

import java.io.InputStream;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

/**
 *
 * @author usuario
 */
public class LectorHtml {

    public org.apache.lucene.document.Document obtenerDocumento(InputStream is, String archivo) {
        Tidy tidy = new Tidy();
        tidy.setQuiet(true);
        tidy.setShowWarnings(false);
        org.w3c.dom.Document root = tidy.parseDOM(is, null);
        Element elemento = root.getDocumentElement();

        org.apache.lucene.document.Document doc
                = new org.apache.lucene.document.Document();

        String cuerpo = obtenerCuerpo(elemento);

        if ((cuerpo != null) && (!cuerpo.equals(""))) {
            doc.add(new TextField("cuerpo", cuerpo, Field.Store.NO));
            doc.add(new StringField("nombre", archivo, Field.Store.YES));
        }

        return doc;
    }

    protected String obtenerTitulo(Element elemento) {
        if (elemento == null) {
            return null;
        }

        String titulo = "";

        NodeList children = elemento.getElementsByTagName("title");
        if (children.getLength() > 0) {
            Element titleElement = ((Element) children.item(0));
            Text text = (Text) titleElement.getFirstChild();
            if (text != null) {
                titulo = text.getData();
            }
        }
        return titulo;
    }

    protected String obtenerCuerpo(Element elemento) {
        if (elemento == null) {
            return null;
        }

        String cuerpo = "";
        NodeList descendientes = elemento.getElementsByTagName("body");
        if (descendientes.getLength() > 0) {
            cuerpo = obtenerTexto(descendientes.item(0));
        }
        return cuerpo;
    }

    protected String obtenerTexto(Node node) {
        NodeList descendientes = node.getChildNodes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < descendientes.getLength(); i++) {
            Node descendiente = descendientes.item(i);
            switch (descendiente.getNodeType()) {
                case Node.ELEMENT_NODE:
                    sb.append(obtenerTexto(descendiente));
                    sb.append(" ");
                    break;
                case Node.TEXT_NODE:
                    sb.append(((Text) descendiente).getData());
                    break;
            }
        }
        return sb.toString();
    }
}

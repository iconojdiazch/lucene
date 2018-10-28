/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.hibernatesearch0002;

import com.curso.hibernatesearch0002.util.LogUtil;
import com.curso.hibernatesearch0002.util.HibernateUtil;
import com.curso.hibernatesearch0002.util.SearchUtil;
import java.util.Optional;
import org.hibernate.SessionFactory;

/**
 *
 * @author usuario
 */
public class Principal implements LogUtil {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Optional<SessionFactory> sf = HibernateUtil.getSessionFactory();
        SearchUtil.crearIndice(sf);
        HibernateUtil.crearObjetosPersistentes(sf);
        HibernateUtil.mostrarTodosLosObjetos(sf);
        final String consulta = "title:El Quijote name:Saavedra";
        final String[] campos = {"title", "subtitle", "authors.name"};
        SearchUtil.buscarSinQueryBuilber(sf, consulta, campos);
        SearchUtil.buscarConQueryBuilder(sf, consulta);
        HibernateUtil.cerrar();        
    }
}

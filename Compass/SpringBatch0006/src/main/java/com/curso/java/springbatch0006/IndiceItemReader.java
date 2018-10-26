/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.java.springbatch0006;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassException;
import org.compass.core.CompassHit;
import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;
import org.compass.spring.CompassDaoSupport;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class IndiceItemReader extends CompassDaoSupport implements ItemReader {

    private static final Logger LOG = Logger.getLogger(IndiceItemReader.class.getName());

    private int ejecucion = 0;

    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException {
        /**
         * Esto es solo para que se ejecute una vez
         */
        if (ejecucion == 0) {
            ejecucion++;
            /**
             * Buscamos todos los planetas de tipo gaseoso y excluimos Martes
             * !!!!
             */
            LOG.log(Level.INFO,"Buscamos todos los planetas de tipo gaseoso y excluimos Martes");
            return buscarPaginado("nombre:* AND tipo:gaseoso AND -martes");
        }
        return null;
    }

    /**
     * Hace una busqueda paginada en el indice, En caso de que el query para la
     * busqueda este vacio devuelve una collection vacia.
     *
     * @param queryIndice queryIndice
     * @return List
     */
    private List<Planeta> buscarPaginado(String queryIndice) {
        CompassDetachedHits results;
        final String query = queryIndice;
        final int from = 0;
        final int size = 100;
        List<Planeta> planeta = new ArrayList<>();

        try {
            // Esto se hace para poder buscar usando un OR
            results = getCompassTemplate().execute((final CompassSession session) -> {
                CompassQuery compassQuery = session.queryBuilder().queryString(query).useOrDefaultOperator().toQuery();
                CompassHits activeHits = compassQuery.hits();
                CompassDetachedHits detachedHits = null;
                // Work around a compass bug.
                if (activeHits.getLength() > (from + size)) {
                    detachedHits = activeHits.detach(from, size);
                } else if (activeHits.getLength() > from) {
                    detachedHits = activeHits.detach(from, (activeHits.getLength() - from));
                }
                return detachedHits;
            });

            if (results != null) {
                for (CompassHit hit : results.getHits()) {
                    planeta.add((Planeta) hit.getData());
                }
            }
        } catch (CompassException e) {
            LOG.log(Level.SEVERE, e.toString());
        }
        return planeta;
    }
}

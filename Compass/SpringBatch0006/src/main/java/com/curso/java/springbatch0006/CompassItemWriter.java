/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.java.springbatch0006;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.compass.core.CompassCallback;
import org.compass.core.CompassException;
import org.compass.core.CompassSession;
import org.compass.spring.CompassDaoSupport;
import org.springframework.batch.item.ItemWriter;

public class CompassItemWriter extends CompassDaoSupport implements ItemWriter<Planeta> {

    private static final Logger LOG = Logger.getLogger(CompassItemWriter.class.getName());

    @Override
    public void write(List<? extends Planeta> item) throws Exception {

        for (final Planeta planeta : item) {
            getCompassTemplate().execute((CompassSession session) -> {
                try {
                    session.save(planeta);
                    LOG.log(Level.INFO, "Guardando Planeta : {0}", planeta);
                } catch (CompassException e) {
                    LOG.log(Level.SEVERE, e.toString());
                }
                return null;
            });
        }
    }
}

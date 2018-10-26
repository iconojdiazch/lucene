/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.curso.java.springbatch0006;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.batch.item.ItemWriter;

public class ConsolaItemWriter implements ItemWriter<Planeta> {
    
    private static final Logger LOG = Logger.getLogger(ConsolaItemWriter.class.getName());
    
    @Override
    public void write(List<? extends Planeta> item) throws Exception {
        try {
            Collection<Planeta> col = (Collection<Planeta>) item.iterator().next();
            col.forEach(planeta -> System.out.println("Planeta leido: " + planeta));            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.toString());            
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.beans;

/**
 *
 * @author miguel
 */
import java.util.Map;
import java.util.HashMap;
import jmc.exception.JMCException;

public abstract class JmcScraper {
    
    
    private String host;
           
    public abstract Contenido contenido(Enlace en) throws JMCException;
    
    protected Map<String,String> meses() {
        Map<String,String> meses = new HashMap();
        
        meses.put("ENERO","01");
        meses.put("FEBRERO","02");
        meses.put("MARZO","03");
        meses.put("ABRIL","04");
        meses.put("MAYO","05");
        meses.put("JUNIO","06");
        meses.put("JULIO","07");
        meses.put("AGOSTO","08");
        meses.put("SEPTIEMBRE","09");
        meses.put("OCTUBRE","10");
        meses.put("NOVIEMBRE","11");
        meses.put("DICIEMBRE","12");
                
        
     return meses;   
    }
    
    protected Map<String,String> mes() {
        Map<String,String> meses = new HashMap();
        
        meses.put("ENE","01");
        meses.put("FEB","02");
        meses.put("MAR","03");
        meses.put("ABR","04");
        meses.put("MAY","05");
        meses.put("JUN","06");
        meses.put("JUL","07");
        meses.put("AGO","08");
        meses.put("SEP","09");
        meses.put("OCT","10");
        meses.put("NOV","11");
        meses.put("DIC","12");
                
        
     return meses;   
    }
    
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
}

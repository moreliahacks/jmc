/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.proc;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import jmc.beans.Contenido;
import jmc.beans.Enlace;
import jmc.exception.JMCException;
import jmc.beans.JmcScraper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 *
 * @author miguel
 */
public class JmcDiaVisScrap extends JmcScraper {
                
    public final String HOST = "http://www.eldiariovision.com.mx";
    
    public JmcDiaVisScrap() {
        super.setHost(HOST);
    }
    
    
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();
        

        try {
            System.out.println("-> url: " + enl.getUrl());
            
            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");
            
            
            if (doc != null && doc.getElementById("Contenido") != null && doc.getElementById("Contenido").hasText() && !doc.getElementsByClass("tituloNota").isEmpty()) {            
                c.setEnlace(enl);                
                c.setTitulo(doc.getElementsByClass("tituloNota").get(0).text().trim());
                
                System.out.println("titulo: "+doc.getElementsByClass("tituloNota").get(0).text().trim());
                
                c.setContenido(doc.getElementById("Contenido").text());
                System.out.println("Contenido: "+doc.getElementById("Contenido").text().trim());
                
                c.setAutor(doc.getElementsByClass("tituloLink").text());
                System.out.println("autor: "+doc.getElementsByClass("tituloLink").text().trim());
                
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String fec = null; 
                                
                String[] fe = doc.getElementsByClass("tituloNota").get(1).text().split(" ");
                fec = fe[2]+" "+fe[0];
                System.out.println("Fecha: "+fec);
                c.setFecha(df.parse(fec));    
                c.setSitio(7l);
            }
            
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }
      return c;  
    }    
    
}

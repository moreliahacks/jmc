/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.proc;

/**
 *
 * @author miguel
 */
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import jmc.beans.JmcScraper;
import jmc.beans.Contenido;
import jmc.beans.Enlace;
import jmc.exception.JMCException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JmcLAVOZScrap extends JmcScraper {
    
    public final String HOST = "http://www.lavozdemichoacan.com.mx";
    
    public JmcLAVOZScrap() {
        super.setHost(HOST);
    }
    
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();

        try {
            System.out.println("-> url: " + enl.getUrl());
            
            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");
            
            if (!doc.getElementsByClass("content-nota").isEmpty() && !doc.getElementsByClass("content-title").isEmpty() && !doc.getElementsByClass("content-fecha").isEmpty()) {            
                c.setEnlace(enl);
                c.setTitulo(doc.getElementsByClass("content-title").text().trim());
                System.out.println("Titulo: "+doc.getElementsByClass("content-title").text().trim());
                c.setContenido(doc.getElementsByClass("content-nota").text().trim());
                System.out.println("Nota: "+doc.getElementsByClass("content-nota").text().trim());
                c.setAutor(doc.getElementsByClass("content-author").text());
                System.out.println("Autor: "+doc.getElementsByClass("content-author").text().trim());
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String fec = null; 
                                
                String[] fe = doc.getElementsByClass("content-fecha").text().split(" ");
                fec = fe[0]+"-"+this.meses().get(fe[2].replaceAll(",", "").toUpperCase().trim())+"-"+fe[3].trim();
                System.out.println("Fecha:"+fec);
                c.setFecha(df.parse(fec));         
                c.setSitio(9l);
            }
            
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }
      return c;  
    }    
    
    
}

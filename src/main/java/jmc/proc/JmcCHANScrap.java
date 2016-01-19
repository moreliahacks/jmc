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


public class JmcCHANScrap extends JmcScraper {
    
    public final String HOST = "http://www.changoonga.com";
    
    public JmcCHANScrap() {
        super.setHost(HOST);
    }
    
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();
        

        try {
            System.out.println("-> url: " + enl.getUrl());
            
            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");
            
            if (!doc.getElementsByTag("article").isEmpty() && !doc.getElementsByClass("entry-title").isEmpty() && !doc.getElementsByClass("entry-content").isEmpty()) {            
                c.setEnlace(enl);
                c.setTitulo(doc.getElementsByClass("entry-title").text().trim());
                c.setContenido(doc.getElementsByClass("entry-content").text().trim());
                c.setAutor("STAFF");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String fec = null; 
                
                
                String[] fe = doc.getElementsByAttributeValue("itemprop", "datePublished").attr("datetime").split("T");
                fec = fe[0]+" "+fe[1];
                c.setFecha(df.parse(fec));  
                c.setSitio(3l);
            }
            
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }

        return c;    
    } 
    
}

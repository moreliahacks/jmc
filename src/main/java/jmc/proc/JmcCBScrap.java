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


public class JmcCBScrap extends JmcScraper {

    public final String HOST = "https://cbtelevision.com.mx";
    
    
    public JmcCBScrap() {
        super.setHost(HOST);
    }
    
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();
        
        try {
            System.out.println("-> url: " + enl.getUrl());

            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");

            if (!doc.getElementsByTag("article").isEmpty() && !doc.getElementsByClass("entry-title").isEmpty() && !doc.getElementsByClass("entry").isEmpty() && !doc.getElementsByClass("post-meta-author").isEmpty()) {
                c.setEnlace(enl);
                c.setTitulo(doc.getElementsByClass("entry-title").text().trim());
                
                System.out.println("Titulo: "+doc.getElementsByClass("entry-title").text().trim());
                
                c.setContenido(doc.getElementsByClass("entry").text().trim());
                 
                System.out.println("Texto: "+doc.getElementsByClass("entry").text().trim());
                
                String fec = doc.getElementsByClass("updated").text().trim();
                String autor = doc.getElementsByClass("post-meta-author").text().trim();
                                
                System.out.println("Autor: "+autor+" fec: "+fec);
                                                                
                c.setAutor(autor);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                c.setFecha(df.parse(fec));
                c.setSitio(2l);
            }

        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }
        
        return c;
    }

}

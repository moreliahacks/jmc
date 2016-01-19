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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Iterator;
import jmc.beans.JmcScraper;
import jmc.beans.Contenido;
import jmc.beans.Enlace;
import jmc.exception.JMCException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class JmcQUAScrap extends JmcScraper {

    public final String HOST = "https://www.quadratin.com.mx"; 
    
    
    public JmcQUAScrap() {
        super.setHost(HOST);
    }
    
    
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {

        Contenido c = new Contenido();                

        try {
            System.out.println("-> url: " + enl.getUrl());
            
            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl().trim()), "utf-8");
            if (doc.getElementById("content_note") != null && doc.getElementsByClass("fecha-impres").hasText()) {
                c.setEnlace(enl);
                
                String titulo = doc.getElementsByClass("title-impres").text().trim();               
                c.setTitulo(titulo.trim());
                
                String autor = doc.getElementById("published-by").text().trim();
                c.setAutor(autor.trim());
                
                String[] ft = doc.getElementById("content_note").text().trim().split(".-");
                
                c.setContenido(ft.length == 2 ? ft[1] : ft[0]);
                                
                String[] fe = null;
                
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                
              //  System.out.println("meta: "+doc.getElementsByTag("meta").attr("property","article:published_time").attr("content"));
                Iterator<Element> it = doc.getElementsByTag("meta").iterator();
                
                while (it.hasNext()) {                    
                  Element e = it.next();
                  if (e.attributes().get("property").equals("article:published_time")) {  
                    fe = e.attributes().get("content").split("T");
                    System.out.println("content: "+e.attributes().get("content"));
                   break; 
                  }                    
                }
                
                
                //fe = doc.getElementsByClass("fecha-impres").text().trim().split("T");
                //String[] fc = fe[0].split("/");
                //String mes = meses().get(fc[1].toUpperCase().trim());
                
                //c.setFecha(df.parse(fc[0]+"/"+mes+"/"+fc[2]+" "+fe[1]));
                c.setFecha(df.parse(fe[0]+" "+fe[1]));
                c.setSitio(4l);
            }
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            //throw new JMCException(e);
        }

        return c;

    }

}

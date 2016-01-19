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



public class JmcIndepScrap extends JmcScraper {
    
    public final String HOST = "http://www.el-independiente.com.mx";

    
    public JmcIndepScrap() {
        super.setHost(HOST);
    }
    
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();
        

        try {
            System.out.println("-> url: " + enl.getUrl());
            
            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");
            
            if (!doc.getElementsByClass("entry-title").isEmpty() && !doc.getElementsByClass("entry-content").isEmpty()) {            
                c.setEnlace(enl);
                c.setTitulo(doc.getElementsByClass("entry-title").text().trim());
                System.out.println(c.getTitulo());
                c.setContenido(doc.getElementsByClass("entry-content").text().trim());
                System.out.println(c.getContenido());
                c.setAutor("EL-INDEPENDIENTE");
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String fec = null; 
                
               // while (doc.getElementsByAttributeValue("style", "text-transform:capitalize;").iterator().hasNext()) {
                    System.out.println(doc.getElementsByAttributeValue("style", "margin:10px 0 10px 0; color:#bf2217;").text());
                    
                    String[] fe = doc.getElementsByAttributeValue("style", "margin:10px 0 10px 0; color:#bf2217;").text().trim().split(" ");
                    fec = fe[1]+"/"+super.meses().get(fe[3].toUpperCase())+"/"+fe[5];
                    System.out.println(fec);
                    
               // }
                
               // String[] fe = doc.getElementsByAttributeValue("itemprop", "datePublished").attr("datetime").split("T");
                //   System.out.println("itemprop: "+doc.getElementsByAttributeValue("itemprop", "datePublished").attr("datetime").split("T"));
               // fec = fe[0]+" "+fe[1];
               c.setFecha(df.parse(fec));      
               c.setSitio(8l);
            }
            
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }

        return c;    
    } 
    
    
}

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

public class JmcLaOpScrap extends JmcScraper  {
    
    
    public final String HOST = "http://laopiniondemichoacan.com.mx";
    public final String HOST_SCRAP = "http://laopiniondemichoacan.com.mx/noticias/?s=MORELIA&paged=$i"; 
    
    public JmcLaOpScrap() {
        super.setHost(HOST);
    }
    
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();
        

        try {
            System.out.println("-> url: " + enl.getUrl());
            
            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");
            
            if (!doc.getElementsByTag("article").isEmpty() && !doc.getElementsByClass("entry-title").isEmpty() && !doc.getElementsByClass("entry_content").isEmpty()) {            
                c.setEnlace(enl);
                c.setTitulo(doc.getElementsByClass("entry-title").text().trim());
                System.out.println(c.getTitulo());
                c.setContenido(doc.getElementsByClass("entry_content").text().trim());
                System.out.println(c.getContenido());
                c.setAutor("LOM");
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String fec = null; 
                
                
                String[] fe = doc.getElementsByClass("meta_date").text().trim().split(" - ");
                fec = fe[0].trim()+" "+fe[1].trim();
                System.out.println(fec);
                c.setFecha(df.parse(fec));                
                
            }
            
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }

        return c;    
    } 
    
}

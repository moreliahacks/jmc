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
import jmc.beans.JmcScraper;
import jmc.beans.Contenido;
import jmc.beans.Enlace;
import jmc.exception.JMCException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JmcExtraScrap extends JmcScraper {
    
    public final String HOST = "http://laextra.mx";
    
    
    public JmcExtraScrap() {
        super.setHost(HOST);    
    }
    
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();
        

        try {
            System.out.println("-> url: " + enl.getUrl());
            
            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");
            
            if (!doc.getElementsByClass("post").isEmpty() && !doc.getElementsByTag("h1").isEmpty()) {            
               
                c.setEnlace(enl);
                c.setTitulo(doc.getElementsByTag("h1").text().trim());
                System.out.println(c.getTitulo());
                c.setContenido(doc.getElementsByClass("post").text().trim());
                System.out.println(c.getContenido());
                c.setAutor("LA-EXTRA");
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String fec = null; 
                
                String[] fe = doc.getElementById("datemeta_l").text().split(" ");
                
                for (int i = 0; i < fe.length; i++) { 
                 System.out.println(i+")"+fe[i]);
                }
                String dia = fe[4].replace("th,", "").replace("rd,", "").replace("st,", "").replace("nd,", "");
                dia = dia.length() == 1 ? "0"+dia : dia;
                String mess = meses().get(fe[3].toUpperCase());
                mess = mess == null ? mes().get(fe[3].toUpperCase()) : mess;
                
                if (mess != null) {
                  fec = dia+"/"+mess+"/"+fe[5];                
                  System.out.println("fec: "+fec);
                  c.setFecha(df.parse(fec));
               } 
                
                c.setSitio(6l);
            }
            
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }

        return c;    
    } 
    
}

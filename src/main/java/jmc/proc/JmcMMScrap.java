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

public final class JmcMMScrap extends JmcScraper {

    public String HOST = "http://www.mimorelia.com";
    
    public JmcMMScrap() {
        super.setHost(HOST);
    }

    @Override
    public Contenido contenido(Enlace enl) throws JMCException {

        Contenido c = new Contenido();
        
        try {            
                System.out.println("-> url: " + enl.getUrl());                                               
                    Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");
                    if (!doc.getElementsByClass("noteBody").isEmpty()) {
                        c.setEnlace(enl);
                        c.setTitulo(doc.getElementsByClass("newsTitle").text().trim());
                        String texto = doc.getElementsByClass("noteBody").text().trim();
                        String[] sf = doc.getElementsByClass("time").text().trim().split("-");

                        String[] fe = sf[1].trim().split(" de ");
                        String[] fh = sf[2].trim().split(" ");

                        String fec = fe[0] + "/" + (meses().get(fe[1].toUpperCase())) + "/" + fe[2] + " " + fh[0];
                        System.out.println("fec: " + fec);
                        c.setContenido(texto.trim());
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        c.setFecha(df.parse(fec));
                        String autor = sf[0].trim();

                        c.setAutor(autor);               
                        c.setSitio(1l);
                }
            
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }

        return c;
    }

}

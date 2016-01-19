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

public class JmcABCScrap extends JmcScraper {

    public final String HOST = "http://diarioabc.mx";
    

    public JmcABCScrap() {
        super.setHost(HOST);        
    }

    @Override
    public Contenido contenido(Enlace enl) throws JMCException {

        Contenido c = new Contenido();

        try {
            System.out.println("-> url: " + enl.getUrl());

            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");

            if (!enl.getUrl().contains("/#comments-button")) {
                if (doc.getElementById("content-area") != null && doc.getElementById("content-area").hasText() && !doc.getElementsByClass("story-title").isEmpty()) {
                    c.setEnlace(enl);
                    c.setTitulo(doc.getElementsByClass("story-title").text().trim());

                    System.out.println("Titulo: " + doc.getElementsByClass("story-title").text().trim());

                    c.setContenido(doc.getElementById("content-area").text().trim());

                    System.out.println("Texto: " + doc.getElementById("content-area").text().trim());

                    doc.getElementsByTag("time").get(0).attr("datetime");

                    String fec = doc.getElementsByTag("time").get(0).attr("datetime").trim();
                    String autor = doc.getElementsByClass("author-name").get(0).text().trim();

                    System.out.println("Autor: " + autor + " fec: " + fec);

                    c.setAutor(autor);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    c.setFecha(df.parse(fec));
                    c.setSitio(5l);
                }
            }
        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }

        return c;
    }

}

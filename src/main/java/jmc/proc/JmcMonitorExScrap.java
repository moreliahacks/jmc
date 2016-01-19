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
import java.util.Date;
import java.util.Iterator;
import jmc.beans.JmcScraper;
import jmc.beans.Contenido;
import jmc.beans.Enlace;
import jmc.exception.JMCException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class JmcMonitorExScrap extends JmcScraper {

    public final String HOST = "http://www.monitorexpresso.com";

    public JmcMonitorExScrap() {
        super.setHost(HOST);
    }

    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();

        try {
            System.out.println("-> url: " + enl.getUrl());

            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");

            if (!doc.getElementsByClass("entry").isEmpty() && !doc.getElementsByTag("h2").isEmpty()) {

                c.setEnlace(enl);
                c.setTitulo(doc.getElementsByTag("h2").get(0).text().trim());
                System.out.println(c.getTitulo());
                c.setContenido(doc.getElementsByClass("entry").text().trim());
                System.out.println(c.getContenido());

                String fec = null;

                String[] ha = null;

                ha = c.getContenido().split(".-");

                System.out.println(ha[0].split(", ")[1]);
                String hth = ha[0].split(", ")[0].replace("Share this on WhatsApp ", "");
                String[] htd = ha[0].split(", ")[1].split(" ");

                fec = htd[2] + "/" + meses().get(htd[4].toUpperCase()) + "/" + htd[6];
                System.out.println(fec);
                c.setAutor(hth);
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                //  System.out.println("meta: "+doc.getElementsByTag("meta").attr("property","article:published_time").attr("content"));
                Iterator<Element> it = doc.getElementsByTag("meta").iterator();
                String fe[] = null;
                while (it.hasNext()) {
                    Element e = it.next();
                    if (e.attributes().get("property").equals("article:published_time")) {
                        fe = e.attributes().get("content").split("T");
                        System.out.println("content: " + e.attributes().get("content"));
                        break;
                    }
                }

                //fe = doc.getElementsByClass("fecha-impres").text().trim().split("T");
                //String[] fc = fe[0].split("/");
                //String mes = meses().get(fc[1].toUpperCase().trim());
                //c.setFecha(df.parse(fc[0]+"/"+mes+"/"+fc[2]+" "+fe[1]));
                c.setFecha(df.parse(fe[0] + " " + fe[1]));
                c.setSitio(10l);

            }

        } catch (IOException e) {
            throw new JMCException(e);
        } catch (ParseException e) {
            throw new JMCException(e);
        }

        return c;
    }

}

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



public class JmcProvScrap extends JmcScraper {

    public final String HOST = "http://www.provincia.com.mx";
    
    public JmcProvScrap() {
        super.setHost(HOST);
    }
        
    @Override
    public Contenido contenido(Enlace enl) throws JMCException {
        Contenido c = new Contenido();

        try {
            System.out.println("-> url: " + enl.getUrl());

            Document doc = Jsoup.parse(Jmcrawl.archivoRemoto(enl.getUrl()), "utf-8");

            if (!doc.getElementsByClass("nota_text").isEmpty() && !doc.getElementsByClass("enc_titulo").isEmpty() && !doc.getElementsByClass("enc_aias").isEmpty() && !doc.getElementsByClass("enc_fecha").isEmpty()) {
                c.setEnlace(enl);
                c.setTitulo(doc.getElementsByClass("enc_titulo").text().trim());
                c.setContenido(doc.getElementsByClass("nota_text").text().trim());
                c.setAutor(doc.getElementsByClass("enc_alias").text().trim());
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                String fec = null;

                String[] fe = doc.getElementsByClass("enc_fecha").text().trim().split(" ");
                fec = fe[0] + "-" + this.meses().get(fe[2]) + "-" + fe[3]+" "+fe[5];
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

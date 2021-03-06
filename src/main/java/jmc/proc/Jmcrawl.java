/*
 * 
 * 
 * 
 */
package jmc.proc;

/**
 *
 * @author miguel
 */
import java.io.Serializable;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import jmc.exception.JMCException;
import jmc.beans.Contenido;
import jmc.beans.Enlace;




public class Jmcrawl implements Serializable {

    public static Properties props;
    public static List<Enlace> enlaces(String url) throws JMCException {

        
        List<Enlace> lc = new ArrayList();

        try {

            Document doc = Jsoup.parse(archivoRemoto(url), "UTF-8");
            List<Element> le = doc.getElementsByTag("a");

            for (Element e : le) {
                String atr = e.attr("href");
                if (atr != null 
                        && !atr.equals ("/") && !atr.equals("#") 
                        && (url.replace("http://", "").replace("https://", "").split("/")[0])
                        .equals(atr.replace("http://", "").replace("https://", "").split("/")[0])
                        && !url.equals(atr)
                    ) 
                {

                    Enlace c = new Enlace();
                    c.setUrl(atr);

                    List<String> ltg = new ArrayList(Arrays.asList(atr.replace("http://", "").replace("http://", "").split("/")));
                    c.setTags(ltg);

                    if (lc.isEmpty()) {
                        lc.add(c);
                    } else {
                        if (!existeEnlace(lc, c)) {
                            lc.add(c);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new JMCException(e);
        }

        return lc;
    }
    
    public static List<Enlace> enlaces(String url, String host) throws JMCException {

        List<Enlace> lc = new ArrayList();

        try {

            Document doc = Jsoup.parse(archivoRemoto(url), "UTF-8");
            List<Element> le = doc.getElementsByTag("a");

            for (Element e : le) {
                String atr = e.attr("href");
                
                if (!atr.contains(host)) atr = host+atr;
                
                if (atr != null 
                        && !atr.equals ("/") && !atr.equals("#") 
                        && (url.replace("http://", "").replace("https://", "").split("/")[0])
                        .equals(atr.replace("http://", "").replace("https://", "").split("/")[0])
                        && !url.equals(atr)
                    ) 
                {

                    Enlace c = new Enlace();
                    c.setUrl(atr);

                    List<String> ltg = new ArrayList(Arrays.asList(atr.replace("http://", "").replace("http://", "").split("/")));
                    c.setTags(ltg);

                    if (lc.isEmpty()) {
                        lc.add(c);
                    } else {
                        if (!existeEnlace(lc, c)) {
                            lc.add(c);
                        }
                    }
                }
            }

        } catch (IOException e) {
            throw new JMCException(e);
        }

        return lc;
    }

    public static File archivoRemoto(String url) throws JMCException {

        File f = null;

        try {
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL ur = new URL(url);
            HttpURLConnection cn = (HttpURLConnection) ur.openConnection();

            cn.setRequestProperty("user-agent", props.getProperty("navegador"));
            cn.setInstanceFollowRedirects(false);
            cn.setUseCaches(false);            
            cn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(cn.getInputStream()));
            String dirTempHtml = props.getProperty("archive_html") + ur.getHost() + "/";
            File fld = new File(dirTempHtml);

            boolean creado = fld.exists() ? true : fld.mkdir();

            if (creado) {

                String archFile = ur.getFile().equals("/") ? "index.html" : ur.getFile().replace("/", "_") + ".html";
                String archivo = dirTempHtml + archFile;
                String linea = null;

                f = new File(archivo);
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                while ((linea = br.readLine()) != null) {
                    bw.append(linea);
                }
                bw.close();
            }

            cn.disconnect();
        } catch (ProtocolException e) {
            //return archivoRemoto(url);
            e.printStackTrace();
        } catch (IOException e) {
            throw new JMCException(e);
        }

        return f;
    }

    
    private static boolean existeEnlace(List<Enlace> l, Enlace s) {
        boolean existe = false;

        int apuntador = l.size() / 2;
        int der = apuntador + 1;
        int izq = apuntador - 1;
        boolean buscando = true;
        boolean inicio = true;

        while (buscando && !existe) {

            if (inicio) {
                if (l.get(apuntador).getUrl().equals(s.getUrl())) {
                    buscando = false;
                    existe = true;
                } else {
                    if ((izq <= l.size() && izq > 0) && l.get(izq).getUrl().equals(s.getUrl())) {
                        existe = true;
                    } else if ((der < l.size()) && l.get(der).getUrl().equals(s.getUrl())) {
                        existe = true;
                    }
                }
                inicio = false;
            } else {

                izq -= 1;
                der += 1;

                if (izq >= 0) {
                    if (l.get(izq).getUrl().equals(s.getUrl())) {
                        existe = true;
                    }
                }
                if (buscando) {
                    if (der < l.size()) {
                        if (l.get(der).getUrl().equals(s.getUrl())) {
                            existe = true;
                        }
                    }
                }
            }

            if (buscando) {
                if (izq <= 0 && der >= l.size()) {
                    buscando = false;
                }
            }
        }

        return existe;
    }

}

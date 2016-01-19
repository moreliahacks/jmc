/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.util;

/**
 *
 * @author miguel
 */
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import jmc.dbc.Dao;
import jmc.exception.JMCException;
import jmc.beans.Fbcoment;
import jmc.beans.Contenido;
import jmc.proc.Jmcrawl;


public class UtlFbComents {

    ///private static final String HOST_FB_GRAPH = "http://graph.facebook.com/comments?id=http://www.proceso.com.mx/?p=424756&limit=100&filter=stream";
    public static void getComentarios(Long idContenido, Long numComents) throws JMCException {

        File f = null;

        try {
            Properties props = ConfigPropiedades.getProperties("props_config.properties");
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL ur = new URL("http://graph.facebook.com/comments?id=" + Contenido.getContenido("Where cont_id = '" + idContenido + "'").get(0).getEnlaceURL() + "&limit=" + numComents + "&filter=stream");
            HttpURLConnection cn = (HttpURLConnection) ur.openConnection();

            cn.setRequestProperty("user-agent", props.getProperty("navegador"));
            cn.setInstanceFollowRedirects(false);
            cn.setUseCaches(false);
            cn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(cn.getInputStream()));
            String dirTempHtml = props.getProperty("archive_json") + "/";
            File fld = new File(dirTempHtml);

            boolean creado = fld.exists() ? true : fld.mkdir();

            if (creado) {
                String archFile = idContenido + ".json";
                String archivo = dirTempHtml + archFile;
                String linea = null;

                f = new File(archivo);
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                while ((linea = br.readLine()) != null) {
                    bw.append(linea);
                }
                bw.close();

                cn.disconnect();

                List<Fbcoment> lfb = Convertidor.getFbcomentObjects(idContenido, archivo);
                Fbcoment.actComents(idContenido, lfb);

            } else {
                cn.disconnect();                
            }

        } catch (IOException e) {
            throw new JMCException(e);
        }
    }

    public static void getURLComentarios(String url, Long numComents) throws JMCException {

        File f = null;

        try {
            Properties props = ConfigPropiedades.getProperties("props_config.properties");
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL ur = new URL("http://graph.facebook.com/comments?id=" + url+ "&limit=" + numComents + "&filter=stream");
            HttpURLConnection cn = (HttpURLConnection) ur.openConnection();

            cn.setRequestProperty("user-agent", props.getProperty("navegador"));
            cn.setInstanceFollowRedirects(false);
            cn.setUseCaches(false);
            cn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(cn.getInputStream()));
            String dirTempHtml = props.getProperty("archive_json") + "/";
            File fld = new File(dirTempHtml);

            boolean creado = fld.exists() ? true : fld.mkdir();

            if (creado) {
                String archFile = "archivo.json";
                String archivo = dirTempHtml + archFile;
                String linea = null;

                f = new File(archivo);
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                while ((linea = br.readLine()) != null) {
                    bw.append(linea);
                }
                bw.close();

                cn.disconnect();

                List<Fbcoment> lfb = Convertidor.getFbcomentObjects(1l, archivo);
               // Fbcoment.actComents(idContenido, lfb);

            } else {
                cn.disconnect();
            }

        } catch (IOException e) {
            throw new JMCException(e);
        }
    }
    
    
    public static String getJSONComentarios(String url, Long numComents) throws JMCException {
        String linea = "";
        String buf = "";

        try {
            Properties props = ConfigPropiedades.getProperties("props_config.properties");
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            URL ur = new URL("http://graph.facebook.com/comments?id=" + url + "&limit=" + numComents + "&filter=stream");
            HttpURLConnection cn = (HttpURLConnection) ur.openConnection();

            cn.setRequestProperty("user-agent", props.getProperty("navegador"));
            cn.setInstanceFollowRedirects(false);
            cn.setUseCaches(false);
            cn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(cn.getInputStream()));

            while ((linea = br.readLine()) != null) {
                buf.concat(linea);
            }
            cn.disconnect();

        } catch (IOException e) {
            throw new JMCException(e);
        }
        return buf;
    }

}

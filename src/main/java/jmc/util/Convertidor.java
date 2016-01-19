/*
 * 
 * 
 * 
 */
package jmc.util;

/**
 *
 * @author miguel
 */
import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import jmc.beans.Contenido;
import jmc.beans.Fbcoment;
import jmc.exception.JMCException;


public class Convertidor {

    public static JSONArray JSON(List<Contenido> lc) {

        JSONArray nar = new JSONArray();

        for (Contenido c : lc) {
            JSONObject jo = new JSONObject();
            jo.put("Enlace", c.getEnlace().getUrl());
            jo.put("Tags", c.getEnlace().getTags());
            jo.put("Titulo", c.getTitulo().trim());
            jo.put("Contenido", c.getContenido().trim());
            jo.put("Autor", c.getAutor().trim());
            jo.put("Fecha", c.getFecha());
            nar.add(jo);
        }

        return nar;
    }

    public static Document DOM(List<Contenido> lc) throws JMCException {

        Document doc = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
            
            Element root = doc.createElement("Noticias");            
            
            for (Contenido c : lc) {
                Element e = doc.createElement("Enlace");
                e.setAttribute("url", c.getEnlace().getUrl());
                
                Element tags = doc.createElement("Tags");
                for (String tg : c.getEnlace().getTags()) {
                    Element t = doc.createElement("Tag");
                    t.setTextContent(tg);
                    tags.appendChild(t);
                }
                e.appendChild(tags);
                
                Element tit = doc.createElement("Titulo");
                tit.setTextContent(c.getTitulo().trim());
                
                Element cont = doc.createElement("Contenido");
                cont.setTextContent(c.getContenido().trim());
                cont.setAttribute("Autor", c.getAutor().trim());
                cont.setAttribute("Fecha", c.getFecha().toString());
                e.appendChild(tit);
                e.appendChild(cont);
                
                root.appendChild(e);                
            }
            doc.appendChild(root);
            
        } catch (ParserConfigurationException e) {
            throw new JMCException(e);
        }

        return doc;
    }

    public static List<Fbcoment> getFbcomentObjects(Long idCont, String path) throws JMCException {
        List<Fbcoment> lfb = new ArrayList();
        
        try {            
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject)parser.parse(new FileReader(path));             
            JSONArray dataList = (JSONArray) obj.get("data");
            Iterator<JSONObject> ij = dataList.iterator();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            while (ij.hasNext()) {
                JSONObject o = ij.next();
                Fbcoment fb = new Fbcoment();
                fb.setFbcoContenido(idCont);
                
                JSONObject on = (JSONObject)o.get("from");
                fb.setFbcoIDN(Long.valueOf(String.valueOf(on.get("id"))));
                fb.setFbcoNombre(String.valueOf(on.get("name")));
                
                
                fb.setFbcoMsg(String.valueOf(o.get("message")));
                fb.setFbcoIdc(String.valueOf(o.get("id")));
                fb.setFcoLkcnt(Long.valueOf(String.valueOf(o.get("like_count"))));             
                fb.setFbcoFecha(sdf.parse(String.valueOf(o.get("created_time")).replace('T', ' ')));
                lfb.add(fb);
            }
          
        } catch (org.json.simple.parser.ParseException e) {
          throw new JMCException(e);                       
        } catch (ParseException e) {    
          throw new JMCException(e);                       
        } catch (IOException e) {
          throw new JMCException(e);                        
        }
        
     return lfb;   
    }
    
    
}

/*
 * 
 * 
 * 
 */
package jmc;

/**
 *
 * @author miguel
 */
import java.lang.InterruptedException;
import java.lang.reflect.Method;
import java.util.List;
import jmc.beans.Contenido;
import jmc.beans.Enlace;
import jmc.proc.Jmcrawl;
import jmc.util.ConfigPropiedades;
import jmc.util.UtlFbComents;
import jmc.exception.JMCException;


public class Jmc {

    private static final boolean DEBUG = false;
    
    public static void main(String[] args) {

        if (args[0].equals("scrap") && args[1].matches("^[0-9]+") && args[2].matches("^[0-9]+")) {

            int pagsInicio = args[1].matches("^[0-9]+") ? Integer.valueOf(args[1]) : 1;
            int pags = args[1].matches("^[0-9]+") ? Integer.valueOf(args[2]) : 10;
            String clase = args[3];

            try {
                
                Class jq = Class.forName(clase);
                Object ob = jq.newInstance();

                Class[] chc = new Class[1];
                Object[] coc = new Object[1];
                Method mtt = jq.getMethod("getHost");
                String host = String.valueOf(mtt.invoke(ob, null));

                Jmcrawl.props = ConfigPropiedades.getProperties("props_config.properties");

                for (int i = pagsInicio; i < pags; i++) {

                    String sitio = Jmcrawl.props.getProperty(clase).replace("$i", String.valueOf(i));
                    System.out.println("#### Crawl Sitio: " + sitio+ " ####");
                    List<Enlace> len = Jmcrawl.enlaces(sitio, host);

                    for (Enlace e : len) {
                        if (!Contenido.existeContenidoURL(e.getUrl().trim())) {
                            Class[] cl = new Class[1];
                            Object[] obj = new Object[1];
                            cl[0] = Class.forName("jmc.beans.Enlace");
                            Method mt = jq.getMethod("contenido", cl);
                            obj[0] = mt.invoke(ob, e);
                            Contenido c = (Contenido) obj[0];

                            if (!DEBUG) {
                                if (c != null && c.getContenido() != null) {
                                    Contenido.agrContenido(c);
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (args[0] != null && args[0].equals("act-fbc") && args[1].matches("^[0-9]+") && args[2].matches("^[0-9]+")) {
         
          try {              
            List<Contenido> lc = Contenido.getContenido("Where cont_sitio = '"+args[1]+"'");            
            int i = 1;
            for (Contenido c : lc) {
                System.out.println(i+") Comentarios para: "+c.getEnlaceURL());
                if (i == 200) {
                  i = 1;
                  Thread.sleep(30000);
                }
                UtlFbComents.getComentarios(c.getContId(), Long.valueOf(args[2]));               
             i++;   
            }            
            
          } catch (JMCException e) {
              e.printStackTrace();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
                       
        } else {
            System.out.println("Usar: java -jar jmc.jar [scrap] [<numero de paginas (default: 10)>] [jmc.proc.JmcCBScrap | jmc.proc.JmcMMScrap | jmc.proc.JmcCHANScrap | jmc.proc.JmcExtraScrap | jmc.proc.JmcLAVOZScrap | jmc.proc.JmcQUAScrap ...] ");
            System.out.println("Usar: java -jar jmc.jar [act-fbc] [1-10 <cadena_sitio_busqueda>] [<numero comentarios>]");
        }
    }

}

/*
 * 
 * 
 * 
 */
package jmc.beans;

/**
 *
 * @author miguel
 */
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jmc.exception.JMCException;
import jmc.dbc.Dao;

public class Contenido implements Serializable {

    private Long contId;
    private String titulo;
    private String contenido;
    private String autor;
    private Date fecha;
    private Enlace enlace;
    private String enlaceURL;
    private Long sitio;
    
    
    public Contenido() {
    }

    public Long getSitio() {
        return sitio;
    }

    public void setSitio(Long sitio) {
        this.sitio = sitio;
    }

    public String getEnlaceURL() {
        return enlaceURL;
    }

    public void setEnlaceURL(String enlaceURL) {
        this.enlaceURL = enlaceURL;
    }

    public Long getContId() {
        return contId;
    }

    public void setContId(Long contId) {
        this.contId = contId;
    }

    public String getAutor() {
        return autor;
    }

    public String getContenido() {
        return contenido;
    }

    public Enlace getEnlace() {
        return enlace;
    }

    public Date getFecha() {
        return fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public void setEnlace(Enlace enlace) {
        this.enlace = enlace;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public static List<Contenido> getContenido(String cond) throws JMCException {
        List<Contenido> lc = new ArrayList();
        Dao dao = new Dao();
        try {
            
            ResultSet rst = dao.consultar("Select cont_id,cont_titulo,cont_contenido,cont_autor,cont_enlace,cont_fecha,cont_sitio From contenido "+cond);
            
            while (rst != null  && rst.next()) {
                Contenido cont = new Contenido();
                cont.setContId(rst.getLong("cont_id"));
                cont.setTitulo(rst.getString("cont_titulo"));
                cont.setContenido(rst.getString("cont_contenido"));
                cont.setAutor(rst.getString("cont_autor"));
                cont.setEnlaceURL(rst.getString("cont_enlace"));
                cont.setFecha(rst.getDate("cont_fecha"));
                cont.setSitio(rst.getLong("cont_sitio"));
              lc.add(cont);
            }
            
            
        } catch (SQLException e) {
            throw new JMCException(e);
        } finally {
            dao.desconectar();
        }
     return lc;   
    }
    

    public static void agrContenido(Contenido c) throws JMCException {

        Dao dao = new Dao();
        try {
            int seq = 0;
            ResultSet rst = dao.consultar("Select nextval('seqcontenido')");
            if (rst != null && rst.next()) {
                seq = rst.getInt(1);
            }

            if (seq != 0) {
                PreparedStatement ptsm = dao.pStatement("insert into CONTENIDO(CONT_ID,CONT_TITULO,CONT_CONTENIDO,CONT_AUTOR,CONT_ENLACE,CONT_FECHA,CONT_SITIO) VALUES (?,?,?,?,?,?,?) ");
                ptsm.setInt(1, seq);
                ptsm.setString(2, c.getTitulo().trim());
                ptsm.setString(3, c.getContenido().trim());
                ptsm.setString(4, c.getAutor().trim());
                ptsm.setString(5, c.getEnlace().getUrl().trim());
                ptsm.setDate(6, new java.sql.Date(c.getFecha().getTime()));
                ptsm.setLong(7, c.getSitio());

                int ret = dao.statementUpdate(ptsm);
                ptsm.clearParameters();
                ptsm.close();

            }

        } catch (SQLException e) {
            throw new JMCException(e);
        } finally {
            dao.desconectar();
        }
    }
    
    public static boolean existeContenidoURL(String url) throws JMCException {
        boolean existe = false;
        
        Dao dao = new Dao();
        try {            
            existe = dao.existe("Select count(*) From contenido Where cont_enlace = '"+url+"'");                                
        } finally {
            dao.desconectar();
        }
        
     return existe;   
    }

}

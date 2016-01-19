/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import jmc.dbc.Dao;
import jmc.exception.JMCException;

public class Fbcoment implements Serializable {
    
    private Long fbcoContenido;
    private String fbcoNombre;
    private Long fbcoIDN;
    private String fbcoMsg;
    private Date fbcoFecha;
    private Long fcoLkcnt;
    private String fbcoIdc;
    
    public Long getFbcoContenido() {
        return fbcoContenido;
    }
    
    public Date getFbcoFecha() {
        return fbcoFecha;
    }
    
    public Long getFbcoIDN() {
        return fbcoIDN;
    }
    
    public String getFbcoIdc() {
        return fbcoIdc;
    }
    
    public String getFbcoMsg() {
        return fbcoMsg;
    }
    
    public String getFbcoNombre() {
        return fbcoNombre;
    }
    
    public Long getFcoLkcnt() {
        return fcoLkcnt;
    }
    
    public void setFbcoContenido(Long fbcoContenido) {
        this.fbcoContenido = fbcoContenido;
    }
    
    public void setFbcoFecha(Date fbcoFecha) {
        this.fbcoFecha = fbcoFecha;
    }
    
    public void setFbcoIDN(Long fbcoIDN) {
        this.fbcoIDN = fbcoIDN;
    }
    
    public void setFbcoIdc(String fbcoIdc) {
        this.fbcoIdc = fbcoIdc;
    }
    
    public void setFbcoMsg(String fbcoMsg) {
        this.fbcoMsg = fbcoMsg;
    }
    
    public void setFbcoNombre(String fbcoNombre) {
        this.fbcoNombre = fbcoNombre;
    }
    
    public void setFcoLkcnt(Long fcoLkcnt) {
        this.fcoLkcnt = fcoLkcnt;
    }
    
    public static List<Fbcoment> getFBComents(String cond) throws JMCException {
        List<Fbcoment> lfb = new ArrayList();
        
        Dao dao = new Dao();
        
        try {
            
            ResultSet rst =
            dao.consultar("SELECT "
                    + " FBCO_CONTENIDO, "
                    + "	FBCO_NOMBRE, "
                    + "	FBCO_IDN, "
                    + "	FBCO_MSG, "
                    + "	FBCO_FECHA, "
                    + "	FBCO_LKCNT, "
                    + "	FBCO_IDC"
                    + "FROM Fbcoment "+cond
                        );
            
            while (rst != null && rst.next()) {
                Fbcoment fb = new Fbcoment();
                fb.setFbcoContenido(rst.getLong("FBCO_CONTENIDO"));
                fb.setFbcoNombre(rst.getString("FBCO_NOMBRE"));
                fb.setFbcoIDN(rst.getLong("FBCO_IDN"));
                fb.setFbcoMsg(rst.getString("FBCO_MSG"));
                fb.setFbcoFecha(rst.getDate("FBCO_FECHA"));
                fb.setFcoLkcnt(rst.getLong("FBCO_LKCNT"));
                fb.setFbcoIdc(rst.getString("FBCO_IDC"));
                lfb.add(fb);
            }
            
        } catch (SQLException e) {
            throw new JMCException(e);
        } finally {
            dao.desconectar();
        }
        
        return lfb;        
    }
    
    public static void actComents(Long contID, List<Fbcoment> lfbc) throws JMCException {
        
        Dao dao = new Dao();
        
        try {
            int ret = 0;
            PreparedStatement prep;
            boolean ex = dao.existe("Select count(*) From FBCOMENT Where fbco_contenido = '" + contID + "'");
            
            if (ex) {
                prep = dao.pStatement("Delete From FBCOMENT Where FBCO_CONTENIDO = ?");
                prep.setLong(1, contID);
                ret = dao.statementUpdate(prep);
                prep.clearParameters();
                prep.close();
            } else {
                ret = 1;
            }            
            
            if (ret > 0) {
                for (Fbcoment fb : lfbc) {
                    prep = dao.pStatement("Insert into fbcoment(FBCO_CONTENIDO,FBCO_NOMBRE,FBCO_IDN,FBCO_MSG,FBCO_FECHA,FBCO_LKCNT,FBCO_IDC) values(?,?,?,?,?,?,?)");
                    prep.setLong(1, fb.getFbcoContenido());
                    prep.setString(2, fb.getFbcoNombre());
                    prep.setLong(3, fb.getFbcoIDN());
                    prep.setString(4, fb.getFbcoMsg());
                    prep.setDate(5, new java.sql.Date(fb.getFbcoFecha().getTime()));
                    prep.setLong(6, fb.getFcoLkcnt());
                    prep.setString(7, fb.getFbcoIdc());
                    dao.statementUpdate(prep);
                    prep.clearParameters();
                    prep.close();
                }
            }
            
        } catch (SQLException e) {
            throw new JMCException(e);
        } finally {
            dao.desconectar();
        }
    }
    
}

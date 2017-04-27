/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmc.dbc;

/**
 *
 * @author miguel
 */
import java.io.IOException;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Properties;
import jmc.util.ConfigPropiedades;
import jmc.exception.JMCException;

public class Dao {

    private Connection conn;
    private Statement stm;
    private ResultSet rst;
    private Properties props;
    private static final String propsDao = "props_dao.properties";
    private boolean autocommit = true;
    private String sparam;
    private String sql;
    private List<SQLParams> lParams;
    private List<SQLResult> lResult;
    private PreparedStatement pstm;

    public Dao() throws JMCException {
        this.conectar();
    }

    public Dao(boolean autocommit) throws JMCException {
        this.autocommit = autocommit;
        this.conectar();
    }

    private void conectar() throws JMCException {

        try {

            props = ConfigPropiedades.getProperties(propsDao);
            conn = DriverManager.getConnection("jdbc:postgresql://" + props.getProperty("host") + ":" + props.getProperty("puerto") + "/" + props.getProperty("sid"),
                    props.getProperty("usuario"),
                    props.getProperty("password")
            );
            conn.setAutoCommit(autocommit);

        } catch (IOException e) {
            throw new JMCException(e);
        } catch (SQLException e) {
            throw new JMCException(e);
        }

    }

    public ResultSet consultar(String sql) throws JMCException {
        try {
            stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rst = stm.executeQuery(sql);
        } catch (SQLException e) {
            throw new JMCException(e);
        }
        return rst;
    }
	
	public PreparedStatement consultarSec(String sql) throws JMCException {
		
		try {
			
			pstm = conn.preparedStatement(sql);
			
		} catch (SQLException e) {
			throw new JMCException(e);
		}
		
		result pstm;		
	}

    public int actualizar(String sql, List<SQLParams> lParams) throws JMCException {
        return this.modificar(sql, lParams);
    }

    public int insertar(String sql, List<SQLParams> lParams) throws JMCException {
        return this.modificar(sql, lParams);
    }

    public int borrar(String sql, List<SQLParams> lParams) throws JMCException {
        return this.modificar(sql, lParams);
    }

    private int modificar(String sql, List<SQLParams> lParams) throws JMCException {
        int ret = 0;

        try {
            this.lParams = lParams;
            this.sql = sql;
            pstm = this.conn.prepareStatement(sql);
            this.armaDatos();
            ret = pstm.executeUpdate();
        } catch (SQLException e) {
            throw new JMCException(e);
        }
        return ret;
    }

    private void armaDatos() throws SQLException {
        int i = 1;
        for (SQLParams pl : lParams) {
            switch (pl.getTipoDato()) {
                case SQLParams.CADENA_CARACTERES:
                    pstm.setString(i, String.valueOf(pl.getDato()));
                    break;
                case SQLParams.FECHA:
                    pstm.setDate(i, Date.valueOf(String.valueOf(pl.getDato())));
                    break;
                case SQLParams.NUMERO_DOUBLE:
                    pstm.setDouble(i, (Double) pl.getDato());
                    break;
                case SQLParams.NUMERO_ENTERO:
                    pstm.setInt(i, (Integer) pl.getDato());
                    break;
                case SQLParams.NUMERO_FLOAT:
                    pstm.setFloat(i, (Float) (pl.getDato()));
                    break;
                case SQLParams.NUMERO_LONG:
                    pstm.setLong(i, (Long) pl.getDato());
                    break;
                case SQLParams.NUMERO_BIGDECIMAL:
                    pstm.setBigDecimal(i, BigDecimal.valueOf((Double) pl.getDato()));
                    break;
            }

            sparam += "[" + i + "," + pl.getDato() + "," + pl.getTipoDato() + "] ";
            i++;
        }
    }

    private int modificar(String sql) throws JMCException {
        int reng = 0;
        this.sql = sql;

       try { 
        stm = conn.createStatement();
        reng = stm.executeUpdate(sql);
       } catch (SQLException e) {
           throw new JMCException(e);
       }
        return reng;
    }

    public int actualizar(String sql) throws JMCException {
        return modificar(sql);
    }

    public int borrar(String sql) throws JMCException {
        return modificar(sql);
    }

    public int insertar(String sql) throws JMCException {
        return modificar(sql);
    }

    
    public PreparedStatement pStatement(String sql) throws JMCException {
        PreparedStatement pstm;        
        try {            
            pstm = conn.prepareStatement(sql);            
        } catch (SQLException e) {
            throw new JMCException(e);
        }
     return pstm;   
    }
    
    public int statementUpdate(PreparedStatement ptsm) throws JMCException {
        int ret = 0;
        try {            
            ret = ptsm.executeUpdate();            
        } catch (SQLException e) {
            throw new JMCException(e);
        }        
      return ret;  
    }
    
    public ResultSet statementResultSet(PreparedStatement ptsm) throws JMCException {
        
        try {            
            rst = ptsm.executeQuery();            
        } catch (SQLException e) {
            throw new JMCException(e);
        }
        
      return rst;  
    }
    
    public boolean existe(String sql) throws JMCException {
        boolean exist = false;
        try {
            
            stm = conn.createStatement();
            rst = stm.executeQuery(sql);
            
            if (rst != null && rst.next()) {
                exist = rst.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            throw new JMCException(e);
        }
      return exist;  
    }
    
    
    public void setCommit() throws JMCException {
       try { 
        conn.commit();
       } catch (SQLException e) {
           throw new JMCException(e);
       } 
    }

    public void setRollBack() throws JMCException {
       try { 
        conn.rollback();
       } catch (SQLException e) {
           throw new JMCException(e);
       } 
    }

    public String getSQL() {
        return sql;
    }

    public String getParams() {
        return sparam;
    }

    public void desconectar() {
        try {
            if (rst instanceof ResultSet) {
                rst.close();
            }

            if (stm instanceof Statement) {
                stm.close();
            }

            if (pstm instanceof PreparedStatement) {
                pstm.close();
            }

            if (conn instanceof Connection) {
                conn.close();
            }
        } catch (Exception e) {
            try {
                rst.close();
                stm.close();
                pstm.close();
                conn.close();
            } catch (SQLException f) {
            }
        } finally {
            rst = null;
            stm = null;
            pstm = null;
            conn = null;
        }
    }

}

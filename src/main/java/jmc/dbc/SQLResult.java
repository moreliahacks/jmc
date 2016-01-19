/* U M S N H
 * Autor: Miguel Angel Cedeno Garciduenas
 * email: miguel@siia.umich.mx, miguecg@gmail.com
 * 
 * M16U3
 */
package jmc.dbc;
/**
 *
 * @author miguel
 */
public class SQLResult {
    
    private String columna;
    private Object dato;
    private String tipoDato;
    private String tabla;
    private String schema;
    private int tamanio;

    public String getSchema() {
        return schema;
    }

    public String getTabla() {
        return tabla;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }
        

    public String getColumna() {
        return columna;
    }

    public Object getDato() {
        return dato;
    }

    public int getTamanio() {
        return tamanio;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }
    
}

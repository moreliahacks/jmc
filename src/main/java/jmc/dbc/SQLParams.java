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
import java.io.Serializable;

public class SQLParams implements Serializable {
    
    public static final String NUMERO_ENTERO = "I";
    public static final String NUMERO_LONG = "L";
    public static final String NUMERO_DOUBLE = "D";
    public static final String CADENA_CARACTERES = "C";
    public static final String NUMERO_FLOAT = "R";
    public static final String FECHA = "F";
    public static final String NUMERO_BIGDECIMAL = "B";
   
    
    private Object dato;
    private String tipoDato;

    public SQLParams(String dato, String tipoDato) {
        this.dato = dato;
        this.tipoDato = tipoDato;        
    }

    public SQLParams() {
    }
        
    
    public Object getDato() {
        return dato;
    }

    public void setDato(Object dato) {
        this.dato = dato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }

    public String getTipoDato() {
        return tipoDato;
    }
       
}

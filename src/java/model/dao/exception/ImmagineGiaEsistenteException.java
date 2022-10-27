/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao.exception;

/**
 *
 * @author Stefano
 */
public class ImmagineGiaEsistenteException extends Exception {

    /**
     * Creates a new instance of <code>ImmagineGiaEsistenteException</code>
     * without detail message.
     */
    public ImmagineGiaEsistenteException() {
    }

    /**
     * Constructs an instance of <code>ImmagineGiaEsistenteException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ImmagineGiaEsistenteException(String msg) {
        super(msg);
    }
}

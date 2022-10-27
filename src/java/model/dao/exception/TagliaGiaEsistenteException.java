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
public class TagliaGiaEsistenteException extends Exception {

    /**
     * Creates a new instance of <code>TagliaGiaEsistenteException</code>
     * without detail message.
     */
    public TagliaGiaEsistenteException() {
    }

    /**
     * Constructs an instance of <code>TagliaGiaEsistenteException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public TagliaGiaEsistenteException(String msg) {
        super(msg);
    }
}

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
public class ProdottoNonDisponibileException extends Exception {
    
    public ProdottoNonDisponibileException() {
    }
    
    public ProdottoNonDisponibileException(String msg){
        super(msg);
    }
}

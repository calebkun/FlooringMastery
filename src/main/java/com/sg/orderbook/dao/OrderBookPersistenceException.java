/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

/**
 *
 * @author calebdiaz
 */
public class OrderBookPersistenceException extends Exception {

    public OrderBookPersistenceException(String message) {
        super(message);
    }

    public OrderBookPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.service;

/**
 *
 * @author calebdiaz
 */
public class OrderBookInvalidDateException extends Exception {

    public OrderBookInvalidDateException(String message) {
        super(message);
    }

    public OrderBookInvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

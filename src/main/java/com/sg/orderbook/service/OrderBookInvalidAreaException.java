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
public class OrderBookInvalidAreaException extends Exception {

    public OrderBookInvalidAreaException(String message) {
        super(message);
    }

    public OrderBookInvalidAreaException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

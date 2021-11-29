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
public class OrderBookIncompleteOrderException extends Exception {

    public OrderBookIncompleteOrderException(String message) {
        super(message);
    }

    public OrderBookIncompleteOrderException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

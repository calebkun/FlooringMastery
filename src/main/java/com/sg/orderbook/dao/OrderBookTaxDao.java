/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Tax;

/**
 *
 * @author calebdiaz
 */
public interface OrderBookTaxDao {
    
    /**
     * Takes in a state from user input and returns the corresponding tax object,
     * or null if no such tax object exists.
     * 
     * @param state - String object from user input
     * @return Tax object or null
     * @throws OrderBookPersistenceException 
     */
    Tax getTax(String state) throws OrderBookPersistenceException;
    
}

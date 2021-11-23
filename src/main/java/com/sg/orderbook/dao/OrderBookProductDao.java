/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Product;
import java.util.List;

/**
 *
 * @author calebdiaz
 */
public interface OrderBookProductDao {
    
    /**
     * Takes in product type from user input and returns corresponding product
     * object, or null if no such product exists.
     * 
     * @param productType - String from user input
     * @return Product object or null
     * @throws OrderBookPersistenceException 
     */
    Product getProduct(String productType) throws OrderBookPersistenceException;
    
    /**
     * Returns a list of product objects.
     * 
     * @return List<Product> list of product objects
     * @throws OrderBookPersistenceException 
     */
    List<Product> getProducts() throws OrderBookPersistenceException;
    
}

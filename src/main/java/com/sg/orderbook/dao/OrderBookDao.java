/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Order;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author calebdiaz
 */
public interface OrderBookDao {
    
    /**
     * Takes in an order object created from user input and adds it to the
     * order book. If order could be placed, returns a null object, otherwise
     * returns the order object created.
     * 
     * @param order - order object created from user input
     * @return order object or null
     * @throws OrderBookPersistenceException 
     */
    Order addOrder(Order order) throws OrderBookPersistenceException;
    
    /**
     * Takes in order object with fields edited using user input. Replaces the 
     * current order object associated with the order date and number with the
     * updated order object. Returns the edited order object.
     * 
     * @param editedOrder - updated order object created from user input
     * @return order object
     * @throws OrderBookPersistenceException
     */
    Order editOrder(Order editedOrder) throws OrderBookPersistenceException;
    
    /**
     * Takes in an Order object and removes corresponding and removes it.
     * Returns the removed order object, or null if no such order
     * object exists.
     * 
     * @param order object to remove
     * @return order object or null
     * @throws OrderBookPersistenceException 
     */
    Order removeOrder(Order order) throws OrderBookPersistenceException;
    
    /**
     * Takes in order date and order number from user input and returns corresponding
     * order object or null if no such order exists. 
     * 
     * @param orderDate - LocalDate object from user input
     * @param orderNumber - String from user input
     * @return order object or null
     * @throws OrderBookPersistenceException 
     */
    Order getOrder(LocalDate orderDate, int orderNumber) throws OrderBookPersistenceException;
    
    /**
     * Takes in order date from user input and returns a list of all order objects associated
     * with the order date, or null if no orders for that date exist.
     * 
     * @param orderDate - LocalDate object from user input
     * @return List<Order> - list containing order objects or null
     * @throws OrderBookPersistenceException 
     */
    List<Order> getAllOrders(LocalDate orderDate) throws OrderBookPersistenceException;

}

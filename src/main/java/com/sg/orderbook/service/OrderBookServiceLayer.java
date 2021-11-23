/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.service;

import com.sg.orderbook.dao.OrderBookPersistenceException;
import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Product;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author calebdiaz
 */
public interface OrderBookServiceLayer {
    
    /*
    Business Logic:
        Order date must be in the future.
        Fields must not be blank.
        State must exist in tax file.
        Product must exist in product file.
        Area must be positive decimal, minimum of 100 sq ft.
    
    */
    
    /**
     * Takes in an order object created from user input, validates it
     * against our business logic, calculates relevant order data,
     * and returns the validated order object.
     * 
     * @param order
     * @return
     * @throws OrderBookPersistenceException
     * @throws OrderBookInvalidAreaException
     * @throws OrderBookInvalidDateException
     * @throws OrderBookProductException
     * @throws OrderBookTaxException 
     */
    Order validateOrder(Order order) throws
            OrderBookPersistenceException,
            OrderBookInvalidAreaException,
            OrderBookInvalidDateException,
            OrderBookProductException,
            OrderBookTaxException;
    
    /**
     * Takes in an order object created from user input and uses dao method 
     * to persist it should it be a valid order. 
     * 
     * @param order object to be persisted
     * @throws OrderBookPersistenceException 
     */
    void createOrder(Order order) throws
            OrderBookPersistenceException;
    
    /**
     * Takes in a LocalDate object containing an order date and calls dao method to
     * return a list of Order objects containing all orders for that date, or null
     * should no orders exist on that date.
     * 
     * @param orderDate - LocalDate object containing order date from user input
     * @return List<Order> - list of Order objects or null.
     * @throws OrderBookPersistenceException 
     */
    List<Order> getAllOrders(LocalDate orderDate) throws
            OrderBookPersistenceException;
    
    /**
     * Takes in order date and order number from user input and calls dao method
     * to return corresponding order object or null if no such order exists. 
     * 
     * @param orderDate - LocalDate object containing order date from user input
     * @param orderNumber - int containing order number
     * @return Order object or null
     * @throws OrderBookPersistenceException 
     */
    Order getOrder(LocalDate orderDate, int orderNumber) throws
            OrderBookPersistenceException;
    
    /**
     * Takes in order object and calls dao method to remove the order.
     * 
     * @param order to remove
     * @throws OrderBookPersistenceException 
     */
    void removeOrder(Order order) throws
            OrderBookPersistenceException;
    
    /**
     * Takes in order object edited from user input and, if valid,
     * calls dao method to persist the object.
     * 
     * @param editedOrder - object containing updated order info
     * @throws OrderBookPersistenceException
     */
    void editOrder(Order editedOrder) throws
            OrderBookPersistenceException;
    
    /**
     * Calls productDao method to return list of products.
     * 
     * @return List<Product> list of products
     * @throws OrderBookPersistenceException 
     */
    List<Product> getProducts() throws
            OrderBookPersistenceException;
}

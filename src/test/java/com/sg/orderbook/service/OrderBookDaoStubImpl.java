/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.service;

import com.sg.orderbook.dao.OrderBookDao;
import com.sg.orderbook.dao.OrderBookPersistenceException;
import com.sg.orderbook.dto.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author calebdiaz
 */
public class OrderBookDaoStubImpl implements OrderBookDao {
    
    public Order onlyOrder;
    
    /**
     * Default constructor which initializes onlyOrder with default test order fields
     */
    public OrderBookDaoStubImpl(){
        BigDecimal onlyArea = new BigDecimal("150");
        onlyArea.setScale(2, RoundingMode.HALF_UP);
        
        Order onlyOrder = new Order(LocalDate.parse("2022-01-01"), "Caleb", "California", "Carpet", onlyArea);
        onlyOrder.setOrderNumber(1);
        
        this.onlyOrder = onlyOrder;
    }
    
    /**
     * Overloaded constructor which allows a test order object to be injected via test class.
     * 
     * @param anOrder - test order object
     */
    public OrderBookDaoStubImpl(Order anOrder){
        this.onlyOrder = anOrder;
    }

    @Override
    public Order addOrder(Order order) throws OrderBookPersistenceException {
        return order;
    }

    @Override
    public Order editOrder(LocalDate orderDate, int orderNumber, Order editedOrder) throws OrderBookPersistenceException {
        if(editedOrder.getOrderDate().equals(orderDate) && (editedOrder.getOrderNumber() == onlyOrder.getOrderNumber())){
            return onlyOrder;
        }
        else {
            return null;
        }
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber) throws OrderBookPersistenceException {
        if(orderDate.equals(onlyOrder.getOrderDate()) && (orderNumber == onlyOrder.getOrderNumber())){
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws OrderBookPersistenceException {
        if(orderDate.equals(onlyOrder.getOrderDate()) && (orderNumber == onlyOrder.getOrderNumber())){
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws OrderBookPersistenceException {
        if(orderDate.equals(onlyOrder.getOrderDate())){
            List<Order> orders = new ArrayList<>();
            orders.add(onlyOrder);
            return orders;
        } else {
            return null;
        }
    }
    
}

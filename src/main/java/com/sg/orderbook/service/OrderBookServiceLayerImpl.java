/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.service;

import com.sg.orderbook.dao.OrderBookAuditDao;
import com.sg.orderbook.dao.OrderBookDao;
import com.sg.orderbook.dao.OrderBookPersistenceException;
import com.sg.orderbook.dao.OrderBookProductDao;
import com.sg.orderbook.dao.OrderBookTaxDao;
import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Product;
import com.sg.orderbook.dto.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author calebdiaz
 */
public class OrderBookServiceLayerImpl implements OrderBookServiceLayer {
    
    private OrderBookDao dao;
    private OrderBookTaxDao taxDao;
    private OrderBookProductDao productDao;
    private OrderBookAuditDao auditDao;

    public OrderBookServiceLayerImpl(OrderBookDao dao, OrderBookTaxDao taxDao, OrderBookProductDao productDao, OrderBookAuditDao auditDao){
        this.dao = dao;
        this.taxDao = taxDao;
        this.productDao = productDao;
        this.auditDao = auditDao;
    }
    
    private void validateTax(String state) 
        throws OrderBookTaxException,
            OrderBookPersistenceException {
        Tax tax = taxDao.getTax(state);
        if(tax == null){
            throw new OrderBookTaxException("Invalid state.");
        }
    }
    
    private void validateProduct(String productType) 
        throws OrderBookProductException,
            OrderBookPersistenceException {
        Product product = productDao.getProduct(productType);
        if (product == null){
            throw new OrderBookProductException("Invalid product type.");
        }
    }
    
    private void validateDate(LocalDate date) throws OrderBookInvalidDateException {
        if (date.compareTo(LocalDate.now()) != 1){
            throw new OrderBookInvalidDateException("Invalid date. Date must be in the future. Today's date: "+LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }
    }
   
    private void validateArea(BigDecimal area) 
        throws OrderBookInvalidAreaException, 
            OrderBookPersistenceException {
        if(area.compareTo(new BigDecimal("100")) == -1){
            throw new OrderBookInvalidAreaException("Invalid area. Must be greater than 100 sq.ft.");
        }
    }

    @Override
    public Order validateOrder(Order anOrder)
        throws OrderBookPersistenceException,
            OrderBookProductException,
            OrderBookTaxException,
            OrderBookInvalidAreaException,
            OrderBookInvalidDateException {
        
        validateProduct(anOrder.getProductType());
        validateTax(anOrder.getState());
        validateArea(anOrder.getArea());
        validateDate(anOrder.getOrderDate());
        
        // Get product and tax objects using order info
        Product product = productDao.getProduct(anOrder.getProductType());
        Tax tax = taxDao.getTax(anOrder.getState());
        
        // Set order fields with product and tax info
        anOrder.setCostPerSquareFoot(product.getCostPerSquareFoot());
        anOrder.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
        anOrder.setTaxRate(tax.getTaxRate());
        
        anOrder.CalculateFields();
        return anOrder;
    }
    
    @Override
    public void createOrder(Order order) 
            throws OrderBookPersistenceException {
        
        Order audit = dao.addOrder(order);
        auditDao.writeAuditEntry("ORDER #"+audit.getOrderNumber()+" PLACED FOR "+audit.getOrderDate()+".");
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws OrderBookPersistenceException {
        return dao.getAllOrders(orderDate);
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws OrderBookPersistenceException {
        return dao.getOrder(orderDate, orderNumber);
    }

    @Override
    public void removeOrder(Order order) throws OrderBookPersistenceException {
        Order audit = dao.removeOrder(order);
        auditDao.writeAuditEntry("ORDER #"+audit.getOrderNumber()+" FOR DATE "+audit.getOrderDate()+" WAS CANCELLED.");
    }

    @Override
    public void editOrder(Order editedOrder) 
        throws OrderBookPersistenceException {
        
        Order audit = dao.editOrder(editedOrder);
        auditDao.writeAuditEntry("ORDER #"+audit.getOrderNumber()+" FOR DATE "+audit.getOrderDate()+" WAS EDITED.");
    }
    
    @Override
    public List<Product> getProducts() throws OrderBookPersistenceException {
        return productDao.getProducts();
    }
    
}

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
    
    /**
     * Helper method which takes in a String with order state and calls taxDao method
     * to check against tax file. If state does not exist, throws OrderBookTaxException.
     * 
     * @param state
     * @throws OrderBookTaxException
     * @throws OrderBookPersistenceException 
     */
    private void validateTax(String state) 
        throws OrderBookTaxException,
            OrderBookPersistenceException {
        Tax tax = taxDao.getTax(state);
        if(tax == null){
            throw new OrderBookTaxException("Invalid state.");
        }
    }
    
    /**
     * Helper method which takes in a String with product type and calls productDao method
     * to check against product file. If product does not exist, throws OrderBookProductException.
     * 
     * @param productType
     * @throws OrderBookProductException
     * @throws OrderBookPersistenceException 
     */
    private void validateProduct(String productType) 
        throws OrderBookProductException,
            OrderBookPersistenceException {
        Product product = productDao.getProduct(productType);
        if (product == null){
            throw new OrderBookProductException("Invalid product type.");
        }
    }
    
    /**
     * Helper method which takes in a LocalDate object with order date and throws OrderBookInvalidDateException
     * if date is not in the future.
     * 
     * @param date
     * @throws OrderBookInvalidDateException 
     */
    private void validateDate(LocalDate date) throws OrderBookInvalidDateException {
        if (date.compareTo(LocalDate.now()) != 1){
            throw new OrderBookInvalidDateException("Invalid date. Date must be in the future. Today's date: "+LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        }
    }
   
    /**
     * Helper method which takes in a BigDecimal object with order area and throws
     * OrderBookInvalidAreaException should area be less than 100 sq ft.
     * 
     * @param area
     * @throws OrderBookInvalidAreaException
     */
    private void validateArea(BigDecimal area) 
        throws OrderBookInvalidAreaException{
        if(area.compareTo(new BigDecimal("100")) == -1){
            throw new OrderBookInvalidAreaException("Invalid area. Must be greater than 100 sq.ft.");
        }
    }

    /**
     * Helper method which takes in an Order object and throws an exception if any of the
     * member fields instantiated by the constructor are empty.
     * 
     * @param anOrder
     * @throws OrderBookIncompleteOrderException 
     */
    private void validateCompleteOrder(Order anOrder) throws OrderBookIncompleteOrderException {
        
        if(anOrder.getCustomerName().trim().isEmpty()
                || anOrder.getOrderDate() == null
                || anOrder.getState().trim().isEmpty()
                || anOrder.getProductType().trim().isEmpty()
                || anOrder.getArea() == null){
            throw new OrderBookIncompleteOrderException("ERROR: All fields "
                    + "[Order Date, Customer Name, State, Product Type, Area] are required.");
        }
    }
    
    @Override
    public Order validateOrder(Order anOrder)
        throws OrderBookPersistenceException,
            OrderBookProductException,
            OrderBookTaxException,
            OrderBookInvalidAreaException,
            OrderBookInvalidDateException,
            OrderBookIncompleteOrderException {
        
        validateCompleteOrder(anOrder);
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
            throws OrderBookPersistenceException,
            OrderBookProductException,
            OrderBookTaxException,
            OrderBookInvalidAreaException,
            OrderBookInvalidDateException,
            OrderBookIncompleteOrderException {
        
        Order validated = validateOrder(order);
        
        Order audit = dao.addOrder(validated);
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
    public Order removeOrder(LocalDate date, int orderNumber) throws OrderBookPersistenceException {
        Order audit = dao.removeOrder(date, orderNumber);
        if (audit != null){
            auditDao.writeAuditEntry("ORDER #"+audit.getOrderNumber()+" FOR DATE "+audit.getOrderDate()+" WAS CANCELLED.");
            return audit;
        }
        else {
            return null;
        }
    }

    @Override
    public void editOrder(Order originalOrder, Order editedOrder) 
        throws OrderBookPersistenceException,
            OrderBookProductException,
            OrderBookTaxException,
            OrderBookInvalidAreaException,
            OrderBookInvalidDateException,
            OrderBookIncompleteOrderException,
            OrderBookInvalidEditException {
        
        Order validated = validateOrder(editedOrder);
        
        if ((editedOrder.getOrderDate().equals(originalOrder.getOrderDate())) && (editedOrder.getOrderNumber() == originalOrder.getOrderNumber())){
            Order audit = dao.editOrder(originalOrder.getOrderDate(), originalOrder.getOrderNumber(), validated);
            auditDao.writeAuditEntry("ORDER #"+audit.getOrderNumber()+" FOR DATE "+audit.getOrderDate()+" WAS EDITED.");
        } else {
            throw new OrderBookInvalidEditException("Cannot edit order date or order number.");
        }
    }
    
    @Override
    public List<Product> getProducts() throws OrderBookPersistenceException {
        return productDao.getProducts();
    }
    
}

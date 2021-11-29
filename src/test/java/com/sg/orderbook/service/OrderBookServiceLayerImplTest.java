/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.service;

import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

 /* Business Logic: enforced only in validateOrder method
        Order date must be in the future.
        Fields must not be blank.
        State must exist in tax file.
        Product must exist in product file.
        Area must be positive decimal, minimum of 100 sq ft.
        Only customerName, state, productType, and area may be edited.

 * 

/**
 * Suite of unit tests to test OrderBook service layer.
 * 
 * TEST PLAN
 * Pass a valid order to validateOrder
 * Pass an order with order date in past to validateOrder
 * Pass an order with order date of today to validateOrder
 * Pass an order with blank field to validateOrder
 * Pass an order with invalid state to validateOrder
 * Pass an order with invalid product to validateOrder
 * Pass an order with negative decimal area to validateOrder
 * Pass an order with positive decimal less than 100 area to validateOrder
 * Pass an order with positive decimal greater than 100 area to validateOrder
 * Pass valid order to createOrder
 * Pass valid order to editOrder
 * Pass an order with edited order date to editOrder
 * Pass an order with edited order number to editOrder
 * Test pass thru methods
 * 
 * @author calebdiaz
 */
public class OrderBookServiceLayerImplTest {
    
    private OrderBookServiceLayer service;
    
    public OrderBookServiceLayerImplTest() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        service = ctx.getBean("service", OrderBookServiceLayer.class);
    }

    @Test
    public void testValidateValidOrder() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("100");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().plusDays(1), "Alec", "California", "Carpet", area);
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        } catch (OrderBookInvalidAreaException | OrderBookInvalidDateException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException e) { 
        // ASSERT
            fail("Order was valid. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testValidateOrderPastDate() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("100");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().minusDays(1), "Alec", "California", "Carpet", area);
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        // ASSERT
            fail("Expected InvalidDate exception was not thrown.");
        } catch (OrderBookInvalidAreaException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookInvalidDateException e){
            return;
        }
    }
    
    @Test
    public void testValidateOrderToday() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("100");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now(), "Alec", "California", "Carpet", area);
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        // ASSERT
            fail("Expected InvalidDate exception was not thrown.");
        } catch (OrderBookInvalidAreaException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookInvalidDateException e){
            return;
        }
    }
    
    @Test
    public void testValidateOrderBlankField() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("100");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().plusDays(1), "", "California", "Carpet", area);
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        // ASSERT
            fail("Expected IncompleteOrder exception was not thrown.");
        } catch (OrderBookInvalidAreaException | OrderBookInvalidDateException |
                    OrderBookProductException | OrderBookTaxException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookIncompleteOrderException e){
            return;
        }
    }
    
    @Test
    public void testValidateOrderPositiveAreaGreaterThan100() throws Exception {
         // ARRANGE
        BigDecimal area = new BigDecimal("101");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().plusDays(1), "Alec", "California", "Carpet", area);
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        } catch (OrderBookInvalidAreaException | OrderBookInvalidDateException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException e) { 
        // ASSERT
            fail("Order was valid. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testValidateOrderPositiveAreaLessThan100() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("99");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().plusDays(1), "Alec", "California", "Carpet", area);
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        // ASSERT
            fail("Expected InvalidArea exception was not thrown.");
        } catch (OrderBookIncompleteOrderException | OrderBookInvalidDateException |
                    OrderBookProductException | OrderBookTaxException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookInvalidAreaException e){
            return;
        }
    }
    
    @Test
    public void testValidateOrderNegativeArea() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("99");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().plusDays(1), "Alec", "California", "Carpet", area.negate());
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        // ASSERT
            fail("Expected InvalidArea exception was not thrown.");
        } catch (OrderBookIncompleteOrderException | OrderBookInvalidDateException |
                    OrderBookProductException | OrderBookTaxException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookInvalidAreaException e){
            return;
        }
    }
    
    @Test
    public void testValidateOrderInvalidProduct() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("100");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().plusDays(1), "Alec", "California", "Tile", area);
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        // ASSERT
            fail("Expected Product exception was not thrown.");
        } catch (OrderBookIncompleteOrderException | OrderBookInvalidDateException |
                    OrderBookInvalidAreaException | OrderBookTaxException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookProductException e){
            return;
        }
    }
    
    @Test
    public void testValidateOrderInvalidTax() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("100");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().plusDays(1), "Alec", "Florida", "Carpet", area);
        order.setOrderNumber(1);
        
        // ACT
        try {
            service.validateOrder(order);
        // ASSERT
            fail("Expected Product exception was not thrown.");
        } catch (OrderBookIncompleteOrderException | OrderBookInvalidDateException |
                    OrderBookInvalidAreaException | OrderBookProductException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookTaxException e){
            return;
        }
    }
    
    @Test
    public void testCreateOrder() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("100");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now().plusDays(1), "Alec", "California", "Carpet", area);
        
        // ACT
        try {
            service.createOrder(order);
        } catch (OrderBookInvalidAreaException | OrderBookInvalidDateException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException e) { 
        // ASSERT
            fail("Order was valid. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testEditValidOrder() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("150");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order testClone = new Order(LocalDate.parse("2022-01-01"), "Caleb", "California", "Carpet", area);
        testClone.setOrderNumber(1);
        
        Order edited = new Order(testClone.getOrderDate(), "Alec", testClone.getState(), testClone.getProductType(), testClone.getArea());
        edited.setOrderNumber(testClone.getOrderNumber());
        
        // ACT
        try {
            service.editOrder(testClone, edited);
        } catch (OrderBookInvalidAreaException | OrderBookInvalidDateException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException | OrderBookInvalidEditException e) { 
        // ASSERT
            fail("Edited order was valid. No exception should have been thrown.");
        }
    }
    
    @Test
    public void testEditOrderEditedDate() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("150");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order testClone = new Order(LocalDate.parse("2022-01-01"), "Caleb", "California", "Carpet", area);
        testClone.setOrderNumber(1);
        
        Order edited = new Order(testClone.getOrderDate().plusDays(1), "Alec", testClone.getState(), testClone.getProductType(), testClone.getArea());
        edited.setOrderNumber(testClone.getOrderNumber());
        
        // ACT
        try {
            service.editOrder(testClone, edited);
        // ASSERT
            fail("Expected InvalidEdit exception to be thrown.");
        } catch (OrderBookInvalidAreaException | OrderBookInvalidDateException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookInvalidEditException e){
            return;
        }
    }
    
    @Test
    public void testEditOrderEditedNumber() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("150");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order testClone = new Order(LocalDate.parse("2022-01-01"), "Caleb", "California", "Carpet", area);
        testClone.setOrderNumber(1);
        
        Order edited = new Order(testClone.getOrderDate(), "Alec", testClone.getState(), testClone.getProductType(), testClone.getArea());
        edited.setOrderNumber(testClone.getOrderNumber()+1);
        
        // ACT
        try {
            service.editOrder(testClone, edited);
        // ASSERT
            fail("Expected InvalidEdit exception to be thrown.");
        } catch (OrderBookInvalidAreaException | OrderBookInvalidDateException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException e) { 
            fail("Incorrect exception was thrown.");
        } catch (OrderBookInvalidEditException e){
            return;
        }
    }
    
    @Test
    public void testGetAllOrders() throws Exception {
        // ARRANGE 
        BigDecimal area = new BigDecimal("150");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order testClone = new Order(LocalDate.parse("2022-01-01"), "Caleb", "California", "Carpet", area);
        testClone.setOrderNumber(1);
        
        // ACT & ASSERT
        assertEquals( 1, service.getAllOrders(testClone.getOrderDate()).size(), 
                                   "Should only have one order.");
        assertTrue( service.getAllOrders(testClone.getOrderDate()).contains(testClone),
                              "The one order should be order #1.");
    }
    
    @Test
    public void testGetOrder() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("150");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order testClone = new Order(LocalDate.parse("2022-01-01"), "Caleb", "California", "Carpet", area);
        testClone.setOrderNumber(1);
        
        // ACT & ASSERT
        Order shouldBeOne = service.getOrder(testClone.getOrderDate(), testClone.getOrderNumber());
        assertNotNull(shouldBeOne, "Getting "+testClone.getOrderDate()+","+testClone.getOrderNumber()+" should not be null.");
        assertEquals(shouldBeOne, testClone, "Order stored under "+testClone.getOrderNumber()+" and "+testClone.getOrderDate()+" should be Order #1");
        
        Order shouldBeNull = service.getOrder(LocalDate.now(), testClone.getOrderNumber());
        assertNull(shouldBeNull, "Getting "+LocalDate.now()+","+testClone.getOrderNumber()+" should be null.");
        
        shouldBeNull = service.getOrder(testClone.getOrderDate(), 2);
        assertNull(shouldBeNull, "Getting "+testClone.getOrderDate()+",2 should be null.");
    }
    
    @Test
    public void testRemoveOrder() throws Exception {
        // ARRANGE
        BigDecimal area = new BigDecimal("150");
        area.setScale(2, RoundingMode.HALF_UP);
        
        Order testClone = new Order(LocalDate.parse("2022-01-01"), "Caleb", "California", "Carpet", area);
        testClone.setOrderNumber(1);
        
        // ACT & ASSERT
        Order shouldBeOne = service.removeOrder(testClone.getOrderDate(), testClone.getOrderNumber());
        assertNotNull(shouldBeOne, "Removing "+testClone.getOrderDate()+","+testClone.getOrderNumber()+" should not be null.");
        assertEquals(shouldBeOne, testClone, "Order removed from "+testClone.getOrderNumber()+" and "+testClone.getOrderDate()+" should be Order #1");
        
        Order shouldBeNull = service.getOrder(LocalDate.now(), testClone.getOrderNumber());
        assertNull(shouldBeNull, "Getting "+LocalDate.now()+","+testClone.getOrderNumber()+" should be null.");
    }
    
    @Test
    public void testGetProducts() throws Exception {
        // ARRANGE 
        BigDecimal costPerSquareFoot = new BigDecimal("2.25");
        costPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCostPerSquareFoot = new BigDecimal("2.10");
        laborCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        
        Product testClone = new Product("Carpet", costPerSquareFoot, laborCostPerSquareFoot);
        
        // ACT & ASSERT
        assertEquals( 1, service.getProducts().size(), 
                                   "Should only have one product.");
        assertTrue( service.getProducts().contains(testClone),
                              "The one product should be Carpet.");
    }
    
}

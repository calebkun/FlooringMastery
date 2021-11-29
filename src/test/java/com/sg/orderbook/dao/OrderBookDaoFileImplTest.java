/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import static com.sg.orderbook.dao.OrderBookDaoFileImpl.HEADER;
import com.sg.orderbook.dto.Order;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite of unit tests to test OrderBook dao.
 * 
 * TEST PLAN
 * Adding and getting order
 * Adding, editing, and getting orders
 * Adding orders and getting all orders
 * Adding and removing orders
 * 
 * 
 * @author calebdiaz
 */
public class OrderBookDaoFileImplTest {
    
    OrderBookDao testDao;
    
    public OrderBookDaoFileImplTest() {
    }
    
    /**
     * Does proper work on existing test file and provides
     * test file stem through overloaded dao constructor.
     * 
     * @throws Exception 
     */
    @BeforeEach
    public void setUp() throws Exception {
        String testFileStem = "testorders_";
        String testFile = dateToPath(testFileStem, LocalDate.now());
  
        // Use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        
        // Add the header to the file to avoid memory error
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(testFile));
        } catch (IOException e) {
            throw new OrderBookPersistenceException(
                    "Could not save order data.", e);
        }

        out.println(HEADER);
        out.flush();
        
        testDao = new OrderBookDaoFileImpl(testFileStem);
    }
    
    
    /**
     * ARRANGE
     * * Create test order object.
     * 
     * ACT
     * * Add test order object.
     * * Get test order object with appropriate order date and order number and store it.
     * 
     * ASSERT
     * * Check equality of object gotten to test object.
     * 
     * @throws Exception
     */
    @Test
    public void testAddGetOrder() throws Exception {
        // Create test Order
        BigDecimal area = new BigDecimal("150");
        area.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal taxRate = new BigDecimal("0");
        taxRate.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal costPerSquareFoot = new BigDecimal("0");
        costPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCostPerSquareFoot = new BigDecimal("0");
        laborCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        
        Order order = new Order(LocalDate.now(), "Caleb", "California", "Tile", area);
        order.setOrderNumber(1);
        order.setTaxRate(taxRate);
        order.setCostPerSquareFoot(costPerSquareFoot);
        order.setLaborCostPerSquareFoot(laborCostPerSquareFoot);
        order.CalculateFields();
        
        // Add order to dao
        testDao.addOrder(order);
        // Get order from dao
        Order retrievedOrder = testDao.getOrder(order.getOrderDate(), order.getOrderNumber());
        
        // Check that data is equal
        assertTrue(retrievedOrder.equals(order));
        
    }
    
    /**
     * ARRANGE
     * * Create two test order objects.
     * 
     * ACT 
     * * Add test order objects.
     * * Get list of all orders with appropriate order date.
     * 
     * ASSERT
     * * Check that list is not null.
     * * Check that list size reflect our two objects.
     * * Check that list contains our two objects.
     * 
     * @throws Exception 
     */
    @Test
    public void testAddGetAllOrders() throws Exception {
        // Create first test Order object
        BigDecimal areaOne = new BigDecimal("150");
        areaOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal taxRateOne = new BigDecimal("0");
        taxRateOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal costPerSquareFootOne = new BigDecimal("0");
        costPerSquareFootOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("0");
        laborCostPerSquareFootOne.setScale(2, RoundingMode.HALF_UP);
        
        Order firstOrder = new Order(LocalDate.now(), "Caleb", "California", "Tile", areaOne);
        firstOrder.setOrderNumber(1);
        firstOrder.setTaxRate(taxRateOne);
        firstOrder.setCostPerSquareFoot(costPerSquareFootOne);
        firstOrder.setLaborCostPerSquareFoot(laborCostPerSquareFootOne);
        firstOrder.CalculateFields();
        
        // Create second test Order object
        BigDecimal areaTwo = new BigDecimal("200");
        areaTwo.setScale(2, RoundingMode.HALF_UP);
        
        Order secondOrder = new Order(LocalDate.now(), "Alec", "Texas", "Carpet", areaTwo);
        secondOrder.setOrderNumber(2);
        secondOrder.setTaxRate(taxRateOne);
        secondOrder.setCostPerSquareFoot(costPerSquareFootOne);
        secondOrder.setLaborCostPerSquareFoot(laborCostPerSquareFootOne);
        secondOrder.CalculateFields();
        
        // Add both orders to DAO
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        
        // Retrieve the list of all orders within the DAO
        List<Order> allOrders = testDao.getAllOrders(LocalDate.now());

        // First check the general contents of the list
        assertNotNull(allOrders, "The list of Orders must not null");
        assertEquals(2, allOrders.size(),"List of Orders should have 2 Orders.");

        // Then the specifics
        assertTrue(testDao.getAllOrders(LocalDate.now()).contains(firstOrder),
                    "The list of Orders should include Order #1.");
        assertTrue(testDao.getAllOrders(LocalDate.now()).contains(secondOrder),
                "The list of Orders should include Order #2.");

    }
    
    /**
     * ARRANGE
     * * Create test order object to edit, and copy of edited object.
     * 
     * ACT
     * * Add test order object.
     * * Edit test order object and store returned object.
     * * Get edited order object and store returned object.
     * * Get list of all orders.
     * 
     * ASSERT
     * * Assert that object returned from editOrder call equals test order object.
     * * Assert that object returned from getOrder call equals copy of edited object.
     * * Assert that list size reflects our only object.
     * * Assert that contains our edited object.
     * 
     * @throws Exception 
     */
    @Test
    public void testEditOrder() throws Exception {
        // Create first test Order object to edit
        BigDecimal areaOne = new BigDecimal("150");
        areaOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal taxRateOne = new BigDecimal("0");
        taxRateOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal costPerSquareFootOne = new BigDecimal("0");
        costPerSquareFootOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("0");
        laborCostPerSquareFootOne.setScale(2, RoundingMode.HALF_UP);
        
        Order toEdit = new Order(LocalDate.now(), "Caleb", "California", "Tile", areaOne);
        toEdit.setOrderNumber(1);
        toEdit.setTaxRate(taxRateOne);
        toEdit.setCostPerSquareFoot(costPerSquareFootOne);
        toEdit.setLaborCostPerSquareFoot(laborCostPerSquareFootOne);
        toEdit.CalculateFields();
        
        // Create second test Order object with edited fields
        BigDecimal areaTwo = new BigDecimal("200");
        areaTwo.setScale(2, RoundingMode.HALF_UP);
        
        Order edited = new Order(toEdit.getOrderDate(), "Alec", "Texas", "Carpet", areaTwo);
        edited.setOrderNumber(toEdit.getOrderNumber());
        edited.setTaxRate(taxRateOne);
        edited.setCostPerSquareFoot(costPerSquareFootOne);
        edited.setLaborCostPerSquareFoot(laborCostPerSquareFootOne);
        edited.CalculateFields();
        
        // Add order to dao
        testDao.addOrder(toEdit);
        // Edit order and store returned object
        Order returnedOrder = testDao.editOrder(toEdit.getOrderDate(), toEdit.getOrderNumber(), edited);
        // Get edited order from dao
        Order retrievedOrder = testDao.getOrder(toEdit.getOrderDate(), toEdit.getOrderNumber());
        // Retrieve the list of all orders within the DAO
        List<Order> allOrders = testDao.getAllOrders(LocalDate.now());
        
        
        // Check that returned object is equal to original order
        assertEquals(returnedOrder,toEdit, "Order returned from editOrder should be original order.");
        
        // Check that data of order gotten after edit equals edited object
        assertEquals(retrievedOrder,edited, "Order should be edited order.");
        
        // Check size of list
        assertEquals(1, allOrders.size(),"List of Orders should have 1 order.");
        
        // Check that list contains edited object
        assertTrue(testDao.getAllOrders(LocalDate.now()).contains(edited),
                    "The list of Orders should include edited Order #1.");
    }
    
    /**
     * ARRANGE
     * * Create TWO test order objects to remove.
     * 
     * ACT
     * * Add test order objects.
     * * Remove first test order object and store returned object.
     * * Get all objects.
     * * Remove second test order object and store returned object.
     * * Get all objects again.
     * * Attempt to get first test order object and store returned object.
     * * Attempt to get second test order object and store returned object.
     * 
     * ASSERT
     * * Check that object returned by call to removeOrder equals first test order object.
     * * Check that object returned by call to getOrder for removed order object
     * * is null.
     * * Check that size of list of orders is 1.
     * * Check that object returned by second call to removeOrder equals second test
     * * order object.
     * * Check that object returned by second call to getOrder for second removed order
     * * object is null.
     * * Check that list of orders is now null.
     * 
     * @throws Exception 
     */
    @Test
    public void testRemoveOrder() throws Exception {
        // Create first test Order object
        BigDecimal areaOne = new BigDecimal("150");
        areaOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal taxRateOne = new BigDecimal("0");
        taxRateOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal costPerSquareFootOne = new BigDecimal("0");
        costPerSquareFootOne.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCostPerSquareFootOne = new BigDecimal("0");
        laborCostPerSquareFootOne.setScale(2, RoundingMode.HALF_UP);
        
        Order firstOrder = new Order(LocalDate.now(), "Caleb", "California", "Tile", areaOne);
        firstOrder.setOrderNumber(1);
        firstOrder.setTaxRate(taxRateOne);
        firstOrder.setCostPerSquareFoot(costPerSquareFootOne);
        firstOrder.setLaborCostPerSquareFoot(laborCostPerSquareFootOne);
        firstOrder.CalculateFields();
        
        // Create second test Order object
        BigDecimal areaTwo = new BigDecimal("200");
        areaTwo.setScale(2, RoundingMode.HALF_UP);
        
        Order secondOrder = new Order(LocalDate.now(), "Alec", "Texas", "Carpet", areaTwo);
        secondOrder.setOrderNumber(2);
        secondOrder.setTaxRate(taxRateOne);
        secondOrder.setCostPerSquareFoot(costPerSquareFootOne);
        secondOrder.setLaborCostPerSquareFoot(laborCostPerSquareFootOne);
        secondOrder.CalculateFields();
        
        // Add both orders to DAO
        testDao.addOrder(firstOrder);
        testDao.addOrder(secondOrder);
        
        // Remove Order #1 and store return
        Order removedOrder = testDao.removeOrder(firstOrder.getOrderDate(), firstOrder.getOrderNumber());
        
        // Check that correct object was removed
        assertEquals(removedOrder, firstOrder, "The removed order should be Order #1.");
        
        // Get all orders
        List<Order> allOrders = testDao.getAllOrders(LocalDate.now());
        
        // First check general contents of list
        assertNotNull(allOrders, "The list of Orders must not null");
        assertEquals(1, allOrders.size(),"List of Orders should have 1 Order.");
        
        // Then the specifics
        assertFalse(testDao.getAllOrders(LocalDate.now()).contains(firstOrder),
                    "The list of Orders should NOT include Order #1.");
        assertTrue(testDao.getAllOrders(LocalDate.now()).contains(secondOrder),
                "The list of Orders should include Order #2.");
        
        // Remove Order #2 and store return
        removedOrder = testDao.removeOrder(secondOrder.getOrderDate(), secondOrder.getOrderNumber());
        
        // Check that correct object was removed
        assertEquals(removedOrder, secondOrder, "The removed order should be Order #2.");
        
        // Get all orders again
        allOrders = testDao.getAllOrders(LocalDate.now());
        
        // Check the contents of the list - it should be empty
        assertNull(allOrders, "The retrieved list of orders should be empty.");
        
        // Try to 'get' both orders by order date and order numbers - they should be null!
        Order retrievedOrder = testDao.getOrder(firstOrder.getOrderDate(), firstOrder.getOrderNumber());
        assertNull(retrievedOrder, "Order #1 was removed, should be null");
        
        retrievedOrder = testDao.getOrder(secondOrder.getOrderDate(), secondOrder.getOrderNumber());
        assertNull(retrievedOrder, "Order #2 was removed, should be null");
        
    }
    
    /**
     * Helper method which takes a String file stem and LocalDate object, converts them to a String of the
     * appropriate format for our orders file name, and returns a String of the appropriate
     * file name and path.
     * 
     * @param file_stem - String with file stem
     * @param date - LocalDate to convert
     * @return String containing file path
     */
    private String dateToPath(String file_stem, LocalDate date){
        String dateAsString = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String dateAsPath = file_stem+dateAsString+".txt";
        return dateAsPath;
    }
    
}

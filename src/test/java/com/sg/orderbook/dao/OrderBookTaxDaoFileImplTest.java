/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author calebdiaz
 */
public class OrderBookTaxDaoFileImplTest {
    
    // Constructor which initializes test clone
    public OrderBookTaxDaoFileImplTest() {
        BigDecimal taxRate = new BigDecimal("4.45");
        taxRate.setScale(2, RoundingMode.HALF_UP);
        
        this.testClone = new Tax("TX", "Texas", taxRate);
    }
    
    OrderBookTaxDao testTaxDao;
    Tax testClone;
    
    /**
     * Passes test file to constructor.
     * 
     * @throws Exception 
     */
    @BeforeEach
    public void setUp() throws Exception {
        String testFile = "testtaxes.txt";
        
        testTaxDao = new OrderBookTaxDaoFileImpl(testFile);
    }

    /**
     * ARRANGE
     * * Already in good state.
     * 
     * ACT
     * * Calls taxDao method to get tax object with associated state.
     * 
     * ASSERT
     * * Asserts object returned by call is not null, and then that it
     * * it is equal to the test object.
     * 
     */
    @Test
    public void testGetTaxValid() throws Exception {
        // Get tax from dao
        Tax retrievedTax = testTaxDao.getTax(testClone.getStateName());
        
        // Check that retrieved Tax object is not null
        assertNotNull(retrievedTax, "Checking that retrieved object is not null.");
        
        // Check the data is equal
        assertTrue(testClone.equals(retrievedTax));
    }
    
    /**
     * ARRANGE
     * * Already in good state.
     * 
     * ACT
     * * Calls taxDao method to get tax object with invalid state
     * 
     * ASSERT
     * * Asserts object returned by call is null.
     * 
     */
    @Test
    public void testGetTaxInvalid() throws Exception {
        // Get tax from dao
        Tax retrievedTax = testTaxDao.getTax("Should fail.");
        
        // Check that retrieved Tax object is null
        assertNull(retrievedTax, "Checking that retrieved object is null.");
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author calebdiaz
 */
public class OrderBookProductDaoFileImplTest {
    
    OrderBookProductDao testProductDao;
    Product testCloneOne;
    Product testCloneTwo;
    
    // Constructor which initializes both test clones
    public OrderBookProductDaoFileImplTest() {
        BigDecimal costPerSquareFoot = new BigDecimal("2.25");
        costPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCostPerSquareFoot = new BigDecimal("2.10");
        laborCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        
        this.testCloneOne = new Product("Carpet", costPerSquareFoot, laborCostPerSquareFoot);
        
        BigDecimal costPerSquareFootTwo = new BigDecimal("1.75");
        costPerSquareFootTwo.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCostPerSquareFootTwo = new BigDecimal("2.10");
        laborCostPerSquareFootTwo.setScale(2, RoundingMode.HALF_UP);
        
        this.testCloneTwo = new Product("Laminate", costPerSquareFootTwo, laborCostPerSquareFootTwo);
    }
    
    /**
     * Passes test file to constructor.
     * 
     * @throws Exception 
     */
    @BeforeEach
    public void setUp() throws Exception {
        String testFile = "testproducts.txt";
        
        testProductDao = new OrderBookProductDaoFileImpl(testFile);
    }
    
    /**
     * ARRANGE
     * * Already in good state.
     * 
     * ACT
     * * Calls productDao method to get product object with associated product type
     * 
     * ASSERT
     * * Asserts object returned by call is not null, and then that it
     * * it is equal to the test object.
     * 
     */
    @Test
    public void testGetProductValid() throws Exception {
        // Get Product from dao
        Product retrievedProduct = testProductDao.getProduct(testCloneOne.getProductType());
        
        // Check that retrieved Product object is not null
        assertNotNull(retrievedProduct, "Checking that retrieved object is not null.");
        
        // Check the data is equal
        assertTrue(testCloneOne.equals(retrievedProduct));
    }
    
    /**
     * ARRANGE
     * * Already in good state.
     * 
     * ACT
     * * Calls productDao method to get product object with invalid product type
     * 
     * ASSERT
     * * Asserts object returned by call is null.
     * 
     */
    @Test
    public void testGetProductInvalid() throws Exception {
        // Get Product from dao
        Product retrievedProduct = testProductDao.getProduct("Should fail.");
        
        // Check that retrieved Product object is null
        assertNull(retrievedProduct, "Checking that retrieved object is null.");

    }
    
    /**
     * ARRANGE
     * * Already in good state.
     * 
     * ACT
     * * Calls productDao method to get all product objects.
     * 
     * ASSERT
     * * Asserts size of list returned is 2, then asserts that returned objects 
     * * are equal to the test objects.
     * 
     */
    @Test
    public void testGetAllProducts() throws Exception {
        // Retrieve the list of all products within the DAO
        List<Product> products = testProductDao.getProducts();

        // First check the general contents of the list
        assertNotNull(products, "The list of products must not null");
        assertEquals(2, products.size(),"List of products should have 2 products.");

        // Then the specifics
        assertTrue(testProductDao.getProducts().contains(testCloneOne),
                    "The list of products should include Carpet.");
        assertTrue(testProductDao.getProducts().contains(testCloneTwo),
                "The list of products should include Laminate.");
    }
    
}

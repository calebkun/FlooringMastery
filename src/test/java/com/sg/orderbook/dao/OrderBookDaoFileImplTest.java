/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import java.io.FileWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author calebdiaz
 */
public class OrderBookDaoFileImplTest {
    
    OrderBookDao testDao;
    
    public OrderBookDaoFileImplTest() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        String testFile = "testroster.txt";
        // Use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        testDao = new OrderBookDaoFileImpl(testFile);
    }
    
    @Test
    public void testSomeMethod() {
        fail("The test case is a prototype.");
    }
    
}

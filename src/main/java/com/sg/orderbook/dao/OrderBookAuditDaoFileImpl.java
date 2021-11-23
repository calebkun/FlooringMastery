/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 *
 * @author calebdiaz
 */
public class OrderBookAuditDaoFileImpl implements OrderBookAuditDao {
    public static final String AUDIT_FILE = "audit.txt";
    
    /**
     * Implementation of audit dao method.
     * 
     * @param entry
     * @throws OrderBookPersistenceException 
     */
    public void writeAuditEntry(String entry) throws OrderBookPersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch (IOException e) {
            throw new OrderBookPersistenceException("Could not persist audit information.", e);
        } // Time stamp along with entry persisted
        LocalDateTime timestamp = LocalDateTime.now(); out.println(timestamp.toString() + " : " + entry); out.flush();
    }
}

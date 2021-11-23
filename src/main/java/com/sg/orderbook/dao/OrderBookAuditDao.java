/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

/**
 *
 * @author calebdiaz
 */
public interface OrderBookAuditDao {
    /**
     * Takes in entry and writes to audit log.
     * 
     * @param entry
     * @throws OrderBookPersistenceException 
     */
    public void writeAuditEntry(String entry) throws OrderBookPersistenceException;
}
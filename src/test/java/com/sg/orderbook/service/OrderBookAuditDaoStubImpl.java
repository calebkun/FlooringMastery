/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.service;

import com.sg.orderbook.dao.OrderBookAuditDao;
import com.sg.orderbook.dao.OrderBookPersistenceException;

/**
 *
 * @author calebdiaz
 */
public class OrderBookAuditDaoStubImpl implements OrderBookAuditDao {

    @Override
    public void writeAuditEntry(String entry) throws OrderBookPersistenceException {
        // do nothing
    }
    
}

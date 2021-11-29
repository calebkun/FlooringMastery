/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.service;

import com.sg.orderbook.dao.OrderBookPersistenceException;
import com.sg.orderbook.dao.OrderBookTaxDao;
import com.sg.orderbook.dto.Tax;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author calebdiaz
 */
public class OrderBookTaxDaoStubImpl implements OrderBookTaxDao {

    Tax onlyTax;
    
    public OrderBookTaxDaoStubImpl(){
        BigDecimal taxRate = new BigDecimal("25.00");
        taxRate.setScale(2, RoundingMode.HALF_UP);
        
        this.onlyTax = new Tax("CA", "California", taxRate);
    }
    
    public OrderBookTaxDaoStubImpl(Tax aTax){
        this.onlyTax = aTax;
    }
    
    @Override
    public Tax getTax(String state) throws OrderBookPersistenceException {
        if(state.equals(onlyTax.getStateName())){
            return onlyTax;
        } else {
            return null;
        }
    }
    
}

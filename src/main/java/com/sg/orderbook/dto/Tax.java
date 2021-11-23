/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dto;

import java.math.BigDecimal;

/**
 *
 * @author calebdiaz
 */
public class Tax {
    private String stateAbbreviation;
    private String stateName;
    private BigDecimal taxRate;
    
    public Tax(String stateAbbreviation, String stateName, BigDecimal taxRate){
        this.stateAbbreviation = stateAbbreviation;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }
    
    // Getters
    
    public String getStateAbbreviation(){
        return this.stateAbbreviation;
    }
    
    public String getStateName(){
        return this.stateName;
    }
    
    public BigDecimal getTaxRate(){
        return this.taxRate;
    }


}

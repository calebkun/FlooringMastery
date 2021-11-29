/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dto;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.stateAbbreviation);
        hash = 67 * hash + Objects.hashCode(this.stateName);
        hash = 67 * hash + Objects.hashCode(this.taxRate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tax other = (Tax) obj;
        if (!Objects.equals(this.stateAbbreviation, other.stateAbbreviation)) {
            return false;
        }
        if (!Objects.equals(this.stateName, other.stateName)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tax{" + "stateAbbreviation=" + stateAbbreviation + ", stateName=" + stateName + ", taxRate=" + taxRate + '}';
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

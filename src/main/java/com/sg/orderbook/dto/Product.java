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
public class Product {
    private String productType;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    
    public Product(String productType, BigDecimal costPerSquareFoot, BigDecimal laborCostPerSquareFoot){
        this.productType = productType;
        this.costPerSquareFoot = costPerSquareFoot;
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.productType);
        hash = 17 * hash + Objects.hashCode(this.costPerSquareFoot);
        hash = 17 * hash + Objects.hashCode(this.laborCostPerSquareFoot);
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
        final Product other = (Product) obj;
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.costPerSquareFoot, other.costPerSquareFoot)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSquareFoot, other.laborCostPerSquareFoot)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Product{" + "productType=" + productType + ", costPerSquareFoot=" + costPerSquareFoot + ", laborCostPerSquareFoot=" + laborCostPerSquareFoot + '}';
    }
    
    
    
    // Getters
    
    public String getProductType(){
        return this.productType;
    }
    
    public BigDecimal getCostPerSquareFoot(){
        return this.costPerSquareFoot;
    }
    
    public BigDecimal getLaborCostPerSquareFoot(){
        return this.laborCostPerSquareFoot;
    }


}

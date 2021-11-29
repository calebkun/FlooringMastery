/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

/**
 *
 * @author calebdiaz
 */
public class Order {
    private LocalDate orderDate;
    private int orderNumber;
    private String customerName;
    private String state;
    private String productType;
    private BigDecimal area;
    private BigDecimal taxRate;
    private BigDecimal costPerSquareFoot;
    private BigDecimal laborCostPerSquareFoot;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal total;
    
    /**
     * Constructor for Order object.
     * 
     * @param orderDate
     * @param customerName
     * @param state
     * @param productType
     * @param area 
     */
    public Order(LocalDate orderDate, String customerName, String state, String productType, BigDecimal area){
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.state = state;
        this.productType = productType;
        this.area = area;
    }
    
    /**
     * Calculates material cost using cost per sq ft and area
     */
    private void calculateMaterialCost(){
        BigDecimal materialCost = this.area.multiply(this.costPerSquareFoot);
        materialCost.setScale(2, RoundingMode.HALF_UP);
        this.materialCost = materialCost;
    }
    
    /**
     * Calculates labor cost using labor cost per sq ft and area
     */
    private void calculateLaborCost(){
        BigDecimal laborCost = this.area.multiply(this.laborCostPerSquareFoot);
        laborCost.setScale(2, RoundingMode.HALF_UP);
        this.laborCost = laborCost;
    }
    
    /**
     * Calculates tax using tax rate, material cost and labor cost
     */
    private void calculateTax(){
        BigDecimal tax = this.taxRate.multiply(this.materialCost.add(this.laborCost));
        tax.setScale(2, RoundingMode.HALF_UP);
        this.tax = tax;
    }
    
    /**
     * calculates total using tax, labor cost and material cost
     */
    private void calculateTotal(){
        BigDecimal total = this.tax.add(this.materialCost.add(this.laborCost));
        total.setScale(2, RoundingMode.HALF_UP);
        this.total = total;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.orderDate);
        hash = 73 * hash + this.orderNumber;
        hash = 73 * hash + Objects.hashCode(this.customerName);
        hash = 73 * hash + Objects.hashCode(this.state);
        hash = 73 * hash + Objects.hashCode(this.productType);
        hash = 73 * hash + Objects.hashCode(this.area);
        hash = 73 * hash + Objects.hashCode(this.taxRate);
        hash = 73 * hash + Objects.hashCode(this.costPerSquareFoot);
        hash = 73 * hash + Objects.hashCode(this.laborCostPerSquareFoot);
        hash = 73 * hash + Objects.hashCode(this.materialCost);
        hash = 73 * hash + Objects.hashCode(this.laborCost);
        hash = 73 * hash + Objects.hashCode(this.tax);
        hash = 73 * hash + Objects.hashCode(this.total);
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
        final Order other = (Order) obj;
        if (this.orderNumber != other.orderNumber) {
            return false;
        }
        if (!Objects.equals(this.customerName, other.customerName)) {
            return false;
        }
        if (!Objects.equals(this.state, other.state)) {
            return false;
        }
        if (!Objects.equals(this.productType, other.productType)) {
            return false;
        }
        if (!Objects.equals(this.orderDate, other.orderDate)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.taxRate, other.taxRate)) {
            return false;
        }
        if (!Objects.equals(this.costPerSquareFoot, other.costPerSquareFoot)) {
            return false;
        }
        if (!Objects.equals(this.laborCostPerSquareFoot, other.laborCostPerSquareFoot)) {
            return false;
        }
        if (!Objects.equals(this.materialCost, other.materialCost)) {
            return false;
        }
        if (!Objects.equals(this.laborCost, other.laborCost)) {
            return false;
        }
        if (!Objects.equals(this.tax, other.tax)) {
            return false;
        }
        if (!Objects.equals(this.total, other.total)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order{" + "orderDate=" + orderDate + ", orderNumber=" + orderNumber + ", customerName=" + customerName + ", state=" + state + ", productType=" + productType + ", area=" + area + ", taxRate=" + taxRate + ", costPerSquareFoot=" + costPerSquareFoot + ", laborCostPerSquareFoot=" + laborCostPerSquareFoot + ", materialCost=" + materialCost + ", laborCost=" + laborCost + ", tax=" + tax + ", total=" + total + '}';
    }

    
    
    
    /**
     * Carries out calculations using member helper methods
     */
    public void CalculateFields(){
        calculateMaterialCost();
        calculateLaborCost();
        calculateTax();
        calculateTotal();
    }
    
    // Getters and Setters
    
    public LocalDate getOrderDate(){
        return this.orderDate;
    }
    
    public int getOrderNumber(){
        return this.orderNumber;
    }
    
    public void setOrderNumber(int orderNumber){
        this.orderNumber = orderNumber;
    }
    
    public String getCustomerName(){
        return this.customerName;
    }
    
    public void setCustomerName(String customerName){
        this.customerName = customerName;
    }
    
    public String getState(){
        return this.state;
    }
    
    public void setState(String state){
        this.state = state;
    }
    
    public String getProductType(){
        return this.productType;
    }
    
    public void setProductType(String productType){
        this.productType = productType;
    }
    
    public BigDecimal getArea(){
        return this.area;
    }
    
    public void setArea(BigDecimal area){
        this.area = area;
    }
    
    public BigDecimal getTaxRate(){
        return this.taxRate;
    }
    
    public void setTaxRate(BigDecimal taxRate){
        this.taxRate = taxRate;
    }
    
    public BigDecimal getCostPerSquareFoot(){
        return this.costPerSquareFoot;
    }
    
    public void setCostPerSquareFoot(BigDecimal costPerSquareFoot){
        this.costPerSquareFoot = costPerSquareFoot;
    }
    
    public BigDecimal getLaborCostPerSquareFoot(){
        return this.laborCostPerSquareFoot;
    }
    
    public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot){
        this.laborCostPerSquareFoot = laborCostPerSquareFoot;
    }
    
    public BigDecimal getMaterialCost(){
        return this.materialCost;
    }
    
    public void setMaterialCost(BigDecimal materialCost){
        this.materialCost = materialCost;
    }

    public BigDecimal getLaborCost(){
        return this.laborCost;
    }
    
    public void setLaborCost(BigDecimal laborCost){
        this.laborCost = laborCost;
    }
    
    public BigDecimal getTax(){
        return this.tax;
    }
    
    public void setTax(BigDecimal tax){
        this.tax = tax;
    }
    
    public BigDecimal getTotal(){
        return this.total;
    }
    
    public void setTotal(BigDecimal total){
        this.total = total;
    }
}

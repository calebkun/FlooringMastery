/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.service;

import com.sg.orderbook.dao.OrderBookPersistenceException;
import com.sg.orderbook.dao.OrderBookProductDao;
import com.sg.orderbook.dto.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author calebdiaz
 */
public class OrderBookProductDaoStubImpl implements OrderBookProductDao {

    Product onlyProduct;
    
    public OrderBookProductDaoStubImpl(){
        BigDecimal costPerSquareFoot = new BigDecimal("2.25");
        costPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        
        BigDecimal laborCostPerSquareFoot = new BigDecimal("2.10");
        laborCostPerSquareFoot.setScale(2, RoundingMode.HALF_UP);
        
        this.onlyProduct = new Product("Carpet", costPerSquareFoot, laborCostPerSquareFoot);
    }
    
    public OrderBookProductDaoStubImpl(Product aProduct){
        this.onlyProduct = aProduct;
    }
    
    @Override
    public Product getProduct(String productType) throws OrderBookPersistenceException {
        if(productType.equals(onlyProduct.getProductType())){
            return onlyProduct;
        } else {
            return null;
        }
    }

    @Override
    public List<Product> getProducts() throws OrderBookPersistenceException {
        List<Product> products = new ArrayList<>();
        products.add(onlyProduct);
        return products;
    }
    
}

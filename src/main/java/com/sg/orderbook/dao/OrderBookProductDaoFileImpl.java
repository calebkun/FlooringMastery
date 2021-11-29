/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author calebdiaz
 */
public class OrderBookProductDaoFileImpl implements OrderBookProductDao {

    // constants used for file persistence
    public final String PRODUCTS_FILE;
    public static final String DELIMITER = "::";
    
    // map containing records for our Products with product type as key
    private Map<String, Product> products = new HashMap<>();
    
    public OrderBookProductDaoFileImpl(){
        this.PRODUCTS_FILE = "Data/products.txt";
    }
    
    // overloaded constructor to allow for different inventory file to be injected
    public OrderBookProductDaoFileImpl(String file_name){
        this.PRODUCTS_FILE = file_name;
    }
    
    /**
     * Helper function which takes in a String object and uses it to create a 
     * BigDecimal object with appropriate scale and rounding.
     * 
     * @param value - String object to be used in BigDecimal constructor
     * @return BigDecimal value
     */
    private BigDecimal toBigDecimal(String value){
        BigDecimal bd = new BigDecimal(value);
        bd.setScale(2, RoundingMode.HALF_UP);
        return bd;
    }
    
     /**
     * Responsible for taking a line from our file and parsing the line to
     * extract info needed to create a corresponding product object.
     * 
     * Format:
     * ProductType::CostPerSquareFoot::LaborCostPerSquareFoot
     * 
     * Example:
     * Tile::3.50::4.15
     * 
     * @param productAsText
     * @return productFromFile
     */
    private Product unmarshallProduct(String productAsText){
        
        String[] productTokens = productAsText.split(DELIMITER);

        // Given the pattern above, the product number is in index 0 of the array.
        String productType = productTokens[0];
        BigDecimal costPerSquareFoot = toBigDecimal(productTokens[1]);
        BigDecimal laborCostPerSquareFoot = toBigDecimal(productTokens[2]);

        // Which we can then use to create a new product object to satisfy
        // the requirements of the product constructor.
        Product productFromFile = new Product(productType, costPerSquareFoot, laborCostPerSquareFoot);

        // We have now created a product!
        return productFromFile;
    }
    
    private void loadProducts() throws OrderBookPersistenceException {
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderBookPersistenceException(
                    "-_- Could not load product data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentItem holds the most recent item unmarshalled
        Product currentProduct;

        // Skip header
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Item
            currentProduct = unmarshallProduct(currentLine);

            // Put currentItem into the map using title as the key
            products.put(currentProduct.getProductType(), currentProduct);
        }
        // close scanner
        scanner.close();
    }

    @Override
    public Product getProduct(String productType) throws OrderBookPersistenceException {
        loadProducts();
        return products.get(productType);
    }

    @Override
    public List<Product> getProducts() throws OrderBookPersistenceException {
        loadProducts();
        return new ArrayList<Product>(products.values());
    }
    
    
    
}

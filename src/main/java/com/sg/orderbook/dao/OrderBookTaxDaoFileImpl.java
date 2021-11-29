/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Tax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author calebdiaz
 */
public class OrderBookTaxDaoFileImpl implements OrderBookTaxDao {

    // constants used for file persistence
    public final String TAXES_FILE;
    public static final String DELIMITER = "::";
    
    // map containing records for our Taxes with state as key
    private Map<String, Tax> taxes = new HashMap<>();
    
    public OrderBookTaxDaoFileImpl(){
        this.TAXES_FILE = "Data/taxes.txt";
    }
    
    // overloaded constructor to allow for different inventory file to be injected
    public OrderBookTaxDaoFileImpl(String file_name){
        this.TAXES_FILE = file_name;
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
     * extract info needed to create a corresponding Tax object.
     * 
     * Format:
     * StateAbbreviation::StateName::TaxRate
     * 
     * Example:
     * TX::Texas::4.45
     * 
     * @param taxAsText
     * @return taxFromFile
     */
    private Tax unmarshallTax(String taxAsText){
        
        String[] taxTokens = taxAsText.split(DELIMITER);

        // Given the pattern above, the Tax number is in index 0 of the array.
        String stateAbbreviation = taxTokens[0];
        String stateName = taxTokens[1];
        BigDecimal taxRate = toBigDecimal(taxTokens[2]);

        // Which we can then use to create a new Tax object to satisfy
        // the requirements of the Tax constructor.
        Tax TaxFromFile = new Tax(stateAbbreviation, stateName, taxRate);

        // We have now created a Tax!
        return TaxFromFile;
    }
    
    private void loadTaxes() throws OrderBookPersistenceException {
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAXES_FILE)));
        } catch (FileNotFoundException e) {
            throw new OrderBookPersistenceException(
                    "-_- Could not load tax data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentTax holds the most recent Tax unmarshalled
        Tax currentTax;
        // Go through TAXES_FILE line by line, decoding each line into a 
        // Tax object by calling the unmarshallTax method.
        // Process while we have more lines in the file
        
        // Skip header
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Tax
            currentTax = unmarshallTax(currentLine);

            // Put currentTax into the map using title as the key
            taxes.put(currentTax.getStateName(), currentTax);
        }
        // close scanner
        scanner.close();
    }
    
    @Override
    public Tax getTax(String state) throws OrderBookPersistenceException {
        loadTaxes();
        return taxes.get(state);
    }
    
}

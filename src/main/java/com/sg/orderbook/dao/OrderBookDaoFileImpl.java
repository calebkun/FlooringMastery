/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.dao;

import com.sg.orderbook.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author calebdiaz
 */
public class OrderBookDaoFileImpl implements OrderBookDao {

    // constants used for file persistence
    public final String ORDERBOOK_FILE;
    public static final String DELIMITER = "::";
    public static final String HEADER = "OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total";
    
    // private member to track next order number
    private int nextOrder = 1;
    
    // map containing records for our Orders with order number as key
    private Map<Integer, Order> orderBook = new HashMap<>();
    
    
    public OrderBookDaoFileImpl(){
        this.ORDERBOOK_FILE = "Orders_";
    }
    
    // overloaded constructor to allow for different orderBook file to be injected
    public OrderBookDaoFileImpl(String file_name){
        this.ORDERBOOK_FILE = file_name;
    }
    
    /**
     * Helper method which takes a LocalDate object, converts it to a String of the
     * appropriate format for our orders file name, and returns a String of the appropriate
     * file name and path.
     * 
     * @param date - LocalDate to convert
     * @return String containing file path
     */
    private String dateToPath(LocalDate date){
        String dateAsString = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        String dateAsPath = ORDERBOOK_FILE+dateAsString+".txt";
        return dateAsPath;
    }
    
    /**
     * Helper object which takes a LocalDate object, calls dateToPath() to create the
     * appropriate file path, and then create the file if it does not already exist. Returns
     * the file path to be read from/written to, or throws exception.
     * 
     * @param date - LocalDate to pass to dateToPath
     * @return String with file path
     * @throws OrderBookPersistenceException 
     */
    private String processDate(LocalDate date) throws OrderBookPersistenceException{
        String dateAsPath = dateToPath(date);
        try {
            File myObj = new File(dateAsPath);
            if (myObj.createNewFile()) {
                PrintWriter out;
                try {
                    out = new PrintWriter(new FileWriter(dateAsPath));
                } catch (IOException e) {
                    throw new OrderBookPersistenceException(
                            "Could not save order data.", e);
                }

                // Add header
                out.println(HEADER);
                out.flush();
              return dateAsPath;
            } else {
              return dateAsPath;
            }
        } catch (IOException e) {
          throw new OrderBookPersistenceException("Order file "+dateAsPath+" could not be opened.");
        }
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
     * extract info needed to create a corresponding Order object.
     * 
     * Format:
     * OrderNumber::CustomerName::State::TaxRate::ProductType::Area::CostPerSquareFoot::LaborCostPerSquareFoot::MaterialCost::LaborCost::Tax::Total
     * 
     * Example:
     * 1::Ada Lovelace::CA::25.00::Tile::249.00::3.50::4.15::871.50::1033.35::476.21::2381.06
     * 
     * @param OrderAsText
     * @return OrderFromFile - order object created from info extracted from file
     */
    private Order unmarshallOrder(String orderAsText, LocalDate date){
         
        String[] orderTokens = orderAsText.split(DELIMITER);

        // Given the pattern above, the Order number is in index 0 of the array.
        String customerName = orderTokens[1];
        String state = orderTokens[2];
        String productType = orderTokens[4];
        BigDecimal area = toBigDecimal(orderTokens[5]);

        // Which we can then use to create a new Order object to satisfy
        // the requirements of the Order constructor.
        Order orderFromFile = new Order(date, customerName, state, productType, area);

        // However, there are 3 remaining tokens that need to be set into the
        // new Order object. We do this manually by using the appropriate setters.
        
        orderFromFile.setOrderNumber(Integer.valueOf(orderTokens[0]));
        orderFromFile.setTaxRate(toBigDecimal(orderTokens[3]));
        orderFromFile.setCostPerSquareFoot(toBigDecimal(orderTokens[6]));
        orderFromFile.setLaborCostPerSquareFoot(toBigDecimal(orderTokens[7]));
        orderFromFile.setMaterialCost(toBigDecimal(orderTokens[8]));
        orderFromFile.setLaborCost(toBigDecimal(orderTokens[9]));
        orderFromFile.setTax(toBigDecimal(orderTokens[10]));
        orderFromFile.setTotal(toBigDecimal(orderTokens[11]));

        // We have now created a Order!
        return orderFromFile;
    }
    
    /**
     * Responsible for taking a Order object and translating it's member fields
     * into the appropriate format to be stored in our file. The string of the appropriate
     * file is then passed as input to unmarshallOrder()
     * 
     * @param anOrder
     * @return OrderAsText
     */
    private String marshallOrder(Order anOrder){

        // We get out each property, and concatenate with our DELIMITER as a kind of spacer. 

        // Start with the Order number, since that's supposed to be first.
        String OrderAsText = String.valueOf(anOrder.getOrderNumber()) + DELIMITER;

        // add the rest of the properties in the correct order:
        
        OrderAsText += anOrder.getCustomerName() + DELIMITER;
        
        OrderAsText += anOrder.getState() + DELIMITER;
        
        OrderAsText += anOrder.getTaxRate().toString() + DELIMITER;
        
        OrderAsText += anOrder.getProductType() + DELIMITER;

        OrderAsText += anOrder.getArea().toString() + DELIMITER;

        OrderAsText += anOrder.getCostPerSquareFoot().toString() + DELIMITER;

        OrderAsText += anOrder.getLaborCostPerSquareFoot().toString() + DELIMITER;

        OrderAsText += anOrder.getMaterialCost().toString() + DELIMITER;

        OrderAsText += anOrder.getLaborCost().toString() + DELIMITER;

        OrderAsText += anOrder.getTax().toString() + DELIMITER;
        
        // orderBook -- skip delimiter
        OrderAsText += anOrder.getTotal().toString();

        // We have now turned an Order to text! Return it!
        return OrderAsText;
    }

    /**
     * Creates Scanner object to open persistence file if it exists 
     * and parses each line, calling unmarshallOrder() on each to create Order objects
     * 
     * @throws OrderBookPersistenceException 
     */
    private void loadOrderBook(LocalDate date) throws OrderBookPersistenceException {
        String filePath = processDate(date);
        
        Scanner scanner;

        try {
            // Create Scanner for reading the file
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(filePath)));
        } catch (FileNotFoundException e) {
            throw new OrderBookPersistenceException(
                    "-_- Could not load order book data into memory.", e);
        }
        // currentLine holds the most recent line read from the file
        String currentLine;
        // currentOrder holds the most recent Order unmarshalled
        Order currentOrder;
        // Go through orderBook_FILE line by line, decoding each line into a 
        // Order object by calling the unmarshallOrder method.
        // Process while we have more lines in the file
        
        // Skip header
        scanner.nextLine();
        
        // Clear local orders map
        orderBook.clear();
        
        while (scanner.hasNextLine()) {
            // get the next line in the file
            currentLine = scanner.nextLine();
            // unmarshall the line into a Order
            currentOrder = unmarshallOrder(currentLine, date);

            // Put currentOrder into the map using title as the key
            orderBook.put(currentOrder.getOrderNumber(), currentOrder);
            
            // Update next order
            nextOrder = currentOrder.getOrderNumber()+1;
        }
        // close scanner
        scanner.close();
    }
    
    /**
    * Writes all Orders in the library out to a orderBook_FILE.  See loadorderBook
    * for file format.
    * 
    * @throws OrderBookDaoException if an error occurs writing to the file
    */
    private void writeOrderBook(LocalDate date) throws OrderBookPersistenceException {
        // We are not handling the IOException - but
        // we are translating it to an application specific exception and 
        // then simple throwing it (i.e. 'reporting' it) to the code that
        // called us.  It is the responsibility of the calling code to 
        // handle any errors that occur.
        
        String filePath = processDate(date);
        
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(filePath));
        } catch (IOException e) {
            throw new OrderBookPersistenceException(
                    "Could not save order data.", e);
        }

        // Add header
        out.println(HEADER);
        out.flush();
        
        // Write out the Order objects to the orderBook file.
        String OrderAsText;
        List<Order> OrderList = this.getAllOrdersHelper();
        for (Order currentOrder : OrderList) {
            // turn an Order into a String
            OrderAsText = marshallOrder(currentOrder);
            // write the Order object to the file
            out.println(OrderAsText);
            // force PrintWriter to write line to the file
            out.flush();
        }
        // Clean up
        out.close();
    }
    
    /**
     * Helper function for writeOrderBook which gets all orders in local memory.
     * Meant to be called only after loadOrderBook in order to provide accurate
     * record of orders.
     * 
     * @return
     * @throws OrderBookPersistenceException 
     */
    private List<Order> getAllOrdersHelper() throws OrderBookPersistenceException {
        return new ArrayList<Order>(orderBook.values());
    }
    
    @Override
    public Order addOrder(Order order) throws OrderBookPersistenceException {
        loadOrderBook(order.getOrderDate());
        order.setOrderNumber(nextOrder);
        orderBook.put(order.getOrderNumber(), order);
        writeOrderBook(order.getOrderDate());
        return order;
    }

    @Override
    public Order editOrder(Order order) throws OrderBookPersistenceException {
        loadOrderBook(order.getOrderDate());
        order.CalculateFields();
        orderBook.put(order.getOrderNumber(), order);
        writeOrderBook(order.getOrderDate());
        return order;
    }

    @Override
    public Order removeOrder(Order order) throws OrderBookPersistenceException {
        loadOrderBook(order.getOrderDate());
        Order removedOrder = orderBook.remove(order.getOrderNumber()); 
        writeOrderBook(order.getOrderDate());
        return removedOrder;
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws OrderBookPersistenceException {
        loadOrderBook(orderDate);
        return orderBook.get(orderNumber);
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws OrderBookPersistenceException {
        loadOrderBook(orderDate);
        return new ArrayList<Order>(orderBook.values());
    }
    
}

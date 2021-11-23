/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.ui;

import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author calebdiaz
 */
public class OrderBookView {
    
    final UserIO io;

    public OrderBookView(UserIO io){
        this.io = io;
    }
    
// Methods for getting user input
    
    /**
     * Prints the menu to the console and prompts the user for a selection.
     * 
     * @return int selection - selection from user input
     */
    public int printMenuAndGetSelection(){
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("*");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the above choices.", 1, 6);
    }
    
    /**
     * Prompts the user for their choice of date and returns a LocalDate object.
     * 
     * @return LocalDate - LocalDate object created from user input containing order date
     */
    public LocalDate getDateChoice(){
        return io.readLocalDate("Please enter the order date: <MM/dd/yyyy>");
    }
    
    /**
     * Prompts the user to input an order number and returns an int.
     * 
     * @return int order number from user input
     */
    public int getOrderNumber(){
        return io.readInt("Please enter the order number: ");
    }
    
    /**
     * Prompts the user to input information for placing an order and returns
     * an order object with the information.
     * 
     * @return Order object created from user input
     */
    public Order getNewOrderInfo(){
        LocalDate date = io.readLocalDate("Please enter the order date: <MM/dd/yyyy>");
        String customerName = io.readString("Please enter your name: ");
        String state = io.readString("Please enter the state: ");
        String productType = io.readString("Please enter the product type: ");
        BigDecimal area = io.readBigDecimal("Please enter the area in square feet: <Minimum 100 sq. ft.>");
        
        Order order = new Order(date, customerName, state, productType, area);

        return order;
    }
    
    public int getAddOrderConfirmation(){
        io.print("Place order?");
        io.print("1. Yes");
        io.print("2. No");
        return io.readInt("Please select an option: ", 1, 2);
    }
    
    /**
     * Takes in an order object, prompts the user to update information for 
     * the order, and returns an order object with the updated information.
     * 
     * @param orderToEdit - order object selected by user to edit
     * @return Order object with updated information created from user input
     */
    public Order getEditOrderInfo(Order orderToEdit){
        if(orderToEdit != null){
            String editedName = io.readString("Please enter customer name ("+orderToEdit.getCustomerName()+"):");
            String editedState = io.readString("Please enter state ("+orderToEdit.getState()+"):");
            String editedProductType = io.readString("Please enter product type ("+orderToEdit.getProductType()+"):");
            BigDecimal editedArea = getEditAreaHelper(orderToEdit.getArea());
            
            io.print("Review changes:");
            io.print("* Name: "+((editedName.trim().isEmpty()) ? orderToEdit.getCustomerName() : editedName));
            io.print("* State: "+((editedState.trim().isEmpty()) ? orderToEdit.getState() : editedState));
            io.print("* Product Type: "+((editedProductType.trim().isEmpty()) ? orderToEdit.getProductType() : editedProductType));
            io.print("* Area: "+((editedArea == null) ? orderToEdit.getArea() : editedArea));
            io.print("");

            io.print("Keep changes?");
            io.print("1. Yes");
            io.print("2. No");
            int selection = io.readInt("Please select an option: ", 1, 2);
            switch(selection){
                case 1:
                    Order edited = new Order(orderToEdit.getOrderDate(), 
                            (editedName.trim().isEmpty()) ? orderToEdit.getCustomerName() : editedName, 
                            (editedState.trim().isEmpty()) ? orderToEdit.getState() : editedState, 
                            (editedProductType.trim().isEmpty()) ? orderToEdit.getProductType() : editedProductType, 
                            (editedArea == null) ? orderToEdit.getArea() : editedArea);
                    edited.setOrderNumber(orderToEdit.getOrderNumber());
                    return edited;
                case 2:
                    return orderToEdit;
            }
        } else {
            io.print("No such order exists.");
        }
        return null;
    }
    
    public int getRemoveConfirmation(){
        io.print("Are you sure you would like to remove the order?");
        io.print("1. Yes");
        io.print("2. No");
        return io.readInt("Please select an option: ", 1, 2);
    }
    
    /**
     * Helper function for getEditOrderInfo which prompts user to enter edit for
     * order area. If user presses enter, methods returns null. If user provides input,
     * continues to reprompt user until input is of correct format to create a 
     * BigDecimal object and then returns the BigDecimal object.
     * 
     * @param area - current area value to display to user
     * @return BigDecimal edited area or null
     */
    private BigDecimal getEditAreaHelper(BigDecimal area){
        boolean invalidInput = true;
            while (invalidInput) {
                try {
                    // print the message msgPrompt (ex: asking for the # of cats!)
                    String editedArea = io.readString("Please enter area ("+area+"):");
                    if(!editedArea.trim().isEmpty()){
                         // Get the input line, and try and parse
                        BigDecimal editedAreaBD = new BigDecimal(editedArea); // if it's 'bob' it'll break
                        editedAreaBD.setScale(2, RoundingMode.HALF_UP);
                        invalidInput = false;
                        return editedAreaBD;
                    } else {
                        invalidInput = false;
                    }
                } catch (NumberFormatException e) {
                    io.print("Input error. Please try again.");
                }
            }
        return null;
    }
    
    
// Methods for displaying information to user
    
    /**
     * Takes in a list of Product objects and displays it to the user.
     * 
     * @param products - list of Product objects
     */
    public void displayProducts(List<Product> products){
        io.print("");
        io.print("Available products: ");
        io.print("Product Type: Cost per sq.ft. ($), Labor cost per sq.ft. ($)");
        for (Product currentProduct : products) {
            String productInfo = String.format("%s: %s, %s",
                  currentProduct.getProductType(),
                  currentProduct.getCostPerSquareFoot(),
                  currentProduct.getLaborCostPerSquareFoot());
            io.print(productInfo);
        }
        io.print("");
    }
    
    /**
     * Takes in a list of Order objects and displays it to the user.
     * 
     * @param orders - list of Order objects
     */
    public void displayOrders(List<Order> orders){
        if (orders.size() != 0){
            for (Order currentOrder : orders) {
                String orderInfo = String.format("%d : %s %s",
                      currentOrder.getOrderNumber(),
                      currentOrder.getCustomerName(),
                      currentOrder.getProductType());
                io.print(orderInfo);
            }
        } else {
            io.print("No orders exist for this date.");
        }
        io.readString("Please hit enter to continue.");
    }
    
    /**
     * Takes in an Order object and displays information of the order.
     * 
     * @param order object to be displayed
     */
    public void displayOrder(Order order){
        //temp implementation
        if (order != null) {
            io.print("");
            io.print("ORDER #"+String.valueOf(order.getOrderNumber())); 
            io.print("* Order date: "+order.getOrderDate());
            io.print("* Customer name: "+order.getCustomerName());
            io.print("* State: "+order.getState());
            io.print("* Tax Rate: "+order.getTaxRate());
            io.print("* Product type: "+order.getProductType());
            io.print("* CostPerSquareFoot: "+order.getCostPerSquareFoot());
            io.print("* LaborCostPerSquareFoot: "+order.getLaborCostPerSquareFoot());
            io.print("* MaterialCost: "+order.getMaterialCost());
            io.print("* LaborCost: "+order.getLaborCost());
            io.print("* Tax: "+order.getTax());
            io.print("* Total: "+order.getTotal());
            io.print("* Area: "+order.getArea());
            io.print("");
        } else {
            io.print("No such order.");
            io.print("");
        }
    }

    
// Methods for displaying alerts to user
    
    public void displayAddOrderBanner(){
        io.print("");
        io.print("=== Add Order ===");
    }
    
    public void displayOrderSummaryBanner(){
        io.print("===Order Summary===");
    }
    
    public void displayCreateSuccessBanner(){
        io.print("");
        io.readString("Successfully added order! Please hit enter to continue.");
    }
    
    public void displayOrderNotPlacedBanner(){
        io.readString("Order was not placed. Please hit enter to continue.");
    }
    
    public void displayDisplayOrdersBanner(LocalDate date){
        io.print("");
        io.print("=== Display Orders "+date+" ===");
    }
    
    public void displayEditOrderBanner(){
        io.print("");
        io.print("=== Edit Order ===");
    }
    
    public void displayEditOrderSuccessBanner(){
        io.print("");
        io.readString("Successfully edited order! Please hit enter to continue.");
    }
    
    public void displayEditNotSavedBanner(){
        io.print("");
        io.readString("Edited information was not saved. Please hit enter to continue.");
    }
    
    public void displayRemoveOrderBanner(){
        io.print("");
        io.print("=== Remove Order ===");
    }
    
    public void displayRemoveOrderSuccessBanner(){
        io.print("");
        io.readString("Successfully removed order! Please hit enter to continue.");
    }
    
    public void displayDidNotRemoveOrderBanner(){
        io.print("");
        io.readString("Order was not removed. Please hit enter to continue.");
    }
    
    public void displayExportDataSuccessBanner(){
        io.print("");
        io.readString("Successfully exported data! Please hit enter to continue.");
    }
    
    public void displayErrorMessage(String errorMsg){
        io.print("");
        io.readString(errorMsg+" Please hit enter to continue.");
    }
    
    public void displayExitBanner(){
        io.print("");
        io.print("Goodbye!");
    }
    
    public void displayUnknownCommandBanner(){
        io.print("");
        io.readString("Unknown command. Please hit enter to continue.");
    }
    
}

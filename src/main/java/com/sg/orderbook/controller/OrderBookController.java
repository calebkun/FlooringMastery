/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.controller;

import com.sg.orderbook.dao.OrderBookPersistenceException;
import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Product;
import com.sg.orderbook.service.OrderBookIncompleteOrderException;
import com.sg.orderbook.service.OrderBookInvalidAreaException;
import com.sg.orderbook.service.OrderBookInvalidDateException;
import com.sg.orderbook.service.OrderBookInvalidEditException;
import com.sg.orderbook.service.OrderBookProductException;
import com.sg.orderbook.service.OrderBookServiceLayer;
import com.sg.orderbook.service.OrderBookTaxException;
import com.sg.orderbook.ui.OrderBookView;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author calebdiaz
 */
public class OrderBookController {
    
    // Dependencies to inject
    private OrderBookView view;
    private OrderBookServiceLayer service;

    public OrderBookController(OrderBookView view, OrderBookServiceLayer service){
        this.view = view;
        this.service = service;
    }
    
    /**
     * Method to run our program
     * 
     */
    public void run(){
    boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
            exitMessage();
        } catch (OrderBookPersistenceException e){
            view.displayErrorMessage(e.getMessage());
        }
    }
    
    /**
     * Pass thru method to call view method to print and get menu selection
     * 
     * @return int menu selection
     */
    private int getMenuSelection(){
        return view.printMenuAndGetSelection();
    }
    
    /**
     * Coordinates view and service methods to create an order object from 
     * user input and add it.
     * 
     * @throws OrderBookPersistenceException 
     */
    private void addOrder() throws OrderBookPersistenceException {
        view.displayAddOrderBanner();
        
        List<Product> products = service.getProducts(); // display available products and pricing
        view.displayProducts(products);
        
        boolean hasErrors = false;
        do {
            Order currentOrder = view.getNewOrderInfo(); 
            try {
                Order validated = service.validateOrder(currentOrder);
                view.displayOrderSummaryBanner();
                view.displayOrder(validated); // display summary of order info to user
                int selection = view.getAddOrderConfirmation(); // confirm that user would like to place the order
                switch(selection){
                    case 1:
                        service.createOrder(validated);
                        view.displayCreateSuccessBanner();
                        break;
                    case 2:
                        view.displayOrderNotPlacedBanner();
                        break;
                }
                hasErrors = false;
            } catch (OrderBookInvalidAreaException | OrderBookInvalidDateException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookIncompleteOrderException e) { 
                hasErrors = false;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
    }
    
    /**
     * Coordinates view and service methods to edit an order from 
     * user input and persist changes.
     * 
     * @throws OrderBookPersistenceException 
     */
    private void editOrder() throws OrderBookPersistenceException {
        view.displayEditOrderBanner();
        LocalDate date = view.getDateChoice();
        int orderNumber = view.getOrderNumber();
        
        Order toEdit = service.getOrder(date, orderNumber); // gets order associated with date and order number
        if (toEdit != null){ // if order exists for order date and number
            Order currentOrder = view.getEditOrderInfo(toEdit);
            if (currentOrder != null){ // if user decided to save edited information
                try {
                    Order validated = service.validateOrder(currentOrder);
                    service.editOrder(toEdit, validated); 
                    view.displayEditOrderSuccessBanner();
                } catch (OrderBookInvalidAreaException | 
                        OrderBookProductException | OrderBookTaxException |
                        OrderBookInvalidDateException | OrderBookIncompleteOrderException |
                        OrderBookInvalidEditException e) { 
                    view.displayErrorMessage(e.getMessage());
                }
            } else { 
                view.displayEditNotSavedBanner();
            }
        } else {
            view.displayErrorMessage("No such order exists.");
        }
    }
    
    /**
     * Coordinates view and service methods to remove an order.
     * 
     * @throws OrderBookPersistenceException 
     */
    private void removeOrder() throws OrderBookPersistenceException {
        view.displayRemoveOrderBanner();
        
        LocalDate date = view.getDateChoice();
        int orderNumber = view.getOrderNumber();
        
        Order toRemove = service.getOrder(date, orderNumber);
        view.displayOrder(toRemove); // displays order info for user to review
        
        if(toRemove != null){
            int selection = view.getRemoveConfirmation(); // prompts user to confirm removal
            switch(selection){
                case 1:
                    service.removeOrder(date, orderNumber);
                    view.displayRemoveOrderSuccessBanner();
                    break;
                case 2:
                    view.displayDidNotRemoveOrderBanner();
                    break;
            }
        }
    }
    
    /**
     * Coordinates view and service methods to display all orders for a given date.
     * 
     * @throws OrderBookPersistenceException 
     */
    private void displayOrders() throws OrderBookPersistenceException {
        LocalDate date = view.getDateChoice();
        List<Order> orders = service.getAllOrders(date);
        if(orders != null){
            view.displayDisplayOrdersBanner(date);
            view.displayOrders(orders);
        } else {
            view.displayErrorMessage("No orders exist for this date.");
        }
    }
    
    /**
     * Pass thru method for view export data banner
     * 
     */
    private void exportData(){
        view.displayExportDataSuccessBanner();
    }
    
    /**
     * Pass thru method for view unknown command banner
     * 
     */
    private void unknownCommand(){
        view.displayUnknownCommandBanner();
    }
    
    /**
     * Pass thru method for view exit banner
     * 
     */
    private void exitMessage(){
        view.displayExitBanner();
    }

}

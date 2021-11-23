/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.controller;

import com.sg.orderbook.dao.OrderBookPersistenceException;
import com.sg.orderbook.dto.Order;
import com.sg.orderbook.dto.Product;
import com.sg.orderbook.service.OrderBookInvalidAreaException;
import com.sg.orderbook.service.OrderBookInvalidDateException;
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
    
    private OrderBookView view;
    private OrderBookServiceLayer service;

    public OrderBookController(OrderBookView view, OrderBookServiceLayer service){
        this.view = view;
        this.service = service;
    }
    
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
    
    private int getMenuSelection(){
        return view.printMenuAndGetSelection();
    }
    
    private void addOrder() throws OrderBookPersistenceException {
        view.displayAddOrderBanner();
        
        List<Product> products = service.getProducts();
        view.displayProducts(products);
        
        boolean hasErrors = false;
        do {
            Order currentOrder = view.getNewOrderInfo(); 
            try {
                Order validated = service.validateOrder(currentOrder);
                view.displayOrderSummaryBanner();
                view.displayOrder(validated);
                int selection = view.getAddOrderConfirmation();
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
                    OrderBookProductException | OrderBookTaxException e) { 
                hasErrors = false;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
    }
    
    private void editOrder() throws OrderBookPersistenceException {
        view.displayEditOrderBanner();
        LocalDate date = view.getDateChoice();
        int orderNumber = view.getOrderNumber();
        
        Order toEdit = service.getOrder(date, orderNumber);
        boolean hasErrors = false;
        do {
            try {
                Order currentOrder = view.getEditOrderInfo(toEdit);
                Order validated = service.validateOrder(currentOrder);
                if(currentOrder == null){
                    hasErrors = false;
                } else if (validated.equals(toEdit)){
                    view.displayEditNotSavedBanner();
                    hasErrors = false;
                } else {
                    service.editOrder(validated); 
                    view.displayEditOrderSuccessBanner();
                    hasErrors = false;
                }
            } catch (OrderBookInvalidAreaException | 
                    OrderBookProductException | OrderBookTaxException |
                    OrderBookInvalidDateException e) { 
                hasErrors = true;
                view.displayErrorMessage(e.getMessage());
            }
        } while (hasErrors);
    }
    
    private void removeOrder() throws OrderBookPersistenceException {
        view.displayRemoveOrderBanner();
        
        LocalDate date = view.getDateChoice();
        int orderNumber = view.getOrderNumber();
        
        Order toRemove = service.getOrder(date, orderNumber);
        view.displayOrder(toRemove);
        
        if(toRemove != null){
            int selection = view.getRemoveConfirmation();
            switch(selection){
                case 1:
                    service.removeOrder(toRemove);
                    view.displayRemoveOrderSuccessBanner();
                    break;
                case 2:
                    view.displayDidNotRemoveOrderBanner();
                    break;
            }
        }
    }
    
    private void displayOrders() throws OrderBookPersistenceException {
        LocalDate date = view.getDateChoice();
        List<Order> orders = service.getAllOrders(date);
        view.displayDisplayOrdersBanner(date);
        view.displayOrders(orders);
    }
    
    private void exportData(){
        view.displayExportDataSuccessBanner();
    }
    
    private void unknownCommand(){
        view.displayUnknownCommandBanner();
    }
    
    private void exitMessage(){
        view.displayExitBanner();
    }

}

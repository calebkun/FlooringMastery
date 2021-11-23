/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook;

import com.sg.orderbook.controller.OrderBookController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author calebdiaz
 */
public class App {
    public static void main(String args[]){
        
        ApplicationContext appContext
                = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        OrderBookController controller = appContext.getBean("controller", OrderBookController.class);
        controller.run();
    }
}

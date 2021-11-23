/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.orderbook.ui;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * User IO interface provided by mthree containing methods to read inputs of
 * different data types
 *
 * @author calebdiaz
 */
public interface UserIO {
    void print(String msg);

    double readDouble(String prompt);

    double readDouble(String prompt, double min, double max);

    float readFloat(String prompt);

    float readFloat(String prompt, float min, float max);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    long readLong(String prompt);

    long readLong(String prompt, long min, long max);
    
    LocalDate readLocalDate(String prompt); 
    
    BigDecimal readBigDecimal(String prompt);

    String readString(String prompt);
}


package com.example.java;

import com.example.java.bean.Stock;
import com.example.java.tables.StockTable;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws SQLException, IOException, IllegalArgumentException {

        //Read in JSON object & insert into database table
        ObjectMapper objectMapper = new ObjectMapper();

        List<Stock> stock = objectMapper.readValue(new URL("file:///Users/rohangrover/Desktop/week1-stocks.json"), new TypeReference<List<Stock>>(){});

        Stock bean = new Stock();

        for (int i = 0; i < stock.size() - 1; i++) {
            bean.setSymbol(stock.get(i).getSymbol());
            bean.setPrice(stock.get(i).getPrice());
            bean.setVolume(stock.get(i).getVolume());
            bean.setDate(stock.get(i).getDate());
            //StockTable.insert(bean);
        }
        //System.out.println("Data inserted into table!\n");

        //Retrieving aggregate data for stock information

        Date date = null;
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter date: ");

        do {
            try {
                date = Date.valueOf(sc.next());
                System.out.println("Stock Information for " + date);
                System.out.println("-----------------");

                StockTable.retrieve(date);

            } catch (IllegalArgumentException e) {
                System.out.print("Enter date again: ");
                sc.nextLine();
            }
        } while (date == null);





    }
}

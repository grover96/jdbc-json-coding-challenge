package com.example.java.tables;

import com.example.java.util.DBUtil;
import com.example.java.bean.Stock;

import javax.xml.transform.Result;
import java.sql.*;
import java.text.NumberFormat;

public class StockTable {

    public static void insert(Stock bean) throws SQLException {

        String query = "INSERT into stocks_tbl (symbol, price, volume, date) " +
                "VALUES (?, ?, ?, ?)";
        ResultSet keys = null;

        //Testing database connection
        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setString(1, bean.getSymbol());
            stmt.setFloat(2, bean.getPrice());
            stmt.setInt(3, bean.getVolume());
            stmt.setTimestamp(4, new java.sql.Timestamp(bean.getDate().getTime()));

            int affected = stmt.executeUpdate();

            if (affected == 1) {
                keys = stmt.getGeneratedKeys();
                keys.next();
                int newKey = keys.getInt(1);
                bean.setUniqueId(newKey);
            } else {
                System.err.println("No rows affected");
            }

        } catch (SQLException e) {
            System.err.println(e);
        } finally {
            if (keys != null) keys.close();
        }
    }


    public static void retrieve(Date date) {

        String query = "SELECT symbol, MAX(price) AS maxStockPrice, MIN(price) AS minStockPrice, SUM(volume) AS sum," +
                " date FROM data.stocks_tbl WHERE stocks_tbl.date ='" + date.toString() + "' GROUP BY symbol";
        ResultSet rs;

        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
        ) {
            rs = stmt.executeQuery(query);
            NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();

            while(rs.next()){
                String symbol = rs.getString("symbol");
                float maxPrice = rs.getFloat("maxStockPrice");
                float minPrice = rs.getFloat("minStockPrice");
                int volume = rs.getInt("sum");
                date = rs.getDate("date");

                System.out.println("Stock: " + symbol + " | Max Price: " + defaultFormat.format(maxPrice) + " | Min Price: " + defaultFormat.format(minPrice) +
                " | Volume: " + volume + " | Date: " + date + " |");
            }

        } catch (SQLException e) {
            System.err.println("Got an exception! ");
            System.err.println(e);
        }
    }
}

package com.technical.philliptrade.da_sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.ColorSpace;
import android.util.Log;

import com.technical.philliptrade.db_sqlite.DB_Base;
import com.technical.philliptrade.model.ModelTraderMarket;

import java.util.ArrayList;

public class DA_M_trade_market extends DB_Base {
    public static final String TABLE_NAME = "m_trade_market";
    public static final String STATEMENT_CREATE = "CREATE TABLE `" + TABLE_NAME + "` (" +
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT, `stock_name` TEXT NOT NULL, " +
            "`price` TEXT NOT NULL, " +
            "`chg` TEXT NOT NULL, `vol` TEXT NOT NULL, " +
            "`act` TEXT NOT NULL, `time` TEXT NOT NULL, `timestamp` DATETIME NOT NULL" +
            ");";

    public static final String STATEMENT_DROP = "DROP TABLE IF EXISTS `" + TABLE_NAME + "`;";
    public static final String STATEMENT_INSERT_OR_REPLACE = "INSERT INTO `" + TABLE_NAME + "` (" +
            "`stock_name`, `price`, `chg`, " +
            "`vol`, `act`, `time`, `timestamp`" +
            ") VALUES (" +
            "?, ?, ?, ?, ?, ?, ?" +
            ");";


    public DA_M_trade_market(Context context) {
        super(context);
    }

    public int insert_or_replace(ArrayList< ModelTraderMarket > vectors) {
        int rtn = 0;
        open();
        try {
            __sqliteDatabase.beginTransactionNonExclusive();
            SQLiteStatement sqliteStatement = __sqliteDatabase.compileStatement(STATEMENT_INSERT_OR_REPLACE);

            for(ModelTraderMarket soapObject : vectors) {
                int i = 1;

                sqliteStatement.bindString(i++, String.valueOf(soapObject.getStock_name()));
                sqliteStatement.bindString(i++, String.valueOf(soapObject.getPrice()));
                sqliteStatement.bindString(i++, String.valueOf(soapObject.getChg()));
                sqliteStatement.bindString(i++, String.valueOf(soapObject.getVol()));
                sqliteStatement.bindString(i++, String.valueOf(soapObject.getAct()));
                sqliteStatement.bindString(i++, String.valueOf(soapObject.getTime()));
                sqliteStatement.bindString(i++, String.valueOf(soapObject.getTimestamp()));

                sqliteStatement.executeInsert();
                sqliteStatement.clearBindings();
                rtn += 1;
            }

            __sqliteDatabase.setTransactionSuccessful();

        } catch(Exception e) {
            Log.d("TAG","onResponse insert_or_replace gagal" + e.getMessage());

        } finally {
            __sqliteDatabase.endTransaction();
            close();
        }
        return rtn;
    }

    public int deleteRowbove50(long timeMillis){
        int rtn = 0;
        open();
        try {
            String queryWhere = "timestamp < '" + timeMillis + "'";
            rtn = __sqliteDatabase.delete(TABLE_NAME, queryWhere, null);

        } catch(Exception e) {
            Log.d("TAG","onResponse deleteRowbove50 gagal" + e.getMessage());

        } finally {
            close();
        }
        return rtn;
    }

    public ArrayList<ModelTraderMarket> getAllTrade(){
        ArrayList<ModelTraderMarket> rtn = new ArrayList<>();
        open();
        try {
            String query = "SELECT * FROM " + TABLE_NAME +" ORDER BY timestamp DESC";
            Cursor pos = __sqliteDatabase.rawQuery(query, null);
            while(pos.moveToNext()) {
                try {
                    int i = 0;
                    ModelTraderMarket modelTraderMarket = new ModelTraderMarket();
                    modelTraderMarket.id = pos.getInt(i++);
                    modelTraderMarket.stock_name = pos.getString(i++);
                    modelTraderMarket.price = pos.getString(i++);
                    modelTraderMarket.chg = pos.getString(i++);
                    modelTraderMarket.vol = pos.getString(i++);
                    modelTraderMarket.act = pos.getString(i++);
                    modelTraderMarket.time = pos.getString(i++);
                    modelTraderMarket.timestamp = pos.getLong(i++);

                    rtn.add(modelTraderMarket);

                }catch(Exception e) {
                    Log.d("TAG","onResponse getAllTrade gagal" + e.getMessage());
                }
            }
            pos.close();
        }catch (Exception e){
            Log.d("TAG","onResponse getList gagal" + e.getMessage());
        }finally {
            close();
        }
        return rtn;
    }

    public ArrayList<String> getListNameOfStock(){
        ArrayList<String> rtn = new ArrayList<>();
        open();
        try {
            String query = "SELECT DISTINCT stock_name FROM " + TABLE_NAME;
            Cursor pos = __sqliteDatabase.rawQuery(query, null);
            while(pos.moveToNext()) {
                try {
                    int i = 0;

                    rtn.add(pos.getString(i++));

                }catch(Exception e) {
                    Log.d("TAG","onResponse getAllTrade gagal" + e.getMessage());
                }
            }
            pos.close();
        }catch (Exception e){
            Log.d("TAG","onResponse getList gagal" + e.getMessage());
        }finally {
            close();
        }
        return rtn;
    }

    public ArrayList<ModelTraderMarket> getAllTradeWithFilter(String data){
        ArrayList<ModelTraderMarket> rtn = new ArrayList<>();
        open();
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE stock_name = '"+data+"' ";
            Log.d("TAG","query = "+query);
            Cursor pos = __sqliteDatabase.rawQuery(query, null);
            while(pos.moveToNext()) {
                try {
                    int i = 0;
                    ModelTraderMarket modelTraderMarket = new ModelTraderMarket();
                    modelTraderMarket.id = pos.getInt(i++);
                    modelTraderMarket.stock_name = pos.getString(i++);
                    modelTraderMarket.price = pos.getString(i++);
                    modelTraderMarket.chg = pos.getString(i++);
                    modelTraderMarket.vol = pos.getString(i++);
                    modelTraderMarket.act = pos.getString(i++);
                    modelTraderMarket.time = pos.getString(i++);
                    modelTraderMarket.timestamp = pos.getLong(i++);

                    rtn.add(modelTraderMarket);
                }catch(Exception e) {
                    Log.d("TAG","onResponse getAllTrade gagal" + e.getMessage());
                }
            }
            pos.close();
        }catch (Exception e){
            Log.d("TAG","onResponse getList gagal" + e.getMessage());
        }finally {
            close();
        }
        return rtn;
    }
}

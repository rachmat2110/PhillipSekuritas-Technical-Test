package com.technical.philliptrade.db_sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.technical.philliptrade.da_sqlite.DA_M_trade_market;

public class DB_sqlite_helper extends SQLiteOpenHelper {
    public static final String DB_NAME = "trade_market.db";
    public static final int   DB_VERSION = 1;

    DB_sqlite_helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        upgradeDatabase(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void createDatabase(SQLiteDatabase db){
        db.execSQL(DA_M_trade_market.STATEMENT_CREATE);
    }

    public void upgradeDatabase(SQLiteDatabase db){
        db.execSQL(DA_M_trade_market.STATEMENT_DROP);
        onCreate(db);
    }
}

package com.technical.philliptrade.db_sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DB_Base {
    protected Context __context;
    protected DB_sqlite_helper __db_sqlite_helper;
    protected SQLiteDatabase __sqliteDatabase;

    public DB_Base(Context context) {
        __context = context;
    }

    public DB_Base open() throws SQLException {
        __db_sqlite_helper = new DB_sqlite_helper(__context);
        __sqliteDatabase = __db_sqlite_helper.getReadableDatabase();

        return this;
    }

    public void close() {
        __sqliteDatabase.close();
        __db_sqlite_helper.close();
    }
}

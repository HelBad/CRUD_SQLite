package com.example.crudsqlite.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE " + Config.TABLE_KARYAWAN + "("
                + Config.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Config.COLUMN_NAMA + " TEXT NOT NULL, "
                + Config.COLUMN_ALAMAT + " TEXT, "
                + Config.COLUMN_TELP + " TEXT, "
                + Config.COLUMN_IMAGE + " BLOB "
                + ")")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + Config.TABLE_KARYAWAN)
        onCreate(db)
    }

    companion object {
        private var databaseHelper: DatabaseHelper? = null
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = Config.DATABASE_NAME

        @Synchronized
        fun getInstance(context: Context): DatabaseHelper {
            if (databaseHelper == null) {
                databaseHelper = DatabaseHelper(context)
            }
            return databaseHelper!!
        }
    }

}

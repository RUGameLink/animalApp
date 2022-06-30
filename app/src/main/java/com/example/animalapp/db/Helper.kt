package com.example.imdbapp.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class

Helper(context: Context): SQLiteOpenHelper(context, DbHist.DATABASE_NAME, null, DbHist.DATABASE_VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) { //Функция создания таблицы в бд
        p0?.execSQL(DbHist.CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) { //Функция обновления таблицы в бд
        p0?.execSQL(DbHist.SQL_DELETE_TABLE)
        onCreate(p0)
    }
}
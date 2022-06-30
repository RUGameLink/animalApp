package com.example.imdbapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class Manager (context: Context) {
    val dbHelper = Helper(context)
    var dataBase: SQLiteDatabase ?= null

    fun openDb(){  //Функция открытия БД
        dataBase = dbHelper.writableDatabase
    }
    fun insertToDb(title: String){ //Функция записи БД
        val values = ContentValues().apply {
            put(DbHist.COLUMN_NAME_TITLE, title)

        }
        dataBase?.insert(DbHist.TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    fun readDbDataTitles(): ArrayList<String>{ //Считывание из бд в лист
        val dataList = ArrayList<String>()

        val cursor = dataBase?.query(DbHist.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null)
        with(cursor){
            while (this?.moveToNext()!!){
                val dataText = cursor?.getString(cursor.getColumnIndex(DbHist.COLUMN_NAME_TITLE)) //Считывание в переменную из столбца
                dataList.add(dataText.toString())
            }
        }
        cursor?.close()
        return dataList//Возврат листа

    }

    fun closeDb(){ //Функция закрытия БД
        dbHelper.close()
    }

    fun deleteAll(){
        dataBase?.execSQL(DbHist.SQL_DELETE_TABLE)
        dataBase?.execSQL(DbHist.CREATE_TABLE)
    }
}
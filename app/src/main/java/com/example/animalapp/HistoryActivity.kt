package com.example.animalapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imdbapp.db.Manager

class HistoryActivity: AppCompatActivity() {
    private val dbManager = Manager(this) //Инициализация бд-менеджера
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        deleteButton = findViewById(R.id.deleteButton)

        dbManager.openDb() //Открытие бд
        val history = dbManager.readDbDataTitles() //Считывание из бд колонки имен в лист
        dbManager.closeDb() //Закрытие бд

        val recyclerView: RecyclerView = findViewById(R.id.history) //Подвязка ресайклера к объекту
        val linearLayoutManager = LinearLayoutManager(applicationContext) //Подготовка лайаут менеджера
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager //Инициализация лайаут менеджера
        recyclerView.adapter = RecyclerAdapter(history!!) //внесение данных из листа в адаптер (заполнение данными)

        deleteButton.setOnClickListener {
            dbManager.openDb() //Открытие бд
            dbManager.deleteAll() //Считывание из бд колонки имен в лист

            val history = dbManager.readDbDataTitles() //Считывание из бд колонки имен в лист
            dbManager.closeDb() //Закрытие бд

            val recyclerView: RecyclerView = findViewById(R.id.history) //Подвязка ресайклера к объекту
            val linearLayoutManager = LinearLayoutManager(applicationContext) //Подготовка лайаут менеджера
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            recyclerView.layoutManager = linearLayoutManager //Инициализация лайаут менеджера
            recyclerView.adapter = RecyclerAdapter(history!!) //внесение данных из листа в адаптер (заполнение данными)
        }
    }
}

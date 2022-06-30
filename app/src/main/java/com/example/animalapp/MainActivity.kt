package com.example.animalapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.imdbapp.db.Manager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    //Инициализация эдементов активити и прочих переменных
    private lateinit var getButton: Button
    private lateinit var itemList: ListView
    private lateinit var animals: Array<String>
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var imageFrame: FrameLayout
    private lateinit var item: String
    private lateinit var imageFragment: ImageFragment
    private lateinit var buttonHistory: Button
    //private lateinit var urlImage: String
    private val dbManager = Manager(this) //Инициализация бд-менеджера

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        animals = resources.getStringArray(R.array.animals) //Сбор данных из массива с животными

        adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, animals) //Инициализация адаптера

        itemList.adapter = adapter

        item = ""

        itemList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            item = parent.getItemAtPosition(position).toString()
            Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show() //Считывание выбранной позиции списка
        }

        getButton.setOnClickListener(getButtonListener) //Объявление слушателя кнопки

        buttonHistory.setOnClickListener(buttonHistoryListener)
    }

    private var buttonHistoryListener: View.OnClickListener = View.OnClickListener { //Обработка нажатия на кнопку
        val i = Intent(this, HistoryActivity::class.java) //интент перехода к другой активити
        startActivity(i)

    }



    private var getButtonListener: View.OnClickListener = View.OnClickListener { //Обработка нажатия на кнопку
        if(item.isEmpty()) { //Обработка случая без запроса (вывод соответствующего Тоста)
            Toast.makeText(applicationContext, "Выберите животное из списка!", Toast.LENGTH_SHORT)
                .show()
            return@OnClickListener
        }
        else{
            dbManager.openDb() //Открытие бд
            dbManager.insertToDb(item) //Запись
            dbManager.closeDb() //Закрытие бд

            item = item.replace(" ", "%20", false)
            Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show()
            showAnimal() //Метод запроса в апишку

        }

    }

    private fun showAnimal(){//Запуск потока
        val thread = Thread {
            try {
                val client = OkHttpClient()

                val request = Request.Builder() //Коннект к Апи
                    .url("https://mlemapi.p.rapidapi.com/randommlem?tag=${item}")
                    .get()
                    .addHeader("X-RapidAPI-Host", "mlemapi.p.rapidapi.com")
                    .addHeader("X-RapidAPI-Key", "8fac8d93edmshc4380d7d88505cdp17d5dfjsndf4c3b2501a4")
                    .build()

                val response = client.newCall(request).execute() //Обращение

                val parse = response.body()?.string()//Получение json и парсинг
                var urlImage = JSONObject(parse).getString("url")
                val bundle = Bundle()
                bundle.putString("urlImage", urlImage) //Упаковка результата парсинга в Бандл
                openFragment(bundle) //Вызов метода открытия фрагмента

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        thread.start()

    }

    private fun openFragment(bundle: Bundle){ //Открытие фрагмента
        imageFragment = ImageFragment() //Инициализация фрагмента
        imageFragment.arguments = bundle //Привязка в него аргументов
        var fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(imageFrame.id, imageFragment) //Замена иного фрагмента
        fragmentTransaction.commit() //Старт фрагмента
    }


    private fun init(){
        getButton = findViewById(R.id.getPict)
        itemList = findViewById(R.id.itemList)
        imageFrame = findViewById(R.id.imageFrame)
        buttonHistory = findViewById(R.id.buttonHistory)
    }
}
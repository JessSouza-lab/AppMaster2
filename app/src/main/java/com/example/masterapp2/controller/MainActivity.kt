package com.example.masterapp2.controller

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.masterapp2.model.Carro
import com.example.masterapp2.R
import com.example.masterapp2.data.dao.CarroDao

class MainActivity : AppCompatActivity() {

    private lateinit var carroDao: CarroDao
    private lateinit var listView: ListView
    private lateinit var emptyTextView: TextView
    private lateinit var adapter: CarroAdapter
    private lateinit var searchView: SearchView
    private lateinit var btnAddCar: Button
    private var carCharsList: List<Carro> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.lvChars)
        emptyTextView = findViewById(R.id.tvEmpty)
        searchView = findViewById(R.id.searchView)
        btnAddCar = findViewById(R.id.btnAddCar)
        carroDao = CarroDao(this)

        loadCarros()


        btnAddCar.setOnClickListener {
            val intent = Intent(this, NewCar::class.java)
            startActivity(intent)
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedCar = listView.getItemAtPosition(position) as Carro
            val intent = Intent(this, DetalheCarroActivity::class.java).apply {
                putExtra("carroId", selectedCar.id)
            }
            startActivity(intent)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCarros(newText)
                return true
            }
        })
    }

    private fun loadCarros() {
        carCharsList = carroDao.getAllChars()
        if (carCharsList.isEmpty()) {
            listView.visibility = ListView.GONE
            emptyTextView.visibility = TextView.VISIBLE
        } else {
            listView.visibility = ListView.VISIBLE
            emptyTextView.visibility = TextView.GONE
            adapter = CarroAdapter(this, carCharsList)
            listView.adapter = adapter
        }
    }

    private fun filterCarros(query: String?) {
        val filteredList = if (query.isNullOrEmpty()) {
            carCharsList
        } else {
            carCharsList.filter {
                it.nome.contains(query, ignoreCase = true) ||
                        it.marca.contains(query, ignoreCase = true) ||
                        it.ano.toString().contains(query)
            }
        }
        adapter = CarroAdapter(this, filteredList)
        listView.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        loadCarros()
    }
}

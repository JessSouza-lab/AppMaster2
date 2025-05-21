package com.example.masterapp2.controller

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Importe AlertDialog para a confirmação de exclusão
import androidx.appcompat.app.AppCompatActivity
import com.example.masterapp2.R
import com.example.masterapp2.data.dao.CarroDao
import com.example.masterapp2.model.Carro

class NewCar : AppCompatActivity() {

    private lateinit var carroDao: CarroDao
    private var carroId: Int = 0
    private lateinit var etNome: EditText
    private lateinit var etMarca: EditText
    private lateinit var etAno: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2) 

        carroDao = CarroDao(this)
        etNome = findViewById(R.id.etNome)
        etMarca = findViewById(R.id.etMarca)
        etAno = findViewById(R.id.etAno)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        carroId = intent.getIntExtra("carroId", 0)

        if (carroId != 0) {
            btnDelete.visibility = Button.VISIBLE
            loadCarDetailsForEdit()
        } else {
            btnDelete.visibility = Button.GONE
        }
        btnSave.setOnClickListener {
            saveCar()
        }
        btnDelete.setOnClickListener {
            confirmAndDeleteCar()
        }
    }
    private fun saveCar() {
        val nome = etNome.text.toString().trim()
        val marca = etMarca.text.toString().trim()
        val anoText = etAno.text.toString().trim()

        if (nome.isEmpty() || marca.isEmpty() || anoText.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        val ano = anoText.toIntOrNull()
        if (ano == null) {
            Toast.makeText(this, "Por favor, digite um ano válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (carroId == 0) {
            val newCar = Carro(nome = nome, marca = marca, ano = ano)
            val result = carroDao.insert(newCar)
            if (result > 0) {
                Toast.makeText(this, "Carro Adicionado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao adicionar carro.", Toast.LENGTH_SHORT).show()
            }
        } else {
            val updatedCar = Carro(id = carroId, nome = nome, marca = marca, ano = ano)
            val result = carroDao.update(updatedCar)
            if (result > 0) {
                Toast.makeText(this, "Carro Atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Erro ao atualizar carro.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun loadCarDetailsForEdit() {
        val car = carroDao.getById(carroId)
        car?.let {
            etNome.setText(it.nome)
            etMarca.setText(it.marca)
            etAno.setText(it.ano.toString())
        } ?: run {
            Toast.makeText(this, "Carro não encontrado para edição.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun confirmAndDeleteCar() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja excluir este carro?")
            .setPositiveButton("Sim") { dialog, which ->
                deleteCar()
            }
            .setNegativeButton("Não", null)
            .show()
    }
    private fun deleteCar() {
        val result = carroDao.delete(carroId)
        if (result > 0) {
            Toast.makeText(this, "Carro excluído com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Erro ao excluir carro.", Toast.LENGTH_SHORT).show()
        }
    }
}

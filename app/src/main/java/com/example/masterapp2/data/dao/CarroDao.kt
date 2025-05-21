package com.example.masterapp2.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.masterapp2.data.db.DBCarro
import com.example.masterapp2.model.Carro

class CarroDao(context: Context) {
    private val dbHelper = DBCarro(context)

    fun getAllChars(): List<Carro> { // Mantido o nome getAllChars para consistência com seu MainActivity
        val carros = mutableListOf<Carro>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(DBCarro.TABLE_NAME, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(DBCarro.COLUMN_ID))
                val nome = cursor.getString(cursor.getColumnIndexOrThrow(DBCarro.COLUMN_NOME))
                val marca = cursor.getString(cursor.getColumnIndexOrThrow(DBCarro.COLUMN_MARCA))
                val ano = cursor.getInt(cursor.getColumnIndexOrThrow(DBCarro.COLUMN_ANO))

                carros.add(Carro(id, nome, marca, ano))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return carros
    }

    fun insert(car: Carro): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBCarro.COLUMN_NOME, car.nome)
            put(DBCarro.COLUMN_MARCA, car.marca)
            put(DBCarro.COLUMN_ANO, car.ano)
        }
        val result = db.insert(DBCarro.TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun update(car: Carro): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBCarro.COLUMN_NOME, car.nome)
            put(DBCarro.COLUMN_MARCA, car.marca)
            put(DBCarro.COLUMN_ANO, car.ano)
        }
        val result = db.update(
            DBCarro.TABLE_NAME,
            values,
            "${DBCarro.COLUMN_ID} = ?",
            arrayOf(car.id.toString())
        )
        db.close()
        return result
    }

    fun delete(id: Int): Int {
        val db = dbHelper.writableDatabase
        val result = db.delete(
            DBCarro.TABLE_NAME,
            "${DBCarro.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
        db.close()
        return result
    }

    fun getById(id: Int): Carro? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBCarro.TABLE_NAME,
            null,
            "${DBCarro.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        var car: Carro? = null
        if (cursor.moveToFirst()) {
            val nome = cursor.getString(cursor.getColumnIndexOrThrow(DBCarro.COLUMN_NOME))
            val marca = cursor.getString(cursor.getColumnIndexOrThrow(DBCarro.COLUMN_MARCA))
            val ano = cursor.getInt(cursor.getColumnIndexOrThrow(DBCarro.COLUMN_ANO))
            car = Carro(id, nome, marca, ano)
        }

        cursor.close()
        db.close()
        return car
    }
}

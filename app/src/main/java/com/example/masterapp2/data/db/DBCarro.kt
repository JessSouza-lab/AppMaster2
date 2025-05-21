package com.example.masterapp2.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBCarro(context: Context) {

    companion object {
        const val DATABASE_NAME = "carros.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Carros" // Mantendo o nome da tabela consistente
        const val COLUMN_ID = "id"
        const val COLUMN_NOME = "nome" // Mantendo a convenção de nomenclatura
        const val COLUMN_MARCA = "marca"
        const val COLUMN_ANO = "ano"
    }

    private val dbHelper = CarroSQLiteOpenHelper(context)
    val writableDatabase: SQLiteDatabase get() = dbHelper.writableDatabase
    val readableDatabase: SQLiteDatabase get() = dbHelper.readableDatabase

    private class CarroSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            val createTable = """
                CREATE TABLE $TABLE_NAME(
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_NOME TEXT NOT NULL,
                    $COLUMN_MARCA TEXT NOT NULL,
                    $COLUMN_ANO INTEGER NOT NULL
                )
            """.trimIndent()
            db.execSQL(createTable)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
        }
    }
}
package com.example.my_sql.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "UsersDB.db"
        const val DB_VERSION = 1
        const val TABLE_NAME = "phoneBook"
        const val NAME = "Name"
        const val PHONE = "Phone"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, $PHONE TEXT UNIQUE, $NAME TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addUsers(users: Map<String, String>): Boolean {
        val db = this.writableDatabase

        var success = -1L

        for ((k, v) in users) {
            val values = ContentValues()

            values.put(NAME, v)
            values.put(PHONE, k)

            success = db.insert(TABLE_NAME, null, values)
        }
        db.close()

        return success > 0
    }

    fun getUniquePhones(): MutableList<String> {
        val list: MutableList<String> = mutableListOf()

        val database = this.readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = database.rawQuery(selectALLQuery, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val phone = cursor.getString(cursor.getColumnIndex(PHONE))

                    list.add(phone)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        database.close()

        return list
    }

}
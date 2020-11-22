package com.example.my_sql

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.my_sql.database.DatabaseHandler
import com.example.my_sql.phone_book.Contacts
import com.example.my_sql.recycler.Data
import com.example.my_sql.recycler.ItemAdapter
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CODE = 10005
    }

    private val itemAdapter = ItemAdapter()
    private val dataList = mutableListOf<Data>()
    private val db = DatabaseHandler(this)

    private val uploadButton: Button
        get() = findViewById(R.id.upload_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        findViewById<RecyclerView>(R.id.RecyclerView).apply {
            adapter = itemAdapter
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            100
        )

        uploadButton.setOnClickListener {
            uploadDatabaseNewContacts()
        }

        showDatabaseContacts()
    }

    private fun uploadDatabaseNewContacts() {
        val uniquePhoneList = db.getUniquePhones()

        val contacts = Contacts().getContacts(this).minus(uniquePhoneList)

        if (contacts.isNotEmpty()) {
            db.addUsers(contacts)

            showDatabaseContacts()
        }
    }

    private fun showDatabaseContacts() {
        readFromDatabase()

        itemAdapter.updateItems(dataList)
    }

    private fun readFromDatabase() {
        dataList.clear()

        val database = db.readableDatabase
        val selectALLQuery = "SELECT * FROM ${DatabaseHandler.TABLE_NAME}"
        val cursor = database.rawQuery(selectALLQuery, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val name = cursor.getString(cursor.getColumnIndex(DatabaseHandler.NAME))
                    val phone = cursor.getString(cursor.getColumnIndex(DatabaseHandler.PHONE))

                    val data = Data(name, phone)

                    dataList.add(data)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        database.close()
    }

    private fun addPermissions(vararg permission: String) {
        val permissionsList: MutableList<String> = ArrayList()

        permissionsList.addAll(permission)

        if (permissionsList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                permissionsList.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }
}
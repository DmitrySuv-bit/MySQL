package com.example.my_sql.phone_book

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract

class Contacts {
    private val list: MutableMap<String, String> = mutableMapOf()

    private companion object {
        val CONTENT_URI: Uri = ContactsContract.Contacts.CONTENT_URI
        val PhoneCONTENT_URI: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        const val ID = ContactsContract.Contacts._ID
        const val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
        const val HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER
        const val Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        const val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER
    }

    fun getContacts(context: Context): MutableMap<String, String> {
        list.clear()
        var phoneNumber = ""

        val cursor: Cursor? = context.contentResolver.query(CONTENT_URI, null, null, null, null)

        if (cursor?.count!! > 0) {
            while (cursor.moveToNext()) {
                val contactId = cursor.getString(cursor.getColumnIndex(ID))
                val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                val hasPhoneNumber =
                    cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)).toInt()

                if (hasPhoneNumber > 0) {
                    val phoneCursor = context.contentResolver.query(
                        PhoneCONTENT_URI, null,
                        "$Phone_CONTACT_ID = ?", arrayOf(contactId), null
                    )

                    while (phoneCursor?.moveToNext()!!) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER))
                    }
                    list[phoneNumber] = name

                    phoneCursor.close()
                }
            }
        }
        cursor.close()

        return list
    }
}
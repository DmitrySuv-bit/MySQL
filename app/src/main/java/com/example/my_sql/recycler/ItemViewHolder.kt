package com.example.my_sql.recycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_sql.R

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private var name: TextView = view.findViewById(R.id.nameText)
    private var phone: TextView = view.findViewById(R.id.phoneText)

    fun bind(data: Data) {
        name.text = data.name
        phone.text = data.phone
    }
}
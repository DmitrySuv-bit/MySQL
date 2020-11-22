package com.example.my_sql.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.my_sql.R

class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    private val itemsList = mutableListOf<Data>()

    fun updateItems(newItems: List<Data>) {
        itemsList.clear()
        itemsList.addAll(newItems)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemsList[position]

        holder.bind(item)
    }

    override fun getItemCount(): Int = itemsList.size
}
package com.example.smartstudy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    val titulo = arrayOf("Proyecto de Apps")
    val detalle = arrayOf("entregar avance de la app")
    val fecha = arrayOf("29/11/2023")

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val V = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(V)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemTitulo.text = titulo[i]
        viewHolder.itemFecha.text = fecha[i]
        viewHolder.itemDetalle.text = detalle[i]
    }

    override fun getItemCount(): Int {
        return titulo.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitulo: TextView
        var itemDetalle: TextView
        var itemFecha: TextView

        init {
            itemTitulo = itemView.findViewById(R.id.item_titulo)
            itemDetalle = itemView.findViewById(R.id.item_detalle)
            itemFecha = itemView.findViewById(R.id.item_fecha)
        }
    }
}
package com.example.smartstudy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var listTareas: ArrayList<Datalist>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.item_titulo)
        val detalle: TextView = itemView.findViewById(R.id.item_detalle)
        val fecha: TextView = itemView.findViewById(R.id.item_fecha)
        val id: TextView = itemView.findViewById(R.id.item_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listTareas.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = listTareas[position]
        holder.titulo.text = currentItem.nombre
        holder.detalle.text = currentItem.instrucciones
        holder.fecha.text = currentItem.fecha
        holder.id.text = currentItem.id

        holder.itemView.setOnClickListener {
            // Handle click event here
            val intent = Intent(holder.itemView.context, DetallesTarea::class.java)
            intent.putExtra("id", currentItem.id)
            holder.itemView.context.startActivity(intent)
        }
    }

}

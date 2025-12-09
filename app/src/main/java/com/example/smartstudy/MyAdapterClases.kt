package com.example.smartstudy

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapterClases(var listClase: ArrayList<DatalistClase>): RecyclerView.Adapter<MyAdapterClases.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val materia: TextView = itemView.findViewById(R.id.item_Materia)
        val horaI: TextView = itemView.findViewById(R.id.item_horaInicio)
        val horaF: TextView = itemView.findViewById(R.id.item_horaFin)
        val aula: TextView = itemView.findViewById(R.id.item_Aula)
        val dia: TextView = itemView.findViewById(R.id.item_dia)
        val id: TextView = itemView.findViewById(R.id.item_id_clase)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_clase, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listClase.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = listClase[position]
        holder.materia.text = currentItem.materia
        holder.horaI.text = currentItem.horaI
        holder.horaF.text = currentItem.horaF
        holder.aula.text = currentItem.aula
        holder.dia.text = currentItem.dia
        holder.id.text = currentItem.id

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetallesClases::class.java)
            intent.putExtra("id", currentItem.id)
            holder.itemView.context.startActivity(intent)
        }
    }
}

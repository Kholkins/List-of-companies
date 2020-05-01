package com.kholkins.listofcompanies.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kholkins.listofcompanies.R
import com.kholkins.listofcompanies.model.Company
import com.squareup.picasso.Picasso

class MainAdapter(var items: List<Company>, val callback: Callback) : RecyclerView.Adapter<MainAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = MainHolder(LayoutInflater.from(parent.context).inflate(R.layout.company_item, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
        private val companyImageView = itemView.findViewById<ImageView>(R.id.companyImageView)

        fun bind(item: Company) {

            var url = item.imageUrl
            Picasso.get()
                .load("http://megakohz.bget.ru/test_task/$url")
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerInside()
                .into(companyImageView)

            nameTextView.text = item.name

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) callback.onItemClicked(items[adapterPosition])
            }


        }
    }

    interface Callback {
        fun onItemClicked(item: Company)
    }

}
package com.android.bungakatafirebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.bungakatafirebase.databinding.ListPuisiBinding

typealias OnClickPuisi = (Puisi) -> Unit
typealias PassDataToEdit = (Puisi) -> Unit
typealias DeleteData = (Puisi) -> Unit

class PuisiAdapter(
    private var listPuisi: List<Puisi>, // Updated to a var
    private val onClickPuisi: OnClickPuisi,
    private val passDataToEdit: PassDataToEdit,
    private val deleteData: DeleteData
) : RecyclerView.Adapter<PuisiAdapter.PuisiViewHolder>() {

    inner class PuisiViewHolder(private val binding: ListPuisiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(puisi: Puisi) {
            with(binding) {
                txtTitle.text = puisi.judul
                txtWriter.text = puisi.penulis

                btnEdit.setOnClickListener {
                    passDataToEdit(listPuisi[adapterPosition])
                }
            }
        }

        init {
            itemView.setOnLongClickListener {
                val background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.bg_list_focused)
                val txtWhite = ContextCompat.getColor(itemView.context, R.color.white)

                itemView.background = background
                with(binding) {
                    layoutHold.visibility = View.VISIBLE
                    txtTitle.setTextColor(txtWhite)
                    txtWriter.setTextColor(txtWhite)

                    btnEdit.setOnClickListener {
                        passDataToEdit(listPuisi[adapterPosition])
                    }

                    btnDelete.setOnClickListener {
                        deleteData(listPuisi[adapterPosition])
                    }
                }

                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PuisiViewHolder {
        val binding = ListPuisiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PuisiViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPuisi.size
    }

    override fun onBindViewHolder(holder: PuisiViewHolder, position: Int) {
        val puisi = listPuisi[position]
        holder.bind(puisi)

        holder.itemView.setOnClickListener {
            onClickPuisi(puisi)
        }
    }

    // Added this function to update the data in the adapter
    fun updateData(newList: List<Puisi>) {
        listPuisi = newList
        notifyDataSetChanged()
    }
}

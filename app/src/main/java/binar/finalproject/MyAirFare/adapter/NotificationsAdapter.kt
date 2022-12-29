@file:Suppress("RemoveEmptyPrimaryConstructor")

package binar.finalproject.MyAirFare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare.databinding.ListItemNotificationsBinding
import java.util.*

class NotificationsAdapter():RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    inner class NotificationsViewHolder(val binding : ListItemNotificationsBinding):RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    private val differ = AsyncListDiffer(this,diffUtil)

    fun submitData(data : MutableList<String>) = differ.submitList(data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
       return NotificationsViewHolder(ListItemNotificationsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.binding.apply {
            val diff = differ.currentList[position]
            if(diff.lowercase(Locale.getDefault()).contains("waiting")){
                tittle.text = "Keranjang"
            }else if(diff.lowercase(Locale.getDefault()).contains("transaksi")){
                tittle.text = "Transaksi"
            }else if(diff.lowercase(Locale.getDefault()).contains("tenggat")){
                tittle.text = "Pembayaran terlewat"
            }else if(diff.lowercase(Locale.getDefault()).contains("dibatalaka")){
                tittle.text = "Pembatalan transaksi"
            }else{
                tittle.text = "Login Success"
            }
            subTittle.text = diff
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}
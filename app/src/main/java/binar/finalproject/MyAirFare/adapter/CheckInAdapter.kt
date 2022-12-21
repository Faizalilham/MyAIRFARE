package binar.finalproject.MyAirFare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare.databinding.ListItemCheckinBinding
import binar.finalproject.MyAirFare.model.room.CheckIn

class CheckInAdapter(private val listener : OnClick):RecyclerView.Adapter<CheckInAdapter.CheckInViewHolder>() {

    inner class CheckInViewHolder(val binding : ListItemCheckinBinding):RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<CheckIn>(){
        override fun areItemsTheSame(oldItem: CheckIn, newItem: CheckIn): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CheckIn, newItem: CheckIn): Boolean {
            return when{
                oldItem.id != newItem.id -> false
                oldItem.from != newItem.from -> false
                oldItem.dest != newItem.dest -> false
                oldItem.chair_number != newItem.chair_number -> false
                oldItem.status != newItem.status -> false
                else -> true
            }
        }
    }
    private val differ = AsyncListDiffer(this,diffUtil)

    fun submitData(data : MutableList<CheckIn>) = differ.submitList(data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckInViewHolder {
        return CheckInViewHolder(ListItemCheckinBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: CheckInViewHolder, position: Int) {
       holder.binding.apply {
           val diff = differ.currentList[position]
           val order_id = "Nomor Pesanan : ${diff.order_id}"
           val flightes =  "${diff.from} - ${diff.dest}"
           orderId.text = order_id
           flight.text = flightes
           chair.text = diff.chair_number
           status.text = diff.status
           btnDelete.setOnClickListener {
               listener.onDelete(diff)
           }
       }
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface OnClick{
        fun onDelete(checkIn : CheckIn)
    }
}
@file:Suppress("RedundantLambdaArrow")

package binar.finalproject.MyAirFare.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare.databinding.ListItemCartBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.model.wait_list.WaitList
import binar.finalproject.MyAirFare.utils.DatePicker
import com.bumptech.glide.Glide

class CartAdapter(private val listener : OnClickListener):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val listChairs = mutableListOf<Int>()

    private val diffUtil = object : DiffUtil.ItemCallback<WaitList>(){
        override fun areItemsTheSame(oldItem: WaitList, newItem: WaitList): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WaitList, newItem: WaitList): Boolean {
           return when{
               oldItem.id != newItem.id -> false
               else -> true
           }
        }

    }

    private val differ = AsyncListDiffer(this,diffUtil)

    fun submiData(data : MutableList<WaitList>) = differ.submitList(data)

    inner class CartViewHolder(val binding : ListItemCartBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        return CartViewHolder(ListItemCartBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.binding.apply {
            val diff = differ.currentList[position]
            Log.d("SIZED","${diff.carts.size}")
            if(diff.carts.size > 0){
                val dateAir = DatePicker.dateCalculation(diff.carts[0].ticket.date_air)
                val estimated = DatePicker.dateCalculation(diff.carts[0].ticket.estimated_up_dest)
                val timeAir = DatePicker.timeCalculation(diff.carts[0].ticket.date_air)
                val timeEstimated = DatePicker.timeCalculation(diff.carts[0].ticket.estimated_up_dest)
                val date = "$dateAir - $estimated"
                Glide.with(root).load("https://binarstudpenfinalprojectbe-production-77a5.up.railway.app${diff.carts[0].ticket.logo}").into(imageLogo)
                tvTime.text = DatePicker.getDifferentTime(diff.carts[0].ticket.date_air)
                tvDate.text = date
                tvAsalTime.text = timeAir
                tvTujuanTime.text = timeEstimated
                when(diff.carts[0].ticket.kelas){
                    1 ->  tvClass.text = "ECONOMY"
                    2 ->  tvClass.text = "BUSSINESS"
                    else  ->  tvClass.text = ""
                }
                tvAsal.text = diff.carts[0].ticket.from
                tvTujuan.text = diff.carts[0].ticket.dest
                tvPrice.text = diff.carts[0].ticket.price.toString()
                tvKodePenerbangan.text = diff.carts[0].ticket.flight_number
                tvType.text = "Sekali Jalan"
                if(diff.carts.size > 1){
                    tvType.text = "Pulang Pergi"
                }
            }
            diff.carts.forEach {
                it.ticket.available.forEach {  its ->
                    listChairs.add(its.chair_number)
                    Log.d("Chairsss","$its")
                }
            }
            val schedule = mutableListOf<Schedule>()
            diff.carts.forEach {
                schedule.add(it.ticket)
                Log.d("SCHEDULES","${it.ticket}")
            }

            card.setOnClickListener {
                listener.onDetail(schedule,listChairs,diff.id.toString())
            }

            btnDelete.setOnClickListener { _ ->
                listener.onDelete(diff.id)
            }

        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface OnClickListener{
        fun onDelete(id : Int)
        fun onDetail(schedule: MutableList<Schedule>, chairs : MutableList<Int>,waitListId : String)
    }
}
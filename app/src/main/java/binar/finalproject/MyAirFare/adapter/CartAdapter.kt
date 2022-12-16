package binar.finalproject.MyAirFare.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare.databinding.ListItemCartBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.model.wait_list.TicketWaitList
import binar.finalproject.MyAirFare.model.wait_list.WaitList
import binar.finalproject.MyAirFare.utils.DatePicker
import com.bumptech.glide.Glide
import kotlin.Boolean
import kotlin.Int
import kotlin.apply

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
            diff.carts.forEach {
                val dateAir = DatePicker.dateCalculation(it.ticket.date_air)
                val estimated = DatePicker.dateCalculation(it.ticket.estimated_up_dest)
                val timeAir = DatePicker.timeCalculation(it.ticket.date_air)
                val timeEstimated = DatePicker.timeCalculation(it.ticket.estimated_up_dest)
                val date = "$dateAir - $estimated"
                Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${it.ticket.logo}").into(imageLogo)
                tvTime.text = DatePicker.getDifferentTime(it.ticket.date_air,it.ticket.estimated_up_dest)
                tvDate.text = date
                tvAsalTime.text = timeAir
                tvTujuanTime.text = timeEstimated
                when(it.ticket.kelas){
                    1 ->  tvClass.text = "ECONOMY"
                    2 ->  tvClass.text = "BUSSINESS"
                    else  ->  tvClass.text = ""
                }
                tvAsal.text = it.ticket.from
                tvTujuan.text = it.ticket.dest
                tvPrice.text = it.ticket.price.toString()
                tvKodePenerbangan.text = it.ticket.flight_number
            }
            diff.carts.forEach {
                it.ticket.available.forEach {  its ->
                    listChairs.add(its.chair_number)
                }
            }
            val schedule = mutableListOf<Schedule>()
            diff.carts.forEach {
                schedule.add(it.ticket)
            }

            card.setOnClickListener {
                listener.onDetail(schedule,listChairs)
            }

            btnDelete.setOnClickListener { _ ->
                listener.onDelete(diff.id)
            }

        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface OnClickListener{
        fun onDelete(id : Int)
        fun onDetail(schedule: MutableList<Schedule>, chairs : MutableList<Int>)
    }
}
package binar.finalproject.MyAirFare.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare.databinding.ListItemHomeBinding
import binar.finalproject.MyAirFare.model.tickets.Schedule
import binar.finalproject.MyAirFare.utils.DatePicker
import com.bumptech.glide.Glide
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*


class TicketAdapter(private val listener : OnClick):RecyclerView.Adapter<TicketAdapter.TicketViewHolder>(){

    inner class TicketViewHolder(val binding : ListItemHomeBinding):RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Schedule>(){
        override fun areItemsTheSame(
            oldItem: Schedule,
            newItem: Schedule
        ): Boolean {
            return oldItem.id  == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Schedule,
            newItem: Schedule
        ): Boolean {
            return when {
                oldItem.id != newItem.id -> false
                oldItem.name != newItem.name -> false
                oldItem.from != newItem.from -> false
                oldItem.dest != newItem.dest -> false
                oldItem.price != newItem.price -> false
                else -> true
            }
        }
    }

    private val differ = AsyncListDiffer(this,diffUtil)

    fun submitData(data : MutableList<Schedule>) = differ.submitList(data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        return TicketViewHolder(ListItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.binding.apply {
            val diff = differ.currentList[position]
            Log.d("KONTOLLL",DatePicker.formatDateDC(diff.date_air,TimeZone.getDefault().id))
            val dateAir = DatePicker.dateCalculation(diff.date_air)
            val estimated = DatePicker.dateCalculation(diff.estimated_up_dest)
            val timeAir = DatePicker.formatDateDC(diff.date_air,TimeZone.getDefault().id)
            val timeEstimated = DatePicker.formatDateDC(diff.estimated_up_dest,TimeZone.getDefault().id)
            val date = "$dateAir - $estimated"
            Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${diff.logo}").into(imageLogo)
            tvTime.text = DatePicker.getDifferentTime(diff.date_air,diff.estimated_up_dest)
            tvDate.text = date
            tvAsalTime.text = timeAir
            tvTujuanTime.text = timeEstimated
            when(diff.kelas){
                1 ->  tvClass.text = "ECONOMY"
                2 ->  tvClass.text = "BUSSINESS"
            }
            tvAsal.text = diff.from
            tvTujuan.text = diff.dest
            tvPrice.text = diff.price.toString()
            tvKodePenerbangan.text = diff.flight_number
            card.setOnClickListener {
                listener.onClicked(diff)
            }

        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface OnClick{
        fun onClicked(schedule: Schedule)
    }
}
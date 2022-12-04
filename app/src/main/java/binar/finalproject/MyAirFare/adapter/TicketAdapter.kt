package binar.finalproject.MyAirFare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare.databinding.ListItemHomeBinding
import binar.finalproject.MyAirFare.model.flight.Schedule


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

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.binding.apply {
            val diff = differ.currentList[position]
            tvDate.text = diff.estimated_up_dest
            tvClass.text = diff.kelas
            tvAsal.text = diff.from
            tvTujuan.text = diff.dest
            tvDate.text = diff.date_air
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
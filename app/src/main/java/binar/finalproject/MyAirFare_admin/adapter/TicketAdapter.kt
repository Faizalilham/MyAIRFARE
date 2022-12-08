package binar.finalproject.MyAirFare_admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare_admin.databinding.ListItemAvailableTicketBinding
import binar.finalproject.MyAirFare_admin.model.ticket.Tickets
import com.bumptech.glide.Glide

class TicketAdapter(private val listener : OnClick):RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(val binding : ListItemAvailableTicketBinding):RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Tickets>(){
        override fun areItemsTheSame(oldItem: Tickets, newItem: Tickets): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tickets, newItem: Tickets): Boolean {
           return when{
               oldItem.id != newItem.id -> false
               oldItem.name != newItem.name -> false
               oldItem.from != newItem.from -> false
               oldItem.dest != newItem.dest -> false
               oldItem.date_air != newItem.date_air -> false
               oldItem.price != newItem.price -> false
               else -> true
           }
        }

    }

    private val differ = AsyncListDiffer(this,diffUtil)

    fun submitData(datas : MutableList<Tickets>) = differ.submitList(datas)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        return TicketViewHolder(ListItemAvailableTicketBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
       holder.binding.apply {
           val diff = differ.currentList[position]
           tvNameBisnis.text = diff.kelas
           tvAsal.text = diff.from
           tvTujuan.text = diff.dest
           tvPrice.text = diff.price.toString()
           tvKode.text = diff.flight_number
           tvNameFlight.text = diff.name
           tvNamePlane.text = diff.name
           Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${diff.logo}").into(imageFlight)
           card.setOnClickListener {
               listener.onDetail(diff)
               listener.onUpdate(diff)
               listener.onDelete(diff)
           }
       }
    }

    override fun getItemCount(): Int  = differ.currentList.size

    interface OnClick{
        fun onDetail(ticket: Tickets)
        fun onUpdate(ticket: Tickets)
        fun onDelete(ticket: Tickets)
    }
}
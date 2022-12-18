package binar.finalproject.MyAirFare_admin.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare_admin.databinding.ListItemSoldTicketBinding
import binar.finalproject.MyAirFare_admin.databinding.ListItemTransactionsBinding
import binar.finalproject.MyAirFare_admin.model.transactions.Transactions
import binar.finalproject.MyAirFare_admin.utils.DatePicker
import com.bumptech.glide.Glide

class TransactionsAdapter(private val listener : OnClick):RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>() {

    inner class TransactionsViewHolder(val binding :ListItemSoldTicketBinding):RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Transactions>(){
        override fun areItemsTheSame(oldItem: Transactions, newItem: Transactions): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transactions, newItem: Transactions): Boolean {
           return when{
               oldItem.id != newItem.id -> false
               oldItem.token_trx != newItem.token_trx -> false
               oldItem.count != newItem.count -> false
               oldItem.price != newItem.price -> false
               oldItem.status != newItem.status -> false
               else -> true
           }
        }
    }
    private val differ = AsyncListDiffer(this,diffUtil)

    fun submitData(datas : MutableList<Transactions>) = differ.submitList(datas)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder {
        return TransactionsViewHolder(ListItemSoldTicketBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int) {
        holder.binding.apply {
            val transactions = differ.currentList[position]
            transactions.carts.forEach {
                Glide.with(root).load("https://binarstudpenfinalprojectbe-production.up.railway.app${it.ticket.logo}").into(imageLogo)
                when(it.ticket.kelas){
                    1 ->  tvClass.text = "Economy"
                    2 ->  tvClass.text = "Bussiness"
                    else ->  tvClass.text = ""
                }
                tvFlightNumber.text = it.ticket.flight_number
                tvAirlane.text = it.ticket.name
                tvFrom.text = it.ticket.from
                tvDestination.text = it.ticket.dest
                val dateAir = DatePicker.dateCalculation(it.ticket.date_air)
                val estimated = DatePicker.dateCalculation(it.ticket.estimated_up_dest)
                val date = "$dateAir - $estimated"
                tvNameFlight.text = date
            }
            card.setOnClickListener {
                listener.onDetail(transactions)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface  OnClick{
        fun onDetail(transactions: Transactions)
    }
}
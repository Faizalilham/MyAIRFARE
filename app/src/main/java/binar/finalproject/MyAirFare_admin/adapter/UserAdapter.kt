package binar.finalproject.MyAirFare_admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import binar.finalproject.MyAirFare_admin.databinding.ListItemUserBinding
import binar.finalproject.MyAirFare_admin.model.user.User
import com.bumptech.glide.Glide

class UserAdapter(private val listener : OnClick):RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return when{
                oldItem.id != oldItem.id -> false
                oldItem.username != oldItem.username -> false
                oldItem.f_name != oldItem.f_name -> false
                oldItem.l_name != oldItem.l_name -> false
                oldItem.email != oldItem.email -> false
                else -> true
            }
        }
    }

    private val differ = AsyncListDiffer(this,diffUtil)

    fun submitData(datas : MutableList<User>) = differ.submitList(datas)

    inner class UserViewHolder(val binding : ListItemUserBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ListItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.binding.apply {
            val diff = differ.currentList[position]
            tvName.text = diff.username
            tvEmail.text = diff.email
            println(diff.photo)
            Glide.with(root).load("https://binarstudpenfinalprojectbe-production-77a5.up.railway.app${diff.photo}").into(imageUser)
            card.setOnClickListener {
                listener.onClicked(diff)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    interface OnClick{
        fun onClicked(user : User)
    }
}
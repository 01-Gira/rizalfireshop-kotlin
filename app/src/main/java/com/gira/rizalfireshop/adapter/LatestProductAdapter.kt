package com.gira.rizalfireshop.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gira.rizalfireshop.R
import com.gira.rizalfireshop.data.model.Product
import com.gira.rizalfireshop.databinding.ItemProductLatestBinding

class LatestProductAdapter: RecyclerView.Adapter<LatestProductAdapter.MyViewHolder> (){
    inner class MyViewHolder(private val binding: ItemProductLatestBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Product) {

            binding.apply {
                Glide.with(itemView.context)
                    .load(item.image)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .into(ivProductImage)
                tvProductName.text = item.name
                tvProductPrice.text = "Rp. ${item.price}"
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product> () {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem ==newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemProductLatestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item =differ.currentList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun isDataEmpty(): Boolean {
        return differ.currentList.isEmpty()
    }

    var onClick: ((Product) -> Unit) ?= null
}
package com.example.dirverapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dirverapp.data.remote.Order
import com.example.dirverapp.databinding.LayoutOrderDetailsBinding

class OrdersAdapter :
    androidx.recyclerview.widget.ListAdapter<Order, RecyclerView.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: LayoutOrderDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Order) {
            with(binding) {
                tvOrderDeliveryLocation.text = item.deliverLocationName
                tvCustomerName.text = item.customerName
                tvDeliveryStatus.text = item.orderStatus
                tvCustomerPhone.text = item.primaryPhone

                val position = layoutPosition

                tvCount.text = "${position + 1} of ${getItem(position)}"

                root.setOnClickListener {
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutOrderDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as OrdersAdapter.ViewHolder).bind(getItem(position))
    }
}

val diffUtil = object : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(
        oldItem: Order,
        newItem: Order,
    ): Boolean {
        return oldItem.orderBatchNumber == newItem.orderBatchNumber
    }

    override fun areContentsTheSame(
        oldItem: Order,
        newItem: Order,
    ): Boolean {
        return oldItem == newItem
    }
}

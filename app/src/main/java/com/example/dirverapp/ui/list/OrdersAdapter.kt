package com.example.dirverapp.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dirverapp.data.remote.orders.OrderEntity
import com.example.dirverapp.databinding.LayoutOrderDetailsBinding

class OrdersAdapter : ListAdapter<OrderEntity, OrdersAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: LayoutOrderDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: OrderEntity) {
            with(binding) {
                tvOrderDeliveryLocation.text = item.deliverLocationName
                tvCustomerName.text = item.customerName
                tvDeliveryStatus.text = item.orderStatus
                tvCustomerPhone.text = item.primaryPhone

                val position = layoutPosition

                tvCount.text = "${position + 1} "

                root.setOnClickListener {
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutOrderDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

val diffUtil = object : DiffUtil.ItemCallback<OrderEntity>() {
    override fun areItemsTheSame(
        oldItem: OrderEntity,
        newItem: OrderEntity,
    ): Boolean {
        return oldItem.orderBatchNumber == newItem.orderBatchNumber
    }

    override fun areContentsTheSame(
        oldItem: OrderEntity,
        newItem: OrderEntity,
    ): Boolean {
        return oldItem == newItem
    }
}

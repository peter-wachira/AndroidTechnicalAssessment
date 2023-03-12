package com.example.dirverapp.other

import com.example.dirverapp.data.remote.orders.Order
import com.example.dirverapp.data.remote.orders.OrderEntity

fun Order.toOrderEntity(): OrderEntity = OrderEntity(
    this.customerName,
    this.address,
    this.orderBatchNumber,
    this.primaryPhone,
    this.secondaryPhone,
    this.latitude,
    this.longitude,
    this.deliverLocationName,
    this.orderStatus,
    this.customerTyCode,
    this.customerCode,
)

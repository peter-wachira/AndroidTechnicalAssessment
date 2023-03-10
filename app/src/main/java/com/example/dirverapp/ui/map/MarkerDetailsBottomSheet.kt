package com.example.dirverapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dirverapp.R
import com.example.dirverapp.data.remote.orders.OrderEntity
import com.example.dirverapp.databinding.DialogMarkerDetailsBinding
import com.example.dirverapp.utils.disable
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerDetailsBottomSheet(private val orderEntity: OrderEntity) : BottomSheetDialogFragment() {
    private val binding: DialogMarkerDetailsBinding by lazy {
        DialogMarkerDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvShopName.text = orderEntity.deliverLocationName
            tvShopLocation.text = orderEntity.address
            tvCustomerName.text = orderEntity.customerName
            imgCall.setOnClickListener {
                dismiss()
            }

            if (orderEntity.orderStatus.equals("delivered", true)) {
                btnDirection.disable()
                with(btnViewDetails) {
                    text = getString(R.string.complete_payment)
                    setOnClickListener {
                        dismiss()
                    }
                }
            } else {
                btnViewDetails.setOnClickListener {
                    dismiss()
                }
                btnDirection.setOnClickListener {
                    dismiss()
                }
            }
        }
    }
}

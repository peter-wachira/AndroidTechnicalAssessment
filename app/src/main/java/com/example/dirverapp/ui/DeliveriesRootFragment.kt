package com.example.dirverapp.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dirverapp.R
import com.example.dirverapp.databinding.FragmentDeliveriesRootBinding
import com.example.dirverapp.ui.adapter.DeliveriesPagerAdapter
import com.example.dirverapp.utils.showToast
import com.google.android.material.tabs.TabLayout
import com.leinardi.android.speeddial.SpeedDialActionItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeliveriesRootFragment : Fragment() {
    private val binding: FragmentDeliveriesRootBinding by lazy {
        FragmentDeliveriesRootBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pagerAdapter = DeliveriesPagerAdapter(childFragmentManager)

        with(binding) {
            deliveriesPager.adapter = pagerAdapter
            tabLayout.setupWithViewPager(deliveriesPager)
        }

        updateTab()
        setupSpeedDial()
    }

    private fun updateTab() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.speedDialView.show()
                    }

                    1 -> {
                        binding.speedDialView.hide()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    private fun setupSpeedDial() {
        // new order pause, end, cancel

        val context = binding.root.context

        val actionPause = SpeedDialActionItem.Builder(R.id.fab_pause, R.drawable.pause_icon)
            .setLabel("Pause Trip")
            .setLabelColor(ContextCompat.getColor(context, android.R.color.white))
            .setLabelBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setFabBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
            .setFabImageTintColor(ContextCompat.getColor(context, R.color.colorAccent))
            .create()

        val actionEnd = SpeedDialActionItem.Builder(R.id.fab_end, R.drawable.ic_truck_loading)
            .setLabel("End Trip")
            .setLabelColor(ContextCompat.getColor(context, android.R.color.white))
            .setLabelBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setFabImageTintColor(ContextCompat.getColor(context, R.color.colorAccent))
            .setFabBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
            .create()

        binding.speedDialView.addAllActionItems(mutableListOf(actionEnd, actionPause))

        binding.speedDialView.setOnActionSelectedListener { item ->
            when (item.id) {
                R.id.fab_new_order -> {
                    activity?.showToast("")
                }

                R.id.fab_pause -> {
                    showNotification()
                }

                R.id.fab_end -> {
                    activity?.showToast("Trip Ended Successfully")
                }
            }

            binding.speedDialView.close()
            return@setOnActionSelectedListener true
        }
    }

    fun showNotification() {
        val channelId = "MyChannelId"
        val notificationBuilder = context?.let {
            NotificationCompat.Builder(it, channelId)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Trip Paused")
                .setContentText("The current trip has been paused successfully and reassigned to driver KDA 145E.")
                .setStyle(NotificationCompat.BigTextStyle().bigText("The current trip has been paused successfully and reassigned to driver KDA 145E."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "MyChannelName", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1, notificationBuilder?.build())
    }
}

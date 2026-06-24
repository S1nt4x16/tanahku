package com.example.tanahku.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NetworkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Toast.makeText(context, "Sinyal internet berubah!", Toast.LENGTH_SHORT).show()

    }
}
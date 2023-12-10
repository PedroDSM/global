package com.example.wear.presentation

import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService

class MyWearableListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/message-path") {
            val data = String(messageEvent.data)
            // Procesar los datos recibidos del Wear
            println("Mensaje recibido en el teléfono: $data")

            // Aquí puedes realizar acciones adicionales según tus necesidades
        }
    }
}

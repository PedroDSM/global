package com.example.wear.presentation

import android.widget.Button
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.example.wear.R
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable

class WearMainActivity : WearableActivity(), MessageClient.OnMessageReceivedListener {

    private val PATH = "/message-path"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wear_activity_main)

        setupWearCommunication()

        val button = findViewById<Button>(R.id.hola)
        button.setOnClickListener {
            sendMessageToPhone("Hola desde el reloj")
            println("Mensaje enviado desde el reloj")
        }

    }

    private fun setupWearCommunication() {
        Wearable.getMessageClient(this).addListener(this)
    }

    private fun sendMessageToPhone(message: String) {
        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodes ->
            val localNodeTask = Wearable.getNodeClient(this).localNode

            localNodeTask.addOnSuccessListener { localNode ->
                val localNodeId = localNode.id
                println("localNodeId: $localNodeId")

                val phoneNode = nodes.firstOrNull { it.id != localNodeId }
                if (phoneNode != null) {
                    val nodeId = phoneNode.id
                    println("Enviando mensaje al nodo: $nodeId")
                    Wearable.getMessageClient(this).sendMessage(nodeId, PATH, message.toByteArray())
                } else {
                    println("No se encontró un nodo para enviar el mensaje.")
                }
            }

            localNodeTask.addOnFailureListener { exception ->
                // Manejar la falla al obtener el nodo local
                println("Error al obtener el nodo local: $exception")
            }
        }
    }


    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == PATH) {
            val data = String(messageEvent.data)
            // Manejar los datos recibidos del teléfono
            println("Mensaje recibido en el reloj: $data")
        }
    }
}

package com.example.como

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.example.como.R
class MainActivity : AppCompatActivity(), MessageClient.OnMessageReceivedListener {

    private val PATH = "/message-path"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupWearCommunication()

        // Agregar llamada al método para imprimir el ID del nodo en la consola
        printLocalNodeId()
    }

    private fun setupWearCommunication() {
        Wearable.getMessageClient(this).addListener(this)
    }

    private fun sendMessageToWear(message: String) {
        Wearable.getNodeClient(this).connectedNodes.addOnSuccessListener { nodes ->
            for (node in nodes) {
                val nodeId = node.id
                Wearable.getMessageClient(this).sendMessage(nodeId, PATH, message.toByteArray())
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        println("Entró a onMessageReceived")

        if (messageEvent.path == PATH) {
            println("messageEvent.path: $messageEvent")
            val data = String(messageEvent.data)
            // Manejar los datos recibidos del reloj
            println("Mensaje recibido en el teléfono: $data")

            // Mostrar el mensaje en el TextView
            val textView = findViewById<TextView>(R.id.recibido)
            textView.text = data
            println("Interfaz de usuario actualizada con el mensaje")
        }
    }

    // Método para imprimir el ID del nodo local en la consola
    private fun printLocalNodeId() {
        Wearable.getNodeClient(this).localNode.addOnSuccessListener { localNode ->
            val localNodeId = localNode.id
            println("ID del nodo local: $localNodeId")
        }.addOnFailureListener { exception ->
            // Manejar la falla al obtener el nodo local
            println("Error al obtener el nodo local: $exception")
        }
    }
}
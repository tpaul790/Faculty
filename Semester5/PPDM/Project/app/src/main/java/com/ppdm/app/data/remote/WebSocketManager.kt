package com.ppdm.app.data.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.*
import javax.inject.Inject

class WebSocketManager @Inject constructor(private val client: OkHttpClient) {
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun connect() {
        // CHANGE URL to match the handler path defined in WebSocketConfig
        // Note: Use port 8080 (standard Spring Boot) unless you specifically set 8100
        val request = Request.Builder()
            .url("ws://10.0.2.2:8080/projects")
            .build()

        client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                // This will now receive "USER_ADDED" (or whatever string you sent)
                CoroutineScope(Dispatchers.IO).launch { _events.emit(text) }
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                t.printStackTrace() // Check Logcat for errors!
            }

            // Add onOpen to confirm connection works
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                android.util.Log.d("WS", "Connected successfully!")
            }
        })
    }
}
package com.example.siasmobile.bancodedados

import android.content.Context
import com.example.siasmobile.R
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class Supabase(private val context: Context) {
    private val client = OkHttpClient()
    private val baseUrl = context.getString(R.string.supabase_url)
    private val apiKey = context.getString(R.string.supabase_public_key)

    fun makeRequest(endpoint: String, callback: Callback) {
        val url = "$baseUrl/$endpoint"
        val request = Request.Builder()
            .url(url)
            .header("apikey", apiKey)
            .build()

        client.newCall(request).enqueue(callback)
    }

    fun insertUserData(table: String, jsonBody: String, callback: Callback) {
        val url = "$baseUrl/rest/v1/$table"
        val mediaType = "application/json".toMediaTypeOrNull()
        val body = jsonBody.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .header("apikey", apiKey)
            .header("Content-Type", "application/json")
            .post(body)
            .build()

        client.newCall(request).enqueue(callback)
    }
}

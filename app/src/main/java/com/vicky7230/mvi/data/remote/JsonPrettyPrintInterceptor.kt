package com.vicky7230.mvi.data.remote

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

class JsonPrettyPrintInterceptor : HttpLoggingInterceptor.Logger {
    companion object {
        val TAG = "OkHttp"
    }

    override fun log(message: String) {
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val prettyPrintJson =
                    GsonBuilder().setPrettyPrinting().create()
                        .toJson(JsonParser.parseString(message))
                Log.d(TAG, prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                Log.e(TAG, message)
            }
        } else {
            Log.d(TAG, message)
        }
    }
}
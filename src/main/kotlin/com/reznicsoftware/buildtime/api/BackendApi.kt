package com.reznicsoftware.buildtime.api

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.IOException

class BackendApi(private val url: String) {

    fun sendData(data: ResultDTO) {

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
        runBlocking {
            println("start send data to backend...")
            try {
                val response: HttpResponse = client.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(data)
                }

                println(response.bodyAsText())
            } catch (ex: IOException) {
                println(ex)
            }
            println("successfully send data to backend...")
        }
    }
}
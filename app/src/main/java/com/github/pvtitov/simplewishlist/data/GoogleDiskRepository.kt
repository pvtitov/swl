package com.github.pvtitov.simplewishlist.data

import android.content.Context
import android.content.Intent
import com.github.pvtitov.simplewishlist.domain.model.WishList
import com.github.pvtitov.simplewishlist.utils.DI
import okhttp3.OkHttpClient
import okhttp3.Request


class GoogleDiskRepository {

    private val okHttpClient by lazy { OkHttpClient() }
    private val jsonParser = DI.jsonParser

    fun download(url: String): WishList? {
        val request = Request.Builder()
            .url(url)
            .build()

        val response = try {
            okHttpClient.newCall(request).execute()
        } catch (e: Throwable) {
            return null
        }

        return response.use { r ->
            val json = r.body?.string()
            json?.let(jsonParser::fromJson)
        }
    }

    fun upload(context: Context, data: WishList): Boolean {
        val dataAsText = jsonParser.toJson(data)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, dataAsText)
            type = JSON_MIME_TYPE
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
        return true
    }
}

private const val JSON_MIME_TYPE = "text/json"
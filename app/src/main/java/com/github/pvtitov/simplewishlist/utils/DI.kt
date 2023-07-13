package com.github.pvtitov.simplewishlist.utils

import com.github.pvtitov.simplewishlist.core.data.model.AccountData

object DI {
    val jsonParser = JsonParser<AccountData>()
}
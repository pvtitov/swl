package com.github.pvtitov.simplewishlist.domain.ui.promises

import com.github.pvtitov.simplewishlist.domain.model.LoginModel
import com.github.pvtitov.simplewishlist.domain.model.WishModel

interface CancelPromiseUI {
    fun cancelPromise(login: LoginModel, wish: WishModel)
}
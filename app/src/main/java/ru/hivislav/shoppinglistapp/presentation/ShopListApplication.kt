package ru.hivislav.shoppinglistapp.presentation

import android.app.Application
import ru.hivislav.shoppinglistapp.di.DaggerApplicationComponent

class ShopListApplication: Application() {


    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
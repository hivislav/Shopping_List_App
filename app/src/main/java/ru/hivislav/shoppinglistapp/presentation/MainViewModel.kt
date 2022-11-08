package ru.hivislav.shoppinglistapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import ru.hivislav.shoppinglistapp.data.ShopListRepositoryImpl
import ru.hivislav.shoppinglistapp.domain.DeleteShopItemUseCase
import ru.hivislav.shoppinglistapp.domain.EditShopItemUseCase
import ru.hivislav.shoppinglistapp.domain.GetShopListUseCase
import ru.hivislav.shoppinglistapp.domain.ShopItem

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
    }
}
package ru.hivislav.shoppinglistapp.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import ru.hivislav.shoppinglistapp.domain.ShopItem
import ru.hivislav.shoppinglistapp.presentation.ShopListApplication
import javax.inject.Inject

class MyContentProvider : ContentProvider() {

    private val component by lazy {
        (context as ShopListApplication).component
    }

    @Inject
    lateinit var shopListDao: ShopListDao

    @Inject
    lateinit var mapper: ShopListMapper

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("ru.hivislav.shoppinglistapp", "shop_items", GET_SHOP_ITEMS_QUERY)
        // # - любое число
        addURI("ru.hivislav.shoppinglistapp", "shop_items/#", GET_SHOP_ITEM_BY_ID_QUERY)
        // * - любая строка
        addURI("ru.hivislav.shoppinglistapp", "shop_items/*", GET_SHOP_ITEM_BY_NAME_QUERY)
    }

    override fun onCreate(): Boolean {
        component.inject(this)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        val code = uriMatcher.match(uri)
        return when (code) {
            GET_SHOP_ITEMS_QUERY -> {
                shopListDao.getShopListCursor()
            }
            else -> {
                null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                if (values == null) return null
                val id = values.getAsInteger("id")
                val name = values.getAsString("name")
                val count = values.getAsInteger("count")
                val enabled = values.getAsBoolean("enabled")

                val shopItem = ShopItem(id = id, name = name, count = count, enabled = enabled)
                shopListDao.addShopItemFromContentResolver(mapper.mapEntityToDbModel(shopItem))
            }
        }
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            GET_SHOP_ITEMS_QUERY -> {
                val id = selectionArgs?.get(0)?.toInt() ?: -1
                shopListDao.deleteShopItemFromContentResolver(id)
            }
            else -> {
                0
            }
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
    }

    companion object {
        private const val GET_SHOP_ITEMS_QUERY = 991
        private const val GET_SHOP_ITEM_BY_ID_QUERY = 118
        private const val GET_SHOP_ITEM_BY_NAME_QUERY = 667
    }
}
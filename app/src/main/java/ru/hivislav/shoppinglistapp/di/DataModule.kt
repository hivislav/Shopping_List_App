package ru.hivislav.shoppinglistapp.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.hivislav.shoppinglistapp.data.AppDatabase
import ru.hivislav.shoppinglistapp.data.ShopListDao
import ru.hivislav.shoppinglistapp.data.ShopListRepositoryImpl
import ru.hivislav.shoppinglistapp.domain.ShopListRepository

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }
    }
}
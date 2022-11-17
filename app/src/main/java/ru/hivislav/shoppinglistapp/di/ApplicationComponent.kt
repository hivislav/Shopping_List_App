package ru.hivislav.shoppinglistapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.hivislav.shoppinglistapp.presentation.MainActivity
import ru.hivislav.shoppinglistapp.presentation.ShopItemFragment

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: ShopItemFragment)

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
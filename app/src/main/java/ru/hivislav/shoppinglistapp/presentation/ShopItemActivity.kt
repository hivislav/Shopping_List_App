package ru.hivislav.shoppinglistapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.hivislav.shoppinglistapp.databinding.ActivityShopItemBinding

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
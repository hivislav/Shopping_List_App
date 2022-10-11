package ru.hivislav.shoppinglistapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.hivislav.shoppinglistapp.R
import ru.hivislav.shoppinglistapp.databinding.ActivityShopItemBinding
import ru.hivislav.shoppinglistapp.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding
    private lateinit var viewModel: ShopItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        when(screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }

        setupTextChangedListeners()
        observeViewModels()
    }

    private fun observeViewModels() {
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.error_name_text_input_shop_item)
            } else {
                null
            }
            binding.textInputShopItemActivityName.error = message
        }

        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                getString(R.string.error_count_text_input_shop_item)
            } else {
                null
            }
            binding.textInputShopItemActivityCount.error = message
        }

        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }
    }

    private fun setupTextChangedListeners() {
        binding.editTextShopItemActivityName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.editTextShopItemActivityCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            with(binding) {
                editTextShopItemActivityName.setText(it.name)
                editTextShopItemActivityCount.setText(it.id.toString())
            }
        }

        with(binding) {
            buttonSaveShopItemActivity.setOnClickListener {
                viewModel.editShopItem(
                    editTextShopItemActivityName.text?.toString(),
                    editTextShopItemActivityCount.text?.toString()
                )
            }
        }
    }

    private fun launchAddMode() {
        with(binding) {
            buttonSaveShopItemActivity.setOnClickListener {
                viewModel.addShopItem(
                    editTextShopItemActivityName.text?.toString(),
                    editTextShopItemActivityCount.text?.toString()
                )
            }
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }

        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}
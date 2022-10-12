package ru.hivislav.shoppinglistapp.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.hivislav.shoppinglistapp.R
import ru.hivislav.shoppinglistapp.databinding.FragmentShopItemBinding
import ru.hivislav.shoppinglistapp.domain.ShopItem

class ShopItemFragment: Fragment() {

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var viewModel: ShopItemViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }

        setupTextChangedListeners()
        observeViewModels()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModels() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_name_text_input_shop_item)
            } else {
                null
            }
            binding.textInputShopItemActivityName.error = message
        }

        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_count_text_input_shop_item)
            } else {
                null
            }
            binding.textInputShopItemActivityCount.error = message
        }

        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
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
        viewModel.shopItem.observe(viewLifecycleOwner) {
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

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }

        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode

        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    interface OnEditingFinishedListener {
        fun onEditingFinished()
    }

        companion object {
            private const val SCREEN_MODE = "extra_mode"
            private const val SHOP_ITEM_ID = "extra_shop_item_id"
            private const val MODE_EDIT = "mode_edit"
            private const val MODE_ADD = "mode_add"
            private const val MODE_UNKNOWN = ""

            fun newInstanceAddItem(): ShopItemFragment {
                return ShopItemFragment().apply {
                    arguments = Bundle().apply {
                        putString(SCREEN_MODE, MODE_ADD)
                    }
                }
            }

            fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
                return ShopItemFragment().apply {
                    arguments = Bundle().apply {
                        putString(SCREEN_MODE, MODE_EDIT)
                        putInt(SHOP_ITEM_ID, shopItemId)
                    }
                }
            }
        }
}
package ru.hivislav.shoppinglistapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.hivislav.shoppinglistapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel
    private val adapter = ShopListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            adapter.shopItemList = it
        }
    }

    private fun setupRecyclerView() {
        val recyclerViewShopList = binding.rvShopList

        with(binding) {
            rvShopList.adapter = adapter
            rvShopList.recycledViewPool.setMaxRecycledViews(SHOP_ITEM_VIEW_TYPE_ENABLED, MAX_POOL_SIZE)
            rvShopList.recycledViewPool.setMaxRecycledViews(SHOP_ITEM_VIEW_TYPE_DISABLED, MAX_POOL_SIZE)
        }

        setupShopItemLongClickListener()
        setupShopItemClickListener()
        setupShopItemSwipeListener(recyclerViewShopList)
    }

    private fun setupShopItemSwipeListener(recyclerViewShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.shopItemList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerViewShopList)
    }

    private fun setupShopItemClickListener() {
        adapter.onShopItemClickListener = {
            Log.d("@@@", "${it.name} был нажат")
        }
    }

    private fun setupShopItemLongClickListener() {
        adapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }
}
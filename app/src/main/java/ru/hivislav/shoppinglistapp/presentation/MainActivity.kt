package ru.hivislav.shoppinglistapp.presentation

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.hivislav.shoppinglistapp.R
import ru.hivislav.shoppinglistapp.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel
    private val adapter = ShopListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as ShopListApplication).component.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerView()
        setupFabClickListener()

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            adapter.submitList(it)
        }

        contentResolver.query(
            Uri.parse("content://ru.hivislav.shoppinglistapp/shop_items/55"),
            null,
            null,
            null,
            null
        )
    }

    private fun landscapeMode(): Boolean {
        return binding.shopItemContainer != null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.shopItemContainer, fragment)
            .addToBackStack(null)
            .commit()
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

    private fun setupFabClickListener() {
        binding.fabAddShopItem.setOnClickListener {
            if (landscapeMode()) {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            } else {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            }
        }
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
                val item = adapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerViewShopList)
    }

    private fun setupShopItemClickListener() {
        adapter.onShopItemClickListener = {
            if (landscapeMode()) {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            } else {

                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            }
        }
    }

    private fun setupShopItemLongClickListener() {
        adapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
    }
}
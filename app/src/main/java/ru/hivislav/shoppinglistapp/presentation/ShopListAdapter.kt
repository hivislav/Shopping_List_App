package ru.hivislav.shoppinglistapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hivislav.shoppinglistapp.databinding.ItemShopDisabledBinding
import ru.hivislav.shoppinglistapp.databinding.ItemShopEnabledBinding
import ru.hivislav.shoppinglistapp.domain.ShopItem

const val SHOP_ITEM_VIEW_TYPE_ENABLED = 0
const val SHOP_ITEM_VIEW_TYPE_DISABLED = 1
const val MAX_POOL_SIZE = 15

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopItemBaseViewHolder>() {

    var shopItemList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemBaseViewHolder {
        return when (viewType) {
            SHOP_ITEM_VIEW_TYPE_ENABLED -> {
                val binding = ItemShopEnabledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ShopItemEnabledViewHolder(binding)
            }
            else -> {
                val binding = ItemShopDisabledBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ShopItemDisabledViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ShopItemBaseViewHolder, position: Int) {
        holder.bind(shopItemList[position])
        holder.itemView.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(shopItemList[position])
            true
        }

        holder.itemView.setOnClickListener {
            onShopItemClickListener?.invoke(shopItemList[position])
        }
    }

    override fun getItemCount(): Int {
       return shopItemList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(shopItemList[position].enabled) {
            true -> SHOP_ITEM_VIEW_TYPE_ENABLED
            false -> SHOP_ITEM_VIEW_TYPE_DISABLED
        }
    }

    abstract class ShopItemBaseViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(shopItem: ShopItem)
    }

    class ShopItemEnabledViewHolder(private val binding: ItemShopEnabledBinding)
        : ShopItemBaseViewHolder(binding.root) {
        override fun bind(shopItem: ShopItem) {
            itemView.apply {
                binding.rvShopListItemName.text = shopItem.name
                binding.rvShopListItemCount.text = shopItem.count.toString()
            }
        }
    }

    class ShopItemDisabledViewHolder(private val binding: ItemShopDisabledBinding)
        : ShopItemBaseViewHolder(binding.root) {
        override fun bind(shopItem: ShopItem) {
            itemView.apply {
                binding.rvShopListItemName.text = shopItem.name
                binding.rvShopListItemCount.text = shopItem.count.toString()
            }
        }
    }
}
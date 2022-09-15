package ru.hivislav.shoppinglistapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.hivislav.shoppinglistapp.databinding.ItemShopEnabledBinding
import ru.hivislav.shoppinglistapp.domain.ShopItem

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopItemList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val binding = ItemShopEnabledBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        holder.bind(shopItemList[position])
    }

    override fun getItemCount(): Int {
       return shopItemList.size
    }

    class ShopItemViewHolder(private val binding: ItemShopEnabledBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(shopItem: ShopItem) {
            itemView.apply {
                binding.rvShopListItemName.text = shopItem.name
                binding.rvShopListItemCount.text = shopItem.count.toString()
            }
        }
    }
}
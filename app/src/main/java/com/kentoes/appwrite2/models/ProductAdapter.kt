package com.kentoes.appwrite2.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.kentoes.appwrite2.R
import com.kentoes.appwrite2.datas.Product

class ProductAdapter :
    ListAdapter<Product, ProductViewHolder>(object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.sku == newItem.sku
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_product, parent, false
            )

        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = currentList[position]
        holder.setName(item.name)
        holder.setPrice(item.price.toString())
        holder.setProductImage(item.imageUrl)
    }

    fun submitNext(product: Product) {
        val current = currentList.toMutableList()
        val index = current.indexOfFirst { it.sku == product.sku }
        if (index != -1) current[index] = product
        else current.add(product)
        submitList(current)
    }

    fun submitDeleted(product: Product) {
        submitList(
            currentList.toMutableList().apply { remove(product) }
        )
    }
}
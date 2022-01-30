package com.worldbeers.beerista.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.worldbeers.beerista.databinding.BeerItemBinding
import com.worldbeers.beerista.databinding.FoodPairingItemBinding
import com.worldbeers.beerista.domain.Beer
import com.worldbeers.beerista.home.BeerViewHolder

class FoodPairingAdapter() :
    RecyclerView.Adapter<FoodPairingViewHolder>() {

    private var foodPairingList: List<String> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodPairingViewHolder {
        val binding = FoodPairingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodPairingViewHolder(binding)
    }

    // every element of the list is connected to a ViewHolder
    override fun onBindViewHolder(holder: FoodPairingViewHolder, position: Int) {
        holder.bind(foodPairingList.get(position))
    }

    override fun getItemCount(): Int {
        return foodPairingList.size
    }

    fun setFoodPairingList(items: List<String>) {
        foodPairingList = items
        notifyDataSetChanged()
    }
}


// ViewHolder is an element of the list.
// For each element of the list a Viewholder is created.
class FoodPairingViewHolder(private val binding: FoodPairingItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(foodPairing : String) {
        binding.foodPairingItem.text = "- "+foodPairing
    }
}



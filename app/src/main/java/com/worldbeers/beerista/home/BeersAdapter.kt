package com.worldbeers.beerista.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.worldbeers.beerista.databinding.BeerItemBinding
import com.worldbeers.beerista.domain.Beer

class BeersAdapter(val context: Context, private val onClick: (Beer) -> Unit) :
    RecyclerView.Adapter<BeerViewHolder>() {

    private var beerList: List<Beer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = BeerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeerViewHolder(binding)
    }

    // every element of the list is connected to a ViewHolder
    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        holder.bind(beerList.get(position), onClick, context)
    }

    override fun getItemCount(): Int {
        return beerList.size
    }

    fun setBeersList(items: List<Beer>) {
        beerList = items
        notifyDataSetChanged()
    }
}


/**
 * ViewHolder is an element of the list.
 * For each element of the list a Viewholder is created.
 */
class BeerViewHolder(private val binding: BeerItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(beer: Beer, onClick: (Beer) -> Unit, context: Context) {

        Glide.with(context).load(beer.image).into(binding.beerImage)
        binding.beerName.text = beer.name
        binding.beerDescription.text = beer.description
        binding.beerAlcoholLevel.text = beer.abv.toString()
        binding.beerIbuValue.text = beer.ibu.toString()
        binding.root.setOnClickListener {
            onClick(beer)
        }
    }
}



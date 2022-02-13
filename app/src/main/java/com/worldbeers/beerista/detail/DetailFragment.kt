package com.worldbeers.beerista.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.worldbeers.beerista.R
import com.worldbeers.beerista.databinding.DetailFragmentBinding
import com.worldbeers.beerista.domain.Beer
import com.worldbeers.beerista.utils.bindings.viewBinding


class DetailFragment : Fragment(R.layout.detail_fragment) {
    private val args: DetailFragmentArgs by navArgs()
    private val binding by viewBinding(DetailFragmentBinding::bind)
    lateinit var beer : Beer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beer = args.beer
        getActivity()?.setTitle("Search");
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val first_brewed = beer.first_brewed
        val brewers_tips = beer.brewers_tips

        val adapter = FoodPairingAdapter()
        adapter.setFoodPairingList(beer.food_pairing)

        binding.foodPairingRecyclerView.adapter = adapter
        binding.brewersTipsText.text = brewers_tips
        binding.firstBrewedText.text = first_brewed
    }
}

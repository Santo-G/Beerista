package com.worldbeers.beerista.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.worldbeers.beerista.R
import com.worldbeers.beerista.databinding.HomeFragmentBinding
import com.worldbeers.beerista.utils.bindings.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : Fragment(R.layout.home_fragment) {
    private val viewModel: HomeViewModel by viewModel()
    private val binding by viewBinding(HomeFragmentBinding::bind)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.send(HomeScreenEvents.OnReady)
        requireActivity().setTitle(R.string.app_name)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val beersAdapter = BeersAdapter(context = requireContext()) { beer ->
            viewModel.send(HomeScreenEvents.OnBeerClick(beer))
        }

        binding.swiperefresh.setOnRefreshListener {
            viewModel.send(HomeScreenEvents.OnRefreshClicked)
        }

        binding.beersList.adapter = beersAdapter

        observeStates(beersAdapter, binding)
        observeActions()
    }

    private fun observeStates(
        adapter: BeersAdapter,
        homeFragmentBinding: HomeFragmentBinding,
    ) {
        viewModel.states.observe(viewLifecycleOwner) { state ->
            Timber.d(state.toString())
            when (state) {
                is HomeScreenStates.Content -> setupContent(homeFragmentBinding, adapter, state)
                is Error -> setupError(state as HomeScreenStates.Error, homeFragmentBinding)
                HomeScreenStates.Loading -> homeFragmentBinding.swiperefresh.isRefreshing = true
            }
        }
    }


    private fun setupContent(
        homeFragmentBinding: HomeFragmentBinding,
        adapter: BeersAdapter,
        state: HomeScreenStates.Content
    ) {
        homeFragmentBinding.swiperefresh.isRefreshing = false
        adapter.setBeersList(state.generalContent.beersList)
        binding.beersList.scrollToPosition(0)
        errorVisibilityGone(homeFragmentBinding)
        homeFragmentBinding.innerLayout.visibility = View.VISIBLE
    }

    private fun errorVisibilityGone(errorBinding: HomeFragmentBinding) {
        errorBinding.innerLayoutServerError.root.visibility = View.GONE
        errorBinding.innerLayoutNoBeerFound.root.visibility = View.GONE
    }

    private fun setupError(state: HomeScreenStates.Error, homeFragmentBinding: HomeFragmentBinding) {
        when (state.error) {
            ErrorStates.ShowNoBeerFound -> {
                errorCustom("No Cocktail Found", homeFragmentBinding)
                homeFragmentBinding.innerLayoutNoBeerFound.root.visibility = View.VISIBLE
            }
            ErrorStates.ShowServerError -> {
                errorCustom("Server Error", homeFragmentBinding)
                homeFragmentBinding.innerLayoutServerError.root.visibility = View.VISIBLE
            }
        }
    }

    private fun errorCustom(error: String, binding: HomeFragmentBinding) {
        binding.innerLayout.visibility = View.GONE
        binding.swiperefresh.isRefreshing = false
    }


    private fun observeActions() {
        viewModel.actions.observe(viewLifecycleOwner) { action ->
            Timber.d(action.toString())
            when (action) {
                is HomeScreenActions.NavigateToDetail -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(action.beer))
                }
            }
        }
    }
}

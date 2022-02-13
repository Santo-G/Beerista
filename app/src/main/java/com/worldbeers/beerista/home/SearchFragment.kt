package com.worldbeers.beerista.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.worldbeers.beerista.R
import com.worldbeers.beerista.databinding.FragmentSearchBinding
import com.worldbeers.beerista.domain.Beer
import com.worldbeers.beerista.utils.bindings.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class SearchFragment : Fragment(R.layout.fragment_search) {
    private val args: SearchFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModel()
    var beerList: ArrayList<Beer>? = ArrayList()
    lateinit var from: String
    lateinit var to: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = args.from
        to = args.to
        viewModel.send(SearchScreenEvents.OnReady(from, to))
        getActivity()?.setTitle("Search");
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val beersAdapter = BeersAdapter(context = requireContext()) { beer ->
            viewModel.send(SearchScreenEvents.OnBeerClick(beer))
        }

        binding.beersList.adapter = beersAdapter

        beersAdapter.setBeersList(beerList ?: ArrayList())

        observeStates(beersAdapter, binding)
        observeActions()
    }

    private fun observeStates(
        adapter: BeersAdapter,
        SearchFragmentBinding: FragmentSearchBinding,
    ) {
        viewModel.states.observe(viewLifecycleOwner) { state ->
            Timber.d(state.toString())
            when (state) {
                is SearchScreenStates.Content -> setupContent(SearchFragmentBinding, adapter, state)
                is SearchScreenStates.Error -> {
                    setupError(state as SearchScreenStates.Error, SearchFragmentBinding)
                }
            }
        }
    }

    private fun observeActions() {
        viewModel.actions.observe(viewLifecycleOwner) { action ->
            Timber.d(action.toString())
            when (action) {
                is SearchScreenActions.NavigateToDetail -> {
                    findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(action.beer))
                }
            }
        }
    }

    private fun setupContent(
        fragmentSearchBinding: FragmentSearchBinding,
        adapter: BeersAdapter,
        state: SearchScreenStates.Content
    ) {
        if (state.beerList.isEmpty()) {
            fragmentSearchBinding.innerLayoutNoBeerFound.root.visibility = View.VISIBLE
        } else {
            fragmentSearchBinding.innerLayoutNoBeerFound.root.visibility = View.GONE
            state.beerList.map { beer ->
                beerList?.add(beer)
            }
            adapter.notifyDataSetChanged()
            errorVisibilityGone(fragmentSearchBinding)
            fragmentSearchBinding.beersList.visibility = View.VISIBLE
        }
    }

    private fun setupError(state: SearchScreenStates.Error, SearchFragmentBinding: FragmentSearchBinding) {
        when (state.error) {
            ErrorStates.ShowNoBeerFound -> {
                SearchFragmentBinding.innerLayoutNoBeerFound.root.visibility = View.VISIBLE
            }
            ErrorStates.ShowServerError -> {
                SearchFragmentBinding.innerLayoutServerError.root.visibility = View.VISIBLE
            }
        }
    }

    private fun errorVisibilityGone(errorBinding: FragmentSearchBinding) {
        errorBinding.innerLayoutServerError.root.visibility = View.GONE
        errorBinding.innerLayoutNoBeerFound.root.visibility = View.GONE
    }
}

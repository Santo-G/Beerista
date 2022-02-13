package com.worldbeers.beerista.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.worldbeers.beerista.R
import com.worldbeers.beerista.databinding.HomeFragmentBinding
import com.worldbeers.beerista.domain.Beer
import com.worldbeers.beerista.utils.bindings.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class HomeFragment : Fragment(R.layout.home_fragment) {
    private val viewModel: HomeViewModel by viewModel()
    private val binding by viewBinding(HomeFragmentBinding::bind)
    private var pageNum: Int = 0
    var beerList: ArrayList<Beer>? = ArrayList()

    companion object {
        var listPosition = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.send(HomeScreenEvents.OnReady)
        requireActivity().setTitle(R.string.app_name)
        // this Home has a menu
        setHasOptionsMenu(true)
        getActivity()?.setTitle("Home");
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

        beersAdapter.setBeersList(beerList ?: ArrayList())

        binding.beersList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    listPosition = binding.beersList.getCurrentPosition()
                    viewModel.send(HomeScreenEvents.OnScroll(pageNum++))
                }
            }
        })

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
        if (state.beerList.isEmpty()) {
            homeFragmentBinding.innerLayoutNoBeerFound.root.visibility = View.VISIBLE
        } else {
            homeFragmentBinding.innerLayoutNoBeerFound.root.visibility = View.GONE
            state.beerList.map { beer ->
                beerList?.add(beer)
            }
            adapter.notifyItemInserted(listPosition + 1)
            errorVisibilityGone(homeFragmentBinding)
            homeFragmentBinding.beersList.visibility = View.VISIBLE
        }
    }

    private fun errorVisibilityGone(errorBinding: HomeFragmentBinding) {
        errorBinding.innerLayoutServerError.root.visibility = View.GONE
        errorBinding.innerLayoutNoBeerFound.root.visibility = View.GONE
    }

    private fun setupError(state: HomeScreenStates.Error, homeFragmentBinding: HomeFragmentBinding) {
        when (state.error) {
            ErrorStates.ShowNoBeerFound -> {
                homeFragmentBinding.swiperefresh.isRefreshing = false
                homeFragmentBinding.innerLayoutNoBeerFound.root.visibility = View.VISIBLE
            }
            ErrorStates.ShowServerError -> {
                homeFragmentBinding.swiperefresh.isRefreshing = false
                homeFragmentBinding.innerLayoutServerError.root.visibility = View.VISIBLE
            }
        }
    }

    private fun observeActions() {
        viewModel.actions.observe(viewLifecycleOwner) { action ->
            Timber.d(action.toString())
            when (action) {
                is HomeScreenActions.NavigateToDetail -> {
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(action.beer))
                }
                is HomeScreenActions.NavigateToSearch -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSearchFragment(
                            action.from!!,
                            action.to!!
                        )
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filter_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.id.filter_item -> {
                showDialog()
            }
        }
        return true
    }

    private fun showDialog() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        val layout = LayoutInflater.from(requireContext()).inflate(R.layout.filter_dialog, null)

        var textFieldFrom = layout.findViewById<EditText>(R.id.outlinedTextFieldFrom)
        var textFieldTo = layout.findViewById<EditText>(R.id.outlinedTextFieldTo)
        val text_error_message = layout.findViewById<TextView>(R.id.error_filter_message)

        alert.setView(layout)
        alert.setPositiveButton(getString(R.string.yes_button), null)
        alert.setNegativeButton(R.string.no_button, DialogInterface.OnClickListener { dialog, whichButton ->
            // Alert Canceled
        })

        val alertDialog = alert.create()

        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    if (textFieldFrom.text.isEmpty() || textFieldTo.text.isEmpty()) {
                        text_error_message.visibility = View.VISIBLE
                    } else {
                        text_error_message.visibility = View.GONE
                        viewModel.send(
                            HomeScreenEvents.OnSearch(
                                textFieldFrom.text.toString(),
                                textFieldTo.text.toString()
                            )
                        )
                        alertDialog.dismiss()
                    }
                }

            }
        )
    }

    // extension function
    fun RecyclerView?.getCurrentPosition(): Int {
        return (this?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
    }

}

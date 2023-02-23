package ru.nikitazar.bnet.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nikitazar.bnet.databinding.FragmentDrugsListBinding
import ru.nikitazar.bnet.ui.adapters.DrugAdapter
import ru.nikitazar.bnet.ui.adapters.PageLoadStateAdapter
import ru.nikitazar.bnet.ui.utils.simpleScan
import ru.nikitazar.bnet.viewModel.DrugsViewModel

@AndroidEntryPoint
class DrugsListFragment : Fragment() {

    lateinit var binding: FragmentDrugsListBinding
    private val viewModel: DrugsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrugsListBinding.inflate(layoutInflater, container, false)

        setupList()
        setupSearchInput()
//        setupSwipeToRefresh()

        return binding.root
    }

    private fun setupList() {
        val adapter = DrugAdapter()
        val footerAdapter = PageLoadStateAdapter()
        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)
        binding.list.adapter = adapterWithLoadState
        observeDrugs(adapter)
//        scrollingToTopWhenSearching(adapter)
    }

    private fun observeDrugs(adapter: DrugAdapter) = lifecycleScope.launchWhenCreated {
        viewModel.pageDataFlow.collectLatest { pagingData ->
            adapter.submitData(pagingData)
        }
    }

    private fun setupSearchInput() {
        //TODO
        //viewModel.setSearch()
    }

//    private fun setupSwipeToRefresh() {
//        binding.swipeRefresh.setOnRefreshListener {
//            viewModel.refresh()
//        }
//    }
//
//    private fun scrollingToTopWhenSearching(adapter: DrugAdapter) = lifecycleScope.launch {
//        getRefreshLoadStateFlow(adapter)
//            .simpleScan(count = 2)
//            .collectLatest { (previousState, currentState) ->
//                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
//                    binding.list.scrollToPosition(0)
//                    binding.swipeRefresh.isRefreshing = false
//                }
//            }
//    }

    private fun getRefreshLoadStateFlow(adapter: DrugAdapter) = adapter.loadStateFlow.map { it.refresh }

}
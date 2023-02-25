package ru.nikitazar.bnet.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.nikitazar.bnet.R
import ru.nikitazar.bnet.databinding.FragmentDrugsListBinding
import ru.nikitazar.bnet.ui.adapters.DrugAdapter
import ru.nikitazar.bnet.ui.adapters.PageLoadStateAdapter
import ru.nikitazar.bnet.ui.utils.simpleScan
import ru.nikitazar.bnet.viewModel.DrugsViewModel


@AndroidEntryPoint
class DrugsListFragment : Fragment(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    lateinit var binding: FragmentDrugsListBinding
    private val viewModel: DrugsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        setupSearch(menu, inflater)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onMenuItemActionExpand(p0: MenuItem) = true

    override fun onMenuItemActionCollapse(p0: MenuItem) = true

    override fun onQueryTextSubmit(query: String?): Boolean {
        setSearch(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            if (it.isEmpty()) {
                setSearch(it)
            }
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDrugsListBinding.inflate(layoutInflater, container, false)

        setupList()
        setupSwipeToRefresh()

        return binding.root
    }

    private fun setupList() {
        val adapter = DrugAdapter()
        val footerAdapter = PageLoadStateAdapter()
        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)
        binding.list.adapter = adapterWithLoadState
        observeDrugs(adapter)
        scrollingToTopWhenSearching(adapter)
    }

    private fun setupSearch(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val menuItem = menu.findItem(R.id.m_search)
        val searchView = menuItem.actionView as SearchView
        searchView.apply {
            setOnQueryTextListener(this@DrugsListFragment)
            queryHint = getString(R.string.search_title)
        }
    }

    private fun observeDrugs(adapter: DrugAdapter) = lifecycleScope.launchWhenCreated {
        viewModel.pageDataFlow.collectLatest { pagingData ->
            adapter.submitData(pagingData)
        }
    }

    private fun setSearch(query: String?) {
        viewModel.setSearch(query ?: "")
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun scrollingToTopWhenSearching(adapter: DrugAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 2)
            .collectLatest { (previousState, currentState) ->
                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                    binding.list.scrollToPosition(0)
                    binding.swipeRefresh.isRefreshing = false
                }
            }
    }

    private fun getRefreshLoadStateFlow(adapter: DrugAdapter) = adapter.loadStateFlow.map { it.refresh }

}
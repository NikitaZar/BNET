package ru.nikitazar.bnet.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.nikitazar.bnet.R
import ru.nikitazar.bnet.databinding.FragmentDrugsListBinding
import ru.nikitazar.bnet.ui.DrugDetailsFragment.Companion.intArg
import ru.nikitazar.bnet.ui.adapters.*
import ru.nikitazar.bnet.ui.utils.simpleScan
import ru.nikitazar.bnet.viewModel.*


@AndroidEntryPoint
class DrugsListFragment : Fragment(), SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    lateinit var binding: FragmentDrugsListBinding
    private val viewModel: ListViewModel by viewModels()

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
        val adapter = DrugAdapter(navigateToDetails())
        val footerAdapter = PageLoadStateAdapter()
        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)
        binding.list.adapter = adapterWithLoadState
        observeDrugs(adapter)
        observeErrors(adapter)
        scrollingToTopWhenSearching(adapter)
    }

    private fun navigateToDetails() = object : OnInteractionListener {
        override fun onItemClickListener(id: Int) {
            findNavController().navigate(
                resId = R.id.action_drugsListFragment_to_drugDetailsFragment,
                args = Bundle().apply { intArg = id }
            )
        }
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

    private fun observeErrors(adapter: DrugAdapter) {
        adapter.addLoadStateListener {
            if (it.refresh is LoadState.Error) {
                val errText = getString(R.string.err_req_mes)
                val btText = getString(R.string.err_bt_snack_bar_text)
                Snackbar.make(binding.root, errText, Snackbar.LENGTH_INDEFINITE)
                    .setAction(btText) { viewModel.refresh() }
                    .show()
            }
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
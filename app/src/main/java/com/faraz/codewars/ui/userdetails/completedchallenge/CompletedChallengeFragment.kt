package com.faraz.codewars.ui.userdetails.completedchallenge

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.faraz.codewars.databinding.FragmentCompletedChallengeBinding
import com.faraz.codewars.ui.userdetails.completedchallenge.CompletedChallengeViewModel
import com.faraz.codewars.ui.userdetails.completedchallenge.CompletedLoadingStateAdapter
import com.faraz.codewars.models.UserChallengeData
import com.faraz.codewars.ui.challengedetails.ChallengeDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


@AndroidEntryPoint
class CompletedChallengeFragment : Fragment(), CompletedAdapterClickListener {

    private var _binding: FragmentCompletedChallengeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompletedChallengeViewModel by viewModels()
    private lateinit var userName: String
    private var searchJob: Job? = null
    private val adapter =
        CompletedChallengeAdapter { this }
    var isManualRefresh = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            userName = it.getString("userName")!!

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()

    }

    @ExperimentalPagingApi
    private fun initUi() {
        setUpAdapter()
        startSearchJob()

        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            isManualRefresh = true

        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                adapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collectLatest { loadStates ->

                        binding.swipeRefreshLayout.isRefreshing =
                            loadStates.refresh is LoadState.Loading

                        if (loadStates.refresh is LoadState.NotLoading && isManualRefresh) {
                            binding.rcvCompletedChallenge.scrollToPosition(0)
                            isManualRefresh = false
                        }
                    }
            }
        }
    }

    @ExperimentalPagingApi
    private fun startSearchJob() {
        viewModel.setUserName(userName)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.completedChallenges.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setUpAdapter() {

        binding.rcvCompletedChallenge.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        binding.rcvCompletedChallenge.adapter = adapter.withLoadStateFooter(
            footer = CompletedLoadingStateAdapter { retry() }
        )

        adapter.addLoadStateListener { loadState ->

            if (loadState.mediator?.refresh is LoadState.Loading) {

                if (adapter.snapshot().isEmpty()) {
                    binding.progressBar.visibility = View.VISIBLE
                }
                binding.txvError.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false

                val error = when {
                    loadState.mediator?.prepend is LoadState.Error -> loadState.mediator?.prepend as LoadState.Error
                    loadState.mediator?.append is LoadState.Error -> loadState.mediator?.append as LoadState.Error
                    loadState.mediator?.refresh is LoadState.Error -> loadState.mediator?.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    if (adapter.snapshot().isEmpty()) {
                        binding.txvError.visibility = View.VISIBLE
                        binding.txvError.text = it.error.localizedMessage
                    }
                }
            }
        }
    }

    private fun retry() {
        adapter.retry()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun itemClicked(data: UserChallengeData) {
        val intent = Intent(this.context, ChallengeDetailsActivity::class.java)
        intent.putExtra("challenge_id", data.id)
        startActivity(intent)
    }
}
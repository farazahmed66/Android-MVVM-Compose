package com.faraz.codewars.ui.userdetails.authoredchallenge

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.faraz.codewars.R
import com.faraz.codewars.databinding.FragmentAuthoredChallengeBinding
import com.faraz.codewars.models.AuthoredChallenge
import com.faraz.codewars.models.AuthoredChallengeData
import com.faraz.codewars.network.Resource
import com.faraz.codewars.ui.challengedetails.ChallengeDetailsActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AuthoredChallengeFragment : Fragment(), AuthoredAdapterClickListener {

    private val viewModel: AuthoredChallengeViewModel by viewModels()

    private var _binding: FragmentAuthoredChallengeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userName: String
    private lateinit var authoredChallengeAdapter: AuthoredChallengeAdapter

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
        _binding = FragmentAuthoredChallengeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()

    }

    private fun initUi() {
        setupRecyclerView()
        observeData()
        viewModel.getAuthoredChallenge(userName)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getAuthoredChallenge(userName)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAuthoredChallengeResponse.collect { res ->
                    handleAuthoredChallengeState(res)
                }
            }
        }
    }

    private fun handleAuthoredChallengeState(res: Resource<AuthoredChallenge>) {
        when (res) {

            is Resource.Success -> {
                val list = res.value.authoredChallengeData

                if (list.isNotEmpty()) {
                    showContent(list)
                } else {
                    showEmptyState()
                }
            }

            is Resource.Failure -> setError(res)

            Resource.Loading -> setLoading()

            Resource.Empty -> Unit
        }
    }

    private fun showContent(list: List<AuthoredChallengeData>) {
        binding.progressBar.isVisible = false
        binding.txvError.isVisible = false
        binding.rcvAuthoredChallenge.isVisible = true
        authoredChallengeAdapter.differ.submitList(list.reversed())
    }

    private fun showEmptyState() {
        binding.progressBar.isVisible = false
        binding.txvError.isVisible = true
        binding.rcvAuthoredChallenge.isVisible = false

        Snackbar.make(
            binding.root,
            getString(R.string.no_data),
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setLoading() {
        binding.progressBar.isVisible = true
        binding.txvError.isVisible = false
        binding.rcvAuthoredChallenge.isVisible = false
    }

    private fun setupRecyclerView() {
        authoredChallengeAdapter = AuthoredChallengeAdapter { this }
        binding.rcvAuthoredChallenge.apply {
            adapter = authoredChallengeAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setError(res: Resource.Failure) {
        binding.progressBar.isVisible = false
        binding.txvError.isVisible = true
        binding.rcvAuthoredChallenge.isVisible = false
        if (res.isNetworkError) {
            Snackbar.make(binding.root, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                .show()
        } else {
            Snackbar.make(
                binding.root,
                getText(R.string.something_went_wrong),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun itemClicked(data: AuthoredChallengeData) {
        val intent = Intent(this.context, ChallengeDetailsActivity::class.java)
        intent.putExtra("challenge_id", data.id)
        startActivity(intent)
    }

}
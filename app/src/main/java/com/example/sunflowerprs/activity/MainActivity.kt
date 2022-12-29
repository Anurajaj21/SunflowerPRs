package com.example.sunflowerprs.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunflowerprs.R
import com.example.sunflowerprs.adapter.PullReqAdapter
import com.example.sunflowerprs.databinding.ActivityMainBinding
import com.example.sunflowerprs.databinding.LayoutErrorDialogBinding
import com.example.sunflowerprs.model.PullReqModel
import com.example.sunflowerprs.utils.AppUtils.isNetworkAvailable
import com.example.sunflowerprs.utils.AppUtils.toPixel
import com.example.sunflowerprs.utils.PaginationListener
import com.example.sunflowerprs.viewModel.MainViewModel
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val prAdapter by lazy { PullReqAdapter() }
    private lateinit var viewModel: MainViewModel

    private var currentPage: Int = 1
    private var isLastPage = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initViews()
        initListeners()
        setObservers()
        initVariable()
        if (isNetworkAvailable(this)) {
            if (viewModel.getTotalList().isEmpty()) getPullRequests()
        } else showErrorDialog(
            R.drawable.ic_no_internet,
            getString(R.string.oops_no_internet),
            getString(R.string.no_internet_msg)
        )
    }

    private fun initListeners() {
        binding.apply {
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.refresh -> {
                        handleRefresh()
                        true
                    }
                    else -> false
                }
            }
            rvPullReq.addOnScrollListener(object :
                PaginationListener(rvPullReq.layoutManager as LinearLayoutManager) {
                override fun isLoading(): Boolean = isLoading

                override fun isLastPage(): Boolean = isLastPage

                override fun loadMoreItems() {
                    currentPage++
                    viewModel.updateCurrentPage(currentPage)
                    isLoading = true
                    getPullRequests()
                }
            })
        }
    }

    private fun handleRefresh() {
        currentPage = 1
        viewModel.updateCurrentPage(currentPage)
        viewModel.clearList()
        isLastPage = false
        prAdapter.differ.submitList(arrayListOf())
        getPullRequests()
    }

    private fun showErrorDialog(icon: Int, title: String, message: String) {
        val bindingNoInternet = LayoutErrorDialogBinding.inflate(LayoutInflater.from(this))
        bindingNoInternet.apply {
            errorIcon.setImageResource(icon)
            errorTitle.text = title
            errorMessage.text = message
        }
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(bindingNoInternet.root)
            .show()
        bindingNoInternet.btnRetry.setOnClickListener {
            if (isNetworkAvailable(this)) {
                getPullRequests()
                dialog.dismiss()
            }
        }
    }

    private fun getPullRequests() {
        viewModel.getClosedPullRequests(currentPage)
    }

    private fun setObservers() {
        viewModel.loading.observe(this) {
            if (it && currentPage == 1) showLoading() else hideLoading()
        }

        viewModel.pullRequests.observe(this) { prList ->
            if (prList != null) handlePullRequestList(prList)
        }

        viewModel.error.observe(this) {
            when (it) {
                is IOException -> {
                    showErrorDialog(
                        R.drawable.ic_no_internet,
                        getString(R.string.oops_no_internet),
                        getString(R.string.no_internet_msg)
                    )
                }
                else -> {
                    if (it.message == "Api request limit exceed") showErrorDialog(
                        R.drawable.ic_limit_exceed,
                        getString(R.string.oops_limit_exceed),
                        getString(R.string.limit_exceed_msg)
                    )
                    else showToast(it.message.toString())
                }
            }
        }
    }

    private fun handlePullRequestList(list: ArrayList<PullReqModel>) {
        isLastPage = list.isEmpty()
        prAdapter.addItems(list, !isLastPage)
        isLoading = false
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        binding.apply {
            rvPullReq.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() {
        binding.apply {
            rvPullReq.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun initVariable() {
        currentPage = viewModel.getCurrentPage()
        viewModel.updatePullRequestList()
    }

    private fun initViews() {
        binding.apply {
            toolbar.inflateMenu(R.menu.main_menu)
            rvPullReq.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = prAdapter
                setHasFixedSize(true)
                if (itemDecorationCount == 0) {
                    val itemDecoration = object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            // set spacing for the first item
                            outRect.top =
                                if (getChildAdapterPosition(view) == 0) 10f.toPixel(resources) else 0
                            // add spacing between items except last item
                            outRect.bottom =
                                if (getChildAdapterPosition(view) != state.itemCount - 1) {
                                    10f.toPixel(resources)
                                } else 0
                        }
                    }
                    addItemDecoration(itemDecoration)
                }
            }
        }
    }
}
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
import com.example.sunflowerprs.databinding.LayoutNoInternetBinding
import com.example.sunflowerprs.utils.AppUtils.isNetworkAvailable
import com.example.sunflowerprs.utils.AppUtils.toPixel
import com.example.sunflowerprs.viewModel.MainViewModel
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val prAdapter by lazy { PullReqAdapter() }
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initViews()
        initListeners()
        setObservers()
        if (isNetworkAvailable(this)) getPullRequests()
        else showNoInternetDialog()
    }

    private fun initListeners() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.refresh -> {
                    getPullRequests()
                    true
                }
                else -> false
            }
        }
    }

    private fun showNoInternetDialog() {
        val bindingNoInternet = LayoutNoInternetBinding.inflate(LayoutInflater.from(this))
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
        viewModel.getClosedPullRequests(1)
    }

    private fun setObservers() {
        viewModel.loading.observe(this) {
            if (it) showLoading() else hideLoading()
        }

        viewModel.pullRequests.observe(this) { list ->
            if (list.isNotEmpty()) prAdapter.differ.submitList(list)
        }

        viewModel.error.observe(this) {
            when (it) {
                is IOException -> showNoInternetDialog()
                else -> showToast(it.message.toString())
            }
        }
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
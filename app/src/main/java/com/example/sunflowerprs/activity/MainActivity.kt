package com.example.sunflowerprs.activity

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunflowerprs.R
import com.example.sunflowerprs.adapter.PullReqAdapter
import com.example.sunflowerprs.databinding.ActivityMainBinding
import com.example.sunflowerprs.model.PullReqModel
import com.example.sunflowerprs.model.User
import com.example.sunflowerprs.utils.AppUtils.toPixel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val prAdapter by lazy { PullReqAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        createDummyData()
    }

    private fun createDummyData() {
        val list = arrayListOf<PullReqModel>()
        for (i in 1..20) {
            list.add(
                PullReqModel(
                    "Dummy PR Title",
                    "21/12/2022 02:45",
                    "25/12/2022 15:25",
                    User("NA")
                )
            )
        }
        prAdapter.differ.submitList(list)
    }

    private fun initViews() {
        binding.apply {
            toolbar.inflateMenu(R.menu.main_menu)
            rvPullReq.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = prAdapter
                if (itemDecorationCount == 0) {
                    val itemDecoration = object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            outRect.top = if(getChildAdapterPosition(view) == 0) 10f.toPixel(resources) else 0
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
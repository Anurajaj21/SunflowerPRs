package com.example.sunflowerprs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sunflowerprs.R
import com.example.sunflowerprs.databinding.ItemLoadingBinding
import com.example.sunflowerprs.databinding.ItemPrBinding
import com.example.sunflowerprs.model.PullReqModel

class PullReqAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_LOADING = 0
    private val VIEW_TYPE_PULL_REQUEST = 1
    private var isLoaderVisible = false


    private val differCallback = object : DiffUtil.ItemCallback<PullReqModel>() {
        override fun areItemsTheSame(oldItem: PullReqModel, newItem: PullReqModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PullReqModel, newItem: PullReqModel): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    class PullReqViewHolder(private val binding: ItemPrBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindPullRequest(data: PullReqModel) {
            binding.apply {
                prTitle.text = data.title
                createdAt.text = data.createdAt
                closedAt.text = data.closedAt
                owner.text = data.user?.name
                Glide.with(userImg)
                    .load(data.user?.profile)
                    .placeholder(R.drawable.ic_placeholder)
                    .circleCrop()
                    .into(userImg)
            }
        }
    }

    class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            VIEW_TYPE_PULL_REQUEST -> PullReqViewHolder(
                ItemPrBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> LoadingViewHolder(
                ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible && position == differ.currentList.size - 1) VIEW_TYPE_LOADING
        else VIEW_TYPE_PULL_REQUEST
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is PullReqViewHolder -> holder.onBindPullRequest(differ.currentList[position])
            else -> {}
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun addItems(list : ArrayList<PullReqModel>, loader : Boolean){
        val newList = arrayListOf<PullReqModel>()
        newList.addAll(differ.currentList)
        if (newList.isNotEmpty()) newList.removeLast()
        if (loader) {
            isLoaderVisible = true
            list.add(PullReqModel(null, null, null, null))
        }
        newList.addAll(list)
        differ.submitList(newList)
    }
}
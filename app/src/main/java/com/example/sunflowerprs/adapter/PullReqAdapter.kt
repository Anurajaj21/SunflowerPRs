package com.example.sunflowerprs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sunflowerprs.databinding.ItemPrBinding
import com.example.sunflowerprs.model.PullReqModel

class PullReqAdapter : RecyclerView.Adapter<PullReqAdapter.PullReqViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<PullReqModel>(){
        override fun areItemsTheSame(oldItem: PullReqModel, newItem: PullReqModel): Boolean {
            return  oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PullReqModel, newItem: PullReqModel): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallback)

    class PullReqViewHolder(private val binding: ItemPrBinding) : RecyclerView.ViewHolder(binding.root){
        fun onBind(data : PullReqModel){
            binding.apply {
                prTitle.text = data.title
                createdAt.text = data.createdAt
                closedAt.text = data.closedAt
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PullReqViewHolder {
        return PullReqViewHolder(
            ItemPrBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PullReqViewHolder, position: Int) {
        holder.onBind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}
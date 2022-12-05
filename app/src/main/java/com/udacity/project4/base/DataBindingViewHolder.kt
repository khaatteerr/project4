package com.udacity.project4.base

import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView



class DataBindingViewHolder<T>(private val binding: ViewDataBinding) :RecyclerView.ViewHolder(binding.root) {
    fun bind(t: T) {
        binding.setVariable(BR.item , t)
        binding.executePendingBindings()
    }
}
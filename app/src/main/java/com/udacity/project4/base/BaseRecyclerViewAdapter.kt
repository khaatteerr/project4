package com.udacity.project4.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T>(private val callback: ((item: T) -> Unit)? = null) :
    RecyclerView.Adapter<DataBindingViewHolder<T>>() {

    private var _myItems: MutableList<T> = mutableListOf()


    private val _items: List<T>
        get() = this._myItems


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {


        val binding = DataBindingUtil
            .inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                getLayoutRes(viewType),
                parent,
                false
            )

        binding.lifecycleOwner = getLifecycleOwner()

        return DataBindingViewHolder(binding)
    }


    override fun getItemCount() = _myItems.size

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            callback?.invoke(getItem(position))
        }
    }

    private fun getItem(position: Int) = _myItems[position]


    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        _myItems.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(items: List<T>) {
        _myItems.addAll(items)
        notifyDataSetChanged()
    }

    open fun getLifecycleOwner(): LifecycleOwner? {
        return null
    }

    @LayoutRes
    abstract fun getLayoutRes(viewType: Int): Int


}


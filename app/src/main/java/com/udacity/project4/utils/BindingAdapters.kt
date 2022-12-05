package com.udacity.project4.utils

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.udacity.project4.base.BaseRecyclerViewAdapter


object BindingAdapters {


    @BindingAdapter("android:fadeVisible")
    @JvmStatic
    fun setFadeVisible(view: View, visible: Boolean? = true) {

        if (view.tag == null) {
            view.tag = true
            view.visibility = if (visible == true) View.VISIBLE else View.GONE
        } else {

            view.animate().cancel()
            if (visible == true) {

                if (view.visibility == View.GONE)
                    view.fadeIn()
            } else {

                if (view.visibility == View.VISIBLE)
                    view.fadeOut()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("android:liveData")
    @JvmStatic
    fun <T> setRecyclerViewData(recyclerView: RecyclerView, items: LiveData<List<T>>?) {

        items?.value?.let { itemList ->

            (recyclerView.adapter as? BaseRecyclerViewAdapter<T>)?.apply {
                clear()

                addData(itemList)

            }
        }
    }



}
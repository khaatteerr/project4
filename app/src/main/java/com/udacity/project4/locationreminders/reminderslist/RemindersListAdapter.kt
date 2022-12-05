package com.udacity.project4.locationreminders.reminderslist

import com.udacity.project4.R
import com.udacity.project4.base.BaseRecyclerViewAdapter


 class RemindersListAdapter(back: (selectedReminder: ReminderDataItem) -> Unit)
     :
    BaseRecyclerViewAdapter<ReminderDataItem>(back) {



    override fun getLayoutRes(viewType: Int) = R.layout.it_reminder
}
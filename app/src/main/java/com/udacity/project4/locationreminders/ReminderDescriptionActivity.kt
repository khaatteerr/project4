package com.udacity.project4.locationreminders

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityReminderDescriptionBinding
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem


class ReminderDescriptionActivity : AppCompatActivity() {



    private lateinit var binding: ActivityReminderDescriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_reminder_description)

        binding.executePendingBindings()
        binding.reminderDataItem = intent.getSerializableExtra(EXTRA_ReminderDataItem) as ReminderDataItem?

    }

    companion object {


        private const val EXTRA_ReminderDataItem = "EXTRA_ReminderDataItem"


        fun newIntent(context: Context, reminderDataItem: ReminderDataItem): Intent {

            val i = Intent(context, ReminderDescriptionActivity::class.java)

            i.putExtra(EXTRA_ReminderDataItem, reminderDataItem)

            return i
        }
    }
}

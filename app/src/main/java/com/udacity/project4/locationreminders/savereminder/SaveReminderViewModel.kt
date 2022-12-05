package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.R
import com.udacity.project4.base.BaseViewModel
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.launch

class SaveReminderViewModel(val app: Application, val dataSource: ReminderDataSource) :
    BaseViewModel(app) {


    val longitude = MutableLiveData<Double?>()

    val reminderTitle = MutableLiveData<String?>()


    val reminderSelectedLocationStr = MutableLiveData<String?>()

    val selectedPOI = MutableLiveData<PointOfInterest?>()

    val reminderDescription = MutableLiveData<String?>()

    val latitude = MutableLiveData<Double?>()






    fun validateAndSaveReminder(reminderData: ReminderDataItem) {
        if (validateEnteredData(reminderData)) {
            saveReminder(reminderData)
        }
    }

    fun saveReminder(reminderData: ReminderDataItem) {
        showLoading.value = true

        viewModelScope.launch {

            dataSource.saveReminder(

                ReminderDTO(

                    reminderData.title,

                    reminderData.description,

                    reminderData.location,

                    reminderData.latitude,

                    reminderData.longitude,

                    reminderData.id
                )
            )
            showLoading.value = false

            showToast.value = app.getString(R.string.reminder_saved)

            navCommand.value = NavigationCommand.Back
        }
    }
    fun onClear() {
        selectedPOI.value = null

        reminderTitle.value = null

        reminderDescription.value = null

        longitude.value = null
        reminderSelectedLocationStr.value = null



        latitude.value = null


    }

    fun validateEnteredData(reminderData: ReminderDataItem): Boolean {


        if (reminderData.location.isNullOrEmpty()) {
            snackBarInt.value = R.string.err_select_location
            return false
        }
        if (reminderData.title.isNullOrEmpty()) {
            snackBarInt.value = R.string.err_enter_title
            return false
        }
        return true
    }
}
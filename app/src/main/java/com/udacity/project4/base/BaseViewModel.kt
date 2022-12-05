package com.udacity.project4.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.udacity.project4.utils.SingleLiveEvent

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {


    val snackBar: SingleLiveEvent<String> = SingleLiveEvent()

    val showToast: SingleLiveEvent<String> = SingleLiveEvent()

    val errorMessage: SingleLiveEvent<String> = SingleLiveEvent()

    val snackBarInt: SingleLiveEvent<Int> = SingleLiveEvent()

    val showLoading: SingleLiveEvent<Boolean> = SingleLiveEvent()

    val navCommand: SingleLiveEvent<NavigationCommand> = SingleLiveEvent()

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

}
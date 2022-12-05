package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
//fix robolectricVersion with newer version,note we can ignore it but viewmodel use context to use in fragment
@Config(maxSdk =28)
@ExperimentalCoroutinesApi
class SaveReminderViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var saveRepository: FakeDataSource
    // Subject under test
    private lateinit var saveListViewModel: SaveReminderViewModel

    //TODO: provide testing to the SaveReminderView and its live data objects

    @Before//fix global intalize becaise each test need has new instance
    fun setupViewModel() {
        stopKoin()
        saveRepository = FakeDataSource()
        saveListViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),saveRepository)
    }
    @After
    fun delRep(){
        runBlocking { saveRepository.deleteAllReminders()}
    }

    @Test
    fun addTask_ReturnPass() = runTest{

        val reminder =  ReminderDataItem(
            title = "star bucks",
            description = "Drink coffee",
            location = "san Stefano",
            latitude = 25.33243,
            longitude = 195.03211)
        saveListViewModel.saveReminder(reminder)
        assertThat(saveListViewModel.showToast.getOrAwaitValue(), `is`("Reminder Saved !"))
    }

    @Test
    fun noLocation_retrunError() = runTest{

        val reminder =  ReminderDataItem(
            title = "star bucks",
            description = "Drink coffee",
            location = "",
            latitude = 25.33243,
            longitude = 195.03211)
        saveListViewModel.validateAndSaveReminder(reminder)
        assertThat(saveListViewModel.snackBarInt.getOrAwaitValue(), `is`(R.string.err_select_location))
    }

    @Test
    fun addNullTitle_retrunError() = runTest {
        val reminder =  ReminderDataItem(
            title = "",
            description = "Drink coffee",
            location = "san Stefano",
            latitude = 25.33243,
            longitude = 195.03211)
        saveListViewModel.validateAndSaveReminder(reminder)
        assertThat(saveListViewModel.snackBarInt.getOrAwaitValue(), `is`(R.string.err_enter_title))

    }

    @Test
    fun addTask_showLoadingBar()= runBlocking{
        val reminder =  ReminderDataItem(
            title = "star bucks",
            description = "Drink coffee",
            location = "san Stefano",
            latitude = 25.33243,
            longitude = 195.03211)
        mainCoroutineRule.pauseDispatcher()
        saveListViewModel.validateAndSaveReminder(reminder)
        assertThat(saveListViewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        assertThat(saveListViewModel.showLoading.getOrAwaitValue(), `is`(false))

    }

}
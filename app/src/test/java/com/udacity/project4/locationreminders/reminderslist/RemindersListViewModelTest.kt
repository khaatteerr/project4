package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
//fix robolectricVersion with newer version,note we can ignore it but viewmodel use context to use in fragment
@Config(maxSdk =28)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    //TODO: provide testing to the RemindersListViewModel and its live data objects
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var remindersRepository: FakeDataSource
    // Subject under test
    private lateinit var remindersListViewModel: RemindersListViewModel



    @Before//fix global intalize becaise each test need has new instance
    fun setupViewModel() {
        remindersRepository = FakeDataSource()
        remindersListViewModel = RemindersListViewModel( getApplicationContext(),remindersRepository)
    }
    @After
    fun delRep(){
        runBlocking { remindersRepository.deleteAllReminders()}
    }

    @Test
    fun returnError() = mainCoroutineRule.runTest {

        remindersRepository.setError(false)
        remindersListViewModel.loadReminders()
        advanceUntilIdle()
        var value=remindersListViewModel.snackBar.getOrAwaitValue()
        assertThat(value, `is`("ERROR IN GET DATA"))

    }


    @Test
    fun addTask_ShowLoadingBar() =  mainCoroutineRule.runBlockingTest {
        remindersRepository.deleteAllReminders()
        val reminder = ReminderDTO(
            title = "star bucks",
            description = "Drink coffee",
            location = "san Stefano",
            latitude = 25.33243,
            longitude = 195.03211)
        remindersRepository.saveReminder(reminder)

        mainCoroutineRule.pauseDispatcher()
        remindersListViewModel.loadReminders()

        assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()

        assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), `is`(false))
        assertThat(remindersListViewModel.showNoData.getOrAwaitValue(), `is`(true))


    }


}
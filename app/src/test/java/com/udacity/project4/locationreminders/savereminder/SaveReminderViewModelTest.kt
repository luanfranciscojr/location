package com.udacity.project4.locationreminders.savereminder

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import getOrAwaitValue

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {


    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var app: Application
    private lateinit var viewModel: SaveReminderViewModel;
    private lateinit var fakeDataSource: FakeDataSource;


    @Before
    fun setupViewModel() {
        stopKoin()
        app = ApplicationProvider.getApplicationContext()
        FirebaseApp.initializeApp(app)
        fakeDataSource = FakeDataSource()
        viewModel = SaveReminderViewModel(app, fakeDataSource)
    }


    @Test
    fun saveReminder_showToastReminderSaved()  = mainCoroutineRule.runBlockingTest {
        // Give a reminder
        val reminder = ReminderDataItem("Teste Title", "Teste Description", "Teste localtion", 1.0, 1.0);

        // When save new reminder
        viewModel.saveReminder(reminderData = reminder);

        // Then the new reminder was created
        MatcherAssert.assertThat(
            viewModel.showToast.getOrAwaitValue(),
            CoreMatchers.`is`(app.getString(R.string.reminder_saved))
        )
    }

    @Test
    fun saveReminder_showLoading()  = mainCoroutineRule.runBlockingTest {
        // Give a reminder
        val reminder = ReminderDataItem("Teste Title", "Teste Description", "Teste localtion", 1.0, 1.0);

        // Pause dispatcher
        mainCoroutineRule.pauseDispatcher()

        // When save new reminder
        viewModel.saveReminder(reminderData = reminder);

        // Then - show loading
        MatcherAssert.assertThat(
            viewModel.showLoading.getOrAwaitValue(),
            (Is.`is`(true))
        )

        // Execute pending coroutines
        mainCoroutineRule.resumeDispatcher()

        // Then - hide loading
        MatcherAssert.assertThat(
            viewModel.showLoading.getOrAwaitValue(),
            (Is.`is`(false))
        )

    }



}
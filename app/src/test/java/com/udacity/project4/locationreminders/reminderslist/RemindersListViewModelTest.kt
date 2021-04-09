package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RemindersListViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var remindersListViewModel: RemindersListViewModel;
    private lateinit var fakeDataSource: FakeDataSource;


    @Before
    fun setupViewModel() {
        stopKoin()
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext())
        fakeDataSource = FakeDataSource()
        remindersListViewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), fakeDataSource)
    }


    @Test
    fun loadReminders_withData() = mainCoroutineRule.runBlockingTest {
        // Given a fresh ViewModel
        val reminder = ReminderDTO("Teste Title", "Teste Description", "Teste localtion", 1.0, 1.0);
        fakeDataSource.addReminders(reminder);

        // When adding a new task
        remindersListViewModel.loadReminders();

        // Then the new task event is triggered
        val reminderList = remindersListViewModel.remindersList.getOrAwaitValue()
        assertThat(reminderList.size, (`is`(1)))
        assertThat(reminderList.first().title, (`is`("Teste Title")))
        assertThat(reminderList.first().description, (`is`("Teste Description")))
        assertThat(reminderList.first().location, (`is`("Teste localtion")))
        assertThat(reminderList.first().latitude, (`is`(1.0)))
        assertThat(reminderList.first().longitude, (`is`(1.0)))
    }


    @Test
    fun loadReminders_showNoData_ReminderListIsEmpty() = mainCoroutineRule.runBlockingTest {

        // When loading reminders
        remindersListViewModel.loadReminders()

        // Then reminders list is empty and no data is shown
        val reminderList = remindersListViewModel.remindersList.getOrAwaitValue()
        val noData = remindersListViewModel.showNoData.getOrAwaitValue()
        assertThat(reminderList.size, (`is`(0)))
        assertThat(noData, (`is`(true)))

    }

    @Test
    fun loadReminders_Loading() = mainCoroutineRule.runBlockingTest {

        // Pause dispatcher
        mainCoroutineRule.pauseDispatcher()

        // When loading reminders
        remindersListViewModel.loadReminders()

        // Then - show loading
        assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), (`is`(true)))

        // Execute pending coroutines
        mainCoroutineRule.resumeDispatcher()

        // Then - hide loading
        assertThat(remindersListViewModel.showLoading.getOrAwaitValue(), (`is`(false)))

    }

}
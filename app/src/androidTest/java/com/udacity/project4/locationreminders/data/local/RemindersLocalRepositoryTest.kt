package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
        repository = RemindersLocalRepository(database.reminderDao(), Dispatchers.Main)
    }

    @After
    fun closeDB() = database.close()

    @Test
    fun getReminders() = runBlocking {

        // Give a reminder
        val reminderDTO = ReminderDTO(
            "Test Title",
            "",
            "Test location",
            1.0,
            1.0        )
        repository.saveReminder(reminderDTO)

        // When get reminders
        val reminders = (repository.getReminders() as Result.Success)

        // Then reminder first element
        val reminder = reminders.data.first()
        assertThat(reminder.id, (`is`(reminderDTO.id)))
        assertThat(reminder.title, (`is`(reminderDTO.title)))
        assertThat(reminder.description, (`is`(reminderDTO.description)))
        assertThat(reminder.location, (`is`(reminderDTO.location)))
        assertThat(reminder.latitude, (`is`(reminderDTO.latitude)))
        assertThat(reminder.longitude, (`is`(reminderDTO.longitude)))
    }

}
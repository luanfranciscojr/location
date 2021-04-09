package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase
    private lateinit var dao: RemindersDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                RemindersDatabase::class.java
        ).build()
        dao = database.reminderDao()
    }

    @After
    fun closeDB() = database.close()

    @Test
    fun getReminders() = runBlockingTest {

        // Give a reminder
        val reminderDTO = ReminderDTO(
                "Test Title",
                "",
                "Teste Description",
                1.0,
                1.0
        )
        dao.saveReminder(reminderDTO)

        // When get reminders
        val reminders = dao.getReminders()

        // Then reminder first element
        val reminder = reminders.first()
        assertThat(reminder.id, (`is`(reminderDTO.id)))
        assertThat(reminder.title, (`is`(reminderDTO.title)))
        assertThat(reminder.description, (`is`(reminderDTO.description)))
        assertThat(reminder.location, (`is`(reminderDTO.location)))
        assertThat(reminder.latitude, (`is`(reminderDTO.latitude)))
        assertThat(reminder.longitude, (`is`(reminderDTO.longitude)))
    }

}
package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO> = mutableListOf()) : ReminderDataSource {

    private var shouldReturnError = false

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
            return Result.Error("Exception getReminder")
        }
        return Result.Success(reminders)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        reminders?.let { return Result.Success(it[0]) }
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }

    fun setReturnError(error: Boolean) {
        shouldReturnError = error
    }


    fun addReminders(vararg remindersAux: ReminderDTO) {
        for (reminder in remindersAux) {
            reminders.add(reminder);
        }
    }

}
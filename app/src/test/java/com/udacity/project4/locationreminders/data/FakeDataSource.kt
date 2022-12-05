package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var tasks: MutableList<ReminderDTO>? = mutableListOf()) : ReminderDataSource {

    //    TODO: Create a fake data source to act as a double to the real data source
    private var error = false


    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return if (!error) {

             Result.Error("ERROR IN GET DATA",404)

        } else {
            tasks.let {
                 Result.Success(ArrayList(it))
            }

        }
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        tasks?.add(reminder)

    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        val data = tasks!!.find {
            it.id == id
        } ?: return Result.Error("NO TASK FOUND", 404)
        return Result.Success(data)
    }


    fun setError(error: Boolean) {
        this.error = error
    }

    override suspend fun deleteAllReminders() {
        tasks?.clear()
    }


}
package com.sargis.khlopuzyan.overplay.repository

import com.sargis.khlopuzyan.overplay.constant.Constants
import com.sargis.khlopuzyan.overplay.data.local.DataStoreUtil
import com.sargis.khlopuzyan.overplay.util.SensorEventUtil
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * Created by Sargis Khlopuzyan on 3/29/2022.
 */

interface OverplayRepository {
    suspend fun getLastSavedDateTime(): String?
    suspend fun saveCurrentDateTime()
    fun getCurrentDateTime(): String
    suspend fun getSessionCount(): Int?
    suspend fun saveSessionCount(count: Int)
    fun calculateTotalMinutesBetweenTwoDates(startDateTime: String, endDateTime: String): Long
    fun calculateDeviceZAxisRotationDegree(sensorEventValues: FloatArray): Float
}

class OverplayRepositoryImpl(
    private val dataStoreUtil: DataStoreUtil,
    private val sensorEventUtil: SensorEventUtil,
    private val coroutineDispatcher: CoroutineDispatcher
) : OverplayRepository {

    override suspend fun getLastSavedDateTime(): String? =
        withContext(coroutineDispatcher) {
            dataStoreUtil.readStringFromDataStore(Constants.DataStore.DATE_TIME)
        }

    override suspend fun saveCurrentDateTime() = withContext(coroutineDispatcher) {
        val sdf = SimpleDateFormat(Constants.App.DATE_TIME_FORMATTER, Locale.getDefault())
        val currentFormattedDate = sdf.format(Date())

        dataStoreUtil.saveStringInDataStore(
            Constants.DataStore.DATE_TIME,
            currentFormattedDate
        )
    }

    override fun getCurrentDateTime(): String {
        val sdf = SimpleDateFormat(Constants.App.DATE_TIME_FORMATTER, Locale.getDefault())
        return sdf.format(Date())
    }

    override suspend fun getSessionCount(): Int? = withContext(coroutineDispatcher) {
        return@withContext dataStoreUtil.readIntFromDataStore(Constants.DataStore.SESSION_COUNT)
    }

    override suspend fun saveSessionCount(count: Int) =
        withContext(coroutineDispatcher) {
            return@withContext dataStoreUtil.saveIntInDataStore(
                Constants.DataStore.SESSION_COUNT,
                count
            )
        }

    override fun calculateTotalMinutesBetweenTwoDates(
        startDateTime: String,
        endDateTime: String
    ): Long {
        val sdf = SimpleDateFormat(Constants.App.DATE_TIME_FORMATTER, Locale.getDefault())

        val startDate = sdf.parse(startDateTime)
        val endDate = sdf.parse(endDateTime)
        val diffInMillies = abs(endDate!!.time - startDate!!.time)
        return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    override fun calculateDeviceZAxisRotationDegree(sensorEventValues: FloatArray): Float {
        return sensorEventUtil.calculateDeviceZAxisRotationDegree(sensorEventValues)
    }
}
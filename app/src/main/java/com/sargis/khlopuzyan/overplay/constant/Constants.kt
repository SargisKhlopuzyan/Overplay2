package com.sargis.khlopuzyan.overplay.constant

/**
 * Created by Sargis Khlopuzyan on 3/29/2022.
 */
interface Constants {

    interface DataStore {
        companion object {
            const val OVERPLAY_PREFERENCES = "OVERPLAY_PREFERENCES"
            const val DATE_TIME = "DATE_TIME"
            const val SESSION_COUNT = "SESSION_COUNT"
        }
    }

    interface App {
        companion object {
            const val NEW_SESSION_DURATION = 10 // in minutes
            const val ROTATION_LEFT_ANGLE = 30 // degree
            const val ROTATION_RIGHT_ANGLE = -30 // degree
            const val DATE_TIME_FORMATTER = "dd/M/yyyy hh:mm:ss"

            // font sizes
            const val ROTATION_LEFT_FONT_SIZE = 12f
            const val ROTATION_RIGHT_FONT_SIZE = 20f
            const val ROTATION_DEFAULT_FONT_SIZE = 16f
        }
    }
}
package ru.olegsvs.custombatterynotification

/*
 *  Copyright (c) 2010-2011 Ran Manor
 *
 *  This file is part of CurrentWidget.
 *
 * 	CurrentWidget is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  CurrentWidget is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with CurrentWidget.  If not, see <http://www.gnu.org/licenses/>.
*/


import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

import android.util.Log

import com.crashlytics.android.Crashlytics

object OneLineReader {

    fun getValue(_f: File): String {

        var readedValue: String? = null

        try {
            val fs = FileInputStream(_f)
            val sr = InputStreamReader(fs)
            val br = BufferedReader(sr)

            readedValue = br.readLine()

            br.close()
            sr.close()
            fs.close()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Crashlytics.logException(ex)
        }

        return readedValue
    }

}
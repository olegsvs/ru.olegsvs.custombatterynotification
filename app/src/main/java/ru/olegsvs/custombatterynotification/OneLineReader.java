package ru.olegsvs.custombatterynotification;

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


        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.InputStreamReader;

        import android.util.Log;

public class OneLineReader {

    public static String getValue(File _f) {

        String readedValue = null;

        try {
            FileInputStream fs = new FileInputStream(_f);
            InputStreamReader sr = new InputStreamReader(fs);
            BufferedReader br = new BufferedReader(sr);

            readedValue = br.readLine();

            br.close();
            sr.close();
            fs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return readedValue;
    }

}
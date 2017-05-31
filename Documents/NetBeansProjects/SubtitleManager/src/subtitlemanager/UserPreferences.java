/*
 * Copyright (C) 2017 atulgpt <atlgpt@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package subtitlemanager;

import java.util.prefs.Preferences;

/**
 *
 * @author atulgpt <atlgpt@gmail.com>
 */
public class UserPreferences {

    static Preferences mPreferences = Preferences.userNodeForPackage(UserPreferences.class);
    private static String mDefaultFileLoc;
    private static boolean mEmbedLangInfo;
    private static int mLookAndFeel;

    private static final String DEFAULT_LOC = "defaultLoccation";
    private static final String DEFAUL_LANF_INFO = "languageInfoShouldBeEmbeddedOrNot";
    private static final String LOOK_AND_FEEL = "lookAndFeelOfTheApp";
    public static final int JAVA_LOOK_AND_FEEL = 0;
    public static final int SYS_LOOK_AND_FEEL = 1;

    public static int getLookAndFeel() {
        return mPreferences.getInt(LOOK_AND_FEEL, UserPreferences.SYS_LOOK_AND_FEEL);
    }

    public static void setLookAndFeel(int lookAndFeel) {
        UserPreferences.mLookAndFeel = lookAndFeel;
        mPreferences.putInt(LOOK_AND_FEEL, lookAndFeel);
    }

    public static String getDefaultFileLoc() {
        mDefaultFileLoc = mPreferences.get(DEFAULT_LOC, System.getProperty("user.home"));
        return mDefaultFileLoc;
    }

    public static void setDefaultFileLoc(String mDefaultFileLoc) {
        UserPreferences.mDefaultFileLoc = mDefaultFileLoc;
        mPreferences.put(DEFAULT_LOC, mDefaultFileLoc);
    }

    public static boolean isEmbedLangInfo() {
        mEmbedLangInfo = mPreferences.getBoolean(DEFAUL_LANF_INFO, false);
        return mEmbedLangInfo;
    }

    public static void setEmbedLangInfo(boolean embedLangInfo) {
        mEmbedLangInfo = embedLangInfo;
        mPreferences.putBoolean(DEFAUL_LANF_INFO, embedLangInfo);
    }
}

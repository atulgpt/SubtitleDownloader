/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

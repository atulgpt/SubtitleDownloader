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
    Preferences mPreferences = Preferences.userNodeForPackage(UserPreferences.class);
    private String mDefaultFileLoc;
    private boolean mEmbedLangInfo;
    private final String DEFAULT_LOC = "defaultLoccation";
    private final String DEFAUL_LANF_INFO = "languageInfoShouldBeEmbeddedOrNot";
    
    public String getDefaultFileLoc() {
        mDefaultFileLoc = mPreferences.get(DEFAULT_LOC, System.getProperty("user.home"));
        return mDefaultFileLoc;
    }

    public void setDefaultFileLoc(String mDefaultFileLoc) {
        this.mDefaultFileLoc = mDefaultFileLoc;
        mPreferences.put(DEFAULT_LOC, mDefaultFileLoc);
    }

    public boolean isEmbedLangInfo() {
        mEmbedLangInfo = mPreferences.getBoolean(DEFAUL_LANF_INFO, false);
        return mEmbedLangInfo;
    }

    public void setEmbedLangInfo(boolean embedLangInfo) {
        mEmbedLangInfo = embedLangInfo;
        mPreferences.putBoolean(DEFAUL_LANF_INFO, embedLangInfo);
    }
}

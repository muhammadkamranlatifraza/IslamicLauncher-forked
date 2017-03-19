package fr.usg.islamiclauncher.appSettings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import fr.usg.islamiclauncher.constants.TAGS;


public class AppSettings implements TAGS {

    private static SharedPreferences prefs;
    private static Editor editor;
    private Context context;

    @SuppressLint("CommitPrefEdits")
    public AppSettings(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences("TAG_PREFERENCES", 0);
        editor = prefs.edit();
    }

    public String getString(String key) {
        return prefs.getString(key, "");
    }

    public int getInt(String key) {
        return prefs.getInt(key, -1);
    }

    public int getFontSizeArabic() {
        int size = prefs.getInt(FONT_SIZE_ARABIC, 16);
        switch (size) {
            case 0:
                return 16;
            case 1:
                return 18;
            case 2:
                return 22;
            case 3:
                return 32;
            case 4:
                return 38;
            default:
                return 18;
        }
    }

    public boolean[] getAyahFormat() {
        boolean[] format = new boolean[3];
        int size = prefs.getInt(AYAH_FORMAT, 16);
        switch (size) {
            case 0:
                format[0] = true;
                format[1] = false;
                format[2] = false;
                return format;
            case 1:
                format[0] = true;
                format[1] = false;
                format[2] = true;
                return format;
            case 2:
                format[0] = true;
                format[1] = true;
                format[2] = false;
                return format;
            case 3:
                format[0] = true;
                format[1] = true;
                format[2] = true;
                return format;
            default:
                format[0] = true;
                format[1] = false;
                format[2] = true;
                return format;
        }
    }


    public int getFontSizeTranslitration() {
        int size = prefs.getInt(FONT_SIZE_TRANSLITRATION, 16);
        switch (size) {
            case 0:
                return 16;
            case 1:
                return 18;
            case 2:
                return 22;
            case 3:
                return 32;
            case 4:
                return 38;
            default:
                return 12;
        }
    }

    public int getFontSizeTranslation() {
        int size = prefs.getInt(FONT_SIZE_TRANSLATION, 16);
        switch (size) {
            case 0:
                return 16;
            case 1:
                return 18;
            case 2:
                return 22;
            case 3:
                return 32;
            case 4:
                return 38;
            default:
                return 16;
        }
    }

    public void lockApp(String app, String PIN) {
        editor.putBoolean(app + "locked", true).putString(app, PIN).commit();
    }

    public void unlockApp(String app) {
        editor.remove(app).remove(app + "locked").commit();
    }

    public boolean isLocked(String app) {
        return prefs.getBoolean(app + "locked", false);
    }

    public String getAppPIN(String app) {
        return prefs.getString(app, "");
    }

    //    public void putSecurityQuestion(String question, String answer) {
//        editor.putString(QUESTION, question).putString(ANSWER, answer).commit();
//    }
//
//    public String getQuestion() {
//        return prefs.getString(QUESTION, "");
//    }
//
    public int getReadingSurah() {
        return prefs.getInt(READING_SURAH, 0);
    }

    public void putReadingSurah(int index) {
        putInt(READING_SURAH, index);
    }

    public void setFontType(int type) {
        putInt(FONT_TYPE, type);
    }

    public String getFontType() {
        int type = getInt(FONT_TYPE);
        switch (type) {
            case 1:
                return "Al_Qalam_Quran_2.ttf";
            case 2:
                return "me_quran_volt_newmet.ttf";
            case 3:
                return "PDMS_Saleem_QuranFont-signed.ttf";
            default:
                return "Al_Qalam_Quran_2.ttf";
        }
    }

    public boolean useCustomFont() {
        int type = getInt(FONT_TYPE);
        return type != 0 && type != -1;
    }

    public double getDouble(String key) {
        return prefs.getFloat(key, -1);
    }

    public String getAppVersion() {
        return prefs.getString(APP_VERSION, "1.0");
    }

    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public boolean getFontColor() {
        return getBoolean(FONT_COLOR);
    }

    public void setWhiteFontColor() {
        putBoolean(FONT_COLOR, true);
    }

    public void setDarkFontColor() {
        putBoolean(FONT_COLOR, false);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public void putDouble(String key, double value) {
        editor.putFloat(key, (float) value).commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value).commit();
    }

    public void putScrollPosition(int index, int top) {
        putInt("scrollIndex", index);
        putInt("scrollTop", top);
    }

    public int[] getScrollPosition() {
        int[] pos = new int[2];
        pos[0] = prefs.getInt("scrollIndex", 0);
        pos[1] = prefs.getInt("scrollTop", 0);
        return pos;
    }

    public void resetSettings() {
        editor.clear().commit();
    }

    public String[] getBottomBarArrayPackageNames() {
        String[] array = new String[4];
        array[0] = getString(APP1 + PACKAGE);
        array[1] = getString(APP2 + PACKAGE);
        array[2] = getString(APP3 + PACKAGE);
        array[3] = getString(APP4 + PACKAGE);
        return array;
    }

    public String[] getBottomBarArrayAppNames() {
        String[] array = new String[4];
        array[0] = getString(APP1 + NAME);
        array[1] = getString(APP2 + NAME);
        array[2] = getString(APP3 + NAME);
        array[3] = getString(APP4 + NAME);
        return array;
    }

    public void putBottomBarArrayPackageNames(String[] array) {
        putString(APP1 + PACKAGE, array[0]);
        putString(APP2 + PACKAGE, array[1]);
        putString(APP3 + PACKAGE, array[2]);
        putString(APP4 + PACKAGE, array[3]);
    }

    public void putBottomBarArrayAppNames(String[] array) {
        putString(APP1 + NAME, array[0]);
        putString(APP2 + NAME, array[1]);
        putString(APP3 + NAME, array[2]);
        putString(APP4 + NAME, array[3]);
    }

    public String[] getFavoriteAppsArray() {
        String[] array = new String[18];
        for (int i = 0; i < 18; i++) {
            array[i] = getString(FAVORITE + i);
        }
        return array;
    }

    public boolean putFavoriteApp(String app) {
        boolean done = false;
        for (int i = 0; i < 18; i++) {
            if (getString(FAVORITE + i).equals("")) {
                putString(FAVORITE + i, app);
                done = true;
                break;
            }
        }
        return done;
    }

    public boolean isFixHooked() {
        return getInt(CONTACTS_FIX) == 1;
    }


    public int calculationMethod() {
        return prefs.getInt(CALCULATION_METHOD, 1);
    }

    public int juristicMethod() {
        return prefs.getInt(JURISTIC_METHOD, 1);
    }

    public int timeFormat() {
        return prefs.getInt(TIME_FORMAT, 1);
    }


}

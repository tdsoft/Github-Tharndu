package com.android.tdsoft.androidappfoundation.base.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.annotation.StringDef;

import java.util.Locale;

/**
 * Created by Admin on 2/10/2016.
 */
public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    public static final String ENGLISH = "en";
    public static final String ESPANOL = "es";

    @StringDef({ENGLISH,ESPANOL})
    @interface LanguageType{}

    public static void onCreate(Context context) {
       @LanguageType String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        setLocale(context, lang);
    }

    public static void onCreate(Context context, @LanguageType String defaultLanguage) {
        @LanguageType String lang = getPersistedData(context, defaultLanguage);
        setLocale(context, lang);
    }

    @LanguageType
    public static String getLanguage(Context context) {
        @LanguageType String lang = ENGLISH;
        String tempType = getPersistedData(context, Locale.getDefault().getLanguage());
        if(tempType == null){
            lang = ENGLISH;
        }else{
            if(tempType.equalsIgnoreCase(ESPANOL)){
                lang = ESPANOL;
            }else{
                lang = ENGLISH;
            }
        }

        return lang;
    }

    public static void setLocale(Context context, @LanguageType String language) {
        persist(context, language);
        updateResources(context, language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(SELECTED_LANGUAGE, language);
        editor.apply();
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
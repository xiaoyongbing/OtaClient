package com.ido.otaclient.env;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by Lize on 2018/4/28.
 */
public class LanguageManager {

    public static final String AUTO = "auto";
    public static final String ZH = "zh";
    public static final String ZH_CN = "zh_CN";
    public static final String ZH_TW = "zh_TW";
    public static final String EN = "en";
    public static final String KO = "ko";
    public static final String DE = "de";
    public static final String FR = "fr";
    public static final String RU = "ru";
    public static final String ES = "es";
    public static final String JA = "ja";
    public static final String PT = "pt";
    public static final String pt_BR = "pt_BR";

    private static final String KEY_APP_LANGUAGE = "app_language";

    private static SharedPreferences getPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * 持久存储语言
     *
     * @param context  context
     * @param language language
     */
    private static void persistenceLanguage(Context context, String language) {
        getPref(context).edit().putString(KEY_APP_LANGUAGE, language).apply();
    }

    private static String toLanguage(Locale locale) {
        String language = locale.getLanguage();
        String country = locale.getCountry();
        // 默认值为英文
        switch (language) {
            case EN:
            case KO:
            case DE:
            case ES:
            case FR:
            case RU:
            case JA:
                break;
            case ZH: {
                switch (country) {
                    case "TW":
                    case "HK":
                    case "MO":
                        language = ZH_TW;
                        break;
                    default:
                        language = ZH_CN;
                        break;
                }
                break;
            }
            default: {
                switch (country) {
                    case "BR":
                        language = pt_BR;
                        break;
                    default:
                        language = EN;
                        break;
                }
                break;
            }
        }
        return language;
    }


    public static Context setLanguage(Context context) {
        return setLanguage(context, getLanguage(context));
    }

    public static Context setLanguage(Context context, String language) {
        persistenceLanguage(context, language);
        return updateResources(context, language);
    }

    public static String getLanguage(Context context) {
        return getPref(context).getString(KEY_APP_LANGUAGE, AUTO);
    }

    public static String getLang() {
        Context context = AppEnv.instance().getContext();
        Locale locale = getLocale(context.getResources());
        return toLanguage(locale).toLowerCase();
    }


    @SuppressLint("ObsoleteSdkInt")
    private static Context updateResources(Context context, String language) {
        Locale locale;
        try {
            if (AUTO.equals(language)) {
                locale = getLocale(context.getResources());
            } else if (language.contains("_")) {
                String[] localeStr = TextUtils.split(language, "_");
                locale = new Locale(localeStr[0], localeStr[1]);
            } else {
                locale = new Locale(language);
            }
            Locale.setDefault(locale);
            Resources res = context.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale);
                context = context.createConfigurationContext(config);
            } else {
                config.locale = locale;
                res.updateConfiguration(config, res.getDisplayMetrics());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppEnv.initialize(context);
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                config.getLocales().get(0) : config.locale;
    }
}

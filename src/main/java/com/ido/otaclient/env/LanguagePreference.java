package com.ido.otaclient.env;

import android.content.Context;

import com.ido.otaclient.base.BasePreference;


/**
 * Created by Lize on 2018/4/13.
 */
public class LanguagePreference extends BasePreference {

    private static final String NAME = "language_ops";
    private static final String KEY =  "language_id";

    public static void save(int id) {
        Context context = AppEnv.instance().getContext();
        saveInt(context, NAME, KEY, id);
    }

    public static int get(int defaultId) {
        Context context = AppEnv.instance().getContext();
        return getInt(context, NAME, KEY, defaultId);
    }
}

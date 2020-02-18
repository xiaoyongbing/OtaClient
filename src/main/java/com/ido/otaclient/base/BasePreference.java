package com.ido.otaclient.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyCharacterMap.UnavailableException;

import java.util.Map;
import java.util.Set;

/**
 * preference基础类
 */
public class BasePreference {


    /**
     * 保存数据：String
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveString(Context context, String preferenceName, String key, String value) {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.apply();
    }
    
    /**
     * 获取数据:String
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：null
     * 
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static String getString(Context context, String preferenceName, String key){
        return getString(context, preferenceName, key, null);
    }

    
    /**
     * 获取数据:String
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultStr
     */
    protected static String getString(Context context, String preferenceName, String key, String defaultStr){
        String result = null;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        result = preference.getString(key, defaultStr);

        return result;
    }
    

    /**
     * 保存数据：long
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveLong(Context context, String preferenceName, String key, long value){
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putLong(key, value);
        editor.apply();
    }
    
    /**
     * 获取数据:long
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：0
     * 
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static long getLong(Context context, String preferenceName, String key){
        return getLong(context, preferenceName, key, 0);
    }
    
    /**
     * 获取数据:long
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param defValue
     */
    protected static long getLong(Context context, String preferenceName, String key, long defValue){
        long result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        result = preference.getLong(key, defValue);
        
        return result;
    }

    /**
     * 保存数据：int
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveInt(Context context, String preferenceName, String key, int value){
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.apply();
    }
    
    /**
     * 获取数据:int
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：0
     * 
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static int getInt(Context context, String preferenceName, String key){
        return getInt(context, preferenceName, key, 0);
    }
    

    /**
     * 获取数据:int
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param defValue
     */
    protected static int getInt(Context context, String preferenceName, String key, int defValue){
        int result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        result = preference.getInt(key, defValue);
        
        return result;
    }
    
    /**
     * 保存数据：boolean
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveBoolean(Context context, String preferenceName, String key, boolean value){
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    
    /**
     * 获取数据:boolean
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：false
     * 
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static boolean getBoolean(Context context, String preferenceName, String key){
        return getBoolean(context, preferenceName, key, false);
    }
    
    /**
     * 获取数据:boolean
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @return
     */
    protected static boolean getBoolean(Context context, String preferenceName, String key , boolean defaultValue){
        boolean result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        result = preference.getBoolean(key, defaultValue);
        
        return result;
    }
    
    /**
     * 保存数据：float
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveFloat(Context context, String preferenceName, String key, float value){
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putFloat(key, value);
        editor.apply();
    }
    
    /**
     * 获取数据:float
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：0
     * 
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static float getFloat(Context context, String preferenceName, String key){
        return getFloat(context, preferenceName, key, 0);
    }
    
    /**
     * 获取数据:float
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @return
     */
    protected static float getFloat(Context context, String preferenceName, String key, float defaultValue){
        float result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        result = preference.getFloat(key, defaultValue);
        
        return result;
    }
    
    /**
     * 保存数据：StringSet
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param value
     */
    protected static void saveStringSet(Context context, String preferenceName, String key, Set<String> value){
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }
    
    /**
     * 获取数据:StringSet
     * 默认模式: Context.MODE_PRIVATE
     * 默认缺省值：0
     * 
     * @param context
     * @param preferenceName
     * @param key
     */
    protected static Set<String> getStringSet(Context context, String preferenceName, String key){
        return getStringSet(context, preferenceName, key, null);
    }
    
    /**
     * 获取数据:StringSet
     * 
     * @param context
     * @param preferenceName
     * @param key
     * @param defaultValue
     * @return
     */
    protected static Set<String> getStringSet(Context context, String preferenceName, String key, Set<String> defaultValue){
        Set<String> result;
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        result = preference.getStringSet(key, defaultValue);
        
        return result;
    }
    
    /**
     * 
     * @param context
     * @param preferenceName
     * @param keyValue
     */
    @SuppressWarnings("unchecked")
    protected static void  saveMulValue(Context context, String preferenceName, Map<String, Object> keyValue) throws UnavailableException {
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        
        for (String key : keyValue.keySet()) {
            Object value = keyValue.get(key);
            
            if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean)value);
            } else if (value instanceof String) {
                editor.putString(key, (String)value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer)value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long)value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float)value);
            } else if (value instanceof Set<?>) {
                editor.putStringSet(key, (Set<String>)value);
            }else {
                throw new UnavailableException("unvalid param:keyValue");
            }
        }
        editor.apply();
    }


    protected static void remove(Context context, String preferenceName, String key){
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.remove(key);
        editor.apply();
    }

    protected static void clear(Context context, String preferenceName){
        SharedPreferences preference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.clear();
        editor.apply();
    }


}

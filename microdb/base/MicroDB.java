package com.tunabaranurut.microdb.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.tunabaranurut.microdb.core.ReservedKeyException;
import com.tunabaranurut.microdb.util.JsonMapper;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by tunabaranurut on 3.06.2018.
 */

public class MicroDB {

    private static String TAG = MicroDB.class.getSimpleName();

    private WeakReference<Context> wmContext;

    private HashSet<String> reservedKeys;
    private KeySet keySet;

    private String KEY_DEPOSIT_REFERENCE = "referenceKeysDeposit0001";

    private String DB_NAME = "MicroDB";

    public MicroDB(Context context) {
        wmContext = new WeakReference<>(context);

        reservedKeys = new HashSet<>();
        reservedKeys.addAll(Arrays.asList(KEY_DEPOSIT_REFERENCE,DB_NAME));

        try {
            keySet = JsonMapper.getObjectFromJson(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE), KeySet.class);
            keySet = keySet == null ? new KeySet() : keySet;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void save(String key, Object object) throws Exception{
        controlKey(key);

        if(JsonMapper.isPrimitive(object)){
            SharedPreferences sharedPref = wmContext.get().getSharedPreferences("preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            switch (object.getClass().getSimpleName()){
                case "String":
                    editor.putString(key,(String)object);
                    break;
                case "Integer":
                case "Short":
                    short s = (Short)object;
                    editor.putInt(key,(int)s);
                    break;
                case "Float":
                case "Double":
                    double d = (Double)object;
                    float f = (float)d;
                    editor.putFloat(key,f);
                    break;
                case "Boolean":
                    editor.putBoolean(key,(Boolean)object);
                    break;
                case "Long":
                    editor.putLong(key,(Long)object);
            }
            editor.apply();
            return;
        }

        JSONObject json = JsonMapper.getJsonObject(object);
        saveToSharedPreferences(key,json.toString());
        addKey(key);
    }

    public <T> T load(String key, Class<? extends T> clss) throws Exception{
        controlKey(key);

        if(JsonMapper.isPrimitive(clss)){
            SharedPreferences sharedPref = wmContext.get().getSharedPreferences("preferences", Context.MODE_PRIVATE);
            Object resp = null;
            switch (clss.getSimpleName()){
                case "String":
                    resp = sharedPref.getString(key,"");
                    return (T)resp;
                case "Integer":
                case "Short":
                    resp = sharedPref.getInt(key,0);
                    return (T)resp;
                case "Float":
                case "Double":
                    resp = sharedPref.getFloat(key,0f);
                    return (T)resp;
                case "Boolean":
                    resp = sharedPref.getBoolean(key,false);
                    return (T)resp;
                case "Long":
                    resp = sharedPref.getLong(key,0L);
                    return (T)resp;
            }
        }

        return JsonMapper.getObjectFromJson(loadFromSharedPreferences(key),clss);
    }

    public void delete(String key) throws Exception{
        controlKey(key);

        SharedPreferences sharedPref = wmContext.get().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
        removeKey(key);
    }

    public List<String> keySet(){
        try {
            return JsonMapper.getObjectFromJson(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE),KeySet.class).getKeys();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void clear(){
        SharedPreferences sharedPref = wmContext.get().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
    }

    private void addKey(String key){
        try {
            if(!keySet.getKeys().contains(key)) {
                KeySet keys = JsonMapper.getObjectFromJson(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE), KeySet.class);
                keys = keys == null ? new KeySet() : keys;
                keys.getKeys().add(key);
                keySet.getKeys().add(key);
                JSONObject json = JsonMapper.getJsonObject(keys);
                saveToSharedPreferences(KEY_DEPOSIT_REFERENCE, json.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeKey(String key){
        try {
            KeySet keys = JsonMapper.getObjectFromJson(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE),KeySet.class);
            keys.getKeys().remove(key);
            keySet.getKeys().remove(key);
            save(KEY_DEPOSIT_REFERENCE, keys);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void controlKey(String key) throws ReservedKeyException {
        if(reservedKeys.contains(key)){
            throw new ReservedKeyException(key);
        }
    }

    private void saveToSharedPreferences(String key, String jsonToSave){
        SharedPreferences sharedPref = wmContext.get().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, jsonToSave);
        editor.apply();
    }

    private String loadFromSharedPreferences(String key){
        SharedPreferences sharedPref = wmContext.get().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String sharedPrefString = sharedPref.getString(key, "");
        return sharedPrefString;
    }

}
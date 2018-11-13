package com.tunabaranurut.microdb.base;

import android.content.Context;
import android.content.SharedPreferences;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tunabaranurut.microdb.core.ReservedKeyException;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by tunabaranurut on 3.06.2018.
 */

public class MicroDB {

    private static String TAG = MicroDB.class.getSimpleName();

    private WeakReference<Context> wmContext;

    private HashSet<String> reservedKeys;
    private HashSet<String> keySet;

    private String KEY_DEPOSIT_REFERENCE = "referenceKeysDeposit0001";

    private String DB_NAME = "MicroDB";

    private ObjectMapper objectMapper;

    public MicroDB(Context context) {
        wmContext = new WeakReference<>(context);

        objectMapper = new ObjectMapper();

        reservedKeys = new HashSet<>();
        reservedKeys.addAll(Arrays.asList(KEY_DEPOSIT_REFERENCE,DB_NAME));

        try {
            String json = loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE);
            HashSet<String> kSet = null;
            if(!json.isEmpty()) {
                kSet = objectMapper.readValue(json, HashSet.class);
            }
            keySet = kSet == null ? new HashSet<String>() : kSet;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void save(String key, Object object) throws Exception{
        controlKey(key);

        String json = objectMapper.writeValueAsString(object);
        saveToSharedPreferences(key,json);

        addKey(key);
    }

    public <T> T load(String key, Class<? extends T> clss) throws Exception{
        String json = loadFromSharedPreferences(key);
        if(json.isEmpty()){
            return null;
        }
        return objectMapper.readValue(json,clss);
    }

    public void delete(String key) throws Exception{
        controlKey(key);

        SharedPreferences sharedPref = wmContext.get().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.apply();
        removeKey(key);
    }

    public HashSet<String> keySet(){
        try {
            return objectMapper.readValue(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE), KeySet.class).getKeys();
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
            if(!keySet.contains(key)) {
//                KeySet keys = objectMapper.readValue(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE), KeySet.class);
//                keys = keys == null ? new KeySet() : keys;
//                keys.getKeys().add(key);
                keySet.add(key);
                String json = objectMapper.writeValueAsString(keySet);
                saveToSharedPreferences(KEY_DEPOSIT_REFERENCE, json);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeKey(String key){
        try {
//            KeySet keys = objectMapper.readValue(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE),KeySet.class);
//            keys = keys == null ? new KeySet() : keys;
//            keys.getKeys().remove(key);
            keySet.remove(key);
            String json = objectMapper.writeValueAsString(keySet);
            saveToSharedPreferences(KEY_DEPOSIT_REFERENCE, json);
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


    public static void main(String[] args){

    }
}
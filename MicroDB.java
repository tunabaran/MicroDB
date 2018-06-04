import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tunabaranurut on 3.06.2018.
 */

public class MicroDB {

    private static String TAG = MicroDB.class.getSimpleName();

    private WeakReference<Context> wmContext;

    private HashSet<String> reservedKeys;

    private String KEY_DEPOSIT_REFERENCE = "referenceKeysDeposit0001";

    private KeySet keySet;

    public MicroDB(Context context) {
        wmContext = new WeakReference<Context>(context);

        reservedKeys = new HashSet<>();
        reservedKeys.addAll(Arrays.asList(KEY_DEPOSIT_REFERENCE));

        try {
            keySet = JsonMapper.getObjectFromJson(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE), KeySet.class);
            keySet = keySet == null ? new KeySet() : keySet;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void save(String key, Object object) throws Exception{
        controlKey(key);

        JSONObject json = JsonMapper.getJsonObject(object);
        saveToSharedPreferences(key,json.toString());
        addKey(key);
    }

    public <T> T load(String key, Class<? extends T> clss) throws Exception{
        controlKey(key);

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

    private void addKey(String key){
        try {
            if(!keySet.getKeys().contains(key)) {
                KeySet keys = JsonMapper.getObjectFromJson(loadFromSharedPreferences(KEY_DEPOSIT_REFERENCE), KeySet.class);
                keys = keys == null ? new KeySet() : keys;
//            keys.setKeys(new LinkedList<String>());
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

    private void controlKey(String key) throws ReservedKeyException{
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

    public class ReservedKeyException extends Exception{

        public ReservedKeyException(String key) {
            super(">" + key + "<" + "is reserved for internal uses.");
        }
    }

    static class KeySet {
        public List<String> keys = new LinkedList<>();

        public List<String> getKeys() {
            return keys;
        }

        public void setKeys(List<String> keys) {
            this.keys = keys;
        }
    }

}
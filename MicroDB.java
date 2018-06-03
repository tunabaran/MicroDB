import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by tunabaranurut on 3.06.2018.
 */

public class MicroDB {

    private static String TAG = MicroDB.class.getSimpleName();

    private WeakReference<Context> wmContext;

    private HashSet<String> primitives;

    public MicroDB(Context context) {
        wmContext = new WeakReference<Context>(context);

        primitives = new HashSet<>();
        primitives.addAll(Arrays.asList("int","Integer","String","boolean","Boolean","double","Double","long","Long","short","Short","float","Float"));
    }

    public void save(String key, Object object) throws Exception{
        JSONObject json = JsonMapper.getJsonObject(object);
        saveToSharedPreferences(key,json.toString());
    }

    public <T> T load(String key, Class<? extends T> clss) throws Exception{
        return JsonMapper.getObjectFromJson(loadFromSharedPreferences(key),clss);
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
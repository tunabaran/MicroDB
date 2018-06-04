import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by tunabaranurut on 3.06.2018.
 */

public abstract class JsonMapper {

    private static String TAG = JsonMapper.class.getSimpleName();

    private static HashSet<String> primitives = new HashSet<>(Arrays.asList("int","Integer","String","boolean","Boolean","double","Double","long","Long","short","Short","float","Float"));
    private static HashSet<String> unSupportedTypes = new HashSet<>(Arrays.asList("char","Character"));

    public static JSONObject getJsonObject(Object object) throws Exception{

        LinkedList<Field> fields = new LinkedList<>(Arrays.asList(object.getClass().getFields()));

        JSONObject fieldJson = new JSONObject();

        for(Field f : fields) {

            String name = f.getName();
            String type = f.getType().getSimpleName();

            controlUnSupportedTypes(type);

            if(f.getType().getName().equals("com.android.tools.ir.runtime.IncrementalChange") ||
                    f.getName().equals("serialVersionUID")){
                continue;
            }

            Class typeClass;
            if(primitives.contains(type)){
                fieldJson.put(name,f.get(object));
            }else if(List.class.isAssignableFrom(f.getType())){
                typeClass = List.class;
                List l = (List)typeClass.cast(f.get(object));
                if(l == null){
                    continue;
                }
                JSONArray jsonArray = new JSONArray();
                for(Object o : l) {
                    if(primitives.contains(o.getClass().getSimpleName())){
                        jsonArray.put(o);
                    }else{
                        jsonArray.put(getJsonObject(o));
                    }
                }
                fieldJson.put(name,jsonArray);
            }else{
                fieldJson.put(name,getJsonObject(f.get(object)));
            }
        }
        return fieldJson;
    }

    public static <T> T getObjectFromJson(String json, Class<? extends T> objectClass) throws Exception{
        if(json.length() == 0){
            return null;
        }
        T objectInstance = objectClass.newInstance();

        JSONObject jsonObject = new JSONObject(json);
        LinkedList<String> nodes = new LinkedList<>();
        for (Iterator<String> i = jsonObject.keys(); i.hasNext();) {
            String item = i.next();
            nodes.add(item);
        }
        for(String node : nodes) {
            Object jsonElement = jsonObject.get(node);
            if (primitives.contains(jsonElement.getClass().getSimpleName())) {
                objectInstance.getClass().getField(node).set(objectInstance, jsonElement);
            }else if(jsonElement.getClass().getSimpleName().equals("JSONArray")){
                JSONArray jsonArray = ((JSONArray) jsonElement);

                if(jsonArray.length() > 0){
                    Class itemClass = jsonArray.get(0).getClass();
                    List list = new LinkedList<>();
                    if(primitives.contains(itemClass.getSimpleName())){
                        for(int i = 0; i < jsonArray.length(); i++){
                            list.add(jsonArray.get(i));
                        }
                    }else{
                        for(int i = 0; i < jsonArray.length(); i++){
                            ParameterizedType stringListType = (ParameterizedType) objectInstance.getClass().getField(node).getGenericType();
                            Class<?> stringListClass = (Class<?>) stringListType.getActualTypeArguments()[0];

                            list.add(getObjectFromJson(jsonArray.getJSONObject(i).toString(),stringListClass));
                        }
                    }
                    objectInstance.getClass().getField(node).set(objectInstance, list);
                }

            }else{
                Object object = getObjectFromJson(jsonObject.getJSONObject(node).toString(),objectClass.getField(node).getType());
                objectInstance.getClass().getField(node).set(objectInstance, object);
            }
        }

        return objectInstance;
    }

    private static void controlUnSupportedTypes(String type) throws UnSupportedTypeException{
        if(unSupportedTypes.contains(type)) {
            throw new UnSupportedTypeException(type);
        }
    }

    public static class UnSupportedTypeException extends Exception{

        public UnSupportedTypeException(String type) {
            super(type + " is not supported by DB.");
        }
    }

}

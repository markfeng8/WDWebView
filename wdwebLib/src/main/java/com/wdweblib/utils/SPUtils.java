package com.wdweblib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-04 16:40
 */
public class SPUtils {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_data";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context application context.
     * @param key     String key
     * @param object  value
     */
    public static void put(Context context, String key, Object object) {
        SecuritySharedPreference securitySharedPreference = new SecuritySharedPreference(
                context.getApplicationContext(), FILE_NAME, Context.MODE_PRIVATE);
        SecuritySharedPreference.SecurityEditor editor = securitySharedPreference.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object != null ? object.toString() : "");
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context       application context
     * @param key           string key
     * @param defaultObject default value
     * @return value in local persist storage.
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SecuritySharedPreference sp = new SecuritySharedPreference(
                context.getApplicationContext(), FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context application context
     * @param key     string key
     */
    public static void remove(Context context, String key) {
        SecuritySharedPreference sp = new SecuritySharedPreference(
                context.getApplicationContext(), FILE_NAME, Context.MODE_PRIVATE);
        SecuritySharedPreference.SecurityEditor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context application context
     */
    public static void clearAll(Context context) {
        SecuritySharedPreference sp = new SecuritySharedPreference(
                context.getApplicationContext(), FILE_NAME, Context.MODE_PRIVATE);
        SecuritySharedPreference.SecurityEditor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context application context
     * @param key     string key
     * @return result of local persist storage.
     */
    public static boolean contains(Context context, String key) {
        SecuritySharedPreference sp = new SecuritySharedPreference(
                context.getApplicationContext(), FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context application context
     * @return result of local persist storage.
     */
    public static Map<String, ?> getAll(Context context) {
        SecuritySharedPreference sp = new SecuritySharedPreference(
                context.getApplicationContext(), FILE_NAME, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 处理加密过渡（从未加密过渡到加密数据，在下次升级前调用一次即可）
     *
     * @param context
     */
    public static void handleTransition(Context context) {
        boolean handleTransition = (boolean) get(context, "handleTransition", false);
        if (!handleTransition) {
            SecuritySharedPreference sp = new SecuritySharedPreference(
                    context.getApplicationContext(), FILE_NAME, Context.MODE_PRIVATE);
            sp.handleTransition();
            put(context, "handleTransition", true);
        }
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {

        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return Method
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor sharedPreferences editor
         */
        @SuppressWarnings("TryWithIdenticalCatches")
        static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }
}

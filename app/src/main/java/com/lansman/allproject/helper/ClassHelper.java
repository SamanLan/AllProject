package com.lansman.allproject.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2017/12/1 11:31<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class ClassHelper {


    private final String UNZIPPATH = "/hotfix_unzip";

    private void note() {
        /**
         * 打出dex的具体操作
         * 直接在build-tools/安卓版本 目录下使用命令行窗口（终端）使用dx.bat
         * dx --dex --output=dex文件完整路径 (空格) 要打包的完整class文件所在目录
         */
        /**
         * PathClassLoader：
         * 只能加载已经安装到Android系统中的apk文件（/data/app目录），是Android默认使用的类加载器。
         */
        /**
         * DexClassLoader：
         * 可以加载任意目录下的dex/jar/apk/zip文件，比PathClassLoader更灵活，是实现热修复的重点。
         */
        // 1. PathClassLoader与DexClassLoader都继承于BaseDexClassLoader。
        // 2. BaseDexClassLoader有DexPathList，DexPathList中含有dex数组
    }

    /**
     * 构造dexClassLoader,进行补丁合并
     * @param context context
     * @param dexFile 补丁文件，dex、jar、zip、apk
     */
    public void structure(Context context, @NonNull File dexFile) {
        try {
            // 解压jar、zip、apk后的dex存放目录
            File unZipPath = new File(context.getFilesDir().getAbsolutePath() + UNZIPPATH);
            if (!unZipPath.exists()) {
                unZipPath.mkdirs();
            }
            // 父亲loader
            PathClassLoader parentLoader = (PathClassLoader) context.getClassLoader();
            // dex的loader
            DexClassLoader dexClassLoader = new DexClassLoader(
                    // 修复好的dex（补丁）所在目录
                    dexFile.getAbsolutePath(),
                    // 存放dex的解压目录（用于jar、zip、apk格式的补丁）
                    unZipPath.getAbsolutePath(),
                    // 加载dex时需要的库
                    null,
                    // 父类加载器
                    parentLoader);

            Object beforeList = getDexPathList(parentLoader);
            Object betweenList = getDexPathList(dexClassLoader);
            Object beforeDexElementsList = getDexElements(beforeList);
            Object betweenDexElementsList = getDexElements(betweenList);
            Object afterDexElementsList = combineArray(betweenDexElementsList, beforeDexElementsList);
            // 需再次获取一次
            Object beforeListAgain = getDexPathList(parentLoader);
            setField(beforeListAgain, afterDexElementsList, "dexElements", beforeListAgain.getClass());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Toast.makeText(context, "打补丁完成", Toast.LENGTH_SHORT).show();
    }

    private Object getDexPathList(Object o) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(o, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private Object getDexElements(Object o) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        return getField(o, o.getClass(), "dexElements");
    }

    /**
     * 反射获取值
     */
    private static Object getField(Object o, Class cls, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(o);
    }

    /**
     * 反射设置值
     */
    private static void setField(Object o, Object value, String fieldName, Class cls) throws NoSuchFieldException, IllegalAccessException {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(o, value);
    }

    /**
     * 数组合并
     */
    private static Object combineArray(Object newList, Object oldList) {
        Class<?> componentType = newList.getClass().getComponentType();
        // 得到左数组长度（补丁数组）
        int i = Array.getLength(newList);
        // 得到原dex数组长度
        int j = Array.getLength(oldList);
        // 得到总数组长度（补丁数组+原dex数组）
        int k = i + j;
        // 创建一个类型为componentType，长度为k的新数组
        Object result = Array.newInstance(componentType, k);
        System.arraycopy(newList, 0, result, 0, i);
        System.arraycopy(oldList, 0, result, i, j);
        return result;
    }
}


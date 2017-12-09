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

        /************************************************************/
        /**
         * JAVA
         * BootStrap ClassLoader：
         * 称为启动类加载器，是Java类加载层次中最顶层的类加载器，负责加载JDK中的核心类库。
         * 但是Bootstrap ClassLoader不继承自ClassLoader，因为它不是一个普通的Java类，底层由C++编写，已嵌入到了JVM内核当中
         * Extension ClassLoader：
         * 称为扩展类加载器，负责加载Java的扩展类库，默认加载JAVA_HOME/jre/lib/ext/目下的所有jar
         * App ClassLoader：
         * 称为系统类加载器，负责加载应用程序classpath目录下的所有jar和class文件
         * 自定义ClassLoader：
         * 自定义的ClassLoader都必须继承自java.lang.ClassLoader类
         *
         * classloader加载class过程
         * 调用loadClass
         *     ↓
         * 调用findLoadedClass(String) 去检测这个class是不是已经加载过了    →   加载过直接返回
         *     ↓
         * 执行父加载器的 loadClass方法。如果父加载器为null，则jvm内置的加载器去替代    →   找到或者加载后即返回
         *     ↓
         * findClass(String)
         *     ↓
         * resolveClass(Class)
         */
        /***********************************************************/
        /**
         * ANDROID
         * BootClassLoader：
         * 作用和 Java 中的 Bootstrap ClassLoader 作用是类似的，是用来加载 Framework 层的字节码文件的
         * PathClassLoader：
         * 只能加载已经安装到Android系统中的apk文件（/data/app目录），是Android默认使用的类加载器。
         * DexClassLoader：
         * 可以加载任意目录下的dex/jar/apk/zip文件，比PathClassLoader更灵活，是实现热修复的重点。
         *
         * PathClassLoader与DexClassLoader都继承于BaseDexClassLoader。
         *
         * BaseDexClassLoader有DexPathList，DexPathList中含有dex数组
         *
         * classloader加载class过程
         * 和Java基本类似
         */
        /***********************************************************/
        /**
         * 自定义ClassLoader直接覆写findClass(String)即可
         * 在 findClass() 方法中调用 defineClass()
         * 一个 ClassLoader 创建时如果没有指定 parent，那么它的 parent 默认就是 AppClassLoader
         */
        /***********************************************************/
        /**
         * 打出dex的具体操作
         * 直接在build-tools/安卓版本 目录下使用命令行窗口（终端）使用dx.bat
         * dx --dex --output=dex文件完整路径 (空格) 要打包的完整class文件所在目录
         */
        /***********************************************************/
        /**
         * 双亲代理模型
         * 1. 类加载的共享功能
         * 2. 类加载的隔离功能
         */
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


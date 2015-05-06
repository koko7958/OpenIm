package com.way.activity;


import java.util.HashMap;
import java.util.Map;


import com.way.xx.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;



import com.way.util.PreferenceConstants;
import com.way.util.PreferenceUtils;
import com.way.util.XMPPHelper;





public class UserInforSettingActivity extends Activity {

    private RelativeLayout re_avatar;//头像
    private RelativeLayout re_name;//昵称
    private RelativeLayout re_fxid;//ID
    private RelativeLayout re_sex; //性别
    private RelativeLayout re_region;//区域
 	private RelativeLayout re_signature; //个性签名
 	
 	private ImageView mHeadIcon;//头像
 	private TextView mNickView; //昵称
 	private TextView mIMIDView; //ID
 	private TextView mAdressView; //地址
 	private TextView mSexView; //性别
 	private TextView mDistrictView; //区域
 	private TextView mSignatureView; //个性签名

 	
    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
    
   
		mHeadIcon = (ImageView) this.findViewById(R.id.iv_avatar); //头像
		mNickView = (TextView) this.findViewById(R.id.tv_name);//昵称
		mIMIDView = (TextView) this.findViewById(R.id.tv_fxid);//ID
		//mAdressView = (TextView) this.findViewById(R.id.tv_fxid);//地址
		mSexView = (TextView) this.findViewById(R.id.tv_sex);//性别
		mDistrictView = (TextView) this.findViewById(R.id.tv_region);//区域
		mSignatureView = (TextView) this.findViewById(R.id.tv_sign);//个性签名
        
	
      // refresh();
		
		mHeadIcon.setImageResource(R.drawable.login_default_avatar);
		mNickView.setText(XMPPHelper.splitJidAndServer(PreferenceUtils.getPrefString(this, PreferenceConstants.MY_NICKNAME, "")));
		mIMIDView.setText(XMPPHelper.splitJidAndServer(PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, "")));
		//mAdressView.setText(XMPPHelper.splitJidAndServer(PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, "")));
		mSexView.setText(XMPPHelper.splitJidAndServer(PreferenceUtils.getPrefString(this, PreferenceConstants.MY_SEX, "")));
		mDistrictView.setText(XMPPHelper.splitJidAndServer(PreferenceUtils.getPrefString(this, PreferenceConstants.MY_DISTRICT, "")));
		mSignatureView.setText(XMPPHelper.splitJidAndServer(PreferenceUtils.getPrefString(this, PreferenceConstants.MY_SIGNATURE, "")));
		
		
		
		//re_avatar = (RelativeLayout) this.findViewById(R.id.re_avatar);
	    re_name = (RelativeLayout) this.findViewById(R.id.re_name);
	    //re_fxid = (RelativeLayout) this.findViewById(R.id.re_fxid);
	    re_sex = (RelativeLayout) this.findViewById(R.id.re_sex);
	    re_region = (RelativeLayout) this.findViewById(R.id.re_region);
	    re_signature = (RelativeLayout) this.findViewById(R.id.re_sign);
	    
	   // re_avatar.setOnClickListener(new MyListener());
	    re_name.setOnClickListener(new MyListener());
	   // re_fxid.setOnClickListener(new MyListener());
	    re_sex.setOnClickListener(new MyListener());
	    re_region.setOnClickListener(new MyListener());
	    re_signature.setOnClickListener(new MyListener());
	



		
		
		
		
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.ACCOUNT, ""));		
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.PASSWORD, ""));
		//System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.GMAIL_SERVER, ""));
	    //System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.REPORT_CRASH, ""));
		//System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.Server, "")); //force close happened if enable it.
		//System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.AUTO_START, ""));
		/*System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.SHOW_MY_HEAD, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.APP_VERSION, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.OFFLINE, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.DND, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.XA, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.AWAY, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.AVAILABLE, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.CHAT, ""));

		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.JID, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.CUSTOM_SERVER, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.PORT, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.RESSOURCE, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.PRIORITY, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.DEFAULT_PORT, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.CONN_STARTUP, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.AUTO_RECONNECT, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.MESSAGE_CARBONS, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.SHOW_OFFLINE, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.LEDNOTIFY, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.VIBRATIONNOTIFY, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.SCLIENTNOTIFY, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.TICKER, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.FOREGROUND, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.SMACKDEBUG, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.REQUIRE_TLS, ""));

		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.STATUS_MODE, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.STATUS_MESSAGE, ""));
		System.out.println(PreferenceUtils.getPrefString(this, PreferenceConstants.THEME, ""));

*/


    }


    class MyListener implements OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.re_avatar:

            //showPhotoDialog();

            break;
        case R.id.re_name:
           // startActivityForResult(new Intent(MyUserInfoActivity.this,
          //          UpdateNickActivity.class),UPDATE_NICK);
            break;
        case R.id.re_fxid:
        	/*
            if (LocalUserInfo.getInstance(MyUserInfoActivity.this)
                    .getUserInfo("fxid").equals("0")) {
                startActivityForResult(new Intent(MyUserInfoActivity.this,
                        UpdateFxidActivity.class),UPDATE_FXID);

            }
            */
            break;
        case R.id.re_sex:
            showSexDialog();
            break;
        case R.id.re_region:

            break;
        case R.id.re_sign:

            break;

        }
    }
    }

    private void showSexDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.alertdialog);
        LinearLayout ll_title = (LinearLayout) window
                .findViewById(R.id.ll_title);
        ll_title.setVisibility(View.VISIBLE);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        tv_title.setText("性别");
        // 为确认按钮添加事件,执行退出应用操作
        TextView tv_paizhao = (TextView) window.findViewById(R.id.tv_content1);
        tv_paizhao.setText("男");
        tv_paizhao.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            public void onClick(View v) {
               // if (!XMPPHelper.splitJidAndServer(PreferenceUtils.getPrefString(this, PreferenceConstants.MY_SEX, "")).equals("1")) 
                {
                	mSexView.setText("男");

                   // updateSex("1");
                }

                dlg.cancel();
            }
        });
        TextView tv_xiangce = (TextView) window.findViewById(R.id.tv_content2);
        tv_xiangce.setText("女");
        tv_xiangce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

               // if (!XMPPHelper.splitJidAndServer(PreferenceUtils.getPrefString(this, PreferenceConstants.MY_SEX, "")).equals("2"))
                {

                	mSexView.setText("女");
                  //  updateSex("2");
                }

                dlg.cancel();
            }
        });

    }

    public void back(View view) {
        finish();
    }

 
}

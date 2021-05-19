package com.company1.automatic_test;



import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.os.SystemClock.sleep;

@SuppressLint("NewApi")
public class MyService extends AccessibleExtract {

    private static final String TAG = "MyAccessibility";

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "config success!");
    }

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // TODO Auto-generated method stub
        int eventType = event.getEventType();
        int flag = 0;//判断易班载入是否出错 出错则退出
        List<String> YiBan = new ArrayList<String>();
        List<String> ZhiNaBai = new ArrayList<String>();
        YiBan.add("易班");
        ZhiNaBai.add("拼多多");
        String eventText = "";
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                Log.i(TAG, "==============Start====================");
                if (event.getText().toString().equals(ZhiNaBai.toString()) == true) {

                    while (this.clickTextView(getRootInActiveWindow(), "签到") == false) ;
                    while (this.returnTextContain(getRootInActiveWindow(), "领取") == null);
                    Log.i(TAG,this.returnTextContain(getRootInActiveWindow(), "领取").toString());
                    sleep(1000);
                    this.returnTextContain(getRootInActiveWindow(), "领取").getParent().getParent().getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
//                    int pdd=0;
//                    while(pdd<10){
//                        if(this.returnTextContain(getRootInActiveWindow(), "0g")!=null){
//                            Rect rect = new Rect();
//                            this.returnTextContain(getRootInActiveWindow(), "0g").getBoundsInScreen(rect);
//                            Log.i(TAG,rect.toString());
//                            this.dispatchGestureView(rect.centerX(),rect.centerX());
//                            pdd++;
//                        }
//                        sleep(1000);
//                    }
                    break;
                }

                if (event.getText().toString().equals(YiBan.toString()) == false) {
                    break;
                }
                while (getRootInActiveWindow() == null) ;
                List<AccessibilityNodeInfo> nodeList = new ArrayList<AccessibilityNodeInfo>();
                while (this.returnTextEqual(getRootInActiveWindow(), "校本化") == null) {
                    flag = flag + 1;
                    if (flag > 10) {
                        break;
                    }
                    Log.i(TAG, String.valueOf(flag));
                    nodeList=getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.yiban.app:id/btn_splash_ad");
                    Log.i(TAG, nodeList.toString());
                    Log.i(TAG, String.valueOf((nodeList.size()==0)));
                    if(nodeList.size()!=0){
                        nodeList.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    SystemClock.sleep(300);
                }
                if (flag >= 10) {

                    Toast.makeText(this, "打卡失败", Toast.LENGTH_LONG).show();
                    break;
                }
                while (this.clickTextView(getRootInActiveWindow(), "校本化") == false) ;
                while (this.clickTextView(getRootInActiveWindow(), "任务反馈")==false);
                int cout=0;
                while (this.clickTextView(getRootInActiveWindow(), "未反馈") == false){
                    sleep(100);
                    cout++;
                    if(cout>=50){
                        break;
                    }
                }
                if(cout>=50){
                    Toast.makeText(this, "今日打卡已完成", Toast.LENGTH_LONG).show();
                    performGlobalAction(GLOBAL_ACTION_HOME);
                    return;
                }
                while(this.clickTextView(getRootInActiveWindow(),"反 馈")==false);
                SystemClock.sleep(1000);
                for (AccessibilityNodeInfo accessibilityNodeInfo : this.allReturnClassEqual(getRootInActiveWindow(), "android.widget.EditText")) {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "37");
                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    Log.i(TAG, accessibilityNodeInfo.toString());
                };
                while (this.clickTextView(getRootInActiveWindow(), "图标") == false) ;
                SystemClock.sleep(1000);
                Rect rect = new Rect();
                this.returnTextEqual(getRootInActiveWindow(),"否").getBoundsInScreen(rect);
                Log.i(TAG,rect.toString());
                this.dispatchGestureScroll(0,rect.centerX(),rect.bottom,rect.centerX(),rect.top+30);
                SystemClock.sleep(1000);
                while (this.clickTextView(getRootInActiveWindow(), "确定") == false) ;
                SystemClock.sleep(300);
                while (this.clickTextView(getRootInActiveWindow(), "提 交") == false) ;
                SystemClock.sleep(2000);
                Toast.makeText(this, "今日打卡已完成", Toast.LENGTH_LONG).show();
                Log.i(TAG, "=============END=====================");
                break;
        }
    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub

    }

}
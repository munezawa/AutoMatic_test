package com.company1.automatic_test;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.os.SystemClock.sleep;

public abstract class AccessibleExtract extends AccessibilityService {
     public AccessibilityNodeInfo returnWebView(AccessibilityNodeInfo nowNode){//深度搜索返回所有子布局中的第一个WebView节点
        if(nowNode==null) return null;

        if(nowNode.getClassName().toString().equals("android.webkit.WebView")){
            return nowNode;
        }
        if(nowNode.getChildCount()==0) return null;

        int size = nowNode.getChildCount();
        AccessibilityNodeInfo webViewNode = null;
        for(int i=0;i<size;i++){
            webViewNode = returnWebView(nowNode.getChild(i));
            if(webViewNode!=null) return webViewNode;
        }
        return null;
    }
    public List<AccessibilityNodeInfo> allReturnClassEqual(AccessibilityNodeInfo nowNode, String text){//深度搜索 以列表形式 返回所有子布局中所有满足类名要求的节点
        List<AccessibilityNodeInfo> nodeList = new ArrayList<AccessibilityNodeInfo>();
        if(nowNode==null) return null;
        if(nowNode.getClassName().toString().equals(text)){
            nodeList.add(nowNode);
        }
        if(nowNode.getChildCount()==0) return nodeList;

        int size = nowNode.getChildCount();
        for(int i=0;i<size;i++){
            for (AccessibilityNodeInfo accessibilityNodeInfo : allReturnClassEqual(nowNode.getChild(i), text)) {
                nodeList.add(accessibilityNodeInfo);
            }
        }
        return nodeList;
    }
    public AccessibilityNodeInfo returnTextEqual(AccessibilityNodeInfo nowNode,String text){//深度搜索返回第一个text满足条件的子布局
        if(nowNode==null) return null;
        if(nowNode.getText()!=null){
            if(nowNode.getText().toString().equals(text)){
                return nowNode;
            }
        }
        if(nowNode.getChildCount()==0) return null;

        int size = nowNode.getChildCount();
        AccessibilityNodeInfo textEqualNode = null;
        for(int i=0;i<size;i++){
            textEqualNode = returnTextEqual(nowNode.getChild(i),text);
            if(textEqualNode!=null) return textEqualNode;
        }
        return null;
    }
    public AccessibilityNodeInfo returnTextContain(AccessibilityNodeInfo nowNode,String text){//深度搜索返回第一个文本值包含text的控件
        if(nowNode==null) return null;
        if(nowNode.getText()!=null){
            if(nowNode.getText().toString().contains(text)){
                return nowNode;
            }
        }
        if(nowNode.getChildCount()==0) return null;

        int size = nowNode.getChildCount();
        AccessibilityNodeInfo textEqualNode = null;
        for(int i=0;i<size;i++){
            textEqualNode = returnTextContain(nowNode.getChild(i),text);
            if(textEqualNode!=null) return textEqualNode;
        }
        return null;
    }
    public AccessibilityNodeInfo returnIdEqual(AccessibilityNodeInfo nowNode,String text){//深度搜索返回第一个Id符合条件的布局
        if(nowNode==null) return null;
        if(nowNode.getText()!=null){
            if(nowNode.getViewIdResourceName().toString().equals(text)){
                return nowNode;
            }
        }
        if(nowNode.getChildCount()==0) return null;

        int size = nowNode.getChildCount();
        AccessibilityNodeInfo textEqualNode = null;
        for(int i=0;i<size;i++){
            textEqualNode = returnTextEqual(nowNode.getChild(i),text);
            if(textEqualNode!=null) return textEqualNode;
        }
        return null;
    }
    public boolean dispatchGestureView(int x, int y) {
        boolean res = false;
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(x, y);
        p.lineTo(x, y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 100L));
        GestureDescription gesture = builder.build();
        Log.d("","点击了位置"+"("+x+","+y+")");
        sleep(200);
        res = dispatchGesture(gesture, new GestureResultCallback(){}, null);
        return res;
    }
    public void dispatchGestureScroll(final int flag, int sx, int sy,int ex,int ey) {
//        debug("sx:"+sx+"sy:"+sy+"ex:"+ex+"ey:"+ey);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(sx, sy);
        p.lineTo(ex, ey);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0L, 100L));
        GestureDescription gesture = builder.build();
        dispatchGesture(gesture, new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, flag+"onCompleted: 完成..........");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, flag+"onCompleted: 取消..........");
            }
        }, null);
    }
    public AccessibilityNodeInfo findClickAble(AccessibilityNodeInfo nowNode){
        if(nowNode==null)
        {
            return null;
        }
        while(nowNode.isClickable()==false)
        {
            while(nowNode.getParent()==null){
                sleep(100);
            }
            nowNode=nowNode.getParent();
        }
        return nowNode;
    }
    public boolean clickTextView(AccessibilityNodeInfo nowNode,String text){
        if(returnTextEqual(nowNode,text)!=null)
        {
            findClickAble(returnTextEqual(nowNode,text)).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return true;
        }
        return false;
    }
    public boolean confirmClickTextView(AccessibilityNodeInfo nowNode, String text){

        int cout=0;
        while (this.clickTextView(nowNode, text) == false){
            sleep(100);
            cout++;
            if(cout>=100){
                break;
            }
        }
        if(cout>=100){ return false; }
        else { return true; }
    }
}

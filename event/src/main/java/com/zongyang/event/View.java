package com.zongyang.event;

/**
 * author : quzongyang
 * e-mail : quzongyang@xiaohe.com
 * time   : 2021/05/28
 * desc   :
 * version: 1.0
 */


public class View {

    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;

    public OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.mOnTouchListener = onTouchListener;
    }

    private int left;
    private int top;
    private int right;
    private int bottom;

    public View(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    //判断点击事件是否在View范围内
    public  boolean isContainer(int x,int y){
        if(x >= left && x<right && y>= top && y< bottom){
            return true;
        }
        return false;
    }

    /**
     *
     * @param event
     * @return
     */
    public boolean dispatchTouchEvent(MotionEvent event){
        boolean result = false;
        if(mOnTouchListener != null && mOnTouchListener.onTouch(this,event)){
            result = true;
        }
        if(!result && onTouchEvent(event)){
            result = true;
        }
        return result;

    }

    private boolean onTouchEvent(MotionEvent event){
      //  System.out.println("==========内部得onTouchEvent=============");
        if(mOnClickListener != null){
            mOnClickListener.onClick(this);
            return true;
        }
        return false;
    }


}

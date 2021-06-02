package com.zongyang.event;

import java.util.ArrayList;
import java.util.List;

/**
 * author : quzongyang
 * e-mail : quzongyang@xiaohe.com
 * time   : 2021/05/28
 * desc   :
 * version: 1.0
 */


public class ViewGroup extends View {
    private TouchTarget mFirstTouchTarget;

    public ViewGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    private String name;

    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];

    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);
        mChildren = (View[]) childList.toArray(new View[childList.size()]);
    }

    //事件拦截
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }


    //事件分发入口
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handle = false;
        boolean intercepted = onInterceptTouchEvent(ev);
        int actionMasked = ev.getActionMasked();
        //第一步 找到Down事件  确定下来事件由哪个事件接收
        if (actionMasked == MotionEvent.ACTION_DOWN && !intercepted) {
            final View[] children = mChildren;
            //遍历子View  倒叙遍历   因为先添加的View在数组前边 ，而后添加的在视图上边，先询问在上层的子View就要倒叙数组遍历
            for (int i = children.length - 1; i >= 0; i--) {
                View child = mChildren[i];
                //判断点击事件是否在View范围内
                if (!child.isContainer(ev.getX(), ev.getY())) {
                    continue;
                }
                //能接受到事件的View
                if (dispatchTransformedTouchEvent(ev, child)) {
                    handle = true;
                    mFirstTouchTarget= addTouchTarget(child);
                }
            }
        }

        if (mFirstTouchTarget == null) { //没有子控件消费
            //自己来消费
            dispatchTransformedTouchEvent(ev, null);
        }else{
            //确定
            TouchTarget target = mFirstTouchTarget;
            while (target != null){
                TouchTarget next = target.next;
                if(dispatchTransformedTouchEvent(ev,target.child)){
                    handle = true;
                }
                target = next;
            }
        }
        return false;
    }

    private TouchTarget addTouchTarget(View child){
        final TouchTarget target = TouchTarget.obtin(child);
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;
        return target;
    }

    //分发处理   子控件 View
    private boolean dispatchTransformedTouchEvent(MotionEvent ev, View child) {
        boolean handle = false;
        if (child != null) {
            handle = child.dispatchTouchEvent(ev);
        } else {
            super.dispatchTouchEvent(ev);
        }
        return handle;
    }

    private static final class TouchTarget {
        public View child; //当前缓存View
        public TouchTarget next;

        private static int sRecycleCount;

        private static final Object sRecycleLock = new Object[0];

        private static TouchTarget sRecycleBin;

        public static TouchTarget obtin(View child) {
            TouchTarget target;
            synchronized (sRecycleBin) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                }
                sRecycleBin = target.next;
                sRecycleCount--;
                target.next = null;
            }
            target.child = child;
            return target;
        }

        public void recycle() {
            if (child == null) {
                throw new IllegalStateException("已经被回收了");
            }
            synchronized (sRecycleLock) {
                if (sRecycleCount < 32) {
                    next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycleCount += 1;
                }
            }
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

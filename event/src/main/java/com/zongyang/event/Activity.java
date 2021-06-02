package com.zongyang.event;

/**
 * author : quzongyang
 * e-mail : quzongyang@xiaohe.com
 * time   : 2021/05/28
 * desc   :
 * version: 1.0
 */


public class Activity {

    public static void main(String[] arg){
        MotionEvent motionEvent = new MotionEvent(100,100);
        motionEvent.setActionMasked(MotionEvent.ACTION_DOWN);
        dispatchTouchEvent(motionEvent);

    }

    private static boolean dispatchTouchEvent(MotionEvent ev) {
        //UI 摆放   手动模拟摆放流程
        ViewGroup viewGroup = new ViewGroup(0,0,1000,1000){
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                return true;
            }
        };
        viewGroup.setName("顶级容器");

        View view = new View(0,0,200,200);
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                System.out.println("==========1=============");
                return true;
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("==========点击=============");
            }
        });
        viewGroup.addView(view);
        viewGroup.dispatchTouchEvent(ev);
        //System.out.println("==========点击=============");
        return true;

    }
}

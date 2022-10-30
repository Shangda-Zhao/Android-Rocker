package com.yaogan.liziguo.yaogan;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.net.SocketException;

/**
 * Created by Liziguo on 2018/6/16.
 */

public class OnTouchMove extends View { //这个view负责监听移动摇杆的手势

//    private CommunicateWordsWithUDP transformWords;

    private Move m;

    public OnTouchMove(Context context,Move move) {
        super(context);
        this.m=move;
        setBackgroundColor(Color.WHITE);//背景色设为白色
        getBackground().setAlpha(my.ontouchAlpha);//设置触控区透明度
        setOnTouchListener(new OnTouchListener() { //设置触控监听
            @Override
            public boolean onTouch(View v, MotionEvent ev) {
                //加上getX() getY()因为这个view不是分布在左上角的
                final float xx = ev.getX() + getX(), yy = ev.getY() + getY();

                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    m.down(xx, yy);//按下时的操作
//                    ToastUtils toastUtils = new ToastUtils();
//                    try {
//                        String ip = "192.168.137.233";
//                        String ip = "127.0.0.1";
////                        transformWords=new CommunicateWordsWithUDP(ip);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                transformWords.sendDataWithUDPSocket("hello world");
//                            }
//                        }).start();
//
//                        toastUtils.showShortToast("hello world");

//                    } catch (SocketException e) {
//                        e.printStackTrace();
//                        toastUtils.showShortToast("非法的ip地址！");
////                        Toast.makeText(MainActivity.this, "非法的ip地址！",Toast.LENGTH_SHORT).show();
//                    }
//                    m.move(xx, yy);
                }
                m.move(xx, yy);//移动时的操作
                if (ev.getAction() == MotionEvent.ACTION_UP) {
                    m.up();//松开时的操作
                }
                return true;//不要返回false
            }
        });
    }

}

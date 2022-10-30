package com.yaogan.liziguo.yaogan;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Liziguo on 2018/6/16.
 */

public class my { //这个类当一个全局变量使用
    public static int w,h;//屏幕的宽高
    public static float bili;
    public static MainActivity main;
    public static RectF re=new RectF();
    public static int ontouchAlpha=0;//触控区透明度0-255 0为透明，为了测试我们先设为100

    public static Bitmap border,cancel,down,yaogan, cd, imgBackground;

    public static Skill skill;//当前正在使用的技能
}

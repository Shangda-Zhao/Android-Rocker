package com.yaogan.liziguo.yaogan;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Liziguo on 2018/6/16.
 */

public abstract class Skill {
    public int jineng;
    private final float x,y;//技能图标中心位置，不是按下时的位置
    private float x2, y2;//技能按下移动后手指的坐标
    private float xxx,yyy;//判断拖动点是否超出两倍r的范围
    private final float calcelx, cancely;
    public float angle;//技能按下后 x y指向xx yy的角度
    public Bitmap img, imgborder, imgdown, imgyaogan,imgcd,imgcancel;
    private final float r2;
    private final float r3=50*my.bili;
    public boolean down=false;
    public boolean down_main=false;//down_main 只触发一次;
    public boolean cancel=false;
    public int cdmax;
    public long last,cd=0;//last最后一次释放技能的时间
    /*
    0 普通攻击
    1 技能1
    2 技能2
    3 技能3
     */
    public Skill(int jineng, int cd, Bitmap image){
        this.jineng=jineng;
        switch (jineng){
            case 0:
//                x= my.w*0.87f;
//                y= my.h*0.8f;
                x= my.w*0.87f;
                y= my.h*0.85f;
                break;
            case 1:
                x= my.w*0.71f;
                y= my.h*0.88f;
                break;
            case 2:
                x= my.w*0.76f;
                y= my.h*0.66f;
                break;
            case 3:
                x= my.w*0.9f;
                y= my.h*0.61f;
                break;
            default:x=y=0;
        }
        cdmax=cd;
        if(jineng == 0) r2=125*my.bili;
        else r2=80*my.bili;
        calcelx =my.w-r2*2;
        cancely =my.h/4;
        img=image;
        imgborder=my.border;
        imgdown=my.down;
        imgyaogan=my.yaogan;
        imgcd=my.cd;
        imgcancel=my.cancel;
    }
    //    public abstract void down();
//    public abstract void move();
//    public abstract void up();
    public void down(){ //DOWN 由ontouch触发
        if(cd>0)return;
        down=true;
        my.skill=this;

        MainActivity.addActionToBuffer(jineng + 1);
    }
    public abstract void down_main(); //DOWN 教程用不到该抽象方法

    public void move(float x,float y){//按下技能后 由ontouch触发
        x2 =x;
        y2 =y;
        angle=getAngle(x2, y2);
        cancel=incancel(x,y);
        if (jineng !=0 && !in2(x,y)) {
            xxx= (float) (this.x+ Math.sin(angle)*r2*2);
            yyy= (float) (this.y+ Math.cos(angle)*r2*2);
        }else{
            xxx=x;
            yyy=y;
        }
    }
    public abstract void move_main();//按下技能后 由MyActor触发 教程用不到该抽象方法
    public abstract void up(); //松开后 由MyActor触发 释放技能

    public boolean in(float xx,float yy){ //判断是否被点中
        double r= Math.sqrt((x - xx)*(x-xx)+(y-yy)*(y-yy));
        if(r<r2)
            return true;
        else return false;
    }
    public boolean in2(float xx, float yy) { //判断拖动点是否超出两倍r的范围
        double r = Math.sqrt((x - xx) * (x - xx) + (y - yy) * (y - yy));
        if (r < r2 * 2)
            return true;
        else return false;
    }
    public boolean incancel(float xx,float yy){ //判断是否取消
        double r= Math.sqrt((calcelx - xx)*(calcelx -xx)+(cancely -yy)*(cancely -yy));
        if(r<r2)
            return true;
        else return false;
    }
    public float getAngle(float xx,float yy){ //x y指向xx yy的角度
        float angle,k;
        if (y==yy)
            if (x > xx)
                angle= (float) (-Math.PI/2);
            else
                angle= (float) (Math.PI/2);
        else{
            k=(x-xx)/(y-yy);
            if (y > yy) {
                angle= (float) (Math.atan(k) + Math.PI);
            } else {
                angle= (float) Math.atan(k);
            }
            if(angle>Math.PI)
                angle-=Math.PI*2;
            else if(angle<-Math.PI)
                angle+=Math.PI*2;

        }
        return angle;
    }
    private float drawpx=10*my.bili;

    public void next(){
        //计算技能冷却时间
        cd=cdmax-System.currentTimeMillis()+last;
    }
    //按下的时候技能图标下移 显示蓝色框框
    public void onDraw(Canvas g, Paint p){
        my.re.left=x-r2;
        my.re.top=y-r2;
        my.re.right=x+r2;
        my.re.bottom=y+r2;
        if(down){
//            new RectF(x-r2,y-r2,x+r2,y+r2);
//            new RectF(x-r2,y-r2+10*my.bili,x+r2,y+r2+10*my.bili);
//            my.re.left=x-r2;
//            my.re.top=y-r2;
//            my.re.right=x+r2;
//            my.re.bottom=y+r2;
            if(jineng!=0){
                final float bl=2;
                my.re.left=x-r2*bl;
                my.re.top=y-r2*bl;
                my.re.right=x+r2*bl;
                my.re.bottom=y+r2*bl;
                //蓝色框框未下移
                g.drawBitmap(imgdown,null,my.re,p);
            }
            my.re.left=x-r2;
            my.re.top=y-r2;
            my.re.right=x+r2;
            my.re.bottom=y+r2;
            ///////////////////////////////////////////////////////////
            //技能图片和技能边框下移
            my.re.top+=drawpx;
            my.re.bottom+=drawpx;
            g.drawBitmap(img,null,my.re,p);
            my.re.left-=drawpx;
            my.re.top-=drawpx;
            my.re.right+=drawpx;
            my.re.bottom+=drawpx;

            g.drawBitmap(imgborder,null,my.re,p);
            if(jineng!=0){
                my.re.left=xxx-r3;
                my.re.top=yyy-r3;
                my.re.right=xxx+r3;
                my.re.bottom=yyy+r3;
                g.drawBitmap(imgyaogan,null,my.re,p);
                //cancle
                my.re.left= calcelx -r2;
                my.re.top= cancely -r2;
                my.re.right= calcelx +r2;
                my.re.bottom= cancely +r2;
                g.drawBitmap(imgcancel,null,my.re,p);

                // ************************  //
//                my.re.left=0;
//                my.re.top=0;
//                my.re.right=600;
//                my.re.bottom=600;
//                g.drawBitmap(imgyaogan,null,my.re,p);
            }
        }else{
            g.drawBitmap(img,null,my.re,p);
            if(jineng!=0 && cd>0) {
                p.setTextSize(40*my.bili);
                p.setColor(Color.WHITE);
                g.drawBitmap(imgcd,null,my.re,p);
                float f=cd/100f;
                f=(int)f;
                f=f/10;
                g.drawText(String.valueOf(f),x-p.getTextSize()*4/5,y+p.getTextSize()/3,p);

            }
            my.re.left-=drawpx;
            my.re.top-=drawpx;
            my.re.right+=drawpx;
            my.re.bottom+=drawpx;
            g.drawBitmap(imgborder,null,my.re,p);
        }
    }
}

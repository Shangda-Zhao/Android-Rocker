package com.yaogan.liziguo.yaogan;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.widget.RelativeLayout;

/**
 * Created by Liziguo on 2018/6/15.
 */

public class Hua extends RelativeLayout implements Runnable{ //继承RelativeLayout 实现Runnable接口

    private Paint p;//画笔
    static public Move m =new Move(0);//移动摇杆0

    static public Move m2 =new Move(1);//移动摇杆1

     /*
    A 普通攻击
    Q 技能1
    W 技能2
    E 技能3
    R 没有R
     */

    public Skill A=new Skill(0,500, BitmapFactory.decodeResource(getResources(),R.mipmap.putonggongji)) {
        @Override
        public void down_main() { }
        @Override
        public void move_main() { }
        @Override
        public void up() { }
    };
    public Skill Q=new Skill(1,1000, BitmapFactory.decodeResource(getResources(),R.mipmap.skill1)) {
        @Override
        public void down_main() { }
        @Override
        public void move_main() { }
        @Override
        public void up() {
            down_main=false;
            if(!cancel){ //技能冷却时间
                last= System.currentTimeMillis();
            }
        }
    };
    public Skill W=new Skill(2,1000, BitmapFactory.decodeResource(getResources(),R.mipmap.skill2)) {
        @Override
        public void down_main() { }
        @Override
        public void move_main() { }
        @Override
        public void up() {
            down_main=false;
            if(!cancel){
                last= System.currentTimeMillis();
            }
        }
    };
    public Skill E=new Skill(3,1000, BitmapFactory.decodeResource(getResources(),R.mipmap.skill3)) {
        @Override
        public void down_main() { }
        @Override
        public void move_main() { }
        @Override
        public void up() {
            down_main=false;
            if(!cancel){
                last= System.currentTimeMillis();
            }
        }
    };

    public Hua(Context context) {
        super(context);
        p=new Paint();
        setBackgroundColor(Color.GRAY);//背景颜色设为黑色
        //实例化一个OnTouchMove
        OnTouchMove onTouchMove=new OnTouchMove(context,m);
        //把onTouchMove添加进来 宽度为屏幕的1/3 高度为屏幕的1/2
        addView(onTouchMove,my.w/3,my.h/2);
        //设置onTouchMove的位置
        onTouchMove.setX(0);
        onTouchMove.setY(my.h/2);

        //实例化一个OnTouchMove
        OnTouchMove onTouchMove2=new OnTouchMove(context,m2);
        //把onTouchMove添加进来 宽度为屏幕的1/3 高度为屏幕的1/2
        addView(onTouchMove2, (int)(my.w*(1-0.65f)), (int)(my.h*0.29));
//        addView(onTouchMove2,my.w/3,my.h/2);
        //设置onTouchMove的位置
        onTouchMove2.setX(my.w*0.63f);
        onTouchMove2.setY(my.h*0.13f);

        //添加技能摇杆监听
        OnTouchSkill onTouchSkill=new OnTouchSkill(context,A,Q,W,E);//后添加的优先级高
        addView(onTouchSkill);
        onTouchSkill.setX(my.w*0.63f);
        onTouchSkill.setY(my.h*0.52f);
        new Thread(this).start();//启动重绘线程
    }

    @Override
    protected void onDraw(Canvas g) {//重写onDraw方法
        super.onDraw(g);

        // **** 画背景 **** //
        my.re.left=0;
        my.re.top=0;
        my.re.right=my.w;
        my.re.bottom=my.h;
        g.drawBitmap(my.imgBackground,null,my.re,p);

        m.onDraw(g,p);//画移动摇杆
        m2.onDraw(g,p);//画移动摇杆

        //画技能
        A.onDraw(g,p);
        Q.onDraw(g,p);
        W.onDraw(g,p);
        E.onDraw(g,p);
    }

    @Override
    public void run() { //每隔20毫秒刷新一次画布
        while(true){
            try {Thread.sleep(20);} catch (InterruptedException e) {e.printStackTrace();}
            //计算冷却时间
            A.next();
            Q.next();
            W.next();
            E.next();
            //释放技能
            if (my.skill != null) {
                my.skill.down_main();//教程用不到该方法
                my.skill.move_main();//教程用不到该方法
                if (my.skill.down == false) {
                    my.skill.up();
                    my.skill = null;
                }
            }

            postInvalidate();//重绘 在子线程重绘不能调用Invalidate()方法
        }
    }
}

////1.  创建一个DatagramSocket对象
//DatagramSocket socket = new  DatagramSocket (4567);
////2.  创建一个 InetAddress ， 相当于是地址
//InetAddress serverAddress = InetAddress.getByName("想要发送到的那个IP地址");
////3.  这是随意发送一个数据
//String str = "hello";
////4.  转为byte类型
//byte data[] = str.getBytes();
////5.  创建一个DatagramPacket 对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
//DatagramPacket  package = new DatagramPacket (data , data.length , serverAddress , 4567);
////6.  调用DatagramSocket对象的send方法 发送数据
//socket . send(package);
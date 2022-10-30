package com.yaogan.liziguo.yaogan;

import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;

import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    static Context context;
    private CommunicateWordsWithUDP transformWords  ;
    private EditText editIPAddr;
    private String ip_des;
//    private String action_buffer = "123456789";
    static private String action_buffer = "";

//    static public Move m =new Move();//移动摇杆

    private TextView msg;
    private Handler handler = new Handler();

    public String getPrintData(Move pm) {
        String str_test;
        double angle = pm.getAngle();
        double distance = pm.getDistance();
        double deltaX = distance * Math.sin(angle/180*Math.PI);
        str_test = "angle: " + Double.toString(angle) + ", " ;
        str_test = str_test + "distance: " + Double.toString(distance) + ", ";
        str_test = str_test + "deltaX: " + Double.toString(deltaX) + "\n";
        return str_test;
    }

    public byte[] getUdpbytesOfControlData(){ //将控制界面的输入数据以一定格式封装到数据帧中
        double angle = Hua.m.getAngle();
        double distance = Hua.m.getDistance();
        double w_spin = Hua.m2.getDistance() * Math.sin(Hua.m2.getAngle()/180*Math.PI);
        double action_id = 0;
        if (action_buffer.length()>0) {
            action_id = action_buffer.charAt(0) - '0';
            action_buffer = action_buffer.substring(1);
        }
        byte[] buffer = new byte[20];
        int posi = 0;
        buffer[posi] = (byte)(254); posi = posi + 1;
        buffer[posi] = (byte)(int)((angle % 360 + 180) / 360 * 200); posi = posi + 1;
        buffer[posi] = (byte)(int)(distance*100); posi = posi + 1;
        buffer[posi] = (byte)(int)(w_spin*100+100); posi = posi + 1;
        buffer[posi] = (byte)(int)(action_id); posi = posi + 1;
        buffer[posi] = (byte)(255); posi = posi + 1;

        byte[] bufferReturn = new byte[posi];
        for(int i=0; i<posi; i++)
            bufferReturn[i] = buffer[i];
        return bufferReturn;
    }
    static public void addActionToBuffer(int action_id){
        String id = String.valueOf(action_id % 10);
        action_buffer = action_buffer + id;
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            this.update();
            handler.postDelayed(this, 500);// 设置间隔多少毫秒
        }

        void update() {
            //定时执行以下代码
//            ToastUtils toastUtils = new ToastUtils();
//            toastUtils.showShortToast("123");

            // print
//            String print_str = getPrintData(Hua.m);
//            String print_str = getPrintData(Hua.m2);
//            final byte[] sendContent = print_str.getBytes();

            /* send udp data */
            final byte[] sendContent = getUdpbytesOfControlData();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    transformWords.sendDataWithUDPSocket(sendContent, ip_des);
                }
            }).start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = MainActivity.this;

        my.main = this;
        getSupportActionBar().hide();//隐藏标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏 隐藏状态栏
        //判断当前是否横屏 如果不是就设为横屏，设为横屏之后会自动调用onCreate方法
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //获取屏幕的宽高
            DisplayMetrics dis = getResources().getDisplayMetrics();
            my.w = dis.widthPixels;
            my.h = dis.heightPixels;
            //获取屏幕分辨率和1920*1080的比例 以便适应不同大小的屏幕
            my.bili = (float) (Math.sqrt(my.w * my.h) / Math.sqrt(1920 * 1080));
            //加载图片
            my.border = BitmapFactory.decodeResource(my.main.getResources(), R.mipmap.border);
            my.cancel = BitmapFactory.decodeResource(my.main.getResources(), R.mipmap.cancel);
            my.down = BitmapFactory.decodeResource(my.main.getResources(), R.mipmap.down);
            my.yaogan = BitmapFactory.decodeResource(my.main.getResources(), R.mipmap.yaogan);
            my.cd = BitmapFactory.decodeResource(my.main.getResources(), R.mipmap.cd);
            my.imgBackground = BitmapFactory.decodeResource(my.main.getResources(), R.mipmap.backimg);
            setContentView(new Hua(this));

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 横屏
        }

        showInputIPDialog();

//        setContentView(R.layout.activity_main);
//        msg = (TextView) findViewById(R.id.msgtxt);
//        msg.setText("你好啊！");

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            handler.postDelayed(runnable, 1000 * 1);
//            Log.e(Tag, "onWindowFocusChanged:" + "true");
        } else {
//            Log.e(Tag, "onWindowFocusChanged:" + "false");
        }
    }

    /**
     * 显示弹框请求用户输入连接对方的IP地址
     */
    private void showInputIPDialog() {
        editIPAddr=new EditText(MainActivity.this);
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setView(editIPAddr);
        builder.setTitle("输入对方IP地址");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(false);
        final AlertDialog dialog=builder.create();
        dialog.show();
        editIPAddr.setText("127.0.0.1");
        if(dialog.getButton(AlertDialog.BUTTON_POSITIVE)!=null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ip_des = editIPAddr.getText().toString();
                    String ip = "192.168.137.1";

//                    ip_des = ip;

                    if(!TextUtils.isEmpty(ip)){
                        //正则表达式判断用户输入的IP地址是否合法
                        String REGEX = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))";
                        Pattern pattern = Pattern.compile(REGEX);
                        Matcher matcher = pattern.matcher(ip);
                        boolean b=matcher.matches();
                        if(b){
                            try {
                                transformWords=new CommunicateWordsWithUDP(ip_des);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String sendContent = "hello world\n";
                                        transformWords.sendDataWithUDPSocket(sendContent);
                                    }
                                }).start();
//                                new Thread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        transformWords.ServerReceviedByUdp();
//                                    }
//                                }).start();
                                Toast.makeText(MainActivity.this,"连接成功～",Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this,"目标ip地址:" + ip_des,Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            } catch (SocketException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this,"非法的ip地址！",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(MainActivity.this,"非法的ip地址！",Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(MainActivity.this,"ip地址不能为空！",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(dialog.getButton(AlertDialog.BUTTON_NEGATIVE)!=null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editIPAddr.setText("");
                }
            });
        }
    }

    /**
     * onDestroy()方法记得关闭socket连接，避免端口持续占用
     * 解注册EventBus
     */
    @Override
    protected void onDestroy(){
        handler.removeCallbacks(runnable); //停止刷新

//        transformWords.disconnect();
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}

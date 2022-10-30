package com.yaogan.liziguo.yaogan;


//import com.alanjet.communicationwithudp.event.WordsEvent;

//import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by moshiyu on 17-11-22.
 * 这是核心的通信类
 */

public class CommunicateWordsWithUDP {
    /**
     * 使用DatagramSocket进行基于UDP的Socket通信
     */
    private DatagramSocket socket = new DatagramSocket(1985);;
    private String ipAddi;


    /**
     * 对方的ip地址在构造方法中传入
     * @param ipAdd
     * @throws SocketException
     */
    public CommunicateWordsWithUDP(String ipAdd) throws SocketException {
//        ToastUtils toastUtils = new ToastUtils();
//        toastUtils.showShortToast("ip地址:" + ipAdd);

        this.ipAddi =ipAdd;

//        String ip = "192.168.238.1";
////        try {
////            this.transformWords = new CommunicateWordsWithUDP(ip);
////            transformWords.sendDataWithUDPSocket("hello world");
////
////        } catch (SocketException e) {
////            e.printStackTrace();
////            ToastUtils toastUtils = new ToastUtils();
////            toastUtils.showShortToast("非法的ip地址！");
//////                        Toast.makeText(MainActivity.this, "非法的ip地址！",Toast.LENGTH_SHORT).show();
////        }

////        DatagramSocket(int port, InetAddress laddr)
//        try {
//            InetAddress serverAddress = InetAddress.getByName(ipAddi);
//            socket= new DatagramSocket(1985);
////            socket= new DatagramSocket(1985, serverAddress);
////            byte data[] = str.getBytes();
////            DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,8000);
////            socket.send(packet);
////        } catch (SocketException e) {
////            e.printStackTrace();
//        } catch (UnknownHostException e) {
////            ToastUtils toastUtils = new ToastUtils();
//            toastUtils.showShortToast("UnknownHostException!");
//            e.printStackTrace();
//        } catch (IOException e) {
//            toastUtils.showShortToast("IOException！");
//            e.printStackTrace();
//        }

    }

    public void sendDataWithUDPSocket(String str, String ip, int des_port) {
        try {
            InetAddress serverAddress = InetAddress.getByName(ip);
            byte data[] = str.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,des_port);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendDataWithUDPSocket(String str, String ip) {
        try {
            InetAddress serverAddress = InetAddress.getByName(ip);
            byte data[] = str.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,8000);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendDataWithUDPSocket(String str) {
        try {
            InetAddress serverAddress = InetAddress.getByName(ipAddi);
            byte data[] = str.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,8000);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendDataWithUDPSocket(byte[] data) {
        try {
            InetAddress serverAddress = InetAddress.getByName(ipAddi);
            DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,8000);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendDataWithUDPSocket(byte[] data, String ip) {
        try {
            InetAddress serverAddress = InetAddress.getByName(ip);
            DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,8000);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端接收服务端返回的数据(保留方法，方便后期扩展)
     */
//    public void ReceiveServerSocketDataReceiveServerSocketData() {
//        DatagramSocket socket;
//        try {
//            socket = new DatagramSocket(1985);
//            byte data[] = new byte[4 * 1024];
//            DatagramPacket packet = new DatagramPacket(data, data.length);
//            socket.receive(packet);
//            String result = new String(packet.getData(), packet.getOffset(),
//                    packet.getLength());
//            socket.close();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 接收消息的方法
     */
//    public void ServerReceviedByUdp(){
//        DatagramSocket socket;
//        try {
//            socket = new DatagramSocket(8000);
//            while (true){
//                byte data[] = new byte[4*1024];
//                DatagramPacket packet = new DatagramPacket(data,data.length);
//                socket.receive(packet);
//                String result = new String(packet.getData(),packet.getOffset() ,packet.getLength());
//                if(!TextUtils.isEmpty(result)){
//                    WordsEvent wordsEvent=new WordsEvent(result);
//                    EventBus.getDefault().post(wordsEvent);
//
//                }
//                System.out.println("收到信息为："+result);
//            }
//
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void disconnect(){
        socket.close();
        socket.disconnect();
    }
}

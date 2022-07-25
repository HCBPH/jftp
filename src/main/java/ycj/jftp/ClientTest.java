package ycj.jftp;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.DefaultFileRegion;
import ycj.jftp.client.JFTPClient;
import ycj.jftp.common.CommandParam;
import ycj.jftp.common.Flag;
import ycj.jftp.ftp.JFTP;
import ycj.jftp.pojo.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Scanner;

/**
 * @author 53059
 * @date 2021/12/10 12:39
 */
public class ClientTest {
    public static void main(String[] args) throws InterruptedException, IOException {

        Scanner scanner = new Scanner(System.in);

        String addr = "";

        int flag = 0;
        if(args.length != 1){
            flag = 1;
        }else if(args[0].split("\\.").length != 4 || !args[0].equals("localhost")){
            flag = 1;
        }else{
            addr = args[0];
        }

        if (flag == 1){
            System.out.print("请输入有效服务端IP地址:");
            while (scanner.hasNext()){
                String s = scanner.nextLine();
                if (s.split("\\.").length == 4 || s.equals("localhost")){
                    addr = s;
                    break;
                }
            }
        }


        JFTPClient client = JFTPClient.getInstance(addr);
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.run();
            }
        }).start();

        Thread.sleep(2500);

        while(client.isConnected()){
            Thread.sleep(500);
            System.out.print("["+JFTP.getAbsolutePath()+"]"+":");
            if (scanner.hasNext()){
                String s = scanner.nextLine();
                if (s.equals("q")){
                    return;
                }
                Command comm = JFTP.parse(s);
                if(comm == null){
                    continue;
                }

                client.getChannel().writeAndFlush(comm).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()){
//                            System.out.println("发送成功");
                        }
                    }
                }).sync();

                if (comm.getType() == CommandParam.TYPE_PUSH){
                    RandomAccessFile randomAccessFile = new RandomAccessFile(comm.getParams().split(" ")[0], "rw");
                    FileChannel channel = randomAccessFile.getChannel();
                    Flag.START_TIME = System.currentTimeMillis();
                    client.getChannel().writeAndFlush(new DefaultFileRegion(channel, 0, channel.size())).addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if (channelFuture.isSuccess()){
                                System.out.println("文件上传成功"+Flag.URI+",耗时:"+(System.currentTimeMillis()-Flag.START_TIME)/1000+"s");
                                Flag.START_TIME = 0;
                            }
                        }
                    }).sync();
                    channel.close();
                    randomAccessFile.close();
                }
            }
        }
    }
}

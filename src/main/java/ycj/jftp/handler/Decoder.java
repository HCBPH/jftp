package ycj.jftp.handler;

import com.sun.corba.se.impl.presentation.rmi.StubFactoryDynamicBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import ycj.jftp.common.CommandParam;
import ycj.jftp.common.Flag;
import ycj.jftp.common.MessageType;
import ycj.jftp.pojo.Command;
import ycj.jftp.pojo.Request;
import ycj.jftp.pojo.Response;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


/**
 * @author 53059
 * @date 2021/12/10 15:13
 */
public class Decoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        try{
            int type = 0;
            int length = 0;
            if (Flag.READING_FILE == 1){
                type = Flag.FILE_TYPE;
                length = Flag.FILE_LENGTH;
            }else{
                // 通过获取in当中的首部字段来解析传送数据的类型
                type = in.readInt();
                length = in.readInt();
            }

            switch (type){
                case MessageType.REQUEST:
                    byte[] buf = new byte[length];
                    in.readBytes(buf);
                    ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                    Request request = (Request) inputStream.readObject();
                    out.add(request);
                    inputStream.close();
                    break;
                case MessageType.RESPONSE:
                    byte[] buf1 = new byte[length];
                    in.readBytes(buf1);
                    ObjectInputStream inputStream1 = new ObjectInputStream(new ByteArrayInputStream(buf1));
                    Response response = (Response) inputStream1.readObject();
                    out.add(response);
                    inputStream1.close();
                    break;
                case MessageType.COMMAND:
                    byte[] buf2 = new byte[length];
                    in.readBytes(buf2);
                    ObjectInputStream inputStream2 = new ObjectInputStream(new ByteArrayInputStream(buf2));
                    Command command = (Command) inputStream2.readObject();
                    out.add(command);
                    inputStream2.close();
                    break;
                case MessageType.FILE:
                    System.out.println("正在下载文件………………");
                    int readableBytes = in.readableBytes();
                    int readerIndex = in.readerIndex();
                    RandomAccessFile r;
                    if (!Flag.URI.isEmpty()){
                        r = new RandomAccessFile(Flag.URI, "rw");
                    }else{
                        r = new RandomAccessFile(System.getProperty("user.dir")+"\\"+"tmp", "rw");
                    }
                    if (Flag.READING_FILE == 1){
                        byte[] b = new byte[readableBytes];
                        in.readBytes(b, 0, readableBytes);
                        r.seek(Flag.POSITION);
                        r.write(b);
                        Flag.POSITION += readableBytes;
                    }else{
                        Flag.READING_FILE = 1;
                        Flag.FILE_TYPE = MessageType.FILE;
                        Flag.FILE_LENGTH = length;
                        Flag.POSITION = 0;
                        Flag.START_TIME = System.currentTimeMillis();

                        if (in.readableBytes() > 0){
                            in.readBytes(r.getChannel(), 0, readableBytes);
                            Flag.POSITION += readableBytes;
                        }
                    }

                    if (Flag.POSITION >= Flag.FILE_LENGTH-1){
                        System.out.println("下载完成:"+Flag.URI+",耗时:"+(System.currentTimeMillis()-Flag.START_TIME)/1000+"s");
                        Flag.READING_FILE = 0;
                        Flag.FILE_LENGTH = -1;
                        Flag.FILE_TYPE = -1;
                        Flag.POSITION = -1;
                        Flag.URI = "";
                        Flag.START_TIME = 0;
                    }
                    r.close();
                    break;
                default:
                    System.out.println("Decoder:解析错误");
                    break;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

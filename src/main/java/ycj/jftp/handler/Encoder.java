package ycj.jftp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.MessageToByteEncoder;
import ycj.jftp.common.MessageType;
import ycj.jftp.pojo.Command;
import ycj.jftp.pojo.Request;
import ycj.jftp.pojo.Response;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author 53059
 * @date 2021/12/10 15:13
 */
public class Encoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        System.out.println("encoder");
        int flag = 0;

        if (o instanceof Request){
            byteBuf.writeInt(MessageType.REQUEST);
        }else if(o instanceof Response){
            byteBuf.writeInt(MessageType.RESPONSE);
        }else if (o instanceof Command){
            byteBuf.writeInt(MessageType.COMMAND);
        }else if(o instanceof DefaultFileRegion) {
            System.out.println("DefaultFileRegion");
        }else {
            System.out.println("Encoder:解析错误");
            flag = 1;
        }

        if(flag == 0){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(o);
            byteBuf.writeInt(outputStream.size());
            byteBuf.writeBytes(outputStream.toByteArray());
            objectOutputStream.close();
            outputStream.close();
        }
    }
}

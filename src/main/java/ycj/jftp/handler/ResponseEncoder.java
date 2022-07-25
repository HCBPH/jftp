package ycj.jftp.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
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
public class ResponseEncoder extends MessageToByteEncoder<Response> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Response o, ByteBuf byteBuf) throws Exception {

        byteBuf.writeInt(MessageType.RESPONSE);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(o);
        byteBuf.writeInt(outputStream.size());
        byteBuf.writeBytes(outputStream.toByteArray());
        objectOutputStream.close();
        outputStream.close();
    }
}

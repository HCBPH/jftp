package ycj.jftp.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import ycj.jftp.common.CommandParam;
import ycj.jftp.common.Flag;
import ycj.jftp.common.MessageType;
import ycj.jftp.common.ResponseParam;
import ycj.jftp.pojo.Command;
import ycj.jftp.pojo.Response;
import ycj.jftp.tool.FileFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author 53059
 * @date 2021/12/10 17:00
 */
public class CommandHandler extends SimpleChannelInboundHandler<Command> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Command msg) throws Exception {
        String remoteAddr = ctx.channel().remoteAddress().toString();
        String params = msg.getParams();

        Response response = new Response();

        switch (msg.getType()){
            case CommandParam.TYPE_LIST:
                File file = new File(params);
                if(file.exists()){
                    response.setCode(ResponseParam.SUCCESSFUL);
                    response.setReason(FileFormat.listFormat(Objects.requireNonNull(file.listFiles())));
                }else{
                    response.setCode(ResponseParam.FAILED);
                    response.setReason("文件目录不存在");
                }
                ctx.writeAndFlush(response);
                break;
            case CommandParam.TYPE_CD:
                break;
            case CommandParam.TYPE_PULL:
                String from = params.split(" ")[0];
                String to = params.split(" ")[1];
                File file_from = new File(from);
                System.out.println(remoteAddr+":获取"+from);
                if (file_from.exists()){
                    if (file_from.isFile()){
                        FileChannel channel = new RandomAccessFile(file_from, "r").getChannel();
                        ctx.write(Unpooled.buffer().writeInt(MessageType.FILE)
                                .writeInt((int) channel.size()));
                        ctx.writeAndFlush(new DefaultFileRegion(channel, 0, channel.size())).sync();
                        channel.close();
                    }else{
                        response.setCode(ResponseParam.FAILED);
                        response.setReason("目标不能是文件夹");
                        ctx.writeAndFlush(response);
                    }
                }else{
                    response.setCode(ResponseParam.FAILED);
                    response.setReason("文件不存在");
                    ctx.writeAndFlush(response);
                }
                break;
            case CommandParam.TYPE_PUSH:
                String guest_file = params.split(" ")[0];
                String host_file = params.split(" ")[1];
                int size = Integer.parseInt(params.split(" ")[2]);
                Flag.FILE_LENGTH = size;
                Flag.FILE_TYPE = MessageType.FILE;
                Flag.URI = host_file;
                Flag.READING_FILE = 1;
                Flag.POSITION = 0;
                System.out.println("准备上传文件中………………");
                break;
            case CommandParam.TYPE_CREATE:
                break;
            case CommandParam.TYPE_DELETE:
                break;
            case CommandParam.TYPE_MOVE:
                break;
            case CommandParam.TYPE_COPY:
                break;
            case CommandParam.TYPE_PRINT:
                System.out.println(remoteAddr+":"+params);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("thi is a test", StandardCharsets.UTF_8));
                break;
        }

    }
}

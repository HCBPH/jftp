package ycj.jftp.ftp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;
import ycj.jftp.common.CommandParam;
import ycj.jftp.common.Flag;
import ycj.jftp.exception.ArgsException;
import ycj.jftp.pojo.Command;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 53059
 * @date 2021/12/10 14:39
 */
public class JFTP {

    public static String ABSOLUTE_PATH = "D:\\";
//    public static String OS_TYPE = System.getProperty("os.name").toLowerCase(Locale.ROOT);

    public static Command parse(String command){

        String[] args = command
                .replace("/", "\\")
                .split(" ");

        switch (args[0].toLowerCase(Locale.ROOT)){
            case "list":
                if(args.length == 1){
                    return list(ABSOLUTE_PATH);
                }else if (args.length != 2){
                    System.out.println("参数解析错误:"+command);
                    return null;
                }
                if (isAbsolutePath(args[1])){//绝对路径
                    return list(args[1]);
                }else{
                    return list(ABSOLUTE_PATH+removeDot(args[1]));
                }

            case "cd":
                if(args.length != 2){
                    System.out.println("参数解析错误:"+command);
                }
                if (args[1].equals("....")){
                    String[] uris = ABSOLUTE_PATH.split("\\\\");
                    args[1] = "..";
                    if (uris.length>=3){
                        cd(String.valueOf(StringUtil.join("\\", Arrays.asList(uris).subList(0, uris.length - 1))));
                    }else{
                        cd(ABSOLUTE_PATH);
                    }
                }
                if (args[1].equals("..")){
                    String[] uris = ABSOLUTE_PATH.split("\\\\");
                    if (uris.length>=2){
                        return cd(String.valueOf(StringUtil.join("\\", Arrays.asList(uris).subList(0, uris.length - 1))));
                    }else{
                        return cd(ABSOLUTE_PATH);
                    }
                }
                if (isAbsolutePath(args[1])){//绝对路径
                    return cd(args[1]);
                }else{
                    return cd(ABSOLUTE_PATH+removeDot(args[1]));
                }

            case "pull":
                String from = "";
                String to = "";
                if(args.length == 2){
                    from = args[1];
                    to = System.getProperty("user.dir")+"\\tmp";
                }else if (args.length == 3){
                    from = args[1];
                    to = args[2];
                }else{
                    System.out.println("参数解析错误:"+command);
                    return null;
                }
                if (!isAbsolutePath(from)){
                    from = ABSOLUTE_PATH + from;
                }
                if (!isAbsolutePath(to)){
                    to = System.getProperty("user.dir") +"\\" + to;
                }
                Flag.URI = to;
                return pull(from, to);
            case "push":
                String local_file = "";
                String remote_uri = "";
                if (args.length == 3){
                    local_file = args[1];
                    remote_uri = args[2];
                }else{
                    System.out.println("参数解析错误:"+command);
                    return null;
                }
                if (!isAbsolutePath(local_file)){
                    local_file = System.getProperty("user.dir") + "\\" + local_file;
                }
                if (!isAbsolutePath(remote_uri)){
                    remote_uri = ABSOLUTE_PATH + remote_uri;
                }
                File file = new File(local_file);
                if (!file.isFile()){
                    System.out.println("文件不存在|该文件不可以是目录:"+local_file);
                    return null;
                }
                long size = file.length();
                return push(local_file, remote_uri, size);

            case "create":
                break;

            case "delete":
                break;

            case "move":
                break;

            case "copy":
                break;

            case "print":
                return print(args);
            default:
                System.out.println("参数解析错误:"+command);
                return null;
        }
        return null;
    }


    public static String getAbsolutePath(){
        return ABSOLUTE_PATH;
    }

    private static boolean isAbsolutePath(String uri) {
        String dst = uri.toLowerCase(Locale.ROOT);
        Pattern pattern = Pattern.compile("^[a-z]:(?:/|\\\\)");
        Matcher matcher = pattern.matcher(dst);
        return matcher.find();
    }

    private static String removeDot(String s){
        if (s.startsWith(".")){
            return s.substring(1, s.length());
        }
        return s;
    }

    private static void login(){
        //TODO:用户向server发送用户名和密码，如果成功，那么返回一段cookie，用户的Command都要带有cookie server才会接受。
    }

    private static Command print(String[] args){
        List<String> s = Arrays.asList(args.clone()).subList(1, args.length);
        Command comm = new Command();
        comm.setType(CommandParam.TYPE_PRINT);
        comm.setParams(String.valueOf(StringUtil.join(" ", s)));
        return comm;
    }

    private static Command cd(String dir){
        if (dir.endsWith("/")){
            dir = dir.substring(0, dir.length()-1);
        }
        if (!dir.endsWith("\\")){
            dir += "\\";
        }
        ABSOLUTE_PATH = dir;
        return null;
    }


    private static Command list(String dir){
        //TODO:netty发送一个Command，请求目录下的内容，如果有，将内容返回，没有就返回错误信息
        Command command = new Command();
        command.setType(CommandParam.TYPE_LIST);
        command.setParams(dir);
        return command;
    }

    private static Command pull(String from, String to){
        Command command = new Command();
        command.setType(CommandParam.TYPE_PULL);
        command.setParams(from + " " + to);
        return command;
    }

    private static Command push(String from, String to, long size){
        Command command = new Command();
        command.setType(CommandParam.TYPE_PUSH);
        command.setParams(from + " " + to + " " + size);
        return command;
    }


    /*
    * 暂时先不用实现
    * */
    private static void create(){

    }

    private static void delete(){

    }

    private static void move(){

    }

    private static void copy(){

    }
}

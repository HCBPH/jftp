package ycj.jftp.tool;

import javax.swing.text.Caret;
import java.io.File;

/**
 * @author 53059
 * @date 2021/12/11 16:10
 */
public class FileFormat {
    public static String listFormat(File[] files){
        String s = "";
        for(File f:files){
            if (f.isFile()){
                s += "file----";
            }else if(f.isDirectory()){
                s += "dir----";
            }else{
                System.out.println("FileFormat:解析错误");
            }
            s += (f.length() / (1024) + "KB" + "----" + f.getName() + "\n");
        }
        return s;
    }
}

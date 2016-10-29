package com.cui.Utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


/**
 * Created by jarvis on 2016/10/29.
 * 文件读写工具
 */
public class FileTool {
    static Logger logger = LoggerFactory.getLogger("FileTool.class");

    static String readFile(File file, String code) {
        String string = null;
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), code));
            String s = null;
            while ((s = bufferedReader.readLine()) != null) {
                string += s;
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            logger.error("读取文件失败！文件名：" + file);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("读取文件成功");
        return string;
    }

    public static boolean saveFile(String string, File filePath) {
        PrintWriter printWriter = null;
        if (string == null) {
            logger.error("待写入信息为空，请确认后再次写入");
            return false;
        }
        try {
            printWriter = new PrintWriter(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("写入文件出错！文件名:" + filePath);
        }
        printWriter.print(string);
        printWriter.close();
        logger.info("写入文件成功！文件名：" + filePath);
        return true;
    }

    public static void main(String[] args) {
        File file = new File("e:/799.html");
        File file1=new File("e:/798.html");
        String result=readFile(file, "gbk");
        System.out.println(result);
        saveFile("12345",file1);
    }
}

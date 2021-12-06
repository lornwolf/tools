package com.lornwolf.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

/**
 * 查找所有文件并替换指定内容。
 */
public class ReplaceAll {
    public static void main(String[] args) {

        // 文件所在路径。
        String root_path = "D:/input/";

        ArrayList<String> output_contents = new ArrayList<String>(); 

        // 查找内容。
        String search = "";
        // 替换内容。
        String replace = "";
        // 文件编码。
        String encoding = "Unicode";

        // 目标文件种类。
        String[] extensions = new String[1];
        extensions[0] = "xml";

        // ファイル内容読込む。
        LinkedList<File> files_list = (LinkedList<File>)FileUtils.listFiles(new File(root_path), extensions, true);

        try {
            for (File file : files_list ) {
                // 读入文件所有内容。
                ArrayList<String> file_content = (ArrayList<String>) FileUtils.readLines(file, encoding);
                // 逐行判断。
                for (int i = 0; i < file_content.size(); i++) {
                    String str_line = file_content.get(i);
                    if (str_line.indexOf(search) >= 0) {
                        System.out.println(file.getPath() + " : " + str_line);
                        output_contents.add(str_line.replace(search, replace));
                    } else {
                        output_contents.add(str_line);
                    }
                }
                // 写入文件。
                FileUtils.writeLines(file, encoding, output_contents);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

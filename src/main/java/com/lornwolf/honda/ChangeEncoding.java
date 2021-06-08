package com.lornwolf.honda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

/**
 * 批量转换文件编码。
 */
public class ChangeEncoding {
    public static void main(String[] args) {

        String root_path = "D:/DateBase";

        String[] extensions = new String[2];
        extensions[0] = "sql";
        extensions[1] = "bat";

        LinkedList<File> files_list = (LinkedList<File>)FileUtils.listFiles(new File(root_path), extensions, true);

        try {
            for(File file : files_list ) {
                ArrayList<String> file_content = (ArrayList<String>) FileUtils.readLines(file, "MS932");
                FileUtils.writeLines(file, "GB18030", file_content);
                System.out.println(file.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

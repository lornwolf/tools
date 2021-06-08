package com.lornwolf.honda;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

/**
 * 检查重复文件。
 */
public class FindDuplicateFiles {

    public static void main(String[] args) {
        
        // Rootパス
        String root_path = "C:/honda/workspace/hm-lot2/";
        
        String[] extensions = new String[1];
        extensions[0] = "java";

        LinkedList<File> files_list = (LinkedList<File>)FileUtils.listFiles(new File(root_path), extensions, true);
        
        HashMap<String, String> files_map = new HashMap<String, String>();
        for (File file : files_list) {
            if (files_map.get(file.getName()) != null) {
                System.out.println(file.getName() + "    " + file.getPath().replace(root_path, "").replace("\\", "/"));
                System.out.println(file.getName() + "    " + files_map.get(file.getName()).replace(root_path, "").replace("\\", "/"));
                System.out.println();
            }
            files_map.put(file.getName(), file.getPath());
        }
    }
}

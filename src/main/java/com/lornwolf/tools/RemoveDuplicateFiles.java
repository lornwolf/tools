package com.lornwolf.tools;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public class RemoveDuplicateFiles {

    public static void main(String[] args) {

        Collection<File> files = FileUtils.listFiles(new File("D:/SAP/"), null, true);
        for (File file : files) {
            // 获取文件名（不包含扩展名）。
            String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
            // 获取扩展名。
            String ext = file.getName().substring(file.getName().lastIndexOf("."));
            // 删除处理逻辑。
            if (fileName.endsWith("(1)")) {
                if (new File(file.getPath().substring(0, file.getPath().lastIndexOf(File.separator) + 1) + fileName.substring(0, fileName.length() - 3) + ext).exists()) {
                    int count = 0;
                    while(true) {
                        if (file.delete()) {
                            System.out.println(file.getAbsoluteFile());
                            break;
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            // 何もしない。
                        }
                        count ++;
                        if (count >= 10) {
                            System.out.println("delete file failed : " + file.getName());
                            break;
                        }
                    }
                }
            }
        }
    }
}

package com.lornwolf.tools;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public final class Rename {

    public static void main(String[] args) {
        rename02();
    }

    public static void rename01() {
        // 遍历目录下所有文件。
        Collection<File> files = FileUtils.listFiles(new File("D:/target"), null, true);
        for (File file : files) {
            // 获取文件名（包含扩展名）。
            String fileName = file.getName();
            // 改名处理。
            String number = fileName.substring(5, 8);
            file.renameTo(new File("D:/target/幽游白书." + number + ".mkv"));
        }
    }

    /**
     * SAP010000_202112120123.txt -> SAP010000.txt 
     */
    public static void rename02() {
        // 遍历目录下所有文件。
        Collection<File> files = FileUtils.listFiles(new File("C:/01_input/hulft"), null, true);
        for (File file : files) {
            // 获取文件名（去掉.txt扩展名）。
            String fileName = file.getName().replace(".txt", "");
            // 以下划线分割字符串。
            String[] name = fileName.split("_");
            file.renameTo(new File("C:/02_output/" + name[0] + ".txt"));
        }
    }
}

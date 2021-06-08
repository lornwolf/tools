package com.lornwolf.tools;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

public final class Rename {
    public static void main(String[] args) {
        // 遍历目录下所有文件。
        Collection<File> files = FileUtils.listFiles(new File("D:/target"), null, true);
        for (File file : files) {
            // 获取文件名（包含扩展名）。
            String fileName = file.getName();
            // 改名处理。
            String number = fileName.substring(5, 8);
            System.out.println("D:/target/幽游白书." + number + ".mkv");
            file.renameTo(new File("D:/target/幽游白书." + number + ".mkv"));
        }
    }
}

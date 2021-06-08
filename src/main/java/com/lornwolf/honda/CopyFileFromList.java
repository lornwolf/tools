package com.lornwolf.honda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.*;

/**
 * 入力ファイルのフォーマット：
 * 
 * /プロジェクトフォルダ/src/....../A.java
 * /プロジェクトフォルダ/webapp/....../B.jsp
 * /プロジェクトフォルダ/resources/....../C.xml
 */
public class CopyFileFromList {

    public static void main(String[] args) {

        // 入力パス
        String import_path = "C:/honda/workspace";

        // 出力パス
        String export_path = "D:/output";

        try {
            // ファイル内容読込む。
            ArrayList<String> file_contents = (ArrayList<String>)FileUtils.readLines(new File("D:\\input.txt"), "UTF-8");

            FileUtils.deleteDirectory(new File(export_path));

            int count = 0;

            for(String line_content : file_contents) {

                line_content = line_content.trim();

                System.out.println(line_content);

                String[] path_info_arr = line_content.split("/");

                /* プロジェクトフォルダを取り除く */
                // 例： /プロジェクトフォルダ/....../A.java → /....../A.java 
                StringBuffer save_path = new StringBuffer(); 
                for (int i=2; i<path_info_arr.length; i++) {
                    save_path.append("/");
                    save_path.append(path_info_arr[i]);
                }

                /* 出力パスにフォルダ階層を作る */
                int size = path_info_arr.length;
                String file_name = path_info_arr[size - 1];
                String path = (export_path + save_path.toString()).replace("/" + file_name, "");
                FileUtils.forceMkdir(new File(path));

                /* ファイルをコピーする */
                String src_file = import_path + line_content;
                String dest_file = export_path + save_path.toString();
                FileUtils.copyFile(new File(src_file), new File(dest_file));

                count = count + 1;
            }
            
            System.out.println("\n☆★☆ 読込んだファイル数： " + file_contents.size());
            System.out.println("\n☆★☆ 処理したファイル数： " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

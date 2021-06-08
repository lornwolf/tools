package com.lornwolf.honda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

public class GenerateEnvironments {

    public static void main(String[] args) {

        // 入力フォルダ。
        String input = "D:/input";
        // 出力フォルダ。
        String output = "D:/output";
        // 読込対象ファイルの拡張子情報。
        String[] extensions = new String[1];
        extensions[0] = "properties";
        // 全件のファイル情報を取得する。
        LinkedList<File> files_list = (LinkedList<File>) FileUtils.listFiles(new File(input), extensions, true);

        try {
            for (File file : files_list) {
                ArrayList<String> output_env = new ArrayList<String>(); 
                ArrayList<String> output_properties = new ArrayList<String>(); 

                ArrayList<String> file_content = (ArrayList<String>) FileUtils.readLines(file, "UTF-8");

                // ファイル内容を比較する。
                for (int i = 0; i < file_content.size(); i++) {

                    String str_line = file_content.get(i);
                    if (str_line.startsWith("#") || str_line.indexOf("=") < 0) {
                        output_properties.add(str_line);
                    } else {
                        String key = str_line.substring(0, str_line.indexOf("=")).trim();
                        String value = str_line.substring(str_line.indexOf("=") + 1).trim();
                        output_env.add("export " + key.replace(".", "_").replace("-", "_").toUpperCase() + "='" + value + "'");
                        // output_env.add("export " + key.replace(".", "_").replace("-", "_").toUpperCase() + "=" + value);
                        output_properties.add(key + "=${" + key.replace(".", "_").replace("-", "_").toUpperCase() + "}");
                    }
                }

                FileUtils.writeLines(new File(output + "/" + file.getName().substring(0, file.getName().indexOf("."))), "UTF-8", output_env);
                FileUtils.writeLines(new File(output + "/" + file.getName().substring(0, file.getName().indexOf(".")) + ".properties"), "UTF-8", output_properties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

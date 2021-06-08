package com.lornwolf.nimbus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

/**
 * 从日志中分析指定信息。
 */
public class Analysis {

    public static void main(String[] args) {

        // 入力フォルダ。
        String input = "C:\\Users\\songzongzheng\\Documents\\AccessLog";
        // 出力フォルダ。
        String output = "D:/output/result.txt";
        // 読込対象ファイルの拡張子情報。
        String[] extensions = new String[1];
        extensions[0] = "log";
        // 全件のファイル情報を取得する。
        LinkedList<File> files_list = (LinkedList<File>) FileUtils.listFiles(new File(input), extensions, true);

        try {
            ArrayList<String> output_contents = new ArrayList<String>(); 

            for (File file : files_list) {
                if (file.isDirectory() || !file.getName().startsWith("nimbus_mail_history")) {
                    continue;
                }

                ArrayList<String> file_content = (ArrayList<String>) FileUtils.readLines(file, "UTF-8");

                // ファイル内容を比較する。
                for (int i = 0; i < file_content.size(); i++) {

                    String str_line = file_content.get(i);

                    if (str_line.indexOf("送信失敗") >= 0) {
                        System.out.println(file.getPath() + " : " + str_line);
                        String line = file_content.get(i + 1).replace("送信者：", "") + "	" 
                            + file_content.get(i + 2).replace("時刻：", "")  + "	" 
                            + file_content.get(i + 3).replace("宛先：", "")  + "	" 
                            + file_content.get(i + 4).replace("件名：", "")  + "	" 
                            + file_content.get(i + 5);
                        output_contents.add(line);
                        i += 6;
                    }
                }
            }

            FileUtils.writeLines(new File(output), "UTF-8", output_contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

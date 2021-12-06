package com.lornwolf.bizsys;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 检查某表名有没有被使用。
 */
public class SearchForString {

    public static void main(String[] args) {

        // 入力ファイル
        String import_path = "C:/01_input/変更テーブル名一覧.txt";

        // 入力パス
        String root_path = "C:/01_input/home/";

        // 対象ファイル種類
        String[] extensions = new String[2];
        extensions[0] = "xfp";
        extensions[1] = "xfp2";

        ArrayList<String> result = new ArrayList<String>();

        // ファイル内容読込む。
        try {
            ArrayList<String> search_target_list = (ArrayList<String>) FileUtils.readLines(new File(import_path), Charset.forName("UTF-8"));
            ArrayList<File> files_list = (ArrayList<File>) FileUtils.listFiles(new File(root_path), extensions, true);

            System.out.println("検索対象のファイル数：" + files_list.size());

            for (String search_content : search_target_list) {

                search_content = search_content.trim();
                if (StringUtils.isEmpty(search_content)) {
                    continue;
                }

                for (File file : files_list) {
                    ArrayList<String> file_contents = (ArrayList<String>)FileUtils.readLines(file, "UTF-8");
                    for (String line_content : file_contents) {
                        line_content = line_content.trim();
                        if (line_content.toLowerCase().indexOf(search_content) >= 0) {
                            if (!result.contains(search_content)) {
                                result.add(search_content);
                            }
                            break;
                        }
                    }
                }
            }
            for (String line : result) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

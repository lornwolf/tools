package com.lornwolf.honda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * あるクラスの使用状況を調べる。
 */
public class SearchForString {

    public static void main(String[] args) {

        // 入力ファイル
        String import_path = "D:/input.txt";

        // 出力パス 
        String output_path = "D:/output/";

        // 入力パス
        String root_path = "C:/honda/workspace/hm-lot2/";

        // 対象ファイル種類
        String[] extensions = new String[2];
        extensions[0] = "java";
        extensions[1] = "jsp";

        // ファイル内容読込む。
        try {
            ArrayList<String> search_target_list = (ArrayList<String>)FileUtils.readLines(new File(import_path));
            LinkedList<File> files_list = (LinkedList<File>)FileUtils.listFiles(new File(root_path), extensions, true);

            ArrayList<String> output_contents = new ArrayList<String>();
            ArrayList<String> total_contents = new ArrayList<String>(); 

            System.out.println("検索対象のファイル数：" + files_list.size());

            for (String search_content : search_target_list) {

                System.out.println(search_content);

                search_content = search_content.trim();
                if (StringUtils.isEmpty(search_content)) {
                    continue;
                }

                output_contents.clear();
                output_contents.add(search_content);

                for (File file : files_list) {

                    boolean use_flag = false;

                    // ファイル名を出力リストに追加する。
                    output_contents.add("    " + file.getPath().replace(root_path, "").replace("\\", "/"));
                    ArrayList<String> file_contents = (ArrayList<String>)FileUtils.readLines(file, "UTF-8");

                    // 行番号
                    int line_number = 1;

                    for(String line_content : file_contents) {

                        line_content = line_content.trim();

                        /** チェック開始 **/
                        if (line_content.indexOf(search_content) >= 0) {
                            output_contents.add("        " + line_number + "行目：" + line_content);
                            use_flag = true;
                        } 

                        line_number = line_number + 1;
                        /** チェック終了 **/
                    }
                    
                    if(!use_flag) {
                        // 使っていない場合、ファイル名を出力リストから削除する。
                        output_contents.remove(output_contents.size() - 1);
                    }
                }

                /** 結果判断処理開始 **/
                if (output_contents.size()==1) {
                    output_contents.set(0, search_content + "（利用なし）");
                    FileUtils.writeLines(new File(output_path + search_content + "_（利用なし）.txt"), output_contents);
                } else{
                    FileUtils.writeLines(new File(output_path + search_content + ".txt"), output_contents);
                }
                
                total_contents.addAll(output_contents);
                /** 結果判断処理終了 **/
            }
            
            FileUtils.writeLines(new File("D:\\output\\まとめ.txt"), total_contents);

            System.out.println("処理終了。結果を「" + output_path + "」に出力した。");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.lornwolf.honda;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

public class CompareProject {

    public static void main(String[] args) {

        String project_01_path = "C:/honda/workspace/hm-lot2-ut2/";

        String project_02_path = "C:/honda/workspace/hm-lot3/";

        String project_01 = "ロット２";

        String project_02 = "ロット３";

        ArrayList<String> output_contents = new ArrayList<String>(); 

        // 対象ファイル種類
        String[] extensions = new String[5];
        extensions[0] = "java";
        extensions[1] = "js";
        extensions[2] = "jsp";
        extensions[3] = "xml";
        extensions[4] = "properties";
        
        // ファイル内容読込む。
        LinkedList<File> project_01_files_list = (LinkedList<File>)FileUtils.listFiles(new File(project_01_path), extensions, true);

        try {
            for(File file_01 :project_01_files_list ) {
    
                if(file_01.getPath().indexOf("\\.ractes\\")>0){
                    continue;
                }
    
                if(file_01.getPath().indexOf("\\ac\\")>0){
                    continue;
                }
    
                if(file_01.getPath().indexOf("\\entity\\")>0){
                    continue;
                }
                
                if(file_01.getPath().indexOf("\\WEB-INF\\classes\\")>0){
                    continue;
                }

                File file_02 = new File(project_02_path + file_01.getPath().replace("\\", "/").replace(project_01_path, ""));
                
                if(!file_02.exists()) {

                	String output_line = project_02 + "にない：" + file_02.getPath().replace("\\", "/").replace(project_01_path, "");

                    output_contents.add(output_line);
                    output_contents.add("");

                    System.out.print(output_line + "\n");
                    continue;
                }
    
                ArrayList file_content_01 = (ArrayList) FileUtils.readLines(file_01, "UTF-8");
                ArrayList file_content_02 = (ArrayList) FileUtils.readLines(file_02, "UTF-8");
                
                // ファイル内容を比較する。
                for(int i=0; i<file_content_01.size(); i++){
    
                    String str_line = file_01.getPath().replace("\\", "/").replace(project_01_path, "");
    
                    if(!file_content_01.get(i).equals(file_content_02.get(i))) {
    
                        System.out.println(str_line + " " + String.valueOf(i+1) + "行目\n");
                        System.out.println("    ※※ " + project_01 + "：" + file_content_01.get(i) + "\n");
                        System.out.println("    ※※ " + project_02 + "：" + file_content_02.get(i) + "\n");
    
                        output_contents.add(str_line + " " + String.valueOf(i+1) + "行目\n");
                        output_contents.add("    ※※ " + project_01 + "：" +  file_content_01.get(i));
                        output_contents.add("    ※※ " + project_02 + "：" +  file_content_02.get(i));
                        output_contents.add("");
                        break;
                    }
                }
    
            }
            FileUtils.writeLines(new File("D:/output.txt"), output_contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

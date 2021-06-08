package com.lornwolf.honda;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;

/**
 * すべてのファイルのBOMSを削除する。
 */
public class Utf8BomRemover {
    public static void main(String[] args) {

        // Rootパス
        String root_path = "D:/01";

        String[] extensions = new String[1];
        extensions[0] = "java";

        // 便利目录下所有文件（包含子目录）。
        LinkedList<File> files_list = (LinkedList<File>) FileUtils.listFiles(new File(root_path), extensions, true);

        try {
            for (File file : files_list) {
                byte[] bs = FileUtils.readFileToByteArray(file);
                if(bs[0]==-17 && bs[1]==-69 && bs[2]==-65) {
                    byte[] nbs = new byte[bs.length - 3];
                    System.arraycopy(bs, 3, nbs, 0, nbs.length);
                    FileUtils.writeByteArrayToFile(file, nbs);
                    System.out.println("Remove BOMS : " + file.getPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

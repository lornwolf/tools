package com.lornwolf.nimbus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.LinkedList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * 解析CSV文件。
 * 
 * 为了切换成gradle，改了CSV的部品，适当修改了一下代码使其能编译通过，但不保证能正确运行。
 * 2021/6/8
 */
public class GrepRetirementDate {

    public static void main(String[] args) {

        // 入力フォルダ。
        String input = "D:/input";
        // 出力フォルダ。
        String output = "D:/output";
        // 読込対象ファイルの拡張子情報。
        String[] extensions = new String[1];
        extensions[0] = "csv";
        // 全件のファイル情報を取得する。
        LinkedList<File> files_list = (LinkedList<File>) FileUtils.listFiles(new File(input), extensions, true);

        try {
            for (File file : files_list) {
                if (file.isDirectory()) {
                    continue;
                }
                FileInputStream inputStream = new FileInputStream(file);
                // CSVファイルを読み込む。
                InputStreamReader ireader=new InputStreamReader(inputStream, "UTF-8");
                CSVReader csvReader = new CSVReader(ireader);
                // 出力フォルダを決める。
                String path = output + file.getAbsolutePath().replace(input, "").replace(File.separator + file.getName(), "");
                // 出力フォルダを作成する。
                FileUtils.forceMkdir(new File(path));
                // CSVファイル出力用オブジェクトを生成する。
                FileOutputStream outputStream = new FileOutputStream(output + File.separator + file.getName());
                CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));
                String[] record = null;
                while ((record = csvReader.readNext()) != null) {
                    // フォーマットチェックをする。
                    if (record.length < 15) {
                        continue;
                    }
                    // 退職日をチェックする。
                    String retirementDate = record[13].trim();
                    if (StringUtils.equals(retirementDate, "20000101")) {
                        csvWriter.writeNext(record);
                    }
                }
                csvWriter.flush();
                csvWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

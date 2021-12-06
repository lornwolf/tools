package com.lornwolf.bizsys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lornwolf.common.ExcelUtil;
import com.opencsv.CSVReader;

/**
 * 比较现行和新环境的帐票出力内容。
 * 
 * 例：
 * input
 *   |_ 00000
 *       |_債権一覧（現）.csv
 *       |_債権一覧（新）.csv
 *       |_債務一覧（現）.csv
 *       |_債務一覧（新）.csv
 *       |_......
 *   |_ 00010
 *       |_債権一覧（現）.csv
 *       |_債権一覧（新）.csv
 *       |_債務一覧（現）.csv
 *       |_債務一覧（新）.csv
 *       |_......
 */
public class CompareCsv {

    public static void main(String[] args) {

        // 入力フォルダ。
        String input = "C:/01_入力/CSV";

        // サブフォルダ情報を取得する。
        String[] directories = new File(input).list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });

        try {
            for (int i = 0; i < directories.length; i++) {

                // 获取目录下所有文件的文件名。
                String[] files = new File(input + "/" + directories[i]).list(new FilenameFilter() {
                    @Override
                    public boolean accept(File current, String name) {
                        return new File(current, name).isFile();
                    }
                });

                for (int j = 0; j < files.length; j++) {
                    // 获取扩展名。
                    String ext = files[j].substring(files[j].lastIndexOf("."));

                    if (files[j].indexOf("（新）") < 0 || !ext.equalsIgnoreCase(".csv")) {
                        continue;
                    }

                    Map<String, List<String>> newCsv = new HashMap<String, List<String>>();
                    Map<String, List<String>> oldCsv = new HashMap<String, List<String>>();

                    // 新帳票ファイルを読み込む。
                    List<String> lines = Files.lines(Paths.get(input + "/" + directories[i] + "/" + files[j]), Charset.forName("MS932")).collect(Collectors.toList());
                    String[] newHeaders = lines.get(0).split(",");

                    for (int k = 1; k < lines.size(); k++) {
                        String[] line = lines.get(k).split("\",\"");
                        for (int m = 0; m < newHeaders.length; m++) {
                            ArrayList<String> list = (ArrayList<String>) newCsv.get(newHeaders[m]);
                            if (list == null) {
                                list = new ArrayList<String>();
                                newCsv.put(newHeaders[m], list);
                            }
                            list.add(line[m].replace("\"", ""));
                        }
                    }

                    // 現帳票ファイルを読み込む。
                    lines = Files.lines(Paths.get(input + "/" + directories[i] + "/" + files[j].replace("（新）", "（現）")), Charset.forName("MS932")).collect(Collectors.toList());
                    String[] oldHeaders = lines.get(0).split(",");

                    for (int k = 1; k < lines.size(); k++) {
                        String[] line = lines.get(k).split("\",\"");
                        for (int m = 0; m < oldHeaders.length; m++) {
                            ArrayList<String> list = (ArrayList<String>) oldCsv.get(oldHeaders[m]);
                            if (list == null) {
                                list = new ArrayList<String>();
                                oldCsv.put(oldHeaders[m], list);
                            }
                            list.add(line[m].replace("\"", ""));
                        }
                    }

                    // 比较结果。
                    boolean flag = true;
                    // 出错行数。
                    int number = 0;
                    // 开始比较。
                    for (int m = 0; m < newHeaders.length; m++) {
                        ArrayList<String> newList = (ArrayList<String>) newCsv.get(newHeaders[m]);
                        ArrayList<String> oldList = (ArrayList<String>) oldCsv.get(newHeaders[m]);
                        if (oldList == null) {
                            continue;
                        }
                        for (int n = 0; n < newList.size(); n++) {
                            if (!newList.get(n).equals(oldList.get(n))) {
                                flag = false;
                                number = n;
                                break;
                            }
                        }
                        if (!flag) {
                            ArrayList<String> outputContents = new ArrayList<String>();
                            outputContents.add("項目名：" + newHeaders[m]);
                            // 因为CSV文件有表头，所以行数加2。
                            outputContents.add("行：" + String.valueOf(number + 2));
                            outputContents.add("新：" + newList.get(number));
                            outputContents.add("旧：" + oldList.get(number));
                            FileUtils.writeLines(new File(input + "/" + directories[i] + "/" + files[j].replace("（新）", "（比較：NG）").replace(".csv", ".txt")), outputContents);
                            System.out.println("不一致：" + directories[i] + " " + files[j].replace("（新）", ""));
                            break;
                        }
                    }
                    if (flag) {
                        ArrayList<String> outputContents = new ArrayList<String>();
                        outputContents.add("比較結果：一致。");
                        FileUtils.writeLines(new File(input + "/" + directories[i] + "/" + files[j].replace("（新）", "（比較：OK）").replace(".csv", ".txt")), outputContents);
                    }

                    /* 以下处理在比较大文件时候效率极低，删掉。
                    Map<CellStyle, CellStyle> styles = new HashMap<CellStyle, CellStyle>();
                    try (XSSFWorkbook workbook = new XSSFWorkbook();
                         FileOutputStream out = new FileOutputStream(input + "/" + directories[i] + "/" + files[j].replace("（新）", "（比較）").replace(".csv", ".xlsx"));) {
    
                        XSSFSheet sheet = workbook.createSheet("比較結果");
                        // 出力表头。
                        for (int m = 0; m < newHeaders.length; m++) {
                            ExcelUtil.getCell(sheet, 0, m).setCellValue(newHeaders[m]);
                        }
                        // 出力比较结果。
                        for (int m = 0; m < newHeaders.length; m++) {
                            ArrayList<String> newList = (ArrayList<String>) newCsv.get(newHeaders[m]);
                            ArrayList<String> oldList = (ArrayList<String>) oldCsv.get(newHeaders[m]);
                            if (oldList == null) {
                                continue;
                            }
                            for (int n = 1; n < newList.size(); n++) {
                                if (newList.get(n).equals(oldList.get(n))) {
                                    ExcelUtil.getCell(sheet, n, m).setCellValue("TRUE");
                                } else {
                                    Cell cell = ExcelUtil.getCell(sheet, n, m);
                                    cell.setCellValue("FALSE");
                                    // 背景を黄色いにする。
                                    CellStyle style = styles.get(cell.getCellStyle());
                                    if (style == null) {
                                        style = workbook.createCellStyle();
                                        style.cloneStyleFrom(cell.getCellStyle());
                                        style.setFillForegroundColor(IndexedColors.RED.getIndex());
                                        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                        styles.put(cell.getCellStyle(), style);
                                    }
                                    cell.setCellStyle(style);
                                }
                            }
                        }
                        workbook.write(out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    */
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

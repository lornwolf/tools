package com.lornwolf.nimbus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lornwolf.common.ExcelUtil;

/**
 * 比较两个Excel格式的表数据。
 * 如果数据较多的话会很耗内存，需要调整虚拟机参数增大内存分配。
 */
public class CompareExcel {

    public static void main(String[] args) {

        // ファイル１
        String excel1 = "D:/input/導入前.xlsx";

        // ファイル２
        String excel2 = "D:/input/導入後.xlsx";

        // 出力パス 
        String outputPath = "D:/output/比較結果.xlsx";

        ArrayList<String> outputContents = new ArrayList<String>();

        // ファイル内容読込む。
        try {

            InputStream ips01 = new FileInputStream(excel1);
            XSSFWorkbook workbook01 = new XSSFWorkbook(ips01);
            XSSFSheet sheet01 = workbook01.getSheetAt(0);

            InputStream ips02 = new FileInputStream(excel2);
            XSSFWorkbook workbook02 = new XSSFWorkbook(ips02);
            XSSFSheet sheet02 = workbook02.getSheetAt(0);

            Map<CellStyle, CellStyle> styles = new HashMap<CellStyle, CellStyle>();

            for (int i = 2; i < 127336; i++) {
                String uid = ExcelUtil.getStr(ExcelUtil.getCell(sheet01, i, 1));
                List<String> diff = new ArrayList<String>();
                for (int j = 0; j < 18; j++) {
                    Cell cell = ExcelUtil.getCell(sheet02, i, j);
                    String str01 = ExcelUtil.getStr(ExcelUtil.getCell(sheet01, i, j));
                    String str02 = ExcelUtil.getStr(cell);
                    if (!StringUtils.equals(str01, str02)) {
                        String colName = ExcelUtil.getStr(ExcelUtil.getCell(sheet02, 1, j));
                        diff.add("(" + colName + ") " + str01 + " --> " + str02);
                        // 背景を黄色いにする。
                        CellStyle style = styles.get(cell.getCellStyle());
                        if (style == null) {
                            style = workbook02.createCellStyle();
                            style.cloneStyleFrom(cell.getCellStyle());
                            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            styles.put(cell.getCellStyle(), style);
                        }
                        cell.setCellStyle(style);

                        // コメントを設定する。
                        if (args.length > 0 && args[0].equals("-comment")) {
                            if (cell.getCellComment() == null) {
                                // コメントの枠の座標
                                final int dx1 = Units.EMU_PER_PIXEL * 10;
                                final int dy1 = Units.EMU_PER_PIXEL * 5;
                                final int dx2 = Units.EMU_PER_PIXEL * 10;
                                final int dy2 = Units.EMU_PER_PIXEL * 5;
                                // コメント左上の行
                                final int row1 = cell.getRowIndex();
                                // コメント左上の列
                                final int col1 = cell.getColumnIndex() + 1;
                                // コメント左下の行
                                final int col2 = col1 + 2;
                                // コメント左下の列
                                final int row2 = row1 + 4;
                                Drawing<?> draw = sheet02.createDrawingPatriarch();
                                Comment comment = draw.createCellComment(new XSSFClientAnchor(dx1, dy1, dx2, dy2, col1, row1, col2, row2));
                                comment.setString(new XSSFRichTextString(str01));
                                cell.setCellComment(comment);
                            }
                        }
                    }
                }
                if (diff.size() > 0) {
                    outputContents.add(uid);
                    System.out.println(uid);
                    for (String o : diff) {
                        outputContents.add("    " + o);
                        System.out.println("    " + o);
                    }
                }
            }

            FileOutputStream fileOut = new FileOutputStream(outputPath);
            workbook02.write(fileOut);

            workbook01.close();
            workbook02.close();

            FileUtils.writeLines(new File("D:/output/変更点.txt"), outputContents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

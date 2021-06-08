package com.lornwolf.bizsys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lornwolf.common.ExcelUtil;

/**
 * 检查两个版本外部IF定义书（Excel格式）。
 */
public class CheckIF {

	private String input201 = "C:\\01_input\\2.0.1_基本設計";
	private String input221 = "C:\\01_input\\2.2.1_基本設計";

	public static void main(String[] args) {
		new CheckIF();
	}

	public CheckIF() {
		fetchAllFiles(new File(input221));
	}

	private void fetchAllFiles(File dir) {
		File[] files = dir.listFiles();
		if (files.length > 0) {
			for (File file : files) {
				if (file.isFile() && file.getName().indexOf("外部I／F定義") >= 0) {

					String file201 = file.getPath().replace("2.2.1_基本設計", "2.0.1_基本設計");
					if (!new File(file201).exists()) {
						System.out.println("2.2.1 追加 : " + file.getPath());
					}
					check(file);
				}
				if (file.isDirectory()) {
					fetchAllFiles(file);
				}
			}
		}
	}

	private void check(File file) {
		try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file))) {
			if (workbook.getNumberOfSheets() > 1) {
				System.out.println("複数シート : " + file.getPath());
			}
			XSSFSheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return;
			}
			String usecase = null;
			int itemStartRow = -1;
			int itemStartCol = -1;
			String inout = null;
			String format = null;
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 35));
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 34));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 33));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 32));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 31));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 30));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 29));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 26));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 6));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 4));
			}
			if (usecase == null || usecase.length() == 0) {
				usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 39));
			}
			if (usecase == null || usecase.length() == 0) {
				System.out.println("ユースケース名 特定不能: " + file.getPath());
			}
			if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet, 5, 10)))) {
				itemStartRow = 6;
				itemStartCol = 10;
			} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet, 5, 9)))) {
				itemStartRow = 6;
				itemStartCol = 9;
			} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet, 7, 8)))) {
				itemStartRow = 8;
				itemStartCol = 8;
			} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet, 8, 4)))) {
				itemStartRow = 9;
				itemStartCol = 4;
			} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet, 5, 6)))) {
				itemStartRow = 6;
				itemStartCol = 6;
			} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet, 5, 7)))) {
				itemStartRow = 6;
				itemStartCol = 7;
			} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet, 5, 8)))) {
				itemStartRow = 6;
				itemStartCol = 8;
			}
			if (itemStartRow == -1 || itemStartCol == -1) {
				System.out.println("論理項目名 列特定不能: " + file.getPath());
			}
			if (ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow, itemStartCol)).length() == 0) {
				if (ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow + 1, itemStartCol)).length() > 0) {
					itemStartRow += 1;
				} else {
					System.out.println("論理項目名 値なし: " + file.getPath());
				}
			}
			inout = ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow, 1));
			if (inout == null || inout.length() == 0 || ("I".equals(inout) && "O".equals(inout))) {
				inout = ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow, 2));
			}
			if (inout == null || inout.length() == 0 || ("I".equals(inout) && "O".equals(inout))) {
				System.out.println("I/O 特定不能: " + file.getPath());
			}
			format = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 7, 4));
			if (format == null || format.length() == 0) {
				format = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 4, 12));
			}
			if (format == null || format.length() == 0) {
				format = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 4, 13));
			}
			if (format == null || format.length() == 0) {
				format = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 4, 14));
			}
			if (format == null || format.length() == 0) {
				format = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 4, 15));
			}
			if (format == null || format.length() == 0) {
				System.out.println("ファイル種類 特定不能: " + file.getPath());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

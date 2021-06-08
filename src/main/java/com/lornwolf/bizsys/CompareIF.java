package com.lornwolf.bizsys;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lornwolf.common.ExcelUtil;

/**
 * 比较两个版本外部IF定义书（Excel格式）并出力差分一览和差分明细。
 */
public class CompareIF {

	// 2.0.1版基本设计保存路径。
	private String input201 = "C:\\01_input\\2.0.1_基本設計";
	// 外部IF比较一览表的数据开始行（从0开始计数）。
	private int listStartRow = 4;
	// 外部IF比较一览表的Sheet全局对象。
	private XSSFSheet listSheet = null;

	public static void main(String[] args) {
		new CompareIF();
	}

	public CompareIF() {
		try (XSSFWorkbook result = new XSSFWorkbook(new FileInputStream(new File("C:/01_input/外部IF差分情報説明資料.xlsx")));
			FileOutputStream out = new FileOutputStream("C:/02_output/外部IF差分情報説明資料.xlsx");) {
			listSheet = result.getSheetAt(0);
			fetchAllFiles(new File(input201));
			result.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void fetchAllFiles(File dir) {
		File[] files = dir.listFiles();

		// 2.2.1版的相应目录下的所有外部IF设计书文件（用来比较新版中是否有新增的文件）。
		File[] files221 = null;

		// 2.2.1版「040_外部IF」目录下的所有目录（用来比较新版中是否有新增的目录）。
		File[] folders221 = null;
		if ("040_外部IF".equals(dir.getName())) {
			folders221 = new File(dir.getPath().replace("2.0.1_基本設計", "2.2.1_基本設計")).listFiles();
		}

		if (files.length > 0) {
			for (File file201 : files) {
				if (file201.isFile() && file201.getName().indexOf("外部I／F定義") >= 0) {

					System.out.println(file201.getPath().substring(input201.length() + 1));
					String file221 = file201.getPath().replace("2.0.1_基本設計", "2.2.1_基本設計");

					// 両方存在する。
					if (new File(file221).exists()) {
						compare(file201, new File(file221));
					}
					// 2.0.1側だけ存在する。
					else {
						String module = file201.getPath().substring(input201.length() + 1);
						module = module.substring(module.indexOf("_") + 1, module.indexOf("\\"));
						ExcelUtil.copyRows(listSheet, listStartRow, listStartRow, listStartRow + 1);
						ExcelUtil.setCellValue(listSheet, listStartRow, 0, listStartRow - 4);
						ExcelUtil.setCellValue(listSheet, listStartRow, 1, module);
						try {
							ExcelUtil.setCellValue(listSheet, listStartRow, 2, getUsecase(new XSSFWorkbook(new FileInputStream(file201)).getSheetAt(0)));
						} catch (Exception e) {}
						ExcelUtil.setCellValue(listSheet, listStartRow, 3, file201.getName().replace(".xlsx", ""));
						ExcelUtil.setCellValue(listSheet, listStartRow, 4, "〇");
						ExcelUtil.setCellValue(listSheet, listStartRow, 5, "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 11, "削除");
						listStartRow += 1;
					}
					// 2.2.1側だけ存在するファイルを取得する。
					if (files221 == null) {
						if (new File(file201.getParent().replace("2.0.1_基本設計", "2.2.1_基本設計")).exists()) {
							files221 = new File(file201.getParent().replace("2.0.1_基本設計", "2.2.1_基本設計")).listFiles();
						}
					}
				}
				if (file201.isDirectory()) {
					// 在输出目录中创建相应目录结构。
					if (file201.getPath().indexOf("040_外部IF") > 0) {
						String outputFolder = file201.getPath().replace("01_input", "02_output");
						if (!new File(outputFolder).exists()) {
							new File(outputFolder).mkdirs();
						}
					}
					// 递归调用，遍历所有文件夹和目录。
					fetchAllFiles(file201);
				}
			}
			// 只在2.2.1版中存在的文件（新增文件），输出到比较一览表中。
			if (files221 != null) {
				List<String> list201 = Arrays.asList(files).stream().map(o -> o.getName()).collect(Collectors.toList());
				List<String> list221 = Arrays.asList(files221).stream().map(o -> o.getName()).collect(Collectors.toList());
				for (String fileName : list221) {
					if (!list201.contains(fileName)) {
						XSSFSheet tmpSheet = null;
						try {
							tmpSheet = new XSSFWorkbook(new FileInputStream(dir.getPath().replace("2.0.1_基本設計", "2.2.1_基本設計") + "\\" + fileName)).getSheetAt(0);
						} catch (Exception e) {}
						String module = dir.getPath().substring(input201.length() + 1);
						module = module.substring(module.indexOf("_") + 1, module.indexOf("\\"));
						String format = getFormat(tmpSheet);
						String inout = getInOut(tmpSheet);
						ExcelUtil.copyRows(listSheet, listStartRow, listStartRow, listStartRow + 1);
						ExcelUtil.setCellValue(listSheet, listStartRow, 0, listStartRow - 4);
						ExcelUtil.setCellValue(listSheet, listStartRow, 1, module);
						try {
							ExcelUtil.setCellValue(listSheet, listStartRow, 2, getUsecase(tmpSheet));
						} catch (Exception e) {}
						ExcelUtil.setCellValue(listSheet, listStartRow, 3, fileName.replace(".xlsx", ""));
						ExcelUtil.setCellValue(listSheet, listStartRow, 4, "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 5, "〇");
						ExcelUtil.setCellValue(listSheet, listStartRow, 6, "I".equals(inout) ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 7, "O".equals(inout) ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 8, format.indexOf("CSV") >= 0 ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 9, format.indexOf("TSV") >= 0 ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 10, format.indexOf("EXCEL") >= 0 ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 11, "追加");
						listStartRow += 1;
					}
				}
			}
			// 只在2.2.1版中存在的目录（新增目录），将其中的文件信息输出到比较一览表中。
			if (folders221 != null) {
				for (int i = 0; i < folders221.length; i++) {
					if (new File(folders221[i].getPath().replace("2.2.1_基本設計", "2.0.1_基本設計")).exists()) {
						continue;
					}
					for (File f : folders221[i].listFiles()) {
						XSSFSheet tmpSheet = null;
						try {
							tmpSheet = new XSSFWorkbook(new FileInputStream(f.getPath())).getSheetAt(0);
						} catch (Exception e) {}
						String module = dir.getPath().substring(input201.length() + 1);
						module = module.substring(module.indexOf("_") + 1, module.indexOf("\\"));
						String format = getFormat(tmpSheet);
						String inout = getInOut(tmpSheet);
						ExcelUtil.copyRows(listSheet, listStartRow, listStartRow, listStartRow + 1);
						ExcelUtil.setCellValue(listSheet, listStartRow, 0, listStartRow - 4);
						ExcelUtil.setCellValue(listSheet, listStartRow, 1, module);
						ExcelUtil.setCellValue(listSheet, listStartRow, 2, getUsecase(tmpSheet));
						ExcelUtil.setCellValue(listSheet, listStartRow, 3, f.getName());
						ExcelUtil.setCellValue(listSheet, listStartRow, 4, "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 5, "〇");
						ExcelUtil.setCellValue(listSheet, listStartRow, 6, "I".equals(inout) ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 7, "O".equals(inout) ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 8, format.indexOf("CSV") >= 0 ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 9, format.indexOf("TSV") >= 0 ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 10, format.indexOf("EXCEL") >= 0 ? "〇" : "-");
						ExcelUtil.setCellValue(listSheet, listStartRow, 11, "追加");
						listStartRow += 1;
					}
				}
			}
		}
	}

	/**
	 * 比较两个外部IF定义设计书。
	 * 
	 * @param file201
	 * @param file221
	 */
	private void compare(File file201, File file221) {
		try (XSSFWorkbook workbook201 = new XSSFWorkbook(new FileInputStream(file201));
			XSSFWorkbook workbook221 = new XSSFWorkbook(new FileInputStream(file221));) {

			// モジュール名（会計コア、一般会計など）を取得する。
			String module = file201.getPath().substring(input201.length() + 1);
			module = module.substring(module.indexOf("_") + 1, module.indexOf("\\"));

			boolean same = true;
			String usecase = null;
			int itemStartRow = -1;
			int itemStartCol = -1;
			String inout = null;
			String format = null;

			// 一个外部IF定义文件中可能有多个Sheet页，所以要循环。
			for (int index = 0; index < workbook201.getNumberOfSheets(); index++) {
				// 比较一下Sheet名。
				String sheetName201 = workbook201.getSheetAt(index).getSheetName();
				if (!sheetName201.equals(workbook221.getSheetAt(index).getSheetName())) {
					System.out.println("·Sheet名不一致 " + file201.getPath());
				}
				// 检查一下Sheet名（有些Sheet不是IF定义）。
				if (sheetName201.equals("イメージ")) {
					continue;
				}
				// 读入模板文件。
				XSSFWorkbook outbook = new XSSFWorkbook(new FileInputStream(new File("C:/01_input/外部I／F定義.xlsx")));
				// 2.0.1のIF情報を読み込む。
				XSSFSheet sheet = workbook201.getSheetAt(index);
				if (sheet == null) {
					return;
				}
				itemStartRow = -1;
				itemStartCol = -1;
				usecase = getUsecase(sheet);
				if (usecase == null || usecase.length() == 0) {
					System.out.println("ユースケース名 特定不能: " + file201.getPath());
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
					System.out.println("論理項目名 列特定不能: " + file201.getPath());
					continue;
				}
				if (ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow, itemStartCol)).length() == 0) {
					// 有的表头占两行，所以多判断一次，日。
					if (ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow + 1, itemStartCol)).length() > 0) {
						itemStartRow += 1;
					} else {
						System.out.println("論理項目名 値なし: " + file201.getPath());
						continue;
					}
				}

				String fieldName = ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow, itemStartCol));
				List<String> list201 = new ArrayList<String>();
				while (fieldName.length() > 0) {
					list201.add(fieldName);
					itemStartRow += 1;
					fieldName = ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow, itemStartCol));
				}
	
				// 2.2.1のIF情報を読み込む。
				XSSFSheet sheet221 = workbook221.getSheetAt(index);
				if (sheet221 == null) {
					return;
				}
				usecase = null;
				itemStartRow = -1;
				itemStartCol = -1;
				inout = null;
				format = null;
				usecase = getUsecase(sheet221);
				if (usecase == null || usecase.length() == 0) {
					System.out.println("USECASE : " + file201.getPath());
				}
				if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet221, 5, 10)))) {
					itemStartRow = 6;
					itemStartCol = 10;
				} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet221, 5, 9)))) {
					itemStartRow = 6;
					itemStartCol = 9;
				} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet221, 7, 8)))) {
					itemStartRow = 8;
					itemStartCol = 8;
				} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet221, 8, 4)))) {
					itemStartRow = 9;
					itemStartCol = 4;
				} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet221, 5, 6)))) {
					itemStartRow = 6;
					itemStartCol = 6;
				} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet221, 5, 7)))) {
					itemStartRow = 6;
					itemStartCol = 7;
				} else if ("論理項目名".equals(ExcelUtil.getStr(ExcelUtil.getCell(sheet221, 5, 8)))) {
					itemStartRow = 6;
					itemStartCol = 8;
				}
				if (itemStartRow == -1 || itemStartCol == -1) {
					System.out.println("論理項目名 列特定不能: " + file221.getPath());
					continue;
				}
				if (ExcelUtil.getStr(ExcelUtil.getCell(sheet221, itemStartRow, itemStartCol)).length() == 0) {
					// 有的表头占两行，所以多判断一次，日。
					if (ExcelUtil.getStr(ExcelUtil.getCell(sheet221, itemStartRow + 1, itemStartCol)).length() > 0) {
						itemStartRow += 1;
					} else {
						System.out.println("論理項目名 値なし: " + file221.getPath());
					}
					continue;
				}
				inout = ExcelUtil.getStr(ExcelUtil.getCell(sheet221, itemStartRow, 1));
				if (inout == null || inout.length() == 0 || ("I".equals(inout) && "O".equals(inout))) {
					inout = ExcelUtil.getStr(ExcelUtil.getCell(sheet221, itemStartRow, 2));
				}
				if (inout == null || inout.length() == 0 || ("I".equals(inout) && "O".equals(inout))) {
					System.out.println("I/O 特定不能: " + file221.getPath());
				}
				format = getFormat(sheet221);
				if (format == null || format.length() == 0) {
					System.out.println("ファイル種類 特定不能: " + file221.getPath());
				}

				fieldName = ExcelUtil.getStr(ExcelUtil.getCell(sheet221, itemStartRow, itemStartCol));
				List<String> list221 = new ArrayList<String>();
				while (fieldName.length() > 0) {
					list221.add(fieldName);
					itemStartRow += 1;
					fieldName = ExcelUtil.getStr(ExcelUtil.getCell(sheet221, itemStartRow, itemStartCol));
				}
	
				// 比較を行う。
				XSSFSheet outSheet = outbook.getSheetAt(0);
				for (int i = 0, j = 0, startRow = 2; i < list201.size() || j < list221.size(); startRow++) {
					// Excelの行をコピ-する。
					ExcelUtil.copyRows(outSheet, startRow, startRow, startRow + 1);
					if (i < list201.size() && j < list221.size()) {
						if (list201.get(i).equals(list221.get(j))) {
							ExcelUtil.setCellValue(outSheet, startRow, 0, startRow - 1);
							ExcelUtil.setCellValue(outSheet, startRow, 1, list201.get(i));
							ExcelUtil.setCellValue(outSheet, startRow, 2, list221.get(j));
							i += 1;
							j += 1;
						} else {
							if (list221.contains(list201.get(i))) {
								ExcelUtil.setCellValue(outSheet, startRow, 0, startRow - 1);
								ExcelUtil.setCellValue(outSheet, startRow, 2, list221.get(j));
								ExcelUtil.setCellValue(outSheet, startRow, 3, "追加");
								j += 1;
							} else {
								ExcelUtil.setCellValue(outSheet, startRow, 0, startRow - 1);
								ExcelUtil.setCellValue(outSheet, startRow, 1, list201.get(i));
								ExcelUtil.setCellValue(outSheet, startRow, 3, "削除");
								i += 1;
							}
							same = false;
						}
					} else if (i >= list201.size() && j < list221.size()) {
						ExcelUtil.setCellValue(outSheet, startRow, 0, startRow - 1);
						ExcelUtil.setCellValue(outSheet, startRow, 2, list221.get(j));
						ExcelUtil.setCellValue(outSheet, startRow, 3, "追加");
						j += 1;
						same = false;
					} else if (i < list201.size() && j >= list221.size()) {
						ExcelUtil.setCellValue(outSheet, startRow, 0, startRow - 1);
						ExcelUtil.setCellValue(outSheet, startRow, 1, list201.get(i));
						ExcelUtil.setCellValue(outSheet, startRow, 3, "削除");
						i += 1;
						same = false;
					}
				}

				// 個別比較ファイルを出力する。
				FileOutputStream out = null;
				String filename = null;
				if (getSheetCount(workbook201) > 1) {
					filename = file201.getPath().replace("01_input", "02_output").replace(".xlsx", "（" + sheet.getSheetName() + "）.xlsx");
				} else {
					filename = file201.getPath().replace("01_input", "02_output");
				}
				out = new FileOutputStream(filename);
				outbook.write(out);
				out.close();
				outbook.close();

				// 一覧に出力する。
				ExcelUtil.copyRows(listSheet, listStartRow, listStartRow, listStartRow + 1);
				ExcelUtil.setCellValue(listSheet, listStartRow, 0, listStartRow - 4);
				ExcelUtil.setCellValue(listSheet, listStartRow, 1, module);
				ExcelUtil.setCellValue(listSheet, listStartRow, 2, usecase);
				ExcelUtil.setCellValue(listSheet, listStartRow, 3, new File(filename).getName().replace(".xlsx", ""));
				ExcelUtil.setCellValue(listSheet, listStartRow, 4, "〇");
				ExcelUtil.setCellValue(listSheet, listStartRow, 5, "〇");
				ExcelUtil.setCellValue(listSheet, listStartRow, 6, "I".equals(inout) ? "〇" : "-");
				ExcelUtil.setCellValue(listSheet, listStartRow, 7, "O".equals(inout) ? "〇" : "-");
				ExcelUtil.setCellValue(listSheet, listStartRow, 8, format.indexOf("CSV") >= 0 ? "〇" : "-");
				ExcelUtil.setCellValue(listSheet, listStartRow, 9, format.indexOf("TSV") >= 0 ? "〇" : "-");
				ExcelUtil.setCellValue(listSheet, listStartRow, 10, format.indexOf("EXCEL") >= 0 ? "〇" : "-");
				ExcelUtil.setCellValue(listSheet, listStartRow, 11, same ? "変更なし" : "変更あり");
				listStartRow += 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getUsecase(XSSFSheet sheet) {
		String usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 35));
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 34));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 33));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 32));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 31));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 30));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 29));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 26));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 6));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 4));
		}
		if (!checkUsecase(usecase)) {
			usecase = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 0, 39));
		}
		return usecase;
	}

	private String getInOut(XSSFSheet sheet) {
		int itemStartRow = -1;
		int itemStartCol = -1;
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
		if (itemStartRow > -1) {
			String inout = ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow, 1));
			if (inout == null || inout.length() == 0 || ("I".equals(inout) && "O".equals(inout))) {
				inout = ExcelUtil.getStr(ExcelUtil.getCell(sheet, itemStartRow, 2));
			}
			return inout;
		}
		return "";
	}

	private String getFormat(XSSFSheet sheet) {
		String format = ExcelUtil.getStr(ExcelUtil.getCell(sheet, 7, 4));
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
		return format;
	}
	
	private int getSheetCount(XSSFWorkbook workbook) {
		int count = 0;
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			String sheetName = workbook.getSheetAt(i).getSheetName();
			if (!sheetName.equals("イメージ")) {
				count += 1;
			}
		}
		return count;
	}

	private boolean checkUsecase(String usecase) {
		return !(usecase == null || usecase.length() == 0 || usecase.equals("追加") || usecase.indexOf("#") > -1);
	}
}
package com.lornwolf.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Excel用支援クラス。
 */
public class ExcelUtil {

    /**
     * アルファベット
     */
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * シート削除Key
     */
    public static final String REMOVE_SHEET = "removeSheet";

    /**
     * 日付変換
     */
    public enum DateType {
        CONVERT_yyyyMMddkkmmss, CONVERT_yyyyMMdd_kkmmss
    }

    /**
     * ExcelUtilのコンストラクタです.
     * 
     */
    public ExcelUtil() {
    }

    /**
     * ファイルコピー
     * 
     * @param in
     *            コピー元
     * @param out
     *            コピー先
     * @throws IOException
     */
    public static void copyFile(File in, File out) throws IOException {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    /**
     * 日付変換
     * 
     * @param date
     *            日付
     * @param dateType
     *            日付タイプ
     * @return 変換日付文字
     */
    public static String convertDate(Date date, DateType dateType) {
        String format = null;

        switch (dateType) {
        case CONVERT_yyyyMMddkkmmss:
            format = "yyyyMMddkkmmss";
            break;
        case CONVERT_yyyyMMdd_kkmmss:
            format = "yyyyMMdd_kkmmss";
            break;
        default :
            break;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, String id, boolean value) {
        getCellById(sheet, id).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param rowIdx
     *            0 based row number。。
     * @param colIdx
     *            0 based column number。。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, int rowIdx, int colIdx, boolean value) {
        getCell(sheet, rowIdx, colIdx).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, String id, Calendar value) {
        getCellById(sheet, id).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param rowIdx
     *            0 based row number。。
     * @param colIdx
     *            0 based column number。。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, int rowIdx, int colIdx, Calendar value) {
        getCell(sheet, rowIdx, colIdx).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, String id, Date value) {
        getCellById(sheet, id).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param rowIdx
     *            0 based row number。。
     * @param colIdx
     *            0 based column number。。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, int rowIdx, int colIdx, Date value) {
        getCell(sheet, rowIdx, colIdx).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, String id, double value) {
        getCellById(sheet, id).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param rowIdx
     *            0 based row number。。
     * @param colIdx
     *            0 based column number。。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, int rowIdx, int colIdx, double value) {
        getCell(sheet, rowIdx, colIdx).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, String id, RichTextString value) {
        getCellById(sheet, id).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param rowIdx
     *            0 based row number。。
     * @param colIdx
     *            0 based column number。。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, int rowIdx, int colIdx, RichTextString value) {
        getCell(sheet, rowIdx, colIdx).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, String id, String value) {
        getCellById(sheet, id).setCellValue(value);
    }

    /**
     * セル値設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param rowIdx
     *            0 based row number。。
     * @param colIdx
     *            0 based column number。。
     * @param value
     *            セル値。
     **/
    public static void setCellValue(Sheet sheet, int rowIdx, int colIdx, String value) {
        getCell(sheet, rowIdx, colIdx).setCellValue(value);
    }

    /**
     * セル書式設定処理.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param formula
     *            書式設定。
     */
    public static void setCellFormula(Sheet sheet, String id, String formula) {
        getCellById(sheet, id).setCellFormula(formula);
    }

    /**
     * 計算式設定処理.
     * 
     * <p>
     * セルに値を設定します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param rowIdx
     *            0 based row number。。
     * @param colIdx
     *            0 based column number。。
     * @param formula
     *            計算式。
     **/
    public static void setCellFormula(Sheet sheet, int rowIdx, int colIdx, String formula) {
        getCell(sheet, rowIdx, colIdx).setCellFormula(formula);
    }

    /**
     * セルコメント設定処理.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param comment
     *            コメント。
     */
    public static void setCellComment(Sheet sheet, String id, Comment comment) {
        getCellById(sheet, id).setCellComment(comment);
    }

    /**
     * セルアクティブ設定処理.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     */
    public static void setAsActiveCell(Sheet sheet, String id) {
        getCellById(sheet, id).setAsActiveCell();
    }

    /**
     * セルエラー値設定処理.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param errorCode
     *            エラーコード。
     */
    public static void setCellErrorValue(Sheet sheet, String id, byte errorCode) {
        getCellById(sheet, id).setCellErrorValue(errorCode);
    }

    /**
     * セルスタイル設定処理.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param style
     *            スタイル。
     */
    public static void setCellErrorValue(Sheet sheet, String id, CellStyle style) {
        getCellById(sheet, id).setCellStyle(style);
    }


    /**
     * セルハイパーリンク設定処理.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @param hyperlink
     *            ハイパーリンク。
     */
    public static void setCellErrorValue(Sheet sheet, String id, Hyperlink hyperlink) {
        getCellById(sheet, id).setHyperlink(hyperlink);
    }

    /**
     * セルboolean値取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return boolean値。
     */
    public static boolean getBooleanCellValue(Sheet sheet, String id) {
        return getCellById(sheet, id).getBooleanCellValue();
    }

    /**
     * セルコメント取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return コメント。
     */
    public static Comment getCellComment(Sheet sheet, String id) {
        return getCellById(sheet, id).getCellComment();
    }

    /**
     * セル書式設定取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return 書式設定。
     */
    public static String getCellFormula(Sheet sheet, String id) {
        return getCellById(sheet, id).getCellFormula();
    }

    /**
     * セルスタイル取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return スタイル。
     */
    public static CellStyle getCellStyle(Sheet sheet, String id) {
        return getCellById(sheet, id).getCellStyle();
    }

    /**
     * セルカラムインデックス取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return カラムインデックス。
     */
    public static int getColumnIndex(Sheet sheet, String id) {
        return getCellById(sheet, id).getColumnIndex();
    }

    /**
     * セル日付取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return 日付。
     */
    public static Date getDateCellValue(Sheet sheet, String id) {
        return getCellById(sheet, id).getDateCellValue();
    }

    /**
     * セルエラー値取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return エラー値。
     */
    public static byte getErrorCellValue(Sheet sheet, String id) {
        return getCellById(sheet, id).getErrorCellValue();
    }

    /**
     * セルハイパーリンク取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return ハイパーリンク。
     */
    public static Hyperlink getHyperlink(Sheet sheet, String id) {
        return getCellById(sheet, id).getHyperlink();
    }

    /**
     * セル数値取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return 数値。
     */
    public static double getNumericCellValue(Sheet sheet, String id) {
        return getCellById(sheet, id).getNumericCellValue();
    }

    /**
     * セルXSSFRichTextString値取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return XSSFRichTextString値。
     */
    public static RichTextString getRichStringCellValue(Sheet sheet, String id) {
        return getCellById(sheet, id).getRichStringCellValue();
    }

    /**
     * セルRow取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return Row。
     */
    public static Row getRow(Sheet sheet, String id) {
        return getCellById(sheet, id).getRow();
    }

    /**
     * セル行インデックス取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return 行インデックス。
     */
    public static int getRowIndex(Sheet sheet, String id) {
        return getCellById(sheet, id).getRowIndex();
    }

    /**
     * セルシート取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return シート。
     */
    public static Sheet getSheet(Sheet sheet, String id) {
        return getCellById(sheet, id).getSheet();
    }

    /**
     * セル文字取得.
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return 文字。
     */
    public static String getStringCellValue(Sheet sheet, String id) {
        return getStr(getCellById(sheet, id));
    }

    /**
     * セル値取得
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return オブジェクト
     */
    public static Object getCellValue(Sheet sheet, String id) {
        Cell cell = getCellById(sheet, id);
        return getCellValue(cell);
    }

    /**
     * セル値取得
     * 
     * @param cell
     *            セル。
     * @return オブジェクト
     */
    public static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {

           case STRING:
              return cell.getRichStringCellValue().getString();

           case NUMERIC:
              if (DateUtil.isCellDateFormatted(cell)) {
                 return cell.getDateCellValue();
              } else {
                 return cell.getNumericCellValue();
              }

           case BOOLEAN:
              return cell.getBooleanCellValue();

           case FORMULA:
              return cell.getCellFormula();

           default:
              return null;
        }
    }

    /**
     * セル値取得
     * 
     * @param cell
     *            セル。
     * @return 文字。
     */
    public static String getStr(Cell cell) {
        switch (cell.getCellType()) {
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());

            case FORMULA:
                return cell.getStringCellValue();

            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // 日付セルも数値型
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    return sdf.format(cell.getDateCellValue());
                } else {
                    CellStyle cs = cell.getCellStyle();
                    DecimalFormat df = new DecimalFormat(cs.getDataFormatString().replace("_", ""));
                    return df.format(cell.getNumericCellValue());
                }

            case STRING:
                return cell.getStringCellValue();

            default:
                return "";
        }
    }

    public static String getDateCellStr(Cell cell) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
            return sdf.format(cell.getDateCellValue());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * セル値取得処理.
     * 
     * <p>
     * セル値を取得します。
     * </p>
     * 
     * @param sheet
     *            シート。
     * @param id
     *            座標。
     * @return Cell セル。
     */
    public static Cell getCellById(Sheet sheet, String id) {
        int x = 0;
        int y = 0;

        if (sheet == null || id == null) {
            System.out.println("ExcelUtil nullエラー");
            return null;
        }

        String upperId = id.toUpperCase();

        try {
            for (int i = 0; i < upperId.length(); i++) {

                int index = ALPHABET.indexOf(upperId.charAt(i));

                if (index > -1) {
                    if (upperId.substring(i + 1).length() == 0) {
                        System.out.println("ExcelUtil セル座標エラー: " + id);
                        break;
                    }
                } else {

                    if (i == 0) {
                        System.out.println("ExcelUtil セル座標エラー: " + id);
                        break;
                    } else {
                        int maxCount = i - 1;
                        for (int j = 0; j < i; j++) {
                            index = ALPHABET.indexOf(upperId.charAt(j));
                            if (j == maxCount) {
                                x = x + index;
                            } else {
                                x = x + (int) ((index + 1) * Math.pow(26, maxCount - j));
                            }
                        }
                    }
                    y = Integer.parseInt(upperId.substring(i));
                    y -= 1;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("ExcelUtil セル座標エラー: " + id);
        }

        Row row = sheet.getRow(y);
        if (row == null) {
            row = sheet.createRow(y);
        }

        Cell cell = row.getCell(x);
        if (cell == null) {
            cell = row.createCell(x);
        }
        return cell;
    }

    /**
     * 行範囲指定削除
     * 
     * <p>
     * 削除開始行から削除行数分まで行の削除を行う</br> 例）2行目から3行削除する場合：removeRows(sheet, 1, 3)
     * </p>
     * 
     * @param sheet
     *            対象シート
     * @param startRow
     *            削除開始行
     * @param deleteCount
     *            削除行数
     */
    public static void removeRows(Sheet sheet, int startRow, int deleteCount) {
        // パラメータチェック
        if (sheet == null)
            return;

        CellRangeAddress cellRangeAdd = null;
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            // 結合されたセルの情報を取得
            cellRangeAdd = sheet.getMergedRegion(i);
            if (startRow <= cellRangeAdd.getFirstRow()) {
                // 結合解除
                sheet.removeMergedRegion(i);
                i = -1;
            }
        }
        for (int i = 0; i < deleteCount; i++) {
            int removeRowIndex = startRow + i;
            Row row = sheet.getRow(removeRowIndex);
            if (row == null)
                row = sheet.createRow(removeRowIndex);
            sheet.removeRow(row);
        }
    }

    /**
     * 行範囲指定削除
     * 
     * <p>
     * 削除開始行からsheetのlastRowNumまで削除を行う
     * </p>
     * 
     * @param sheet
     *            対象シート
     * @param startRow
     *            削除開始行
     */
    public static void removeRows(Sheet sheet, int startRow) {
        int lastRowNum = sheet.getLastRowNum();

        // パラメータチェック
        if (sheet == null || startRow > lastRowNum)
            return;

        CellRangeAddress cellRangeAdd = null;
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            // 結合されたセルの情報を取得
            cellRangeAdd = sheet.getMergedRegion(i);
            if (startRow <= cellRangeAdd.getFirstRow()) {
                // 結合解除
                sheet.removeMergedRegion(i);
                i = -1;
            }
        }
        for (int i = startRow; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null)
                row = sheet.createRow(i);
            sheet.removeRow(row);
        }
    }

    /**
     * 行範囲指定コピー
     * 
     * @param sheet
     *            対象シート
     * @param startRow
     *            コピー元開始行
     * @param endRow
     *            コピー元終了行
     * @param copyStartRow
     *            コピー先開始行
     */
    public static void copyRows(Sheet sheet, int startRow, int endRow, int copyStartRow) {
        // パラメータチェック
        if (sheet == null || startRow > endRow || endRow >= copyStartRow)
            return;

        CellRangeAddress cellRangeAdd = null;
        int merged = sheet.getNumMergedRegions();
        int nextIndex = copyStartRow;
        for (int i = 0; i < merged; i++) {
            // 結合されたセルの情報を取得
            cellRangeAdd = sheet.getMergedRegion(i);
            if (startRow <= cellRangeAdd.getFirstRow() && cellRangeAdd.getLastRow() <= endRow) {
                CellRangeAddress newAdd = new CellRangeAddress(cellRangeAdd.getFirstRow() + copyStartRow - startRow,
                        cellRangeAdd.getLastRow() + copyStartRow - startRow, cellRangeAdd.getFirstColumn(),
                        cellRangeAdd.getLastColumn());
                sheet.addMergedRegion(newAdd);
            }
        }
        nextIndex = copyStartRow;
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            copyRow(row, sheet.createRow(nextIndex));
            nextIndex++;
        }
    }

    /**
     * 行範囲指定コピー
     * 
     * @param sheet
     *            対象シート
     * @param startRow
     *            コピー元開始行
     * @param endRow
     *            コピー元終了行
     * @param copyStartRow
     *            コピー先開始行
     */
    public static void copyRowsWithFormula(Sheet sheet, int startRow, int endRow, int copyStartRow) {
        // パラメータチェック
        if (sheet == null || startRow > endRow || endRow >= copyStartRow)
            return;

        CellRangeAddress cellRangeAdd = null;
        int merged = sheet.getNumMergedRegions();
        int nextIndex = copyStartRow;
        for (int i = 0; i < merged; i++) {
            // 結合されたセルの情報を取得
            cellRangeAdd = sheet.getMergedRegion(i);
            if (startRow <= cellRangeAdd.getFirstRow() && cellRangeAdd.getLastRow() <= endRow) {
                CellRangeAddress newAdd = new CellRangeAddress(cellRangeAdd.getFirstRow() + copyStartRow - startRow,
                        cellRangeAdd.getLastRow() + copyStartRow - startRow, cellRangeAdd.getFirstColumn(),
                        cellRangeAdd.getLastColumn());
                sheet.addMergedRegion(newAdd);
            }
        }
        nextIndex = copyStartRow;
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            copyRowWithFormula(row, sheet.createRow(nextIndex));
            nextIndex++;
        }
    }

    /**
     * 行コピー
     * 
     * @param src
     *            コピー元
     * @param dst
     *            コピー先
     */
    private static void copyRow(Row src, Row dst) {
        dst.setHeight(src.getHeight());
        int length = src.getLastCellNum();
        for (int i = 0; i < length; i++) {
            Cell srcCell = src.getCell(i);
            Cell cell = dst.createCell(i);
            if (srcCell != null) {
                cell.setCellStyle(srcCell.getCellStyle());

                switch (srcCell.getCellType()) {
                case BLANK:
                    break;

                case BOOLEAN:
                    cell.setCellValue(srcCell.getBooleanCellValue());
                    break;

                case ERROR:
                    cell.setCellValue(srcCell.getErrorCellValue());
                    break;

                case FORMULA:
                    cell.setCellValue(srcCell.getStringCellValue());
                    break;

                case NUMERIC:
                    cell.setCellValue(srcCell.getNumericCellValue());
                    break;

                case STRING:
                    cell.setCellValue(srcCell.getStringCellValue());
                    break;

                default:
                    break;
                }
            }
        }
    }

    /**
     * 行コピー
     * 
     * @param src
     *            コピー元
     * @param dst
     *            コピー先
     */
    private static void copyRowWithFormula(Row src, Row dst) {
        dst.setHeight(src.getHeight());
        int length = src.getLastCellNum();
        for (int i = 0; i < length; i++) {
            Cell srcCell = src.getCell(i);
            Cell cell = dst.createCell(i);
            if (srcCell != null) {
                cell.setCellStyle(srcCell.getCellStyle());

                switch (srcCell.getCellType()) {
                case BLANK:
                    break;

                case BOOLEAN:
                    cell.setCellValue(srcCell.getBooleanCellValue());
                    break;

                case ERROR:
                    cell.setCellValue(srcCell.getErrorCellValue());
                    break;

                case FORMULA:
                    cell.setCellFormula(srcCell.getCellFormula());
                    break;

                case NUMERIC:
                    cell.setCellValue(srcCell.getNumericCellValue());
                    break;

                case STRING:
                    cell.setCellValue(srcCell.getStringCellValue());
                    break;

                default:
                    break;
                }
            }
        }
    }

    /**
     * Row取得処理.
     * 
     * <p>
     * Rowを取得します。
     * </p>
     * 
     * @param hssfSheet
     *            シート。
     * @param rowIdx
     *            0 based row number。
     * @return Row 行。
     */
    public static Row getRow(Sheet hssfSheet, int rowIdx) {
        Row hssfRow = hssfSheet.getRow(rowIdx);

        if (hssfRow == null) {
            hssfRow = hssfSheet.createRow(rowIdx);
        }

        return hssfRow;
    }

    /**
     * セル取得処理.
     * 
     * <p>
     * セルを取得します。
     * </p>
     * 
     * @param hssfRow
     *            行。
     * @param colIdx
     *            0 based column number。
     * @return Cell セル。
     */
    public static Cell getCell(Row hssfRow, int colIdx) {
        Cell hssfCell = hssfRow.getCell(colIdx);

        if (hssfCell == null) {
            hssfCell = hssfRow.createCell(colIdx);
        }

        return hssfCell;
    }

    /**
     * セル取得処理.
     * 
     * <p>
     * セルを取得します。
     * </p>
     * 
     * @param hssfSheet
     *            Sheet。
     * @param rowIdx
     *            0 based row number。
     * @param colIdx
     *            0 based column number。
     * @return Cell セル。
     */
    public static Cell getCell(Sheet hssfSheet, int rowIdx, int colIdx) {
        return getCell(getRow(hssfSheet, rowIdx), colIdx);
    }

    /**
     * セルコピー
     * 
     * @param hssfSheet
     *            Sheet
     * @param srcRowIdx
     *            src行index
     * @param srcColIdx
     *            src列index
     * @param dstRowIdx
     *            dst行index
     * @param dstColIdx
     *            dst列index
     */
    public static void copyCell(Sheet hssfSheet, int srcRowIdx, int srcColIdx, int dstRowIdx, int dstColIdx) {
        Cell srcCell = getCell(getRow(hssfSheet, srcRowIdx), srcColIdx);
        Cell dstCell = getRow(hssfSheet, dstRowIdx).createCell(dstColIdx);

        if (srcCell != null) {
            dstCell.setCellStyle(srcCell.getCellStyle());

            switch (srcCell.getCellType()) {
            case BLANK:
                break;

            case BOOLEAN:
                dstCell.setCellValue(srcCell.getBooleanCellValue());
                break;

            case ERROR:
                dstCell.setCellValue(srcCell.getErrorCellValue());
                break;

            case FORMULA:
                dstCell.setCellFormula(srcCell.getCellFormula());
                break;

            case NUMERIC:
                dstCell.setCellValue(srcCell.getNumericCellValue());
                break;

            case STRING:
                dstCell.setCellValue(srcCell.getStringCellValue());
                break;

            default:
                break;
            }
        }
    }

    /**
     * セル削除
     * 
     * @param hssfSheet
     *            Sheet
     * @param rowIdx
     *            行index
     * @param colIdx
     *            列index
     */
    public static void removeCell(Sheet hssfSheet, int rowIdx, int colIdx) {
        Row hssfRow = getRow(hssfSheet, rowIdx);

        hssfRow.removeCell(getCell(hssfRow, colIdx));
    }

    /**
     * columnのnext文字を取得します。
     * 
     * @param eng
     * @return
     */
    public static String getNextColumnEng(String eng) {

        int i = 0;
        int j = 0;

        for (i = 0; i < ALPHABET.length(); i++) {
            if (ALPHABET.substring(i, i + 1).equals(eng)) {

                if (ALPHABET.substring(i, i + 1).equals("Z")) {
                    return "AA";
                } else {
                    return ALPHABET.substring(i + 1, i + 2);
                }
            }
        }

        String firstStr = null;
        for (i = 0; i < ALPHABET.length(); i++) {

            firstStr = ALPHABET.substring(i, i + 1);

            for (j = 0; j < ALPHABET.length(); j++) {
                if ((firstStr + ALPHABET.substring(j, j + 1)).equals(eng)) {

                    if (ALPHABET.substring(j, j + 1).equals("Z")) {
                        return ALPHABET.substring(i + 1, i + 2) + "A";
                    } else {
                        return firstStr + ALPHABET.substring(j + 1, j + 2);
                    }
                }
            }
        }

        return null;
    }

    /**
     * CellStyleコピー
     * 
     * @param hssfSheet
     *            Sheet
     * @param srcRowIdx
     *            src行index
     * @param srcColIdx
     *            src列index
     * @param dstRowIdx
     *            dst行index
     * @param dstColIdx
     *            dst列index
     */
    public static void copyCellStyle(Sheet hssfSheet, int srcRowIdx, int srcColIdx, int dstRowIdx, int dstColIdx) {
        Cell srcCell = getCell(hssfSheet, srcRowIdx, srcColIdx);

        Cell dstCell = getCell(hssfSheet, dstRowIdx, dstColIdx);
        dstCell.setCellStyle(srcCell.getCellStyle());
    }
}
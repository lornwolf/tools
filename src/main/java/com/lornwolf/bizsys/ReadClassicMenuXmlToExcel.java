package com.lornwolf.bizsys;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lornwolf.bizsys.bean.Menu;
import com.lornwolf.common.ExcelUtil;

/**
 * 读取旧版本intra-mart导出的菜单定义XML，出力成Excel文件。
 * 
 * ※XML中所有MENU都在一个层级，通过parent-menu-id指示上级菜单。
 */
public class ReadClassicMenuXmlToExcel {

    private static String space = "";
    // 全局Sheet对象。
    private static XSSFSheet sheet = null;
    // Excel出力開始行。
    private static int startRow = 7;
    // 行番号。
    private static int no = 1;
    // モジュール名
    private static String module = "";

    private static List<Menu> menuList = new ArrayList<Menu>();

    public static void main(String[] args) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("C:/01_input/メニュー情報（テンプレート）.xlsx")));
            FileOutputStream out = new FileOutputStream("C:/02_output/メニュー情報.xlsx");) {

            sheet = workbook.getSheet("メニュー一覧");
            if (sheet == null) {
                return;
            }

            // ファイル内容を読み込む。
            List<String> lines = Files.lines(Paths.get("C:/01_input/menu_20210609.xml"), StandardCharsets.UTF_8).collect(Collectors.toList());
            StringBuffer buf = new StringBuffer();
            for (String line : lines) {
                buf.append(line);
            }

            // ドキュメントビルダーファクトリを生成。
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            // ドキュメントビルダーを生成。
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            // パースを実行してDocumentオブジェクトを取得。
            Document doc = builder.parse(new InputSource(new ByteArrayInputStream(buf.toString().getBytes("UTF-8"))));
            // ルート要素を取得。
            Element root = doc.getDocumentElement();
            // ルート要素から解析する。
            walk(root);

            workbook.write(out);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void walk(Node node) {
        for (Node ch = node.getFirstChild(); ch != null; ch = ch.getNextSibling()) {
            if ("menu".equals(ch.getNodeName())) {
                Menu menu = new Menu();
                menu.setDescription(getAttribute(ch, "description"));
                if (menu.getDescription().length() == 0) {
                    menu.setDescription(getDisplayName(ch));
                }
                menu.setMenuId(getAttribute(ch, "menu-id"));
                menu.setMenuType(getAttribute(ch, "menu-type"));
                menu.setParentMenuId(getAttribute(ch, "parent-menu-id"));
                menuList.add(menu);
            }
        }
        writeToExcel(menuList.stream().filter(o -> o.getParentMenuId().length() == 0).collect(Collectors.toList()));
    }
    
    private static void writeToExcel(List<Menu> list) {
        for (Menu menu : list) {
            String menuId = menu.getMenuId();
            String description = menu.getDescription();
            String type = menu.getMenuType();

            // Excelの行をコピーする。
            ExcelUtil.copyRows(sheet, startRow, startRow, startRow + 1);
            ExcelUtil.setCellValue(sheet, startRow, 0, String.valueOf(no));
            ExcelUtil.setCellValue(sheet, startRow, 1, module);
            ExcelUtil.setCellValue(sheet, startRow, 2, "folder".equals(type) ? "見出し" : "実行");
            ExcelUtil.setCellValue(sheet, startRow, 3, menuId);
            ExcelUtil.setCellValue(sheet, startRow, 4, "");
            ExcelUtil.setCellValue(sheet, startRow, 5, space + description);

            startRow += 1;
            no += 1;
            
            if ("folder".equals(type)) {
                space += "    ";
            }
            List<Menu> sons = menuList.stream().filter(o -> menuId.equals(o.getParentMenuId())).collect(Collectors.toList());
            if (sons.size() > 0) {
                writeToExcel(sons);
            }
            if ("folder".equals(type)) {
                space = space.substring(0, space.length() - 4);
            }
        }
    }

    /**
     * display_nameの日本語名を取得する。
     * 例：
     * <display-name>
     *     <locale-display-name>テナント管理</locale-display-name>
     *     <locale>ja</locale>
     * </display-name>
     */
    private static String getDisplayName(Node node) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if ("display-name".equals(list.item(i).getNodeName())) {
                NodeList children = list.item(i).getChildNodes();
                String displayName = null;
                for (int j = 0; j < children.getLength(); j++) {
                    if ("locale-display-name".equals(children.item(j).getNodeName())) {
                        displayName = children.item(j).getTextContent();
                    }
                    if ("locale".equals(children.item(j).getNodeName())) {
                        String local = children.item(j).getTextContent();
                        if ("ja".equals(local)) {
                            return displayName;
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * Nodeの属性値を取得する。
     * 
     * 例：
     * <node>
     *     <attr>text</attr>
     * </node>
     */
    private static String getAttribute(Node node, String attr) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (attr.equals(list.item(i).getNodeName())) {
                return list.item(i).getTextContent();
            }
        }
        return "";
    }

    private static List<Menu> getSubList(List<Menu> list, String parent) {
        return list.stream().filter(o -> parent.equals(o.getParentMenuId())).collect(Collectors.toList());
    }
}
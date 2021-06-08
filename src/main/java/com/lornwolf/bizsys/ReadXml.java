package com.lornwolf.bizsys;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

import com.lornwolf.common.ExcelUtil;

/**
 * 读取intra-mart导出的菜单定义XML，出力成Excel文件。
 */
public class ReadXml {

    private static String space = "";
    // メニュー階層。
    private static int hierarchy = 1;
    // 全局Sheet对象。
    private static XSSFSheet sheet = null;
    // Excel出力開始行。
    private static int startRow = 7;
    // 行番号。
    private static int no = 1;
    // モジュール名
    private static String module = "";

    public static void main(String[] args) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("C:/01_input/メニュー情報.xlsx")));
            FileOutputStream out = new FileOutputStream("C:/02_output/メニュー情報.xlsx");) {

            sheet = workbook.getSheet("メニュー一覧");
            if (sheet == null) {
                return;
            }

            // ファイル内容を読み込む。
            List<String> lines = Files.lines(Paths.get("C:/01_input/exported-menu-group.xml"), StandardCharsets.UTF_8).collect(Collectors.toList());
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
            space = "";
            getSpecificNodeValue("menu-item", ch.getNodeName(), ch);
        }
    }

    private static void getSpecificNodeValue(String targetNodeName, String nodeName, Node node) {
        String type = "";
        String displayName = "";
        String menuId = "";
        String menuHierarchy = "";
        if (targetNodeName.equals(nodeName)) {
            displayName = getDisplayName(node);
            menuId = getAttribute(node, "menu-id");
            type = getAttribute(node, "type");
            menuHierarchy = String.valueOf(hierarchy);
            if (hierarchy == 1) {
                module = "-";
            } else if (hierarchy == 2) {
                module = displayName;
            }

            System.out.println(space + displayName);

            // Excelの行をコピーする。
            ExcelUtil.copyRows(sheet, startRow, startRow, startRow + 1);
            ExcelUtil.setCellValue(sheet, startRow, 0, String.valueOf(no));
            ExcelUtil.setCellValue(sheet, startRow, 1, module);
            ExcelUtil.setCellValue(sheet, startRow, 2, "folder".equals(type) ? "見出し" : "実行");
            ExcelUtil.setCellValue(sheet, startRow, 3, menuId);
            ExcelUtil.setCellValue(sheet, startRow, 4, menuHierarchy);
            ExcelUtil.setCellValue(sheet, startRow, 5, space + displayName);

            startRow += 1;
            no += 1;
        }

        for (Node ch = node.getFirstChild(); ch != null; ch = ch.getNextSibling()) {
            if ("folder".equals(type)) {
                space += "    ";
                hierarchy += 1;
            }
            getSpecificNodeValue("menu-item", ch.getNodeName(), ch);
            if ("folder".equals(type)) {
                space = space.substring(0, space.length() - 4);
                hierarchy -= 1;
            }
        }
    }

    /**
     * display_nameの日本語名を取得する。
     * 例：
     * <display-names>
     *     <display-name locale="en">Tenant Management</display-name>
     *     <display-name locale="zh_CN">Tenant管理</display-name>
     *     <display-name locale="ja">テナント管理</display-name>
     * </display-names>
     */
    private static String getDisplayName(Node node) {
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if ("display-names".equals(list.item(i).getNodeName())) {
                NodeList children = list.item(i).getChildNodes();
                for (int j = 0; j < children.getLength(); j++) {
                    if ("display-name".equals(children.item(j).getNodeName())
                            && "ja".equals(getAttribute(children.item(j), "locale"))) {
                        return children.item(j).getTextContent();
                    }
                }
            }
        }
        return "";
    }

    /**
     * Nodeの属性値を取得する。
     * 
     * <node_name 属性="属性値" ...>
     */
    private static String getAttribute(Node node, String attr) {
        if (node.getAttributes() != null && node.getAttributes().getNamedItem(attr) != null) {
            return node.getAttributes().getNamedItem(attr).getNodeValue();
        }
        return "";
    }
}
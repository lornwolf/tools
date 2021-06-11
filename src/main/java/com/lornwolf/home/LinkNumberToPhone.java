package com.lornwolf.home;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.lornwolf.common.ExcelUtil;
import com.lornwolf.home.bean.People;

/**
 * 从一个Excel文件中读取姓名、身份证和手机号码信息，
 * 在另一个只有姓名的Excel文件中，填入相应的身份证号和手机号。
 */
public class LinkNumberToPhone {

    public static void main(String[] args) {

        // 保存身份证号与手机号的文件。
        String excel1 = "D:/现有实际居住人口摸底调查登记表新增.xlsx";

        // 需要填入数据的文件。
        String excel2 = "D:/县内接种人员名单.xlsx";

        // 输出文件的路径。
        String outputPath = "D:/县内接种人员名单（完成）.xlsx";

        // 人员信息Map。因为有重名，所以是一个名字对应一个列表。
        Map<String, List<People>> infoMap = new HashMap<String, List<People>>();

        try {

            InputStream ips01 = new FileInputStream(excel1);
            XSSFWorkbook workbook01 = new XSSFWorkbook(ips01);
            XSSFSheet sheet01 = workbook01.getSheetAt(0);

            for (int i = 3; i < 1096; i++) {
                String name = ExcelUtil.getStr(ExcelUtil.getCell(sheet01, i, 1));
                String number = ExcelUtil.getStr(ExcelUtil.getCell(sheet01, i, 8)).replace("General", "");
                String phone = ExcelUtil.getStr(ExcelUtil.getCell(sheet01, i, 9)).replace("General", "");
                if (StringUtils.isNotEmpty(number)) {
                    People people = new People();
                    people.setNumber(number);
                    people.setPhone(phone);
                    if (infoMap.get(name) != null) {
                        List<People> peoples = infoMap.get(name);
                        peoples.add(people);
                    } else {
                        List<People> peoples = new ArrayList<People>();
                        peoples.add(people);
                        infoMap.put(name, peoples);
                    }
                }
            }
            workbook01.close();

            InputStream ips02 = new FileInputStream(excel2);
            XSSFWorkbook workbook02 = new XSSFWorkbook(ips02);
            XSSFSheet sheet02 = workbook02.getSheetAt(0);

            for (int i = 3; i < 622; i++) {
                String name = ExcelUtil.getStr(ExcelUtil.getCell(sheet02, i, 1));
                List<People> peoples = infoMap.get(name);
                if (peoples != null) {
                    if (peoples.size() == 1) {
                        ExcelUtil.getCell(sheet02, i, 2).setCellValue(peoples.get(0).getNumber());
                        ExcelUtil.getCell(sheet02, i, 3).setCellValue(peoples.get(0).getPhone());
                    } else if (peoples.size() > 1) {
                        // 按年龄降序排序。
                        peoples = peoples.stream().sorted(Comparator.comparing(People::getAge).reversed()).collect(Collectors.toList());
                        ExcelUtil.getCell(sheet02, i, 2).setCellValue(peoples.get(0).getNumber());
                        ExcelUtil.getCell(sheet02, i, 3).setCellValue(peoples.get(0).getPhone());
                        peoples.remove(0);
                        infoMap.put(name, peoples);
                    }
                }
            }

            FileOutputStream fileOut = new FileOutputStream(outputPath);
            workbook02.write(fileOut);
            workbook02.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 检查一下有没有重复信息。
        List<String> list = new ArrayList<String>();
        try (InputStream ips01 = new FileInputStream(outputPath);
            XSSFWorkbook workbook01 = new XSSFWorkbook(ips01);) {
            XSSFSheet sheet01 = workbook01.getSheetAt(0);
            for (int i = 3; i < 622; i++) {
                String name = ExcelUtil.getStr(ExcelUtil.getCell(sheet01, i, 1));
                String number = ExcelUtil.getStr(ExcelUtil.getCell(sheet01, i, 2));
                String phone = ExcelUtil.getStr(ExcelUtil.getCell(sheet01, i, 3));
                if (list.contains(name + " " + number + " " + phone)) {
                    System.out.println(name + " " + number + " " + phone);
                } else {
                    list.add(name + " " + number + " " + phone);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.lornwolf.tools;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * 批量修改图片尺寸。
 * 指定修改后的宽度，高度自动计算。
 */
public class ImageReSize {

    /**
     * @param srcPath  源图片文件夹路径
     * @param desPath  修改大小后图片文件夹路径 
     * @param scaleSize 图片的修改比例，目标宽度。
     */
    public static void getFiles(String path, String destPath, int scaleSize) throws IOException {
        File file = new File(path);
        File[] tempList = file.listFiles();
        // 循环读取目录下图片
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) { 
                System.out.println("文件：" + tempList[i].getName() + "-" + tempList[i].getAbsolutePath().replaceAll("\\\\","/"));
                resizeImage(tempList[i].getAbsolutePath().replaceAll("\\\\","/"), destPath + "\\" + tempList[i].getName(), scaleSize);  
            }
        }
    }

    /**
     * @param srcPath  源图片路径
     * @param desPath  修改大小后图片路径 
     * @param scaleSize 图片的修改比例，目标宽度。
     */  
    public static void resizeImage(String srcPath, String desPath,int scaleSize) throws IOException {  

        File srcFile = new File(srcPath);  
        Image srcImg = ImageIO.read(srcFile);  
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(srcFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        float width = bi.getWidth(); // 像素
        float height = bi.getHeight(); // 像素
        float scale = width/scaleSize;
        BufferedImage buffImg = null;  
        buffImg = new BufferedImage(scaleSize, (int)(height/scale), BufferedImage.TYPE_INT_RGB); 
        //使用TYPE_INT_RGB修改的图片会变色 
        buffImg.getGraphics().drawImage(  
                srcImg.getScaledInstance(scaleSize, (int)(height/scale), Image.SCALE_SMOOTH), 0,  
                0, null);  

        ImageIO.write(buffImg, "JPEG", new File(desPath));
    }  

    public static void main(String []args) throws IOException{ 
        getFiles("C:\\01_入力\\pictures","C:\\02_出力\\pictures", 200);
    }
}
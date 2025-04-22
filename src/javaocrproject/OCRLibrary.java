/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package javaocrproject;

/**
 *
 * @author Xiang
 */
public interface OCRLibrary {
    /**
    * 识别图片路径中的文字
    * @param imagePath 图片文件路径
    * @return 返回识别结果的字符串
    */
    String recognize(String imagePath);
}

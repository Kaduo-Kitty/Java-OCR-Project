/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaocrproject;

import java.io.File;
import net.sourceforge.tess4j.TesseractException;

/**
 * IOcrService interface defines the contract for OCR (Optical Character Recognition) operations.
 * Any class implementing this interface must provide concrete implementations for the specified OCR methods.
 * This interface promotes abstraction and allows for different OCR engines to be used interchangeably.
 *
 * IOcrService 接口定义了 OCR（光学字符识别）操作的契约。
 * 任何实现此接口的类都必须为指定的 OCR 方法提供具体的实现。
 * 此接口促进了抽象性，并允许不同的 OCR 引擎可互换使用。
 * 
 * @author Xiang
 */
public interface IOcrService {
    //Performs OCR on a given image file using the specified language.
    //使用指定语言对给定的图像文件执行 OCR。
    String doOCR(File imageFile, String lang) throws TesseractException;
    
    /*
        Performs OCR on a given image file using the specified language and DPI (Dots Per Inch) setting.
        This overloaded method allows for more precise control over the OCR process. 
        使用指定语言和 DPI（每英寸点数）设置对给定的图像文件执行 OCR。
        此重载方法允许对 OCR 过程进行更精确的控制。
    */
    String doOCR(File imageFile, String lang, int dpi) throws TesseractException;
}

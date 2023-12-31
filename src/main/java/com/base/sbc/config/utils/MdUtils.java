package com.base.sbc.config.utils;

import gui.ava.html.image.generator.HtmlImageGenerator;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * md文档工具类
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月17日
 */
public class MdUtils {
	/**
	 * md转换为图片
	 * 
	 * @param mdText md文本
	 * @return
     */
    public static BufferedImage mdToImage(String mdText) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(mdText);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String content = renderer.render(document);
        content = content.replace("\n", "<br/>");
        if (content.endsWith("<br/>")) {
            content = content.substring(0, content.length() - 5);
        }

        return htmlToImage(content);
    }

    /**
     * html 转换为图片
     *
     * @param content
     * @return
     */
    public static BufferedImage htmlToImage(String content) {
        // 宽度600px
        content = "<div style=\"font-size:18px;width:700px\">" + content + "</div>";
        HtmlImageGenerator gen = new HtmlImageGenerator();
        gen.loadHtml(content);
        BufferedImage bufferedImage = gen.getBufferedImage();
        // 清理白色背景
        ImageIcon imageIcon = new ImageIcon(bufferedImage);
        Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
        g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
        int alpha = 0;
        for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
            for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                int rgb = bufferedImage.getRGB(j2, j1);
                int R = (rgb & 0xff0000) >> 16;
                int G = (rgb & 0xff00) >> 8;
                int B = (rgb & 0xff);
                if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                    rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                }
                bufferedImage.setRGB(j2, j1, rgb);
            }
        }
        g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
        return bufferedImage;
    }
}

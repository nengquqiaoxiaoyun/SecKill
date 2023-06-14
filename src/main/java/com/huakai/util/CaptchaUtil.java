package com.huakai.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class CaptchaUtil {

    // 随机字符集合
    private static final char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    // 验证码长度
    private static final int length = 4;
    // 图片宽度
    private static final int width = 100;
    // 图片高度
    private static final int height = 40;
    // 混淆线数量
    private static final int lines = 20;
    // 随机数生成器
    private static Random random = new Random();

    /**
     * 生成随机验证码字符串
     */
    public static String generateCaptchaString() {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    /**
     * 创建验证码图片并返回
     */
    public static BufferedImage createCaptchaImage(String captchaString) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        Font font = new Font("Arial", Font.BOLD, 30);
        g.setFont(font);
        for (int i = 0; i < length; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawString(String.valueOf(captchaString.charAt(i)), 20 * i + 10, 28);
        }
        for (int i = 0; i < lines; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
        }
        g.dispose();
        return image;
    }

    /**
     * 将验证码图片保存到本地文件
     */
    public static void saveCaptchaImage(BufferedImage image, OutputStream outputStream) throws IOException {
        ImageIO.write(image, "jpeg", outputStream);
    }

    public static void main(String[] args) throws IOException {
        String captchaString = generateCaptchaString();

        System.out.printf("验证码: %s%n", captchaString);
        BufferedImage captchaImage = createCaptchaImage(captchaString);
        FileOutputStream fileOutputStream = new FileOutputStream("H:\\聚焦Java性能优化 打造亿级流量秒杀系统（附赠秒杀项目）\\第10章 防刷限流技术【保护系统，免于过载】\\captcha.jpg");
        saveCaptchaImage(captchaImage, fileOutputStream);

    }
}

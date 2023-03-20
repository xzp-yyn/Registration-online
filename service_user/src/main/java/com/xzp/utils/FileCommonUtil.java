package com.xzp.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 时间未到，资格未够，继续努力！
 *
 * @Author xuezhanpeng
 * @Date 2022/11/11 10:04
 * @Version 1.0
 */
@Component
public class FileCommonUtil {

    @Value("${yt.cardimg}")
    public void setCardimg(String cardimg) {
        FileCommonUtil.cardimg = cardimg;
    }
    private static String cardimg;
    public static String upload(MultipartFile file){
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String imgname = uuid + suffix;
        File dir = new File(cardimg);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(cardimg+imgname));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgname;
    }

    public static void download(String name, HttpServletResponse response) throws Exception {
        //输入流，读取图片
        FileInputStream inputStream = new FileInputStream(new File(cardimg+name));
        int len=0;
        byte[] bytes=new byte[1024];
        response.setContentType("image/jpeg");
        ServletOutputStream outputStream = response.getOutputStream();
        while ((len=inputStream.read(bytes))!=-1){
            outputStream.write(bytes);
            outputStream.flush();
        }
        inputStream.close();
        outputStream.close();
    }

}

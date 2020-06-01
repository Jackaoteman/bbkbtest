package com.liyue.liyuetest.file;

import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MultipartFileDemo {
    public static void main(String[] args) throws IOException {

        File file = new File("C:\\Users\\Administrator\\Desktop\\题库导入.txt");

        FileInputStream fileInputStream = null;
        MultipartFile multipartFile = null;
        try {
            fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String originalFilename = multipartFile.getOriginalFilename();
        //读取每一行
        byte[] bytes = multipartFile.getBytes();
        String name = multipartFile.getName();

        System.out.println(originalFilename);
        System.out.println(bytes);
        System.out.println(name);
        System.out.println(multipartFile.getSize());
        System.out.println("===============================================");
        String ext = org.springframework.util.StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        InputStream in = null;
        if ("txt".equals(ext)) {
            //限制txt 文件大小
            if (multipartFile.getSize() / 1024 > 10) {
                System.out.println("导入文件不能大于10M");
            }
            //读取每一行，封装成题目对象
            in = multipartFile.getInputStream();
            byte[] data = multipartFile.getBytes();
            in.read(data);
            String[] str = new String(data, "UTF-8").split("\r\n\r\n");
            System.out.println(str[0]);
            System.out.println("----------------------------");
            System.out.println(str[1]);
            System.out.println("---------------------");

           StringBuffer sb=new StringBuffer();

            for (int i = 0; i < str.length; i++) {
                if(!"".equals(str[i])){
                    sb.append(str[i].trim());
                }
                if(i!=str.length-1){
                    sb.append("=");
                }
            }
            String[] str_01 = sb.toString().split("=");

            int size=0;


            System.out.println("数组长度="+str_01.length);
            System.out.println("size长度："+(size-1));
        }
    }
}

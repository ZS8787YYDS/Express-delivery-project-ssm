package com.zisheng.Controller;

import com.zisheng.Pojo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传与下载
 * 文件上传：
 * 采用MultiPartFile类型的对象接收到传递过来的文件之后，重新设置文件的名称并且将文件保存在本地磁盘目录下。
 * 返回文件的新名称
 * 文件下载：
 * 采用IO流的方式将图片写回到浏览器进行展示出来
 */
@RestController
@RequestMapping("/common")
public class ImageController {
    private static final Logger log  = LoggerFactory.getLogger(ImageController.class);
    @Value("${File.url}")
    private String fileUrl;
    /**
     * 文件上传功能
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file) throws Exception {
        log.info("接收到的数据为：{}",file);
        if(file == null) return Result.error("未接收到数据");
        // 获得原始文件名称
        String originalName = file.getOriginalFilename();
        String newFileName = null;
        if (originalName != null) {
            newFileName = UUID.randomUUID().toString() + originalName.substring(originalName.lastIndexOf("."));
            // 创建File文件对象，与文件接通
            File file1 = new File(fileUrl);
            // 如果文件不存在的话，直接进行创建
            if(!file1.exists()) file1.mkdirs();
            // 将文件转储在本地
            file.transferTo(new File(fileUrl + newFileName));
        }
        log.info("上传之后获得的路径为：{}" + ( fileUrl + newFileName));
        return Result.success(newFileName);
    }

    /**
     * 文件下载功能
     * @param name
     * @param response
     * @return
     */
    @GetMapping("/download")
    public Result downloadFile(String name, HttpServletResponse response)
    {
        log.info("下载时接收到的文件名称为：{}",name);
        try(
                // 创建文件字节输入流管道，与源文件接通
                FileInputStream inputStream = new FileInputStream(fileUrl + name);
                ServletOutputStream outputStream = response.getOutputStream();
                ) {
            int len;
            byte[] bytes = new byte[1024];
            // 设置响应给前端的内容类型
            response.setContentType("image/jpeg");
            // 每次循环读取一个字节数组的内容，返回读取到的字节个数，只要个数不为-1，说明读取到了字节
            while((len = inputStream.read(bytes)) != -1)
            {
                // 将读取到的字节通过字节输出流输出到浏览器中
                outputStream.write(bytes,0,len);
                // 刷新一下
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}

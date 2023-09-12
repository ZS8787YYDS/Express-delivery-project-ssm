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
public class CommonController {
    private static final Logger log  = LoggerFactory.getLogger(CommonController.class);
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
            // 如果文件不存在的，直接进行创建
            if(!file1.exists()) file1.mkdirs();
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
                FileInputStream inputStream = new FileInputStream(fileUrl + name);
                ServletOutputStream outputStream = response.getOutputStream();
                ) {
            int len;
            byte[] bytes = new byte[1024];
            // 设置响应给前端的内容类型
            response.setContentType("image/jpeg");
            while((len = inputStream.read(bytes)) != -1)
            {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}

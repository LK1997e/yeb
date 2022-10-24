package com.example.server.util;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class OssUploadUtil {
    public static String uploadFile(MultipartFile multipartFile){
        String endpoint = "oss-cn-hangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tDBkdi5eDmZLhgPwyJ3";
        String accessKeySecret = "rPUMVi8V8kAQR1icW0pAkaBjZxrVKC";
        // 填写Bucket名称
        String bucketName = "workbarket";


        OSS ossClient = null;
        try {

            // 创建OSSClient实例。
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            //获取文件上传的流
            InputStream inputStream = multipartFile.getInputStream();
            //构建日期目录
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String datePath = dateFormat.format(new Date());
            String originalName = multipartFile.getOriginalFilename();
            String fileName = UUID.randomUUID().toString();
            String suffix = originalName.substring(originalName.lastIndexOf("."));
            String newName = fileName + suffix;
            String fileUrl = datePath + "/" + newName;
            ossClient.putObject(bucketName, fileUrl, inputStream);
            return "https://"+bucketName+"."+endpoint+"/"+fileUrl;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}

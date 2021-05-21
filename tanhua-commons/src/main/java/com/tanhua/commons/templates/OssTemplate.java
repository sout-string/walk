package com.tanhua.commons.templates;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.tanhua.commons.properties.OssProperties;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author : 涂根
 * @date : 2021/05/04 下午 8:01
 */
public class OssTemplate {
    private OssProperties ossProperties;

    public OssTemplate(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    public String upload(String filename, InputStream inputStream) {
        //读取配置信息
        String endpoint = ossProperties.getEndpoint();
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        //获取OSS对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //配置上传文件信息
        String suffix = filename.substring(filename.lastIndexOf("."));
        String ymd = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        String name = "images/" + ymd + "/" + UUID.randomUUID().toString() + suffix;
        //上传文件
        ossClient.putObject(ossProperties.getBucketName(), name, inputStream);
        //关闭OSS对象
        ossClient.shutdown();
        return ossProperties.getUrl() + "/" + name;
    }
    /**
     * 删除头像
     */
    public void delete(String objectName) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ossProperties.getAccessKeyId();
        String accessKeySecret = ossProperties.getAccessKeySecret();
        String bucketName = ossProperties.getBucketName();
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 删除文件。如需删除文件夹，请将ObjectName设置为对应的文件夹名称。如果文件夹非空，则需要将文件夹下的所有object删除后才能删除该文件夹。
        ossClient.deleteObject(bucketName, objectName.replace(ossProperties.getUrl()+ "/",""));
        // 关闭OSSClient。
        ossClient.shutdown();
    }
}

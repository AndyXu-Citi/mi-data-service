package com.marathon.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.marathon.common.exception.ServiceException;
import com.marathon.config.MinioConfig;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * 文件上传工具类
 *
 * @author marathon
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadUtil {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件访问路径
     */
    public String uploadFile(MultipartFile file, String folderName) {
        try {
            // 检查存储桶是否存在
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());

            // 如果不存在则创建
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .build());
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = folderName + "/" + UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;

            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            // 返回文件访问URL
            return minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;

        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL
     */
    public void deleteFile(String fileUrl) {
        if (StrUtil.isBlank(fileUrl)) {
            return;
        }

        try {
            // 解析文件路径
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            // 删除文件
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(fileName)
                    .build();
            minioClient.removeObject(args);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new ServiceException("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 检查文件
     *
     * @param file 文件
     */
    private void checkFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ServiceException("上传文件不能为空");
        }

        // 检查文件大小（默认限制10MB）
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new ServiceException("上传文件大小不能超过10MB");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ServiceException("上传文件只能是图片类型");
        }
    }

    /**
     * 检查存储桶是否存在，不存在则创建
     */
    private void checkBucket() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .build());
            }
        } catch (Exception e) {
            log.error("检查存储桶失败", e);
            throw new ServiceException("检查存储桶失败: " + e.getMessage());
        }
    }

    /**
     * 生成文件名
     *
     * @param file 文件
     * @return 文件名
     */
    private String generateFileName(MultipartFile file) {
        // 获取原文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            originalFilename = "unknown";
        }

        // 获取文件后缀
        String suffix = FileUtil.extName(originalFilename);

        // 生成文件名：日期路径 + UUID + 后缀
        String datePath = DateUtil.format(new Date(), "yyyy/MM/dd");
        return datePath + "/" + IdUtil.fastSimpleUUID() + "." + suffix;
    }
}
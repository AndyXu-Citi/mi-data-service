package com.marathon.controller;

import com.marathon.common.api.R;
import com.marathon.util.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 *
 * @author marathon
 */
@Tag(name = "文件管理", description = "文件上传相关接口")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileUploadUtil fileUploadUtil;

    /**
     * 上传文件
     */
    @Operation(summary = "上传文件", description = "上传文件到MinIO")
    @PostMapping("/upload")
    public R<String> upload(@RequestParam("file") MultipartFile file,@RequestParam("folderName") String folderName) {
        String url = fileUploadUtil.uploadFile(file, folderName);
        return R.ok(url, "上传成功");
    }
}
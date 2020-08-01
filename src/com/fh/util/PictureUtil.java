package com.fh.util;

import org.springframework.web.multipart.*;
import com.fh.entity.result.*;
import java.io.*;
import org.springframework.web.client.*;
import org.springframework.core.io.*;
import org.springframework.util.*;
import org.springframework.http.*;

/**
 * 功能描述：上传图片 接入公司的图床
 *
 * @author Ajie
 * @date 2020/5/11 0011
 */
public class PictureUtil {

    static final String PICTURE_API = "https://www.sctupian.xyz/index/Vipupload/matchupload/modeles/filename";

    public static R upload(final MultipartFile pictureFile, final String fileName) {
        final PageData pd = new PageData();
        if (null != fileName && !fileName.isEmpty()) {

            final String tempFilePath = System.getProperty("java.io.tmpdir") + pictureFile.getOriginalFilename();
            // 创建临时文件
            final File tempFile = new File(tempFilePath);
            try {
                pictureFile.transferTo(tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            final RestTemplate restTemplate = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
            final MultiValueMap<String, Object> param = (MultiValueMap<String, Object>) new LinkedMultiValueMap();
            final FileSystemResource resource = new FileSystemResource(tempFilePath);
            param.add("file", resource);
            param.add("filename", fileName);
            final HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity(param, headers);
            final ResponseEntity<PageData> responseEntity = restTemplate.postForEntity(PICTURE_API, formEntity, PageData.class);
            final PageData result = responseEntity.getBody();

            tempFile.delete();

            // 请求成功
            final Integer success = 1;
            if (result.get("status").equals(success)) {
                pd.put("urls", result.get("urls"));
            }
            return R.ok().data(pd);
        }
        return R.parError();
    }
}

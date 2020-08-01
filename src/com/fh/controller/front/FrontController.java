package com.fh.controller.front;


import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：前台页面 等
 *
 * @author Ajie
 * @date 2020年5月4日17:40:18
 */
@Controller
@RequestMapping(value = "/front")
@Slf4j
@Api(tags = "前台页面等")
public class FrontController extends BaseFrontController {


    /**
     * 功能描述：图片上传，接入公司图床API 需要用户登录之后才能上传
     *
     * @author Ajie
     * @date 2020/5/11 0011
     */
    @RequestMapping(value = {"/addImg"}, method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("图片上传-需要用户认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "文件流对象", required = true, dataType = "__File"),
            @ApiImplicitParam(name = "filesName", value = "项目名称", example = "J0518", required = true)
    })
    public R addPic(@RequestParam(name = "files") final MultipartFile pictureFile, final String filesName) {
        return PictureUtil.upload(pictureFile, filesName);
    }

}



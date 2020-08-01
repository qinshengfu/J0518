package com.fh.controller.front;


import com.fh.controller.base.BaseController;
import com.fh.entity.MemUser;
import com.fh.entity.result.R;
import com.fh.util.*;
import com.fh.util.express.ImageUtils;
import com.fh.util.verificationCode.PhoneVerificaCodeUtil;
import com.fh.util.verificationCode.RandomCodeUtil;
import com.fh.util.verificationCode.VerifyCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.session.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 功能描述：前台页面跳转【不需要登录】
 *
 * @author Ajie
 * @date 2019/11/26 0026
 */
@Controller
@RequestMapping(value = "/release")
@Slf4j
public class ReleaseController extends BaseController {

    // 加减乘 验证码
    @Resource(name = "verifyCode")
    private VerifyCodeService verifyCodeService;


    /**
     * 获取头部信息
     *
     * @return
     */
    @RequestMapping(value = "/getWebSocketList")
    @ResponseBody
    public Object getWebSocketList() {
        PageData pd = new PageData();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            pd = this.getPageData();
            Session session = Jurisdiction.getSession();
            // 前台当前登录用户信息
            MemUser user = (MemUser) session.getAttribute(Const.SESSION_MEMUSER);
            map.put("user", user);
            String strWEBSOCKET = Tools.readTxtFile(Const.WEBSOCKET);//读取WEBSOCKET配置
            if (null != strWEBSOCKET && !"".equals(strWEBSOCKET)) {
                String strIW[] = strWEBSOCKET.split(",fh,");
                if (strIW.length == 7) {
                    map.put("oladress", strIW[2] + ":" + strIW[3]);        //在线管理和站内信服务器IP和端口
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppUtil.returnObject(pd, map);
    }

    /**
     * 功能描述：访问测试页面
     *
     * @param
     * @return
     * @author Ajie
     * @date 2019/10/29 0029
     */
    @RequestMapping(value = "/toAdminTest")
    private ModelAndView toAddimg() {
        ModelAndView mv = this.getModelAndView();
        mv.setViewName("front/A_pictureUpload2");
        return mv;
    }

    /**
     * 功能描述：update返回值测试
     *
     * @author Ajie
     * @date 2020/4/17 0017
     */
    @RequestMapping(value = "/test")
    @ResponseBody
    private R testLock() throws Exception {
        PageData pd = new PageData();


        return R.ok().message("用户列表");
    }

    /**
     * 功能描述：生成加减成验证码
     *
     * @author Ajie
     * @date 2019/10/30 0030
     */
    @RequestMapping(value = "/verifyCode")
    public void getMiaoshaVerifyCod(HttpServletResponse response) {

        try {
            BufferedImage image = verifyCodeService.getVerify();
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @描述：图片上传
     * @参数：请求和文件数据
     * @返回值：UUID后的图片路径
     * @创建人：Ajie
     * @创建时间：2019/10/15 0015
     */
    @RequestMapping(value = "/addPic")
    @ResponseBody
    public String addUser(HttpServletRequest request, @RequestParam(name = "files") MultipartFile pictureFile) {
        PageData pd = this.getPageData();
        // 得到上传图片的地址
        String imgPath = "";
        try {
            // ImageUtils为之前添加的工具类
            // 判断是二维码还是普通图片
            String tag = pd.getString("tag");
            if (Tools.isEmpty(tag)) {
                imgPath = ImageUtils.upload(request, pictureFile, Const.FILEPATHIMG);
            } else {
                imgPath = ImageUtils.upload(request, pictureFile, Const.FILEPATHTWODIMENSIONCODE);
            }
            if (imgPath != null) {
                System.out.println("-----------------图片上传成功！");
            } else {
                System.out.println("-----------------图片上传失败！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("----------------图片上传失败！");
        }
        System.out.println("图片上传路径：" + imgPath);
        return imgPath;
    }

    /**
     * 功能描述：图片上传，接入公司图床API
     *
     * @author Ajie
     * @date 2020/5/11 0011
     */
    @RequestMapping(value = {"/addImg"}, method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("图片上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "files", value = "文件流对象", required = true, dataType = "__File"),
            @ApiImplicitParam(name = "filesName", value = "项目名称", example = "J0518", required = true)
    })
    public R addPic(@RequestParam(name = "files") final MultipartFile pictureFile, final String filesName) {
        return PictureUtil.upload(pictureFile, filesName);
    }

    /**
     * 功能描述：发送短信验证码
     *
     * @param phone - 目标手机号
     * @author Ajie
     * @date 2019/10/31 0031
     */
    @RequestMapping(value = "/phoneCode", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String phoneCode(@RequestParam(name = "phone") String phone) throws Exception {
        // 获取6位 随机纯数字验证码
        String code = RandomCodeUtil.getInvitaCode(6, 1);
        System.out.println("短信验证码：" + code);
        // 有效期/分钟
        int validity = 3;
        String resule = PhoneVerificaCodeUtil.SendSMS(phone, code, validity);
        if ("success".equals(resule)) {
            // 放入session中
            Jurisdiction.getSession().setAttribute(Const.SESSION_PHONE_CHECK_CODE, code);
            log.info("本次短信验证码：{}", code);
        } else {
            log.info("短信验证码发送失败，原因是：{}", resule);
        }

        //TimerTask实现N分钟后从session中删除验证码
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Jurisdiction.getSession().removeAttribute(Const.SESSION_PHONE_CHECK_CODE);
                System.out.println("短信验证码--> 删除成功");
                timer.cancel();
            }
        }, validity * 60 * 1000);
        String spare = PhoneVerificaCodeUtil.getSpare();
        log.info("短信余额：{}", spare);

        return resule;
    }


    /**
     * 功能描述：语言转换（中/英）
     *
     * @param lang 语言标识
     * @author Ajie
     * @date 2020/4/6 0006
     */
    @RequestMapping(value = "/langSwitching")
    private String langSwitching(@Param("lang") String lang) {
        Session session = Jurisdiction.getSession();
        session.setAttribute("lang", lang);
        return "redirect:toLogin";
    }

    /**
     * 功能描述：访问输入密保页面
     *
     * @author Ajie
     * @date 2020年4月6日15:29:01
     */
    @RequestMapping(value = "/toSecretProtection")
    public ModelAndView toSecretProtection() throws Exception {
        ModelAndView mv = this.getModelAndView();
        MemUser user = (MemUser) Jurisdiction.getSession().getAttribute(Const.SESSION_MEMUSER_TEMPORARY);
        mv.setViewName("front/login/securityQuestion");
        mv.addObject("user", user);
        return mv;
    }

    /**
     * 功能描述：退出登录
     *
     * @return 重定向到登录页
     * @author Ajie
     * @date 2020/3/28 0028
     */
    @RequestMapping(value = "outLogin")
    private ModelAndView outLogin() {
        ModelAndView mv = this.getModelAndView();
        PageData pd = this.getPageData();
        // 清缓存
        this.removeCache();
        pd.put("msg", pd.getString("msg"));
        mv.setViewName("front/login/login");
        mv.addObject("pd", pd);
        return mv;
    }

    /**
     * 功能描述：清空缓存
     *
     * @author Ajie
     * @date 2020/3/27 0027
     */
    public void removeCache() {
        Session session = Jurisdiction.getSession();
        // 清空session
        session.removeAttribute(Const.SESSION_MEMUSER);

    }

    /**
     * 功能描述：访问接口文档
     *
     * @author Ajie
     * @date 2020/5/11 0011
     */
    @RequestMapping(value = "doc")
    public ModelAndView doc() {
        final ModelAndView mv = this.getModelAndView();
        mv.setViewName("swagger-bootstrap-ui");
        return mv;
    }

    /**
     * 功能描述：修改每天生成K线图数据定时任务时间
     *
     * @author Ajie
     * @date 2020/6/22 0022
     */
    @RequestMapping(value = "saveJobTimeTest", method = RequestMethod.GET)
    @ResponseBody
    public R saveJobTimeTest() {
        PageData pd = this.getPageData();
        String data = "2020-06-22 ";
        String time = data + pd.getString("time");
        String jobTime = DateUtil.getCronDay(time);
        QuartzManager.modifyJobTime("beginOfDayTaskJob", jobTime);
        // 查看所有定时任务
        QuartzManager.taskMethod();
        return R.ok();
    }

}



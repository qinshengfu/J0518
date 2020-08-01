package com.fh.util.voice;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.baidu.aip.util.Util;
import com.fh.util.Const;
import com.fh.util.PathUtil;
import javazoom.jl.player.Player;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * 功能描述：基于百度AI开发平台 的语音合成
 *
 * @author Ajie
 * @date 2020/5/6 0006
 */
public class speechSynthesisUtil {

    // 设置APPID/AK/SK
    // 百度AI开发平台的控制台中创建一个语音应用即可获得
    private static final String APP_ID = "19736093";
    private static final String API_KEY = "3CExsuNuQov9uMs4WwRPTX4h";
    private static final String SECRET_KEY = "Z5cB44E8CF2hGtYtAKYjZiHxBDdS4wcU";

    private static final AipSpeech AIP_SPEECH = getAipSpeech();

    public static void main(String[] args) {
        speechSynthesisUtil speechSynthesisUtil = new speechSynthesisUtil();
        String text = "第1个号码是18！";
        String path = PathUtil.getClasspath() + Const.FILE_PATH_AUDIO + "18.mp3";
        if (!speechSynthesisUtil.speechSynthesizer(text, path)) {
            System.out.println("文字转换语音失败");
        } else {
            speechSynthesisUtil.playAudio(path);
        }
    }

    /**
     * 功能描述：获取实例
     *
     * @author Ajie
     * @date 2020/5/6 0006
     */
    private static AipSpeech getAipSpeech() {
        // 初始化一个AipSpeech
        AipSpeech aipSpeech = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        aipSpeech.setConnectionTimeoutInMillis(2000);
        aipSpeech.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //aipSpeech.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //aipSpeech.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
//         System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        return aipSpeech;
    }

    /**
     * 将文字转为MP3文件，需联网，依靠百度语音合成
     *
     * @param word       文字内容
     * @param outputPath 合成语音生成路径
     * @return 是否成功
     */
    public boolean speechSynthesizer(String word, String outputPath) {

        // 设置可选参数
        HashMap<String, Object> options = new HashMap<>();
        // 语速，取值0-9，默认为5中语速
        options.put("spd", "5");
        // 音调，取值0-9，默认为5中语调
        options.put("pit", "5");
        // 音量，取值0-15，默认为5中音量
        options.put("vol", "5");
        // 发音人选择, 0为女声，1为男声，3为情感合成-度逍遥，4为情感合成-度丫丫，默认为普通女
        options.put("per", "4");

        // 调用接口
        // text 合成的文本，使用UTF-8编码。小于2048个中文字或者英文数字。（文本在百度服务器内转换为GBK后，长度必须小于4096字节）
        // lang 固定值zh。语言选择,目前只有中英文混合模式，填写固定值zh
        // ctp 客户端类型选择，web端填写固定值1
        TtsResponse res = AIP_SPEECH.synthesis(word, "zh", 1, options);
        // 如果合成成功,下行数据为二进制语音文件，包含在data中。 如果合成出现错误，则会填充返回值到result中。
        // 若请求错误，服务器将返回的JSON文本包含以下参数：
        // error_code：错误码。
        // error_msg：错误描述信息，帮助理解和解决发生的错误。
        byte[] data = res.getData();
        JSONObject res1 = res.getResult();
        if (data != null) {
            try {
                Util.writeBytesToFileSystem(data, outputPath);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        if (res1 != null) {
            try {
                System.out.println(res1.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 功能描述：播放MP3
     * @author Ajie
     * @date 2020/5/6 0006
     */
    public void playAudio(String finePath){
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(finePath));
            // 需导入javazoom.jl.player.Player，下载地址http://www.javazoom.net/javalayer/sources/jlayer1.0.1.zip
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

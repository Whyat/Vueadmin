package com.whyat.controller;

import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.whyat.common.lang.Const;
import com.whyat.common.lang.Result;
import com.whyat.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class AuthController {
    //验证码生产者顶级接口
    //注入配置好的DefaultKaptcha
    @Autowired
    Producer producer;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 谷歌Kaptcha验证码
     *
     * @return
     */
    @GetMapping("/captcha")
    public Result captcha() throws IOException {

        //1.uuid生成一个key，验证码的唯一标识
        String key = UUID.randomUUID().toString();
        String code = producer.createText();


        //2.生成验证码图片并写入到流中
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        log.info("生成了验证码图片是:" + code);

        //3.使用Base64编码图片字节数组
        BASE64Encoder encoder = new BASE64Encoder();
        String prefix = "data:image:/jpg;base64,";
        String content = encoder.encode(outputStream.toByteArray());
        String base64ImgStr = prefix + content;

        //4.将验证码唯一标识key存入redis
        redisUtil.hset(Const.CAPTCHA, key, code, 120);

        //5.内部创建1个hashmap存放唯一标识和base64图片编码
        //build()最终返回hashmap实例
        Map<Object, Object> map = MapUtil.builder()
                .put("key", key)
                .put("captchaImg", base64ImgStr)
                .build();

        return Result.success(map);
    }
}


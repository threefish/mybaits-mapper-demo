package com.example.mybaitsmapper.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 黄川 huchuc@vip.qq.com
 */
@RestController
public class AppApi {

    @GetMapping("test")
    public String test(){
        return "say hi";
    }
}

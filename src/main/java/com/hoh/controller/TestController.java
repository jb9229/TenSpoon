package com.hoh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by jeong on 2015-10-15.
 */
@Controller
public class TestController {

    @RequestMapping("/")
    public String intro(){
        return "test";
    }

}

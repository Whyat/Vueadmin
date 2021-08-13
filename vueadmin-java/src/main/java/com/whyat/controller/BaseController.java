package com.whyat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO
 *
 * @Author Whyat
 * @Date 2021/8/12 15:42
 */
public class BaseController {
    @Autowired
    HttpServletRequest req;
}

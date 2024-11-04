package com.example.campus.controller;

import com.example.campus.util.SecurityUtil;

public abstract class BaseController {
    protected String getRequester() {
        return SecurityUtil.getUsername();
    }
}

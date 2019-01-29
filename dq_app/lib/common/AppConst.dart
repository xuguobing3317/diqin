import 'package:flutter/material.dart';
import 'package:fluro/fluro.dart';

import 'package:dq_app/home.dart';
import 'package:dq_app/LoginPage.dart';
import 'dart:io';

import 'package:fluttertoast/fluttertoast.dart';

Router router = new Router();

var loginHandler =
    Handler(handlerFunc: (BuildContext context, Map<String, dynamic> params) {
  return LoginPage();
});

var homeHandler =
    Handler(handlerFunc: (BuildContext context, Map<String, dynamic> params) {
  return HomePage();
});

void defineRoutes(Router router) {
  router.define("/home", handler: homeHandler);
  router.define("/login", handler: loginHandler);
}

void initRouter() {
  defineRoutes(router);
}

Map<String, String> headers = {
  'OsName': Platform.operatingSystem.toUpperCase()
};

const String RESP_CODE = "responseCode";
const String RESP_MSG = "responseDesc";
const String RESP_DATA = "data";

Color appColor = Colors.lightGreen;

const String SUCCESS = "000000"; // "请求处理成功"
const String PROCESSING = "000001"; // "请求已受理"),
const String PROCESS_ERROR = "000002"; // "请求受理失败"),
const String PROCESS_CANCEL = "000003"; // "请求受理撤销"),
const String USER_NOT_EXISTS = "100001"; // "用户不存在"),
const String LOGIN_PWD_ERROR = "100002"; // "密码不正确"),
const String TOKEN_NULL = "100003"; // "TOKEN为空"),
const String TOKEN_ERROR = "100004"; // "TOKEN错误"),
const String TOKEN_INVALID = "100005"; // "TOKEN已失效"),
const String PARAM_ERROR = "100006"; // "参数错误"),
const String PAGE_NOT_FOUND = "000404"; // "页面不存在"),
const String PAGE_ERROR = "000500"; // "服务内部错误"),
const String BUSI_ERROR = "999999"; // "业务异常"),


void showMsg(String _msg) {
  Fluttertoast.instance.showToast(
              msg: _msg,
              toastLength: Toast.LENGTH_SHORT,
              gravity: ToastGravity.BOTTOM,
              timeInSecForIos: 1,
              backgroundColor: Color(0xFF499292),
              textColor: Color(0xFFFFFFFF));
}




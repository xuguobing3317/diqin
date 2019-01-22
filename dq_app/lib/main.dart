import 'package:flutter/material.dart';
import 'SplashPage.dart';
import 'package:dq_app/common/AppConst.dart';


void main() {
  initRouter();
  runApp(new MyApp());
} 

class MyApp extends StatelessWidget {
// This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: '嫡清医疗',
      debugShowCheckedModeBanner: false, 
      theme:
      new ThemeData(
          primarySwatch: appColor,
          brightness: Brightness.light,
          backgroundColor: Colors.white,
          platform: TargetPlatform.iOS),
      home: new SplashPage(), // 闪屏页
    );
  }
}
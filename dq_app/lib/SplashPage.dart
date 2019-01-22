import 'dart:async';
import 'package:flutter/widgets.dart';
import 'package:dq_app/common/AppConst.dart';

class SplashPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _SplashPageState();
  }
}

class _SplashPageState extends State<SplashPage> {

  
  @override
  Widget build(BuildContext context) {
    return new Image.asset(
      "images/spash.jpg",
      fit: BoxFit.fill,);
  }

  @override
  void initState() {
    super.initState();
    countDown();
  }

// 倒计时
  void countDown() {
    var _duration = new Duration(seconds: 3);
    new Future.delayed(_duration, go2LoginPage);
  }

  void go2LoginPage() {
    router.navigateTo(context, '/login', replace: true);
  }
}

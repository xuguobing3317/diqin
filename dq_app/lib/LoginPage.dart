import 'package:flutter/material.dart';
import 'package:dq_app/common/AppConst.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:dq_app/common/AppUrl.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class LoginPage extends StatefulWidget {
  LoginPage({Key key, this.title}) : super(key: key);
  final String title;

  @override
  _LoginPageState createState() => new _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  bool _isLoading = false;
  TextEditingController _phonecontroller = new TextEditingController();
  TextEditingController _pwdcontroller = new TextEditingController();

  @override
  void initState() {
    super.initState();
    setState(() {
      _phonecontroller.text = 'admin';
      _pwdcontroller.text = '123456';
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
        appBar: new AppBar(
          title: new Text(
            '嫡清医疗',
            textAlign: TextAlign.center,
            style: TextStyle(color: Colors.white),
          ),
        ),
        body: new GestureDetector(
          child: new Stack(fit: StackFit.expand, children: <Widget>[
            new Column(
              mainAxisSize: MainAxisSize.max,
              mainAxisAlignment: MainAxisAlignment.start,
              children: <Widget>[
                _buildIcon(),
                _buildUserName(),
                _buildPassword(),
                _buildSubmit(context)
              ],
            ),
            _loadingContainer()
          ]),
          onTap: () {
            FocusScope.of(context).requestFocus(FocusNode());
          },
        ));
  }

  Future<bool> _login(BuildContext context) async {
    String userName = _phonecontroller.text;
    String password = _pwdcontroller.text;

    if (null == userName || userName.isEmpty) {
      showMsg('请输入用户名');
      return false;
    }

    if (null == password || password.isEmpty) {
      showMsg('请输入密码');
      return false;
    }

    setState(() {
      _isLoading = true;
    });
    bool result;

    try {
      Map _params = {'userName': userName, 'password': password};
      print('请求报文:body:$_params');
      print('请求报文:headers:$headers');
      print('请求URL:LoginUrl:$LoginUrl');
      result = await http
          .post(LoginUrl, body: _params, headers: headers)
          .then((http.Response response) {
        var data = json.decode(response.body);
        print('响应报文:$data');
        String rescode = data[RESP_CODE];
        String resMsg = data[RESP_MSG];
        if (rescode != SUCCESS) {
          String _msg = '登录失败';
          if (resMsg != null) {
            _msg = "登录失败[$resMsg]";
          }
          setState(() {
            _isLoading = false;
          });
          showMsg(_msg);
        } else {
          setState(() {
            var content = data[RESP_DATA];
            String _token = content['token'].toString();
            headers['token'] = _token;
          });
          var _duration = new Duration(seconds: 1);
          new Future.delayed(_duration, () {
            setState(() {
              _isLoading = false;
            });
            router.navigateTo(context, '/home', replace: true);
            showMsg("  登录成功！ ");
          });
        }
      });
    } catch (e) {
      setState(() {
        _isLoading = false;
      });
      showMsg("  网络异常 ");
      return result;
    }

    return result;
  }

  Widget _buildIcon() {
    return new Padding(
        padding: new EdgeInsets.all(30.0),
        child: new Image.asset(
          'images/ic_launcher.png',
          scale: 1.2,
        ));
  }

  Widget _buildSubmit(BuildContext context) {
    return new Container(
      width: 340.0,
      child: new Card(
        color: appColor,
        elevation: 16.0,
        child: new FlatButton(
          child: new Padding(
            padding: new EdgeInsets.all(10.0),
            child: new Text(
              '登录',
              style: new TextStyle(color: Colors.white, fontSize: 16.0),
            ),
          ),
          onPressed: () => _login(context),
        ),
      ),
    );
  }

  Widget _buildPassword() {
    return new Padding(
      padding: new EdgeInsets.fromLTRB(20.0, 15.0, 20.0, 40.0),
      child:
          new Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
        new Padding(
          padding: new EdgeInsets.fromLTRB(0.0, 0.0, 5.0, 0.0),
          child: new Image.asset(
            'images/icon_password.png',
            width: 40.0,
            height: 40.0,
            fit: BoxFit.fill,
            color: appColor,
          ),
        ),
        new Expanded(
          child: new TextField(
            controller: _pwdcontroller,
            decoration: new InputDecoration(
              hintText: '请输入密码',
              suffixIcon: new IconButton(
                icon: new Icon(Icons.clear, color: appColor),
                onPressed: () {
                  _pwdcontroller.clear();
                },
              ),
            ),
            obscureText: true,
          ),
        ),
      ]),
    );
  }

  Widget _buildUserName() {
    return new Padding(
      padding: new EdgeInsets.fromLTRB(20.0, 0.0, 20.0, 15.0),
      child: new Stack(
        alignment: new Alignment(1.0, 1.0),
        //statck
        children: <Widget>[
          new Row(mainAxisAlignment: MainAxisAlignment.spaceEvenly, children: [
            new Padding(
              padding: new EdgeInsets.fromLTRB(0.0, 0.0, 5.0, 0.0),
              child: new Image.asset(
                'images/icon_username.png',
                width: 40.0,
                height: 40.0,
                fit: BoxFit.fill,
                color: appColor,
              ),
            ),
            new Expanded(
              child: new TextField(
                controller: _phonecontroller,
                keyboardType: TextInputType.text,
                decoration: new InputDecoration(
                  hintText: '请输入用户名',
                ),
              ),
            ),
          ]),
          new IconButton(
            icon: new Icon(Icons.clear, color: appColor),
            onPressed: () {
              _phonecontroller.clear();
            },
          ),
        ],
      ),
    );
  }

  Widget _loadingContainer() {
    return !_isLoading
        ? SizedBox(height: 1.0)
        : Container(
            constraints: BoxConstraints.expand(),
            color: Colors.black12,
            child: Center(
              child: Opacity(
                opacity: 0.9,
                child: SpinKitCircle(
                  color: Colors.blueAccent,
                  size: 50.0,
                ),
              ),
            ));
  }
}

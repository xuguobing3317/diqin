import 'package:flutter/material.dart';
import 'package:pulltorefresh_flutter/pulltorefresh_flutter.dart';
import 'package:dq_app/common/AppConst.dart';
import 'package:dq_app/common/AppUrl.dart';
import 'package:dq_app/common/DateUtil.dart';
import 'package:http/http.dart' as http;
import 'package:fluttertoast/fluttertoast.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'dart:convert';
import 'dart:async';

class TodayViewPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => TodayViewPageWidgetState();
}

class TodayViewPageWidgetState extends State<TodayViewPage>
    with TickerProviderStateMixin {
  List<Map> _itemMap = new List<Map>(); //当前页面的记录
  List<Map> _queryItemMap = new List<Map>(); //每次查询的列表
  ScrollController _scrollController = ScrollController();
  int _page = 1; //加载的页数
  String loadingFlag = '1'; //1:加载中 2：加载到数据  3：无数据
  int _rows = 10; //每页条数

  int total = -1;
  bool totalFlag = false;

  ScrollController controller = new ScrollController();
  ScrollPhysics scrollPhysics = new RefreshAlwaysScrollPhysics();
  String customRefreshBoxIconPath = "images/icon_arrow.png";
  AnimationController customBoxWaitAnimation;
  int rotationAngle = 0;
  String customHeaderTipText = "松开加载！";
  String defaultRefreshBoxTipText = "松开加载！";

  ///button等其他方式，通过方法调用触发下拉刷新
  TriggerPullController triggerPullController = new TriggerPullController();

  @override
  void initState() {
    super.initState();
    customBoxWaitAnimation = new AnimationController(
        duration: const Duration(milliseconds: 1000 * 100), vsync: this);
    getData();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text('今日随访'),
        ),
        body: getBody());
  }

  Widget _getCustomHeaderBox() {
    return new Container(
        color: Colors.grey,
        child: new Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            new Align(
              alignment: Alignment.centerLeft,
              child: new RotatedBox(
                quarterTurns: rotationAngle,
                child: new RotationTransition(
                  //布局中加载时动画的weight
                  child: new Image.asset(
                    customRefreshBoxIconPath,
                    height: 45.0,
                    width: 45.0,
                    fit: BoxFit.cover,
                  ),
                  turns: new Tween(begin: 100.0, end: 0.0)
                      .animate(customBoxWaitAnimation)
                        ..addStatusListener((animationStatus) {
                          if (animationStatus == AnimationStatus.completed) {
                            customBoxWaitAnimation.repeat();
                          }
                        }),
                ),
              ),
            ),
            new Align(
              alignment: Alignment.centerRight,
              child: new ClipRect(
                child: new Text(
                  customHeaderTipText,
                  style: new TextStyle(fontSize: 18.0, color: appColor),
                ),
              ),
            ),
          ],
        ));
  }

  Widget itemCard(int i) {
    String _name = _itemMap[i]['userName'];
    int _age = _itemMap[i]['age'];
    String _mobile = _itemMap[i]['mobile']==null?"-":_itemMap[i]['mobile'];
    String _weixin = _itemMap[i]['weixin']==null?"-":_itemMap[i]['weixin'];
    String _remark = _itemMap[i]['remark']==null?"-":_itemMap[i]['remark'];
    String _lastAccessTime =
        _itemMap[i]['lastAccessTime'] == null ? "-" : _itemMap[i]['lastAccessTime'];
    


    return new Card(
        child: InkWell(
            onTap: () {},
            child: new ListTile(
              title: new Text('$_name ($_age岁)',
              style: TextStyle(fontSize: 18.0),),
              subtitle: new Container(
                child: new Column(
                  children: <Widget>[
                    new Row(
                      children: <Widget>[
                        Expanded(
                          child: Text(
                            '手机:$_mobile',
                            style: TextStyle(fontSize: 15.0),
                          ),
                        ),
                        Expanded(
                          child: Text(
                            '微信:$_weixin',
                            style: TextStyle(fontSize: 15.0),
                          ),
                        )
                      ],
                    ),
                    new Row(
                      children: <Widget>[
                        Expanded(
                          child: Text(
                            '上次随访时间:$_lastAccessTime',
                            style: TextStyle(fontSize: 15.0),
                          ),
                        ),
                      ],
                    ),
                    new Row(
                      children: <Widget>[
                        Expanded(
                          child: Text(
                            '备注:$_remark',
                            style: TextStyle(fontSize: 15.0),
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
              leading: Icon(Icons.person, size: 50.0),
            )));
  }

  void _handleStateCallback(AnimationStates animationStates,
      RefreshBoxDirectionStatus refreshBoxDirectionStatus) {
    switch (animationStates) {
      //RefreshBox高度达到50,上下拉刷新可用;RefreshBox height reached 50，the function of load data is  available
      case AnimationStates.DragAndRefreshEnabled:
        setState(() {
          //3.141592653589793是弧度，角度为180度,旋转180度；3.141592653589793 is radians，angle is 180⁰，Rotate 180⁰
          rotationAngle = 2;
        });
        break;

      //开始加载数据时；When loading data starts
      case AnimationStates.StartLoadData:
        setState(() {
          customRefreshBoxIconPath = "images/refresh.png";
          customHeaderTipText = "加载.....";
        });
        customBoxWaitAnimation.forward();
        break;

      //加载完数据时；RefreshBox会留在屏幕2秒，并不马上消失，这里可以提示用户加载成功或者失败
      // After loading the data，RefreshBox will stay on the screen for 2 seconds, not disappearing immediately，Here you can prompt the user to load successfully or fail.
      case AnimationStates.LoadDataEnd:
        customBoxWaitAnimation.reset();
        setState(() {
          rotationAngle = 0;
          if (refreshBoxDirectionStatus == RefreshBoxDirectionStatus.PULL) {
            customRefreshBoxIconPath = "images/icon_ok.png";
            customHeaderTipText = "刷新成功";
          } else if (refreshBoxDirectionStatus ==
              RefreshBoxDirectionStatus.PUSH) {
            customRefreshBoxIconPath = "images/icon_ok.png";
            if (totalFlag) {
              customHeaderTipText = "没有更多数据了";
            } else {
              customHeaderTipText = "加载成功！";
            }
          }
        });
        break;

      //RefreshBox已经消失，并且闲置；RefreshBox has disappeared and is idle
      case AnimationStates.RefreshBoxIdle:
        setState(() {
          rotationAngle = 0;
          defaultRefreshBoxTipText = customHeaderTipText = "松开加载";
          customRefreshBoxIconPath = "images/icon_arrow.png";
        });
        break;
    }
  }

  Future _loadData(bool isPullDown) async {
    if (!isPullDown) {
      setState(() {
        if (_itemMap.length == total) {
          totalFlag = true;
        } else {
          totalFlag = false;
          _page++;
        }
      });
      if (_itemMap.length != total) {
        toGetData(isPullDown);
      }
    } else {
      setState(() {
        totalFlag = false;
        _page = 1;
      });
      toGetData(isPullDown);
    }
  }

  Future toGetData(isPullDown) async {
    await getHttpData().then((_v) {
      setState(() {
        if (isPullDown) {
          _itemMap.clear();
        }
        _itemMap.addAll(_queryItemMap);
      });
    });
  }

  Widget getBody2() {
    return new PullAndPush(
      defaultRefreshBoxTipText: defaultRefreshBoxTipText,
      headerRefreshBox: _getCustomHeaderBox(),
      footerRefreshBox: _getCustomHeaderBox(),
      triggerPullController: triggerPullController,
      animationStateChangedCallback: (AnimationStates animationStates,
          RefreshBoxDirectionStatus refreshBoxDirectionStatus) {
        _handleStateCallback(animationStates, refreshBoxDirectionStatus);
      },
      listView: new ListView.builder(
          //ListView的Item
          itemCount: _itemMap.length, //+2,
          controller: controller,
          physics: scrollPhysics,
          itemBuilder: (BuildContext context, int index) {
            return itemCard(index);
          }),
      loadData: (isPullDown) async {
        await _loadData(isPullDown);
      },
      scrollPhysicsChanged: (ScrollPhysics physics) {
        //这个不用改，照抄即可；This does not need to change，only copy it
        setState(() {
          scrollPhysics = physics;
        });
      },
    );
  }

  Widget getBody() {
    if (loadingFlag == '1') {
      return new Stack(children: <Widget>[
        new Padding(
          padding: new EdgeInsets.fromLTRB(0.0, 0.0, 0.0, 35.0),
          child: new Center(
            child: SpinKitFadingCircle(
              color: appColor,
              size: 30.0,
            ),
          ),
        ),
        new Padding(
          padding: new EdgeInsets.fromLTRB(0.0, 35.0, 0.0, 0.0),
          child: new Center(
            child: new Text(
              '列表加载中...',
              style: TextStyle(color: appColor),
            ),
          ),
        ),
      ]);
    } else if (loadingFlag == '2') {
      return getBody2();
    } else {
      return new Stack(
        children: <Widget>[
          new Padding(
            padding: new EdgeInsets.fromLTRB(0.0, 35.0, 0.0, 0.0),
            child: new Center(
              child: new Text(
                '未查询到数据',
                style: TextStyle(color: appColor),
              ),
            ),
          ),
        ],
      );
    }
  }

  @override
  void dispose() {
    super.dispose();
    _scrollController.dispose();
  }

  Future<bool> getData() async {
    setState(() {
      loadingFlag = "1";
    });
    bool result = false;
    result = await getHttpData().then((_v) {
      setState(() {
        _itemMap.addAll(_queryItemMap);
        if (_itemMap.length > 0) {
          loadingFlag = "2";
        } else {
          loadingFlag = "3";
        }
      });
    });
    return result;
  }

  Future<bool> getHttpData() async {
    _queryItemMap = new List<Map>();
    bool result = false;
    String accessTime = DateUtil.formatDateTime(
        DateUtil.getNowDateStr(), DateFormat.YEAR_MONTH_DAY, null, null);
    Map<String, String> _params = {
      'pageSize': _rows.toString(),
      'pageNum': _page.toString(),
      'accessTime': accessTime
    };
    debugPrint('url:$PersonListUrl');
    debugPrint('body:$_params');
    debugPrint('headers:$headers');

    result = await http
        .post(PersonListUrl, body: _params, headers: headers)
        .then((http.Response response) {
      var data = json.decode(response.body);
      String rescode = data[RESP_CODE];
      String resMsg = data[RESP_MSG];
      print('$resMsg[$rescode]');
      if (rescode != SUCCESS) {
        String _msg = '未查询到数据[$resMsg]';
        Fluttertoast.showToast(
            msg: _msg,
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.BOTTOM,
            timeInSecForIos: 1,
            backgroundColor: Color(0xFF499292),
            textColor: Color(0xFFFFFFFF));
      } else {
        setState(() {
          Map<String, dynamic> _dataMap = data[RESP_DATA];
          List _listMap = _dataMap['content'];
          _listMap.forEach((item) {
            _queryItemMap.add(item);
          });
          total = _dataMap['totalElements'];
        });
      }
    });
    return result;
  }
}

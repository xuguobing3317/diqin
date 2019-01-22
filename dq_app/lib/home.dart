import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';

class HomePage extends StatefulWidget {
  HomePage();
  @override
  State<StatefulWidget> createState() {
    return new HomePageState();
  }
}

class HomePageState extends State<HomePage> {
  int _currentIndex = 0;
  var _pageController = new PageController(initialPage: 0);
  var _bottomText = ['今日随访', '所有病人'];
  var _bottomIcons = [
    [
      new Icon(Icons.category, color: Colors.grey),
      new Icon(Icons.category, color: Colors.blue),
    ],
    [
      new Icon(Icons.account_circle, color: Colors.grey),
      new Icon(Icons.account_circle, color: Colors.blue),
    ]
  ];

  Icon changeIconStyle(int curIndex) {
    if (curIndex == _currentIndex) {
      return _bottomIcons[curIndex][1];
    }
    return _bottomIcons[curIndex][0];
  }

  Text changeTextStyle(int curIndex) {
    if (curIndex == _currentIndex) {
      return new Text(_bottomText[curIndex],
          style: new TextStyle(color: Colors.blue));
    } else {
      return new Text(_bottomText[curIndex],
          style: new TextStyle(color: Colors.grey));
    }
  }


  Text changeBannerTitle() {
      return new Text(_bottomText[_currentIndex], style: TextStyle(color: Colors.white),);
  }

  void _pageChange(int index) {
    setState(() {
      if (_currentIndex != index) {
        _currentIndex = index;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: changeBannerTitle(),
      ),
      body: new PageView.builder(
        onPageChanged: _pageChange,
        controller: _pageController,
        itemBuilder: (BuildContext context, int index) {
          var str =
              (index == 0) ? "这里是【HomePage】->【签到】页面" : "这里是【HomePage】->【我】页面";
          return new Center(
            child: new Container(
              width: 340.0,
              child: new Card(
                color: Colors.blue,
                elevation: 16.0,
                child: new FlatButton(
                  onPressed: (){},
                  child:
                      new Text(str, style: new TextStyle(color: Colors.white)),
                ),
              ),
            ),
          );
        },
        itemCount: _bottomText.length,
      ),
      bottomNavigationBar: new BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        items: [
          new BottomNavigationBarItem(
              icon: changeIconStyle(0), title: changeTextStyle(0)),
          new BottomNavigationBarItem(
              icon: changeIconStyle(1), title: changeTextStyle(1)),
        ],
        //设置当前的索引
        currentIndex: _currentIndex,
        //tabBottom的点击监听
        onTap: (int index) {
//          _pageController.jumpToPage(index); //没有动画的页面切换
          _pageController.animateToPage(index,
              duration: new Duration(seconds: 2),
              curve: new ElasticOutCurve(0.8));
          _pageChange(index);
          _currentIndex = index;
        },
      ),
    );
  }
}
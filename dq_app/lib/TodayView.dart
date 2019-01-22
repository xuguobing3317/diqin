import 'package:flutter/material.dart';

class TodayViewPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => TodayViewPageWidgetState();
}

class TodayViewPageWidgetState extends State<TodayViewPage> {
  List<Widget> list = List();

  @override
  void initState() {
    
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title:Text('今日随访'),
        ),
    );
  }
}
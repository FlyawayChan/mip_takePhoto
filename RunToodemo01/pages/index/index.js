//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
      "userName":"小明",
      "age":18,
      "lists":[
          {
              "id":1,
              "name":"switch1"
          },{
              "id": 2,
              "name": "switch2"
          },{
              "id": 3,
              "name": "switch3"
          }
      ]
  },
  onLoad: function () {
  
  },
  tapEvent:function(event){
      var lists = this.data.lists;//1、this相当于获取的是这个页面对象Page
      lists.splice(0,0,{//2、往lists加入一个json类型的按钮数据
          id:4,
          name:'switch4'
      });
      this.setData({//3、记住要使用this.setData来刷新数据
          lists:lists
      });
  }
})

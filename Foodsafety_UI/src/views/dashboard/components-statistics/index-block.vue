<template>
  <div class="container" style="position: relative;width: 100%;height: 100%;padding-top: 20%">
    <div class="circle">
      <!--左半边圆-->
      <div class="circle_left" ref="circleLeft"></div>
      <!--右半边圆-->
      <div class="circle_right"></div>
      <div class="mask"></div>
    </div>
    <div ref="showData" class="show-data">
      <div>{{formatterNumber(countData)}}</div>
      <div>{{title}}</div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "index-block",
      props: {
        color : {
          default : ""
        },
        countData : {
          default : 0
        },
        title : {
          default : ""
        },
      },
      data(){
          return {
            timer:null
          };
      },
      mounted (){
        this.$refs.circleLeft.style.background = this.color;
        this.$refs.showData.style.color = this.color;
        this.changeSize();
        //监听屏幕的改变
        window.onresize = () => {
          this.changeSize();
        }
      },
      methods: {
          //自适应方法
        changeSize(){
          let _self = this;
          _self.timer = window.setInterval(function(){
            var width = $(".container").width()*0.5;
            if(width!==$(".container").width()*0.5){
              width = $(".container").width()*0.5;
            }else{
              $(".circle").css({"height":width,"width":width});
              $(".show-data").css({"height":"100%","width":width,"position": "absolute","left":"50%","top":"35%"});
              $(".circle_left").css({"height":width,"width":width, "clip":"rect(0,"+width*0.6+"px,auto,0)"});
              $(".circle_right").css({"height":width,"width":width,"clip":"rect(0,auto,auto,"+width*0.6+"px)"});
              $(".mask").css({"height":width*0.95,"width":width*0.95,"left":"2.7%","top":"2.7%"});
             _self.clearTime();
            }
          },100)
        },
        //清除定时器
        clearTime(){
          var _self = this;
          window.clearInterval(_self.timer);
        },
        //数据格式化
        formatterNumber(value){
          var count = parseInt(value);
          if(count>9999){
            //判断能否被10000整除
            if(String(count/10000).indexOf(".")>-1){//不能整除
              return parseInt(count/10000)+'万+';
            }else{
              return parseInt(count/10000)+'万';
            }
          }else{
          return count;
          }
        }
      }

    }


</script>

<style scoped >
  .circle{
    position: absolute;
  }
  .circle_left, .circle_right{
    position: absolute;
    border-radius: 50%;
    top: 0px;
    left: 0px;
  }
  /*出于展示用的改变背景色*/
  .circle_right{
      background:  #073B57;
  }


  /*
  *当top和left取值为auto时，相当于0
  *当bottom和right取值为auto时，相当于100%
  */
  .mask {
    border-radius: 50%;
    background: #102133;
    position: absolute;
  }
  .show-data{
    position: relative;
  }
  .show-data>div:nth-child(1){
    font-size:1.8vh;
  }
  .show-data>div:nth-child(2){
    font-size: 1.3vh;
  }
</style>

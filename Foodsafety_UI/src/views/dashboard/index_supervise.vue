<template>
  <div class="main-index background_img"  :class="[{all_full_screen:getfullscreenPath=='/dashboard_full'},mDashboard]">
      <el-row  style="height: 66%;padding-left: 2px;">
        <el-col :span="5" >
          <el-row  class="div-block  relative_box" >
            <el-col style="padding:1.3%" :class="[{chart_samll:getfullscreenPath=='1'}]" >
              <div class="relative_box border_line " >

                <div class="all_btn" v-if="getfullscreenPath==false">
                      <a  href="" target="_blank"  ref="target"></a>
                      <span  @click="screen_all" class="btn_color big_btn">全屏</span>
                </div>
                <!-- <div class="each_btn btn_color" v-if="getfullscreenPath==false" >
                     <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'1'}}" class="" >全屏111</router-link>
                </div> -->
                <index-sensor-gauge ref="echarts1"></index-sensor-gauge>
              </div>
            </el-col>
          </el-row>
          <el-row  class="div-block ">
            <el-col class="item_box" style="padding:1.3% " :class="[{chart_samll:getfullscreenPath=='2'}]">
              <div class="relative_box border_line " >
               <!-- <div class="each_btn"  v-if="getfullscreenPath==false" >
                     <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'2'}}" class="btn_color" >全屏2222</router-link>
                </div> -->
              <index-video-pie ref="echarts2"></index-video-pie>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :span="12" class="relative_box title_bg  "   :class="[{chart_samll:getfullscreenPath=='3'}]">
             <!-- <div class="each_btn"  v-if="getfullscreenPath==false" >
                  <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'3'}}" class="btn_color" >全屏0000</router-link>
              </div> -->
              <img class="p_lt" src="../../assets/full_screen/lt.png" alt="">
              <img class="p_rt" src="../../assets/full_screen/rt.png" alt="">
              <img class="p_lb" src="../../assets/full_screen/lb.png" alt="">
              <img class="p_rb" src="../../assets/full_screen/rb.png" alt="">
              <el-row style="height: 10%;width: 100%;">
                <el-col style="padding-left:2px;padding-top: 2px;">
                  <!-- <div class="title">松江区食品安全监管平台</div> -->
                    <img class="title_img" src="../../assets/full_screen/title.png" alt="title">
                </el-col>
              </el-row>
              <el-row style="height: 88%;width: 96%;margin:0 auto">
                <el-col style="padding-left:2px;">
                  <index-map></index-map>
                </el-col>
              </el-row>
        </el-col>
        <el-col :span="2" style="padding:0.4% 0.3%" >
          <div   class="relative_box border_line ">
          <el-row class="block">
            <el-col >
              <div class="circle_div">
                <router-link to="/admin/enterprise/query">
                  <index-block :color="bolckData.color1" :countData="bolckData.data1" :title="bolckData.title1" ></index-block>
                </router-link>
              </div>
              <div style="height: 10px;border-bottom: 1px #0C3048 solid;width: 95%;margin-top: 20px;"></div>
            </el-col>
          </el-row>
          <el-row class="block">
            <el-col>
              <div class="circle_div">
                <router-link to="/admin/enterprise/register/verify">
                  <index-block :color="bolckData.color2" :countData="bolckData.data2" :title="bolckData.title2" ></index-block>
                </router-link>
              </div>
              <div style="height: 10px;border-bottom: 1px #0C3048 solid;width: 95%;margin-top: 20px;"></div>
            </el-col>
          </el-row>
          <el-row class="block">
            <el-col>
              <div class="circle_div">
                <index-block :color="bolckData.color3" :countData="bolckData.data3" :title="bolckData.title3" ></index-block>
              </div>
              <div style="height: 10px;border-bottom: 1px #0C3048 solid;width: 95%;margin-top: 20px;"></div>
            </el-col>
          </el-row>
          <el-row class="block">
            <el-col>
              <div class="circle_div">
                <index-block :color="bolckData.color4" :countData="bolckData.data4" :title="bolckData.title4" ></index-block>
              </div>
            </el-col>
          </el-row>
          </div>
        </el-col>
        <el-col :span="5" >
          <el-row  class="div-block" >
            <el-col  class="item_box"  style="padding:1.3% "  :class="[{chart_samll:getfullscreenPath=='4'}]">
              <div  class="relative_box border_line" >
                <!-- <div class="each_btn"  v-if="getfullscreenPath==false" >
                      <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'4'}}" class="btn_color" >全屏7777</router-link>
                  </div> -->
                <index-grid  ref="echarts3"></index-grid>
              </div>
            </el-col>
          </el-row>
          <el-row  class="div-block">
            <el-col  class="item_box"  style="padding:1.3% "  :class="[{chart_samll:getfullscreenPath=='5'}]">
              <div  class="relative_box border_line" >
                  <!-- <div class="each_btn"  v-if="getfullscreenPath==false" >
                            <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'5'}}" class="btn_color" >全屏888</router-link>
                    </div> -->
                  <index-supervise ref="echarts4"></index-supervise>
              </div>
            </el-col>
          </el-row>
        </el-col> 
      </el-row>
      <el-row  style="height: 33%;padding-left: 2px;">
        <el-col :span="5" class="item_box"   :class="[{chart_samll:getfullscreenPath=='9'}]">
      <!--    <div class="table-div" style="text-align:center;background-color:#102133;height: 100%;width: 100%">
            <div class="table-title">安全事务完成率</div>
            <index-table></index-table>
          </div>-->
          <div  class="relative_box border_line" >
            <!-- <div class="each_btn"  v-if="getfullscreenPath==false" >
              <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'9'}}" class="btn_color" >全屏3333</router-link>
             }
            </div> -->
            <index-alarmTreatedRate ref="echarts5" ></index-alarmTreatedRate>
          </div>
        </el-col>
        <el-col :span="7"  class="item_box"  :class="[{chart_samll:getfullscreenPath=='6'}]">
          <div  class="relative_box border_line" >
            <!-- <div class="each_btn"  v-if="getfullscreenPath==false" >
              <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'6'}}" class="btn_color" >全屏444</router-link>
            </div> -->
            <index-sensor-trend ref="echarts6" ></index-sensor-trend>
          </div>
        </el-col>
        <el-col :span="7"  class="item_box"   :class="[{chart_samll:getfullscreenPath=='7'}]">
           <div  class="relative_box border_line" >
          <!-- <div class="each_btn"  v-if="getfullscreenPath==false" >
            <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'7'}}" class="btn_color" >全屏5555</router-link>
          </div> -->
          <index-alarm ref="echarts7"></index-alarm>
           </div>
        </el-col>
        <el-col :span="5" class="item_box"   :class="[{chart_samll:getfullscreenPath=='8'}]">
           <div  class="relative_box border_line" >
          <!-- <div class="each_btn"  v-if="getfullscreenPath==false" >
            <router-link tag="a" target="_blank" :to="{name:'dashboard_full',query:{id:'8'}}" class="btn_color" >全屏9999</router-link>
          </div> -->
          <index-warning ref="echarts8"></index-warning>
           </div>
        </el-col>
      </el-row>
    </div>
</template>

<script>
  import {getEnterpriseTotal,getRegisterTotal} from 'api/admin/dashboard/index';
  import { loadDashboardHeight } from "api/screen";
  import { mapGetters } from "vuex";
    export default {
      name: "index_supervise",
      components: {
        "index-alarm": () => import("./components-statistics/index-alarm"),
        "index-map": () => import("./components-statistics/index-map"),
        "index-block": () => import("./components-statistics/index-block"),
        "index-supervise": () => import("./components-statistics/index-supervise"),
        // "index-subject": () => import("./components-statistics/index-subject"),
        "index-grid": () => import("./components-statistics/index-grid"),
        "index-table": () => import("./components-statistics/index-table"),
        "index-alarmTreatedRate": () => import("./components-statistics/index-alarmTreatedRate"),
        // "index-inspection": () => import("./components-statistics/index-inspection"),
        // "index-score":() => import("./components-statistics/index-score"),
        "index-warning":() => import("./components-statistics/index-warning"),
        "index-sensor-gauge":() => import("./components-statistics/index-sensor-gauge"),
        "index-sensor-trend":() => import("./components-statistics/index-sensor-trend"),
        "index-video-pie": () => import("./components-statistics/index-video-pie"),
      },
      data(){
          return {
            bolckData:{
              color1:'#1FDDB4',
              color2:'#F2D357',
              color3:'#00BFFF',
              color4:'#919BEE',
              data1:"1",
              data2:"2",
              data3:"20",
              data4:"12",
              title1:"企业总数",
              title2:"待审企业数",
              title3:"监控点位数",
              title4:"在线点位数",
            },
            isfull:true,
            chart_map:{
              all:{isfull:true},
              left1:{isfull:false}
            }

          }
      },
      computed: {
        ...mapGetters(["user", "staticData"]),
        mDashboard(){
          return loadDashboardHeight();
        },
        getfullscreenPath(){
          if(this.$route.query.id){
             return this.$route.query.id
          }else if(this.$route.path=='/dashboard_full'){
              return '/dashboard_full'
          }else{
            return false
          }

        }
      },
      created(){
        this.getTotalCountInit();
      },
      mounted () {
        let _self=this;
        //设置首页样式
        // $(".main-container").css({height:"100%"});
        $(".main-container").css({height:"100%",backgroundColor:"#000C43"});
        $("#app").css({height:"100%"});
        //监听菜单栏收缩事件
        var timer = [];
        $(window).resize(function(){
          timer.push(window.setInterval(function (){
            let width = $(".main-menu-container").width();
            try{
              if(width!==$(".main-menu-container").width()){
                width = $(".main-menu-container").width();
              }else {
                resizeEcharts();
                clearTime()
              }
            }catch (e){
              clearTime()
            }
          },100));
        });
        function clearTime(){
          timer.forEach(item=>{
            window.clearInterval(item);
          })
          timer=[];
        };
        //重新设置echarts的大小
        function resizeEcharts(){
          _self.$refs.echarts1.resizeEchart();
          _self.$refs.echarts2.resizeEchart();
          _self.$refs.echarts3.resizeEchart();
          _self.$refs.echarts4.resizeEchart();
          _self.$refs.echarts5.resizeEchart();
          _self.$refs.echarts6.resizeEchart();
          _self.$refs.echarts7.resizeEchart();
          _self.$refs.echarts8.resizeEchart();
        };
      },
      methods:{
        screen_all(){
            let target = this.$refs.target
            target.setAttribute('href', window.location.origin + '/#/dashboard_full')
            target.click()
        },
        test2(){
            // console.log(this.$router.path);
            // location.href="http://www.baidu.com"
            // window.open("http://localhost:8080/#/dashboard","newWindow","menubar=0,scrollbars=1, resizable=1,status=1,titlebar=0,toolbar=0,location=1");
                // this.$router.push({
                //      name:'dashboard',
                // })
        },
        getTotalCountInit(){
          getEnterpriseTotal({finalAreaId:this.user.areaId,enterpriseStatus:"3"}).then(response => {
            this.bolckData.data1=response;
          })
          getRegisterTotal({areaId:this.user.areaId,registerStatus:"0"}).then(response => {
            this.bolckData.data2=response;
          })
        },
      }
    }
</script>

<style scoped>
  .main-index{
    overflow: hidden;
  }
  .el-col{
    height: 100%;
  }

  .div-block{
    width: 100%;
    height: 50%;
    /*text-align: center;*/
  }
  .circle_div{
    height: 75%;
    width: 100%;
    margin-top: 10%;
  }
  .title{
    font-size: 3.2vh;
    letter-spacing:1px;
    color: #a4feff;
    font-weight: bold;
    height: 100%;
    text-align: center;
    background-color: #102133;
  }
  .block{
    width: 100%;
    height: 25%;
    margin-left: 5px;
    text-align: center;
  }
  .table-title{
    font-size: 1.6vh;
    font-weight: bolder;
    color:#3cfdff;
    text-align: center;
    line-height: 200%;
  }

  /* 全屏样式 开始*/
  .relative_box{
    position: relative;
  }
  .border_line{
      width:100%;height:100%;box-sizing: border-box; border:1px solid rgb(16, 61, 121) ;
      /* border-radius: 5px; */
      padding:7px;
  }
  .item_box{
    padding:0.3%;
  }
  .all_btn{
    position: absolute;
    top:0;
    left:0;
    padding:4px 8px;
    z-index: 100;
    cursor: pointer;
  }
  .each_btn{
    position: absolute;
    top:0;
    right:0;
    z-index: 100;

  }
  .big_btn{
    font-size: 2.0vh;
    font-weight: 700;
  }
  .btn_color{
    display: inline-block;
    padding:4px 15px;
     color:rgb(203, 209, 209);
     opacity: 0;
     transition:all 0.3s linear;
  }
  .btn_color:hover{
     opacity: 1;
     color:#3cfdff;
  }
  .all_full_screen{
    position: fixed;
    height: 100%;
    z-index: 99955;
    width: 100%;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    padding-top: 16px;
    background: #001059 ;
  }

.chart_samll{
    position: fixed;
    width: 100%;
    height: auto;
    z-index: 88888;
    padding: 5px;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: #001059 ;
}

.background_img{
  background: url("../../assets/full_screen/bg.png");
  background-repeat: no-repeat;
  background-size: cover;
  -webkit-background-size: cover;
  -o-background-size: cover;
  background-position: center 0;
}
.title_img{
    height: 64%;
    text-align: center;
    width:96%;
    margin: 0 auto;
    padding-top: 5px;
}
.title_bg{
  background: url("../../assets/full_screen/title_bg.png");
  background-repeat: no-repeat;
  background-size: 99.5% 100.5%;
  -webkit-background-size: 99.5% 100.5%;
  -o-background-size:  99.5% 100.5%;
  background-position: center;
}

.p_lt{position: absolute;top: 0.5%;left: 0.7%;}
.p_rt{position: absolute;top: 0.5%;right: 0.8%;}
.p_lb{position: absolute;bottom: 1.4%;left: 0.7%;}
.p_rb{position: absolute;bottom: 1.4%;right:0.8%;}
  /* 全屏样式 结束*/

</style>

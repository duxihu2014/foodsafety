<template>
  <div style="height: 100%">
    <div class="table_title">视频及IOT报警事件</div>
    <div class="table-main" v-on:mouseout="Up()" v-on:mouseover="Stop()">
      <table class="table" id="table1">
        <template v-for="(item, index) in tableData">
          <tr>
            <td><img :src="eventTypeColor(item.eventType)"></td>
            <td><div class="ename">{{item.equName}}</div></td>
            <td>{{eventTypeFormatter(item)}}</td>
            <td><div :class="eventStatusColorFormatter(item.status)">{{eventStatusFormatter(item.status)}}</div></td>
            <td class="time">{{item.alarmTime}}</td>
          </tr>
        </template>
      </table>
      <table class="table" id="table2">
        <template v-for="(item, index) in tableData">
          <tr>
            <td><img :src="eventTypeColor(item.eventType)"></td>
            <td><div class="ename">{{item.equName}}</div></td>
            <td>{{eventTypeFormatter(item)}}</td>
            <td><div :class="eventStatusColorFormatter(item.status)">{{eventStatusFormatter(item.status)}}</div></td>
            <td class="time">{{item.alarmTime}}</td>
          </tr>
        </template>
      </table>
    </div>
  </div>
</template>

<script>
  import fetch from 'utils/fetch';
  import { mapGetters } from "vuex";
  export default {
    name: "VideoAlarmList",
    mounted (){
      this.getData();
      let _self =this;
      _self.timeId =window.setInterval(function(){
        _self.ScrollUp()
      },_self.speed);
    },
    destroyed(){
      window.clearInterval(this.timeId);
    },
    data(){
      return {
        tableData: [],
        speed: 40,
        timeId: undefined
      }
    },
    computed: {
      ...mapGetters(["user"]),
    },
    methods: {
      getData() {
        let isEnt = this.user['enterpriseId'];
        let url = isEnt
          ?`/api/admin/alert/list?page=1&limit=20&enterpriseId=${this.user['enterpriseId']}`
          :'/api/admin/alert/list?page=1&limit=20';
        fetch({
          url,
          method: 'get',
        }).then(data => {
          this.tableData = data.rows;
        }).catch(err =>{
          this.$message.error('报警列表数据获取失败！');
        });
      },
      eventTypeColor(eventType) {
        if (eventType === '1'){
          // return require('assets/camera.png');
          return require('assets/new_camera.png');
        }
        else{
          return require('assets/new_humiture.png');
        }
      },
      eventTypeFormatter(item){
        // console.log(item);
        if (item.eventType === '1') {
          return item.content;
        } else {
          switch (item.eventId) {
            case "10001":
              return '温度高';
            case "10002":
              return '温度低';
            case "10003":
              return '湿度高';
            case "10004":
              return '湿度低';
            default:
              return '未知类型';
          }
        }
      },
      eventStatusFormatter(status){
        return status==='1'? '未处理': '已处理';
      },
      eventStatusColorFormatter(status){
        return status==='1'? 'estatusUnDone': 'estatusDone';
      },
      ScrollUp(){
        let scrollDiv = $(".table-main")[0];
        let table1 = $("#table1")[0];
        if(scrollDiv&&table1){
          if( scrollDiv.scrollTop>=table1.scrollHeight){
            scrollDiv.scrollTop=0;
          }else{
            scrollDiv.scrollTop++;
          }
        }
      },
      Stop(){
        window.clearInterval(this.timeId);
      },
      Up(){
        let _self =this;
        _self.timeId =window.setInterval(function(){
          _self.ScrollUp()
        },_self.speed);
      },
    },
  }
</script>

<style scoped>
  .table-main {
    height: 90%;
    /* background-color: #102133; */
    color: #2d8ac7;
    overflow-y: scroll;
    overflow-x: hidden;
  }

  .table-main::-webkit-scrollbar {
    display: none;
  }

  .table_title {
    font-size: 15px;
    font-weight: bolder;
    color:#3cfdff;
    text-align: center;
    /* background-color: #102133; */
    /*padding-bottom: 5%;*/
    height: 10%;
  }

  .table {
    font-size: 14px;
    color: white;
    text-align: center;
    width: 100%;
    height: 100%;
    margin-top: 0;
  }

  .table tr {
    height: 40px;
    line-height: 40px;
    border-bottom: 1px #013968 solid;
    border-left: 1px #013968 solid;
    border-right: 1px #013968 solid;
  }

  .table tr:first-child{
    border-top: 1px #013968 solid;
  }

  table td:first-child {
    /*color: white;*/
    /*font-size: 18px;*/
    /*font-weight: 700;*/
    width: 5%;
    line-height: 10px
  }
  table td:nth-child(2) {
    width: 8%;
    text-align: left;
    max-width: 60px;
  }
  table td:nth-child(3) {
    text-align: left;
    line-height: 24px;
    padding-left: 8px;
    width: 5%;
  }
  table td:nth-child(3):first-line {
    color: #0af;
  }
  table td:nth-child(4) {
    width: 5%;
    text-align: center;
  }
  table td:nth-child(5) {
    width: 10%;
  }
  .ename {
    max-width: 150px;
    white-space:nowrap;
    overflow:hidden;
    text-overflow:ellipsis;
    color: #51d7f8;
  }
  .estatusDone {
    color: green;
    white-space:nowrap;
  }
  .estatusUnDone {
    color: #ff035c;
    white-space:nowrap;
  }


/* 改版 开始 */
.time{
    color: #ff035c;
}
.img_size{
  width:20px !important;
  height:20px !important;
}
/* 改版 结束 */

</style>

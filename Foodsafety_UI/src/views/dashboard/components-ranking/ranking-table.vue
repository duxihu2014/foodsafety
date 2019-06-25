<template>
  <div style="height: 100%">
    <div class="table_title">企业报警排名趋势</div>
    <div class="table-main" v-on:mouseout="Up()" v-on:mouseover="Stop()">
      <table class="table" id="table1">
        <template v-for="(item, index) in tableData">
          <tr>
            <td>{{index+1}}.</td>
            <td><div class="ename">{{item.enterpriseName}}</div></td>
            <td><img style="vertical-align: middle;" :src="rankingIcon(item)"></td>
            <td><div :class="rankChangeCss(item)">{{ rankChange }}</div></td>
          </tr>
        </template>
      </table>
      <table class="table" id="table2">
        <template v-for="(item, index) in tableData">
          <tr>
            <td>{{index+1}}.</td>
            <td><div class="ename">{{item.enterpriseName}}</div></td>
            <td><img style="vertical-align: middle;" :src="rankingIcon(item)"></td>
            <td><div :class="rankChangeCss(item)">{{ rankChange }}</div></td>
          </tr>
        </template>
      </table>
    </div>
  </div>
</template>

<script>
  import { getAlarmRanking } from 'api/admin/dashboard/index';
  import { mapGetters } from "vuex";
  export default {
    name: "ranking-table",
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
        rankChange: undefined,
        speed: 40,
        timeId: undefined
      }
    },
    computed: {
      ...mapGetters(["user"]),
    },
    methods: {
      getData() {
        getAlarmRanking({areaId: this.user.areaId}).then(response => {
          // console.log(response);
          if (response.rel) {
            this.tableData = response.data;
          }
        });
      },
      rankingIcon(item) {
        if (item.rankingLastMonth === 0) {
          return require('assets/right.png');
        } else {
          if (item.ranking < item.rankingLastMonth) {
            return require('assets/up.png');
          } else if (item.ranking > item.rankingLastMonth) {
            return require('assets/down.png');
          } else {
            return require('assets/right.png');
          }
        }
      },
      rankChangeCss(item){
        if (item.rankingLastMonth === 0) {
          this.rankChange = undefined;
          return '';
        } else {
          if (item.ranking > item.rankingLastMonth) {
            this.rankChange = item.rankingLastMonth - item.ranking;
            return 'up';
          } else if (item.ranking > item.rankingLastMonth) {
            this.rankChange = item.ranking - item.rankingLastMonth;
            return 'down';
          } else {
            this.rankChange = undefined;
            return '';
          }
        }
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
      }
    },
  }
</script>

<style scoped>
  .table-main {
    height: 90%;
    background-color: #102133;
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
    background-color: #102133;
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
    vertical-align: top;
  }

  .table tr:first-child{
    border-top: 1px #013968 solid;
  }

  table td:first-child {
    /*color: white;*/
    font-size: 18px;
    font-weight: 700;
    width: 15%;
    height: 40px;
    line-height: 40px;
    text-align: center;
  }
  table td:nth-child(2) {
    width: 70%;
    text-align: left;
  }
  table td:nth-child(3) {
    width: 12%;
    text-align: right;
  }
  table td:nth-child(4) {
    width: 3%;
    font-weight: 600;
    height: 40px;
    line-height: 40px;
    text-align: center;
  }
  .ename {
    white-space:nowrap;
    overflow:hidden;
    text-overflow:ellipsis;
  }
  .up {
    color: lawngreen;
  }
  .down {
    color: red;
  }
</style>

<template>
    <div class="table-main" v-on:mouseout="Up()" v-on:mouseover="Stop()">
      <table class="table" id="table1">
        <template v-for="(item, index) in roadData">
          <tr>
            <td>{{index+1}}</td>
            <td>{{item.name}}<br>{{item.direction}}</td>
            <td v-if="item.status==0" >{{formatterStatus(item.status)}}</td>
            <td v-else-if="item.status==1" style="color: #198a1b">{{formatterStatus(item.status)}}</td>
            <td v-else-if="item.status==2" style="color: #ffb336">{{formatterStatus(item.status)}}</td>
            <td v-else-if="item.status==3" style="color: #ff1a1e">{{formatterStatus(item.status)}}</td>
            <td>{{item.speed}}km/h</td>
          </tr>
        </template>
      </table>
      <table class="table"  id="table2">
        <template v-for="(item, index) in roadData">
          <tr>
            <td>{{index+1}}</td>
            <td>{{item.name}}<br>{{item.direction}}</td>
            <td v-if="item.status==0" >{{formatterStatus(item.status)}}</td>
            <td v-else-if="item.status==1" style="color: #198a1b">{{formatterStatus(item.status)}}</td>
            <td v-else-if="item.status==2" style="color: #ffb336">{{formatterStatus(item.status)}}</td>
            <td v-else-if="item.status==3" style="color: #ff1a1e">{{formatterStatus(item.status)}}</td>
            <td>{{item.speed}}km/h</td>
          </tr>
        </template>
      </table>
    </div>
</template>

<script>
    export default {
      name: "road",
      props:["roadData"],
      data(){
        return{
          speed:40,
          timeId:undefined,
        }
      },
      watch:{
        roadData(){
          $(".table-main").css("scrollTop",0);
          $(".table-main").scrollTop(0);
        }
      },
      mounted() {
        let _self =this;
        _self.timeId =window.setInterval(function(){
          _self.ScrollUp()
        },_self.speed);
      },
      destroyed(){
        window.clearInterval(this.timeId);
      },
      methods: {
        formatterStatus(value){
          if(value==0){
            return "未知";
          }else if(value==1){
            return "正常";
          }else if(value==2){
            return "缓行";
          }else if(value==3){
            return "拥堵";
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
        },

      }
    }
</script>

<style scoped>
  .table-main{
    height: 100%;
    background-color: #102133;
    color:#ffffff;
    color: #2d8ac7;
    overflow-y: scroll;
  }
  .table-main::-webkit-scrollbar {
    display: none;
  }
  .table {
    font-size: 14px;
    color: white;
    text-align: center;
    width: 100%;
    height: 100%;
    margin-top: 0;
  }

  #table1 tr:first-child{
    border-top: 1px #013968 solid;
  }
  .table tr {
    height: 41px;
    line-height: 41px;
    border-bottom: 1px #013968 solid;
    border-left: 1px #013968 solid;
    border-right: 1px #013968 solid;
  }
  table td:first-child {
    color: white;
    font-size: 20px;
    font-weight: 700;
    width: 10%;
  }
  table td:nth-child(2) {
    text-align: left;
    line-height: 24px;
    padding-left: 8px;
    width: 60%;
  }
  table td:nth-child(2):first-line {
    color: #0af;
  }
  table td:nth-child(3) {
    width: 20%;
    text-align: center;
  }
  table td:nth-child(4) {
    width: 10%;
  }
</style>

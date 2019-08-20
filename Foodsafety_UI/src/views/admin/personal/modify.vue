<template>
  <div class="app-container calendar-list-container">


    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="档案更新" name="a1"><verify1 tabName="pane-a1" @setCount="setCount"></verify1></el-tab-pane>
      <el-tab-pane label="证照更新" name="a2"><verify2 tabName="pane-a2" @setCount="setCount"></verify2></el-tab-pane>
      <el-tab-pane label="产品更新" name="a3"><verify3 tabName="pane-a3" @setCount="setCount"></verify3></el-tab-pane>
      <el-tab-pane label="原料更新" name="a4"><verify4 tabName="pane-a4" @setCount="setCount"></verify4></el-tab-pane>
      <el-tab-pane label="设备更新" name="a5"><verify5 tabName="pane-a5" @setCount="setCount"></verify5></el-tab-pane>
      <el-tab-pane label="规模更新" name="a6"><verify6 tabName="pane-a6" @setCount="setCount"></verify6></el-tab-pane>
    </el-tabs>

  </div>
</template>
<script>
  import verify1 from "views/admin/enterprise/base/verify";
  import verify2 from "views/admin/enterprise/certificate/verify";
  import verify3 from "views/admin/enterprise/product/verify";
  import verify4 from "views/admin/enterprise/material/verify";
  import verify5 from "views/admin/enterprise/equipment/verify";
  import verify6 from "views/admin/enterprise/scale/verify";
  export default {
    components: {
      verify1,verify2,verify3,verify4,verify5,verify6
    },
    data() {
      return {
        activeName: 'a1',
        countArry:[0,0,0,0,0,0],
        count:0,
      };
    },
     watch:{
      count(val){
        let count = this.countArry;
        if(val==this.countArry.length){
          if(count[0]>0){
            this.activeName="a1";
          }else if(count[1]>0){
            this.activeName="a2";
          }else if(count[2]>0){
            this.activeName="a3";
          }else if(count[3]>0){
            this.activeName="a4";
          }else if(count[4]>0){
            this.activeName="a5";
          }else if(count[5]>0){
            this.activeName="a6";
          }
        }
      }
    },
    methods: {
      handleClick(tab, event) {
        // console.log(tab, event);
      },
      setCount(value,index){
        this.count++;
        if(index==1){
          this.countArry[0]=value;
        }else if(index==2){
          this.countArry[1]=value;
        }else if(index==3){
          this.countArry[2]=value;
        }else if(index==4){
          this.countArry[3]=value;
        }else if(index==5){
          this.countArry[4]=value;
        }else {
          this.countArry[5]=value;
        }
      }
    }
  };
</script>

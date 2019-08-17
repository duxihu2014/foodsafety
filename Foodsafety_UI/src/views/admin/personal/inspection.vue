<template>
  <div class="app-container calendar-list-container">


    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="日常检查" name="a1"><rcCheck tabName="pane-a1" @setCount="setCount"></rcCheck></el-tab-pane>
      <el-tab-pane label="回访事件" name="a2"><hfCheck tabName="pane-a2" @setCount="setCount"></hfCheck></el-tab-pane>
      <el-tab-pane label="现场核查" name="a3"><xcCheck tabName="pane-a3" @setCount="setCount"></xcCheck></el-tab-pane>
    </el-tabs>

  </div>
</template>
<script>
  import rcCheck from "views/admin/safety/inspection/index";
  import hfCheck from "views/admin/safety/revisit/index";
  import xcCheck from "views/admin/enterprise/onsite/index";
  export default {
    components: {
      rcCheck,hfCheck,xcCheck
    },
    data() {
      return {
        activeName: 'a1',
        countArry:[0,0,0],
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
          }
        }
      }
    },
    methods: {
      handleClick(tab, event) {
        //console.log(tab, event);
      },
      setCount(value,index){
        this.count++;
        if(index==1){
          this.countArry[0]=value;
        }else if(index==2){
          this.countArry[1]=value;
        }else{
          this.countArry[2]=value;
        }

      }
    }
  };
</script>

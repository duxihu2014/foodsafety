<template>
  <div class="app-container calendar-list-container">


    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="企业证照" name="a1"><certProduce tabName="pane-a1" @setCount="setCount"></certProduce></el-tab-pane>
      <el-tab-pane label="人员健康证" name="a2"><certHealth tabName="pane-a2" @setCount="setCount"></certHealth></el-tab-pane>
    </el-tabs>

  </div>
</template>
<script>
  import certProduce from "views/admin/personal/certProduce";
  import certHealth from "views/admin/personal/certHealth";
  export default {
    components: {
      certProduce, certHealth
    },
    data() {
      return {
        activeName: 'a1',
        countArry:[0,0],
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
          }
        }
      }
    },
    methods: {
      handleClick(tab, event) {

      },
      setCount(value,index){
        this.count++;
        if(index==1){
          this.countArry[0]=value;
        }else{
          this.countArry[1]=value;
        }

      }
    }
  };
</script>

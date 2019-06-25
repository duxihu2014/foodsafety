<template>
  <div class="app-container calendar-list-container">


    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="档案更新" name="a1"><verify1 tabName="pane-a1"></verify1></el-tab-pane>
      <el-tab-pane label="证照更新" name="a2"><verify2 tabName="pane-a2"></verify2></el-tab-pane>
      <el-tab-pane label="产品更新" name="a3"><verify3 tabName="pane-a3"></verify3></el-tab-pane>
      <el-tab-pane label="原料更新" name="a4"><verify4 tabName="pane-a4"></verify4></el-tab-pane>
      <el-tab-pane label="设备更新" name="a5"><verify5 tabName="pane-a5"></verify5></el-tab-pane>
      <el-tab-pane label="规模更新" name="a6"><verify6 tabName="pane-a6"></verify6></el-tab-pane>
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
        activeName: 'a1'
      };
    },
    methods: {
      handleClick(tab, event) {
        // console.log(tab, event);
      }
    }
  };
</script>

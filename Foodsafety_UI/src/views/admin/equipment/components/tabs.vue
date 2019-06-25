<template>
  <div class="app-container calendar-list-container">


    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="摄像头管理" name="a1"><verify1 ref="tab1"></verify1></el-tab-pane>
      <el-tab-pane label="传感器管理" name="a2"><verify2 ref="tab2"></verify2></el-tab-pane>
    </el-tabs>

  </div>
</template>
<script>
  import verify1 from "views/admin/equipment/components/equipment";
  import verify2 from "views/admin/equipment/components/sensor";
  export default {
    components: {
      verify1,verify2
    },
    data() {
      return {
        activeName: 'a2',
        listQuery: {},

      };
    },
    methods: {
      handleClick(tab, event) {
        // console.log(tab, event);
      },
      getSelectNodeData(id, level, pid) {
        /*if(level == "1"){
          this.listQuery.enterpriseId = id;
        }else {
          this.listQuery.enterpriseId = pid;
          this.listQuery.enterpriseGroupId = id;
        }*/
        this.$refs["tab1"].getSelectNodeData(id, level, pid);
        this.$refs["tab2"].getSelectNodeData(id, level, pid);

      },
    }
  };
</script>

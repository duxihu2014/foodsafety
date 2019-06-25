<template>
  <div class="app-container calendar-list-container">


    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="日常检查" name="a1"><rcCheck tabName="pane-a1"></rcCheck></el-tab-pane>
      <el-tab-pane label="回访事件" name="a2"><hfCheck tabName="pane-a2"></hfCheck></el-tab-pane>
      <el-tab-pane label="现场核查" name="a3"><xcCheck tabName="pane-a3"></xcCheck></el-tab-pane>
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
        activeName: 'a1'
      };
    },
    methods: {
      handleClick(tab, event) {
        //console.log(tab, event);
      }
    }
  };
</script>

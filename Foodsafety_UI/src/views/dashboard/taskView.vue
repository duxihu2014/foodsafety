<template>
  <div class="app-container calendar-list-container">


    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="日常检查" name="a1"><rcCheck :gridId="currAreaInfo.gridId"></rcCheck></el-tab-pane>
      <el-tab-pane label="回访事件" name="a2"><hfCheck :gridId="currAreaInfo.gridId"></hfCheck></el-tab-pane>
      <el-tab-pane label="现场核查" name="a3"><xcCheck :gridId="currAreaInfo.gridId"></xcCheck></el-tab-pane>
    </el-tabs>

  </div>
</template>
<script>
  import rcCheck from "./components/rcCheck";
  import hfCheck from "./components/hfCheck";
  import xcCheck from "./components/xcCheck";
  export default {
    components: {
      rcCheck,hfCheck,xcCheck
    },
    props: [
      'currAreaInfo'
    ],
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

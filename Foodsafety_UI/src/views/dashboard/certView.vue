<template>
  <div class="app-container calendar-list-container">
    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="餐饮服务许可证" name="a1"><certProduce :gridId="currAreaInfo.gridId"></certProduce></el-tab-pane>
      <el-tab-pane label="人员健康证" name="a2"><certHealth :gridId="currAreaInfo.gridId"></certHealth></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import certProduce from "./components/certProduce";
  import certHealth from "./components/certHealth";
    export default {
        name: "cert-view",
      props: [
        'currAreaInfo'
      ],
        components: {
          certProduce, certHealth
        },
        data() {
          return {
            activeName: 'a1'
          };
        },
        methods: {
          handleClick(tab, event) {
            //debugger
            //console.log(tab, event);
          }
        }
    }
</script>

<style scoped>

</style>

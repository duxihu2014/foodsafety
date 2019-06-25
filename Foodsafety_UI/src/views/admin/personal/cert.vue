<template>
  <div class="app-container calendar-list-container">


    <el-tabs v-model="activeName"  @tab-click="handleClick">
      <el-tab-pane label="企业证照" name="a1"><certProduce tabName="pane-a1"></certProduce></el-tab-pane>
      <el-tab-pane label="人员健康证" name="a2"><certHealth tabName="pane-a2"></certHealth></el-tab-pane>
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
        activeName: 'a1'
      };
    },
    methods: {
      handleClick(tab, event) {

      }
    }
  };
</script>

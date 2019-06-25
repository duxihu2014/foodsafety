<template>
    <div class="app-container">
      <el-tabs v-model="tabPosition" >
        <el-tab-pane label="基本信息">
          <comp-base ></comp-base>
        </el-tab-pane>
        <el-tab-pane label="证照信息">
          <comp-certificate ></comp-certificate>
        </el-tab-pane>
        <el-tab-pane label="产品信息">
          <comp-product ></comp-product>
        </el-tab-pane>
        <el-tab-pane label="原料信息">
          <comp-material ></comp-material>
        </el-tab-pane>
        <el-tab-pane label="设备信息">
          <comp-equipment ></comp-equipment>
        </el-tab-pane>
        <el-tab-pane label="规模信息">
          <comp-scale ></comp-scale>
        </el-tab-pane>
      </el-tabs>
    </div>
</template>

<script>
    export default {
      name: "verify",
      components: {
        "comp-base": () => import("./components/base"),
        "comp-certificate": () => import("./components/certificate"),
        "comp-scale": () => import("./components/scale"),
        "comp-product": () => import("./components/product"),
        "comp-equipment": () => import("./components/equipment"),
        "comp-material": () => import("./components/material"),
      },
      data(){
        return {
          tabPosition:'0',
        }
      },
      methods:{

      },
    }
</script>

<style scoped>

</style>

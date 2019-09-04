<template>
  <div class='tabs-view-container' :class="{hideSidebar:!sidebar.opened}">
    <router-link class="tabs-view" v-for="tag in Array.from(tabs)" :to="tag.path" :key="tag.path">
      <el-tag :closable="closable(tag.path)" :type="isActive(tag.path)?'primary':''" @close='closeViewTabs(tag,$event)' :class="dotClass(tag.path)">
        {{tag.name}}
      </el-tag>
    </router-link>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import Cookies from "js-cookie";

export default {
  computed: {
    tabs() {
      //   console.log(JSON.parse(Cookies.get("visitedViews")));
      if(this.$store.state.app.visitedViews.length>10){
        this.$store.state.app.visitedViews.splice(1,1);
      }
      return this.$store.state.app.visitedViews;
    },
    ...mapGetters(["visitedViews"]),
    sidebar() {
      return this.$store.state.app.sidebar;
    }
  },
  mounted() {
    if (Cookies.get("visitedViews")) {
      const visitedViews = JSON.parse(Cookies.get("visitedViews"));
      if (
        visitedViews &&
        visitedViews.length > 0 &&
        this.visitedViews.length === 1
      ) {
        this.setViews(visitedViews);
      }
    }
  },
  created() {
    //this.isActive();
  },
  methods: {
    ...mapActions(["setViews"]),
    closable(path) {
      return path !== "/";
    },
    dotClass(path) {
      if (path === "/") {
        return "dot";
      } else {
        return "";
      }
    },
    closeViewTabs(view, $event) {
      this.$store.dispatch("delVisitedViews", view);
      this.$router.push(this.visitedViews[this.visitedViews.length - 1].path);
      $event.preventDefault();
    },
    generateRoute() {
      // debugger
      if (this.$route.matched[this.$route.matched.length - 1].name) {
        return this.$route.matched[this.$route.matched.length - 1];
      } else {
        return;
      }
      /* this.$route.matched[0].path = '/'
      return this.$route.matched[0] */
    },
    changeMainContainerStyle(){
      if("/dashboard"== this.$route.path||"/admin/delivery/supplyObject/mapQuery" == this.$route.path){
        $(".main-container").css({height:"100%",backgroundColor:"#010E1C"});
        $("#app").css({height:"100%"});
      }else{
        $(".main-container").css({height:"",backgroundColor:""});
        $("#app").css({height:""});
      }
    },
    addViewTabs() {
      const currentPath = this.generateRoute();
      if (currentPath) {
        this.$store.dispatch("addVisitedViews", currentPath);
      }
    },
    isActive(path) {
      this.changeMainContainerStyle();
      return path === this.$route.path;
    }
  },
  watch: {
    $route() {
      this.addViewTabs();
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.tabs-view-container {
  vertical-align: top;
  border-top: 1px solid rgba(100, 100, 100, 0.1);
  margin-left: 12.5rem;
  height: 40px;
  padding: 2px 10px;
  width: 100%;
  z-index: 1;
  overflow: hidden;
  top: 60px;
  position: fixed;
  box-shadow: 0 2px 1px 1px rgba(100, 100, 100, 0.1);
  background: rgb(240, 240, 240);
   .tabs-view {
    padding-left: 5px;
    display: inline-block;
    line-height: 40px;
    span {
      height: 32px;
      line-height: 32px;
      background-color: rgb(255, 255, 255);
      color: rgb(73, 80, 96);
    }
    .el-tag--primary {
      background-color: rgba(32, 160, 255, 0.1) !important;
      border-color: rgba(32, 160, 255, 0.2) !important;
      color: #20a0ff !important;
    }
    .dot {
      &:before {
        content: "";
        display: inline-block;
        height: 12px;
        width: 12px;
        border-radius: 50%;
        background-color: #2d8cf0;
      }
    }
  }

  /*  .router-link-active{
    span{
       background-color: rgba(32, 160, 255, .1) !important;
      border-color: rgba(32, 160, 255, .2) !important;
      color: #20a0ff !important;
    }
  } */
}
.hideSidebar {
  margin-left: 64px;
}
</style>

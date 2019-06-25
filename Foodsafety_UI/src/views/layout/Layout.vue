<template>
  <div class="app-wrapper" :class="{hideSidebar:!sidebar.opened}">
    <navbar class="navbar-container" style="height: 0px"></navbar>
    <sidebar class="sidebar-container"></sidebar>
    <div class="main-container">
      <app-main></app-main>
    </div>
  </div>
</template>

<script>
  import {Navbar, Sidebar, AppMain} from 'views/layout';

  export default {
    name: 'layout',
    components: {
      Navbar,
      Sidebar,
      AppMain
    },
    computed: {
      sidebar() {
        return this.$store.state.app.sidebar;
      }
    },
    mounted: function () {
      let _self = this;
      _self.setSidebarHeight();
      $(window).resize(function () {
        _self.setSidebarHeight();
      })
      // 自定义透明滚动条
      $('.main-container').slimScroll({
        height: '100%',
        color: 'transparent',
        railColor: 'transparent',
        railVisible: true,
        alwaysVisible: true
      });
    },
    methods: {
      setSidebarHeight(){
        this.$nextTick(function () {
          $(".sidebar-container").css({"height": $("body").height() - $(".navbar").height() + 'px'})
        })
      },
      /*setMainContainerHeight(){
       this.$nextTick(function () {
       $(".main-container").css({"height":$("body").height()-$(".navbar").height()-45+'px'})
       })
       },
       setMainContainerWidth(){
       console.log($(".sidebar-container").width())
       this.$nextTick(function () {
       $(".main-container").css({"width":$("body").width()-$(".sidebar-container").width()+'px'})
       })
       }*/
    },

  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/mixin.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
    &.hideSidebar {
      .sidebar-container {
        width: 4rem;

      }
      .main-container {
        margin-left: 4rem;
      }
    }
    .sidebar-container {
      background-color: #324157;
      width: 12.5rem;
      position: fixed;
      top: 60px;
      bottom: 0;
      left: 0;
      z-index: 1;
    }
    .main-container {
      position: relative;
      min-height: 100%;
      height: 100%;
      padding-top: 110px;
      margin-left: 12.5rem;
    }
  }
</style>

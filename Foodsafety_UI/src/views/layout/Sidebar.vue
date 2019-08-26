<template>
  <div class="main-menu-container">
      <div class="main-menu-shrink">
        <i class="fa fa-bars main-menu-shrink-btn" aria-hidden="true" @click="toggleClick" :class="{'is-active':isCollapse}"></i>
      </div>
    <div class="main-menu-scroll">
      <el-menu class="main-menu"
               :unique-opened="true"
               :default-active="$route.path"
               :collapse="isCollapse"
               background-color="#324157"
               text-color="#bfcbd9"
               active-text-color="#20a0ff">
        <sidebar-item :routes='permission_routers'></sidebar-item>
      </el-menu>
    </div>
  </div>
</template>


<script>
  import { mapGetters } from 'vuex';
  import SidebarItem from './SidebarItem';
  export default {
    components: { SidebarItem },
    mounted: function () {
      let _self = this;
      _self.setScroll();
      $(window).resize(function(){
        _self.setScroll();
      })
    },
    computed: {
      ...mapGetters([
        'permission_routers',
        'sidebar'
      ]),
      isCollapse() {
        return !this.sidebar.opened
      }
    },
    methods: {
      toggleClick() {
        // console.log(this.permission_routers)
        this.$store.dispatch("ToggleSideBar");
        $(window).trigger("resize");
      },
      setScroll(){
        this.$nextTick(function () {
          $('.main-menu-scroll').slimScroll({
            width: '100%',
            height: $(".main-menu-container").height()-$(".main-menu-shrink").height()-1+'px',
            color: 'transparent',
            railColor: 'transparent',
            allowPageScroll:false,
            railVisible: true,
            alwaysVisible: true
          });
        })

      }
    }
  }
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  /* 菜单组件 */
  .main-menu {
    position: relative;
    width: 100%;
    height: 100%;
    -webkit-border-radius: 0;
    -moz-border-radius: 0;
    border-radius: 0;
    user-select: none;
  }
  /* 收缩按钮容器*/
  .main-menu-shrink{
    position: relative;
    height: 3rem;
    background-color: #324157;
    border-bottom: 1px dashed rgba(191,203,217,1);
  }
  /* 收缩按钮 */
  .main-menu-shrink-btn{
    position: absolute;
    right: 1.2rem;
    font-size: 22px;
    color: #bfcbd9;
    line-height: 3rem;
    display: inline-block;
    cursor: pointer;
  }
  .main-menu-shrink-btn.is-active {
    transform: rotate(90deg);
  }
</style>

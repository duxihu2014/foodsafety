<template>
	<div class='menu-wrapper'>
		<template v-for="item in routes">

			<router-link v-if="!item.hidden&&item.noDropdown&&item.children.length>0" :to="item.path+'/'+item.children[0].path">

				<el-menu-item :index="item.path+'/'+item.children[0].path" class='submenu-title-noDropdown'>
       <i   v-if='item.icon' class="menu-icon" :class="item.icon" aria-hidden="true"></i>
<!--			 	<icon-svg v-if='item.icon' :icon-class="item.icon"></icon-svg>-->
					<span slot="title">{{item.children[0].name}}</span>
				</el-menu-item>
			</router-link>

			<el-submenu :index="item.name" v-if="!item.noDropdown&&!item.hidden">
				<template slot="title">
            <i  v-if='item.icon' class="menu-icon" :class="item.icon" aria-hidden="true"></i>
				<!--<icon-svg v-if='item.icon' :icon-class="item.icon"></icon-svg>-->
					<span>{{item.name}}</span>
				</template>
				<template v-for="child in item.children" v-if='!child.hidden'>
					<sidebar-item class='nest-menu' v-if='child.children&&child.children.length>0' :routes='[child]'> </sidebar-item>
					<router-link v-else :to="item.path+'/'+child.path">
						<el-menu-item :index="item.path+'/'+child.path">
<!--              <i   v-if='child.icon' :class="child.icon" aria-hidden="true"></i>
 -->						<!-- 	<icon-svg v-if='child.icon' :icon-class="child.icon"></icon-svg> -->
							<span>{{child.name}}</span>
						</el-menu-item>
					</router-link>

				</template>

			</el-submenu>

		</template>
	</div>
</template>

<script>
import iconSvg from 'components/Icon-svg/index';
export default {
	components: {
    iconSvg
  },
  name: "SidebarItem",
  props: {
    routes: {
      type: Array
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  /* 菜单图标 */
  .menu-icon {
    display: inline-block;
    font-size: 18px;
    margin: 0 10px 0 5px;
    width: 18px;
    height: 18px;
  }
</style>


<template>
	<div id="app">
		<router-view></router-view>
	</div>
</template>

<script>
  export default{
    name: 'App',
    mounted() {
      //this.$store.commit("SET_SCREENHEIGHT", screen.height);
      this.$store.commit("SET_SCREENHEIGHT", document.documentElement.clientHeight);
    }
  }
</script>

<style lang="scss">
  @import './styles/index.scss'; // 全局自定义的css样
</style>

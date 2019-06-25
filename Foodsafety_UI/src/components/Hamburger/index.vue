<template>
	<div>
    <div class="main-menu-shrink">
      <i v-menu-shrink class="fa fa-bars main-menu-shrink-btn " aria-hidden="true"  @click="toggleClick" :class="{'is-active':isActive}"></i>
    </div>
	</div>
</template>

<script>
  export default {
    name: 'hamburger',
    props: {
      isActive: {
        type: Boolean,
        default: false
      },
      toggleClick: {
        type: Function,
        default: null
      }
    }
  }
</script>

<style scoped>

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
  transition: .38s;
}

</style>

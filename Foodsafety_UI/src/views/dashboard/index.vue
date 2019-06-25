<template>
  <div v-if="user.userType==3">
    <index-supervise></index-supervise>
  </div>
  <div v-else-if="user.userType==4">
    <index-enterprise></index-enterprise>
  </div>
  <div v-else  style="background-color: #1f2d3d">
  </div>
</template>

<script>
  import { mapGetters } from "vuex";
  export default {
    name: "index",
    components: {
      "index-supervise": () => import("./index_supervise"),
      "index-enterprise": () => import("./index_enterprise"),
    },
    data(){
      return {

      }
    },
    computed: {
      ...mapGetters(["user"]),
    },
    created(){

    },
    mounted () {

    },
    methods:{

    }
  }
</script>

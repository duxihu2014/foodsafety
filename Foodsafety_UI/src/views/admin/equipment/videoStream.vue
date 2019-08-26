<template>
 <div class="videoPlayer" ref="videoBox" v-if="vurl.isShow" >
    <video :id="videoid+vurl.vid" class="video-js video-js vjs-big-play-centered video" playsinline webkit-playsinline controls preload="auto"  autoplay>
            <source :src="videoUrl" type="application/x-mpegURL">  
      </video>
 </div>
</template>
 
<script>
export default {
  name:'videoStream',
  props:{
    vurl:{
      type:Object
    }
  },
 data () {
    return {
      videoUrl:this.$props.vurl.url,
      myPlayer:undefined,
      videoid:'videoid',
      id:0,
    }
 },
 computed:{
 },
 
 components: {
 },
  mounted() {
    if(this.$props.vurl.isShow){
    this.$nextTick(()=>{
      if(this.$props.vurl.isShow){
        this.myPlayer = videojs(this.videoid+this.$props.vurl.vid);
        this.myPlayer.play();
        // console.log(3555,this.$props.vurl)
      }
      })
    }
  },
 methods: {
 },
 beforeDestroy() {
   this.myPlayer.dispose();
   this.myPlayer=undefined;
   this.videoUrl=undefined;
   
 },

}
</script>
 
<style type="text/css" scoped>

</style>

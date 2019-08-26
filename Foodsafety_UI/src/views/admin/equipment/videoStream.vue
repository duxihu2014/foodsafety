<template>
 <div class="videoPlayer" ref="videoBox" v-if="vurl.isShow" >
      <div class="iconCose" style="height:4%;width:100%">
          <div class="el-icon-circle-close  iconStyle" @click="handleCloseVideo()"><span>关闭</span></div>
      </div>
    <video style="width:100%;height:100%" :id="videoid+vurl.vid" class="video-js video-js vjs-big-play-centered video" playsinline webkit-playsinline controls preload="auto"  autoplay>
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
      // if(this.myPlayer){
      //     this.myPlayer.dispose();
      //     this.myPlayer=undefined;
      //     this.videoUrl=undefined;
      // }
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
     handleEventClose(){
       this.$emit("handleEventClose",false)
      //  console.log(56555656);
      // this.isVideoShow=false
    },
 },
 beforeDestroy() {
   this.myPlayer.dispose();
   this.myPlayer=undefined;
   this.videoUrl=undefined;
   
 },

}
</script>
 
<style type="text/css" scoped>
.iconCose{
  width: 100%;
  box-sizing: border-box;
  background:#409EFF;
  color:#fff;
}
.iconStyle{
  font-size:23px;
  padding: 3px 8px;
  cursor:pointer;
  box-sizing: border-box;
}
.iconStyle>span{
  font-size: 18px;
  vertical-align: middle;
}

</style>

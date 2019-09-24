<template>
  <div class="videoPlayer"    v-if="vurl.isShow" >
    <VueDragResize :isDraggable ="true"  :clicked="true"  :isActive="true" :aspectRatio="true"  :w="600" :h="600"  :sticks="sticks" style="background:#000;">
          <video
           v-if="videoUrl" style="width:100%;height:100%"  :id="videoid+vurl.vid" class="video-js video-js vjs-big-play-centered video" playsinline webkit-playsinline controls preload="auto"  autoplay>
              <source :src="videoUrl" type="application/x-mpegURL">  
          </video>
          <div  v-if="!videoUrl" class="wraing_mas" >视频格式错误,加载失败!</div>
          <div class="el-icon-circle-close close_btn "  @click="handleCloseVideo()"><span></span></div>
    </VueDragResize>
  </div>
</template>
<script>
import VueDragResize from 'vue-drag-resize'
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
      sticks:['tl','tr', 'br','bl'],
      isplay:'b'
    }
 },
 directives: {
    drag: {
      bind: function (el) {
        let odiv = el; 
        odiv.onmousedown = (e) => {
          let disX = e.clientX - odiv.offsetLeft;
          let disY = e.clientY - odiv.offsetTop;
          document.onmousemove = (e)=>{
            let left = e.clientX - disX;    
            let top = e.clientY - disY;
            //移动当前元素
            odiv.style.left = left + 'px';
            odiv.style.top = top + 'px';
          };
          document.onmouseup = (e) => {
            document.onmousemove = null;
            document.onmouseup = null;
          };
        };
      }
    }
  },
 computed:{
 },
 components: {
   VueDragResize
 },
  mounted() {
    if(this.$props.vurl.isShow&&this.videoUrl){
      this.video_dispose()
      this.$nextTick(()=>{
        if(this.$props.vurl.isShow&&this.videoUrl){
          this.myPlayer = videojs(this.videoid+this.$props.vurl.vid);
          this.myPlayer.play();
        }
      })
    }
  },
 methods: {
   handleCloseVideo(){
     this.$emit("handleEventClose",false)
   },
 video_dispose(){
    if(this.myPlayer){
      this.myPlayer.dispose();
      this.myPlayer=undefined;
      this.videoUrl=undefined;
    }
 },

 },
 beforeDestroy() {
   this.video_dispose()
 },

}
</script>
 
<style type="text/css" scoped>
.videoPlayer{
  position: relative;

}
.close_btn{
  position: absolute;
  top: 2%;
  right: 2%;
  font-size:33PX;
  padding: 3px 8px;
  color:#ccc;
}
.close_btn:hover{
  color:#fff;
  cursor:pointer;
}
.wraing_mas{
    width:100%;height:100%;text-align:center;padding-top:45%;color:#fff;font-size:0.66rem;
}
</style>

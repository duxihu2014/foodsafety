<template>
    <div class="main">
      <div class="location">
        <span>{{weatherData.city}}</span>
        <i class="fa fa-map-marker"></i>
      </div>
      <div class="temperature">
        <span >{{weatherData.temperature}}</span>
        <span>
          ℃
        </span>
      </div>
      <div class="weather">{{weatherData.weather}}</div>
      <div class="humidity">湿度:{{weatherData.humidity}}%</div>
      <div class="winddirection">风向:{{weatherData.winddirection}}</div>
      <div class="windpower">风力:{{weatherData.windpower}}</div>
    </div>
</template>

<script>
    export default {
      name: "weather",
      props:["weatherData"],
      data(){
        return {

        }
      },
      mounted () {

      }

    }
</script>

<style scoped>
  .main{
    height: 100%;
    background-color: #102133;
    color:#ffffff;
    text-align: center;
    color: #2d8ac7;
  }
  .location{
    font-size: 20px;
  }
  .temperature>span:nth-child(1){
    font-size: 80px;
    display: inline-block;
  }
  .temperature>span:nth-child(2){
    display: inline-block;
    font-size: 30px;
    position: relative;
    top: -33px;
  }
  .weather{
    font-size: 23px;
  }

</style>

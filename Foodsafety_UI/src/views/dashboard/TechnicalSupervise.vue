<template>
    <div class="tec-page">
        <div class="top-container"> <!-- height: 70% -->
            <div class="page-title">松江区食品安全技防监管态势图</div> <!-- height: 8% -->
            <el-row :span="24" style="height: 92%;">
                <!-- 报警地图 -->
                <el-col :span="18" style="height: 100%; background-color: green;">
                    <alarm-map ref="map-alarm"></alarm-map>
                </el-col>
                <!-- 报警图表 -->
                <el-col :span="6" style="height: 100%; border-left: 1px dashed #a4feff;">
                    <el-row style="width: 100%; height: 50%; border-bottom: 1px dashed #a4feff;">
                        <!--<h4>柱状图1(IOT报警：类型、总数、已处理、未处理)</h4>-->
                        <iot-alarm></iot-alarm>
                    </el-row>
                    <el-row style="width: 100%; height: 50%;">
                        <!--<h4>柱状图2(视频报警：类型、总数、已处理、未处理)</h4>-->
                        <video-alarm></video-alarm>
                    </el-row>
                </el-col>
            </el-row>
        </div>

        <div class="bottom-container"> <!-- height: 30% -->
            <el-col :span="12" style="height: 100%; padding: 5px;">
                <!--<h4>视频报警列表：报警名称、类型、时间</h4>-->
                <video-alarm-list @showComp="showCompInMap"></video-alarm-list>
            </el-col>
            <el-col :span="12" style="height: 100%; padding: 5px;">
                <humiture-alarm-list></humiture-alarm-list>
            </el-col>
        </div>
    </div>
</template>

<script>
    import AlarmMap from 'views/dashboard/components-technical/AlarmMap.vue'
    import IotAlarm from 'views/dashboard/components-technical/IotAlarm.vue'
    import VideoAlarm from 'views/dashboard/components-technical/VideoAlarm.vue'
    import VideoAlarmList from 'views/dashboard/components-technical/VideoAlarmList.vue'
    import HumitureAlarmList from 'views/dashboard/components-technical/HumitureAlarmList.vue'
    import fetch from 'utils/fetch';
    export default {
        name: 'TechnicalSupervise',
        components: {AlarmMap, IotAlarm, VideoAlarm, VideoAlarmList, HumitureAlarmList},
        data(){
            return {

            }
        },
        mounted(){

        },
        methods: {
          showCompInMap(compId){
            // alert('-----'+compId);
            this.$refs["map-alarm"].moveMap(compId);
          }

        }

    }


</script>

<style scoped>

    .tec-page {
        width: 100%;
        height: 100%;
        overflow: hidden;
        background-color: rgb(1, 14, 28);
    }

    .top-container {
        height: 60%;
    }

    .bottom-container {
        height: 29%;

    }

    .page-title {
        height: 8%;
        line-height: 5vh;
        font-size: 3vh;
        letter-spacing: 1px;
        color: #a4feff;
        font-weight: bold;
        text-align: center;
        background-color: #102133;
    }


</style>

<template>
    <div ref="iotAlarm" class="tec-page"></div>
</template>

<script>
    import fetch from 'utils/fetch';
    export default {
        name: 'IotAlarm',
        components: {},
        dara(){
            return {

            }
        },
        mounted(){
            this.getData();
            $(window).resize(()=> {
                this.echart.resize();
            });
        },
        methods: {
            getData(){
                fetch({
                 url: '/reportAlarm/api/list?type=2',
                 method: 'get',
                 }).then(data => {
                    this.initEchart(data);
                 }).catch(err =>{
                    this.$message.error('温湿度列表数据获取失败！');
                 });
            },
            filterData(data, type){
                let result = {};
                data.forEach(item=>{
                    if(item.eventId === type && item.eventStatus ==='1'){
                        result.untreated = item.count;
                    }
                    if(item.eventId === type && item.eventStatus ==='2'){
                        result.treated = item.count;
                    }
                });
                if(!result.untreated){result.untreated=0}
                if(!result.treated){result.treated=0}
                result.count = result.untreated+result.treated;
                return result;
            },
            initEchart(data){

                let xData = ['温度高', '温度低', '湿度高', '湿度低'];

                let wh = this.filterData(data, '10001');
                let wl = this.filterData(data, '10002');
                let sh = this.filterData(data, '10003');
                let sl = this.filterData(data, '10004');

                this.echart = this.echarts.init(this.$refs['iotAlarm']);
                let option = {
                    //backgroundColor: "#000001",
                    "title": {
                        "text": "IOT报警",
                        //"subtext": "BY Wang Dingding",
                        x: "4%",
                        y: "3%",
                        textStyle: {
                            color: '#a4feff',
                            fontSize: '16'
                        },
                        /*subtextStyle: {
                            color: '#90979c',
                            fontSize: '16',

                        },*/
                    },
                    "tooltip": {
                        "trigger": "axis",
                        "axisPointer": {
                            "type": "shadow",
                            textStyle: {
                                color: "#fff"
                            }

                        },
                    },
                    "grid": {
                        "borderWidth": 0,
                        "top": 80,
                        "bottom": 40,
                        "left": 50,
                        "right": 10,
                        textStyle: {
                            color: "#fff"
                        }
                    },
                    "legend": {
                        x: '4%',
                        top: '13%',
                        textStyle: {
                            color: '#fff',
                        },
                        "data": ['已处理', '未处理']
                    },

                    "calculable": true,
                    "xAxis": [{
                        "type": "category",
                        "axisLine": {
                            lineStyle: {
                                color: '#a4feff'
                            }
                        },
                        "splitLine": {
                            "show": false
                        },
                        "axisTick": {
                            "show": false
                        },
                        "splitArea": {
                            "show": false
                        },
                        "axisLabel": {
                            "interval": 0,

                        },
                        "data": xData,
                    }],
                    "yAxis": [{
                        "type": "value",
                        "splitLine": {
                            "show": false
                        },
                        "axisLine": {
                            lineStyle: {
                                color: '#a4feff'
                            }
                        },
                        "axisTick": {
                            "show": false
                        },
                        "axisLabel": {
                            "interval": 0,

                        },
                        "splitArea": {
                            "show": false
                        },

                    }],
                    "series": [
                        {
                            "name": "已处理",
                            "type": "bar",
                            "stack": "总量",
                            "itemStyle": {
                                "normal": {
                                    "color": "rgba(8,185,232,.9)",
                                    "barBorderRadius": 0,
                                    "label": {
                                        "show": true,
                                        "position": "top",
                                        formatter: function(p) {
                                            return p.value > 0 ? (p.value) : '';
                                        }
                                    }
                                }
                            },
                            "data": [
                                wh.treated,
                                wl.treated,
                                sh.treated,
                                sl.treated
                            ]
                        },
                        {
                            "name": "未处理",
                            "type": "bar",
                            "stack": "总量",
                            "barMaxWidth": 35,
                            "barGap": "10%",
                            "itemStyle": {
                                "normal": {
                                    "color": "rgba(246,33,87, .9)",
                                    "label": {
                                        "show": true,
                                        "textStyle": {
                                            "color": "#fff"
                                        },
                                        "position": "insideTop",
                                        formatter: function(p) {
                                            return p.value > 0 ? (p.value) : '';
                                        }
                                    }
                                }
                            },
                            "data": [
                                wh.untreated,
                                wl.untreated,
                                sh.untreated,
                                sl.untreated
                            ]
                        },
                        {
                            "name": "总数",
                            "type": "line",
                            "stack": "总量",
                            symbolSize:10,
                            symbol:'circle',
                            "itemStyle": {
                                "normal": {
                                    "color": "#a4feff",
                                    "barBorderRadius": 0,
                                    "label": {
                                        "show": true,
                                        "position": "top",
                                        formatter: function(p) {
                                            return p.value > 0 ? (p.value) : '';
                                        }
                                    }
                                }
                            },
                            "data": [
                                wh.count,
                                wl.count,
                                sh.count,
                                sl.count
                            ]
                        },
                    ]
                };
                this.echart.setOption(option);
            }
        }

    }


</script>

<style scoped>

    .tec-page {
        width: 100%;
        height: 100%;
    }


</style>

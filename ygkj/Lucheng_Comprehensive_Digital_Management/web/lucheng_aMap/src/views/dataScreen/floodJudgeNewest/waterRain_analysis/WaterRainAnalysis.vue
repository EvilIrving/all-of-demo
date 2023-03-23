<template>
  <div class="water-rain-analysis">
    <!-- 天气预报 -->
    <div class="weather-forecast">
      <div
        class="weather-item"
        :key="index"
        v-for="(item, index) in weatherList"
      >
        <div class="week-date">{{ item.time }}</div>
        <div class="weather-type"><i :class="item.icon"></i></div>
        <div class="weather-value">{{ item.minTemp }}-{{ item.maxTemp }}℃</div>
      </div>
    </div>

    <!-- 地图统计 -->
    <div class="map-statistics" >
      <div class="map-statistics-item">
        <div class="left-part">
          <i class="icon-statistic-reservoir"></i>
        </div>
        <div class="right-part">
          <div class="part-title">1h超警雨量站</div>
          <div class="part-value" :style="{ color: oneHourStation>0? 'red':'#23E3FB' }">{{oneHourStation}}</div>
        </div>
      </div>

      <div class="map-statistics-item">
        <div class="left-part">
          <i class="icon-statistic-reservoir"></i>
        </div>
        <div class="right-part">
          <div class="part-title">超汛限水库</div>
          <div class="part-value" :style="{ color: floodReservoir>0? 'red':'#23E3FB' }">{{floodReservoir}}</div>
        </div>
      </div>
    </div>

    <!-- 勾选 -->
    <div
      class="layer-control"
    >
      <!-- <div class="one-key" v-if="pageType == 'rain'"></div> -->
      <div class="layer-control-item">
        <p>实时雨量</p>
        <el-checkbox-group
          class="layer-control-box"
          v-model="checkedRealLayer"
          @change="handleCheckedRealLayerChange"
        >
          <el-checkbox
            v-for="(item, index) in realLayer"
            :label="item.label"
            :key="index"
            >{{ item.label }}</el-checkbox
          >
        </el-checkbox-group>
      </div>

      <div class="layer-control-item">
        <p>预报雨量</p>
        <el-checkbox-group
          class="layer-control-box"
          v-model="checkedExpectLayer"
          @change="handleCheckedExpectLayerChange"
        >
          <el-checkbox
            v-for="(item, index) in expectLayer"
            :label="item.label"
            :key="index"
            >{{ item.label }}</el-checkbox
          >
        </el-checkbox-group>
      </div>

      <div class="layer-control-item">
        <p>实时+预报雨量</p>
        <el-checkbox-group
          class="layer-control-box"
          v-model="checkedMixLayer"
          @change="handleCheckedMixChange"
        >
          <el-checkbox
            v-for="(item, index) in mixLayer"
            :label="item.label"
            :key="index"
            >{{ item.label }}</el-checkbox
          >
        </el-checkbox-group>
      </div>
    </div>

    <!-- 地图图例 -->
    <div class="map-legend">
      <div class="map-legend-item" :key="index" v-for="(item,index) in legendArr">
        <div class="legend-title">{{item.title}}</div>
        <div class="legend-detail" :key="index" v-for="(item,index) in legendObj[item.list]">
          <i :class="item.type"></i>
          <span>{{ item.label }}</span>
        </div>
      </div>
    </div>

    <!-- 地图区域 -->
    <screen-map ref="map" class="screen_map" element-loading-background="rgba(0, 0, 0, 0.8)" v-loading="mapRainfallLoading"></screen-map>

    <!-- 左侧菜单 -->
    <div class="bigScreen-left">
      <transition name="fade" v-on:after-leave="transitionDoneLeft">
        <div  class="sidebar" :style="{ width: '380px' }" v-show="barshowLeft">
          <!-- 面雨量 -->
          <div class="screen-title">
            <div class="title-left">
              <i class="icon-judge-icon"></i>
              <span>面雨量(mm)</span>
            </div>
          </div>

          <div v-loading="rainfallLoading" element-loading-background="rgba(0, 0, 0, 0.8)">
            <div class="total-info" >
              <div class="info-title">温州市整体面雨量</div>
              <div class="info-content">
                <div class="info-item">
                  <span>1h</span>
                  <span>{{totalOne.toFixed(2)}}mm</span>
                </div>
                <div class="info-item">
                  <span>3h</span>
                  <span>{{totalThree.toFixed(2)}}mm</span>
                </div>
                <div class="info-item">
                  <span>6h</span>
                  <span>{{totalSix.toFixed(2)}}mm</span>
                </div>
              </div>
            </div>

            <div class="sidebar-list">
              <el-table
                ref="table1"
                :data="waterDeep"
                style="width: 100%"
                height="240"
                highlight-current-row
              >
                <el-table-column type="index" label width="55">
                  <template slot-scope="scope">
                    <span class="table-order">{{ scope.$index + 1 }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="areaName"
                  label="地区"
                  width="120"
                ></el-table-column>
                <el-table-column
                  prop="rainfall_1"
                  label="1h"
                  width="60"
                ></el-table-column>
                <el-table-column
                  prop="rainfall_3"
                  label="3h"
                  width="60"
                ></el-table-column>
                <el-table-column
                  prop="rainfall_6"
                  label="6h预报"
                ></el-table-column>
              </el-table>
            </div>
          </div>

          <!-- 雨量站top10 -->
          <div v-loading='rainLoading' element-loading-background="rgba(0, 0, 0, 0.8)">
            <div class="screen-title">
              <div class="title-left">
                <i class="icon-judge-icon"></i>
                <span>雨量站</span>
              </div>
              <div class="title-right">
                <span @click="rainTabsClick(item.value)" :class="rainActive == item.value?'active':''" :key="index" v-for="(item,index) in rainTabs">{{item.label}}</span>
              </div>
            </div>

            <div class="sidebar-list">
              <el-table
                ref="table2"
                :data="rainStationArr"
                style="width: 100%"
                height="180"
                highlight-current-row
                row-click="rowClick"
              >
                <el-table-column type="index" label width="50">
                  <template slot-scope="scope">
                    <span v-if="scope.$index == 0" class="rank-first table-order">{{ scope.$index + 1 }}</span>
                    <span v-if="scope.$index == 1" class="rank-second table-order">{{ scope.$index + 1 }}</span>
                    <span v-if="scope.$index == 2" class="rank-third table-order">{{ scope.$index + 1 }}</span>
                    <span v-if="scope.$index > 2" class="table-order">{{ scope.$index + 1 }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="stationName" label="名称">
                  <template slot-scope="scope">
                    <span :title="scope.row.stationName">{{scope.row.stationName}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="areaName"
                  label="行政区划"
                ></el-table-column>
                <el-table-column
                  prop="basicName"
                  label="流域"
                  width="55"
                ></el-table-column>
                <el-table-column
                  prop="drp"
                  label="降雨(mm)"
                ></el-table-column>
              </el-table>
            </div>
          </div>

          <div class="switch-btn" @click="toggleSideBarLeft()"></div>
        </div>
      </transition>
      <div
        class="switch-btn show-btn"
        v-show="showBtnLeft"
        @click="toggleSideBarLeft()"
      ></div>
    </div>

    <!-- 右侧菜单 -->
    <div class="bigScreen-right">
      <transition name="fade" v-on:after-leave="transitionDone">
        <div class="sidebar" v-show="barshow">
          <!-- 大中型水库水位 -->
          <div class="screen-title">
            <div class="title-left">
              <i class="icon-judge-icon"></i>
              <span>大中型水库水位</span>
            </div>
            <div class="title-right">
              <el-switch
                v-model="reservoirVal"
                active-color="#106CE0"
                inactive-color="#ff4949"
                class="sidebar-switch"
              ></el-switch>
            </div>
          </div>

          <div class="sidebar-list">
            <el-table
              ref="table3"
              :data="sluiceArr"
              style="width: 100%"
              height="270"
              highlight-current-row
            >
              <el-table-column type="index" label width="55">
                <template slot-scope="scope">
                  <span class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="reservoirName"
                label="名称"
                width="120"
              ></el-table-column>
              <el-table-column
                prop="currentWaterLevel"
                label="水位(m)"
                width="80"
              ></el-table-column>
              <el-table-column
                prop="limitLevel"
                label="警戒水位(m)"
              ></el-table-column>
            </el-table>
          </div>

          <!-- 最接近汛限水位水库top10 -->
          <div class="screen-title">
            <div class="title-left">
              <i class="icon-judge-icon"></i>
              <span>最接近汛限水位水库</span>
            </div>
            <div class="title-right">
              <el-switch
                v-model="reservoirValTop"
                active-color="#106CE0"
                inactive-color="#ff4949"
                class="sidebar-switch"
              ></el-switch>
            </div>
          </div>

          <div class="sidebar-list">
            <el-table
              ref="table4"
              :data="sluiceTopArr"
              style="width: 100%"
              height="270"
              highlight-current-row

            >
              <el-table-column type="index" label width="55">
                <template slot-scope="scope">
                  <span v-if="scope.$index == 0" class="rank-first table-order">{{ scope.$index + 1 }}</span>
                  <span v-if="scope.$index == 1" class="rank-second table-order">{{ scope.$index + 1 }}</span>
                  <span v-if="scope.$index == 2" class="rank-third table-order">{{ scope.$index + 1 }}</span>
                  <span v-if="scope.$index > 2" class="table-order">{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="reservoirName"
                label="名称"
                width="120"
              ></el-table-column>
              <el-table-column
                prop="currentWaterLevel"
                label="水位(m)"
                width="80"
              ></el-table-column>
              <el-table-column
                prop="limitLevel"
                label="警戒水位(m)"
              ></el-table-column>
            </el-table>
          </div>

          <router-view name="right"></router-view>
          <div class="switch-btn" @click="toggleSideBar()"></div>
        </div>
      </transition>
      <div
        class="switch-btn show-btn"
        v-show="showBtn"
        @click="toggleSideBar()"
      ></div>
    </div>
  </div>
</template>

<script>
import ScreenMap from "../components/ScreenMap";
import { weatherPredict } from "@/api/system";
import { rainApi } from "@/api/dataScreen/floodJudge/index";
import axios from "axios";
export default {
  name: "WaterRainAnalysis",
  data() {
    return {
      showBtnLeft:false,
      barshowLeft:true,
      showBtn:false,
      barshow:true,
      waterDeep:[],//面雨量数组
      waterFallObj:{},//面雨量对象
      reservoirVal:false,//大中型水库地图点位显隐标记
      reservoirValTop:false,//最接近汛限水位地图点位显隐标记    
      weatherList:[],//天气数组
      legendObj:{
        'rainLegend':[{
          label:'0-10',
          type:'icon-legend-rain0'
        },{
          label:'10-25',
          type:'icon-legend-rain9'
        },{
          label:'25-50',
          type:'icon-legend-rain25'
        },{
          label:'50-100',
          type:'icon-rain_50_100'
        },{
          label:'100-250',
          type:'icon-rain_100_250'
        },{
          label:'>250',
          type:'icon-rain_250'
        }],//天气数组
        'reservoirLegendList':[{
          label:'正常水位',
          type:'icon-zhengchangshuiku'
        },{
          label:'超警水位',
          type:'icon-chaojinshuiku'
        }],//水库图例列表
      },
      legendArr:[{
        title:"雨量",
        list:"rainLegend",
      },{
        title:"水位",
        list:"reservoirLegendList",
      }],//显示图例数组
      checkedRealLayer: [],//实时雨量勾选
      realLayer:[
        {
          label: "1h",
          value: 60,
        },
        {
          label: "3h",
          value: 180,
        },
        {
          label: "6h",
          value: 360,
      }],
      checkedExpectLayer:[],//预报雨量勾选
      expectLayer:[
        {
          label: "1h",
          value: 1,
        },
        {
          label: "3h",
          value: 3,
        },
        {
          label: "6h",
          value: 6,
      }],
      checkedMixLayer:[],//实时+预报勾选
      mixLayer:[{ 
        label: "前1h+预报1h",
        value: 1,
      },
      {
        label: "前3h+预报3h",
        value: 2,
      },
      {
        label: "前6h+预报6h",
        value: 3,
      }],
      rainTabs:[{
        label:"1h",
        value:1,
      },{
        label:"3h",
        value:3,
      },{
        label:"6h",
        value:6,
      }],
      rainActive:1,//雨量top10tab选中值
      sluiceArr:[],//大中型水库列表
      sluiceTopArr:[],//接近汛限水位水库top10列表
      totalOne:0,//温州整体1h
      totalThree:0,//温州整体3h
      totalSix:0,//温州整体6h
      rainfallLoading:true,//面雨量列表加载loading
      mapRainfallLoading:false,//地图面雨量加载loading
      rainLoading:true,
      rainStationArr:[],//雨量站top10
      oneHourStation:0,//1h超警雨量站
      floodReservoir:0,//超汛限水库
    }
  },
  components: {
    ScreenMap,
  },
  watch:{
    reservoirVal(val){
      if(val == true){
        this.$refs.map.reservoirPoint(this.sluiceArr);
        this.reservoirValTop = false;
      }else{
        this.$refs.map.clearReservoirLayer();
      }
    },
    reservoirValTop(val){
      if(val == true){
        this.$refs.map.sluiceTopArr(this.sluiceTopArr);
        this.reservoirVal = false;
      }else{
        this.$refs.map.clearsluiceTopLayer();
      }
    }
  },
  created(){
    this.getWeatherPredict();
    this.getSurfaceRainfall();
    // this.getRealRain('1h');
    this.getReservoirWaterAndRain();
    this.getTopNRes();
    this.getRainfallTop10();
    this.getBeyondNumber();
    this.getSuperRainNumber();
  },
  mounted(){
    this.$nextTick(()=> {
      // 拿到表格中承载数据的div元素
      const divData1 = this.$refs.table1.bodyWrapper;
      const divData2 = this.$refs.table2.bodyWrapper;
      const divData3 = this.$refs.table3.bodyWrapper;
      const divData4 = this.$refs.table4.bodyWrapper;
      this.initRollTable(divData1);
      this.initRollTable(divData2);
      this.initRollTable(divData3);
      this.loadReservoirArea();
      // this.initRollTable(divData4);
    })
  },
  methods:{
    initRollTable(dom){
      setInterval(() => {
        // 元素自增距离顶部1像素
        dom.scrollTop += 1
        // 判断元素是否滚动到底部(可视高度+距离顶部=整个高度)
        if (dom.clientHeight + dom.scrollTop == dom.scrollHeight) { dom.scrollTop = 0 }// 重置table距离顶部距离
      }, 100);
    },
    toggleSideBarLeft(){
      this.showBtnLeft = false;
      this.barshowLeft = !this.barshowLeft;
    },
    transitionDoneLeft() {
      this.showBtnLeft = true;
    },
    transitionDone() {
      this.showBtn = true;
    },
    toggleSideBar() {
      this.showBtn = false;
      this.barshow = !this.barshow;
    },
    //实时雨量选中改变
    handleCheckedRealLayerChange(val){
      this.$refs.map.clearIsohyetLayer();
      if(val.length > 0){
        this.mapRainfallLoading = true;
        if(val.length>1){
          this.checkedRealLayer.shift();
        }
        this.getRealRain(this.checkedRealLayer[0]);
      }
    },
    //预报雨量选中改变
    handleCheckedExpectLayerChange(val){
      this.mapRainfallLoading = true;
      //清空实时雨量
      this.checkedRealLayer = [];
      if(val.length>1){
        this.checkedExpectLayer.shift();
      }
      this.getRainfallPredict();
    },
    //实时+预报雨量选中改变
    handleCheckedMixChange(){

    },
    getDays(num) {
      //获取系统当前时间
      var leng = num;
      var now = new Date();
      var nowTime = now.getTime();
      var oneDayTime = 24 * 60 * 60 * 1000;
      var timeArr = [];
      for (var i = 0; i < leng; i++) {
        //显示星期
        var ShowTime = nowTime + i * oneDayTime;
        //初始化日期时间
        var myDate = new Date(ShowTime);
        var str = "周" + "日一二三四五六".charAt(myDate.getDay());
        timeArr.push(str);
      }
      return timeArr;
    },
    //获取气象数据
    async getWeatherPredict(){
      let res = await weatherPredict('');
      let icon = "icon-weather-qing";
      let outTime = this.getDays(7);
      if (res.data.length) {
        this.weatherList = [];
        res.data.map((v, i) => {
          if (i < 7) {
            switch (v.wpname) {
              case "晴":
              case "多云转晴":
                icon = "icon-weather-qing";
                break;
              case "阴":
              case "小雨转多云":
              case "晴转多云":
              case "多云":
                icon = "icon-weather-yin";
                break;
              case "雨":
              case "小雨":
              case "大雨":
              case "阴转小雨":
                icon = "icon-weather-yu";
                break;
            }
            this.weatherList.push({
              time: outTime[i],
              maxTemp: parseFloat(v.maxTemp).toFixed(0),
              minTemp: parseFloat(v.minTemp).toFixed(0),
              icon: icon,
            });
          }
        });
      }
    },
    //获取雨量top10
    async getRainfallTop10(){
      let opt = {
        hours:this.rainActive
      }
      let res = await rainApi.rainfallTop10(opt);
      this.rainStationArr = res.data;
      this.rainLoading = false;
      this.$refs.map.rainPoint(res.data);
    },
    //雨量数据转换成等雨量面参数格式
    formatRainfallParam(list,paramName){
      let geoJson = {
        "features":[],
      }
      let featuresArr = [];
      list.forEach(item => {
        // let drp = item.drp > 0 ? item.drp : 0;
        if(item[paramName] != '-'){
          let obj = {
            "attributes":{
              "x":item.longitude,
              "y":item.latitude,
              "value":item[paramName]
            },
            "geometry":{
              "x":item.longitude,
              "y":item.latitude,
            }
          }
          featuresArr.push(obj);
        }
      });
      geoJson.features = featuresArr;
      return geoJson;
    },
    //获取实时雨量
    async getRealRain(hour){
      let _this = this;
      let minuteObj = {
        '1h':60,
        '3h':180,
        '6h':360
      }
      let minutes = minuteObj[hour];
      this.$store.dispatch("getRealRainList", {
        params: {
          minutes: minutes,
        },
        callback: (list) => {
          let opt = _this.formatRainfallParam(list,'drp');
          _this.getIsohyetArea(opt);
        },
      });
      // console.log(res);
    },
    //获取等雨量面
    async getIsohyetArea(opt){
      let params = new URLSearchParams();
      params.append("station", JSON.stringify(opt));
      params.append("f", "json");
      let _this = this;
      await axios({
        url:
          "http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/GPServer/dengzhiPolygonIDW/submitJob",
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then(async (res) => {
        let interVal = setInterval(async ()=>{
          let url = "http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/GPServer/dengzhiPolygonIDW/jobs/"+res.data.jobId+"/results/Idwres?f=pjson";
          await axios({
              url:url,
              method: "get",
            }).then(async (result) => {
              if(!result.data.value.mapImage.error){
                clearInterval(interVal);
                //加载透明等雨量面
                let mapImage = result.data.value.mapImage.extent;
                _this.getIsohyetOpacity(mapImage.xmin,mapImage.ymin,mapImage.xmax,mapImage.ymax,res.data.jobId);
                // _this.$refs.waterWorkMap.showIsohyetLayer(result.data.value.mapImage);
              }
            })
          // console.log(res.data);
        },1000);
      })
    },
    //获取透明背景等雨量面
    async getIsohyetOpacity(xmin,ymin,xmax,ymax,jobId){
      let bbox = xmin + ',' + ymin + ',' + xmax + ',' + ymax;
      let params = new URLSearchParams();
      params.append("bbox", bbox);
      params.append("f", "pjson");
      params.append("transparent", true);
      let _this = this;
      let url = "http://117.149.227.112:6080/arcgis/rest/services/gps/dengzhiPolygonIDW/MapServer/jobs/"+jobId+"/export";
      await axios({
        url:url,
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then(res=>{
        _this.$refs.map.showIsohyetLayer(res.data);
        this.mapRainfallLoading = false;
      })
    },
    //获取预报雨量
    async getRainfallPredict(){
      let hourObj = {
        '1h':1,
        '3h':3,
        '6h':6
      }
      let hour = hourObj[this.checkedExpectLayer[0]];
      let opt = {
        hour: hour,
      }
      let res = await rainApi.rainfallPredict(opt);
      let params = this.formatRainfallParam(res.data,'drp');
      this.getIsohyetArea(params);
      // this.$refs.map.rainPoint(res.data);
    },
    //1/3/6小时预报的面雨量
    async getSurfaceRainfall(){
      let res = await rainApi.getSurfaceRainfall();
      this.waterDeep=[];
      if(res.code == 0){
        let list = res.data;
        let rainArr = [];//1 3 6小时雨量测站数据
        list.forEach(item => {
          if(item.rainData.length>0){
            rainArr = rainArr.concat(item.rainData);
          } 
        })
        //获取1h面雨量
        let param1 = this.formatRainfallParam(rainArr,'rainfall_1');
        this.getAreaRainFall(param1,'rainfall_1').then(()=>{
          //获取3h面雨量
          let param3 = this.formatRainfallParam(rainArr,'rainfall_3');
          this.getAreaRainFall(param3,'rainfall_3').then(()=>{
            //获取6h面雨量
            let param6 = this.formatRainfallParam(rainArr,'rainfall_6');
            this.getAreaRainFall(param6,'rainfall_6').then(()=>{
              // console.log(this.waterFallObj);
              for(let key in this.waterFallObj){
                this.totalOne += this.waterFallObj[key].rainfall_1;
                this.totalThree += this.waterFallObj[key].rainfall_3;
                this.totalSix += this.waterFallObj[key].rainfall_6;
                let obj = {
                  areaName:key,
                  rainfall_1:this.waterFallObj[key].rainfall_1.toFixed(2),
                  rainfall_3:this.waterFallObj[key].rainfall_3.toFixed(2),
                  rainfall_6:this.waterFallObj[key].rainfall_6.toFixed(2)
                }
                this.waterDeep.push(obj);
              }
              this.rainfallLoading = false;
            })
          })
        });
      }
    },
    //获取面雨量数据
    async getAreaRainFall(param,type){
      let params = new URLSearchParams();
      params.append("inputStations", JSON.stringify(param));
      params.append("f", "json");
      await axios({
        url:
          "http://117.149.227.112:6080/arcgis/rest/services/gps/voronoiSimple/GPServer/voronoiSimple/execute",
        method: "post",
        headers: { "content-type": "application/x-www-form-urlencoded" },
        data: params,
      }).then(async (res) => {
        let features = res.data.results[0].value.features;
        features.forEach(item=>{
          let obj = new Object();
          if(this.waterFallObj[item.attributes.NAME]){
            obj = this.waterFallObj[item.attributes.NAME];
          }
          obj[type] = item.attributes.SUM_RainfallQZ;
          this.waterFallObj[item.attributes.NAME] = obj;
        })
      })
    },
    //雨量top10
    rowClick(item){
      console.log(item);
    },
    //大中型水库水位
    async getReservoirWaterAndRain(){
      let res = await rainApi.reservoirWaterAndRain();
      this.sluiceArr = res.data;
      // console.log(res);
    },
    rainTabsClick(data){
      this.rainLoading = true;
      this.rainActive = data;
      this.getRainfallTop10();
    },
    //最接近汛限水位水库
    async getTopNRes(){
      let res = await rainApi.getTopNRes();
      this.sluiceTopArr = res.data;
      // console.log(res);
    },
    //水库超汛限个数
    async getBeyondNumber(){
      let res = await rainApi.getBeyondNumber();
      this.floodReservoir = res.data;
      
    },
    //雨量1小时超警个数
    async getSuperRainNumber(){
      let res = await rainApi.getSuperRainNumber();
      this.oneHourStation = res.data;
      // console.log(res);
    },
    //加载水库水域面
    loadReservoirArea(){
      let opt = {
        id: "RV_sym",
        label: '河道水域面',
        index: "7"
      };
      this.$refs.map.lodeRiverFeatureLayers(opt);
    }
  }
 }
</script>

<style lang="scss" scoped>
@import "@/style/bigScreen.scss";
  .water-rain-analysis{
    width: 100%;
    height: 100%;
    // ::v-deep.el-loading-mask{
    //   z-index:10000000;
    // }
    ::v-deep.el-table__body-wrapper::-webkit-scrollbar {
        width: 0px;
    }
    .table-order{
      display: inline-block;
      width: 26px;
      height: 26px;
      line-height: 26px;
    }
    .rank-first{
      background: url(~assets/images/rank-first.png) no-repeat
       top center;
    }
    .rank-second{
      background: url(~assets/images/rank-second.png) no-repeat
       top center;
    }
    .rank-third{
      background: url(~assets/images/rank-third.png) no-repeat
       top center;
    }
    .layer-control {
      transition: left 0.6s ease-in-out;
      position: absolute;
      bottom: 80px;
      left: 490px;
      z-index: 1999;
      &-item {
        color: #FFFFFF;
        > p {
          line-height: 28px;
          font-size: 12px;
        }
        background-color: #060e36;
        padding: 4px 10px 10px 10px;
        margin-bottom: 12px;
        ::v-deep.el-checkbox{
          color: #FFFFFF;
        }
      }
      .layer-control-box {
        display: flex;
        flex-direction: column;
      }
    }

    .weather-forecast{
      width: 428px;
      height: 126px;
      position: absolute;
      top: 75px;
      left: 25px;
      @include flexbox;
      @include flexflow(row,nowrap);
      .weather-item{
        @include flex(1);
        font-size: 14px;
        color: #FFFFFF;
        div{
          text-align: center;
          &.weather-type{
            margin: 8px 0 16px;
            i{
              display: inline-block;
              width: 36px;
              height: 36px;
            }
          }
        }
      }
    }
  }

  .bigScreen-left {
    .total-info {
      width: 100%;
      height: 100px;
      background: url(~assets/images/popup-bg2.png) no-repeat center center;
      background-size: 100% 100%;
      padding: 20px;
      .info-title {
        color: #23e3fb;
        font-size: 16px;
      }
      .info-content {
        padding: 16px 0;
        .info-item {
          width: 33%;
          float: left;
          @include flexbox;
          @include flexAC;
          span {
            &:first-child {
              text-align: center;
              display: inline-block;
              width: 20px;
              height: 20px;
              line-height: 20px;
              color: #a6dff6;
              font-size: 14px;
              margin-right: 10px;
              box-shadow: inset 0 0 13px 0 #3c93f8;
              border-radius: 50%;
            }
          }
        }
      }
    }
  }


</style>
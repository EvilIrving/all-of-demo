import { weatherPredict } from "@/api/hydraulic_amap/weather";
//气象相关操作module
export const weatherModule = {
  //风场时间轴
  async getWindTimes() {
    let res = await weatherPredict.windFarmsTimeList();
    this.windTimes = res.data;
  },
  //风场数据
  async getWindDatas(id) {
    //获取风场数据
    let res = await weatherPredict.getWindDataList(id);
    if (res.code == 0) {
      //解析风场数据，获取风场字符串
      // console.log(res, "===================");
      let timeDataArr = res.data.predictTime.split(" ");
      let timeStr =
        timeDataArr[0].replace(new RegExp("-", "g"), "") +
        timeDataArr[1].slice(0, 2);
      this.windDatas = JSON.parse(res.data.dataJson)[timeStr];
    }
  },
  //浪场时间轴
  async getWaveTimes() {
    let res = await weatherPredict.waveFieldList();
    this.waveTimes = res.data;
    // console.log(this.waveTimes, "this.waveTimes");
  },
  //浪场数据
  async getWaveDatas(id) {
    //获取浪场数据
    let res = await weatherPredict.getWaveDataList(id);
    //解析浪场数据，获取浪场字符串
    // let timeDataArr = res.data.predictTime.split(" ");
    // let timeStr = timeDataArr[0].replace(new RegExp("-","g"),"") + timeDataArr[1].slice(0,2);
    // this.waveDatas = JSON.parse(res.data.dataJson)[timeStr];
    this.waveDatas = JSON.parse(res.data.dataJson).data.data;
    console.log(this.waveDatas, "getWaveDatas=========");
  },
  //风场控制
  controlWind(isShow) {
    if (isShow) {
      this.getWindDatas(this.windTimes[0].id);
      let setInterF = setInterval(() => {
        if (this.windDatas != null) {
          this.MapComponent.addWatherLayer(this.windDatas, "wind");
          clearInterval(setInterF);
        }
      }, 2000);
    } else {
      this.MapComponent.closeWeatherLayers("wind");
    }
  },
  controlWave(isShow) {
    if (isShow) {
      this.getWaveDatas(this.waveTimes[0].id);
      let setInterL = setInterval(() => {
        // console.log(this.windDatas, "this.windDatas");
        if (this.waveDatas != null) {
          this.MapComponent.addWatherLayer(this.waveDatas, "wave");
          clearInterval(setInterL);
        }
      }, 2000);
    } else {
      this.MapComponent.closeWeatherLayers("wave");
    }
  },
  //卫星云图
  async loadClound(isShow) {
    if (isShow) {
      let res = await weatherPredict.getWenzhouCloudData();
      if (res.code == 0 && res.data.length > 0) {
        let imgUrl = res.data[res.data.length - 1].url;
        this.MapComponent.loadCloundImg(imgUrl);
      }
    } else {
      this.MapComponent.removeLayerByName("cloundLayer");
    }
  },
  //雷达降雨
  async radarRain(isShow) {
    if (isShow) {
      let res = await weatherPredict.getWenzhouRadarData();
      if (res.code == 0 && res.data.length > 0) {
        let imgUrl = res.data[res.data.length - 1].url;
        this.MapComponent.loadRadarRainImg(imgUrl);
      }
    } else {
      this.MapComponent.removeLayerByName("radarRainLayer");
    }
  }
}
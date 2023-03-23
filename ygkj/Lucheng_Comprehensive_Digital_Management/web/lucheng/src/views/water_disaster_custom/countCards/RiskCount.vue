<!--
 * @Author: dyn
 * @Date: 2021-07-01 18:29:34
 * @LastEditTime: 2022-03-11 09:27:29
 * @LastEditors: dtb
 * @Description: 风险隐患统计卡片
 * @FilePath: \lucheng\src\views\water_disaster_custom\countCards\RiskCount.vue
-->
<template>
  <count-wrap title="风险隐患" class="risk-count">
    <el-row class="has-count">
      <el-col :span="12" class="fs24">
        <p @click="handleRiskChange('251')" style="cursor:pointer">
          隐患点未整改<span class="orange ml12 mr12 fs36">{{ hiddenDanger }}</span>个
        </p>
      </el-col>
      <el-col :span="12" class="fs24">
        <p @click="handleRiskChange('253')" style="cursor:pointer">
          薄弱点未整改<span class="yellow fs36 ml12 mr12">{{ weakPoints }}</span>个
        </p>
      </el-col>
    </el-row>
  </count-wrap>
</template>

<script>
import CountWrap from "../components/CountWrap.vue";
import { disasterPreventionApi } from "@/api/RightSildePanel";
export default {
  name: "RiskCount",
  data() {
    return {
      hiddenDanger: 0,
      weakPoints: 0,
      timer: null,
      riskPointList: [], //风险点集合
    };
  },
  components: { CountWrap },
  computed: {
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  mounted() {
    this.cancellationHiddenDangerStatic();
    this.cancellationWeakPointsStatic();
    this.getbusRiskListPage();
    this.timer = setInterval(() => {
      this.cancellationHiddenDangerStatic();
      this.cancellationWeakPointsStatic();
      this.getbusRiskListPage();
    }, 300000);
  },
  methods: {
    /**
     * 隐患点销号情况
     */
    async cancellationHiddenDangerStatic() {
      let res = await disasterPreventionApi.cancellationHiddenDangerStatic();
      if (res.code == 0) {
        const index = res.data.findIndex((item) => item.reccondition == "未整改");
        this.hiddenDanger = res.data[index]?res.data[index].num:0;
      }
    },
    /**
     * 薄弱点销号情况统计
     */
    async cancellationWeakPointsStatic() {
      let res = await disasterPreventionApi.cancellationWeakPointsStatic();
      if (res.code == 0) {
        const index = res.data.findIndex((item) => item.reccondition == "未整改");
        this.weakPoints = res.data[index].num;
      }
    },
    /**
     * 风险清单list
     */
    async getbusRiskListPage() {
      let res = await disasterPreventionApi.busRiskListPage({
        pageNum: 1,
        pageSize: 6000
      });
      if (res.code == 0) {
        this.riskPointList = res.data.list;
      }
    },
    handleRiskChange(type) {
      this.map.drawClusterPoint([], {}, 'riskLayer');
      this.map.drawClusterPoint([], {}, 'dangerLayer');
      this.map.drawClusterPoint([], {}, 'weakLayer');
      this.$emit('legend-change', '风险隐患', {});
      let style = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/images/MapPoint/risk-group.png'),
        src: require('@/assets/images/MapPoint/risk-point.png')
      };
      let style1 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/images/MapPoint/hiddentrouble-group.png'),
        src: require('@/assets/images/MapPoint/hiddentrouble-point.png')
      };
      let style2 = {
        scale: 0.7,
        anchor: [27, 30],
        anchorXUnits: 'pixels',
        srcs: require('@/assets/images/MapPoint/weakness-group.png'),
        src: require('@/assets/images/MapPoint/weakness-point.png')
      };
      let pointList = []; //高风险点列表
      let dangerList = []; //隐患点列表
      let weakList = []; //薄弱点列表
      for (let point of this.riskPointList) {
        if (point.lat && point.lng) {
          let obj = {
            整改情况: point.recCondition,
            工程类型: point.projType,
            行政区划: point.county,
            整改责任单位: point.dutyUnit,
            上报人员: point.reportPerson,
            完成时间: point.reportTime
          };
          if (point.sbSign === '252' && point.recCondition === '未整改') {
            //高风险点
            pointList.push({
              name: point.projName,
              type: '风险隐患',
              sbSign: point.sbSign,
              lng: Number(point.lng),
              lat: Number(point.lat),
              code: point.sbId,
              props: obj
            });
          } else if (point.sbSign == '251' && point.recCondition === '未整改') {
            //隐患点
            obj = {
              ...obj,
              隐患类型: point.dangerType
            };
            dangerList.push({
              name: point.projName,
              type: '风险隐患',
              sbSign: point.sbSign,
              lng: Number(point.lng),
              lat: Number(point.lat),
              code: point.sbId,
              props: obj
            });
          } else if (point.sbSign == '253' && point.recCondition === '未整改') {
            //薄弱点
            weakList.push({
              name: point.projName,
              type: '风险隐患',
              sbSign: point.sbSign,
              lng: Number(point.lng),
              lat: Number(point.lat),
              code: point.sbId,
              props: obj
            });
          }
        }
      }
      if (type === '251') {
        // this.map.drawClusterPoint(pointList, style, 'riskLayer');
        this.map.drawClusterPoint(dangerList, style1, 'dangerLayer');
        // this.map.drawClusterPoint(weakList, style2, 'weakLayer');
        // this.$emit('legend-change', '风险隐患', {
        //   label: '风险隐患',
        //   clusterLayer: ['riskLayer', 'dangerLayer', 'weakLayer'],
        //   children: [
        //     {
        //       label: '隐患点',
        //       icon: 'icon-hiddentrouble-point'
        //     },
        //     {
        //       label: '高风险点',
        //       icon: 'icon-risk-point'
        //     },
        //     {
        //       label: '薄弱点',
        //       icon: 'icon-weakness-point'
        //     }
        //   ]
        // });
      } else {
        this.map.drawClusterPoint(weakList, style2, 'weakLayer');
      }
    }
  },
};
</script>
<style lang="scss" scoped>
@import './style/CountCard';
.risk-count .has-count {
  height: 100%;
  .el-col {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }
}
</style>

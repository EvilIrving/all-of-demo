<template>
  <div class="tyhoon-card">
    <page-pane v-model="pageName" @change="change">
      <page-pane-item name="analysisTyphoon" label="台风分析">
        <!-- 别处使用此组件就用这个事件 -->
        <AnalysisTypoon
          @selectTyphoon="onSelectTyphoon"
          @rowClick="onRowClick"
          @showTyphoonCircle="showTyphoonCircle"
          @legend-change="onLegendchange"
        />
        <!--   -->
      </page-pane-item>
      <page-pane-item name="currentTyphoon" label="当前台风">
        <CurrentTypoon ref="currentTyphoon" />
      </page-pane-item>
    </page-pane>
  </div>
</template>

<script>
import vueSeamlessScroll from 'vue-seamless-scroll';
import EchartInstrument from '../charts/echart_instrument/EchartInstrument.vue'; // 仪表盘图
import { getTyphoonDetail } from '@/api/typhoon.js';
import AnalysisTypoon from './CardPages/AnalysisTyphoon/index.vue';
import CurrentTypoon from './CardPages/CurrentTyphoon/index.vue';
import { PagePane, PagePaneItem } from '../components/PagePane';
import { mapActions, mapGetters } from 'vuex';

const layerList = ['水库', '山塘', '堤防', '海塘', '泵站', '水闸', '闸站', '灌区', '农饮水', '水电站'];

export default {
  name: 'TyphoonCard',
  layerName: ['台风', 'typhoonRiskAreaLayer', 'probabilityCircleLayer'],
  legendName: '台风登陆次数',
  components: {
    EchartInstrument,
    vueSeamlessScroll,
    PagePane,
    PagePaneItem,
    AnalysisTypoon,
    CurrentTypoon
  },
  provide() {
    return {
      typhoonCard: this
    };
  },
  beforeDestroy() {
    this.ScreenMap.clearAllTyphoon();
  },
  computed: {
    ...mapGetters(['ScreenMap', 'TyphoonPointData'])
  },
  watch: {
    TyphoonPointData(val) {
      if (val) {
        // this.pageName = "currentTyphoon";
      }
    }
  },
  data() {
    return {
      pageName: 'analysisTyphoon',
      currentTypoon: {},
      circleCode: '' //风圈显示台风标记
    };
  },
  methods: {
    ...mapActions(['SetTyphoonPointData', 'SetActiveCount']),
    onRowClick(data) {
      // console.log(11);
      getTyphoonDetail({ code: data.code }).then((res) => {
        if (res.code == 0) {
          this.currentTypoon = res.data;
          // this.pageName = "currentTyphoon";
          this.$refs.currentTyphoon.initTyphoon(this.currentTypoon);
          //有实时台风第一次加载时 选中最后一个点
          if (data.firstFlag) {
            const points = eval(res.data.points);
            // console.log(points,999)
            const { lng, lat, move_speed, power, pressure, speed, time, tfbh, typhoonInfo } = points[points.length - 1];
            const { ne, nw, se, sw } = points[points.length - 1].radius10_quad;
            const point = { lng, lat, ne, nw, se, sw };
            const param = {
              lng,
              lat,
              move_speed,
              power,
              pressure,
              speed,
              point,
              time,
              code: tfbh,
              typhoonInfo
            };
            this.SetTyphoonPointData(param);
          }
          this.$forceUpdate();
        }
      });
    },
    //加载台风70%概率风圈
    showTyphoonCircle(code) {
      this.circleCode = code;
      getTyphoonDetail({ code }).then((res) => {
        const featureStr = res.data.feature;
        const feature = JSON.parse(featureStr);
        // console.log(feature, 333);
        this.ScreenMap.addTyphoonProbabilityCircle(feature.geometry.coordinates);
      });
    },
    onSelectTyphoon({ newChecked, unChecked }) {
      unChecked.forEach((item) => {
        this.ScreenMap.clearTyphoon(item.code);
        if (item.code == this.circleCode) {
          this.ScreenMap.removeProbabilityCircle();
        }
        // this.$emit("clearTyphoon", item.code);
      });
      newChecked.some((item, index) => {
        this.getTyphoonDetail(item.code);
      });
    },
    getTyphoonDetail(code) {
      getTyphoonDetail({ code }).then((res) => {
        if (res.code == 0) {
          let points = eval(res.data.points);
          this.$refs.currentTyphoon.initTyphoon(res.data);
          const data = {
            ...res.data,
            points
          };
          this.$emit('showTyphoon', data);
        }
      });
    },
    // 标题点击打开统计卡片
    change(name) {
      if (name == 'currentTyphoon') {
        this.SetActiveCount('台风');
      }
      // console.log("ontitle", name);
    },
    onLegendchange(label, data) {
      this.$emit('legend-change', label, data);
    }
  }
};
</script>
<style lang="scss">
.tyhoon-card {
  height: 100%;
  .el-tabs {
    display: flex;
    flex-direction: column;
    .el-tabs__header {
      .el-tabs__nav {
        border: 0;
        .el-tabs__item {
          color: #fff;
          opacity: 0.6;
          border: 1px solid #ffffff;
          line-height: 32px;
          height: 32px;
          font-size: 16px;
          &.is-active {
            opacity: 1;
            color: #56fefe;
            box-shadow: inset 0 0 5px 0 #56fefe;
            background: #4898fb1a;
            border: 1px solid #56fefe;
          }
        }
      }
    }
    .el-tabs__content {
      height: 100%;
      .el-tab-pane {
        height: 100%;
        /** 所有的el-input样式 */
        .el-input {
          .el-input__inner {
            height: 32px;
            border: 1px solid #ffffff;
            border-radius: 4px;
          }
          .el-input__icon {
            line-height: 32px;
          }
        }
        .el-radio-group {
          .el-radio {
            .el-radio__label {
              color: #fff;
            }
          }
        }

        .el-checkbox {
          &.is-checked {
            .el-checkbox__input.is-checked {
              .el-checkbox__inner {
                border-color: #56fefe;
                &::after {
                  border: 1px solid #56fefe;
                  border-left: 0;
                  border-top: 0;
                }
              }
            }
            .el-checkbox__label {
              color: #56fefe;
            }
          }
        }
      }
    }
  }
}
</style>

<template>
  <div class="water-efficiency-card">
    <PagePane title="用水效率" @onTitle="tclick('用水效率')">
      <template #append>
        <RadioDropdown v-model="year" :options="yearOption" label-suffix="年" @change="handleYear" />
        <i title="查看详表" class="icon-liebiao icon-sty" @click.stop="openDetailsDialog('用水效率')" />
      </template>
      <el-carousel trigger="click" arrow="never" :autoplay="false" :interval="5000" @change="onCarouselChange">
        <el-carousel-item class="carousel-item-content">
          <CapsuleTabs :tabs="['万元GDP用水量', '万元工业增加值用水量']" @change="handleTabChange" />
          <div class="area-box">
            <p style="text-align: right">单位：m³/万元</p>
            <el-row :gutter="14" class="bar_line">
              <el-col :span="6">{{ `1 ${firstData.adnm || '温州市'}` }}</el-col>
              <el-col :span="15">
                <el-progress
                  :show-text="false"
                  :stroke-width="14"
                  :percentage="getPercent(firstData)"
                  :color="'#19AF47'"
                />
              </el-col>
              <el-col :span="3" :style="{ color: '#19AF47' }">{{ firstData.value || 0 }}</el-col>
            </el-row>
            <div class="bar_box">
              <el-row v-for="(item, index) in restData" :key="index" :gutter="14" class="bar_line">
                <el-col :span="6">{{ `${index + 2} ${item.adnm}` }}</el-col>
                <el-col :span="15">
                  <el-progress :show-text="false" :stroke-width="8" :percentage="getPercent(item)" :color="'#1df8f3'" />
                </el-col>
                <el-col :span="3" :style="{ color: '#1df8f3' }">{{ item.value }}</el-col>
              </el-row>
            </div>
          </div>
        </el-carousel-item>
        <el-carousel-item class="carousel-item-content">
          <ve-line v-bind="varietyOptions" :data="varietyData" height="220px" width="auto" judge-width></ve-line>
        </el-carousel-item>
      </el-carousel>
    </PagePane>
    <WaterEfficiencyDetail :visible.sync="detailVisible" />
  </div>
</template>
<script>
import { defineComponent } from '@vue/composition-api';
import { mapGetters, mapActions } from 'vuex';
import WaterEfficiencyDetail from './dialog/WaterEfficiencyDetail.vue';
import { getUnitCard4WatConEffect, getUnitCWindows4WatConEffect } from '@/api/micro-service/wra';
import PagePane from '../components/PagePane/PagePane.vue';
import CapsuleTabs from '@/views/WaterDisasterPreventionScreen/components/CapsuleTabs.vue';
import RadioDropdown from '../components/RadioDropdown.vue';

const CURRENT_YEAR = new Date().getFullYear();

export default defineComponent({
  name: 'WaterEfficiencyCard',
  components: { PagePane, WaterEfficiencyDetail, CapsuleTabs, RadioDropdown },
  data() {
    return {
      carouselIndex: 0,
      activeTab: 1,
      detailVisible: false,
      timer: null,
      year: CURRENT_YEAR,
      yearOption: new Array(CURRENT_YEAR + 1 - 1990)
        .fill(0)
        .map((_, year) => 1990 + year)
        .reverse(),
      yearDropdownVisible: false,

      totalLength: 0,
      maxValue: 0,
      varietyOptions: {
        settings: {
          labelMap: { year: '年份', wpg: '万元GDP用水量' },
          yAxisName: ['m³/万元']
        },
        extend: {
          grid: { top: '20%', bottom: '10%', containLabel: true },
          textStyle: { color: '#fff' }
        },
        legend: { selectedMode: false, textStyle: { color: '#fff' } },
        afterConfig: (v) => ({ ...v, darkMode: true })
      },
      varietyData: {
        columns: ['year', 'wpg'],
        rows: []
      },
      rankingData: []
    };
  },
  computed: {
    ...mapGetters(['previewModule']),
    map() {
      return this.$store.state.screen.mapComponent;
    },
    firstData() {
      return this.rankingData[0] || { adnm: '温州市', value: 0 };
    },
    restData() {
      // eslint-disable-next-line no-unused-vars
      const [first, ...rest] = this.rankingData;
      return /** @type {any[]} */ (rest);
    },
    sumData() {
      if (this.rankingData.length == 0) return 0;
      return this.rankingData.map((item) => item.value).reduce((a, b) => a + b);
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.getOverView();
      this.getCenterData();
      this.timer = setInterval(() => {
        this.getOverView();
        this.getCenterData();
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(['SetDetailDialogParam', 'SetWaterEfficiencyCount']),
    getPercent(item) {
      return item.value != 0 ? Number((item.value / this.sumData) * 100) : 0;
    },
    // 获取用水效率数据
    async getOverView() {
      const opt = { targetType: this.carouselIndex + 1, type: this.activeTab, year: this.year };
      await getUnitCard4WatConEffect(opt).then((res) => {
        if (res.code == 0) {
          const data = res.data.map((item) => ({
            adnm: item.adnm || '-',
            year: `${item.year}年`,
            value: item[this.activeTab == 1 ? 'watPerGdp' : 'wpgInduDelta'] || 0,
            wpg: item.wpg
          }));
          if (this.carouselIndex == 0) {
            this.rankingData = data;
            console.log(this.rankingData,'用水情况数据');
          } else {
            this.varietyData.rows = data;
          }
          this.$forceUpdate();
        }
      });
    },
    // 获取C位数据
    async getCenterData() {
      getUnitCWindows4WatConEffect({ year: this.year }).then((res) => {
        this.SetWaterEfficiencyCount(res.data);
      });
    },
    /** 打开详情弹框点击事件 */
    openDetailsDialog() {
      this.detailVisible = true;
    },
    /** 标题点击打开统计卡片 */
    tclick(title) {
      this.$emit('tclick', title);
    },
    handleTabChange(val) {
      this.activeTab = val + 1;
      this.getOverView();
    },
    // 下拉年份选择后
    handleYear() {
      this.getOverView();
      this.getCenterData();
    },
    onCarouselChange(index) {
      this.carouselIndex = index;
      this.getOverView();
    }
  }
});
</script>
<style lang="scss" scoped>
@import './style/SildePanel.scss';
.water-efficiency-card {
  ::v-deep.el-carousel {
    height: 100%;
    .el-carousel__container {
      height: 100%;
    }
  }
  width: 100%;
  height: 100%;
  .area-box {
    width: 100%;
    height: calc(100% - 34px);
    padding: 8px;
    overflow: hidden;
    position: relative;
    .bar_box {
      position: absolute;
      overflow: hidden scroll;
      height: calc(100% - 3.75rem);
      width: 99%;
      .bar_line {
        margin-top: 0;
        width: 98%;
      }
    }
    .bar_line {
      width: 100%;
      margin: 1rem;
      display: flex;
      align-items: center;
    }
  }

  .el-dropdown {
    cursor: pointer;
    top: -2px;
    right: 10px;
    .el-dropdown-link {
      display: flex;
      align-items: center;
      &.dropdown-active {
        span,
        i {
          color: #1cfff5;
        }
      }
    }
  }
  .el-icon-caret-bottom {
    margin-left: 3px;
  }
}
#barChart {
  width: 100%;
  height: 100%;
}
</style>

<style lang="scss" scoped>
.icon-sty {
  cursor: pointer;
  margin-right: 10px;
}
</style>

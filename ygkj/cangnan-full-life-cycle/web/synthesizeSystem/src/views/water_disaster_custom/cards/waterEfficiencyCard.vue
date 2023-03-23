<!--
 * @Autor: ysl
 * @Date: 2021-08-11 17:46:22
 * @LastEditors: ysl
 * @LastEditTime: 2021-08-13 15:29:04
 * @Description: 用水效率
 * @FilePath: /synthesizeSystem/src/views/water_disaster_custom/cards/waterEfficiencyCard.vue
-->
<template>
  <div class="water-efficiency-card">
    <panel-item
      title="用水效率"
      has-switch
      tc
      @tclick="tclick('用水效率')"
      :action-tabs="['万元GDP用水量', '万元工业增加值用水量']"
      @change="handleTabChange"
    >
      <template slot="label-description"></template>

      <template slot="title-switch">
        <el-dropdown placement="bottom-start" trigger="click">
          <span
            class="el-dropdown-link"
            :class="yearDropdownVisible ? 'dropdown-active' : ''"
          >
            <span style="font-size: 1rem">{{ checkType }}</span>
            <i class="el-icon-caret-bottom"></i>
          </span>
          <el-dropdown-menu slot="dropdown" class="year-dropdown">
            <el-dropdown-item v-for="(item, index) in typeOptions" :key="index">
              <el-radio
                v-model="checkType"
                :label="item"
                @change="handleType"
                >{{ item }}</el-radio
              >
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <i
          v-show="!previewModule"
          title="查看详表"
          class="icon-liebiao icon-sty"
          @click.stop="openDetailsDialog('用水效率')"
          style="margin-left: 10px"
        />
      </template>

      <div class="area-box">
				<p style="text-align: right">单位：m³/万元</p>
				<el-row :gutter="14" class="bar_line">
					<el-col :span="6">{{ `1 ${ firstData.adnm || '温州市' }` }}</el-col>
						<el-col :span="15">
							<el-progress 
								:show-text="false"
								:stroke-width="14" 
								:percentage="((firstData.value || 0) / maxValue) * 100" 
								:color="'#19AF47'"></el-progress>
						</el-col>
						<el-col :span="3" :style="{'color': '#19AF47'}">{{ firstData.value || 0 }}</el-col>
				</el-row>
				<div class="bar_box" v-if="barDataList.length > 1">
					<el-row v-for="(item, index) in barDataList" :key="index" :gutter="14" class="bar_line">
						<el-col :span="6">{{ `${ index + 2 } ${ item.adnm }` }}</el-col>
						<el-col :span="15">
							<el-progress 
								:show-text="false"
								:stroke-width="8" 
								:percentage="(item.value / maxValue) * 100" 
								:color="'#1df8f3'"></el-progress>
						</el-col>
						<el-col :span="3" :style="{'color': '#1df8f3'}">{{ item.value }}</el-col>
					</el-row>
				</div>
      </div>
    </panel-item>
  </div>
</template>
<script>
import { mapGetters, mapActions } from "vuex";
import PanelItem from "../components/PanelItem.vue";
import { generateDataZoom } from "./utils/chart.js";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";

import * as echarts from "echarts"; // echart插件

export default {
  name: "waterEfficiencyCard",
  components: {
    PanelItem,
  },
  computed: {
    ...mapGetters(["previewModule"]),
    map() {
      return this.$store.state.screen.mapComponent;
    },
  },

  data() {
    return {
      activeTab: 0,
      timer: null,
      typeOptions: [ "省内", "国内", "国际" ],
      yearDropdownVisible: false,
      checkType: "省内",
			
			totalLength: 0,
      type: [
        { label: "万元GDP用水量", value: 1},
        { label: "万元工业增加值用水量", value: 2},
      ],
			maxValue: 0,
			firstData: {},
			barDataList: [
				{ areaName: "丽水", value: 31 },
				{ areaName: "宁波", value: 28 },
				{ areaName: "杭州", value: 27 },
				{ areaName: "嘉兴", value: 25 },
				{ areaName: "瑞安", value: 10 },
			],
    };
  },
  created() {
  },
  mounted() {
    this.$nextTick(() => {
      this.getOverView()
      this.getCenterData()
      this.timer = setInterval(() => {
        this.getOverView()
        this.getCenterData()
      }, 300000);
    });
  },
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    ...mapActions(["SetDetailDialogParam", "SetWaterEfficiencyCount"]),

    // 获取用水效率数据
    async getOverView() {
      let opt = {
        areaLevel: this.checkType,
      };
      this.barDataList = []
      this.firstData = []
      this.maxValue = 0
      await waterResourceApi.wpgEffect(opt).then((res) => {
				if ( res.code == 0 ) {
          this.barDataList = res.data.map(item => {
            item.value = this.activeTab == 0 ? Number(item.wpg).toFixed(2) : Number(item.induWpg).toFixed(2)
            this.maxValue = this.maxValue > item.value ? this.maxValue : item.value
            return item
          })
          this.firstData = res.data.filter(item => {
            return item.adcd == "330300000000"
          })[0]
        }
      });
    },
    // 获取C位数据
    async getCenterData() {
      let opt = {};
      await waterResourceApi.redLine4CWindow(opt).then((res) => {
        let data = res.data.data
        data.wpg = Number(data.wpg).toFixed(2)
        data.induWpg = Number(data.induWpg).toFixed(2)
        this.SetWaterEfficiencyCount(res.data);
      });
    },
		format(percentage) {
      return percentage;
    },
    /**
     * 打开详情弹框点击事件
     */
    openDetailsDialog(val) {
			this.$emit("openDialog", "用水效率")
    },
    // 标题点击打开统计卡片
    tclick(title) {
      this.$emit("tclick", title);
    },
    handleTabChange(val) {
      this.activeTab = val;
      this.getOverView();
    },
    // 下拉对比对象选择后
    handleType() {
      this.getOverView();
    },
  },
};
</script>
<style lang="scss" scoped>
@import "./style/SildePanel.scss";
.water-efficiency-card {
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

<style lang="scss">
.el-dropdown-menu.year-dropdown {
  max-height: 200px;
  overflow: auto;
  background: rgba(2, 10, 28, 0.7);
  //   background: #061639;
  border: 1px solid #1cfff5;
  .el-dropdown-menu__item {
    line-height: 26px;
  }
  .el-radio {
    color: #fff;
  }
  .el-radio__input.is-checked .el-radio__inner {
    border-color: #1cfff5;
    background: transparent;
    &::after {
      width: 8px;
      height: 8px;
      background: #1cfff5;
    }
  }
  .el-radio__input.is-checked + .el-radio__label {
    color: #1cfff5;
  }
  .el-radio__inner {
    background: transparent;
    border: 1px solid #fff;
  }
}
.el-dropdown-menu.year-dropdown.el-popper[x-placement^="bottom"]
  .popper__arrow::after {
  border-bottom-color: rgba(2, 10, 28, 0.7);
}
</style>

<style lang="scss">
.water-efficiency-card .panel-item .panel-item__content--tabs .tab-button,
.water-efficiency-card .panel-item .panel-item__title--tabs .tab-button {
  display: inline-block;
  font-size: 16px;
  padding: 0 12px;
}
.water-efficiency-card .panel-item .panel-item__content--tabs .tab-button.active,
.water-efficiency-card .panel-item .panel-item__title--tabs .tab-button.active {
  padding: 0 12px;
}
</style>

<style lang="scss" scoped>
.icon-sty {
  cursor: pointer;
  margin-right: 10px;
}
.icon-circle {
  width: 12px !important;
  height: 12px !important;
  border-radius: 50%;
}
.icon-color-1 {
  @include chart-icon();
  background: #15E2DB;
}
.icon-color-2 {
  @include chart-icon();
  background: #008fe8;
}
.chart-panel {
  height: 200px;
}
</style>
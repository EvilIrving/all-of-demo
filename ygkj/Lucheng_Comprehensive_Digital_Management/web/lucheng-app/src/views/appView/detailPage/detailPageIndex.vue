<template>
  <div class="wrap-page">
    <mapViewPoint />
    <!--此处样式已被提取成公共样式 在style/util.css  start-->
    <div class="detail-page-title">
      <div class="page-title">
        <div class="title-text">仰义水库</div>
        <div class="tip-text">超汛限</div>
      </div>
      <div class="detail-page">水位：<span>6.4m</span></div>
    </div>
    <!------end------->
    <div class="page-detail-box">
      <wrapTitle title="水情信息" />
      <div class="picker-box">
        <div>
          <span @click="showPicker = true">{{columns[defaultIndex]}}</span>
          <span class="el-icon-caret-bottom"></span>
        </div>
      </div>
      <div class="van-grid-box">
        <div class="van-grid-item" v-for="item in 5" :key="item">
          <div>当前水位</div>
          <div :class="{'green-color': item < 3, 'orange-color': item == 3, 'blue-color': item > 3}">
            <span>62.5</span>
            <span>m</span>
          </div>
        </div>
      </div>
      <div class="echarts-box">
        <div class="echarts-bar-box">
          <EchartsBar :extraOption="extraOption" :widthIndex="defaultIndex" />
        </div>
        <div class="echarts-bar-box">
          <EchartsLine :extraOption="extraOption" />
        </div>
      </div>
      <div>
        <wrapTitle title="水位列表" />
        <div>
          <tableList
            :theadList="theadList"
            :list="list"
            :finished="finished"
            @onLoad="onLoad"
          >
          <template v-slot:tm='item'><span class="orange-color">{{item.data.tm}}</span></template>
          </tableList>
        </div>
      </div>
    </div>
    <!--选择器-->
    <vantPicker
      @onConfirm="onConfirm"
      @onCancel="onCancel"
      :defaultIndex="defaultIndex"
      :showPicker="showPicker"
      :columns="columns"
    />
  </div>
</template>

<script>
import mapViewPoint from "@/components/mapViewPoint.vue";
import wrapTitle from "@/components/wrapTitle.vue";
import vantPicker from "@/components/vantPicker.vue";
import EchartsBar from "@/components/EchartsBar.vue";
import EchartsLine from "@/components/EchartsLine.vue";
import tableList from "@/components/tableList.vue";

export default {
  components: {
    mapViewPoint,
    wrapTitle,
    vantPicker,
    EchartsBar,
    EchartsLine,
    tableList,
  },
  data() {
    return {
      columns: ["杭州", "宁波", "温州", "绍兴", "湖州", "嘉兴", "金华", "衢州"],
      defaultIndex: 0,
      showPicker: false,
      theadList: [
        {
          label: "时间",
          prop: "tm",
          slot: true,
        },
        {
          label: "雨量（mm）",
          prop: "rainfall",
          slot: false,
        },
        {
          label: "水位（m）",
          prop: "waterLevel",
          slot: false,
        },
      ],
      list: [
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
        {
          tm: "2021.12.02 15:22:00",
          rainfall: "2.03",
          waterLevel: "2.03",
        },
      ],
      finished: false,
      extraOption: {
        xAxis:{
          data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
          axisLabel:{
            show: false
          }
        },
        series: [
          {
            data: [82, 93, 90, 93, 129, 133, 132],
            markLine:{
              data:  [
                {
                  yAxis: 100,
                },
              ]
            }
          },
        ],
      },
    };
  },
  methods: {
    onConfirm(data, index) {
      this.defaultIndex = index
      this.showPicker = false;
    },
    onCancel() {
      this.showPicker = false;
    },
    onLoad() {
      setTimeout(() => {
        for (let i = 0; i < 10; i++) {
          this.list.push(this.list[0]);
        }
        // 数据全部加载完成
        if (this.list.length >= 40) {
          this.finished = true;
        }
      }, 1000);
    },
  },
};
</script>

<style lang="scss" scoped>
.page-detail-box {
  margin-top: 6px;
  background-color: #fff;
  .picker-box {
    padding: 0 8px;
    > div {
      border-top: 1px solid #e9f0fd;
      padding: 17px 8px;
      span {
        color: #666666;
        font-size: 12px;
        &:first-child {
          padding-right: 10px;
        }
        &:last-child {
          font-size: 14px;
          color: #aaaaaa;
        }
      }
    }
  }
  .van-grid-box {
    display: flex;
    border-top: 1px solid #e6e6e6;
    border-bottom: 1px solid #e6e6e6;
    .van-grid-item {
      flex: 1;
      padding: 11px 0;
      text-align: center;
      border-right: 1px solid #e6e6e6;
      &:last-child {
        border-right: none;
      }
      > div:first-child {
        font-size: 12px;
        color: #666666;
        font-weight: 600;
      }
      > div:last-child {
        padding-top: 5px;
        > span {
          &:first-child {
            font-size: 16px;
            font-weight: 500;
            line-height: 25px;
          }
          &:last-child {
            font-size: 10px;
          }
        }
      }
    }
  }
  .echarts-box {
    padding-top: 13px;
    .echarts-bar-box {
      height: 180px;
      width: 100%;
      padding: 0 12px;
      box-sizing: border-box;
    }
  }
}
</style>
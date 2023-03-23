<template>
  <div class="wrap-page">
    <van-nav-bar fixed placeholder safe-area-inset-top :title="projectDetail.name"  :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <mapViewPoint v-if="point.length" :point="point" />
    <!--此处样式已被提取成公共样式 在style/util.css  start-->
    <div class="detail-page-title">
      <div class="page-title">
        <div class="title-text">{{ projectDetail.name }}</div>
        <!-- <div class="tip-text">超汛限</div> -->
      </div>
      <div class="detail-page">
        预报高潮位：<span>{{ maxLevel }}米</span>
      </div>
    </div>
    <!------end------->
    <div class="page-detail-box">
      <wrapTitle title="潮位信息" />
      <!-- <div class="picker-box">
        <div>
          <span @click="showPicker = true">{{columns[defaultIndex]}}</span>
          <span class="el-icon-caret-bottom"></span>
        </div>
      </div> -->
      <div class="van-grid-box">
        <div
          class="van-grid-item"
          v-for="(item, index) in propList"
          :key="index"
        >
          <div>{{ item.name }}</div>
          <div>
            <span :class="[item.class ? item.class : 'blue_color']"
              >{{ item['data'] || '-' }}
            </span>
            <span>{{ item.unit }}</span>
          </div>
        </div>
      </div>
      <div class="echarts-box">
        <!-- <div class="echarts-bar-box">
          <EchartsBar :extraOption="extraOption" />
        </div> -->
        <div class="echarts-bar-box">
          <EchartsLine v-if="extraOption.xAxis" :extraOption="extraOption" :widthIndex="defaultIndex" />
        </div>
        <div class="echarts_legend">
            <div>
              <div class="line"></div>
              预报潮位
            </div>
            <div>
              <div class="line" style="background-color: #026eff"></div>
              实时潮位
            </div>
            <div>
              <div class="dashedLine"></div>
              限值线
            </div>
          </div>
      </div>
      <div>
        <wrapTitle title="潮位列表" />
        <div class="table_wrap">
          <tableList :theadList="theadList" :list="tableData">
            <template v-slot:tm="item"
              ><span>{{ item.data.tm }}</span></template
            >
          </tableList>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import echarts from 'echarts'
import { tidalLevelForecast } from '@/api/projectDetail'
import mapViewPoint from '@/components/mapViewPoint.vue'
import wrapTitle from '@/components/wrapTitle.vue'
import vantPicker from '@/components/vantPicker.vue'
import EchartsBar from '@/components/EchartsBar.vue'
import EchartsLine from '@/components/EchartsLine.vue'
import tableList from '@/components/tableList.vue'
const Statuslist = {
  潮位: [
    {
      text: '实时潮位',
      class: 'green_color',
      data: '',
      prop: 'limitLevel',
      unit: 'm',
    },
    {
      text: '预报低潮位',
      class: 'orange_color',
      data: '',
      prop: 'minLevel',
      unit: 'm',
    },
    {
      text: '预报高潮位',
      class: 'blue_color',
      data: '',
      prop: 'maxLevel',
      unit: 'm',
    },
    {
      text: '出现时间',
      class: 'blue_color',
      data: '',
      prop: 'maxTime',
      unit: '',
    },
  ],
}

export default {
  components: {
    mapViewPoint,
    wrapTitle,
    EchartsLine,
    tableList,
  },
  computed: {
    // routes() {
    //   var routes = {
    //     children: this.$router.options.routes,
    //   };
    //   var route = this.$route.matched;
    //   for (var i = 0; i < route.length - 1; i++) {
    //     routes = routes.children.find((e) => e.name == route[i].name);
    //   }
    //   if (this.type != '水闸') {
    //     console.log('不等于水闸');
    //     routes.children = routes.children.slice(0, 2)
    //   }
    //   return routes.children;
    // },
    // routeDeep() {
    //   let routeDeep = [];
    //   for (var i = 0; i < this.routes.length; i++) {
    //     routeDeep.push(this.routes[i].path);
    //   }
    //   return routeDeep;
    // },
    // type() {
    //   return this.$localData('get', 'engineeringInfo').type;
    // },
    projectDetail() {
      return this.$localData('get', 'tidalLevelForecastInfo')
    },
  },
  data() {
    return {
      maxLevel: '',
      propList: [],
      //   columns: ['杭州', '宁波', '温州', '绍兴', '湖州', '嘉兴', '金华', '衢州'],
      //   defaultIndex: 0,
      //   showPicker: false,
      theadList: [
        {
          label: '时间',
          prop: 'tm',
          slot: true,
        },
        {
          label: '实时潮位（m）',
          prop: 'nowTdz',
          slot: false,
        },
        {
          label: '预报潮位（m）',
          prop: 'futureTdz',
          slot: false,
        },
      ],
      tableData: [],
      timeData: [],
      realTimeData: [],
      limitLine: '',
      predictionData: [],
      extraOption: {
        // xAxis: {
        //   data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        //   axisLabel: {
        //     show: false,
        //   },
        // },
        // series: [
        //   {
        //     name: 'Email',
        //     type: 'line',
        //     data: [82, 93, 90, 93, 129, 133, 132],
        //     markLine: {
        //       data: [
        //         {
        //           yAxis: 100,
        //         },
        //       ],
        //     },
        //   },

        //   {
        //     name: 'Union Ads',
        //     type: 'line',
        //     data: [86, 96, 96, 95, 126, 130, 139],
        //     // markLine: {
        //     //   data: [
        //     //     {
        //     //       yAxis: 100,
        //     //     },
        //     //   ],
        //     // },
        //   },
        // ],
      },
      seriesData: {},
      point: [],
    }
  },
  mounted() {
    // console.log(this.projectDetail)
    this.getSingleList()
    if (this.projectDetail.lng && this.projectDetail.lat) {
      this.point = [{point: [this.projectDetail.lng, this.projectDetail.lat]}]
    }
  },
  methods: {
    async getSingleList() {
      let res = await tidalLevelForecast.singleList({
        sectionId: this.projectDetail.section_id,
      })
      if (res.code == 0) {
        this.tableData = res.data.list
       /* for (let i = 0; i < res.data.list.length; i++) {
          if (i <= 23) {
            res.data.list[i].nowTdz = res.data.list[i].futureTdz
          }
        }*/
        res.data.list.map((item) => {
          this.timeData.push(item.tm.substring(5, 16))
          this.realTimeData.push(item.nowTdz - 0)
          this.predictionData.push(item.futureTdz - 0)
        })
        this.limitLine = res.data.blueLevel

        this.extraOption = {
          tooltip: {
            formatter: (params) => {
            //   console.log(params, 'params')
              return `<div>实时潮位：${
                params[0].value ? params[0].value : '-'
              }mm</div>
              <div>预报潮位：${params[1].value}mm</div>`
            },
          },
          xAxis: {
            data: this.timeData,
            axisLabel: {
              show: true,
            },
          },
          series: [
            {
              name: '实时潮位',
              type: 'line',
              symbol: "none",
              data: this.realTimeData,
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(3, 110, 255, 0.55)' },
                  { offset: 1, color: 'rgba(3, 110, 255, 0.3)' },
                ]),
              },
              markLine: {
                data: [
                  {
                    yAxis: this.limitLine,
                  },
                ],
              },
            },
            {
              name: '预报潮位',
              type: 'line',
              // symbol: "none",
              data: this.predictionData,
              itemStyle: {
                color: '#00ECFF',
              },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(0, 236, 255, 0.55)' },
                  { offset: 1, color: 'rgba(0, 236, 255, 0.3)' },
                ]),
              },
              lineStyle: {
                color: '#00ECFF',
              },
              // markLine: {
              //   data: [
              //     {
              //       yAxis: 100,
              //     },
              //   ],
              // },
            },
          ],
        }
        this.seriesData.timeData = this.timeData
        // this.seriesData.dataList = dataList
        // this.seriesData.limitLevel = limitLevel
        // console.log(this.extraOption, 'test')
        // res.data.forEach((element, index) => {
        //   if (index < 3) {
        //     element.phone = element.phone.substring(0, 3) + '****' + element.phone.substring(7)
        //   }
        // });
        let resProp = res.data,
          list = []
        resProp.maxTime = resProp.maxTime.substring(11)
        for (let item of Statuslist['潮位']) {
          list.push({
            name: item.text,
            class: item.class,
            unit: item.unit || '',
            prop: item.prop,
            data: (resProp && resProp[item.prop]) || '-',
          })
        }
        this.propList = list
        // console.log(this.propList[0].data)
        this.propList[0].data =
          this.realTimeData[this.realTimeData.length / 2 - 1]
        this.maxLevel = res.data.maxLevel
        this.tableData = res.data.list
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.table_wrap {
  max-height: 300px;
  overflow: auto;
}
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
            &.blue_text {
              color: #276af0;
            }
            &.green_color {
              color: #01a59c;
            }
            &.red_color {
              color: #e02020;
            }
            &.orange_color {
              color: #fa6400;
            }
            &.blue_color {
              color: #276af0;
            }
            &.pink_color {
              color: #fa00b4;
            }
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
     .echarts_legend {
        display: flex;
        justify-content: space-between;
        color: #666666;
        padding: 10px 16px 0px;
        .line {
          display: inline-block;
          width: 34px;
          height: 3px;
          background-color: #00ecff;
          vertical-align: middle;
        }
        .dashedLine {
          display: inline-block;
          width: 34px;
          height: 1px;
          border-bottom: 3px dashed #ff0000;
          position: relative;
          bottom: 5px;
        }
      }
  }
}
</style>
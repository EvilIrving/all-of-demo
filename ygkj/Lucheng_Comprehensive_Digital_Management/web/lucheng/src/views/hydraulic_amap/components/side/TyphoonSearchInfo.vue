<script>
import { PrimaryTyphoon, PrimaryTabs } from '@/components';
import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
import * as echarts from 'echarts';

let renderInfoItem = {
  name: '',
  props: {
    title: String
  },
  render() {
    return (
      <div class="info-item">
        <div class="sub-title">{this.title}</div>
        <div class="item-content">{this.$slots.default}</div>
      </div>
    );
  }
};
export default {
  name: 'TyphoonSearchInfo',
  props: {},
  data() {
    return {
      detailActiveTab: 0,
      typhoonCode: '',
      typhoonList: [],

      associatedTable: [],
      similarType: 'path',
      similarTable: []
    };
  },
  mounted() {
    this.typhoonCode = this.$route.query.code;
    this.getTyphoonDetail();
    this.drawWindInfo();
    this.getTyhoonSimilar();
  },
  render() {
    let basicInfo = this.renderBasicInfo();
    let detailInfo = this.renderDetailInfo();
    return (
      <div class="typhoon-info">
        {basicInfo}
        {detailInfo}
      </div>
    );
  },
  methods: {
    handleReturnClick() {
      this.$parent.closeTyphoonDetail();
    },
    renderBasicInfo() {
      return (
        <div class="typhoon-basic-info">
          <div class="typhoon-basic-info__tabs">
            <div class="sub-title">
              请选择要分析的台风
              <el-button class="return-button" onClick={this.handleReturnClick}>
                关闭
              </el-button>
            </div>
          </div>
          <div class="typhoon-basic-info__content" style={{ height: '450px' }}>
            <PrimaryTyphoon
              typhoonList={this.typhoonList}
              onTyphoonCardClick={() => {
                // console.log("onTyphoonCardClickEvent");
              }}
            />
          </div>
        </div>
      );
    },
    renderTyphoonInfo() {
      return [
        <renderInfoItem title="距离统计">
          <div
            class="no-data"
            style={{
              height: '130px',
              lineHeight: '140px',
              textAlign: 'center',
              color: '#fff',
              userSelect: 'none'
            }}
          >
            暂无数据
          </div>
        </renderInfoItem>,
        <renderInfoItem title="风速气压">
          <div id="WindInfo" style={{ height: '140px' }} />
        </renderInfoItem>
      ];
    },
    renderAssociated() {
      return (
        <el-table data={this.associatedTable}>
          <el-table-column label="工程类型" />
          <el-table-column label="名称" />
        </el-table>
      );
    },
    renderSimilarList() {
      return [
        <el-radio-group
          value={this.similarType}
          onInput={(type) => {
            this.similarType = type;
            this.getTyhoonSimilar();
          }}
          class="similar-type"
        >
          <el-radio label={'path'}>路径相似</el-radio>
          <el-radio label={'season'}>季节相似</el-radio>
          <el-radio label={'born_land'}>生成地相似</el-radio>
        </el-radio-group>,
        <renderInfoItem title="相似台风列表">
          <el-table data={this.similarTable}>
            <el-table-column prop="tfbh" label="台风编号" />
            <el-table-column prop="name" label="中文名" />
            <el-table-column prop="ename" label="英文名" />
          </el-table>
        </renderInfoItem>
      ];
    },
    renderDetailInfo() {
      let detailList = [{ name: '台风信息' }, { name: '关联工程' }, { name: '相似台风' }];
      let activeComponentsMap = {
        台风信息: this.renderTyphoonInfo(),
        关联工程: this.renderAssociated(),
        相似台风: this.renderSimilarList()
      };
      let activeComponents = activeComponentsMap[detailList[this.detailActiveTab].name];
      return (
        <div class="typhoon-detail-info">
          <div class="typhoon-detail-info__tabs">
            <PrimaryTabs
              list={detailList}
              value={0}
              onInput={(value) => {
                this.detailActiveTab = value;
                this.$nextTick(() => {
                  this.drawWindInfo();
                });
              }}
            />
          </div>
          <div class="typhoon-detail-info__content">{activeComponents}</div>
        </div>
      );
    },
    getTyphoonDetail() {
      hydraulicApi.getTyphoonDetail(this.typhoonCode).then((res) => {
        this.typhoonList = eval(res.data.points);
        this.typhoonList.map((item) => {
          item.time = new Date(item.time).format('yyyy-MM-dd hh:mm');
        });
      });
    },
    getTyhoonSimilar() {
      hydraulicApi.typhoonAnalysis(this.typhoonCode, this.similarType).then((res) => {
        this.similarTable = res.data.data;
      });
    },
    drawWindInfo() {
      const el = document.querySelector('#WindInfo');
      let chart = echarts.init(el);
      let getMin = (index, max) => {
        let min = Math.min.apply(null, option.series[index].data);
        if (min == max) {
          min = min - 1;
        }
        return min;
      };
      let getMax = (index) => {
        return Math.max.apply(null, option.series[index].data);
      };

      let option = {
        textStyle: { color: '#ffffffbb' },
        grid: {
          top: '20%',
          bottom: '3%',
          left: '5%',
          right: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: [
          {
            name: '风力',
            type: 'value'
          },
          {
            name: '气压',
            type: 'value'
          }
        ],
        series: [
          {
            yAxisIndex: 0,
            data: [7, 7, 7, 7, 7, 7, 7],
            type: 'line',
            smooth: true,
            symbol: 'emptyCircle',
            symbolSize: 6,
            itemStyle: { color: '#1b9eb7' },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: '#3F9BFC88' // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: '#48FFF488' // 100% 处的颜色
                  }
                ]
              }
            }
          },
          {
            yAxisIndex: 1,
            data: [1001.5, 1001.5, 1004, 1004, 1004, 1001.5, 1001.5],
            type: 'line',
            smooth: true,
            symbol: 'emptyCircle',
            symbolSize: 6,
            itemStyle: { color: '#3191ca' },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: '#48FFF488' // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: '#3F9BFC88' // 100% 处的颜色
                  }
                ]
              }
            }
          }
        ]
      };

      option.yAxis[0].max = getMax(0);
      option.yAxis[0].min = getMin(0, option.yAxis[0].max);
      option.yAxis[0].interval = (option.yAxis[0].max - option.yAxis[0].min) / 5;

      option.yAxis[1].max = getMax(1);
      option.yAxis[1].min = getMin(1, option.yAxis[1].max);
      option.yAxis[1].interval = (option.yAxis[1].max - option.yAxis[1].min) / 5;

      chart.setOption(option);
    }
  }
};
</script>

<style lang="scss">
.typhoon-info {
  height: 100%;
  display: flex;
  flex-direction: column;
  .sub-title {
    border-left: 3px solid #1fcde4;
    color: #1fcde4;
    padding-left: 8px;
    font-size: 16px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .return-button {
      width: 88px;
      height: 32px;
      line-height: 32px;
      color: #fff;
      padding: 0;
      background: #239def;
      border: 0;
      border-radius: 4px;
    }
  }
  .typhoon-basic-info,
  .typhoon-detail-info {
    height: 50%;
    display: flex;
    flex-direction: column;
  }
  .typhoon-basic-info {
    padding-bottom: 16px;
  }
  .typhoon-basic-info__content,
  .typhoon-detail-info__content {
    padding-top: 8px;
    overflow-y: scroll;
    overflow-x: hidden;
    .info-item {
      .item-content {
      }
    }
  }
  .similar-type {
    margin-bottom: 12px;
    .el-radio {
      .el-radio__input {
        .el-radio__inner {
          background-color: transparent;
          border: 1px #4088e2 solid;
        }

        &.is-checked {
          .el-radio__inner {
            &::after {
              width: 10px;
              height: 10px;
              background-color: #23e3fb;
            }
          }
        }
      }
      .el-radio__label {
        color: #fff;
      }
    }
  }
  .el-table {
    .el-table__header-wrapper {
      th {
        background: transparent;
      }
      .el-table-column--selection {
        .el-checkbox {
          display: none;
        }
      }
    }
  }
}
</style>

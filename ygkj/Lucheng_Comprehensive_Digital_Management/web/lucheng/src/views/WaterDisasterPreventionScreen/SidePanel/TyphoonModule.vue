<template>
  <!-- 台风板块 -->
  <div class="collapse-panel__content">
    <el-row style="margin: 10px 0" :gutter="16">
      <el-col :span="12">
        <el-select v-model="value3" placeholder="年份" @change="typhoonTime">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.value"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="12">
        <el-select
          v-model="value"
          placeholder="台风选择"
          @change="typhoonVal"
          style="color: #fff; font-size: 16px"
        >
          <el-option
            v-for="item in historyTyphoonList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-col>
    </el-row>

    <div class="not-available-sty" v-if="!noTyphoon"></div>
    <panel-item
      :title="noTyphoon = false ? '历史台风' : '当前台风'"
      v-if="noTyphoon"
    >
      <!-- small-module-sty 为标题下容器样式 -->
      <div class="small-module-sty">
        <!-- 台风名称 -->
        <div class="typhoon-name-sty">
          <p>
            {{
              historyTyphoonData.name == null ? "-" : historyTyphoonData.name
            }}
            {{
              historyTyphoonData.englishName == null
                ? "-"
                : historyTyphoonData.englishName
            }}
            <span>
              {{
                particularsData.power == null ? "-" : historyTyphoonData.power
              }}</span
            >
            级
          </p>
          <p>
            影响时间：<span>
              {{
                historyTyphoonData.hours == null
                  ? "-"
                  : historyTyphoonData.hours
              }} </span
            >h
          </p>
        </div>
        <!-- 台风详情 -->
        <div class="typhoon-details-sty">
          <div>
            <el-row>
              <el-col :span="12" class="typhoon-time-sty">
                <p><i class="icon-time-bg icon-sty"></i></p>
                <div>
                  <p>
                    {{
                      particularsData.time == null
                        ? "-"
                        : particularsData.time.slice(11, 16)
                    }}
                  </p>
                  <p>
                    {{
                      particularsData.time == null
                        ? "-"
                        : particularsData.time.slice(0, 10)
                    }}
                  </p>
                </div>
              </el-col>
              <el-col :span="12" class="typhoon-time-sty typhoon-distance-sty">
                <p><i class="icon-location-bg icon-sty"></i></p>
                <div>
                  <p>
                    {{
                      particularsData.distance == null
                        ? "-"
                        : particularsData.distance
                    }}
                    <span> 公里</span>
                  </p>
                  <p>距离温州</p>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24" class="typhoon-time-sty windstorm">
                <p><i class="icon-time-to-backup icon-sty"></i></p>
                <div>
                  <p>
                    {{
                      particularsData.strong == null
                        ? "-"
                        : particularsData.strong
                    }}
                  </p>
                  <p>台风级别</p>
                </div>
              </el-col>
            </el-row>
          </div>
          <!-- 台风详细信息 -->
          <!-- 中心位置 -->
          <div class="typhoon-information-sty">
            <div>
              <p>
                {{ particularsData.lng == null ? "-" : particularsData.lng }}
                <span> °E</span>
                {{ particularsData.lat == null ? "-" : particularsData.lat
                }}<span> °N</span>
              </p>
              <div>
                <span>中心位置</span>
                <p class="typhoon-singlet-line"></p>
              </div>
            </div>
            <div class="typhoon-information-style"></div>
          </div>
          <!-- 中心气压 -->
          <div class="typhoon-information-sty">
            <div>
              <p>
                {{
                  particularsData.pressure == null
                    ? "-"
                    : particularsData.pressure
                }}
                <span>百帕</span>
              </p>
              <div>
                <span>中心气压</span>
                <p class="typhoon-singlet-line"></p>
              </div>
            </div>
            <div class="typhoon-pressure-style"></div>
          </div>
          <!-- 风力风速 -->
          <div class="typhoon-information-sty">
            <div>
              <p>
                {{
                  particularsData.power == null ? "-" : particularsData.power
                }}
                <span> 级</span>
                {{
                  particularsData.speed == null ? "-" : particularsData.speed
                }}
                <span> 米 / 秒</span>
              </p>
              <div>
                <span>风力风速</span>
                <p class="typhoon-singlet-line"></p>
              </div>
            </div>
            <div class="wind-speed-style"></div>
          </div>
          <!-- 移动速度 -->
          <div class="typhoon-information-sty">
            <div>
              <p>
                {{
                  particularsData.move_speed == null
                    ? "-"
                    : particularsData.move_speed
                }}
                <span>千米 / 小时</span>
              </p>
              <div>
                <span>移动速度</span>
                <p class="typhoon-singlet-line"></p>
              </div>
            </div>
            <div class="typhoon-movement-style"></div>
          </div>
        </div>
        <!-- tab表格 -->
        <div class="areal-rainfall-sty">
          <div>
            <i class="icon-diamond icon-sty" />
            <span>历史相似台风</span>
          </div>
          <capsule-tabs
            v-model="realtimePreChartTab"
            :tabs="['路径', '生成地', '季节']"
            @change="forecastChartTabChange"
          />
        </div>
        <div class="sidebar-list">
          <el-table ref="table1" :data="sluiceTopArr" height="260" stripe>
            <el-table-column prop="tfbh" label="台风编号" width="80" />
            <el-table-column prop="name" label="台风名称" width="90" />
            <el-table-column prop="land_time" label="登录时间" width="100" />
            <el-table-column prop="position" label="登录地点" width="100" />
            <el-table-column label="详情" width="65">
              <template slot-scope="scope">
                <i
                  class="icon-xiang-qing"
                  @click="particularsList(scope.row)"
                ></i>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </panel-item>
  </div>
</template>

<script>
import PanelItem from "../components/PanelItem";
import CapsuleTabs from "../components/CapsuleTabs";
import { disasterPreventionApi } from "@/api/waterDisasterPrevention";
import { mapGetters } from "vuex";
export default {
  name: "TyphoonModule",
  props: ["typhoonList"],
  components: {
    PanelItem,
    CapsuleTabs,
  },
  data() {
    return {
      options: [
        {
          value: "2019",
        },
        {
          value: "2016",
        },
        {
          value: "2013",
        },
        {
          value: "2009",
        },
        {
          value: "2008",
        },
        {
          value: "2007",
        },
        {
          value: "2006",
        },
        {
          value: "2005",
        },
        {
          value: "2004",
        },
        {
          value: "2003",
        },
      ],
      value: "",
      value3: "",
      noTyphoon: false,
      realtimePreChartTab: 0,
      realtimePreChartTabList: ["路径", "生成地", "季节"],
      sluiceTopArr: [],
      typhoonsType: "path",
      historyTyphoonData: {},
      historyTyphoonList: [],
      particularsData: {},
      typhoonCode: "",
      historyTyphoon: [],
    };
  },
  computed: { ...mapGetters(["ScreenMap"]) },
  watch: {
    typhoonList(val) {
      this.particularsData = val;
    },
  },
  mounted() {
    this.$nextTick(() => {
      // this.typhoons();
      this.getRealTyphoons();
    });
  },
  methods: {
    typhoonVal() {
      this.noTyphoon = true;
      this.historyTyphoonData.id = this.value;
      this.getTyphoonDetail();
    },
    typhoonTime() {
      this.typhoons(this.value3);
    },
    /**
     * 历史相似台风详情
     */
    particularsList(val) {
      this.$store.commit("particularsList", val);
      this.$store.commit("typhoonShowList", true);
    },
    /**
     * 历史台风接口
     */
    async typhoons(val) {
      let str = {
        begin: val + "-01",
        end: val + "-12",
      };
      this.historyTyphoonList = [];
      await disasterPreventionApi.typhoons(str).then((res) => {
        if (res.code == 0) {
          this.historyTyphoon = res.data;
          res.data.forEach((item, index) => {
            let str = {};
            str.value = item.id;
            str.label = item.name;
            this.historyTyphoonList.push(str);
          });
        }
      });
    },
    /**
     * 当前台风
     */
    async getRealTyphoons() {
      await disasterPreventionApi.getRealTyphoon().then((res) => {
        if (res.code == 0) {
          let str = {};
          if (res.data) {
            this.noTyphoon = false;
          }
          str.id = res.data[0].id;
          str.name = res.data[0].name;
          str.code = res.data[0].tfbh;
          str.hours = res.data[0].hours;
          str.englishName = res.data[0].ename;
          this.historyTyphoonData = str;
          this.typhoonCode = this.historyTyphoonData.code;
          if (this.historyTyphoonData.code) {
            this.getTyphoons();
          }
          this.getTyphoonDetail();
        } else {
          this.noTyphoon = true;
        }
      });
    },
    /**
     * 台风分析
     */
    async getTyphoonDetail() {
      this.ScreenMap.clearTyphoonLayer();
      if (this.historyTyphoonData.id) {
        await disasterPreventionApi
          .getTyphoonDetail(this.historyTyphoonData.id)
          .then((res) => {
            if (res.code == 0) {
              if (!this.noTyphoon) {
                if (res.data) {
                  this.noTyphoon = true;
                }
                let newArrar = {};
                newArrar = this.historyTyphoon.find((item) => {
                  if (item.id === this.value) {
                    return item;
                  }
                });
                this.typhoonCode = newArrar.code;
                if (this.typhoonCode) {
                  this.getTyphoons();
                }
                this.historyTyphoonData.name = newArrar.name;
                this.historyTyphoonData.englishName = newArrar.englishName;
                this.historyTyphoonData.power = newArrar.landPower;
                this.historyTyphoonData.hours = newArrar.hours;
              }
              this.particularsData = JSON.parse(res.data.points).slice(-1)[0];
              this.ScreenMap.showTyphoon(res.data);
            }
          });
      }
    },
    /**
     * 历史相似台风切换
     */
    forecastChartTabChange(index) {
      switch (index) {
        case 0:
          this.typhoonsType = "path";
          this.getTyphoons();
          break;
        case 1:
          this.typhoonsType = "born_land";
          this.getTyphoons();
          break;
        case 2:
          this.typhoonsType = "season";
          this.getTyphoons();
          break;
      }
    },
    /**
     * 获取历史相似台风列表
     */
    async getTyphoons() {
      let res = await disasterPreventionApi.typhoonAnalysis(
        this.typhoonCode,
        this.typhoonsType
      );
      if (res.code == 0) {
        this.sluiceTopArr = res.data.data;
      }
    },
  },
};
</script>
<style lang="scss" scoped>
@import "../style/SildePanel.scss";
.not-available-sty {
  margin: 50% 10%;
  width: 80%;
  height: 300px;
  background: url(~assets/images/WaterDisastersImage/zhanwe-taifeng.png)
    no-repeat top center;
  background-size: 100% 100%;
}
.small-module-sty {
  padding: 5px 0;
  .typhoon-name-sty {
    > p:nth-child(1) {
      font-size: 18px;
      color: #33ffff;
      text-align: left;
      line-height: 24px;
      font-weight: 500;
      > span {
        margin-left: 5px;
        font-size: 24px;
      }
    }
    > p {
      font-size: 14px;
      color: #ffffff;
      line-height: 24px;
      font-weight: 500;
      > span {
        margin-left: 5px;
        color: #33ffff;
        font-size: 20px;
      }
    }
  }
  ::v-deep .el-table th {
    font-size: 15px;
    color: #1ef8f2;
  }
  ::v-deep .el-table td {
    padding: 0;
    color: #fff;
    font-size: 14px;
  }
  ::v-deep .el-table .el-checkbox__inner {
    border: 0.0625rem solid #00c1ff;
    background-color: #1ef8f2;
    background: rgba(0, 0, 0, 0);
  }
  ::v-deep .el-table-column--selection .cell {
    padding-left: 8px;
    padding-right: 10px;
  }
  .icon-diamond {
    display: inline-block;
    height: 1.5rem;
    width: 1.5rem;
    background-image: url(~assets/images/WaterDisastersImage/diamond.gif);
  }
  // 台风详情内容样式
  .typhoon-details-sty {
    margin: 10px;
    padding: 10px 0;
    border-top: 1px solid rgba($color: #00c1ff, $alpha: 0.25);
    border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.25);
    .typhoon-time-sty {
      display: flex;
      justify-content: left;
      > p {
        margin-right: 16px;
      }
      > div {
        > p:nth-child(1) {
          font-size: 24px;
          color: #00f5ff;
          line-height: 24px;
          font-weight: 500;
        }
        > p:nth-child(2) {
          padding-top: 5px;
          font-size: 14px;
          color: #ffffff;
          line-height: 24px;
          font-weight: 400;
        }
      }
    }
    .typhoon-distance-sty {
      > div {
        > p:nth-child(1) {
          > span {
            font-size: 14px;
            color: #ffffff;
            line-height: 24px;
            font-weight: 400;
          }
        }
      }
    }
    .windstorm {
      padding: 10px 0;
      border-bottom: 1px solid rgba($color: #00c1ff, $alpha: 0.25);
    }
    .typhoon-information-style {
      height: 80px;
      width: 100px;
      float: right;
      margin: 10px 0;
      color: #00f5ff;
      background: url(~assets/images/WaterDisastersImage/zhong-xin.png)
        no-repeat top center;
      background-size: 100% 100%;
    }
    .typhoon-pressure-style {
      height: 80px;
      width: 100px;
      float: right;
      color: #00f5ff;
      background: url(~assets/images/WaterDisastersImage/zhong-xin-qi-ya.png)
        no-repeat top center;
      background-size: 100% 100%;
    }
    .wind-speed-style {
      height: 80px;
      width: 100px;
      float: right;
      color: #00f5ff;
      background: url(~assets/images/WaterDisastersImage/fengshu-fengli.png)
        no-repeat top center;
      background-size: 100% 100%;
    }
    .typhoon-movement-style {
      height: 80px;
      width: 100px;
      float: right;
      color: #00f5ff;
      background: url(~assets/images/WaterDisastersImage/yi-dong-shu-du.png)
        no-repeat top center;
      background-size: 100% 100%;
    }
    .typhoon-information-sty {
      position: relative;
      display: flex;
      justify-content: space-between;
      > div {
        > p {
          position: absolute;
          top: 30px;
          left: 0;
          font-size: 24px;
          color: #1ff1f0;
          line-height: 24px;
          font-weight: 500;
          > span {
            font-size: 14px;
            color: #ffffff;
            line-height: 24px;
            font-weight: 400;
          }
        }
        > div {
          display: flex;
          margin-top: 50px;
          justify-content: space-between;
          > span {
            font-size: 14px;
            color: #ffffff;
            line-height: 24px;
            font-weight: 400;
            margin-top: 15px;
            margin-right: 16px;
          }
        }
      }
    }
    .typhoon-singlet-line {
      width: 15.625rem;
      height: 30px;
      background: url(~assets/images/WaterDisastersImage/tai-feng-dan-xian.png)
        no-repeat top center;
      background-size: 100% 100%;
    }
  }
  .areal-rainfall-sty {
    display: flex;
    justify-content: space-between; /* 横向中间自动空间 */
    > div:nth-child(1) {
      vertical-align: middle;
      > i {
        vertical-align: middle;
        margin-top: -6px;
        margin-right: 10px;
      }
      > span {
        font-size: 18px;
        color: #ffffff;
        line-height: 24px;
        font-weight: 500;
      }
    }
    > div:nth-child(2) {
      line-height: 21px;
      .tab-switch-sty {
        padding: 2px 10px;
        border-radius: 15px;
        background: #00c1ff;
      }
      > span {
        color: #fff;
        margin-left: 15px;
        font-size: 15px;
      }
    }
  }
}
::v-deep.el-select .el-input__inner {
  color: #fff;
  font-size: 16px;
}
::v-deep.el-select .el-input .el-select__caret {
  margin-top: -2px;
  font-size: 20px;
}
</style>

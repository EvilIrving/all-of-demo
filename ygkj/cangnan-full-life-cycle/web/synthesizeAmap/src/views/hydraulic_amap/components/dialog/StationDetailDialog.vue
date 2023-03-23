<template>
  <div>
    <div
      class="statistics-sty"
      v-if="stationType != '水闸工情' && stationType != '雨情'"
    >
      <!-- 总数统计 -->
      <div>
        <p><i class="icon-shui-ku-zong-shu"></i></p>
        <div>
          <p>水位总数</p>
          <p>
            <span>{{ allCount }} </span> 个
          </p>
        </div>
      </div>
      <!-- 超警数量统计 -->
      <div>
        <p><i class="icon-shui-ku-bing-xian"></i></p>
        <div>
          <p>超警数量</p>
          <p>
            <span style="color: #ff7144">{{ warningCount }} </span> 个
          </p>
        </div>
      </div>
    </div>

    <!-- 水闸统计 -->
    <div class="statistics-sty" v-if="stationType == '水闸工情'">
      <!-- 数量统计 -->
      <div>
        <p><i class="icon-shuizha-shuliang-tongji"></i></p>
        <div>
          <p>数量统计</p>
          <p>
            <span> {{ reservoirStatistical.totalNum }} </span> 个
          </p>
        </div>
      </div>
      <!-- 闸门开启数量统计 -->
      <div
        :class="count % 2 ? 'gates-open-sty' : ''"
        style="padding: 10px"
        @click="openGates"
      >
        <p><i class="icon-shuizha-zhamen-kaiqi-shuliang"></i></p>
        <div>
          <p>闸门开启数量</p>
          <p>
            <span>{{ reservoirStatistical.openGateWagaNum }} </span> 个
          </p>
        </div>
      </div>
      <!-- 水闸泄洪量 -->
      <div class="water-gate-sty">
        <div class="area-style">
          <p>
            <i class="el-icon-time icon-sty"></i><span>累计:</span>
            <el-input-number
              v-model="numTime"
              size="small"
              controls-position="right"
              :min="1"
              :max="72"
              @input="changeHours"
            />
            <span style="margin-left: 10px; font-size: 16px">小时</span>
          </p>
          <div>
            泄洪总量
            <span>{{ reservoirStatistical.drainageTotal }} </span> 万m³
          </div>
        </div>
      </div>
    </div>

    <div class="scope-content">
      <div class="table-box" ref="tableBox">
        <div ref="searchBox">
          <el-row :gutter="20" class="search-panel">
            <el-col
              :span="6"
              v-if="stationType != '水闸工情' && stationType != '雨情'"
            >
              <div class="search-item">
                <span class="text">行政区划：</span>
                <div class="content">
                  <el-select
                    v-model="areaVal"
                    class="primary-select"
                    @change="getDetail(stationType)"
                  >
                    <el-option
                      v-for="item in areaoOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    >
                    </el-option>
                  </el-select>
                </div>
              </div>
            </el-col>
            <el-col :span="8" v-if="stationType == '水库水位'">
              <div class="search-item">
                <span class="text">是否绑定工程：</span>
                <div class="content">
                  <el-radio-group
                    v-model="bindProj"
                    @change="getDetail(stationType)"
                  >
                    <el-radio :label="null">全部</el-radio>
                    <el-radio :label="true">是</el-radio>
                    <el-radio :label="false">否</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </el-col>
            <el-col :span="8" v-if="stationType == '水库水位'">
              <div class="search-item">
                <span class="text">是否主测站：</span>
                <div class="content">
                  <el-radio-group
                    v-model="main"
                    @change="getDetail(stationType)"
                  >
                    <el-radio :label="null">全部</el-radio>
                    <el-radio :label="true">是</el-radio>
                    <el-radio :label="false">否</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </el-col>
            <el-col
              :span="12"
              v-if="stationType == '水库水位' || stationType == '水闸工情'"
            >
              <div class="search-item">
                <span class="text">工程规模：</span>
                <div class="content">
                  <el-radio-group
                    v-model="engScal"
                    @change="getDetail(stationType)"
                  >
                    <el-radio :label="''">全部</el-radio>
                    <el-radio :label="'大中型'">大中型</el-radio>
                    <el-radio :label="'小（Ⅰ）型'">小(1)型</el-radio>
                    <el-radio :label="'小（Ⅱ）型'">小(2)型</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </el-col>
            <el-col :span="12" v-if="stationType == '河道水位'">
              <div class="search-item">
                <span class="text">河道类型：</span>
                <div class="content">
                  <el-radio-group
                    v-model="riverStationType"
                    @change="getDetail(stationType)"
                  >
                    <el-radio :label="''">全部</el-radio>
                    <el-radio :label="'平原河网'">平原河网</el-radio>
                    <el-radio :label="'山区河道'">山区河道</el-radio>
                    <el-radio :label="'其他'">其他</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </el-col>
            <el-col
              :span="8"
              v-if="stationType != '水闸工情' && stationType != '雨情'"
            >
              <div class="search-item">
                <span class="text">防汛等级：</span>
                <div class="content">
                  <el-radio-group
                    v-model="isFocus"
                    @change="getDetail(stationType)"
                  >
                    <el-radio :label="null">全部</el-radio>
                    <el-radio :label="true">重点</el-radio>
                    <el-radio :label="false">一般</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </el-col>

            <el-col :span="5" v-if="stationType == '雨情'">
              <div class="search-item">
                <span class="text">类型:</span>
                <el-select
                  v-model="realTimeType"
                  size="small"
                  placeholder="全部"
                  @blur="changeVal"
                  @change="getDetail(stationType)"
                >
                  <el-option
                    v-for="item in realTimeStration"
                    :key="item.name"
                    :label="item.name"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col
              v-show="realTimeType == '点雨量'"
              :span="6"
              class="aa"
              v-if="stationType == '雨情'"
            >
              <div class="search-item">
                <span class="text">统计类型:</span>
                <el-select
                  v-model="surfaceType"
                  size="small"
                  @blur="changeVal"
                  @change="getDetail(stationType)"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in surfaceTypeStration"
                    :key="item.name"
                    :label="item.name"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>

            <el-col
              :style="{ marginTop: realTimeType == '点雨量' ? '' : '' }"
              v-show="realTimeType == '点雨量' && surfaceType == '流域'"
              :span="6"
              class="aa"
              v-if="stationType == '雨情'"
            >
              <div class="search-item">
                <span class="text">流域：</span>
                <el-select
                  v-model="drainageType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @blur="changeVal"
                  @change="getDetail(stationType)"
                >
                  <el-option
                    v-for="item in administrative"
                    :key="item.name"
                    :label="item.name"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col
              :style="{ marginTop: realTimeType == '点雨量' ? '' : '' }"
              v-show="realTimeType == '面雨量' || surfaceType == '行政区划'"
              :span="6"
              class="aa"
              v-if="stationType == '雨情'"
            >
              <div class="search-item">
                <span class="text">行政区划：</span>
                <el-select
                  v-model="areaVal"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getDetail(stationType)"
                >
                  <el-option
                    v-for="item in areaoOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col
              v-show="realTimeType == '点雨量'"
              :span="6"
              class="aa"
              v-if="stationType == '雨情'"
              style="margin-right: 10px"
            >
              <div class="search-item" style="height: 35px">
                <el-radio
                  v-model="radio"
                  class="el-radio"
                  :label="true"
                  style="color: #fff"
                  @change="changeVal"
                  @input="getDetail(stationType)"
                  >参与计算雨量站</el-radio
                >
                <el-radio
                  v-model="radio"
                  class="el-radio"
                  :label="false"
                  style="color: #fff"
                  @change="changeVal"
                  @input="getDetail(stationType)"
                  >全部雨量站</el-radio
                >
              </div>
            </el-col>
            <el-col :span="18" class="aa" v-if="stationType == '雨情'">
              <div class="search-item">
                <span class="text" style="line-height: 32px">降雨历时:</span>
                <el-checkbox-group
                  v-model="durationRainfall"
                  @change="getDetail(stationType)"
                >
                  <el-checkbox
                    v-for="item in durationRainfallList"
                    :key="item.name"
                    :label="item.value"
                    >{{ item.name }}</el-checkbox
                  >
                </el-checkbox-group>
              </div>
            </el-col>

            <el-col
              :span="6"
              v-if="stationType != '水闸工情' && stationType != '雨情'"
            >
              <div class="reset" @click="resetParam">重置</div>
            </el-col>
          </el-row>
        </div>

        <primary-map-table
          :style="tableHeight"
          :columnList="columnList"
          :tableIndex="tableIndex"
          :dataList="dataList"
          :tableLoading="tableLoading"
        ></primary-map-table>
      </div>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from "@/components";
import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
import { getDict } from "@/api/system";

export default {
  name: "DetailTable",
  props: {
    stationType: {
      type: String,
      default: "水库水位",
    },
  },
  data() {
    return {
      tableHeight: 0,
      areaVal: "", // 行政区划
      areaoOptions: [
        {
          value: "",
          label: "全部",
        },
      ],
      typeVal: "RR", // 类型
      typeOptions: [
        {
          value: "",
          label: "全部",
        },
        {
          value: "RR",
          label: "水库",
        },
        {
          value: "ZZ",
          label: "河道",
        },
        {
          value: "DD",
          label: "堰闸",
        },
        {
          value: "TT",
          label: "潮位",
        },
      ],
      tableIndex: 0,
      columnList: [
        {
          prop: "stName",
          label: "名称",
        },
        {
          prop: "time",
          label: "更新时间",
        },
        {
          prop: "waterLevel",
          label: "水位(m)",
          sortable: true,
        },
      ],
      dataList: [],
      tableLoading: false, // 表格loading
      allCount: 0,
      warningCount: 0,

      main: null, // 是否主测站
      bindProj: null,
      engScal: "",
      riverStationType: "",
      isFocus: null,

      waterGateData: [],
      reservoirStatistical: {},
      numTime: "1",
      count: 0,

      realTimeType: "点雨量",
      realTimeStration: [
        {
          value: "面雨量",
          name: "面雨量",
        },
        {
          value: "点雨量",
          name: "点雨量",
        },
      ],
      surfaceType: "",
      surfaceTypeStration: [
        {
          value: "",
          name: "全部",
        },
        {
          value: "行政区划",
          name: "行政区划",
        },
        {
          value: "流域",
          name: "流域",
        },
      ],
      radio: true,
      durationRainfall: [],
      durationRainfallList: [
        {
          value: 1,
          name: "前1小时",
        },
        {
          value: 3,
          name: "前3小时",
        },
        {
          value: 6,
          name: "前6小时",
        },
        {
          value: 24,
          name: "前24小时",
        },
        {
          value: 72,
          name: "前72小时",
        },
      ],
      drainageType: "",
      //水库流域
      administrative: [
        {
          value: "",
          name: "全部",
        },
        {
          value: "飞云江",
          name: "飞云江",
        },
        {
          value: "瓯江",
          name: "瓯江",
        },
        {
          value: "鳌江",
          name: "鳌江",
        },
      ],
    };
  },
  components: {
    PrimaryMapTable,
  },
  created() {
    this.setColumnList(this.stationType);
  },
  mounted() {
    this.getAdcdList();
    this.getDetail();
    this.tableHeight = {
      height:
        this.$refs.tableBox.offsetHeight -
        this.$refs.searchBox.offsetHeight -
        30 +
        "px",
    };
  },
  methods: {
    resetParam() {
      this.areaVal = "";
      this.main = null; // 是否主测站
      this.bindProj = null;
      this.engScal = "";
      this.riverStationType = "";
      this.isFocus = null;
      this.getWaterStations();
    },
    //设置表头
    setColumnList(val) {
      let columnArr = [];
      switch (val) {
        case "水库水位":
          columnArr = [
            {
              prop: "limitLevel",
              label: "汛限水位(m)",
              sortable: true,
            },
            {
              prop: "floodLevel",
              label: "防洪高水位(m)",
              sortable: true,
            },
          ];
          break;
        case "河道水位":
          columnArr = [
            {
              prop: "warningLevel",
              label: "警戒水位(m)",
              sortable: true,
            },
            {
              prop: "guaranteeLevel",
              label: "保证水位(m)",
              sortable: true,
            },
          ];
        case "堰闸水位":
          this.columnList = [];
          columnArr = [
            {
              prop: "stName",
              label: "名称",
            },
            {
              prop: "time",
              label: "更新时间",
            },
            {
              prop: "upz",
              label: "水位(m)",
              sortable: true,
            },
            {
              prop: "warningLevel",
              label: "警戒水位(m)",
              sortable: true,
            },
            {
              prop: "guaranteeLevel",
              label: "保证水位(m)",
              sortable: true,
            },
          ];
          break;
        case "潮位":
          this.columnList = [];
          columnArr = [
            {
              prop: "stName",
              label: "名称",
            },
            {
              prop: "time",
              label: "更新时间",
            },
            {
              prop: "tdz",
              label: "水位(m)",
              sortable: true,
            },
            {
              prop: "warningLevel",
              label: "警戒水位(m)",
              sortable: true,
            },
          ];
          break;
        case "流量":
          columnArr = [
            {
              prop: "flow",
              label: "流量(m³/s)",
              sortable: true,
            },
          ];
          break;
        case "水闸工情":
          this.columnList = [];
          columnArr = [
            {
              prop: "wagaName",
              label: "名称",
              sortable: true,
            },
            {
              prop: "bas",
              label: "流域",
            },
            {
              prop: "areaName",
              label: "行政区划",
            },
            {
              prop: "engScal",
              label: "工程规模",
              width: "100px",
            },
            {
              prop: "upz",
              label: "闸上水位(m)",
              width: "150px",
              sortable: true,
            },
            {
              prop: "dwz",
              label: "闸下水位(m)",
              width: "150px",
              sortable: true,
            },
            {
              prop: "ovs",
              label: "闸门开度",
              width: "150px",
              sortable: true,
            },
            {
              prop: "mxgtq",
              label: "最大过闸流量(㎡/s)",
              width: "180px",
              sortable: true,
            },
            {
              prop: "ll",
              label: "实际过闸流量(㎡/s)",
              width: "180px",
              sortable: true,
            },
            {
              prop: "drainage",
              label: "累计" + this.numTime + "小时泄洪量(万m³)",
              width: "200px",
              sortable: true,
            },
          ];
          break;
        case "雨情":
          this.columnList = [];
          columnArr = [
            {
              prop: "name",
              label: "名称",
              sortable: true,
            },
            {
              prop: "newest1hour",
              label: "近1小时(mm)",
              width: "140px",
              sortable: true,
            },
            {
              prop: "1hour",
              label: "前1小时(mm)",
              width: "140px",
              sortable: true,
            },
            {
              prop: "3hour",
              label: "前3小时(mm)",
              width: "140px",
              sortable: true,
            },
            {
              prop: "6hour",
              label: "前6小时(mm)",
              width: "140px",
              sortable: true,
            },
            {
              prop: "24hour",
              label: "前24小时(mm)",
              width: "140px",
              sortable: true,
            },
            {
              prop: "72hour",
              label: "前72小时(mm)",
              width: "140px",
              sortable: true,
            },
          ];
          break;
      }
      columnArr.forEach((item) => {
        this.columnList.push(item);
      });
      this.tableIndex = Math.random(10000);
    },
    // 获取行政区
    async getAdcdList() {
      let opt = {
        areaCode: "3303",
        level: 3,
      };
      let res = await hydraulicApi.getAdcdList(opt);
      if (res.code == 0) {
        this.areaoOptions = [
          {
            value: "",
            label: "全部",
          },
        ];
        res.data.forEach((item) => {
          let obj = {
            value: item.adcd,
            label: item.adnm,
          };
          this.areaoOptions.push(obj);
        });
      }
    },
    getDetail() {
      if (this.stationType == "雨情") {
        if (this.realTimeType == "面雨量") {
          this.drainageType = "";
          this.surfaceType = "";
          this.areaVal = "";
          this.bas = "";
        } else {
          if (this.surfaceType == "流域" || this.surfaceType == "全部") {
            this.areaVal = "";
          } else {
            this.drainageType = "";
          }
        }
      }
      switch (this.stationType) {
        case "水库水位":
          this.typeVal = "RR";
          this.getWaterStations();
          break;
        case "河道水位":
          this.typeVal = "ZZ";
          this.getWaterStations();
          break;
        case "堰闸水位":
          this.typeVal = "DD";
          this.getWaterStations();
          break;
        case "潮位":
          this.typeVal = "TT";
          this.getWaterStations();
          break;
        case "水闸工情":
          this.wagaProjList();
          break;
        case "雨情":
          this.grandTotalRainfallGrid();
          break;
      }
    },
    // 水情
    async getWaterStations() {
      let opt = {
        stationType: this.typeVal,
        stationName: "",
        areaCode: this.areaVal,
        isFocus: this.isFocus,
      };

      switch (this.stationType) {
        case "水库水位":
          opt = {
            ...opt,
            main: this.main,
            bindProj: this.bindProj,
            engScal: this.engScal,
          };
          break;
        case "河道水位":
          opt = {
            ...opt,
            riverStationType: this.riverStationType,
          };
          break;
      }

      this.tableLoading = true;

      // 获取水位总数
      let res1 = await hydraulicApi.waterStations(opt);
      this.dataList = res1.data;
      this.allCount = res1.data.length;

      // 获取超警数量
      opt.warning = true;
      let res2 = await hydraulicApi.waterStations(opt);
      this.warningCount = res2.data.length;

      this.tableLoading = false;
    },
    // 水闸工情
    async wagaProjList() {
      this.tableLoading = true;
      let obj = {
        // filterOpenGate: this.filterOpenGate,
        engScal: this.engScal,
        hours: this.numTime,
      };
      await hydraulicApi
        .wagaProjList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.dataList = res.data.list;
            this.reservoirStatistical = res.data;
          }
          this.setColumnList(this.stationType);
          this.tableLoading = false;
        })
        .catch((error) => {
          this.tableLoading = false;
        });
    },
    // 实时降雨
    async grandTotalRainfallGrid() {
      this.tableLoading = true;
      let obj = {
        hour: this.durationRainfall[this.durationRainfall.length - 1],
        type: this.realTimeType, //只能是面雨量或者点雨量
        surfaceType: this.surfaceType, //在type选择面雨量之后，只能传流域或者行政区划
        areaCode: this.areaVal, //行政区划编码，只有在type选择点雨量的时候才传
        bas: this.drainageType, //流域名称（瓯江/飞云江/鳌江），只有在type选择点雨量的时候才传
        // stationName: this.NameVal, // 搜索框
        isSurfaceRainStation: this.radio, // 重点时传true 非重点传false
      };
      await hydraulicApi
        .grandTotalRainfallGrid(obj)
        .then((res) => {
          if (res.code == 0) {
            if (this.durationRainfall == "") {
              this.dataList = res.data;
            } else {
              if (
                this.durationRainfall[this.durationRainfall.length - 1] == 1
              ) {
                let arr = [];
                this.dataList.forEach((item, index) => {
                  item["1hour"] = res.data[index]["1hour"];
                  arr.push(item);
                });
                this.dataList = arr;
              } else if (
                this.durationRainfall[this.durationRainfall.length - 1] == 3
              ) {
                let arr = [];
                this.dataList.forEach((item, index) => {
                  item["3hour"] = res.data[index]["3hour"];
                  arr.push(item);
                });
                this.dataList = arr;
              } else if (
                this.durationRainfall[this.durationRainfall.length - 1] == 6
              ) {
                let arr = [];
                this.dataList.forEach((item, index) => {
                  item["6hour"] = res.data[index]["6hour"];
                  arr.push(item);
                });
                this.dataList = arr;
              } else if (
                this.durationRainfall[this.durationRainfall.length - 1] == 24
              ) {
                let arr = [];
                this.dataList.forEach((item, index) => {
                  item["24hour"] = res.data[index]["24hour"];
                  arr.push(item);
                });
                this.dataList = arr;
              } else if (
                this.durationRainfall[this.durationRainfall.length - 1] == 72
              ) {
                let arr = [];
                this.dataList.forEach((item, index) => {
                  item["72hour"] = res.data[index]["72hour"];
                  arr.push(item);
                });
                this.dataList = arr;
              }
            }
          }
          this.tableLoading = false;
        })
        .catch((error) => {
          this.tableLoading = false;
        });
    },
    // 开启数
    openGates() {
      this.count++;
      this.numTime = "1";
      if (this.count % 2) {
        this.filterOpenGate = true;
        this.wagaProjList();
      } else {
        this.filterOpenGate = false;
        this.wagaProjList();
      }
    },
    changeHours() {
      this.wagaProjList();
    },
    changeVal() {
      this.durationRainfall = [];
      // this.dataList = [];
    },
  },
};
</script>

<style lang="scss" scoped>
.search-panel {
  margin: 16px 0;
  .search-item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    .text {
      margin-right: 10px;
    }
    .el-radio {
      width: auto;
      margin-right: 10px;
      &:last-child {
        margin-right: 0;
      }
    }
  }
}

.reset {
  background-color: rgba(0, 0, 0, 0);
  border: 1px solid #56fefe;
  border-radius: 4px;
  font-size: 16px;
  color: #56fefe;
  width: 64px;
  height: 34px;
  line-height: 34px;
  text-align: center;
}
.scope-content {
  display: flex;
  height: 568px;
  padding: 0 22px;
  // 统计样式
  .statistics-sty {
    padding: 0 0 10px 0;
    width: 100%;
    display: flex;
    justify-content: left;
    border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
    > div {
      // margin-right: 50px;
      padding: 10px 0;
      margin-right: 30px;
      display: flex;
      justify-content: space-between;
      > p {
        padding-top: 5px;
        margin-right: 15px;
      }
      > div {
        > p:nth-child(1) {
          font-family: PingFangSC-Regular;
          font-size: 15px;
          color: #ffffff;
          letter-spacing: 0;
          line-height: 24px;
          font-weight: 400;
          margin-bottom: 5px;
        }
        > p:nth-child(2) {
          font-family: PingFangSC-Medium;
          font-size: 14px;
          color: #fff;
          letter-spacing: 0;
          line-height: 24px;
          font-weight: 500;
          > span {
            font-size: 24px;
            color: #56fefe;
          }
        }
      }
    }
    .gates-open-sty {
      background: url(~assets/images/WaterDisastersImage/zhamen-kaiqi.png)
        no-repeat center center;
      background-size: 100% 100%;
    }
    .water-gate-sty {
      > .area-style {
        border-left: 1px solid rgba($color: #0bbbff, $alpha: 0.5);
        background: none;
        display: flex;
        justify-content: flex-start;
        > p:nth-child(1) {
          padding: 15px 15px 0 40px;
          > span {
            font-family: PingFangSC-Regular;
            font-size: 16px;
            color: #ffffff;
            font-weight: 400;
            margin-right: 10px;
          }
          > i {
            line-height: 30px;
            margin: 0 10px 0 0;
            color: #fff;
          }
          ::v-deep.el-input__inner {
            background: none;
            border: 0.0625rem solid #fff;
          }
          ::v-deep.el-input-number__decrease {
            border-left: 0.0625rem solid #fff;
            background: none;
            > i {
              color: #fff;
            }
          }
          ::v-deep.el-input-number__increase {
            height: 16px;
            border-left: 0.0625rem solid #fff;
            background: none;
            > i {
              color: #fff;
            }
          }
        }
        > div:nth-child(2) {
          padding: 15px 15px 0 20px;
          font-size: 16px;
          color: #fff;
          > span {
            font-family: PingFangSC-Medium;
            font-size: 24px;
            color: #1cfff5;
            letter-spacing: 0;
            text-align: right;
            line-height: 24px;
            font-weight: 500;
            padding: 0;
            margin: 0;
          }
        }
      }
    }
  }
  ::v-deep .el-input__inner {
    width: 120px;
    height: 32px;
    border: 1px solid rgba(255, 255, 255, 0.6);
    border-radius: 4px;
    background: transparent;
  }
  ::v-deep .el-input__icon {
    line-height: 24px;
  }

  .table-wrap {
    height: 474px;
  }

  ::v-deep .el-table {
    tr {
      background-color: transparent;
    }
  }
  .table-box {
    color: #fff;
    width: 100%;
    .select-group {
      padding: 16px 0 24px;
      display: flex;
      .select-box {
        width: 171px;
        margin-right: 11px;
        &.search-area {
          ::v-deep .el-input {
            width: fit-content;
            .el-input__inner {
              width: 200px;
            }
          }
        }
      }
      .reset {
        background-color: rgba(0, 0, 0, 0);
        border: 1px solid #56fefe;
        border-radius: 4px;
        font-size: 16px;
        color: #56fefe;
        width: 64px;
        height: 34px;
        line-height: 34px;
        text-align: center;
      }
    }
  }
  ::v-deep .el-loading-mask {
    background: transparent;
  }

  ::v-deep .is-checked .el-checkbox__label {
    color: #70adf8;
  }
  ::v-deep .is-checked .el-checkbox__inner {
    border-color: #70adf8;
    color: #70adf8;
    &::after {
      border-color: #70adf8;
    }
  }
}
</style>

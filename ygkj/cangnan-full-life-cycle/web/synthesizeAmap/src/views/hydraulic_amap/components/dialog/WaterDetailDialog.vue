<template>
  <div>
    <!-- 水源 统计 -->
    <div class="statistics-sty" v-if="sideType == '水源'">
      <div>
        <p><i class="icon-shuiyuandi icon-shuiyuandi-1"></i></p>
        <div>
          <p>水源地总数</p>
          <p>
            <span> 123 </span>
            个
          </p>
        </div>
      </div>
      <div>
        <p><i class="icon-shuiyuandi icon-shuiyuandi-1"></i></p>
        <div>
          <p>县级以上</p>
          <p>
            <span> 123 </span>
            个
          </p>
        </div>
      </div>
      <div>
        <p><i class="icon-shuiyuandi icon-shuiyuandi-2"></i></p>
        <div>
          <p>千吨万人</p>
          <p>
            <span style="color: #008fe8"> 123 </span>
            个
          </p>
        </div>
      </div>
      <div>
        <p><i class="icon-shuiyuandi icon-shuiyuandi-3"></i></p>
        <div>
          <p>200吨~1000吨</p>
          <p>
            <span style="color: #6267ff"> 123 </span>
            个
          </p>
        </div>
      </div>
      <div>
        <p><i class="icon-shuiyuandi icon-shuiyuandi-4"></i></p>
        <div>
          <p>200吨以下</p>
          <p>
            <span style="color: #a000c8"> 123 </span>
            个
          </p>
        </div>
      </div>
    </div>

    <div class="scope-content">
      <div class="table-box">
        <div class="select-group">
          <!-- 水源地 筛选项 -->
          <el-row :gutter="20" v-if="sideType == '水源'">
            <el-col :span="8">
              <div class="demo-input-suffix">
                <p style="width: 80px">行政区划：</p>
                <el-select
                  v-model="acdc"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getWaterSourList"
                >
                  <el-option
                    v-for="item in adcdOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="demo-input-suffix">
                <p style="width: 50px">类型：</p>
                <el-select
                  v-model="waterSourType"
                  size="small"
                  clearable
                  @change="getWaterSourList"
                >
                  <el-option
                    v-for="item in waterSourTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="demo-input-suffix">
                <p style="width: 50px">规模：</p>
                <el-select
                  v-model="waterSourScale"
                  size="small"
                  clearable
                  @change="getWaterSourList"
                >
                  <el-option
                    v-for="item in waterSourScaleOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="demo-input-suffix">
                <p style="width: 90px">关键字：</p>
                <el-input
                  v-model="waterSourSearchVal"
                  placeholder="请输入内容"
                  @input="getWaterSourList"
                ></el-input>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <div class="reset" @click="resetParam">重置</div>
              </div>
            </el-col>
          </el-row>
        </div>
        <primary-map-table
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
  name: "WaterDetailDialog",
  props: {
    sideType: {
      type: String,
      default: "水库",
    },
  },
  data() {
    return {
      tableIndex: 0,
      columnList: [],
      dataList: [],
      stationData: [],
      tableLoading: false, // 表格loading

      acdc: "",
      adcdOption: [],
      waterSourType: "",
      waterSourTypeOption: [],
      waterSourScale: "",
      waterSourScaleOption: [],
      waterSourSearchVal: "",
    };
  },
  computed: {},
  components: {
    PrimaryMapTable,
  },
  created() {
    this.setColumnList(this.sideType);
  },
  mounted() {
    this.getAreaList();
  },
  watch: {},
  methods: {
    getWaterSourList() {},
    //设置表头
    setColumnList(val) {
      let columnArr = [];
      switch (val) {
        case "水源":
          columnArr = [
            {
              prop: "watShedArea",
              label: "水源地名称",
            },
            {
              prop: "watShedArea",
              label: "行政区划",
            },
            {
              prop: "watShedArea",
              label: "类型",
            },
            {
              prop: "watShedArea",
              label: "水量预警等级",
            },
          ];
          break;
      }
      this.columnList = columnArr;
      this.tableIndex = Math.random(10000);
    },
    //重置筛选条件
    resetParam() {},
    // 行政区划
    async getAreaList() {
      let res = await hydraulicApi.getArea({ areaCode: "3303", level: 3 });
      if (res.code == 0) {
        this.adcdOption.unshift({
          label: "全部",
          value: "",
        });
        for (let item of res.data) {
          this.adcdOption.push({
            label: item.adnm,
            value: item.adcd,
          });
        }
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.scope-content {
  display: flex;
  height: 568px;
  // padding: 0 22px;
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
  }
  ::v-deep .el-input__inner {
    // width: 100px;
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
      padding: 16px 0;
      display: flex;
      // .select-box {
      //   width: 171px;
      //   margin-right: 11px;
      //   &.search-area {
      //     ::v-deep .el-input {
      //       width: fit-content;
      //       .el-input__inner {
      //         width: 200px;
      //       }
      //     }
      //   }
      // }
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
}

.demo-input-suffix {
  display: flex;
  justify-content: left;
  align-items: center;
  margin-bottom: 10px;
}

.icon-shuiyuandi {
  background-size: 100% 100%;
  display: inline-block;
  width: 48px;
  height: 48px;
}
.icon-shuiyuandi-1 {
  background-image: url(~assets/images/shuiyuandi-1.png);
}
.icon-shuiyuandi-2 {
  background-image: url(~assets/images/shuiyuandi-2.png);
}
.icon-shuiyuandi-3 {
  background-image: url(~assets/images/shuiyuandi-3.png);
}
.icon-shuiyuandi-4 {
  background-image: url(~assets/images/shuiyuandi-4.png);
}
.icon-shuiyuandi-5 {
  background-image: url(~assets/images/shuiyuandi-5.png);
}
.icon-shuiyuandi-6 {
  background-image: url(~assets/images/shuiyuandi-6.png);
}
.icon-shuiyuandi-7 {
  background-image: url(~assets/images/shuiyuandi-7.png);
}
.icon-shuiyuandi-8 {
  background-image: url(~assets/images/shuiyuandi-8.png);
}
</style>

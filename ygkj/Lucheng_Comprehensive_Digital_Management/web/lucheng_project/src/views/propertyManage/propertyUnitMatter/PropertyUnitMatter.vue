<!--
 * @Author: hanyu
 * @LastEditTime: 2022-04-20 11:00:58
 * @Description: 海塘工程名录
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyUnitMatter\PropertyUnitMatter.vue
-->
<template>
  <div class="wrap">
    <!-- <div class="wrap_top" v-if="userType != '3'">
      <div class="top_item_box">
        <div class="item_box_title">
          <p>海塘预警</p>
        </div>
        <bar-chart class="chart_wrap" :chartData="chartData1"></bar-chart>
      </div>
      <div class="top_item_box">
        <div class="item_box_title">
          <p>海塘报警</p>
        </div>
        <bar-chart class="chart_wrap" :chartData="chartData2"></bar-chart>
      </div>
      <div class="top_item_box">
        <div class="item_box_title">
          <p>安全鉴定</p>
        </div>
        <pie-chart class="chart_wrap" :chartData="chartData3"></pie-chart>
      </div>
      <div class="top_item_box">
        <div class="item_box_title">
          <p>海塘等级</p>
        </div>
        <pie-chart class="chart_wrap" :chartData="chartData4"></pie-chart>
      </div>
    </div> -->
    <div class="wrap_bottom">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="水库" name="水库"></el-tab-pane>
        <el-tab-pane label="水闸" name="水闸"></el-tab-pane>
        <el-tab-pane label="堤防" name="堤防"></el-tab-pane>
        <el-tab-pane label="海塘" name="海塘"></el-tab-pane>
      </el-tabs>
      <div class="list_search_box">
        <div class="search_left">
          <span style="font-size: 14px">工程名称</span>
          <el-input
            placeholder="请输入"
            @change="getList"
            v-model="projectName"
            clearable
            style="width: 180px; margin: 0 20px 0 15px"
          />
        </div>
        <div class="search_right">
          <div
            :class="['change_model_btn', listModel === 1 ? 'active' : '']"
            @click="changeListModel(1)"
          >
            <img
              v-show="listModel === 1"
              src="../../../assets/images/listmodel1.png"
              alt=""
            />
            <img
              v-show="listModel !== 1"
              src="../../../assets/images/listmodel0.png"
              alt=""
            />
            <span>清单模式</span>
          </div>
          <div
            :class="['change_model_btn', listModel === 2 ? 'active' : '']"
            @click="changeListModel(2)"
          >
            <img
              v-show="listModel === 2"
              src="../../../assets/images/mapmodel1.png"
              alt=""
            />
            <img
              v-show="listModel !== 2"
              src="../../../assets/images/mapmodel0.png"
              alt=""
            />
            <span>地图模式</span>
          </div>
        </div>
      </div>
      <div class="list_model" v-if="listModel === 1">
        <el-table
          :data="projectList"
          border
          style="width: 100%"
          @row-click="goProjectDetail"
          v-loading="loading"
        >
          <el-table-column
            type="index"
            align="center"
            width="50"
            label="序号"
          ></el-table-column>
          <el-table-column prop="seawallName" label="工程名称" align="center">
            <template slot-scope="scope">
              <div style="color: #1492ff; cursor: pointer">
                {{ scope.row.name }}
              </div>
            </template>
          </el-table-column>
          <el-table-column label="管理层级" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.superviseLevel | superviseLevel }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="adcdctiy"
            label="所在市"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="adcdcountry"
            label="所在县"
            align="center"
          ></el-table-column>
          <el-table-column
            :prop="item.prop"
            :label="item.label"
            align="center"
            v-for="item in tableHeaderList"
            :key="item.prop"
          ></el-table-column>
          <el-table-column prop="safetyConclusion" label="操作" align="center">
            <template>
              <span style="color: #1492ff; cursor: pointer">查看详情</span>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="pageTotal"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          @pagination="getList"
        />
      </div>
      <div class="map_model" v-if="listModel === 2">
        <map-model ref="mapModelDom"></map-model>
        <div
          class="map_model_list"
          ref="tableContainer"
          :class="{ showList: showList }"
        >
          <img
            v-show="!showList"
            src="../../../assets/images/map_left_open.png"
            alt=""
            @click="clickShowList(true)"
          />
          <img
            v-show="showList"
            src="../../../assets/images/map_left_close.png"
            alt=""
            @click="clickShowList(false)"
          />
          <el-table
            :data="projectAllList"
            border
            style="width: 100%"
            @row-click="goProjectDetail"
            :height="tableHeight"
          >
            <el-table-column
              type="index"
              align="center"
              width="50"
              label="序号"
            ></el-table-column>
            <el-table-column prop="seawallName" label="工程名称" align="center">
              <template slot-scope="scope">
                <div style="color: #1492ff; cursor: pointer">
                  {{ scope.row.name }}
                </div>
              </template>
            </el-table-column>
            <el-table-column label="管理层级" align="center">
              <template slot-scope="scope">
                <span>{{ scope.row.superviseLevel | superviseLevel }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="adcdctiy"
              label="所在市"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="adcdcountry"
              label="所在县"
              align="center"
            ></el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getOutsideToken } from "@/api/index";
import { loadSkPageApi } from "@/api/projectUnitDetail";
import BarChart from "@/components/BarChart";
import PieChart from "@/components/PieChart";
import tableProps from "./projectTypeTable";
import MapModel from "./components/MapModel";
export default {
  name: "ProjectList",
  props: {},
  components: {
    BarChart,
    PieChart,
    MapModel,
  },
  filters: {
    superviseLevel(value) {
      switch (value) {
        case "1":
          return "省本级";
        case "2":
          return "市本级";
        case "3":
          return "县本级";
        case "4":
          return "乡镇街道";
        default:
          return "-";
      }
    },
  },
  data() {
    return {
      projectList: [],
      projectAllList: [],
      listModel: 1,
      tableHeight: 500,
      showList: true,
      projectName: "",
      pageNum: 1,
      pageSize: 10,
      pageTotal: 0,
      projectType: "水库",
      activeName: "水库",
      pageNumType: true,
      tableHeaderList: [],
      loading: false,
    };
  },

  mounted() {
    this.setProps();
    this.getList();
  },
  methods: {
    handleClick() {
      if (this.activeName != this.projectType) {
        this.projectType = this.activeName;
        this.loading = true;
        this.pageNum = 1;
        this.pageSize = 10;
        this.projectName = "";
        if (this.listModel == 1) {
          this.getList();
          this.setProps();
        } else {
          this.getAllProject();
        }
      }
    },
    setProps() {
      switch (this.projectType) {
        case "水库":
          this.tableHeaderList = tableProps.reservoir;
          break;
        case "水闸":
          this.tableHeaderList = tableProps.sluice;
          break;
        case "堤防":
          this.tableHeaderList = tableProps.dike;
          break;
        default:
          this.tableHeaderList = tableProps.seawall;
          break;
      }
    },
    async getList() {
      let res = await loadSkPageApi({
        projectName: this.projectName,
        projectType: this.projectType,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code === 0) {
        this.projectList = res.data.rows;
        this.pageTotal = res.data.total;
      }
      this.loading = false;
    },
    async getAllProject() {
      let res = await loadSkPageApi({
        projectName: this.projectName,
        projectType: this.projectType,
        pageNum: 1,
        pageSize: 9999,
      });
      if (res.code === 0) {
        this.projectAllList = res.data.rows;
        this.$nextTick(function () {
          const height = this.$refs.tableContainer.clientHeight;
          this.tableHeight = height;
          window.onresize = function () {
            const height = this.$refs.tableContainer.clientHeight;
            this.tableHeight = height;
          };
          this.listPoints();
        });
      }
    },
    async goProjectDetail(item) {
      this.$store.commit("SET_PROJECT_INFO", item);
      this.$router.push("/projectUnitMenu");
    },
    changeListModel(num) {
      this.listModel = num;
      if (num === 2) {
        this.getAllProject();
      } else {
        this.getList();
        this.setProps();
      }
    },
    listPoints() {
      this.$refs.mapModelDom.removeAllLayer()
      let lineArr = [];
      if (this.projectType != "海塘") {
        this.projectAllList.map((v) => {
          v.lat = v.lttd;
          v.lng = v.lgtd;
        });
        this.$refs.mapModelDom._drawPoints(this.projectAllList, this.projectType);
      } else {
        this.projectAllList.map((v) => {
          lineArr.push({
            line: v.coorpot
              ? JSON.parse(
                  v.coorpot.replace(new RegExp("&quot;", "gm"), '"')
                )[0]
              : "",
            level: v.scale,
          });
        });
        this.$refs.mapModelDom._drawLines(lineArr);
      }
    },
    clickShowList(flag) {
      this.showList = flag;
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  padding-bottom: 16px;
  .wrap_top {
    @include flexbox();
    @include flexJC(space-between);
    margin-bottom: 16px;
    .top_item_box {
      @include contentBox();
      width: calc(25% - 12px);
      height: 320px;
      .chart_wrap {
        width: 100%;
        height: calc(100% - 56px);
      }
    }
  }
  .wrap_bottom {
    @include contentBox();
    box-sizing: border-box;
    padding: 8px 16px 16px 16px;
    .list_search_box {
      @include flexbox;
      @include flexAI(center);
      @include flexJC(space-between);
      padding: 16px 0;
      .search_left {
      }
      .search_right {
        @include flexbox;
        .change_model_btn {
          width: 110px;
          height: 32px;
          background: #f8f8f8;
          border: 1px solid #d6d6d6;
          border-radius: 4px;
          box-sizing: border-box;
          cursor: pointer;
          @include flexbox;
          @include flexAI(center);
          @include flexJC(center);
          img {
            margin-right: 8px;
            vertical-align: middle;
          }
          span {
            color: #333;
            font-size: 14px;
            vertical-align: middle;
          }
          &.active {
            border: 1px solid #1492ff;
            background: #1492ff;
            span {
              color: #fff;
            }
          }
          &:first-child {
            margin-right: 8px;
          }
        }
      }
    }
    .map_model {
      position: relative;
      width: 100%;
      height: 500px;
      overflow: hidden;
      .map_model_list {
        height: 100%;
        width: 40%;
        position: absolute;
        top: 0;
        right: -40%;
        @include transition(all, 0.3s, ease);
        & > img {
          position: absolute;
          left: -20px;
          top: 50%;
          margin-top: -33px;
          cursor: pointer;
        }
      }
      .showList {
        right: 0;
      }
    }
  }
}
</style>
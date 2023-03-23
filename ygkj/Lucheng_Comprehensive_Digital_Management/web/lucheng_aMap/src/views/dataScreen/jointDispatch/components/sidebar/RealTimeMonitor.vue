<!-- 实时视频 -->
<template>
  <div class="real-time-monitor sidebar-box">
    <div class="sidebar-search">
      <el-input
        class="search-inp"
        v-model="name"
        placeholder="请输入关键字进行搜索"
        @change="getList"
      >
        <i slot="prefix" class="el-input__icon el-icon-search"></i>
      </el-input>
    </div>
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="水位信息" name="1">
        <div class="sidebar-list">
          <el-table
            :data="tableData"
            style="width: 100%"
            height="260"
            highlight-current-row
            v-loading="loading"
            @row-click="openWaterDialog"
          >
            <el-table-column type="index" label width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="adnm" label="站点名称"></el-table-column>
            <el-table-column prop="drpSum" label="水位(m)"></el-table-column>
            <el-table-column prop="drpSum" label="时间"></el-table-column>
          </el-table>
        </div>
      </el-collapse-item>
      <el-collapse-item title="降雨信息" name="2">
        <div class="sidebar-list">
          <el-table
            :data="tableData"
            style="width: 100%"
            height="260"
            highlight-current-row
            v-loading="loading"
            @row-click="openRainDialog"
          >
            <el-table-column type="index" label width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="adnm" label="名称"></el-table-column>
            <el-table-column prop="drpSum" label="雨量(m)"></el-table-column>
          </el-table>
        </div>
      </el-collapse-item>
      <el-collapse-item title="视频信息" name="3">
        <div class="sidebar-list">
          <el-table
            :data="tableData"
            style="width: 100%"
            height="260"
            highlight-current-row
            v-loading="loading"
            @row-click="openVideoDialog"
          >
            <el-table-column type="index" label width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="drpSum"
              label="摄像机名称(m)"
            ></el-table-column>
          </el-table>
        </div>
      </el-collapse-item>
    </el-collapse>

    <!-- 雨量 -->
    <rain-dialog
      v-if="rainVisible"
      :dialogVisible="rainVisible"
      @handleClose="rainClose"
      :info="info"
      :name="name"
    ></rain-dialog>
    <!-- 水位 -->
    <water-dialog
      v-if="waterVisible"
      :dialogVisible="waterVisible"
      @handleClose="waterClose"
      :info="info"
      :name="name"
    ></water-dialog>
    <!-- 视频 -->
    <video-dialog
      v-if="videoVisible"
      :dialogVisible="videoVisible"
      @handleClose="videoClose"
      :info="info"
      :name="name"
    ></video-dialog>
  </div>
</template>

<script>
import { rainApi } from "@/api/dataScreen/floodJudge/index";
import RainDialog from "../RainDialog";
import WaterDialog from "../WaterDialog";
import VideoDialog from "../VideoDialog";

export default {
  name: "Sidebar",
  props: {},
  components: {
    RainDialog,
    WaterDialog,
    VideoDialog,
  },
  data() {
    return {
      rainVisible: false,
      waterVisible: false,
      videoVisible: false,
      activeName: 1,
      name: "",
      activeSidebar: 0,
      tableData: [],
      loading: false,
      info: {},
      name: "",
    };
  },
  mounted() {
    // this.getList();
  },
  methods: {
    async getList() {
      this.loading = true;
      let res = await rainApi.realRainFall({
        adnm: this.name,
      });
      if (res) {
        res.data.map((item) => {
          if (item.drpSum == null) {
            item.highestLevel = "-";
          }
        });
        this.tableData = res.data;
        this.loading = false;
      }
    },
    openRainDialog(row) {
      this.name = row.adnm;
      this.rainVisible = true;
    },
    openWaterDialog(row) {
      this.name = row.adnm;
      this.waterVisible = true;
    },
    openVideoDialog(row) {
      this.name = row.adnm;
      this.videoVisible = true;
    },
    rainClose() {
      this.rainVisible = false;
    },
    waterClose() {
      this.waterVisible = false;
    },
    videoClose() {
      this.videoVisible = false;
    },
  },
};
</script>

<style lang='scss' scoped>
@import "@/style/bigScreenSidebar.scss";
.real-time-monitor.sidebar-box {
  .sidebar-list {
    margin-top: 0;
  }
  .search-inp {
    margin-bottom: 10px;
  }
}
</style>
<style lang="scss">
.el-collapse {
  border: 0;
  .el-collapse-item__header {
    height: 32px;
    background-color: transparent;
    border: none;
    color: #3f9bfc;
    font-size: 14px;
    padding-left: 10px;
    background: url(../../../../../assets/images/title-border.png) no-repeat;
    background-size: 100% 100%;
    margin-bottom: 10px;
  }
  .el-collapse-item__wrap {
    background-color: transparent;
    border: none;
  }
  .el-collapse-item__content {
    padding-bottom: 0;
  }
}
$searchInpHeight: 25px;
$mainColor: #3f9bfc;
$tdHeight: 37px;
.sidebar-box {
  .el-input__inner,
  .el-input__icon {
    color: $mainColor;
    height: $searchInpHeight;
    line-height: $searchInpHeight;
    &::-webkit-input-placeholder {
      color: $mainColor;
    }

    &::-moz-placeholder {
      color: $mainColor;
    }

    &:-ms-input-placeholder {
      color: $mainColor;
    }
  }
  .el-select__caret.el-input__icon.el-icon-arrow-up {
    color: $mainColor;
  }
  .sidebar-list {
    .el-table .cell {
      height: $tdHeight;
      line-height: $tdHeight;
    }
    .el-table th,
    .el-table td {
      padding: 0;
      font-size: 16px;
      color: #2c5a90;
      text-align: left;
    }
    .el-table th {
      padding: 3px 0;
      background-color: transparent;
      font-weight: bold;
    }
    .el-table__body tr.current-row > td {
      color: #fff;
      background-color: transparent;
    }
  }
}
.bigScreen-sel {
  .el-select-dropdown__item.selected {
    color: $mainColor;
  }
  .el-select-dropdown__item:hover {
    background-color: transparent;
  }
}
.sidebar-box .el-loading-mask {
  background-color: transparent;
}
</style>
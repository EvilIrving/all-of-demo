<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>水域审批</p>
    </div>

    <div class="map_box">
      <map-model ref="mapModelDom" @mapPointClick="mapPointClick"></map-model>
      <div class="box_info">
        <div class="info_title">
          <img src="../../../assets/images/info_title.png" alt="" />
          <span>水域审批信息</span>

          <span style="font-size: 14px; margin-left: 78px; margin-top: 6px"
            >年份：</span
          >
          <el-select
            clearable
            v-model="value"
            @change="getList"
            placeholder="请选择"
            size="mini"
            style="width: 100px"
          >
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.label"
            >
            </el-option>
          </el-select>
        </div>
        <img src="../../../assets/images/title_line.png" alt="" />
        <div class="table_box">
          <el-input
            placeholder="请输入关键字查询"
            prefix-icon="el-icon-search"
            v-model="keyword"
            size="small"
            @change="getList"
            style="
              height: 32px;
              width: 366px;
              margin-top: 16px;
              margin-bottom: 16px;
            "
          >
          </el-input>
          <el-table
            :data="tableData"
            ref="chooseTable"
            stripe
            height="520"
            style="width: 100%"
          >
            <el-table-column
              type="index"
              align="center"
              width="50"
              label="序号"
            ></el-table-column>
            <el-table-column
              show-overflow-tooltip
              prop="projectName"
              label="项目名称"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="principalName"
              label="联系人"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="wadingTime"
              label="涉河时长"
              align="center"
            ></el-table-column
            >``
          </el-table>
          <!-- <pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[10,20]" @pagination="getList" /> -->
        </div>
      </div>
    </div>
    <el-dialog
      :title="mapData.projectName"
      :visible.sync="dialogVisible"
      width="1200px"
      :before-close="handleClose"
    >
      <el-descriptions direction="vertical" :column="3" border>
        <el-descriptions-item label-class-name="my-label" label="项目编码">{{
          mapData.projectCode || "-"
        }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="单位名称">{{
          mapData.enterpriseName || "-"
        }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="涉河水域">{{
          mapData.wadingWaters || "-"
        }}</el-descriptions-item>
        <el-descriptions-item
          label-class-name="my-label"
          label="占用水域面积㎡"
          >{{ mapData.occupiedWaterArea || "-" }}</el-descriptions-item
        >
        <el-descriptions-item label-class-name="my-label" label="批复文号">{{
          mapData.approvalNumber || "-"
        }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="涉河时长">{{
          mapData.wadingTime || "-"
        }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="联系人">{{
          mapData.principalName || "-"
        }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="联系电话">{{
          mapData.principalTel || "-"
        }}</el-descriptions-item>
        <el-descriptions-item label-class-name="my-label" label="受理日期">{{
          mapData.applyDate || "-"
        }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { approvalProject } from "@/api/projectSupervision";
import MapModel from "@/views/occupyApprove/hydrograph/components/MapModel";
export default {
  name: "waterApproval",
  prop: {},
  components: {
    MapModel,
  },
  data() {
    return {
      tableData: [],
      options: [
        {
          value: 2019,
          label: 2019,
        },
        {
          value: 2020,
          label: 2020,
        },
        {
          value: 2021,
          label: 2021,
        },
        {
          value: 2022,
          label: 2022,
        },
      ],
      value: "",
      keyword: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      dialogVisible: false,
      mapData: {},
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    handleClose() {
      this.dialogVisible = false;
    },
    mapPointClick(data) {
      this.mapData = data;
      this.dialogVisible = true;
    },
    async getList() {
      await approvalProject
        .list({
          key: this.keyword,
          year: this.value,
          fetchAll: true,
          // pageSize: this.pageSize,
          // pageNum: this.pageNum,
        })
        .then((res) => {
          if (res.code === 0) {
            this.tableData = res.data.list;
            this.pageTotal = res.data.totalRows;
            this.listPoints();
          }
        });
    },
    listPoints() {
      let pointArr = this.tableData.map((v) => {
        return {
          lat: v.prLat,
          lng: v.prLong,
          type: "project",
          ...v,
        };
      });
      this.$refs.mapModelDom._drawPoints(pointArr);
    },
  },
};
</script>

<style scoped lang="scss">
.wrap {
  @include contentBox();
  height: 100%;
  .item_box_title {
    display: block;
    padding-top: 16px;
    height: 40px;
  }
  .map_box {
    height: 834px;
    .box_info {
      position: absolute;
      top: 242px;
      left: 1497px;
      @include contentBox();
      height: 75%;
      width: 400px;
      img {
        width: 366px;
        margin-left: 16px;
      }
      .info_title {
        margin-top: 16px;
        display: flex;
        flex-direction: row;
        img {
          width: 18px;
          height: 18px;
          margin-right: 9px;
        }
        span {
          font-size: 18px;
        }
      }
      .chart_box {
        display: flex;
        flex-direction: row;
      }
      .table_box {
        width: 366px;
        margin-left: 16px;
      }
    }
  }
}
</style>

<style>
.my-label {
  background: #f2f3f5 !important;
}
</style>
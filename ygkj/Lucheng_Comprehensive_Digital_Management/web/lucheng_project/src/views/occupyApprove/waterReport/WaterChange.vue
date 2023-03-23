<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-06 15:04:20
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\waterReport\WaterChange.vue
-->
<template>
  <div class="wrap">
    <div v-if="isShowWatersReport">
      <div class="item_box_title">
        <p>水域变化报表</p>
      </div>
      <div class="list_search_box">
        <div class="search_left">
          <span style="font-size: 14px">名称：</span>
          <el-input
            placeholder="请输入"
            v-model="name"
            @change="getList"
            clearable
            style="width: 180px; margin: 0 20px 0 0"
          />
          <span style="font-size: 14px">行政区划：</span>
          <el-select
            v-model="areaValue"
            placeholder="请选择"
            style="width: 180px; margin: 0 20px 0 0"
            clearable
            @change="getList"
          >
            <el-option
              v-for="item in areaOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </div>
        <div class="search_right">
          <el-button
            type="primary"
            @click="exportTable"
            v-permission="'waterChange:export'"
            >导出</el-button
          >
        </div>
      </div>
      <div class="tb_wrapper">
        <el-table :data="tableData" border style="width: 100%">
          <!-- <el-table-column type="selection" width="50"> </el-table-column> -->
          <el-table-column
            type="index"
            align="center"
            width="50"
            label="序号"
          ></el-table-column>
          <el-table-column
            prop="name"
            label="水域名称"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="adcdNames"
            label="行政区划"
            align="center"
          ></el-table-column>
          <el-table-column prop="area" label="水域面积 ㎡" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.area ? scope.row.area : "-" }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" width="190">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="skip(scope.row)"
                  v-permission="'waterChange:look'"
                  >查看</span
                >
              </div>
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
    </div>
    <div v-if="isShowWatersChange">
      <div class="item_box_title">
        <p>{{ waterName }}</p>
      </div>
      <div class="search-right">
        <el-button type="primary" @click="exportData">导出</el-button>
        <el-button type="primary" @click="edit('新增')">新增</el-button>
        <el-button type="primary" @click="goBack">返回</el-button>
      </div>
      <div class="tb_wrapper">
        <el-table :data="tableChangeData" border style="width: 100%">
          <el-table-column
            prop="yearTime"
            label=""
            align="center"
          ></el-table-column>
          <el-table-column prop="area" label="水域面积 ㎡" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.area ? scope.row.area : "-" }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="capacity" label="库容量" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.capacity ? scope.row.capacity : "-" }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="bottomElevation"
            label="河底高程"
            align="center"
          >
          </el-table-column>
          <el-table-column prop="shoreline" label="实际岸线" align="center">
          </el-table-column>
          <el-table-column align="center" label="操作" width="190">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="edit('编辑', scope.row.id)"
                  >编辑</span
                >
                <span
                  class="deal_btn deal_btn_del"
                  @click="delChange(scope.row.id)"
                  >删除</span
                >
              </div>
            </template>
          </el-table-column>
        </el-table>
        <!-- <pagination
          :total="pageChangeTotal"
          :page.sync="pageChangeNum"
          :limit.sync="pageChangeSize"
          :page-sizes="[10, 20, 50, 100]"
          @pagination="getList"
        /> -->
      </div>
      <el-dialog
        :title="changeTitle"
        width="960px"
        :visible.sync="visibleChangeDialog"
        :before-close="handleChangeClose"
      >
        <WaterChangeAddFrom
          @closeChangeDialog="closeChangeDialog"
          :id="changeId"
          :reportId="reportId"
          v-if="visibleChangeDialog"
        />
      </el-dialog>
    </div>
  </div>
</template>

<script>
import WaterChangeAddFrom from "./components/WaterChangeAddFrom.vue";
import { watersReportApi } from "@/api/occupyApprove.js";
import { projectSupervisionApi } from "@/api/projectSupervision";
import LookFile from "@/components/LookFile";
import { watersReportExport, watersChangeExport } from "@/utils/export.js";
export default {
  data() {
    return {
      isShowWatersReport: true,
      isShowWatersChange: false,
      waterName: "",
      name: "",
      areaOptions: [],
      areaValue: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      visibleDialog: false,
      id: "",
      title: "新增",
      pictureDialog: false,
      picList: [],
      //   pageChangeTotal: 0,
      //   pageChangeNum: 1,
      //   pageChangeSize: 10,
      tableChangeData: [],
      visibleChangeDialog: false,
      changeId: "",
      changeTitle: "新增",
      reportId: "",
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: {
    // WaterReportAddFrom,
    WaterChangeAddFrom,
  },
  mounted() {
    this.getAdcdList(); // 行政区划接口
    this.getList();
  },
  methods: {
    // 行政区划
    async getAdcdList() {
      let res = await projectSupervisionApi.getAdcdList({
        areaCode: "330302",
        level: 4,
      });
      if (res.code == 0) {
        console.log(res.data);
        // this.areaOptions = [
        //   {
        //     value: null,
        //     label: '全部',
        //   },
        // ]
        res.data.forEach((item) => {
          this.areaOptions.push({
            value: item.adcd,
            label: item.adnm,
          });
        });
      }
    },
    exportTable() {
      let obj = {
        name: this.name,
        adcds: this.areaValue,
        waterType: this.waterTypeValue,
        area: this.waterAreaValue,
      };
      watersReportExport(obj);
    },
    exportData() {
      let obj = {
        reportId: this.reportId,
      };
      watersChangeExport(obj);
    },
    async getList() {
      await watersReportApi
        .list({
          name: this.name,
          adcds: this.areaValue,
          waterType: this.waterTypeValue,
          area: this.waterAreaValue,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
        })
        .then((res) => {
          if (res.code === 0) {
            console.log(res.data.list);
            this.tableData = res.data.list;
            this.pageTotal = res.data.totalRows;
          }
        });
    },
    async getChangeList() {
      await watersReportApi
        .changeList({
          reportId: this.reportId,
          pageSize: this.pageChangeSize,
          pageNum: this.pageChangeNum,
        })
        .then((res) => {
          if (res.code === 0) {
            this.tableChangeData = res.data;
            // this.pageChangeTotal = res.data.totalRows
          }
        });
    },
    handleChangeClose() {
      this.visibleChangeDialog = false;
    },
    closeChangeDialog() {
      this.visibleChangeDialog = false;
      this.getChangeList();
    },
    edit(type, id) {
      // console.log(id);
      this.changeId = id;
      this.changeTitle = type;
      this.visibleChangeDialog = true;
    },
    skip(item) {
      console.log(item);
      this.reportId = item.id;
      this.getChangeList();
      this.waterName = item.name;
      this.isShowWatersReport = !this.isShowWatersReport;
      this.isShowWatersChange = !this.isShowWatersChange;
    },
    goBack() {
      this.getList();
      this.isShowWatersReport = !this.isShowWatersReport;
      this.isShowWatersChange = !this.isShowWatersChange;
    },
    delChange(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        watersReportApi
          .changeDel({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getChangeList();
            } else {
              this.$message({
                type: "error",
                message: "删除失败！",
              });
            }
          });
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .list_search_box {
    @include flexbox;
    @include flexAI(center);
    @include flexJC(space-between);
    padding: 25px 16px 16px;
  }
  .point {
    display: inline-block;
    border-radius: 50%;
    width: 6px;
    height: 6px;
    background: #01a59c;
    vertical-align: middle;
    margin-right: 7px;
    &.blue {
      background: #1492ff;
    }
    &.red {
      background: #ff0000;
    }
  }
  .search-right {
    text-align: right;
    padding: 25px 16px 16px;
  }
}
.popover_title {
  span {
    float: right;
  }
}
.popover_content {
  background: rgba(216, 216, 216, 0.19);
  border-radius: 2px;
}
::v-deep .el-dialog__body {
  //   padding: 0;
}
</style>
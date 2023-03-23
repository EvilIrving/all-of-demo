<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-06 15:02:30
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\occupyApprove\waterReport\WaterArea.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>水域面积报表</p>
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
          v-permission="'waterArea:export'"
          @click="exportTable"
          >导出</el-button
        >
        <el-button
          type="primary"
          v-permission="'waterArea:add'"
          @click="editTable('新增')"
          >新增</el-button
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
                @click="editTable('编辑', scope.row.id)"
                v-permission="'waterArea:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                v-permission="'waterArea:del'"
                >删除</span
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
    <el-dialog
      :title="title"
      width="520px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <WaterAreaAddFrom
        @close="closeDialog"
        :id="id"
        :areaOptions="areaOptions"
        v-if="visibleDialog"
      />
    </el-dialog>
  </div>
</template>

<script>
import WaterAreaAddFrom from "./components/WaterAreaAddFrom.vue";
import { watersReportApi } from "@/api/occupyApprove.js";
import { projectSupervisionApi } from "@/api/projectSupervision";
import { watersReportExport } from "@/utils/export.js";
export default {
  data() {
    return {
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
      //   pageChangeTotal: 0,
      //   pageChangeNum: 1,
      //   pageChangeSize: 10,
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: {
    WaterAreaAddFrom,
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
      };
      watersReportExport(obj);
    },
    async getList() {
      await watersReportApi
        .list({
          name: this.name,
          adcds: this.areaValue,
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
    handleClose() {
      this.visibleDialog = false;
    },
    closeDialog() {
      this.visibleDialog = false;
      this.getList();
    },
    editTable(type, id) {
      // console.log(id);
      this.id = id;
      this.title = type;
      this.visibleDialog = true;
    },
    del(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        watersReportApi
          .del({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getList();
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
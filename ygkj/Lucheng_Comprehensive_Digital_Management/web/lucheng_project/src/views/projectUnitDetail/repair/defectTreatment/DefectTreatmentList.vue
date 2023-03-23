<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-06 16:05:13
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\repair\defectTreatment\DefectTreatmentList.vue
-->
<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-04-20 17:52:40
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\operation\dispatch\DispatchOperation.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>隐患处理</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <!-- <span style="font-size: 14px">名称：</span>
        <el-input
          placeholder="请输入"
          v-model="projectName"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
          @change="search"
        /> -->
        <span style="font-size: 14px">隐患状态：</span>
        <el-select
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="status"
          placeholder="请选择"
          clearable
          @change="search"
        >
          <el-option label="待处理" value="0"> </el-option>
          <el-option label="处理中" value="1"> </el-option>
          <el-option label="已处理" value="2"> </el-option>
        </el-select>
        <span style="font-size: 14px">处理方式：</span>
        <el-select
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="processWay"
          placeholder="请选择"
          clearable
          @change="search"
        >
          <el-option label="简单处理" value="简单处理"> </el-option>
          <el-option label="日常跟踪" value="日常跟踪"> </el-option>
          <el-option label="日常维修" value="日常维修"> </el-option>
          <el-option label="年度大修" value="年度大修"> </el-option>
          <el-option label="综合整治" value="综合整治"> </el-option>
        </el-select>
        <span style="font-size: 14px">隐患来源：</span>
        <el-select
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="source"
          clearable
          @change="search"
        >
          <el-option label="日常巡查" value="日常巡查"> </el-option>
          <el-option label="汛前检查" value="汛前检查"> </el-option>
          <el-option label="汛后检查" value="汛后检查"> </el-option>
          <el-option label="专项检查" value="专项检查"> </el-option>
          <el-option label="特别检查" value="特别检查"> </el-option>
        </el-select>
      </div>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="projectType"
          label="工程类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="projectName"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="hiddenDanger"
          label="隐患描述"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="processWay"
          label="处理方式"
          align="center"
        ></el-table-column>
        <el-table-column label="来源" prop="problemSource" align="center">
        </el-table-column>
        <el-table-column label="上报人" prop="creator" align="center">
        </el-table-column>
        <el-table-column
          prop="hiddenDangerStatus"
          label="隐患状态"
          align="center"
        >
          <template slot-scope="scope">
            <span class="wathet-color">{{
              scope.row.hiddenDangerStatus | hiddenDangerStatus
            }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span class="deal_btn deal_btn_look" @click="lookTable(scope.row)" v-permission="'defectTreatment:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="removeTable(scope.row.id)"
                v-permission="'defectTreatment:del'"
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
        @pagination="dailyTask"
      />
    </div>
  </div>
</template>

<script>
import { annulMaintenance } from "@/api/projectUnitDetail";
export default {
  data() {
    return {
      projectName: "",
      status: "",
      processWay: "",
      source: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      visibleDialog: false,
      id: "",
      title: "新增",
      sluiceDialog: false,
      tunnelSpillwayDialog: true,
    };
  },
  filters: {
    hiddenDangerStatus(value) {
      switch (value) {
        case 2:
          return "已处理";
        case 1:
          return "处理中";
        default:
          return "待处理";
      }
    },
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: {},
  mounted() {
    this.dailyTask();
  },
  methods: {
    search() {
      this.pageNum = 1;
      this.dailyTask();
    },
    async dailyTask() {
      let res = await annulMaintenance.DailyTask({
        prcd: this.projectDetail.prcd,
        projectName: this.projectName,
        hiddenDangerStatus: this.status,
        processWay: this.processWay,
        problemSource: this.source,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    removeTable(id) {
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          annulMaintenance
            .delDailyTask({
              id,
            })
            .then((res) => {
              if (res.code == 0) {
                this.$message.success("删除成功");
                this.dailyTask();
              } else {
                this.$message.error(res.message);
              }
            })
            .catch(() => {
              this.$message.error(res.message);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    lookTable(data) {
      this.$store.commit("SET_TROUBLE_DETAIL", data);
      this.$emit("checkType", 2);
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
  .wathet-color {
    color: #1492ff;
  }
}
</style>
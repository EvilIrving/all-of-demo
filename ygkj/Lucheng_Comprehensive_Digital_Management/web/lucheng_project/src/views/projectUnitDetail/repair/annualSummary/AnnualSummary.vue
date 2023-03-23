<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-06 16:05:56
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\repair\annualSummary\AnnualSummary.vue
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
      <p>年度汇总</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <!-- <span style="font-size: 14px">名称：</span>
        <el-input placeholder="请输入" v-model="projectName" clearable style="width: 180px; margin: 0 20px 0 15px" @change="search" /> -->
        <span style="font-size: 14px">年份：</span>
        <el-date-picker
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="year"
          placeholder="请选择"
          clearable
          @change="search"
          value-format="yyyy"
          type="year"
        >
        </el-date-picker>
        <span style="font-size: 14px">日期：</span>
        <el-date-picker
          v-model="date"
          clearable
          @change="search"
          type="date"
          style="width: 180px; margin: 0 20px 0 15px"
          placeholder="选择日期"
        >
        </el-date-picker>
        <span style="font-size: 14px">问题状态：</span>
        <el-select
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="hiddenDangerStatus"
          clearable
          @change="search"
        >
          <el-option label="待处理" value="2"> </el-option>
          <el-option label="处理中" value="3"> </el-option>
          <el-option label="已处理 " value="1"> </el-option>
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
          prop="year"
          label="年份"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="tm"
          label="日期"
          align="center"
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
        <el-table-column label="存在问题" prop="hiddenDanger" align="center">
        </el-table-column>
        <el-table-column
          prop="hiddenDangerStatus"
          label="问题状态"
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
              <span
                class="deal_btn deal_btn_look"
                @click="lookTable(scope.row)"
                v-permission="'annualSummary:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_del"
                v-permission="'annualSummary:del'"
                @click="removeTable(scope.row.id)"
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
        @pagination="loadPageMtDailyTask"
      />
    </div>
    <el-dialog
      title="查看"
      :visible.sync="dialogVisible"
      width="960px"
      :before-close="handleClose"
    >
      <div>
        <div class="wrap_box_title">
          <p>基本信息</p>
        </div>
        <div class="info-one-box">
          <div class="info-one padding-22">
            <div>
              <div class="lable-item">工程名称</div>
              <div class="value-item">{{ detailTableInfo.projectName }}</div>
            </div>
            <div>
              <div class="lable-item">工程类型</div>
              <div class="value-item">{{ detailTableInfo.projectType }}</div>
            </div>
            <div>
              <div class="lable-item">问题状态</div>
              <div class="value-item">
                <span>{{
                  detailTableInfo.hiddenDangerStatus | hiddenDangerStatus
                }}</span>
              </div>
            </div>
            <div>
              <div class="lable-item">日期</div>
              <div class="value-item">{{ detailTableInfo.tm }}</div>
            </div>
          </div>
          <div class="padding-22">
            <div class="lable-item">存在问题</div>
            <div class="value-item">
              {{ detailTableInfo.hiddenDanger }}
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  loadPageMtDailyTaskApi,
  annulMaintenance,
} from "@/api/projectUnitDetail";
export default {
  data() {
    return {
      projectName: "",
      hiddenDangerStatus: "",
      date: "",
      year: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      dialogVisible: false,
      detailTableInfo: {},
    };
  },
  filters: {
    hiddenDangerStatus(value) {
      // 2-已处理、0-待处理、1-处理中）
      switch (value) {
        case 0:
          return "待处理";
        case 2:
          return "已处理";
        default:
          return "处理中";
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
    this.loadPageMtDailyTask();
  },
  methods: {
    handleClose() {
      this.dialogVisible = false;
    },
    search() {
      this.pageNum = 1;
      this.loadPageMtDailyTask();
    },
    async loadPageMtDailyTask() {
      let res = await loadPageMtDailyTaskApi({
        prcd: this.projectDetail.prcd,
        year: this.year,
        hiddenDangerStatus: this.hiddenDangerStatus,
        date: this.date,
        projectName: this.projectName,
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
                this.loadPageMtDailyTask();
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
      this.detailTableInfo = data;
      this.dialogVisible = true;
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
.wrap_box_title {
  position: relative;
  line-height: 25px;
  padding: 0 10px 16px 14px;
  border-bottom: 1px solid #eeeeee;
  &::after {
    content: "";
    position: absolute;
    width: 4px;
    height: 20px;
    background: #1492ff;
    left: 0px;
    top: 2px;
  }
  > p {
    font-size: 18px;
    font-weight: bold;
    color: #333333;
  }
}
.info-one-box {
  padding: 24px 0 0 14px;
}
.lable-item {
  font-size: 16px;
  color: #999999;
  padding-bottom: 10px;
}
.value-item {
  font-weight: 500;
  color: #333333;
  line-height: 18px;
  font-size: 16px;
  span {
    color: #1492ff;
  }
}
.padding-22 {
  padding-bottom: 22px;
}
.info-one {
  display: flex;
  > div {
    flex: 1;
  }
}
</style>
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>值班值守</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <!-- <span style="font-size: 14px; margin-left: 15px">工程名称：</span>
      <el-input
        placeholder="请输入"
        v-model="name"
        style="width: 180px; margin-right: 15px"
        disabled
      /> -->
      <span style="font-size: 14px; margin-left: 15px">值班日期：</span>
      <el-date-picker
        type="date"
        placeholder="请选择"
        format="yyyy-MM"
        value-format="yyyy-MM"
        v-model="date"
        @change="getList"
      ></el-date-picker>

      <el-button
        type="primary"
        style="margin-right: 32px"
        class="right"
        @click="inportTable()"
        v-permission="'duty:import'"
        >导入</el-button
      >
      <el-button
        type="primary"
        style="margin-right: 32px"
        class="right"
        @click="exportTable()"
        v-permission="'duty:export'"
        >导出</el-button
      >
      <el-button
        type="primary"
        class="right"
        @click="editTable('新增', '')"
        style="margin-right: 24px"
        v-permission="'duty:add'"
        >新增</el-button
      >
    </div>

    <div class="tb_wrapper">
      <el-table
        :data="tableData"
        ref="chooseTable"
        stripe
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center">
        </el-table-column>
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="dutyDate"
          label="值班日期"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="leaderName"
          label="值班负责人"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="mgtDayName"
          label="值班员"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="mgtNightName"
          label="水工巡查人员"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="mgtNightPhone"
          label="联系电话"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="devicePatrolName"
          label="设备巡查人员"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="devicePatrolPhone"
          label="联系电话"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="dutyPhone"
          label="值班电话"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="patrolType"
          label="巡查类型"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'duty:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delCard(scope.row.id)"
                v-permission="'duty:del'"
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
        :page-sizes="[10, 20, 30, 40]"
        @pagination="getList"
      />
    </div>

    <el-dialog :title="title" :visible.sync="showEditDialog" width="960px">
      <new-duty-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></new-duty-form>
    </el-dialog>

    <el-dialog :title="intitle" :visible.sync="showInportDialog" width="960px">
      <inport-form
        v-if="inportDialog"
        :id="id"
        @cancelDialog="closeEditDialog"
      ></inport-form>
    </el-dialog>
  </div>
</template>

<script>
import newDutyForm from "@/views/projectUnitDetail/duty/newDutyForm";
import inportForm from "@/views/projectUnitDetail/duty/inportForm";
import { beOnDuty } from "@/api/projectUnitDetail";
import { exportDuty } from "@/utils/export.js";
export default {
  name: "Index",
  props: {},
  components: {
    newDutyForm,
    inportForm,
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  data() {
    return {
      name: "",
      tableData: [],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      id: "",
      url: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      fileDialog: false,
      date: "",
      intitle: "导入",
      inportDialog: false,
      showInportDialog: false,
    };
  },
  mounted() {
    this.name = this.projectInfo.name;
    this.getList();
  },
  methods: {
    async getList() {
      let res = await beOnDuty.getOnDutyList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        pid: this.projectInfo.prcd,
        timeYm: this.date,
      });
      console.log(res);
      if (res.code == 0) {
        console.log(res);
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    editTable(title, id) {
      this.id = id;
      this.title = title;
      //console.log(id,title)
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    inportTable() {
      this.showInportDialog = true;
      this.inportDialog = false;
      this.$nextTick().then(() => {
        this.inportDialog = true;
      });
    },
    delCard(id) {
      this.$confirm("此操作会删除该值班任务所有信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        beOnDuty.delOnDutyList({ id: id }).then((res) => {
          if (res.code == 0) {
            if (this.tableData.length == 1 && this.pageNum > 1) {
              this.pageNum--;
            }
            this.getList();
            this.$message({
              type: "success",
              message: "删除成功!",
            });
          } else {
            this.$message({
              type: "error",
              message: "删除失败！",
            });
          }
        });
      });
    },
    handleSelectionChange(val) {
      console.log(val, "val");
      this.checkItems = val;
      this.num = val.length;
    },
    exportTable() {
      let obj = {
        pid: this.projectInfo.prcd,
        timeYm: this.date,
      };
      exportDuty(obj);
    },
    closeEditDialog(num) {
      if (num) {
        this.getList();
      }
      this.editDialog = false;
      this.showEditDialog = false;
      this.inportDialog = false;
      this.showInportDialog = false;
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
  .look {
    cursor: pointer;
  }
  .info_option {
    height: 80px;
  }
}
</style>
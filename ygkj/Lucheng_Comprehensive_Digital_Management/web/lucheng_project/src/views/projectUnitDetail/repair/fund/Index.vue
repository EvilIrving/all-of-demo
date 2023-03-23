<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>维修养护资金</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input
        placeholder="请输入批复单位"
        prefix-icon="el-icon-search"
        v-model="name"
        @change="getList"
        style="width: 180px; margin-right: 15px"
        clearable
      />
      <span style="font-size: 14px; margin-left: 15px">年份：</span>
      <el-select
        v-model="scale"
        placeholder="请选择"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
        @change="getList"
      >
        <el-option
          v-for="item in scaleOptions"
          :key="item.label"
          :label="item.label"
          :value="item.label"
        >
        </el-option>
      </el-select>
      <el-button
        type="primary"
        class="right"
        @click="editTable('新增', '')"
        v-permission="'fund:add'"
        style="margin-right: 24px"
        >新增</el-button
      >
      <el-button
        type="primary"
        style="margin-right: 32px"
        class="right"
        v-permission="'fund:export'"
        @click="exportTable()"
        >导出</el-button
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
          prop="year"
          label="年份"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="declarationTime"
          label="申报时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="approvalUnit"
          label="批复单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="approvalTime"
          label="批复时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="dclrFunds"
          label="申报资金(万元)"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="superiorfunds"
          label="上级下达资金(万元)"
          align="center"
          width="150"
        ></el-table-column>
        <el-table-column
          prop="selffunds"
          label="自筹资金(万元)"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="usedamount"
          label="已使用资金(万元)"
          align="center"
          width="130"
        ></el-table-column>
        <el-table-column
          prop="paidamount"
          label="已支付资金(万元)"
          align="center"
          width="130"
        ></el-table-column>
        <el-table-column prop="aid" label="申报文件(附件)" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.aid"
              @click="lookFile(scope.row.aidFiles, scope.row.sourceType)"
              src="../../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column prop="mtnPlan" label="计划文件(附件)" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.mtnPlan"
              @click="lookFile(scope.row.mtnPlanFiles, scope.row.sourceType)"
              src="../../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'fund:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delCard(scope.row.id)"
                v-permission="'fund:del'"
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
      <new-fund
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></new-fund>
    </el-dialog>

    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file
        v-if="fileDialog"
        :url="url"
        @closeLookDialog="closeEditDialog"
      ></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { exportMtFund } from "@/utils/export";
import newFund from "@/views/projectUnitDetail/repair/fund/components/newFund";
import { annulMaintenance } from "@/api/projectUnitDetail";

export default {
  name: "Index",
  props: {},
  components: {
    newFund,
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  data() {
    return {
      name: "",
      scale: "",
      scaleOptions: [
        { label: "2022", value: "" },
        { label: "2021", value: 1 },
        { label: "2020", value: 2 },
        { label: "2019", value: 3 },
      ],
      tableData: [],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      url: "",
      fileDialog: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await annulMaintenance.fundList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        pid: this.projectInfo.prcd,
        approvalUnit: this.name,
        year: this.scale,
      });
      if (res.code == 0) {
        console.log(res);
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    delCard(id) {
      this.$confirm("此操作会删除该条资金相关所有信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        annulMaintenance.delFund({ id: id }).then((res) => {
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
    editTable(title, id) {
      this.id = id;
      this.title = title;
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    exportTable() {
      let obj = {
        name: this.name,
        type: this.type,
        pid: this.projectInfo.prcd,
      };
      exportMtFund(obj);
    },
    handleSelectionChange(val) {
      console.log(val, "val");
      this.checkItems = val;
      this.num = val.length;
    },
    closeEditDialog(num) {
      if (num) {
        this.getList();
      }
      this.editDialog = false;
      this.showEditDialog = false;
    },
    lookFile(files, sourceType) {
      if (sourceType == 1) {
        window.open(files[0].fileUrl);
      } else {
        this.url = files[0].fileUrl;
        this.fileDialog = true;
      }
    },
  },
};
</script>

<style scoped lang="scss">
.wrap {
  @include contentBox();
  height: 100%;
  .look {
    cursor: pointer;
  }
}
</style>
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>生物防治</p>
    </div>

    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input
        placeholder="请输入"
        prefix-icon="el-icon-search"
        v-model="name"
        style="width: 180px; margin-right: 15px"
        clearable
        @change="getList"
      />
      <span style="font-size: 14px; margin-left: 15px">防治类型：</span>
      <el-select
        v-model="type"
        placeholder="请选择"
        @change="getList()"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in typeOptions"
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
        v-permission="'control:add'"
        style="margin-right: 24px"
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
          prop="materialDate"
          label="日期"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="managementUnit"
          label="工程管理单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="client"
          label="委托单位"
          align="center"
        ></el-table-column>
        <el-table-column prop="ptType" label="防治类型" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.ptType === 1">白蚁防治</span>
            <span v-if="scope.row.ptType === 2">外来生物防治</span>
          </template>
        </el-table-column>
        <el-table-column prop="contract" label="合同" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.contract"
              @click="lookFile(scope.row.contractFiles)"
              src="../../../../src/assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column prop="resultsReport" label="成果报告" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.resultsReport"
              @click="lookFile(scope.row.resultsReportFiles)"
              src="../../../../src/assets/images/c_att.png"
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
                v-permission="'control:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delCard(scope.row.id)"
                v-permission="'control:del'"
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
      <new-control-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></new-control-form>
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
import newControlForm from "@/views/projectUnitDetail/control/newControlForm";
import LookFile from "@/components/LookFile";
import { biologicalControl } from "@/api/projectUnitDetail";
export default {
  name: "Index",
  props: {},
  components: {
    newControlForm,
    LookFile,
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  data() {
    return {
      name: "",
      id: "",
      tableData: [],
      type: "",
      typeOptions: [
        { label: "白蚁防治", value: "" },
        { label: "外来生物防治", value: 1 },
      ],
      url: "",
      title: "新增",

      editDialog: false,
      showEditDialog: false,
      fileDialog: false,

      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await biologicalControl.getBiologicalControlList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        prcd: this.projectInfo.prcd,
        managementUnit: this.name,
        ptType: this.type,
      });
      if (res.code == 0) {
        console.log(res);
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    lookFile(file) {
      this.url = file[0].fileUrl;
      this.fileDialog = true;
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
    closeEditDialog(num) {
      if (num) {
        this.getList();
      }
      this.editDialog = false;
      this.showEditDialog = false;
      /*this.projectDialog = false
      this.showProjectDialog = false*/
    },
    delCard(id) {
      this.$confirm("此操作会删除该条相关所有信息，是否继续", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        biologicalControl.delBiologicalControl({ id: id }).then((res) => {
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
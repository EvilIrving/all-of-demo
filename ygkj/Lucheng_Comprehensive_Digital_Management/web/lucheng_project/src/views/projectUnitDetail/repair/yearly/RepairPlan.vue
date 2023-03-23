<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>年度维修计划</p>
    </div>
    <div class="info_option">
      <!-- <span style="font-size: 14px; margin-left: 15px">名称：</span>
        <el-input
            placeholder="请输入"
            prefix-icon="el-icon-search"
            @change="getList"
            v-model="name"
            style="width: 180px;
            margin-right: 15px"
            clearable />
        <span style="font-size: 14px; margin-left: 15px">年份：</span>
        <el-select  v-model="scale" placeholder="请选择" clearable style="width: 180px; margin: 0 20px 0 15px">
          <el-option v-for="item in scaleOptions" :key="item.label" :label="item.label" :value="item.label">
          </el-option>
        </el-select> -->
      <!-- <el-button  type="primary" class="right" @click="editTable('新增', '')" style="margin-right: 24px">新增</el-button> -->
      <el-button type="primary" @click="exportTable()" v-permission="'repairPlan:export'">导出</el-button>
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
          prop="annual"
          label="年份"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="comment"
          label="年底计划说明"
          align="center"
        ></el-table-column>
        <el-table-column prop="file" label="培养计划文件" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.file"
              @click="lookFile(scope.row.file)"
              src="../../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('编辑', scope.row.id)"
              >编辑</span
              >
              <span class="deal_btn deal_btn_del" @click="delCard(scope.row.id)"
              >删除</span
              >
            </div>
          </template>
        </el-table-column> -->
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
      <add-new-plan
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></add-new-plan>
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
import { exportMtDmPlan } from "@/utils/export";
import { annulMaintenance, getFileUrlApi } from "@/api/projectUnitDetail";
import LookFile from "@/components/LookFile";
import addNewPlan from "@/views/projectUnitDetail/repair/yearly/components/addNewPlan";
export default {
  name: "YearlyRepair",
  props: {},
  components: {
    addNewPlan,
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
      url: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      fileDialog: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async lookFile(file) {
      try {
        let res = await getFileUrlApi({
          pid: this.projectInfo.prcd,
          fileId: file,
        });
        console.log(res, "sdasdasdasdasd");
        if (res.code == 0) {
          this.url = res.data;
          this.fileDialog = true;
        } else {
          this.$message.error(res.message)
        }
      } catch {
        this.$message.error('服务请求失败，请稍候重试~')
      }
    },
    async getList() {
      let res = await annulMaintenance.annulList({
        pid: this.projectInfo.prcd,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name,
        annual: this.scale,
      });
      if (res.code == 0) {
        console.log(res);
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
        //console.log(this.tableData)
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
    delCard(id) {
      this.$confirm("此操作会删除该计划相关所有信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        annulMaintenance.delDmPlan({ id: id }).then((res) => {
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
        name: this.name,
        type: this.type,

        prcd: this.projectInfo.prcd,
      };
      console.log(obj);
      exportMtDmPlan(obj);
    },

    closeEditDialog(num) {
      if (num) {
        this.getList();
      }
      this.editDialog = false;
      this.showEditDialog = false;
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
  .info_option {
    width: 100%;
    padding: 25px 16px 16px 16px;
    box-sizing: border-box;
    text-align: right;
  }
}
</style>
<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-08-16 16:48:42
 * @Description: 
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\appointAcceptanceStandard\acceptanceStandard\AcceptanceStandard.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>验收标准</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">物业公司：</span>
        <el-select
          style="width: 240px; margin: 0 20px 0 15px"
          v-model="deptId"
          placeholder="请选择"
          @change="loadPageOverhaul"
          clearable
        >
          <el-option
            :label="item.companyName"
            :value="item.id"
            v-for="item in unitList"
            :key="item.id"
          >
          </el-option>
        </el-select>
        <span style="font-size: 14px">验收事项：</span>
        <el-input
          placeholder="请输入"
          v-model="dispatchName"
          @change="loadPageOverhaul"
          clearable
          style="width: 240px; margin: 0 20px 0 15px"
        />
      </div>
      <div class="search_right">
        <el-button
          type="primary"
          v-permission="'acceptanceStandard:add'"
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
          prop="items"
          label="验收事项"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="requirement"
          label="验收标准"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="scoreCriteria"
          label="赋分原则"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="standardScore"
          label="标准分"
          align="center"
          width="100"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="180">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('查看', scope.row.id)"
                v-permission="'acceptanceStandard:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'acceptanceStandard:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="removeTable(scope.row.id)"
                v-permission="'acceptanceStandard:del'"
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
        @pagination="loadPageOverhaul"
      />
    </div>
    <el-dialog
      :title="title"
      width="900px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <acceptance-standard-form
        @close="closeDialog"
        :id="id"
        :title="title"
        v-if="visibleDialog"
      />
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file v-if="fileDialog" :url="url"></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { contractOperate, propertyUnitApi } from "@/api/propertyManage.js";
import AcceptanceStandardForm from "./AcceptanceStandardForm";
import LookFile from "@/components/LookFile.vue";
export default {
  data() {
    return {
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      title: "新增",
      visibleDialog: false,
      id: "",
      options: [],
      fileDialog: false,
      url: "",
      deptId: "",
      dispatchName: "",
      unitList: [],
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: { AcceptanceStandardForm, LookFile },
  mounted() {
    this.getList();
    this.loadPageOverhaul();
  },
  methods: {
    async getList() {
      let res = await propertyUnitApi.list({
        pageNum: 1,
        pageSize: 999999,
      });
      if (res.code == 0) {
        this.unitList = res.data.list;
      }
    },
    preview(url) {
      this.url = url;
      this.fileDialog = true;
    },
    handleClose() {
      this.visibleDialog = false;
    },
    closeDialog(data) {
      if (data) {
        this.pageNum = 1;
        this.loadPageOverhaul();
      }
      this.handleClose();
    },
    search() {
      this.pageNum = 1;
      this.loadPageOverhaul();
    },
    removeTable(id) {
      this.$confirm("此操作将删除该数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          contractOperate
            .delCriteria({
              id,
            })
            .then((res) => {
              if (res.code == 0) {
                this.$message.success("删除成功");
                this.loadPageOverhaul();
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
    async loadPageOverhaul() {
      let res = await contractOperate.loadPageCriteria({
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        items: this.dispatchName,
        deptId: this.deptId,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    editTable(title, id = "") {
      this.title = title;
      this.id = id;
      this.visibleDialog = true;
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
    justify-content: space-between;
    padding: 25px 16px 16px;
  }
}
</style>
<!--
 * @Date: 2022-04-24 13:49:24
 * @Author: 
 * @Description: 合同管理-考核管理-督察检查
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-06 15:54:00
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\assessmentMmanagement\SupervisedCheck.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>督察检查</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">工程名称</span>
      <el-input
        size="mini"
        placeholder="请输入"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="name"
        style="width: 180px; margin-right: 15px"
        clearable
      />
      <span style="font-size: 14px; margin-left: 15px">年份：</span>
      <el-select
        size="mini"
        v-model="year"
        placeholder="请选择"
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in yearOptions"
          :key="item.label"
          :label="item.label"
          :value="item.label"
        >
        </el-option>
      </el-select>
      <el-button
        size="mini"
        type="primary"
        class="right"
        @click="editTable('新增', '')"
        v-permission="'supervisedCheck:add'"
        >新增</el-button
      >
      <el-button
        size="mini"
        style="margin-right: 10px"
        class="right"
        @click="exportTable()"
        v-permission="'supervisedCheck:export'"
        >导出</el-button
      >
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
          prop="projectName"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="inspectionTime"
          label="检查时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="responsibleUnitName"
          label="责任单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="responsiblePeopleName"
          label="责任人"
          align="center"
        ></el-table-column>
        <el-table-column label="督办状态" align="center">
          <template slot-scope="scope">{{
            scope.row.supervisionStatus | supervisionStatus
          }}</template>
        </el-table-column>
        <el-table-column prop="inspector" label="隐患图片" align="center">
          <template slot-scope="scope">
            <img
              style="cursor: pointer"
              v-if="scope.row.problemsPicture"
              @click="lookPic(scope.row.problemsPictureList)"
              src="../../../../assets/images/c_img.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="problems"
          label="存在问题"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'supervisedCheck:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delTable(scope.row.id)"
                v-permission="'supervisedCheck:del'"
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
    <el-dialog :title="title" :visible.sync="showEditDialog" width="65%">
      <supervised-check-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></supervised-check-form>
    </el-dialog>
    <el-dialog title="图片预览" :visible.sync="pictureDialog">
      <div
        v-for="(item, index) in picList"
        :key="index"
        @click="lookFile(item.fileUrl)"
        style="margin: 5px"
        class="lookPic"
      >
        <span style="font-size: 16px">{{ item.fileName }}</span>
      </div>
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
import { supervisedCheck } from "@/api/propertyManage";
import SupervisedCheckForm from "./SupervisedCheckForm.vue";
import { exportInspection } from "@/utils/export.js";
export default {
  name: "SupervisedCheck",
  props: {},
  components: {
    SupervisedCheckForm,
  },
  filters: {
    supervisionStatus: (value) => {
      switch (value) {
        case "1":
          return "未督促";
        case "2":
          return "已督促";
        default:
          return "-";
      }
    },
  },
  data() {
    return {
      name: "",
      year: "",
      yearOptions: [
        { label: 2022, value: "2022" },
        { label: 2021, value: "2021" },
        { label: 2020, value: "2020" },
        { label: 2019, value: "2019" },
        { label: 2018, value: "2018" },
      ],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      fileDialog: false,
      pictureDialog: false,
      picList: [],
      url: "",
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await supervisedCheck.list({
        projectName: this.name,
        year: this.year,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
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
    delTable(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        supervisedCheck
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
    exportTable() {
      let obj = {
        name: this.name,
        year: this.year,
      };
      exportInspection(obj);
    },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
    lookPic(images) {
      this.pictureDialog = true;
      this.picList = images;
    },
    lookFile(url) {
      this.url = url;
      this.fileDialog = true;
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .lookPic {
    border: 1px solid #276af0;
    border-radius: 4px;
    line-height: 20px;
    padding: 5px 10px;
    cursor: pointer;
  }
}
</style>
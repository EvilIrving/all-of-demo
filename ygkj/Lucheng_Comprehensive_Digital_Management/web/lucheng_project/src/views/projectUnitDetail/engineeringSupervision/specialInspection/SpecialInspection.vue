<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-26 16:07:30
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringSupervision\specialInspection\SpecialInspection.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>特别检查</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">时间：</span>
        <el-date-picker
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="date"
          value-format="yyyy"
          type="year"
          placeholder="请选择"
          clearable
          @change="getSpecialCheckList"
        >
        </el-date-picker>
      </div>
      <div class="search_right">
        <el-button
          type="primary"
          v-permission="'specialInspection:export'"
          @click="exportsData"
          >导出</el-button
        >
        <!-- <el-button type="primary" @click="editTable('新增')">新增</el-button> -->
      </div>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="selection" width="50"> </el-table-column>
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="checkTm"
          label="检查时间"
          align="center"
        ></el-table-column>
        <el-table-column prop="uploaderName" label="检查人员" align="center">
          <template slot-scope="scope">
            <span>{{
              scope.row.uploaderName ? scope.row.uploaderName : "-"
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="responUnit" label="组织单位" align="center">
        </el-table-column>
        <el-table-column
          prop="checkContent"
          label="存在问题（编辑）"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="problemType" label="问题类型" align="center">
        </el-table-column>
        <!-- <el-table-column prop="domType" label="检查照片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.checkPic"
              @click="lookFile(scope.row.checkPic)"
              src="../../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column> -->
        <el-table-column label="检查报告（记录表）" width="240" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.aid"
              @click="lookFile(scope.row.aidFileList)"
              src="../../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="160">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="look('查看', scope.row.id)"
                v-permission="'specialInspection:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'specialInspection:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                v-permission="'specialInspection:del'"
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
        @pagination="getSpecialCheckList"
      />
    </div>
    <el-dialog
      :title="title"
      width="960px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <special-inspection-add-from
        @close="closeDialog"
        :id="id"
        v-if="visibleDialog"
      />
    </el-dialog>
    <el-dialog
      :title="lookTitle"
      :visible.sync="showProjectDialog"
      width="960px"
    >
      <special-inspection-detail-from
        v-if="projectDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></special-inspection-detail-from>
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
import { specialInspectionApi } from "@/api/projectUnitDetail.js";
import { exportSpecialCheck } from "@/utils/export.js";
import specialInspectionAddFrom from "./components/specialInspectionAddFrom.vue";
import specialInspectionDetailFrom from "./components/specialInspectionDetailFrom.vue";
import LookFile from "@/components/LookFile";

export default {
  data() {
    return {
      dispatchType: "",
      date: "",
      dispatchName: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      visibleDialog: false,
      id: "",
      title: "新增",
      lookTitle: "查看",
      projectDialog: false,
      showProjectDialog: false,
      fileDialog: false,
      pictureDialog: false,
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: {
    specialInspectionAddFrom,
    specialInspectionDetailFrom,
    LookFile,
  },
  mounted() {
    this.getSpecialCheckList();
  },
  methods: {
    async getSpecialCheckList() {
      await specialInspectionApi
        .specialCheckList({
          date: this.date,
          pid: this.projectDetail.prcd,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
        })
        .then((res) => {
          if (res.code === 0) {
            this.tableData = res.data.list;
            this.pageTotal = res.data.totalRows;
          }
        });
    },
    lookFile(file) {
      this.url = file[0].fileUrl;
      this.fileDialog = true;
    },
    handleClose() {
      this.visibleDialog = false;
    },
    closeDialog() {
      this.visibleDialog = false;
      this.getSpecialCheckList();
    },
    closeEditDialog() {
      this.showProjectDialog = false;
      this.projectDialog = false;
    },
    editTable(type, id) {
      // console.log(id);
      this.id = id;
      this.title = type;
      this.visibleDialog = true;
    },
    look(title, id) {
      this.id = id;
      this.lookTitle = title;
      //console.log(id,title)
      this.showProjectDialog = true;
      this.projectDialog = false;
      this.$nextTick(() => {
        this.projectDialog = true;
      });
    },
    del(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        specialInspectionApi
          .delete({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getSpecialCheckList();
            } else {
              this.$message({
                type: "error",
                message: "删除失败！",
              });
            }
          });
      });
    },
    exportsData() {
      let obj = {
        pid: this.projectDetail.prcd,
        date: this.date,
      };
      exportSpecialCheck(obj);
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
}
</style>
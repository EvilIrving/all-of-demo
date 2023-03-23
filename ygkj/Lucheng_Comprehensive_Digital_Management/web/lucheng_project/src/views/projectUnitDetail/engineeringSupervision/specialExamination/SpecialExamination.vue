<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-26 16:07:44
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringSupervision\specialExamination\SpecialExamination.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>专项检查</p>
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
          @change="getMpSpList"
        >
        </el-date-picker>
      </div>
      <div class="search_right">
        <el-button
          type="primary"
          v-permission="'specialExamination:export'"
          @click="exportsData"
          >导出</el-button
        >
        <el-button
          type="primary"
          v-permission="'specialExamination:add'"
          @click="editTable('新增')"
          >新增</el-button
        >
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
          prop="tm"
          label="检查时间"
          align="center"
        ></el-table-column>
        <el-table-column prop="checkUser" label="检查人员" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.checkUser ? scope.row.checkUser : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unitName" label="组织单位" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.unitName ? scope.row.unitName : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="problemDesc"
          label="存在问题（编辑）"
          align="center"
        >
        </el-table-column>
        <el-table-column prop="problemType" label="问题类型" align="center">
        </el-table-column>
        <el-table-column prop="checkPic" label="检查照片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.checkPic"
              @click="lookPic(scope.row.checkPicList)"
              src="../../../../assets/images/c_img.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column label="检查报告（记录表）" width="240" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.fileIds"
              @click="lookFile(scope.row.fileList)"
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
                v-permission="'specialExamination:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'specialExamination:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                v-permission="'specialExamination:del'"
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
        @pagination="getMpSpList"
      />
    </div>
    <el-dialog
      :title="title"
      width="960px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <special-examination-add-from
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
      <special-examination-detail-from
        v-if="projectDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></special-examination-detail-from>
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file
        v-if="fileDialog"
        :url="url"
        @closeLookDialog="closeEditDialog"
      ></look-file>
    </el-dialog>
    <el-dialog title="图片预览" :visible.sync="pictureDialog">
      <div
        v-for="(item, index) in picList"
        :key="index"
        style="margin: 5px"
        class="lookPic"
      >
        <!-- <span
            style="font-size: 16px"
            @click="lookFile(item.fileUrl)"
        >{{item.fileName}}</span> -->
        <el-image
          style="width: 100px; height: 100px"
          :src="item.fileUrl"
        ></el-image>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { specialExaminationApi } from "@/api/projectUnitDetail.js";
import { mpSpExport } from "@/utils/export.js";
import specialExaminationAddFrom from "./components/specialExaminationAddFrom.vue";
import specialExaminationDetailFrom from "./components/specialExaminationDetailFrom.vue";
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
      picList: [],
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: {
    specialExaminationAddFrom,
    specialExaminationDetailFrom,
    LookFile,
  },
  mounted() {
    this.getMpSpList();
  },
  methods: {
    async getMpSpList() {
      await specialExaminationApi
        .mpSpList({
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
    lookPic(images) {
      this.pictureDialog = true;
      this.picList = images;
      console.log(this.picList);
    },
    handleClose() {
      this.visibleDialog = false;
      this.sluiceDialog = false;
      this.tunnelSpillwayDialog = false;
    },
    closeDialog() {
      this.visibleDialog = false;
      this.getMpSpList();
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
        specialExaminationApi
          .delete({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getMpSpList();
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
      mpSpExport(obj);
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
  .lookPic {
    display: inline-block;
  }
}
</style>
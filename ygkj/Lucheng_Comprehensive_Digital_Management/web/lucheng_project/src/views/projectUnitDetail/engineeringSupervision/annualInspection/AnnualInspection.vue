<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-01 15:06:43
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringSupervision\annualInspection\AnnualInspection.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>年度检查</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <!-- <span style="font-size: 14px">名称：</span>
        <el-input
          placeholder="请输入"
          v-model="dispatchName"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        /> -->
        <span style="font-size: 14px">时间：</span>
        <el-date-picker
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="date"
          value-format="yyyy"
          type="year"
          placeholder="请选择"
          clearable
          @change="getBhLgMpAcList"
        >
        </el-date-picker>
      </div>
      <div class="search_right">
        <el-button
          type="primary"
          v-permission="'annualInspection:export'"
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
          prop="tm"
          label="检查时间"
          align="center"
        ></el-table-column>
        <el-table-column prop="checkUid" label="带队检查人" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.checkUid ? scope.row.checkUid : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="joinUser" label="参加人员" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.joinUser ? scope.row.joinUser : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="hidCount" label="隐患数量" align="center">
        </el-table-column>
        <el-table-column prop="problemType" label="问题类型" align="center">
        </el-table-column>
        <el-table-column prop="checkPic" label="检查照片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.checkPic"
              @click="lookPic(scope.row.checkPicList, scope.row.sourceType)"
              src="../../../../assets/images/c_img.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column label="检查报告" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.aid"
              @click="lookFile(scope.row.aidFileList, scope.row.sourceType)"
              src="../../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column
          label="年度检查工程运行管理检查记录表"
          width="240"
          align="center"
        >
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.runActionRecord"
              @click="
                lookFile(scope.row.runActionRecordFiles, scope.row.sourceType)
              "
              src="../../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="sOrderCode"
          label="汛前及年度检查工程外观检查记录表"
          width="250"
          align="center"
        >
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.projCheckRecord"
              @click="
                lookFile(scope.row.runActionRecordFiles, scope.row.sourceType)
              "
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
                v-permission="'annualInspection:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'annualInspection:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                v-permission="'annualInspection:del'"
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
        @pagination="getBhLgMpAcList"
      />
    </div>
    <el-dialog
      :title="title"
      width="960px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <annual-inspection-add-from
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
      <annual-inspection-detail-from
        v-if="projectDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></annual-inspection-detail-from>
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
        v-for="item in picList"
        :key="item.id"
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
import { annualInspectionApi } from "@/api/projectUnitDetail.js";
import { exportBhLgMpAc } from "@/utils/export.js";
import annualInspectionAddFrom from "./components/annualInspectionAddFrom.vue";
import annualInspectionDetailFrom from "./components/annualInspectionDetailFrom.vue";
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
    annualInspectionAddFrom,
    annualInspectionDetailFrom,
    LookFile,
  },
  mounted() {
    this.getBhLgMpAcList();
  },
  methods: {
    async getBhLgMpAcList() {
      await annualInspectionApi
        .bhLgMpAcList({
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
    lookFile(file, type) {
      if (type == 1) {
        window.open(file[0].fileUrl);
      } else {
        this.url = file[0].fileUrl;
        this.fileDialog = true;
      }
    },
    lookPic(images) {
      this.pictureDialog = true;
      this.picList = images;
      console.log(this.picList);
    },
    handleClose() {
      this.visibleDialog = false;
    },
    closeDialog() {
      this.visibleDialog = false;
      this.getBhLgMpAcList();
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
        annualInspectionApi
          .delete({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getBhLgMpAcList();
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
      exportBhLgMpAc(obj);
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
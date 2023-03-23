<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>日常任务创建</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <!-- <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input placeholder="请输入" prefix-icon="el-icon-search"  @change="getList" v-model="name" style="width: 180px; margin-right: 15px" clearable /> -->
      <span style="font-size: 14px; margin-left: 15px">年份：</span>
      <el-select
        v-model="year"
        placeholder="请选择"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
        @change="getList"
      >
        <el-option
          v-for="item in yearOptions"
          :key="item.label"
          :label="item.label"
          :value="item.label"
        >
        </el-option>
      </el-select>
      <span style="font-size: 14px; margin-left: 15px">问题类型：</span>
      <el-select
        v-model="type"
        placeholder="请选择"
        @change="getList"
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
        v-permission="'taskCreation:add'"
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
          prop="projectType"
          label="工程类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="projectName"
          label="工程名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="problemType"
          label="问题类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="tm"
          label="时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="taskDescribe"
          label="任务简述"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="hiddenDanger"
          label="具体隐患"
          align="center"
        ></el-table-column>
        <el-table-column prop="imgIds" label="隐患图片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.imgIds"
              @click="lookPic(scope.row.images)"
              src="../../../../assets/images/c_img.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="processWay"
          label="处理方式"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="planTm"
          label="计划完成时间"
          align="center"
          width="122px"
        ></el-table-column>
        <el-table-column
          prop="problemSource"
          label="问题来源"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="implementUnit"
          label="实施单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="implementUser"
          label="实施人员"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="processWay"
          label="处理方式"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="instructions "
          label="指导意见"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'taskCreation:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delCard(scope.row.id)"
                v-permission="'taskCreation:del'"
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
      <new-daily-task
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></new-daily-task>
    </el-dialog>

    <el-dialog title="图片预览" :visible.sync="pictureDialog">
      <div
        v-for="(item, index) in picList"
        :key="index"
        style="margin: 5px"
        class="lookPic"
      >
        <span style="font-size: 16px" @click="lookFile(item.fileUrl)">{{
          item.fileName
        }}</span>
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
import newDailyTask from "@/views/projectUnitDetail/repair/yearly/components/newDailyTask";
import { annulMaintenance } from "@/api/projectUnitDetail";
import LookFile from "@/components/LookFile";

export default {
  name: "TaskCreation",
  props: {},
  components: {
    newDailyTask,
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
      year: "",
      yearOptions: [
        { label: "2022", value: "" },
        { label: "2021", value: 1 },
        { label: "2020", value: 2 },
        { label: "2019", value: 3 },
      ],
      type: "",
      typeOptions: [
        { label: "一般问题", value: "" },
        { label: "较重问题", value: 1 },
        { label: "严重问题", value: 2 },
      ],
      tableData: [],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,

      pictureDialog: false,
      picList: [],

      fileDialog: false,
      url: "",
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await annulMaintenance.DailyTask({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        prcd: this.projectInfo.prcd,
        name: this.name,
        year: this.year,
        problemType: this.type,
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
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    delCard(id) {
      this.$confirm("此操作会删除该日常任务相关所有信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        annulMaintenance.delDailyTask({ id: id }).then((res) => {
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
    closeEditDialog(num) {
      if (num) {
        this.getList();
      }
      this.editDialog = false;
      this.showEditDialog = false;
      /*this.projectDialog = false
      this.showProjectDialog = false*/
    },
    lookPic(images) {
      this.pictureDialog = true;
      this.picList = images;
      console.log(this.picList);
    },
    lookFile(url) {
      this.url = url;
      this.fileDialog = true;
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
  .lookPic {
    span:hover {
      color: #3ebeb6;
    }
  }
}
</style>
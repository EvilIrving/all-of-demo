<!--
 * @Date: 2022-04-25 10:24:11
 * @Author: 
 * @Description: 物业管理事项-人员培训
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-01 15:08:43
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\stafftraining\StaffTraining.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>人员培训</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input
        size="mini"
        placeholder="请输入"
        prefix-icon="el-icon-search"
        v-model="name"
        style="width: 180px; margin-right: 15px"
        clearable
        @change="getList"
      />
      <!-- <span style="font-size: 14px; margin-left: 15px">年份：</span>
      <el-select  v-model="scale" placeholder="请选择"  clearable style="width: 180px; margin: 0 20px 0 15px">
        <el-option v-for="item in scaleOptions" :key="item.label" :label="item.label" :value="item.label">
        </el-option>
      </el-select> -->
      <el-button
        size="mini"
        type="primary"
        class="right"
        @click="editTable('新增', '')"
        v-permission="'staffTraining:add'"
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
        <!-- <el-table-column type="selection" width="55" align="center">
        </el-table-column> -->
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="person"
          label="培训人员"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="time"
          label="培训时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="fund"
          label="培训经费(元)"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="execStatus"
          label="执行情况"
          align="center"
        ></el-table-column>
        <el-table-column prop="aid" label="培训证明" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.files">
              <div
                v-for="(item, index) in scope.row.files"
                :key="index"
                @click="preview(item, scope.row.sourceType)"
              >
                <i
                  class="el-icon-paperclip"
                  style="font-size: 20px; color: #409eff; cursor: pointer"
                ></i>
              </div>
            </div>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'staffTraining:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delCard(scope.row.id)"
                v-permission="'staffTraining:del'"
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
      <staff-training-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></staff-training-form>
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
import { staffTraining } from "@/api/propertyManage";
import StaffTrainingForm from "./StaffTrainingForm.vue";

export default {
  name: "staffTraining",
  props: {},
  components: {
    StaffTrainingForm,
  },
  data() {
    return {
      name: "",
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
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await staffTraining.list({
        pjcd: this.projectDetail.prcd,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        person: this.name,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    delCard(id) {
      this.$confirm("此操作会删除该条信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        staffTraining.del({ id: id }).then((res) => {
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
      this.showProjectDialog = false
      this.peopleDialog = false
      this.showPeopleDialog = false*/
    },
    preview(files, type) {
      if (type == 1) {
        window.open(files.fileUrl);
      } else {
        this.url = files.fileUrl;
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
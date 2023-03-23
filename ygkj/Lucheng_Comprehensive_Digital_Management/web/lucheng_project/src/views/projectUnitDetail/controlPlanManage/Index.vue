<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>控运计划管理</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px">年份：</span>
      <el-date-picker
        style="width: 180px; margin: 0 20px 0 15px"
        v-model="dispatchYear"
        value-format="yyyy"
        type="year"
        placeholder="请选择"
        clearable
        @change="searchList"
      >
      </el-date-picker>

      <el-button type="primary" class="right" @click="editTable('新增', '')" v-permission="'controlPlanManage:add'"
        >新增</el-button
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
          prop="year"
          label="年份"
          align="center"
        ></el-table-column>
        <el-table-column prop="file" label="控运计划文件" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.fileIds"
              @click="lookPic(scope.row.fileList)"
              src="../../../assets/images/c_att.png"
              alt=""
            />
          </template>
        </el-table-column>

        <el-table-column align="center" label="操作" width="220">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'controlPlanManage:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delTable(scope.row.id)"
                v-permission="'controlPlanManage:del'"
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
    <el-dialog :title="title" :visible.sync="showEditDialog" width="600px">
      <index-from v-if="showEditDialog" :id="id" @closeEditDialog="closeEditDialog"></index-from>
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
import { bhControlPlan } from "@/api/propertyManage.js";
import IndexFrom from "./IndexFrom.vue";
export default {
  name: "Index",
  props: {},
  components: { IndexFrom },
  data() {
    return {
      editDialog: "",
      id: "",
      showEditDialog: false,
      title: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      dispatchYear: "",
      fileDialog: false,
      pictureDialog: false,
      picList: [],
      url: "",
    };
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  mounted() {
    this.getList();
  },
  methods: {
    editTable(title, id = "") {
      this.title = title;
      this.id = id;
      this.showEditDialog = true;
    },
    closeEditDialog(type) {
      if (type) {
        this.getList();
      }
      this.showEditDialog = false;
    },
    searchList() {
      this.pageNum = 1;
      this.getList();
    },
    async getList() {
      let res = await bhControlPlan.list({
        pid: this.projectInfo.prcd,
        year: this.dispatchYear,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
      });
      if (res.code == 0) {
        this.pageTotal = res.data.totalRows;
        this.tableData = res.data.list;
      }
    },
    delTable(id) {
      this.$confirm("此操作会删除该条控运计划，是否继续", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        bhControlPlan.del({ id: id }).then((res) => {
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
  .lookPic {
    border: 1px solid #276af0;
    border-radius: 4px;
    line-height: 20px;
    padding: 5px 10px;
    cursor: pointer;
  }
}
</style>
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>绿化保洁</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input
        placeholder="请输入"
        @change="getList"
        prefix-icon="el-icon-search"
        v-model="name"
        style="width: 180px; margin-right: 15px"
        clearable
      />
      <el-button
        type="primary"
        class="right"
        @click="editTable('新增', '')"
        style="margin-right: 24px"
        v-permission="'greenClean:add'"
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
          prop="implementer"
          label="实施人员"
          align="center"
        ></el-table-column>
        <el-table-column prop="startPhoto" label="实施前照片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.startPhoto"
              @click="lookPic(scope.row.startPhotoFiles)"
              src="../../../../src/assets/images/c_img.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column prop="endPhoto" label="实施后照片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.endPhoto"
              @click="lookPic(scope.row.endPhotoFiles)"
              src="../../../../src/assets/images/c_img.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="180">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="lookTable('查看', scope.row.id)"
                v-permission="'greenClean:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'greenClean:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delCard(scope.row.id)"
                v-permission="'greenClean:del'"
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

    <el-dialog :title="addTitle" :visible.sync="showEditDialog" width="960px">
      <new-clean-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></new-clean-form>
    </el-dialog>

    <el-dialog
      :title="lookTitle"
      :visible.sync="showProjectDialog"
      width="960px"
    >
      <detail-form
        v-if="projectDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></detail-form>
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
import newCleanForm from "@/views/projectUnitDetail/greenClean/newCleanForm";
import detailForm from "@/views/projectUnitDetail/greenClean/detailForm";
import { greenCleaning } from "@/api/projectUnitDetail";
export default {
  name: "Index",
  props: {},
  components: {
    newCleanForm,
    detailForm,
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  data() {
    return {
      id: "",
      tableData: ["1"],
      name: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      picList: [],
      url: "",
      addTitle: "新增",
      lookTitle: "查看",
      editDialog: false,
      showEditDialog: false,
      projectDialog: false,
      showProjectDialog: false,
      fileDialog: false,
      pictureDialog: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await greenCleaning.getGreenCleaningDetail({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        prcd: this.projectInfo.prcd,
        implementer: this.name,
      });
      if (res.code == 0) {
        console.log(res);
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    lookTable(title, id) {
      this.id = id;
      this.lookTitle = title;
      //console.log(id,title)
      this.showProjectDialog = true;
      this.projectDialog = false;
      this.$nextTick(() => {
        this.projectDialog = true;
      });
    },
    editTable(title, id) {
      this.id = id;
      this.addTitle = title;
      //console.log(id,title)
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    delCard(id) {
      this.$confirm("此操作会删除该条相关所有信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        greenCleaning.delGreenCleaning({ id: id }).then((res) => {
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
      this.projectDialog = false;
      this.showProjectDialog = false;
    },
    exportTable() {
      let obj = {
        name: this.name,
        type: this.type,

        prcd: this.projectInfo.prcd,
      };
      console.log(obj);
      //exportMtDmPlan(obj);
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

<style lang="scss" scoped>
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
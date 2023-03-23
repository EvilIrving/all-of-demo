<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>硬件设备维护</p>
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
        v-permission="'hardwareMt:add'"
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
          prop="materialDate"
          label="日期"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="equipmentName"
          label="设备名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="maintainUnit"
          label="维护单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="projectLeader"
          label="负责人"
          align="center"
        ></el-table-column>
        <el-table-column prop="beforePhoto" label="实施前图片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.beforePhoto"
              @click="lookPic(scope.row.beforePhotoFiles)"
              src="../../../../assets/images/c_img.png"
              alt=""
            />
          </template>
        </el-table-column>
        <el-table-column prop="afterPhoto" label="实施后图片" align="center">
          <template slot-scope="scope">
            <img
              class="look"
              v-if="scope.row.afterPhoto"
              @click="lookPic(scope.row.afterPhotoFiles)"
              src="../../../../assets/images/c_img.png"
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
                v-permission="'hardwareMt:look'"
                >查看</span
              >
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'hardwareMt:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delCard(scope.row.id)"
                v-permission="'hardwareMt:del'"
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
      <hardware-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></hardware-form>
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
import hardwareForm from "@/views/projectUnitDetail/support/hardwareMt/hardwareForm";
import detailForm from "@/views/projectUnitDetail/support/hardwareMt/detailForm";
import LookFile from "@/components/LookFile";
import { databasedM } from "@/api/projectUnitDetail";
export default {
  name: "Index",
  props: {},
  components: {
    hardwareForm,
    LookFile,
    detailForm,
  },
  computed: {
    projectInfo() {
      return this.$store.state.projectInfo;
    },
  },
  data() {
    return {
      tableData: ["1"],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      id: "",
      title: "新增",
      lookTitle: "查看",
      name: "",
      editDialog: false,
      showEditDialog: false,

      pictureDialog: false,
      picList: [],
      projectDialog: false,
      showProjectDialog: false,

      fileDialog: false,
      url: "",
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await databasedM.getHardwareMtList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        pid: this.projectInfo.prcd,
        equipmentName: this.name,
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
      this.title = title;
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    delCard(id) {
      this.$confirm("此操作会删除该条相关所有信息，是否继续", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        databasedM.delHardware({ id: id }).then((res) => {
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
    border: 1px solid #276af0;
    border-radius: 4px;
    line-height: 20px;
    padding: 5px 10px;
    cursor: pointer;
  }
}
</style>
<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-08-23 11:14:00
 * @Description: 
 * @FilePath: \lucheng_project\src\views\propertyManage\contractManagement\appointAcceptanceStandard\covenant\Covenant.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>物业合同</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <span style="font-size: 14px">查询：</span>
        <el-input
          placeholder="请输入"
          v-model="dispatchName"
          @change="search"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        />
        <span style="font-size: 14px">试运行时间：</span>
        <el-date-picker
          @change="search"
          v-model="dispatchYear"
          value-format="yyyy-MM-dd"
          clearable
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 240px; margin: 0 20px 0 15px"
        >
        </el-date-picker>
      </div>
      <div class="search_right">
        <el-button
          type="primary"
          v-permission="'covenant:add'"
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
          prop="contractName"
          label="物业合同名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="contractClientNm"
          label="委托方单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="contractUnitNm"
          label="物业管理单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="contractContent"
          label="物业委托管理内容"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="starttm"
          label="起始时间"
          align="center"
          width="180"
        >
          <!-- <template slot-scope="scope">
            {{ scope.row.serviceStartTime }} - {{ scope.row.serviceEndTime }}
          </template> -->
        </el-table-column>
        <el-table-column
          prop="endtm"
          label="终止时间"
          align="center"
          width="180"
        >
          <!-- <template slot-scope="scope">
            {{ scope.row.serviceStartTime }} - {{ scope.row.serviceEndTime }}
          </template> -->
        </el-table-column>
        <el-table-column
          prop="qualification"
          label="专项委托资质"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="funds"
          label="总合同经费(万元)"
          align="center"
        ></el-table-column>
        <el-table-column prop="file" label="合同文件" align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.contractFile">
              <div @click="preview(scope.row.contractFile)">
                <i
                  class="el-icon-link"
                  style="color: #409eff; cursor: pointer"
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
                v-if="scope.row.sourceType != 1"
                v-permission="'covenant:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="removeTable(scope.row.id)"
                v-permission="'covenant:del'"
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
      width="1100px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <covenant-form
        :options="options"
        @close="closeDialog"
        :id="id"
        v-if="visibleDialog"
      />
    </el-dialog>
    <el-dialog title="文件预览" :visible.sync="fileDialog" fullscreen>
      <look-file v-if="fileDialog" :url="url"></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { contractOperate } from "@/api/propertyManage.js";
import { loadSkPageApi } from "@/api/projectUnitDetail.js";
import CovenantForm from "./CovenantForm";
import LookFile from "@/components/LookFile.vue";
export default {
  data() {
    return {
      dispatchYear: "",
      dispatchName: "",
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
    };
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo;
    },
  },
  components: { CovenantForm, LookFile },
  mounted() {
    this.loadPageOverhaul();
    // this.loadSkPage("水库");
    // this.loadSkPage("水闸");
    // this.loadSkPage("堤防");
    // this.loadSkPage("海塘");
  },
  methods: {
    async loadSkPage(type) {
      let res = await loadSkPageApi({
        projectType: type,
        pageNum: 1,
        pageSize: 9999,
      });
      if (res.code == 0) {
        let obj = {
          value: type,
          label: type,
          children: [],
        };
        obj.children = res.data.rows.map((item) => {
          return {
            value: item.prcd,
            label: item.name,
            ...item,
          };
        });
        this.options.push(obj);
      }
    },
    preview(url) {
      this.url = url;
      if (this.url.indexOf("https://") == -1) {
        this.url = this.$config.skFilesHttps + this.url;
      }
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
            .delCovenant({
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
      let res = await contractOperate.loadPageCovenant({
        starttm: this.dispatchYear ? this.dispatchYear[0] : "",
        endtm: this.dispatchYear ? this.dispatchYear[1] : "",
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        key: this.dispatchName,
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
    @include flexJC(space-between);
    padding: 25px 16px 16px;
  }
}
</style>
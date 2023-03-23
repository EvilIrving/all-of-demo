<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>预测断面</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input
        placeholder="请输入"
        prefix-icon="el-icon-search"
        @change="getList"
        v-model="name"
        style="width: 180px; margin-right: 15px"
        clearable
      />
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
          prop="name"
          label="断面名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="tdz"
          label="潮位(m)"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="h1"
          label="H1%(m)"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="h2"
          label="H2%(m)"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="h13"
          label="H13%(m)"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="180">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                v-permission="'predictingSection:look'"
                class="deal_btn deal_btn_look"
                @click="lookTable('详情', scope.row.id)"
                >详情</span
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

    <el-dialog
      :title="lookTitle"
      :visible.sync="showProjectDialog"
      width="960px"
    >
      <section-detail
        v-if="projectDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></section-detail>
    </el-dialog>
  </div>
</template>

<script>
import sectionDetail from "@/views/seawallPrevent/stormWarning/predictingSection/sectionDetail";
import { stormSurgeWarningApi } from "@/api/seawallPrevent";
export default {
  name: "index",
  props: {},
  components: {
    sectionDetail,
  },
  data() {
    return {
      id: "",
      date: "",
      name: "",
      tableData: ["1"],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,

      lookTitle: "详情",
      showProjectDialog: false,
      projectDialog: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await stormSurgeWarningApi.forecastSectionList({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        name: this.name,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
      console.log(res);
    },
    closeEditDialog() {
      this.projectDialog = false;
      this.showProjectDialog = false;
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
    handleSelectionChange(val) {
      console.log(val, "val");
      this.checkItems = val;
      this.num = val.length;
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
}
</style>
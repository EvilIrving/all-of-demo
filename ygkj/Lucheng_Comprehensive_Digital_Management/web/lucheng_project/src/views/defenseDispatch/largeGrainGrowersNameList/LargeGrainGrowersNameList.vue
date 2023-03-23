<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-06 15:00:44
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\defenseDispatch\largeGrainGrowersNameList\LargeGrainGrowersNameList.vue
-->
<template>
  <div class="wrap">
    <div
      class="info_option"
      style="width: 100%; padding: 16px; box-sizing: border-box"
    >
      <el-input
        placeholder="请输入人名、街道、村"
        prefix-icon="el-icon-search"
        @change="getList"
        v-model="search"
        style="width: 240px; margin-right: 15px"
        clearable
      />
      <el-button
        type="primary"
        class="right"
        @click="editTable('新增', '')"
        v-permission="'largeGrainGrowersNameList:add'"
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
          prop="name"
          label="大户姓名"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="city"
          label="街道"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="village"
          label="村"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="联系电话"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'largeGrainGrowersNameList:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                v-permission="'largeGrainGrowersNameList:del'"
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
    <el-dialog :title="title" :visible.sync="showEditDialog" width="650px">
      <large-grain-growers-name-list-from
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></large-grain-growers-name-list-from>
    </el-dialog>
  </div>
</template>

<script>
import { LargeGrainGrowersNameListApi } from "@/api/defenseDispatch.js";
import LargeGrainGrowersNameListFrom from "./LargeGrainGrowersNameListFrom";
export default {
  name: "personLiable",
  props: {},
  components: {
    LargeGrainGrowersNameListFrom,
  },
  data() {
    return {
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      search: "",
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await LargeGrainGrowersNameListApi.growGrainList({
        key: this.search,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
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
    del(id) {
      this.$confirm("此操作会删除该条信息, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        LargeGrainGrowersNameListApi.delete({ id: id }).then((res) => {
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

    closeEditDialog() {
      this.getList();
      this.editDialog = false;
      this.showEditDialog = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .look {
    cursor: pointer;
  }
}
</style>

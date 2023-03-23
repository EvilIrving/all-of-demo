<!--
 * @Author: wanxin
 * @LastEditTime: 2022-07-06 15:01:23
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\defenseDispatch\defenseObject\DefenseObject.vue
-->
<template>
  <div class="wrap">
    <!-- <div class="item_box_title">
      <p>山洪灾害责任人表</p>
    </div> -->
    <div
      class="info_option"
      style="width: 100%; padding: 16px; box-sizing: border-box"
    >
      <el-input
        placeholder="请输入乡、行政村、自然村、防御对象、转移责任人"
        prefix-icon="el-icon-search"
        @change="getList"
        v-model="search"
        style="width: 300px; margin-right: 15px"
        clearable
      />
      <el-button
        type="primary"
        v-permission="'defenseObject:add'"
        class="right"
        @click="editTable('新增', '')"
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
          prop="town"
          label="乡（镇、街道）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="adminAdnm"
          label="行政村"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="naturalAdnm"
          label="自然村"
          align="center"
        ></el-table-column>
        <el-table-column prop="lev" label="风险区等级" align="center">
          <template slot-scope="scope">
            {{ scope.row.lev ? scope.row.lev : "-" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="defenObject"
          label="防御对象"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="type"
          label="类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="contact"
          label="联系人"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="contactTel"
          label="联系方式"
          align="center"
        ></el-table-column>
        <el-table-column prop="pcount" label="人口数量" align="center">
        </el-table-column>
        <el-table-column prop="tcharger" label="转移责任人" align="center">
        </el-table-column>
        <el-table-column
          prop="tchargertel"
          label="转移责任人联系方式"
          align="center"
        >
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'defenseObject:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="del(scope.row.id)"
                v-permission="'defenseObject:del'"
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
      <defense-object-from
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></defense-object-from>
    </el-dialog>
  </div>
</template>

<script>
import { defenseObjectApi } from "@/api/defenseDispatch.js";
import DefenseObjectFrom from "./DefenseObjectFrom.vue";
export default {
  name: "defenseObject",
  props: {},
  components: {
    DefenseObjectFrom,
  },
  data() {
    return {
      search: "",
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList() {
      let res = await defenseObjectApi.defenseObjectList({
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
        defenseObjectApi.delete({ id: id }).then((res) => {
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

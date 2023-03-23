<template>
  <div class="warp-page">
    <div class="warp-unit-list" v-if="userInfo.userType != 3">
      <unit-list @checkItem="checkItem" />
    </div>
    <div class="wrap" :class="{ 'wrap-type': userInfo.userType == 3 }">
      <div
        class="info_option"
        style="
          width: 100%;
          padding: 25px 16px 16px 16px;
          box-sizing: border-box;
        "
      >
        <span style="font-size: 14px; margin-left: 15px">工程名称：</span>
        <el-input
          size="mini"
          placeholder="请输入"
          prefix-icon="el-icon-search"
          @change="getList()"
          v-model="name"
          style="width: 180px; margin-right: 15px"
          clearable
        />
        <span style="font-size: 14px; margin-left: 15px">工程类型：</span>
        <el-select
          size="mini"
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
          size="mini"
          type="primary"
          class="right"
          @click="editTable('新增', '')"
          v-permission="'proPerviceItem:add'"
          >新增</el-button
        >
        <!-- <el-button style="margin-right: 10px;" class="right" @click="exportTable()">导出</el-button> -->
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
            prop="companyName"
            label="服务单位"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="projectName"
            label="建设单位"
            align="center"
          ></el-table-column>
          <!-- <el-table-column
          prop="projectType"
          label="工程类型"
          align="center"
        ></el-table-column> -->

          <el-table-column prop="serviceTime" label="服务时间" align="center">
            <template slot-scope="scope">
              <span
                >{{ scope.row.serviceStartTime }}-{{
                  scope.row.serviceEndTime
                }}</span
              >
            </template>
          </el-table-column>
          <el-table-column
            prop="projectItem"
            label="服务事项"
            align="center"
          ></el-table-column>
          <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('编辑', scope.row.id)"
                  v-permission="'proPerviceItem:edit'"
                  >编辑</span
                >
                <span
                  class="deal_btn deal_btn_del"
                  @click="delTable(scope.row.id)"
                  v-permission="'proPerviceItem:del'"
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
      <el-dialog :title="title" :visible.sync="showEditDialog" width="35%">
        <service-item-form
          v-if="editDialog"
          :id="id"
          :deptId='deptId'
          @closeEditDialog="closeEditDialog"
        ></service-item-form>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { operationUnit } from "@/api/propertyManage";
import ServiceItemForm from "./ServiceItemForm.vue";
import UnitList from "@/components/UnitList.vue";

// import { exportWareHouses } from "@/utils/export.js";
export default {
  name: "serviceItem",
  props: {},
  components: {
    ServiceItemForm,
    UnitList,
  },
  computed: {},
  watch: {},
  created() {},
  data() {
    return {
      name: "",
      type: "",
      typeOptions: [
        { label: "水库", value: "" },
        { label: "水闸", value: "" },
        { label: "海塘", value: "" },
      ],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      deptId: "",
      userInfo: {},
    };
  },
  mounted() {
    this.userInfo = this.$sessionData("get", "userInfo");
    if (this.userInfo.userType == 3) {
      this.deptId = this.userInfo.deptId;
      this.getList();
    }
  },
  methods: {
    checkItem(item) {
      this.deptId = item.id;
      this.getList();
    },
    async getList() {
      let res = await operationUnit.servicelist({
        projectName: this.name,
        projectType: this.type,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        deptId: this.deptId,
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
    delTable(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        operationUnit
          .servicedel({
            id: id,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getList();
            } else {
              this.$message({
                type: "error",
                message: "删除失败！",
              });
            }
          });
      });
    },
    // exportTable() {
    //   let obj = {
    //     name: this.name,
    //     type: this.type,
    //   };
    //   exportWareHouses(obj);
    // },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
  },
};
</script>

<style lang='scss' scoped>
.warp-page {
  display: flex;
  justify-content: space-between;
  width: 100%;
  height: 100%;
}
.warp-unit-list {
  @include contentBox();
  height: 100%;
  width: 255px;
}
.wrap {
  @include contentBox();
  height: 100%;
  width: calc(100% - 275px);
  .look {
    cursor: pointer;
  }
}
.wrap-type {
  width: 100%;
}
</style>
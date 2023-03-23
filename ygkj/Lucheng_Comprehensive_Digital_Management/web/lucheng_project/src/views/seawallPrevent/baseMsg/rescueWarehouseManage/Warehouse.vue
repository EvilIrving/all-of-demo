<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:42:35
 * @Description: 救援仓库
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\rescueWarehouseManage\Warehouse.vue
-->
<template>
  <div class="wrap">
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">名称：</span>
      <el-input
        placeholder="请输入"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="name"
        style="width: 180px; margin-right: 15px"
        clearable
      />
      <span style="font-size: 14px; margin-left: 15px">仓库类型：</span>
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
        v-permission="'rescueWarehouseManageWarehouse:add'"
        type="primary"
        class="right"
        @click="editTable('新增', '')"
        >新增</el-button
      >
      <el-button
        v-permission="'rescueWarehouseManageWarehouse:export'"
        style="margin-right: 10px"
        class="right"
        @click="exportTable()"
        >导出</el-button
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
          label="仓库名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="type"
          label="仓库类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="acreage"
          label="仓库面积"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="materialPrice"
          label="物资总价值"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="address"
          label="详细地址"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="chargePerson"
          label="联系人"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="chargePhone"
          label="联系方式"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                v-permission="'rescueWarehouseManageWarehouse:edit'"
                @click="editTable('编辑', scope.row.id)"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delTable(scope.row.id)"
                v-permission="'rescueWarehouseManageWarehouse:del'"
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
      <warehouse-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></warehouse-form>
    </el-dialog>
  </div>
</template>

<script>
import { rescueWarehouseApi } from "@/api/seawallPrevent";
import WarehouseForm from "./WarehouseForm";
import { exportWareHouses } from "@/utils/export.js";
export default {
  name: "Warehouse",
  props: {},
  components: {
    WarehouseForm,
  },
  data() {
    return {
      name: "",
      type: "",
      typeOptions: [
        { label: "省级仓库", value: "省级仓库" },
        { label: "市级仓库", value: "市级仓库" },
        { label: "县级仓库", value: "县级仓库" },
        { label: "省供销社", value: "省供销社" },
        { label: "社会厂家", value: "社会厂家" },
      ],
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
      let res = await rescueWarehouseApi.list({
        name: this.name,
        type: this.type,
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
    delTable(id) {
      this.$confirm("确认删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        rescueWarehouseApi
          .del({
            ids: id,
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
    exportTable() {
      let obj = {
        name: this.name,
        type: this.type,
      };
      exportWareHouses(obj);
    },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
  },
};
</script>

<style lang='scss' scoped>
.wrap {
}
</style>
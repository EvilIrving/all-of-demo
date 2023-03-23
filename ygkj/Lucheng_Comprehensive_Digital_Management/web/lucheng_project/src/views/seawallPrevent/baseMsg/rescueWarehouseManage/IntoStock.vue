<template>
  <div class="wrap">
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <span style="font-size: 14px; margin-left: 15px">仓库名称：</span>
      <el-select
        v-model="warehouseId"
        placeholder="请选择"
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in warehousesList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        >
        </el-option>
      </el-select>
      <span style="font-size: 14px; margin-left: 15px">物资名称：</span>
      <el-input
        v-model="name"
        placeholder="请输入"
        style="width: 180px; margin: 0 20px 0 15px"
        clearable
      >
        <i
          slot="suffix"
          class="el-input__icon el-icon-search"
          @click="getList"
          style="cursor: pointer"
        />
      </el-input>
      <el-button type="primary" class="right" v-permission="'rescueWarehouseManageIntoStock:add'" @click="editTable('新增', '')"
        >新增</el-button
      >
      <!--      <el-button style="margin-right: 10px;" class="right" @click="exportTable()">导出</el-button>-->
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column label="序号" width="60" align="center">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <!--          <template slot="header" slot-scope="scope">
                      <el-checkbox  @change="handleSelectionChange"/>
                    </template>
                    <template slot-scope="scope">
                      <el-checkbox v-model="scope.row.isSelected" @change="checkedItem(scope.row)">{{ scope.row.index }}</el-checkbox>
                    </template>-->

        <el-table-column
          prop="warehouseId"
          label="物资仓库名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="materialName"
          label="物资名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="type"
          label="入库类型"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="num"
          label="物资数量"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="tm"
          label="入库时间"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="amount"
          label="物资价值（元）"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="recorder"
          label="记录人"
          align="center"
        ></el-table-column>
        <el-table-column label="附件" align="center" width="80">
          <template slot-scope="scope">
            <i
              v-show="scope.row.fileList"
              style="color: #449eff; cursor: pointer"
              class="el-icon-paperclip"
              @click="openFile(scope.row.fileList)"
            />
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="80">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                v-permission="'rescueWarehouseManageIntoStock:look'"
                @click="editTable('查看', scope.row.id)"
                >查看</span
              >
              <!--              <span
                  class="deal_btn deal_btn_del"
                  @click="delTable(scope.row.id)"
              >删除</span>-->
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
    <el-dialog :title="title" :visible.sync="showEditDialog" width="50%">
      <into-stock-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></into-stock-form>
    </el-dialog>
  </div>
</template>

<script>
import { intoOutScrappedApi } from "@/api/seawallPrevent";
import IntoStockForm from "./IntoStockForm";
import { exportWareHouses } from "@/utils/export.js";

export default {
  name: "Warehouse",
  props: {},
  components: {
    IntoStockForm,
  },
  data() {
    return {
      name: "",
      warehouseId: "",
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
      tableData: [
        /*   {
          isSelected: false,
          index: 1,
          label1: 'label1',
          label2: 'label2',
          label3: 'label3',
          label4: 'label4',
          label5: 'label5',
          label6: 'label6'
        },*/
      ],
      inboundType: [
        { label: "采购入库", value: "1" },
        { label: "归还入库", value: "2" },
      ],
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      // 防汛仓库
      warehousesList: [],

      // 选中列
      checkAll: false,
      selectionItem: null,
      multipleSelection: [],
    };
  },
  mounted() {
    this.getWarehousesList();
    this.getList();
  },
  methods: {
    openFile(file) {
      window.open(file[0].fileUrl);
    },
    async getWarehousesList() {
      await intoOutScrappedApi.warehousesList().then((res) => {
        this.warehousesList = res.data.list;
      });
    },
    checkedItem(item) {
      // console.log(item)
      // this.multipleSelection.push(item)
    },
    handleSelectionChange(val) {
      // console.log(val)
      if (val) {
        this.multipleSelection = this.tableData;
        this.multipleSelection.forEach((value) => {
          value.isSelected = true;
        });
      } else {
        this.multipleSelection = this.tableData;
        this.multipleSelection.forEach((value) => {
          value.isSelected = false;
        });
      }
    },
    async getList() {
      let res = await intoOutScrappedApi.intoList({
        materialName: this.name,
        warehouseId: this.warehouseId,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;

        this.tableData.forEach((item) => {
          this.warehousesList.map((item1) => {
            if (item.warehouseId === item1.id) item.warehouseId = item1.name;
          });
          this.inboundType.map((item2) => {
            if (item.type === item2.value) item.type = item2.label;
          });
        });
        console.log(this.tableData);
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

/*.tb_wrapper ::v-deep .el-checkbox::after {
  content: "1";
  margin-left: 5px;
}
.tb_wrapper ::v-deep .el-table__header-wrapper .el-checkbox::after{
  content: "";
}*/
</style>

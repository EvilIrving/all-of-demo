<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:46:42
 * @Description: 测站极值管理
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\refugeManage\StationEVM.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>测站极值管理</p>
    </div>
    <div class="list_wrap">
      <div class="list_left">
        <div class="tree-list">
          <el-tree
            ref="popularTree"
            class="setting-tree-list"
            :data="deplList"
            node-key="id"
            @node-click="handleNodeClick"
            :current-node-key="currentLivingId"
            :default-checked-keys="['DD']"
            :props="defaultProps"
            default-expand-all
            :expand-on-click-node="false"
            :highlight-current="true"
          >
            <span class="custom-tree-node" slot-scope="{ node }">
              <div class="tree-box">
                <div class="tree-name" :title="node.label">
                  {{ node.label }}
                </div>
              </div>
            </span>
          </el-tree>
        </div>
      </div>
      <div class="list_right">
        <div
          class="info_option"
          style="
            width: 100%;
            padding: 25px 16px 16px 16px;
            box-sizing: border-box;
          "
        >
          <el-input
            size="mini"
            placeholder="请输入名称"
            prefix-icon="el-icon-search"
            @change="getList()"
            v-model="pointName"
            style="width: 240px; margin-right: 15px"
            clearable
          />
          <!-- <el-button type="primary" class="right" @click="editTable('新增')()" >新增</el-button>
      <el-button style="margin-right: 10px" class="right" @click="exportTable()">导出</el-button> -->
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
              prop="stName"
              label="测站名称"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="limitLevel"
              label="汛限水位"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="guaranteeLevel"
              label="保证水位"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="warningLevel"
              label="警戒水位"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="stLong"
              label="经度"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="stLat"
              label="纬度"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="dtmElev"
              label="高程"
              align="center"
            ></el-table-column>
            <el-table-column align="center" label="操作" width="150">
              <template slot-scope="scope">
                <div class="deal_box">
                  <span
                    class="deal_btn deal_btn_look"
                    v-permission="'StationEVM:edit'"
                    @click="editTable('编辑', scope.row.stCode)"
                    >编辑</span
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
      </div>
    </div>

    <el-dialog :title="title" :visible.sync="showEditDialog" width="60%">
      <StationEVMForm
        v-if="editDialog"
        :stCode="stCode"
        @closeEditDialog="closeEditDialog"
      ></StationEVMForm>
    </el-dialog>
  </div>
</template>

<script>
import { stController } from "@/api/seawallPrevent";
import StationEVMForm from "./StationEVMForm.vue";

export default {
  name: "StationEVM",
  props: {},
  components: {
    StationEVMForm,
  },
  data() {
    return {
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      pointName: "",
      id: "",
      title: "新增",
      editDialog: false,
      showEditDialog: false,
      currentLivingId: "DD",
      defaultProps: {
        children: "list",
        label: "deptname",
      },
      deplList: [
        {
          deptname: "水利工程",
          id: 1,
          list: [
            {
              deptname: "水库水位站",
              id: "RR",
              pid: "1",
            },
            {
              deptname: "水闸",
              id: "DD",
              pid: "1",
            },
            {
              deptname: "河道",
              id: "ZZ",
              pid: "1",
            },
            {
              deptname: "潮位站",
              id: "TT",
              pid: "1",
            },
            {
              deptname: "雨量站",
              id: "PP",
              pid: "1",
            },
            {
              deptname: "气象站",
              id: " MM",
              pid: "1",
            },
            {
              deptname: "位移",
              id: "SM",
              pid: "1",
            },
            {
              deptname: "流量站",
              id: "ZQ",
              pid: "1",
            },
          ],
        },
      ],
      proType: "DD",
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    handleNodeClick(row) {
      this.currentLivingId = row.id;
      this.proType = row.id;
      this.getList();
    },
    async getList() {
      let res = await stController.page({
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        stName: this.pointName,
        stType: this.proType,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    editTable(title, stCode) {
      this.stCode = stCode;
      this.title = title;
      this.showEditDialog = true;
      this.editDialog = false;
      this.$nextTick(() => {
        this.editDialog = true;
      });
    },
    closeEditDialog() {
      this.showEditDialog = false;
      this.editDialog = false;
      this.getList();
    },
  },
};
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
}
.list_wrap {
  border-top: 1px solid #eee;
  @include flexbox();
}
.list_left {
  width: 280px;
}
.list_right {
  width: calc(100% - 280px);
  border-left: 1px solid #eee;
}
</style>

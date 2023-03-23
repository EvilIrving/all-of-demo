<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:47:09
 * @Description: 专家库管理
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\expertDatabaseManage\ExpertDatabaseManage.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>专家库管理</p>
    </div>
    <div
      class="info_option"
      style="width: 100%; padding: 25px 16px 16px 16px; box-sizing: border-box"
    >
      <el-input
        placeholder="请输入名称"
        prefix-icon="el-icon-search"
        @change="getList()"
        v-model="name"
        style="width: 240px; margin-right: 15px"
        clearable
      />
      <span style="font-size: 14px; margin-left: 15px">专家类型：</span>
      <el-select
        v-model="type"
        placeholder="请选择"
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in expertTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <span style="font-size: 14px; margin-left: 15px">专家级别：</span>
      <el-select
        v-model="level"
        placeholder="请选择"
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in expertLevelOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <span style="font-size: 14px; margin-left: 15px">擅长方向：</span>
      <el-select
        v-model="goodAt"
        placeholder="请选择"
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in directionTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        >
        </el-option>
      </el-select>
      <el-button type="primary" v-permission="'expertDatabaseManage:add'" class="right" @click="editTable('新增', '')"
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
          label="姓名"
          align="center"
        ></el-table-column>
        <el-table-column prop="type" label="专家类型" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.type == '1'">堤防海塘抢险</span>
            <span v-if="scope.row.type == '2'">水闸泵站抢险</span>
          </template></el-table-column
        >
        <el-table-column prop="level" label="级别" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.level == '1'">省级专家</span>
            <span v-if="scope.row.level == '2'">市级专家</span>
            <span v-if="scope.row.level == '3'">县级专家</span>
          </template></el-table-column
        >
        <el-table-column prop="goodAt" label="擅长方向" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.goodAt == '1'">防汛抢险专家</span>
            <span v-if="scope.row.goodAt == '2'">防汛调度专家</span>
          </template></el-table-column
        >
        <el-table-column
          prop="department"
          label="所在单位"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="title"
          label="职称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="mobile"
          label="联系方式"
          align="center"
        ></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                v-permission="'expertDatabaseManage:edit'"
                @click="editTable('编辑', scope.row.dtsCmfId)"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                v-permission="'expertDatabaseManage:del'"
                @click="delTable(scope.row.dtsCmfId)"
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
      <expert-database-manage-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></expert-database-manage-form>
    </el-dialog>
  </div>
</template>

<script>
import { basMsgZjkApi } from "@/api/seawallPrevent";
import ExpertDatabaseManageForm from "./ExpertDatabaseManageForm";

export default {
  name: "ExpertDatabaseManage",
  props: {},
  components: {
    ExpertDatabaseManageForm,
  },
  data() {
    return {
      name: "",
      type: "",
      expertTypeOptions: [
        {
          label: "堤防海塘抢险",
          value: "1",
        },
        {
          label: "水闸泵站抢险",
          value: "2",
        },
      ],
      level: "",
      expertLevelOptions: [
        {
          label: "省级专家",
          value: "1",
        },
        {
          label: "市级专家",
          value: "2",
        },
        {
          label: "县级专家",
          value: "3",
        },
      ],
      goodAt: "",
      directionTypeOptions: [
        {
          label: "防汛抢险专家",
          value: "1",
        },
        {
          label: "防汛调度专家",
          value: "2",
        },
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
      let res = await basMsgZjkApi.list({
        name: this.name,
        type: this.type,
        level: this.level,
        goodAt: this.goodAt,
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
        basMsgZjkApi.del(id).then((res) => {
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
  @include contentBox();
  height: 100%;
}
</style>
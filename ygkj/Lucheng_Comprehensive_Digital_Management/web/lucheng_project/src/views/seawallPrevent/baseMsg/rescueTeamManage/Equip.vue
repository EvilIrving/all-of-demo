<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:40:09
 * @Description: 队伍设备管理
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\rescueTeamManage\Equip.vue
-->
<template>
  <div class="wrap">
    <div class="list_left">
      <div class="item_box_title">
        <p>队伍列表</p>
      </div>
      <div class="team_list">
        <div
          :class="['team_item', index == teamIndex ? 'active' : '']"
          v-for="(item, index) in teamList"
          :key="item.id"
          @click="clickTeam(index)"
        >
          {{ item.name }}
        </div>
      </div>
    </div>
    <div class="list_right">
      <div class="item_box_title">
        <p>队伍设备列表</p>
      </div>
      <div
        class="info_option"
        style="width: 100%; padding: 0 16px; box-sizing: border-box"
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
        <el-button v-permission="'rescueTeamManageEquip:add'" type="primary" class="right" @click="editTable('新增', '')"
          >新增</el-button
        >
        <el-button
          style="margin-right: 10px"
          class="right"
          @click="exportTable()"
          v-permission="'rescueTeamManageEquip:export'"
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
            label="设备名称"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="type"
            label="设备类型"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="num"
            label="数量"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="measure"
            label="单位"
            align="center"
          ></el-table-column>
          <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span
                  class="deal_btn deal_btn_look"
                  @click="editTable('编辑', scope.row.id)"
                  v-permission="'rescueTeamManageEquip:edit'"
                  >编辑</span
                >
                <span
                  class="deal_btn deal_btn_del"
                  @click="delTable(scope.row.id)"
                  v-permission="'rescueTeamManageEquip:del'"
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
    </div>

    <el-dialog :title="title" :visible.sync="showEditDialog" width="35%">
      <equip-form
        v-if="editDialog"
        :teamId="teamId"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></equip-form>
    </el-dialog>
  </div>
</template>

<script>
import { rescueTeamApi, rescueTeamEquipApi } from "@/api/seawallPrevent";
import EquipForm from "./EquipForm";
import { exportEquipList } from "@/utils/export.js";
export default {
  name: "Equip",
  props: {},
  components: {
    EquipForm,
  },
  data() {
    return {
      teamId: "",
      teamIndex: 0,
      teamList: [],
      name: "",
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
    this.getTeamList();
  },
  methods: {
    async getTeamList() {
      let res = await rescueTeamApi.list({
        pageNum: 1,
        pageSize: 10000,
      });
      if (res.code == 0) {
        this.teamList = res.data.list;
        this.teamId = res.data.list[0].id;
        this.getList();
      }
    },
    async getList() {
      let res = await rescueTeamEquipApi.list({
        name: this.name,
        teamId: this.teamId,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      });
      if (res.code == 0) {
        this.tableData = res.data.list;
        this.pageTotal = res.data.totalRows;
      }
    },
    clickTeam(index) {
      this.teamIndex = index;
      this.teamId = this.teamList[index].id;
      this.getList();
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
        rescueTeamEquipApi
          .del({
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
    exportTable() {
      let obj = {
        name: this.name,
      };
      exportEquipList(obj);
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
  @include flexbox();
  width: 100%;
  .item_box_title {
    border: none;
  }
  .list_left {
    width: 280px;
    .team_list {
      .team_item {
        font-size: 16px;
        font-weight: 500;
        color: #666666;
        line-height: 48px;
        height: 48px;
        cursor: pointer;
        padding-left: 47px;
        &:hover {
          background: #eaf5fe;
        }
        &.active {
          background: #eaf5fe;
        }
      }
    }
  }
  .list_right {
    width: calc(100% - 280px);
    border-left: 1px solid #eee;
  }
}
</style>
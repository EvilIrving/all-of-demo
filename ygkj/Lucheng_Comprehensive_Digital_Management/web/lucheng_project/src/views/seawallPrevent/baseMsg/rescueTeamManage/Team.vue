<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:38:19
 * @Description: 队伍管理
 * @FilePath: \lucheng_project\src\views\seawallPrevent\baseMsg\rescueTeamManage\Team.vue
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
      <span style="font-size: 14px; margin-left: 15px">抢险专业：</span>
      <el-select
        v-model="rescueSpecialty"
        placeholder="请选择"
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in rescueSpecialtyOptions"
          :key="item.name"
          :label="item.name"
          :value="item.name"
        >
        </el-option>
      </el-select>
      <span style="font-size: 14px; margin-left: 15px">单位性质：</span>
      <el-select
        v-model="unitNature"
        placeholder="请选择"
        @change="getList"
        clearable
        style="width: 180px; margin: 0 20px 0 15px"
      >
        <el-option
          v-for="item in unitNatureOptions"
          :key="item.name"
          :label="item.name"
          :value="item.name"
        >
        </el-option>
      </el-select>
      <el-button type="primary" class="right" v-permission="'rescueTeamManageTeam:add'" @click="editTable('新增', '')">新增</el-button>
      <el-button style="margin-right: 10px;" v-permission="'rescueTeamManageTeam:export'" class="right" @click="exportTable()">导出</el-button>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
        <el-table-column prop="name" label="名称" align="center"></el-table-column>
        <el-table-column prop="typeName" label="队伍类型" align="center"></el-table-column>
        <el-table-column prop="rescueSpecialty" label="抢险专业" align="center"></el-table-column>
        <el-table-column prop="unitNature" label="单位性质" align="center"></el-table-column>
        <el-table-column prop="organizationNumber" label="编制人数" align="center"></el-table-column>
        <el-table-column prop="existingNumber" label="现有人数" align="center"></el-table-column>
        <el-table-column prop="address" label="详细地址" align="center"></el-table-column>
        <el-table-column prop="chargePerson" label="负责人" align="center"></el-table-column>
        <el-table-column prop="chargePhone" label="联系方式" align="center"></el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'rescueTeamManageTeam:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                @click="delTable(scope.row.id)"
                v-permission="'rescueTeamManageTeam:del'"
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
      <team-form
        v-if="editDialog"
        :id="id"
        @closeEditDialog="closeEditDialog"
      ></team-form>
    </el-dialog>
  </div>
</template>

<script>
import { rescueTeamApi } from '@/api/seawallPrevent'
import TeamForm from './TeamForm'
import { exportTeamList } from "@/utils/export.js";
export default {
  name: 'Team',
  props: {},
  components: {
    TeamForm,
  },
  data() {
    return {
      name: '',
      rescueSpecialty: '',
      rescueSpecialtyOptions: [
        { name: '防汛救灾', value: 1 },
        { name: '水利抢险', value: 1 },
        { name: '工程救援', value: 1 },
        { name: '水利专业', value: 1 },
        { name: '水上救援', value: 1 },
        { name: '水利工程抢险', value: 1 },
        { name: '水上抢险', value: 1 },
        { name: '其他应急抢险', value: 1 },
        { name: '堤防抢险', value: 1 }
      ],
      unitNature: '',
      unitNatureOptions: [
        { name: '水利局机关单位抢险组织', value: 1 },
        { name: '社会救援机构', value: 1 },
        { name: '自收自支事业单位', value: 1 },
        { name: '差额事业单位', value: 1 },
        { name: '全额事业单位', value: 1 }
      ],
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: '',
      title: '新增',
      editDialog: false,
      showEditDialog: false,
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      let res = await rescueTeamApi.list({
        name: this.name,
        unitNature: this.unitNature,
        rescueSpecialty: this.rescueSpecialty,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      })
      if (res.code == 0) {
        this.tableData = res.data.list
        this.pageTotal = res.data.totalRows
      }
    },
    editTable(title, id) {
      this.id = id
      this.title = title
      this.showEditDialog = true
      this.editDialog = false
      this.$nextTick(() => {
        this.editDialog = true
      })
    },
    delTable(id) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        rescueTeamApi.del({
          ids: id
        }).then((res) => {
          if (res.code == 0) {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.getList()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！',
            })
          }
        })
      })
    },
    exportTable() {
      let obj = {
        name: this.name,
        unitNature: this.unitNature,
        rescueSpecialty: this.rescueSpecialty,
      };
      exportTeamList(obj);
    },
    closeEditDialog() {
      this.showEditDialog = false
      this.editDialog = false
      this.getList()
    },
  },
}
</script>

<style lang='scss' scoped>
.wrap {
  
}
</style>
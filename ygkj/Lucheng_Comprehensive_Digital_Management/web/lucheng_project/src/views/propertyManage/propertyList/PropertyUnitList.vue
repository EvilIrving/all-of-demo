<!--
 * @Author: hanyu
 * @LastEditTime: 2022-06-09 16:23:28
 * @Description: 物业管理-公司列表
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyList\PropertyUnitList.vue
-->

<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>物业管理</p>
    </div>
    <div class="info_option" style="width: 100%; padding: 16px 16px 0 0; box-sizing: border-box;text-align:right">
      <el-button type="primary" @click="editTable('新增', '')">新增</el-button>
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
          label="物业公司名称"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="principal"
          label="负责人"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="phone"
          label="负责人电话"
          align="center"
        ></el-table-column>
        <el-table-column
          label="公司人员"
          align="center"
        >
          <template slot-scope="scope">
            <div class="deal_box">
              <el-button size="mini" @click="peopleTable(scope.row.id)">查看</el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="所管工程"
          align="center"
        >
          <template slot-scope="scope">
            <div class="deal_box">
              <el-button size="mini" @click="projectTable(scope.row.id)">查看</el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                >编辑</span
              >
              <span class="deal_btn deal_btn_del" @click="delCard(scope.row.id)"
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
    <el-dialog :title="title" :visible.sync="showEditDialog" width="550px">
      <property-unit-form v-if="editDialog" :id="id" @closeEditDialog="closeEditDialog"></property-unit-form>
    </el-dialog>
    <el-dialog title="所管工程" :visible.sync="showProjectDialog" width="600px">
      <property-project-list v-if="projectDialog" :id="id" @closeEditDialog="closeEditDialog"></property-project-list>
    </el-dialog>
    <el-dialog title="公司人员" :visible.sync="showPeopleDialog" width="800px">
      <property-people-list v-if="peopleDialog" :deptId="id" @closeEditDialog="closeEditDialog"></property-people-list>
    </el-dialog>
  </div>
</template>

<script>
import { propertyUnitApi } from '@/api/propertyManage'
import PropertyUnitForm from './PropertyUnitForm'
import PropertyProjectList from './components/PropertyProjectList'
import PropertyPeopleList from './components/PropertyPeopleList'
export default {
  name: 'TrainingManage',
  props: {},
  components: {
    PropertyUnitForm,
    PropertyProjectList,
    PropertyPeopleList
  },
  data() {
    return {
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      id: '',
      title: '新增',
      editDialog: false,
      showEditDialog: false,
      projectDialog: false,
      showProjectDialog: false,
      peopleDialog: false,
      showPeopleDialog: false
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      let res = await propertyUnitApi.list({
        pageNum: this.pageNum,
        pageSize: this.pageSize
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
    peopleTable(id){
      this.id = id
      this.showPeopleDialog = true
      this.peopleDialog = false
      this.$nextTick(() => {
        this.peopleDialog = true
      })
    },
    projectTable(id){
      this.id = id
      this.showProjectDialog = true
      this.projectDialog = false
      this.$nextTick(() => {
        this.projectDialog = true
      })
    },
    delCard(id) {
      this.$confirm('此操作会删除该物业公司下所有人员信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        propertyUnitApi.del({ id: id }).then((res) => {
          if (res.code == 0) {
            if (this.tableData.length == 1 && this.pageNum > 1) {
              this.pageNum--
            }
            this.getList()
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！',
            })
          }
        })
      })
    },
    
    closeEditDialog(num) {
      if (num) {
        this.getList()
      }
      this.editDialog = false
      this.showEditDialog = false
      this.projectDialog = false
      this.showProjectDialog = false
      this.peopleDialog = false
      this.showPeopleDialog = false
    },
  },
}
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

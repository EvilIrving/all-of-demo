<!--
 * @Date: 2021-12-09 14:02:47
 * @Author: dtb
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2021-12-10 09:51:07
 * @FilePath: \lucheng_project\src\views\projectDetail\dailyManage\components\ProjectMaintenance.vue
-->
<template>
  <div class="people-box">
    <div class="people-right">
      <div class="info_option" style="
          width: 100%;
          padding: 25px 16px 16px 16px;
          box-sizing: border-box;
          margin-bottom: 20px;
        ">
        <template>
          <el-select @change="getList" v-model="slectedType" placeholder="请选择类型">
            <el-option v-for="item in slecteOption" :label="item.label" :value="item.value" clearable :key="item.value">
            </el-option>
          </el-select>
        </template>
        <!-- <el-button type="primary" class="right" @click="newProject('新增')">新增</el-button>
        <el-button style="margin-right: 10px" class="right" @click="exportTable()">导出</el-button> -->
      </div>
      <div class="tb_wrapper">
        <el-table :data="personalList" border style="width: 100%">
          <el-table-column type="index" align="center" width="50" label="序号"></el-table-column>
          <el-table-column prop="mfTypeName" label="类型" align="center"></el-table-column>
          <el-table-column prop="position" label="部位" align="center"></el-table-column>
          <el-table-column prop="project" label="维养项目" align="center"></el-table-column>
          <!-- <el-table-column align="center" label="操作" width="150">
            <template slot-scope="scope">
              <div class="deal_box">
                <span class="deal_btn deal_btn_look" @click="editTable('编辑', scope.row.id)">编辑</span>
                <span class="deal_btn deal_btn_del" @click="del(scope.row.id)">删除</span>
              </div>
            </template>
          </el-table-column> -->
        </el-table>
        <Pagination :total="pageTotal" :page.sync="pageNum" :limit.sync="pageSize" :page-sizes="[5, 10, 15, 20]" @pagination="getList" />
      </div>
    </div>
  </div>
</template>

<script>
import { maintenance } from "@/api/projectDetail";
export default {
  name: 'ProjectMaintenance',
  data() {
    return {
      tableLoading: false,
      mfType: '',
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      slectedType: '',
      slecteOption: [
        {
          label: '全部',
          value: '',
        },
        {
          label: '工程',
          value: 1,
        },
        {
          label: '设备',
          value: 2,
        },
        {
          label: '环境',
          value: 3,
        },
        {
          label: '其他',
          value: 4,
        },

      ],
      personalList:[]
    };
  },
  mounted() {
    this.getList()
  },
  components: {},
  methods: {
    //表格
    async getList() {
      const seawallCode = this.$route.path === '/maintenance' ? this.$store.state.seawallDetail.prcd : '';
      let opt = {
        seawallCode: seawallCode,
        pageSize: this.pageSize,
        mfType: this.slectedType,
        pageNum: this.pageNum
      }
      this.tableLoading = true;
      await maintenance.mfpm(opt).then((res) => {
        if (res.code == 0) {
          this.tableLoading = false;
          this.personalList = res.data.list;
          this.pageTotal = res.data.totalRows;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.people-box {
  box-sizing: border-box;
  display: flex;
  justify-content: space-between;
  .people-right {
    @include contentBox();
    width: 100%;
  }
}
</style>
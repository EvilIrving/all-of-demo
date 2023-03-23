<!--
 * @Author: dengzhao
 * @LastEditTime: 2022-07-06 15:56:33
 * @Description: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\operation\dispatch\DispatchOperation.vue
-->
<template>
  <div class="wrap">
    <div class="item_box_title">
      <p>调度运行</p>
    </div>
    <div class="list_search_box">
      <div class="search_left">
        <!-- <span style="font-size: 14px">名称：</span>
        <el-input
          placeholder="请输入"
          v-model="dispatchName"
          clearable
          style="width: 180px; margin: 0 20px 0 15px"
        /> -->
        <span style="font-size: 14px">年份：</span>
        <el-date-picker
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="dispatchYear"
          value-format="yyyy"
          type="year"
          placeholder="请选择"
          clearable
          @change="getLoadPageDisDom"
        >
        </el-date-picker>
        <span style="font-size: 14px">调度类型：</span>
        <el-select
          style="width: 180px; margin: 0 20px 0 15px"
          v-model="dispatchType"
          placeholder="请选择"
          @change="getLoadPageDisDom"
        >
          <el-option label="全部" value=""> </el-option>
          <el-option label="上级调度" value="1"> </el-option>
          <el-option label="计划内调度" value="2"> </el-option>
        </el-select>
      </div>
      <div class="search_right">
        <el-button @click="exportsData" v-permission="'dispatchOperation:export'">导出</el-button>
        <el-button type="primary" @click="editTable('新增')" v-permission="'dispatchOperation:add'">新增</el-button>
      </div>
    </div>
    <div class="tb_wrapper">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column type="selection" width="50"> </el-table-column>
        <el-table-column
          type="index"
          align="center"
          width="50"
          label="序号"
        ></el-table-column>
        <el-table-column
          prop="dYear"
          label="年份"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="tm"
          label="调令下达时间"
          width="160"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="uploaderName"
          label="上传人"
          align="center"
          :key="Math.random()"
          v-if="dispatchType != 2"
        >
        <template slot-scope="scope">
            <span>{{scope.row.uploaderName ? scope.row.uploaderName : '-'}}</span>
        </template>
        </el-table-column>
        <el-table-column
          prop="domType"
          label="调度类型"
          align="center"
        >
        <template slot-scope="scope">
            <div>
              <!-- 0未处置 1已处置 -1超期未处置 -->
              <!-- <span v-if="scope.row.status == 0">未处置</span> -->
              <span v-if="scope.row.domType == 1">上级调度</span>
              <span v-if="scope.row.domType == 2">计划内调度</span>
            </div>
        </template>
        </el-table-column>
        <el-table-column
          prop="problemDescribe"
          label="闸门状态"
          align="center"
        >
        <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="lookSluiceGate(scope.row.sOrderCode)"
                >详情</span
              >
            </div>
          </template>
        </el-table-column>
        <el-table-column label="泄洪洞状态" align="center">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="lookSpillwayTunnel(scope.row.sOrderCode)"
                >详情</span
              >
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="sOrderCode" label="调令编号" align="center">
          <!-- <template slot-scope="scope">
            <div>
              0未处置 1已处置 -1超期未处置
              <span v-if="scope.row.status == 0">未处置</span>
              <span v-if="scope.row.status == 1">已处置</span>
              <span v-if="scope.row.status == -1">超期未处置</span>
            </div>
          </template> -->
        </el-table-column>
        <el-table-column
          prop="gateOpenTm"
          label="调令闸门开启时间"
          width="160"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="gateCloseTm"
          label="调令闸门关闭时间"
          width="160"
          align="center"
        ></el-table-column>
        <el-table-column
          prop="uploadfile"
          label="调令文件（扫描）"
          align="center"
          :key="Math.random()"
          v-if="dispatchType != 2"
        >
          <template slot-scope="scope">
            <div v-if="scope.row.uploadFiles">
              <div
                v-for="(item, index) in scope.row.uploadFiles"
                :key="index"
                @click="preview(item.fileUrl)"
              >
                <i
                  class="el-icon-link"
                  style="color: #409eff; cursor: pointer"
                ></i>
              </div>
            </div>
            <span v-else>无</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作" width="150">
          <template slot-scope="scope">
            <div class="deal_box">
              <span
                class="deal_btn deal_btn_look"
                @click="editTable('编辑', scope.row.id)"
                v-permission="'dispatchOperation:edit'"
                >编辑</span
              >
              <span
                class="deal_btn deal_btn_del"
                v-permission="'dispatchOperation:del'"
                @click="del(scope.row.id)"
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
        @pagination="getLoadPageDisDom"
      />
    </div>
    <el-dialog
      :title="title"
      width="960px"
      :visible.sync="visibleDialog"
      :before-close="handleClose"
    >
      <dispatch-add-from @close="closeDialog" :id="id" v-if="visibleDialog" />
    </el-dialog>
    <el-dialog
      title="闸门状态"
      width="1020px"
      :visible.sync="sluiceDialog"
      :before-close="handleClose"
    >
      <sluice-gate-from @close="handleClose" :soId="soId" v-if="sluiceDialog" />
    </el-dialog>
    <el-dialog
      title="泄洪洞状态"
      width="1020px"
      :visible.sync="tunnelSpillwayDialog"
      :before-close="handleClose"
    >
      <tunnel-spillway-from
        @close="handleClose"
        :soId="soId"
        v-if="tunnelSpillwayDialog"
      />
    </el-dialog>
  </div>
</template>

<script>
import dispatchAddFrom from './components/dispatchAddFrom.vue'
import sluiceGateFrom from './components/sluiceGateFrom.vue'
import tunnelSpillwayFrom from './components/tunnelSpillwayFrom.vue'
import { matterApi } from '@/api/propertyManage.js'
import { exportSchedulingOperation } from "@/utils/export.js";

export default {
  data() {
    return {
      dispatchType: '',
      dispatchYear: '',
      dispatchName: '',
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      visibleDialog: false,
      id: '',
      title: '新增',
      soId: '',
      sluiceDialog: false,
      tunnelSpillwayDialog: false,
    }
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo
    },
  },
  components: {
    dispatchAddFrom,
    sluiceGateFrom,
    tunnelSpillwayFrom,
  },
  mounted() {
    this.getLoadPageDisDom()
  },
  methods: {
    async getLoadPageDisDom() {
      await matterApi
        .queryByPage({
          dYear: this.dispatchYear,
          domType: this.dispatchType,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          pid: this.projectDetail.prcd,
        })
        .then((res) => {
          if (res.code === 0) {
            this.tableData = res.data.list
            this.pageTotal = res.data.totalRows
          }
        })
    },
    handleClose() {
      this.visibleDialog = false
      this.sluiceDialog = false
      this.tunnelSpillwayDialog = false
    },
    closeDialog() {
      this.visibleDialog = false
      this.getLoadPageDisDom()
    },
    editTable(type, id) {
      // console.log(id);
      this.id = id
      this.title = type
      this.visibleDialog = true
    },
    lookSluiceGate(soId) {
      this.soId = soId
      this.sluiceDialog = true
    },
    lookSpillwayTunnel(soId) {
      this.soId = soId
      this.tunnelSpillwayDialog = true
    },
    preview(fileUrl) {
      // console.log(fileUrl);
      window.open(fileUrl)
    },
    del(id) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        matterApi.del({
          id: id
        }).then((res) => {
          if (res.code == 0) {
            this.$message({
              type: 'success',
              message: '删除成功!',
            })
            this.getLoadPageDisDom()
          } else {
            this.$message({
              type: 'error',
              message: '删除失败！',
            })
          }
        })
      })
    },
    exportsData() {
      let obj = {
        dYear: this.dispatchYear,
        domType: this.dispatchType,
      };
      exportSchedulingOperation(obj);
    },
  },
}
</script>

<style lang="scss" scoped>
.wrap {
  @include contentBox();
  height: 100%;
  .list_search_box {
    @include flexbox;
    @include flexAI(center);
    @include flexJC(space-between);
    padding: 25px 16px 16px;
  }
}
</style>
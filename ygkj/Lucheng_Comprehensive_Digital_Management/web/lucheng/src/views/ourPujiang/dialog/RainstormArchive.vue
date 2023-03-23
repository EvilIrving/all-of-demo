<!--
 * @Date: 2022-05-10 16:41:00
 * @Author: 
 * @Description: 
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-22 17:16:52
 * @FilePath: \lucheng\src\views\ourPujiang\dialog\RainstormArchive.vue
-->
<template>
  <div>
    <div class="derive-sty" @click="editTable('新增')">新增</div>
    <div class="derive-sty float" @click="editTable('新增')">导入</div>
    <div class="table-box">
      <v-table :operation="true" :tableData="tableData" :dynamicColumnSetting="dynamicColumnSetting">
        <template v-slot:name="row">
          <span>{{ row.data.name }}</span>
        </template>
        <template v-slot:operation="{ data }">
          <div class="deal_box">
            <span class="deal_btn deal_btn_look" style="color: #00BAFF" @click="look('详情', data.id)">详情</span>
            <span class="deal_btn deal_btn_edit" style="color: #00BAFF" @click="editTable('编辑', data.id)">编辑</span>
            <span class="deal_btn deal_btn_del" style="color: #FCEE27" @click="del(data.id)">删除</span>
          </div>
        </template>
      </v-table>
      <div class="bottom__pagination" style="float: right;margin-top: 9px;">
        <el-pagination :current-page.sync="pageNum" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next" :total="totalPage" @size-change="onSizeChange" @current-change="onCurrentChange" />
      </div>
    </div>
    <PrimaryMapDialog :mainTitle="title" :visible="addRainstormDialog" @handleClose="handleNewClose" :appendToBody="true" :modal="true" top="6vh" width="600px">
      <div class="dialog-content">
        <el-form ref="form" class="form-box-super" :rules="rules" :model="form" label-width="75px">
          <el-form-item label="暴雨名称：">
            <el-input size="small" v-model="form.name" placeholder="请输入"></el-input>
          </el-form-item>
          <el-form-item label="时间起点：">
            <el-date-picker size="small" style="width: 100%" :picker-options="pickerOptions" v-model="form.time" type="datetime" value-format="yyyy-MM-dd hh" placeholder="选择日期时间"></el-date-picker>
          </el-form-item>
          <template v-for="item in rainfallList">
            <el-row :gutter="50" :key="'rainfallList-' + item.label">
              <el-col :span="12">
                <el-form-item label="降雨时序：">
                  <el-input size="small" v-model="item.label" disabled>
                    <span slot="suffix">h</span>
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="降雨量：">
                  <el-input size="small" v-model="item.value">
                    <span slot="suffix">mm</span>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </template>
          <el-button v-show="rainfallList.length>2" style="margin-left: 75px;margin-bottom: 15px;color: #FCEE27;border-color: #FCEE27;background-color: rgba(11, 187, 255, 0.1);" size="small" plain @click="deleteSequence">删除</el-button>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button style="margin-right: 288px;color: #00BAFF;border-color: #00BAFF;" type="primary" size="small" plain @click="addSequence">新增时序</el-button>
          <el-button style="color: #00BAFF;border-color: #00BAFF;" type="primary" size="small" plain @click="handleNewClose">取消</el-button>
          <el-button style="background: linear-gradient(180deg, #36C0E4 0%, #0639FF 100%)" type="primary" size="small" plain @click="submitForm('form')">确定</el-button>
        </div>
      </div>
    </PrimaryMapDialog>

    <PrimaryMapDialog mainTitle="详情" :visible="checkRainstormDialog" @handleClose="handleCheckClose" :modal="true" :appendToBody="true" top="6vh" width="1100px">
      <div class="dialog-content" style="height:600px">
        <rainstorm-detail v-if="checkRainstormDialog" :code="clickedId" />
      </div>
    </PrimaryMapDialog>
  </div>
</template>

<script>
import { PrimaryMapDialog } from '@/components';
import VTable from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/components/table/VTable.vue';
import RainstormDetail from './RainstormDetail.vue'
import { HeavyRainsArchive } from "@/api/bigScreenPart";
export default {
  name: 'RainstormArchive',
  props: {},
  components: {
    VTable, PrimaryMapDialog, RainstormDetail
  },
  data() {
    return {
      dynamicColumnSetting: [
        {
          prop: 'name',
          label: '暴雨名称'
        },
        {
          prop: 'startTime',
          label: '时间段'
        },
        {
          prop: 'rainfall',
          label: '总降雨量(mm)'
        }
      ],
      tableData: [],
      pageSize: 10,
      totalPage: 1,
      pageNum: 1,
      addRainstormDialog: false,//新增 或 编辑
      checkRainstormDialog: false,//详情
      clickedId: '',
      isAdd: false,
      form: { name: '', startTime: '', endTime: '', duration: 0, rainfall: '', },
      rules: {
        name: [{ required: true, message: '请填写名称', trigger: 'blur' }],
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > new Date().getTime();
        },
      }, //限制日期选择范围
      title:'新增',
      rainfallList: [
        {label: '0-1', value: ''},
        {label: '1-2', value: ''}
      ]
    };
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {
    this.getList()
  },
  methods: {
    async changeTime(time) {
      console.log(time,'time');
      this.form.startTime = time[0]
      this.form.endTime = time[1]
      this.form.duration = (new Date(`${time[1]}:00:00`).getTime() - new Date(`${time[0]}:00:00`).getTime()) / 1000 / 60 / 60
      let res = await HeavyRainsArchive.precipitation({ startTime: time[0], endTime: time[1] })
      this.form.rainfall = res.data || '-'
    },
    async getList() {
      let res = await HeavyRainsArchive.list({
        pageNum: this.pageNum,
        pageSize: this.pageSize
      })
      this.tableData = res.data.list
      this.totalPage = res.data.totalRows
    },
    onSizeChange(size) {
      this.pageNum = 1;
      this.pageSize = size;
      this.getWaterLoggingList();
    },
    onCurrentChange(page) {
      this.pageNum = page
      this.getWaterLoggingList();
    },
    // 详情
    look(title, id) {
      this.isAdd = false
      this.title = '详情';
      this.clickedId = id;
      this.checkRainstormDialog = true;
    },
    //编辑  新增
    async editTable(title, id = '') {
      this.isAdd = title === '新增';
      this.title = title;
      this.clickedId = id;
      this.addRainstormDialog = true;
      if (!this.isAdd) {
        let res = await HeavyRainsArchive.list({ id: id })
        this.form = res.data.list[0]
        this.form.time = [this.form.startTime,this.form.endTime]
        this.rainfallList = [
          {label: '0-1', value: ''},
          {label: '1-2', value: ''}
        ]
      } else {
        this.form = { name: '', startTime: '', endTime: '', duration: 0, rainfall: '', }
        this.rainfallList = [
          {label: '0-1', value: ''},
          {label: '1-2', value: ''}
        ]
      }
    },
    handleNewClose() {
      this.addRainstormDialog = false
    },
    handleCheckClose() {
      this.checkRainstormDialog = false
    },
    //删除
    del(id) {
      this.$confirm("此操作将删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          await HeavyRainsArchive.del({ id: id })
            .then((res) => {
              if (res.code == 0) {
                this.$message({
                  message: "删除成功",
                  type: "success",
                });
                this.getPerson();
              } else {
                this.$message.error(res.msg);
              }
            });
        })
        .catch(() => { });
    },
    submitForm(formName) {
      let that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          const { id, name, startTime, endTime, duration, rainfall } = that.form
          let option = { id, name, startTime, endTime, duration, rainfall }
          HeavyRainsArchive.save(option)
            .then((res) => {
              if (res.code == 0) {
                that.$message.success('新增成功');
                that.handleNewClose();
                that.getList();
              } else {
                that.$message.error(res.msg);
              }
            })
            .catch((err) => {
              that.$message.error(err.msg);
            });
        } else {
          return false
        }
      })
    },
    addSequence() {
      const len = this.rainfallList.length
      this.rainfallList.push({
        label: len + '-' + (len + 1),
        value: ''
      })
    },
    deleteSequence() {
      const len = this.rainfallList.length
      if (len > 2) {
        this.rainfallList.pop()
      }
    }
  },
};
</script>

<style scoped lang="scss">
.derive-sty {
  width: 60px;
  padding: 0 10px;
  text-align: center;
  border-radius: 4px;
  line-height: 26px;
  color: #fff;
  margin: 10px 0px;
  cursor: pointer;
  border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
  background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
  float: left;
  &.float {
    float: right;
  }
}
.confirm {
  padding: 0 10px;
  text-align: center;
  border-radius: 4px;
  line-height: 30px;
  float: right;
  color: #fff;
  cursor: pointer;
  border-color: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%) !important;
  background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
}
.dialog-footer {
  position: absolute;
  right: 20px;
  bottom: 20px;
  .el-button--primary.is-plain {
    color: #fff;
    border-color: #0bbbff;
    background-color: rgba($color: #0bbbff, $alpha: 0.1);
  }
}
</style>
<style>
.rain-daterange {
  border: 1px solid #08749a;
  background-color: transparent;
}
.rain-daterange .el-icon-date {
  color: #fff;
  line-height: 27px;
}
.rain-daterange .el-range-separator {
  color: #fff;
  line-height: 24px;
  padding: 0;
}
.rain-daterange .el-range-input {
  background-color: transparent;
  color: #fff;
}
</style>

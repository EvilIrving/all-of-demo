<!--
 * @Author: hanyu
 * @LastEditTime: 2022-06-09 16:39:13
 * @Description: 物业管理-工程绑定
 * @FilePath: \lucheng_project\src\views\propertyManage\propertyList\components\PropertyProjectList.vue
-->
<template>
  <div class='dialog_wrap'>
    <el-input style="width: 220px" clearable v-model="projectName" placeholder="请输入工程名称" @change="getList"></el-input>
    <el-table
      ref="multipleTable"
      :data="tableData"
      tooltip-effect="dark"
      style="width: 100%;margin-top: 16px"
      height="500px"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="50"> </el-table-column>
      <!-- <el-table-column type="index" align="center" width="50" label="序号"></el-table-column> -->
      <el-table-column prop="project_name" label="工程名称" align="center"></el-table-column>
      <el-table-column prop="project_type" label="工程类型" align="center"></el-table-column>
    </el-table>
    <div class="submit-box">
      <el-button @click="()=>{this.$emit('closeEditDialog')}">取消</el-button>
      <el-button type="primary" @click="save()">确定</el-button>
    </div>
  </div>
</template>

<script>
import { seawallListApi } from '@/api/seawallPrevent'
import { propertyProjectApi, getAllProjectList } from '@/api/propertyManage'
export default {
  name: 'PropertyProjectList',
  props: {
    id: [String, Number]
  },
  components: {
    
  },
  data () {
    return {
      tableData: [],
      selectArr: [],
      prcds: '',
      projectName: ''
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    async getList(){
      let res = await getAllProjectList({
        name: this.projectName
      })
      if(res.code === 0){
        this.tableData = res.data
        this.getDetail()
      }
    },
    async getDetail(){
      let res = await propertyProjectApi.detail({
        wyhId: this.id,
      });
      if(res.code == 0 && res.data != null){
        let checkArr = res.data.prcd ? res.data.prcd.split(',') : []
        this.$nextTick(() => {
          checkArr.map(item => {
            let index = this.tableData.findIndex(v => v.project_code == item);
            if (index > -1)
              this.$refs.multipleTable.toggleRowSelection(
                this.tableData[index],
                true
              );
          });
        });
      }
    },
    async save(){
      let res = await propertyProjectApi.save({
        wyhId: this.id,
        prcd: this.prcds,
      });
      if(res.code == 0){
        this.$message.success('保存成功');
        this.$emit("closeEditDialog", 1);
      }
    },
    handleSelectionChange(val){
      if(val.length){
        let arr = []
        for (let i = 0; i < val.length; i++) {
          arr.push(val[i].project_code)
        }
        this.prcds = arr.join(',')
      }else{
        this.prcds = ''
      }
    }
  }
}
</script>

<style lang='scss' scoped>
  .dialog_wrap{
    .submit-box{
      text-align: center;
      padding: 16px 0;
    }
  }
</style>
<template>
<div class="new_task">
  <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
  <van-cell-group>
    <van-field
        v-model="form.projectType"
        is-link
        readonly
        label="工程类型"
        placeholder="请选择工程类型"
        @click="showPicker = true"
    />
    <vant-picker
        @onConfirm="onConfirm"
        @onCancel="onCancel"
        :defaultIndex="defaultIndex"
        :showPicker="showPicker"
        :columns="options"
    />
    <van-field
        v-model="form.projectName"
        is-link
        readonly
        label="工程名称"
        placeholder="请选择工程名称"
        @click="showNamePicker = true"
    />
    <vant-picker
        @onConfirm="onConfirmName"
        @onCancel="this.showNamePicker = false"
        :defaultIndex="showNameIndex"
        :showPicker="showNamePicker"
        :columns="proNameOptions"
    />
    <van-field
        v-model="form.problemType"
        is-link
        readonly
        label="问题类型"
        placeholder="请选择问题类型"
        @click="problemShowPicker = true"
    />
    <vant-picker
        @onConfirm="onConfirmOne"
        @onCancel="onCancelOne"
        :defaultIndex="problemDefaultIndex"
        :showPicker="problemShowPicker"
        :columns="problemOptions"
    />
    <van-field
        v-model="form.tm"
        is-link
        readonly
        label="时间"
        placeholder="请选择时间"
        @click="timeShowPicker = true"
    />
    <van-popup v-model="timeShowPicker" position="bottom" round>

      <van-datetime-picker
          type="datetime"
          @confirm="onConfirmTwo"
          @cancel="timeShowPicker = false"
      />
    </van-popup>
    <van-field v-model="form.taskDescription" label="任务简述" placeholder="请输入任务简述"/>
    <van-field v-model="form.hiddenDanger" label="具体隐患" placeholder="请输入具体隐患"/>
    <van-field
        is-link
        readonly
        label="隐患图片(视频)"
    >

      <template #input>
        <el-upload
            :action="$config.uploadUrl"
            :on-success="handlePicture"
            :show-file-list="false"
            :before-upload="handleBefore"
            accept=".png,.jpg,.jpeg"
            v-model="form.imgIds"
            :file-list="picList"
            name="uploadFile"
        >
          <span style="color: rgba(153, 153, 153, 0.6)" v-if="picList.length===0">请上传隐患图片(视频)</span>
          <img
              src="../../../../../assets/images/c_img.png"
              v-if="picList.length!==0"
              style="width: 20px;height: 17px;margin-top: 10px"
          />
        </el-upload>

      </template>

    </van-field>

    <van-field
        v-model="form.processWay"
        is-link
        readonly
        label="处理方式"
        placeholder="请选择处理方式"
        @click="progressWayPicker = true"
    />
    <vant-picker
        @onConfirm="onConfirmWay"
        @onCancel="onCancelWay"
        :defaultIndex="progressWayIndex"
        :showPicker="progressWayPicker"
        :columns="progressWayOptions"
    />

    <van-field
        v-model="form.planTm"
        is-link
        readonly
        label="计划完成时间"
        placeholder="请选择计划完成时间"
        @click="finishTimePicker = true"
    />
    <van-popup v-model="finishTimePicker" position="bottom" round>
      <van-datetime-picker
          type="date"
          @confirm="onConfirmFinishTime"
          @cancel="finishTimePicker = false"
      />
    </van-popup>

    <van-field
        v-model="form.problemSource"
        is-link
        readonly
        label="问题来源"
        placeholder="请选择问题来源"
        @click="sourcePicker = true"
    />
    <vant-picker
        @onConfirm="onConfirmSource"
        @onCancel="this.sourcePicker = false"
        :defaultIndex="sourceDefaultIndex"
        :showPicker="sourcePicker"
        :columns="sourceOptions"
    />

    <van-field
        v-model="form.implementUnit"
        is-link
        readonly
        label="实施单位"
        placeholder="请选择实施单位"
        @click="unitPicker = true"
    />
    <vant-picker
        @onConfirm="onConfirmUnit"
        @onCancel="onCancelUnit"
        :defaultIndex="unitIndex"
        :showPicker="unitPicker"
        :columns="unitOptions"
    />

    <van-field
        v-model="form.implementUser"
        is-link
        readonly
        label="实施人员"
        placeholder="请选择实施人员"
        @click="userPicker=true"
    />
<!--    <van-popup v-model="userPicker" position="bottom" round>
      <van-picker
          show-toolbar
          confirm-button-text="确认"
          cancel-button-text="取消"
          :columns="userOptions"
          @onConfirm="onConfirmUser"
          @onCancel="userPicker=false"
          :defaultIndex="userIndex"

      >
        <template v-slot:title>
          <van-search placeholder="请输入实施人员" @change="searchName" v-model="search" style="width: 200px"></van-search>
        </template>
      </van-picker>
    </van-popup>-->

    <vant-picker
        @onConfirm="onConfirmUser"
        @onCancel="userPicker=false"
        :defaultIndex="userIndex"
        :showPicker="userPicker"
        :columns="userOptions"

    />




    <van-field v-model="form.instructions" label="指导意见" placeholder="请输入指导意见"/>
    <van-button color="#036EFF" block @click="submit">完成</van-button>
  </van-cell-group>
</div>
</template>

<script>
import VantPicker from "@/components/vantPicker";
import {propertyMt} from "@/api/appView";
export default {
  name: "AddNewTask",
  props:{},
  components:{
    VantPicker
  },
  data(){
    return{
      form:{
        projectName:'',
        projectType:'',
        problemType:'',
        tm:'',
        taskDescription:'',
        hiddenDanger:'',
        imgIds:[],
        planTm:'',
        processWay:'',
        problemSource:'',
        implementUnit:'',
        instructions:'',
        implementUser:'',
      },

      search:'',
      picList:[],
      defaultIndex:0,
      problemDefaultIndex:0,
      sourceDefaultIndex:0,
      unitIndex:0,
      progressWayIndex:0,
      showNameIndex:0,
      userIndex:0,

      showPicker:false,
      problemShowPicker:false,
      timeShowPicker:false,
      finishTimePicker:false,
      sourcePicker:false,
      unitPicker:false,
      progressWayPicker:false,
      picShowPicker:false,
      showNamePicker:false,
      userPicker:false,

      options:['水库','水闸','海塘','堤防','泵站'],
      problemOptions:['一般问题','较重问题','严重问题'],
      sourceOptions:['日常巡查','汛前检查','汛后检查','专项检查','特别检查'],
      unitOptions:['物业单位','水管单位','水利局'],
      progressWayOptions:['简单处理','日常跟踪','日常维修','年度大修','综合整治计划'],
      proNameOptions:[],
      userOptions:[],

    }
  },
  mounted() {
    this.addZwlog(this)
  },
  methods:{
    searchName(){
      console.log(1111)
    },
    onConfirm(data, index) {
      this.form.projectType  = data
      this.defaultIndex = index
      this.showPicker = false;
      this.getProNameList(this.form.projectType)

    },
    onConfirmName(data,index){
      this.form.projectName = data
      this.showNameIndex = index
      this.showNamePicker = false

    },
    onConfirmOne(data, index) {
      this.form.problemType  = data
      this.problemDefaultIndex = index
      this.problemShowPicker = false;
    },
    onConfirmTwo(data){
      const d = new Date(data)
      const date = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + ' ' + d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds();
      this.form.tm = date
      this.timeShowPicker = false
    },
    p(s) {
      return s < 10 ? '0' + s : s
    },
    onConfirmWay(data,index){
      this.form.processWay = data
      this.progressWayIndex = index
      this.progressWayPicker = false
    },
    onConfirmFinishTime(data){
      const d = new Date(data)
      const date = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate() + ' ' + d.getHours() + ':' + d.getMinutes() + ':' + d.getSeconds();
      this.form.planTm = date
      this.finishTimePicker = false
    },
    onConfirmUnit(data, index) {
      this.form.implementUnit  = data
      this.unitIndex = index
      this.unitPicker = false;
      this.getPeople(this.form.implementUnit)

    },
    onConfirmUser(data,index){
      this.form.implementUser  = data
      this.userIndex = index
      this.userPicker = false;
    },
    onConfirmSource(data,index){
      this.form.problemSource = data
      this.sourceDefaultIndex = index
      this.sourcePicker = false
    },

    onCancel() {
      this.showPicker = false;
    },
    onCancelOne() {
      this.problemShowPicker = false;
    },
    onCancelWay(){
      this.progressWayPicker = false
    },
    onCancelUnit(){
      this.unitPicker = false
    },
    async getProNameList(type){
      let res = await propertyMt.getProjectName({
        pageNum:1,
        pageSize:9999,
        projectType: type
      })
      console.log(res)
      if(res.code===0){
        let list = res.data.rows
        for(var item of list){
          this.proNameOptions.push(item.name)
        }
      }
    },
    async getPeople(unit){

      if(unit=== '水管单位'){
        let res = await propertyMt.getWaterManageUser({
          pageNum:1,
          pageSize:9999,
        })
        console.log(res)
        if(res.code===0){
          let list =res.data.list
          for(var item of list){
            this.userOptions.push(item.username)
          }
        }

      }
      if(unit === '水利局'){
        this.userOptions = []
        let res = await propertyMt.getUserData({
          pageNum: 1,
          pageSize: 9999,
          username:'',
        })
        console.log(res)
        if(res.code===0){
          let list =res.data.list
          for(var item1 of list){
            this.userOptions.push(item1.username)
          }
        }

      }
    },
    handlePicture(res, file) {
      if (res.code == 0) {
        let index = this.picList.findIndex(
            (item) => item.uid == file.uid
        );
        this.picList[index] = {
          ...this.picList[index],
          ...res.data,
          fileUrl: res.data.filePath,
        };
        this.$set(this.picList, index, this.picList[index]);
        this.form.imgIds.push(res.data.id);
        console.log(this.picList, 22222222);
      } else {
        let index = this.picList.findIndex(
            (item) => item.uid == file.uid
        );
        this.$message.error(
            `${this.picList[index].name}上传失败，请重新上传`
        );
        this.picList.splice(index, 1);
      }
    },
    handleBefore(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.picList.push(obj);
    },
    submit(){
      console.log(this.picList)

      if(this.picList!==0){
        for(var item of this.picList){
          this.form.imgIds.push(item.id)
        }
        this.form.imgIds = this.form.imgIds.join(',')
      }
      propertyMt.updateDailyTask(this.form).then((res)=>{
        if(res.code===0){
          let msg = "新增成功~";
          this.$message.success(msg);
          this.$router.push('/businessApproval')
        }


      })
    },




  }
}
</script>

<style scoped lang="scss">
.new_task{
  .van-button{
    width: 350px;
    height: 45px;
    margin-left: 12px;
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    letter-spacing: 2px;
    margin-top: 25px;
  }

}
</style>

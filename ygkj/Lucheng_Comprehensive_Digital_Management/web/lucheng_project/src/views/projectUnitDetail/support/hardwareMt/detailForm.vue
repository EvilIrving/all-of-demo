<template>
  <div>
    <div class="wrap">
      <div class="item_box_title">
        <p>基本信息</p>
      </div>
      <el-row>
        <el-col :span="6" >设备名称</el-col>
        <el-col :span="6">维护单位</el-col>
        <el-col :span="6">日期</el-col>
        <el-col :span="6">负责人</el-col>
      </el-row>
      <el-row>
        <el-col :span="6">
          <span >{{name}}</span>
        </el-col>
        <el-col :span="6">
          <span >{{unit}}</span>
        </el-col>
        <el-col :span="6">
          <span >{{date}}</span>
        </el-col>
        <el-col :span="6">
          <span >{{people}}</span>
        </el-col>
      </el-row>
    </div>
    <div class="wrap">
      <div class="item_box_title">
        <p>实施前照片</p>
      </div>
      <div class="img_box">
        <div v-for="item in beforePhoto">
          <el-image
              style="width: 100%; height: 100%"
              :src="item.fileUrl"
              :preview-src-list="[item.fileUrl]"
          ></el-image>
        </div>
      </div>
    </div>

    <div class="wrap">
      <div class="item_box_title">
        <p>实施后照片</p>
      </div>
      <div class="img_box">
        <div v-for="item in afterPhoto" >
          <el-image
              style="width: 100%; height: 100%"
              :src="item.fileUrl"
              :preview-src-list="[item.fileUrl]"
          ></el-image>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {databasedM, greenCleaning} from "@/api/projectUnitDetail";

export default {
  name: "detailForm",
  props:{
    id:{
      type:String,
      default:'',
    }
  },
  components:{},
  computed:{
    projectInfo(){
      return this.$store.state.projectInfo
    }
  },
  data(){
    return{
      name:'',
      unit:'',
      date:'',
      people:'',
      afterPhoto:[],
      beforePhoto:[]
    }
  },
  mounted() {
    if (this.id) {
      this.getDetail();
    }
  },
  methods:{
    async getDetail() {
      let res = await databasedM.getHardwareDetail({
        id: this.id,
      });
      if (res.code == 0) {
        console.log(this.id,res)
        //Object.assign(this.form, res.data)
        this.name=res.data.equipmentName
        this.date = res.data.materialDate
        this.unit = res.data.maintainUnit
        this.people = res.data.projectLeader
        this.afterPhoto = res.data.afterPhotoFiles
        this.beforePhoto = res.data.beforePhotoFiles
      }
    },
  }
}
</script>

<style scoped lang="scss">
.wrap{
  @include contentBox();
  height: 100%;
  margin-bottom: 20px;
  .item_box_title{
    display: block;
    padding-top: 16px;
    height: 45px;
  }
  .el-row{
    margin-top: 20px;
    margin-left: 10px;
    .el-col{
      font-size: 16px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      span{
        color: #333333;
        font-weight: 500;
      }
    }
  }
  .img_box{
    @include flexbox();
    flex-direction: row;
    height: 80px;
    > div{
      height: 80px;
      width: 80px;
      margin-right: 5px;
    }

  }
}
</style>
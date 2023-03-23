<template>
  <div>
    <div class="wrap">
      <div class="item_box_title">
        <p>基本信息</p>
      </div>
      <el-row>
        <el-col :span="8" >工程名称</el-col>
        <el-col :span="8">实施人员</el-col>
        <el-col :span="8">日期</el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <span >{{name}}</span>
        </el-col>
        <el-col :span="8">
          <span >{{people}}</span>
        </el-col>
        <el-col :span="8">
          <span >{{date}}</span>
        </el-col>
      </el-row>
    </div>
    <div class="wrap">
      <div class="item_box_title">
        <p>实施前照片</p>
      </div>
      <div class="img_box">
        <div v-for="(item, index) in startPhotoFiles" :key="index">
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
        <div v-for="(item, index) in endPhotoFiles" :key="index">
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
import {greenCleaning} from "@/api/projectUnitDetail";

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
      tableData:[],
      name:'',
      people:'',
      date:'',
      endPhotoFiles:[],
      startPhotoFiles:[]
    }
  },
  mounted() {
    this.name=this.projectInfo.name
    if (this.id) {
      this.getDetail();
    }
  },
  methods:{
    async getDetail() {
      let res = await greenCleaning.getGreenCleaning({
        id: this.id,
      });
      if (res.code == 0) {
        console.log(this.id,res)
        //Object.assign(this.form, res.data)
        this.people =res.data.implementer
        this.date = res.data.materialDate
        this.endPhotoFiles = res.data.endPhotoFiles
        this.startPhotoFiles = res.data.startPhotoFiles


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

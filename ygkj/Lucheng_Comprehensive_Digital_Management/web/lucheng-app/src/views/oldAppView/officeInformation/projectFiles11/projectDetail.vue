<template>
<div class="project_detail">
  <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
    <div class="top_input">
      <van-field v-model="input" placeholder="请输入关键字" @change="getProList"/>
    </div>
  <div class="list_content">
    <van-list
        v-model="loading"
        :finished="finished"
        @load="onLoad"
        v-if="list.length!=0"
    >
      <van-cell v-for="(item,index) in list" :key="index">
        <div class="item" @click="getDetail(item)">
          <img :src="imgSrc1" v-if="item.type==='pdf'"/>
          <img :src="imgSrc2" v-if="item.type==='docx'"/>
          <img :src="imgSrc" v-if="item.type=='folder'"/>
          <div class="item_box">
            <div class="title">
              <span>{{item.name}}</span>
            </div>
            <span class="num">{{item.fileSize}} MB</span>
            <span class="time">{{item.createTime}} 创建</span>
          </div>
        </div>
      </van-cell>
    </van-list>
    <div class="no-data" v-if="list.length==0">
      <img :src="noData">
      <div>暂无数据</div>
    </div>

    <el-dialog
        title="文件预览"
        :visible.sync="fileShow"
        width="90%"
        style="height: 60%"
    >
      <iframe :src="fileSrc" ></iframe>
    </el-dialog>

  </div>
</div>
</template>

<script>
import {appFiles} from "@/api/appView";

export default {
  name: "projectDetail",
  props:{},
  components:{},
  data(){
    return{
      input:'',
      loading:false,
      finished:true,
      imgSrc:require("@/assets/images/officeInformation/file.png"),
      imgSrc1:require("@/assets/images/pdf_icon.png"),
      imgSrc2:require("@/assets/images/docx_icon.png"),
      noData:require("@/assets/images/officeInformation/no_data.png"),

      list:[],
      id:'',
      type:'',

      fileShow:false,
      fileSrc:'',
    }
  },
  mounted() {
    this.id = this.$route.query.id
    this.type = this.$route.query.type
    this.getProList(this.id)
  },
  methods:{
   async getProList(id){
     var typeNum;
     if(this.type==='工程'){
       typeNum = 1
     }else{
       typeNum = 2
     }
     let res = await appFiles.getAppAchievement({
       parentId:id,
       keyWord:this.input,
       achievementType:typeNum,
     })
     if(res.code===0){
       this.list = res.data
     }


    },
    onLoad() {

    },
    getDetail(item){
     if(item.type==='folder'){
       this.getProList(item.id)
     }else {
       this.fileShow = true
       //this.fileSrc ='https://file.ygwjg.com/9883398eed780d6a4f30195f2e79d58529c9fc55.pdf'
     }
    },


  }
}
</script>

<style scoped lang="scss">
.project_detail{
  height: 100%;
  width: 100%;
  .item{
    display: flex;
    flex-direction: row;
    font-size: 12px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 200;
    color: #666666;
    line-height: 16px;
    margin-bottom: 10px;
    margin-top: 10px;
    margin-left: 21px;
    img{
      width: 32px;
      height: 40px;
    }
    .title{
      font-size: 15px;
      font-family: SourceHanSansSC-Medium, SourceHanSansSC;
      font-weight: 250;
      color: #000000;
      margin-left: 12px;
      margin-bottom: 5px;
    }
    .num{
      margin-left: 12px;
      margin-right: 14px;
    }

  }

  .no-data{
    margin-top: 60px;
    margin-left: 129px;

    img{
      width: 117px;
      height: 114px;
      margin-bottom: 28px;
    }
    > div{
      width: 78px;
      height: 22px;
      font-size: 12px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 200;
      color: #999999;
      line-height: 22px;
      margin-left: 32px;
    }
  }
}
</style>
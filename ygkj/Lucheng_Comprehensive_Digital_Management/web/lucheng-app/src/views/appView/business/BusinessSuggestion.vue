<template>
<div class="suggestion_wrap">
  <div class="item">
    <div class="title">处理前照片：</div>
    <div class="pic_box">
      <div
          class="preview-item"
          v-for="(item, index) in beforePicList"
          :key="index"
      >
        <span
            class="el-icon-error icon-btn"
            @click.stop="clearImage(index, 1)"
        ></span>
        <div class="preview-list" v-if="item.filePath" >
          <el-image
              style="width: 100%; height: 100%"
              :src="item.filePath"
              :preview-src-list="[item.filePath]"
          >
          </el-image>
        </div>
      </div>
<!--      <el-upload
          :action="$config.uploadUrl"
          :on-success="handlePictureOne"
          :show-file-list="false"
          :before-upload="handleBeforeOne"
          accept=".png,.jpg,.jpeg"
          v-model="form.imagesBefore"
          :file-list="beforePicList"
          name="uploadFile"
      >

        <img
            src="../../../assets/images/addPic.png"
            style="width: 110px;height: 110px"
        />
      </el-upload>-->

    </div>

  </div>
  <div class="item">
    <div class="title">处理后照片：</div>
    <div class="pic_box">
      <div
          class="preview-item"
          v-for="(item, index) in afterPicList"
          :key="index"
      >
        <span
            class="el-icon-error icon-btn"
            @click.stop="clearImage(index, 2)"
        ></span>
        <div class="preview-list" v-if="item.filePath" >
          <el-image
              style="width: 100%; height: 100%"
              :src="item.filePath"
              :preview-src-list="[item.filePath]"
          >
          </el-image>
        </div>
      </div>
      <el-upload
          :action="$config.uploadUrl"
          :on-success="handlePictureTwo"
          :show-file-list="false"
          :before-upload="handleBeforeTwo"
          accept=".png,.jpg,.jpeg"
          v-model="form.imagesAfter"
          :file-list="afterPicList"
          name="uploadFile"
      >

        <img
            src="../../../assets/images/addPic.png"
            style="width: 110px;height: 110px"
        />
      </el-upload>
    </div>

  </div>
  <div class="item">
    <div class="title">处理后文件：</div>
    <div style="width: 250px">
      <div class="pic_box">
        <el-upload
            :action="$config.uploadUrl"
            accept=".pdf"
            :on-success="handleSuccess"
            :on-remove="handleRemove"
            :limit="100"
            :fileList="beforeFileList"
            :size="15"
            name="uploadFile"
            :on-preview="handlePreview"
        >
          <div style="position: relative;top: 0;left:-60px">
            <img :src="uploadIcon" />
            <span style="margin-left: 7px">添加文件</span>
          </div>

          <div class="extra">
            <span>只能上传pdf文件，且单个文件不超过15</span>
          </div>
        </el-upload>

      </div>
    </div>

  </div>
  <div class="bottom_btn">
    <van-button block color="#036EFF" @click="toApprovalSuggestion">提交</van-button>
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
</template>

<script>
import {propertyMt} from "@/api/appView";

export default {
  name: "BusinessSuggestion",
  props:{},
  components:{

  },
  data(){
    return{
      form:{
        taskId:'',
        sort:'',
        imagesBefore:[],
        imagesAfter:[],
        fileIds:[],
      },
      beforePicList:[],
      afterPicList:[],
      beforeFileList:[],
      uploadIcon:require('@/assets/images/officeInformation/uploadIcon.png'),
      taskId:'',
      status:'',
      fileSrc:'',
      fileShow:false
    }
  },
  mounted() {
    this.addZwlog(this)
    this.form.taskId = this.$route.query.taskId
    this.form.sort = this.$route.query.sort
    this.getPicList()
  },
  methods:{
    async getPicList(){

      let res = await propertyMt.getHiddenDangerDetail({
        id:this.form.taskId
      })
      if(res.code===0){
        this.beforePicList = res.data[0].info.images
      }
      console.log(this.beforePicList)
    },
    handlePictureTwo(res, file) {
      if (res.code == 0) {
        let index = this.afterPicList.findIndex(
            (item) => item.uid == file.uid
        );
        this.afterPicList[index] = {
          ...this.afterPicList[index],
          ...res.data,
          fileUrl: res.data.filePath,
        };
        this.$set(this.afterPicList, index, this.afterPicList[index]);
        this.form.imagesAfter.push(res.data.id);
        console.log(this.afterPicList, 22222222);
      } else {
        let index = this.afterPicList.findIndex(
            (item) => item.uid == file.uid
        );
        this.$message.error(
            `${this.afterPicList[index].name}上传失败，请重新上传`
        );
        this.afterPicList.splice(index, 1);
      }
    },

    handleBeforeTwo(file) {
      let obj = {
        num: 0,
        uid: file.uid,
        name: file.name,
      };
      this.afterPicList.push(obj);
    },

    handleSuccess(res) {
      if (res.code == 0) {
        let files = res.data;
        let obj = {
          name: files.originalName,
          url: files.filePath,
          ...files,
        };
        this.beforeFileList.push(obj);
        this.form.fileIds.push(files.id);
      } else {
        this.$message.error(res.msg);
      }
    },
    handleRemove(file) {
      let index = this.beforeFileList.findIndex((item) => item.id == file.id);
      this.beforeFileList.splice(index, 1);
      this.form.fileIds.splice(index, 1);
    },
    handlePreview(data){
      console.log(data)
      this.fileShow=true
      this.fileSrc=data.filePath
    },
    clearImage(index, type) {
      if (type == 1) {
        this.form.imagesBefore.splice(index, 1);
        this.beforePicList.splice(index, 1);
        console.log(this.beforePicList)
      } else {
        this.form.imagesAfter.splice(index, 1);
        this.afterPicList.splice(index, 1);
      }
    },
    toApprovalSuggestion(){
      if(this.beforePicList!==0){
        this.form.imagesBefore = this.form.imagesBefore.join(',')
      }
      if(this.afterPicList!==0){

        this.form.imagesAfter = this.form.imagesAfter.join(',')
      }
      if(this.beforeFileList!==0){

        this.form.fileIds = this.form.fileIds.join(',')
      }
      propertyMt.implementSubmit(this.form).then((res)=>{
        if(res.code===0){
          this.$router.push({
            path:'/businessApprovalSuggestion',
            query:{
              sort:this.form.sort,
              taskId:this.form.taskId
            }
          })
        }
      })


    },

  }
}
</script>

<style scoped lang="scss">
.suggestion_wrap{
  width: 100%;
  height: 100%;
  > div{
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
  }
  .item{
    margin-top: 15px;
    margin-left: 10px;
    margin-bottom: 34px;

    .pic_box{
      .preview-item {
        width: 80px;
        height: 80px;
        border-radius: 2px;
        // overflow: hidden;
        margin: 0 12px 12px 0;
        background: #ebedf0;
        position: relative;

        .preview-list {
          width: 100%;
          height: 100%;

          ::v-deep .el-image {
            .el-image-viewer__wrapper {
              .el-image-viewer__close {
                color: #ffffff;
              }
            }
          }
        }
      }
      .icon-btn {
        position: absolute;
        top: -5px;
        left: -5px;
        z-index: 3;
        cursor: pointer;
        color: #969799;
        font-size: 20px;
       /* display: none;*/
      }
      img{
        width: 14px;
        height: 14px;

      }
    }
    .extra{
      width: 230px;
      font-size: 12px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 200;
      color: #999999;
      line-height: 17px;
      margin-top: 3px;
    }
  }
  .bottom_btn{
    width: 350px;
    height: 45px;
    border-radius: 5px;
    font-size: 18px;
    font-family: PingFangSC-Medium, PingFang SC;
    font-weight: 250;
    color: #FFFFFF;
    line-height: 25px;
    letter-spacing: 2px;
    margin-left: 13px;
  }
  iframe{

  }
}
</style>
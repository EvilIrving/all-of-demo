<!-- 预览文件-->
<template>
  <el-dialog
    :visible.sync="showDialog"
    width="80%"
    center
    top="6vh"
    title="预览文件"
    class="list-dialog preview-pdf"
    :modal-append-to-body="false"
     append-to-body
    @closed="close"
  >
    <div class="preview-body">
      <div class="preview-left">
           <ul v-for="(file,index) in fileList" :key="file.id" >
             <li class="file-item" 
             @click="showFile(file.name,file.url,index)"
             :class="{active : activeIndex===index}">
                <p> <span>{{index+1}}.&nbsp;</span> {{ file.name}} </p>
             </li>
           </ul> 
      </div>
      <div class="preview-right">
          <div class="pdf" v-show="showPdfWrapper">
        <!-- <pdf v-for="item in numPages" :key="item" :src="src" :page="item" /> -->
         <iframe :src="fileUrl" frameborder="0" width="100%" height="100%"></iframe>
      </div>
      <div class="pdf" v-show="showImgWrapper">
        <img style="width: 100%" :src="fileUrl" alt="" />
      </div>
      </div>
    </div>

    <div slot="footer">
      <el-row>
        <div class="form-footer">
          <el-button class="form-btn btn-cancel" @click="close">关闭</el-button>
        </div>
      </el-row>
    </div>
  </el-dialog>
</template>
<script>
import { getFileList } from "@/api/login";
// import pdf from "vue-pdf";
export default {
  props: {
    visible: Boolean,
    id: String,
  },
  components: {  },
  data() {
    return {
      showPdfWrapper:false,
      showImgWrapper:false,
      ids:"",
      show: false,
      isLook: false,
      fileList: [],
      src: "",
      fileId: "",
      fileUrl: "",
      activeIndex:'',
      showDialog:false
    };
  },
  watch:{
    id(val){
      //监听文件id 变化，存入session
      if(val.length>0){
         sessionStorage.setItem("fileId",val);
         this.ids=sessionStorage.getItem("fileId");
      }
    },
    //控制预览文件显示状态。 获取seesion 中的文件id,查找文件
    visible(val,oldVal){
      this.showDialog=val;
      let id=sessionStorage.getItem("fileId")
      console.log(id);
      this.getFile();
    },
   'fileList':{  //默认一进来展示第一个文件
     handler: function(val,oldVal){
       if(val.length>0){
         this.showFile(val[0].name,val[0].url,0); 
       }
     },
     deep:true,
   }
  },
  methods: {
    closeAddNote() {
      this.addNoteShow = false;
    },
    // 关闭
    close() {
      this.$emit("close");
      this.showDialog=false;
      this.showImgWrapper=false;
      this.showPdfWrapper=false;
      this.fileUrl="";
      this.fileList=[];
    },
    // 获取图片id
    async getFile() {
      this.fileList = [];
      this.ids=sessionStorage.getItem("fileId");
      await getFileList({ ids: this.ids }).then((res) => {
        if (res.code == 0) {
          let info = res.data.data;
          info.map((item) => {
            this.fileList.push({
              name: item.fileName,
              url: item.fileUrl,
              id: item.id,
            });
          });
        }
      });
    },
    //根据文件的后缀名称，控制预览的文件显示框。
    showFile(fileName,fileUrl,index){
      this.activeIndex=index;
      let fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
       this.fileUrl=fileUrl;
      if(fileType==="pdf"){
        this.showPdfWrapper=true;
        this.showImgWrapper=false;
      }
      if(fileType==="img" || fileType==="png"|| fileType==="jpg" || fileType==="jpeg"){
         this.showImgWrapper=true;
         this.showPdfWrapper=false;
      }
    },
  },
};
</script>
<style lang='scss'>
@import "@/style/listDialog.scss";
.list-dialog.preview-pdf .el-dialog--center .el-dialog__body {
  padding-bottom: 0;
  width:100%;
  .list-dialog.preview-pdf .preview-body {
    .pdf {
      width: 100%;
      height: 700px;
      overflow-y: auto;
    }
  }
}
</style>
<style lang="scss" scoped>
.list-dialog.preview-pdf .preview-body {
  height: 700px;
  width: 100%;
  display: flex;
  font-size: 14px;
  .preview-left{
    flex:0 0 24%;
    border-right: 2px solid #EEEEEE;
    .file-item{
      padding: 5px 5px;
      margin: 5px 5px;
      cursor: pointer;
    }
    .active{
      background: #6CC1AD;
      color:#fff;
    }
  }
  .preview-right{
    width:100%;
    padding: 5px 5px;
    .pdf {
    width: 100%;
    height: 700px;
    overflow-y: auto;
  }
  }
  .menu-list {
    width: 240px;
    height: 100%;
    padding-top: 10px;
    overflow-y: auto;
    .menu-item {
      line-height: 36px;
      color: #999999;
      cursor: pointer;
      text-align: center;
      .active-circle {
        display: inline-block;
        width: 6px;
        height: 6px;
        border-radius: 50%;
        background-color: #328173;
        vertical-align: middle;
      }
      &.active {
        color: #328173;
      }
    }
  }
  .notes-list {
    display: flex;
    flex-direction: column;
    width: 330px;
    height: 100%;
    padding-left: 16px;
    padding-top: 20px;
    .el-input .el-input__inner {
      background: rgba(255, 255, 255, 0.4);
      border: 1px solid #e5e5e5;
      border-radius: 20px;
      border-radius: 20px;
    }
    .notes-operate {
      margin: 10px 0;
      i {
        cursor: pointer;
        &:first-child {
          margin-right: 10px;
        }
      }
    }
    .notes-box {
      height: calc(100% - 64px);
      overflow-y: auto;
    }
    .notes-item {
      padding: 8px;
      margin-bottom: 10px;
    }
    .notes-item.active {
      background: #f2f2f2;
    }
    .notes-name {
      color: #328173;
      line-height: 20px;
    }
    .notes-content {
      color: #333333;
      line-height: 20px;
    }
  }
}
</style>

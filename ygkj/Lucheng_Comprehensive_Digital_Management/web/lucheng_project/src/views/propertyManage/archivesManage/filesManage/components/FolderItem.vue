<template>
  <div :class="['folder-item',isChecked ? 'active' : '']">
    <b class="check-icon icon-check-active-icon" v-show="isChecked"></b>
    <div class="folder-content">
      <!-- 文件夹 -->
      <i v-if="!fileData.type" class="icon-folder-icon"></i>
      <p v-if="!fileData.fileName" :title="fileData.fileName">{{fileData.name}}</p>
      <!-- 文件 -->
      <i
        v-if="fileData.fileType === 'jpg'||fileData.fileType === 'png'||fileData.fileType === 'jpeg'||fileData.fileType === 'gif'"
        class="icon-img-bg"
      ></i>
      <!-- <i v-if="fileData.fileType === 'pdf'" class="icon-pdf-big"></i> -->
      <i v-if="fileData.fileType === 'doc'||fileData.fileType === 'docx'" class="icon-word-big"></i>
      <i v-if="fileData.fileType === 'xls'||fileData.fileType === 'xlsx'" class="icon-excel-big"></i>
      <i v-if="fileData.fileType === 'ppt'||fileData.fileType === 'pptx'" class="icon-ppt-big"></i>
      <p :title="fileData.fileName" v-if="fileData.type === 'file'">{{fileData.fileName}}</p>
      <!-- <p>{{subtitle}}</p> -->
    </div>
  </div>
</template>

<script>
export default {
    name:'FolderItem',
    props:{
        title:String,
        subtitle:String,
        fileData:Object,
        isChecked:Boolean
    },
    data () {
        return {

        };
    },
    components: {

    },
    mounted() {

    },
    methods: {
        
    }
}
</script>

<style lang='scss' scoped>
.folder-item {
  width: 170px;
  height: 170px;
  position: relative;
  @include flexbox;
  @include flexAC;
  @include flexJC(center);
  cursor: pointer;
  border: 1px solid rgba(0, 0, 0, 0);
  margin: 0 12px 12px 0;
  &.active {
    background: #edebe9;
  }
  &:hover {
    border: 1px solid #777574;
  }
  .check-icon {
    position: absolute;
    right: 8px;
    top: 8px;
    width: 20px;
    height: 20px;
  }
  .folder-content {
    @include flexbox;
    @include flexflows(column, wrap);
    @include flexAC;
    i {
      display: block;
      width: 90px;
      height: 90px;
      margin-bottom: 8px;
      &.icon-folder-icon {
        width: 110px;
      }
    }
    p {
      text-align: center;
      padding: 3px 0;
      font-size: 15px;
      &:last-child {
        font-size: 14px;
        width: 110px;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}
</style>
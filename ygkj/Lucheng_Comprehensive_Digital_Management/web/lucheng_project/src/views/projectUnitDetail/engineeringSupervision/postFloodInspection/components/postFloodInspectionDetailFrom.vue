<!--
 * @Author: wanxin
 * @LastEditTime: 2022-05-21 11:34:04
 * @Descripttion: 
 * @FilePath: \lucheng_project\src\views\projectUnitDetail\engineeringSupervision\postFloodInspection\components\postFloodInspectionDetailFrom.vue
-->
<template>
  <div>
    <div class="wrap">
      <div class="item_box_title">
        <p>基本信息</p>
      </div>
      <el-row>
        <el-col :span="8">工程名称</el-col>
        <el-col :span="8">检查时间</el-col>
        <el-col :span="8">隐患数量</el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <span>{{ name }}</span>
        </el-col>
        <el-col :span="8">
          <span>{{ date }}</span>
        </el-col>
        <el-col :span="8">
          <span>{{ num }}</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">参加人员</el-col>
      </el-row>
      <el-row>
        <el-col :span="24"
          ><span>{{ people }}</span></el-col
        >
      </el-row>
    </div>
    <div class="wrap">
      <div class="item_box_title">
        <p>检查照片</p>
      </div>
      <div class="img_box">
        <div v-for="item in checkPhotoFiles">
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
        <p>附件列表</p>
      </div>
      <div class="file_box">
        <div v-for="item in aidFiles">
          <div class="fileStyle">1.{{ item.fileName }}<span @click="download(item)">下载</span
            ><span @click="lookFile(item)">查看</span></div>
        </div>
        <!-- <div v-for="item in projCheckRecordFiles">
          <div class="fileStyle">2.{{ item.fileName }}</div>
        </div> -->
      </div>
    </div>
    <el-dialog
      title="文件预览"
      append-to-body
      :visible.sync="fileDialog"
      fullscreen
    >
      <look-file
        v-if="fileDialog"
        :url="url"
        @closeLookDialog="closeEditDialog"
      ></look-file>
    </el-dialog>
  </div>
</template>

<script>
import { postFloodInspectionApi } from '@/api/projectUnitDetail.js'
import LookFile from '@/components/LookFile'
import axios from 'axios'

export default {
  name: 'detailForm',
  props: {
    id: {
      type: String,
      default: '',
    },
  },
  computed: {
    projectDetail() {
      return this.$store.state.projectInfo
    },
  },
  data() {
    return {
      tableData: [],
      name: '',
      date: '',
      num: '',
      people: '',
      checkPhotoFiles: [],
      aidFiles: [],
      fileDialog: false,
    }
  },
   components: {
    LookFile,
  },
  mounted() {
    this.name = this.projectDetail.name
    if (this.id) {
      this.getDetail()
    }
  },
  methods: {
    async getDetail() {
      let res = await postFloodInspectionApi.mpAfList({
        pid: this.projectDetail.prcd,
        id: this.id,
      })
      if (res.code == 0) {
        console.log(this.projectDetail, res)
        //Object.assign(this.form, res.data)
        let data = res.data.list[0]
        this.date = data.tm
        this.num = data.hitCount
        this.people = data.joinUser
        this.checkPhotoFiles = data.checkPicList
        this.aidFiles = data.fileList
        console.log(this.checkPhotoFiles)
      }
    },
    lookFile(file) {
      this.url = file.fileUrl
      this.fileDialog = true
    },
    closeEditDialog() {
      this.fileDialog = false
    },
    download(item) {
      axios({
        method: 'get',
        responseType: 'blob', // 因为是流文件，所以要指定blob类型
        url: item.fileUrl, // 自己的服务器，提供的一个word下载文件接口
      }).then(({ data }) => {
        console.log(data) // 后端返回的是流文件
        const blob = new Blob([data]) // 把得到的结果用流对象转一下
        var a = document.createElement('a') //创建一个<a></a>标签
        a.href = URL.createObjectURL(blob) // 将流文件写入a标签的href属性值
        a.download = item.fileName //设置文件名
        a.style.display = 'none' // 障眼法藏起来a标签
        document.body.appendChild(a) // 将a标签追加到文档对象中
        a.click() // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
        a.remove() // 一次性的，用完就删除a标签
      })
    },
  },
}
</script>

<style scoped lang="scss">
.wrap {
  @include contentBox();
  height: 100%;
  margin-bottom: 20px;

  .item_box_title {
    display: block;
    padding-top: 16px;
    height: 45px;
  }
  .el-row {
    margin-top: 20px;
    margin-left: 10px;
    .el-col {
      font-size: 16px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #999999;
      span {
        color: #333333;
        font-weight: 500;
      }
    }
  }
  .img_box {
    @include flexbox();
    flex-direction: row;
    height: 80px;
    > div {
      height: 80px;
      width: 80px;
      margin-right: 5px;
    }
  }
  .file_box {
    padding: 20px;
    box-sizing: border-box;
    .fileStyle {
      margin-bottom: 20px;
      height: 48px;
      background: #f4f8fe;
      line-height: 48px;
      padding-left: 16px;
      box-sizing: border-box;
      > span {
        width: 32px;
        height: 22px;
        font-size: 16px;
        color: #1492ff;
        float: right;
        margin-right: 20px;
        cursor: pointer;
      }
    }
  }
}
</style>

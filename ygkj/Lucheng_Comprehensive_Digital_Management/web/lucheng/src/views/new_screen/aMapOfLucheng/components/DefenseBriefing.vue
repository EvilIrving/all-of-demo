<!--
 * @Author: wqn
 * @Date: 2022-09-26 09:42:18
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-12-07 17:25:44
 * @Description: 防御简报弹窗内容
-->
<template>
  <div class='defense_briefing'>
    <div class="briefing_tab">
      <p :class="{active: waterRainType == 1}" @click="waterRainType = 1">汛情简报</p>
      <p :class="{active: waterRainType == 2}" @click="waterRainType = 2">旱情简报</p>
    </div>
    <div class="briefing_top">
      <div>
        <span>日期：</span>
        <el-date-picker v-show="waterRainType == 1" v-model="time1" type="date" size="small" placeholder="选择日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd" @change="changeTime"></el-date-picker>
        <el-date-picker v-show="waterRainType == 2" v-model="time2" type="month" placeholder="选择日期" format="yyyy-MM" size="small" value-format="yyyy-MM" :clearable="false" @change="getAllMonthByDate"></el-date-picker>
      </div>
      <div class="briefing_btn">
        <div style="cursor: pointer" @click="floodPreventionBulletin">生成简报</div>
        <div class="export" style="cursor: pointer" @click="exporBriefing">导出</div>
      </div>
    </div>
    <div class="briefing_content">
      <div class="briefing_content_left">
        <div class="briefing_times">
          <div :class="['briefing_time', timeIndex == i ? 'selectActive' : '']" v-for="(item, i) in times" :key="item.id" @click="selectTime(item.file_id, i)">
            {{ item.tm }}
          </div>
        </div>
      </div>
      <div class="briefing_content_right" :class="{ briefing_content_right_two: waterRainType == 2 }">
        <div class="wordDom" v-if="fileUrl && waterRainType == 1" ref="file"></div>
        <iframe class="iframe-box" v-if="fileUrl && waterRainType == 2" :src="fileUrl" frameborder="0" style=""></iframe>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
const docx = require('docx-preview');
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
export default {
  name: 'defenseBriefing',
  props: {},
  components: {},
  data() {
    return {
      waterRainType: 1,
      times: [],
      time1: new Date().format('yyyy-MM-dd'),
      time2: new Date().format('yyyy-MM'),
      timeIndex: 0,
      fileUrl: '',
      fileName: ''
    };
  },
  computed: {},
  watch: {
    waterRainType(val) {
      if (val === 1) {
        this.getAllHoursByDate();
      } else {
        this.getAllMonthByDate();
      }
    }
  },
  created() {
    if (this.waterRainType === 1) {
      this.getAllHoursByDate();
    } else {
      this.getAllMonthByDate();
    }
  },
  mounted() {},
  destroyed() {},
  methods: {
    changeTime() {
      this.getAllHoursByDate();
    },
    async getAllMonthByDate() {
      this.fileUrl = '';
      let res = await disasterPreventionApi.getAllMonthByDate({ date: this.time2 });
      this.times = res.data;
      res.data.length > 0 && this.getBriefing(res.data[0].file_id);
    },
    async floodPreventionBulletin() {
      if (this.waterRainType == 1) {
        await disasterPreventionApi.floodPreventionBulletin();
        await this.getAllHoursByDate();
      } else {
        await disasterPreventionApi.waterAndRainBriefing();
        await this.getAllMonthByDate();
      }
    },
    exporBriefing() {
      let fileUrl = this.fileUrl;
      if (this.waterRainType == 2) {
        fileUrl = fileUrl.split('src=')[1];
      }
      axios({
        method: 'get',
        responseType: 'blob', // 因为是流文件，所以要指定blob类型
        url: fileUrl // 自己的服务器，提供的一个word下载文件接口
      }).then(({ data }) => {
        const blob = new Blob([data]); // 把得到的结果用流对象转一下
        var a = document.createElement('a'); //创建一个<a></a>标签
        a.href = URL.createObjectURL(blob); // 将流文件写入a标签的href属性值
        a.download = this.fileName; //设置文件名
        a.style.display = 'none'; // 障眼法藏起来a标签
        document.body.appendChild(a); // 将a标签追加到文档对象中
        a.click(); // 模拟点击了a标签，会触发a标签的href的读取，浏览器就会自动下载了
        a.remove(); // 一次性的，用完就删除a标签
      });
    },
    selectTime(id, i) {
      this.timeIndex = i;
      this.getBriefing(id);
    },
    async getAllHoursByDate() {
      let res = await disasterPreventionApi.getAllHoursByDate({ date: this.time1 });
      this.times = res.data;
      res.data.length > 0 && this.getBriefing(res.data[0].file_id);
    },
    async getBriefing(id) {
      let res = await disasterPreventionApi.getBriefing({ ids: id });
      this.fileName = res.data[0].fileName;
      if (this.waterRainType == 1) {
        this.fileUrl = res.data[0].fileUrl;
        this.setDocx();
      } else {
        let src = 'https://view.officeapps.live.com/op/view.aspx?src=';
        this.fileUrl = src + res.data[0].fileUrl;
      }
    },
    setDocx() {
      axios({
        method: 'get',
        responseType: 'blob', // 设置响应文件格式
        url: this.fileUrl
      }).then(({ data }) => {
        docx.renderAsync(data, this.$refs.file); // 渲染到页面预览
      });
    }
  }
};
</script>
<style lang='scss' scoped>
.defense_briefing {
  .briefing_tab {
    margin-top: 16px;
    display: flex;
    > p {
      width: 114px;
      height: 32px;
      border: 1px solid #ffffff;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #ffffff;
      line-height: 30px;
      text-align: center;
      cursor: pointer;
      &:not(:first-child) {
        margin-left: 16px;
      }
      &.active {
        background: rgba(72, 152, 251, 0.1);
        box-shadow: inset 0px 0px 5px 0px #00baff;
        border: 1px solid #00baff;
        color: #00baff;
      }
    }
  }
  .briefing_top {
    margin-top: 16px;
    display: flex;
    justify-content: space-between;
    color: #ffffff;
    ::v-deep .el-input__inner {
      border: 1px solid #e5e5e5;
    }
    .briefing_btn {
      display: flex;
      > div {
        height: 35px;
        width: 100px;
        line-height: 35px;
        background: linear-gradient(180deg, #36c0e4 0%, #0639ff 100%);
        border-radius: 4px;
        text-align: center;
      }
      .export {
        width: 67px;
        margin-left: 38px;
      }
    }
  }
  .briefing_content {
    margin-top: 20px;
    height: 600px;
    background: #ffffff;
    border-radius: 4px;
    display: flex;
    .briefing_content_left {
      width: 290px;
      height: 100%;
      border-right: 1px solid #e6e6e6;
    }
    .briefing_content_right {
      width: calc(100% - 290px);
      position: relative;
      &.briefing_content_right_two {
        overflow: hidden;
      }
      .iframe-box {
        width: 100%;
        height: 115%;
        position: absolute;
        left: 0;
        top: -95px;
      }
    }
  }
  .briefing_times {
    padding: 10px;
    height: 100%;
    .briefing_time {
      width: 270px;
      height: 32px;
      border: 1px solid #e6e6e6;
      text-align: center;
      line-height: 30px;
      margin-bottom: 8px;
      &.selectActive {
        box-shadow: 0px 0px 4px 0px rgba(80, 166, 255, 0.65);
        border: 1px solid #026fe0;
      }
    }
  }
  .wordDom {
    height: 100%;
    //   overflow: auto;
    ::v-deep .docx-wrapper {
      height: 100%;
      overflow: auto;
      background: #fff;
    }
    ::v-deep .docx {
      //   overflow: auto;
      min-height: 0;
    }
    //   ::v-deep section.docx {
    //     overflow: auto;
    //   }
  }
  ::v-deep section.docx {
    overflow: auto;
  }
}
</style>
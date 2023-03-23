<template>
<div class="progress_detail">
  <van-nav-bar fixed placeholder safe-area-inset-top :title="$route.meta.title" :left-arrow="$store.state.viewType !== 'irs'" @click-left="$router.go(-1)" />
  <van-cell-group>
    <van-cell
        value-class="cell-value"
        title-class="cell-title"
        title="工程名称"
        :value="form.projectName"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="工程类型"
        :value="form.projectType"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="问题类型"
        :value="form.problemType"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="上报人"
        :value="form.creator"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="任务简述"
        :value="form.taskDescribe"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="隐患图片(视频)"
        @click="imageView"
    >
      <template #default>
        <img
          src="../../../../../assets/images/c_img.png"
            v-if="form.images!==null"
            style="width: 20px;height: 17px;margin-top: 2px"
        />
      </template>
    </van-cell>
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="处理方式"
        :value="form.processWay"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="计划完成时间"
        :value="form.planTm"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="实施单位"
        :value="form.implementUnit"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="实施人"
        :value="form.implementUser"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="问题来源"
        :value="form.problemSource"
    />
    <van-cell
        value-class="cell-value"
        is-link
        title-class="cell-title"
        title="指导意见"
        :value="form.instructions"
    />
  </van-cell-group>
  <div class="worksheet">
    <div class="worksheet-title">隐患处理工作单</div>
    <div class="block">
      <el-timeline>
        <el-timeline-item
            :timestamp="form.tm"
            color="#036EFF"
            class="active-timeline"
            placement="top"
        >
          <el-card class="card-box">
            <p >上报人： {{form.creator}}</p>
            <p >隐患信息： {{form.instructions}}</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item
            :timestamp="item.tm"
            color="#036EFF"
            placement="top"
            v-for="(item,index) in workForm"
            :key="index"
        >
          <el-card class="card-box">
            <p>{{ item.status | statusType }}人：{{item.operator}}</p>
            <p>{{ item.status | statusType }}状态：
              <span class="orange-color">{{
                  item.status | status
                }}</span>
            </p>
            <p>隐患信息： {{item.opinion}}</p>
          </el-card>
        </el-timeline-item>

      </el-timeline>
    </div>
  </div>
</div>
</template>

<script>
import {propertyMt} from "@/api/appView";
import {ImagePreview} from "vant";

export default {
  name: "ProgressDetail",
  props:{},
  components:{},
  data(){
    return{
      id:'',
      form:{},
      workForm:{},
      lastForm:{},
      imgs:[],
    }
  },
  filters: {
    statusType(value) {
      if (value == 1 || value == 4) {
        return "处理";
      } else if (value == 2 || value == 3 || value == 5) {
        return "审核";
      }
    },
    status(value) {
      switch (value) {
        case 1:
          return "待处理";
        case 2:
          return "已通过";
        case 3:
          return "已驳回";
        case 4:
          return "已处理";
        case 5:
          return "待审核";
        default:
          return "归档";
      }
    },
  },
  mounted() {
    this.addZwlog(this)
    this.id = this.$route.query.id
    this.getDetail()
  },
  methods:{
    async getDetail(){
      let res = await propertyMt.getHiddenDangerDetail({
        id:this.id
      })
      if(res.code===0){
        this.form = res.data[0].info
        this.lastForm = res.data[res.data.length - 1].status == 6
            ? res.data[res.data.length - 1]
            : {};
        this.workForm = res.data.splice(1, res.data.length - 1);

        if (this.lastForm.status) {
          this.workForm.pop();
        }
        for(var item of this.form.images){
          this.imgs.push(item.fileUrl)
        }

      }
      console.log(this.form)


    },
    imageView(){
      ImagePreview({
            images:this.imgs
         },
      )
    }

  }
}
</script>

<style scoped lang="scss">
.progress_detail{
  width: 100%;
  min-height: 100%;
  background: #f2f6f9;
  .cell-value {
    color: #333333;
    font-size: 14px;
    font-weight: 500;
    flex: 1.8;
  }
  .cell-title {
    font-weight: 500;
    color: #666666;
    font-size: 14px;
  }
  .worksheet {
    margin-top: 6px;
    background-color: #ffffff;
    .worksheet-title {
      padding: 15px 0 0 11px;
      font-size: 16px;
      font-weight: 600;
      color: #333333;
    }
    .block {
      padding: 25px 12px 40px 27px;
      .card-box {
        box-shadow: none;
        border: none;
        background-color: #e9f4ff;
        position: relative;
        p {
          font-size: 12px;
          color: #333333;
          padding-bottom: 6px;
          &:last-child {
            padding-bottom: 0;
          }
        }
        .card-btn{
          position: absolute;
          font-size: 14px;
          color: #036EFF;
          font-weight: 500;
          right: 15px;
          top: 50%;
          margin-top: -8px;
        }
      }
    }
  }
}
</style>
<style lang="scss">
.van-cell {
  padding-left: 10px;
  padding-right: 10px;
  &::after {
    left: 0;
    right: 0;
    border-color: #e7e7ec;
  }
}
.block {
  .active-timeline{
    .el-timeline-item__tail{
      border-color: #036EFF;
    }
  }
  .el-timeline-item__timestamp {
    color: #333333;
    font-size: 14px;
  }
  .card-box {
    .el-card__body {
      padding: 8px 15px 8px 10px;
    }
  }
}
</style>
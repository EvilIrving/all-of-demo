<!--
 * @Author: wqn
 * @Date: 2022-09-05 09:49:19
 * @LastEditors: wqn
 * @LastEditTime: 2023-01-03 15:02:44
 * @Description: 防洪调度自定义头部
-->
<template>
  <div class='info_box'>
    <div class="dispatch_analysis">
      <span>调度分析</span>
      <el-checkbox-group v-model="dispatchAnalysisValue" @change="dispatchAnalysisChange">
        <el-checkbox v-for="item in dispatchAnalysis" :label="item.value" :key="item.value">{{item.text}}</el-checkbox>
      </el-checkbox-group>
    </div>
    <div style="display: flex;">
      <div class="rainstorm_file_btn" :class="rainstormFileIsShow ? 'open' : 'default'" @click="rainstormFileDisplayToggle"></div>
      <PrimaryMapDialog mainTitle="暴雨归档" ref="multipleDialog" :visible="rainstormFileIsShow" @handleClose="rainstormFileIsShow=false" :appendToBody="true" :modal="true" top="6vh" width="1000px">
        <div class="dialog-content">
          <RainstormArchive v-if="rainstormFileIsShow" />
        </div>
      </PrimaryMapDialog>
      <div class="project_catalogue_btn" :class="projectCatalogueIsShow ? 'open' : 'default'" @click="projectCatalogueDisplayToggle"></div>
      <div class="project_catalogue_content" v-show="projectCatalogueIsShow">
        <i v-for="i in 4" :key="i"></i>
        <!-- <el-input v-model="projectCatalogueQueryKeyword" placeholder="请输入关键字查询"></el-input> -->
        <ul class="project_catalogue_list" :style="{'max-height': projectCatalogueListHeight + 'px'}" v-show="projectCatalogueList.length > 0">
          <li v-for="(item, index) in projectCatalogueList" :key="'project_' + index" :class="{checked: item.checked}">
            <el-checkbox v-model="item.checked" @change="(e) => projectCatalogueCheckedChange(e, item.id)">{{item.name || '-'}}</el-checkbox>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import { projectList } from '@/api/new_screen_api.js';
import { PrimaryMapDialog } from '@/components';
import RainstormArchive from '@/views/ourPujiang/dialog/RainstormArchive.vue';
export default {
  name: 'controlFloodDispatchInfo',
  props: {},
  components: {
    PrimaryMapDialog,
    RainstormArchive
  },
  data() {
    return {
      dispatchAnalysisValue: ['baseMap'],
      dispatchAnalysis: [
        { value: 'baseMap', text: '地图' },
        { value: 'waterLevelChart', text: '水位图' },
        { value: 'riskMap', text: '风险图' }
      ],
      rainstormFileIsShow: false,
      projectCatalogueIsShow: false,
      projectCatalogueQueryKeyword: '',
      projectCatalogueList: [],
      projectCatalogueListHeight: 0
    };
  },
  computed: {},
  watch: {},
  created() {
    this.getProjectCatalogueList();
  },
  mounted() {
    this.getProjectCatalogueListHeight();
    window.addEventListener('resize', this.getProjectCatalogueListHeight);
  },
  destroyed() {
    window.removeEventListener('resize', this.getProjectCatalogueListHeight);
  },
  methods: {
    /**
     * @description 调度分析类型切换
     */
    dispatchAnalysisChange() {
      if (this.dispatchAnalysisValue.length > 1) {
        this.dispatchAnalysisValue.shift();
        this.$emit('dispatchAnalysisChange', this.dispatchAnalysisValue[0] || 'baseMap');
      }
    },
    /**
     * @description 打开关闭暴雨归档弹窗
     */
    rainstormFileDisplayToggle() {
      this.rainstormFileIsShow = !this.rainstormFileIsShow;
    },
    /**
     * @description 工程目录切换展开和折叠的状态
     */
    projectCatalogueDisplayToggle() {
      this.projectCatalogueIsShow = !this.projectCatalogueIsShow;
    },
    /**
     * @description 获取工程目录列表数据
     */
    getProjectCatalogueList() {
      projectList().then((res) => {
        if (res.code === 0) {
          const data = res.data || [];
          data.map((e) => {
            e.checked = e.type !== '断面测站' || e.name === '断面1';
            if (e.type === '断面测站') {
              e.lgtd = (JSON.parse(e.coordinates)[0][0] + JSON.parse(e.coordinates)[1][0]) / 2;
              e.lttd = (JSON.parse(e.coordinates)[0][1] + JSON.parse(e.coordinates)[1][1]) / 2;
            }
            return e;
          });
          this.projectCatalogueList = data;
          let arr = [];
          this.projectCatalogueList.forEach((element) => {
            arr.unshift(element);
          });
          this.$emit('getAllProject', arr);
          this.$emit(
            'selectedProject',
            arr.filter((e) => e.checked)
          );
        }
      });
    },
    /**
     * @description 获取工程目录列表最大高度
     */
    getProjectCatalogueListHeight() {
      // this.projectCatalogueListHeight = document.documentElement.clientHeight - 719;
      this.projectCatalogueListHeight = document.documentElement.clientHeight - 687;
    },
    /**
     * @description 切换选中工程
     */
    projectCatalogueCheckedChange(e, code) {
      this.projectCatalogueList.map((item) => {
        item.checked = item.stCode === code ? e : item.checked;
        item.id = item.type + '_' + (item.dmId || item.stCode);
        return item;
      });
      let arr = [];
      this.projectCatalogueList.forEach((element) => {
        arr.unshift(element);
      });
      this.$emit(
        'selectedProject',
        arr.filter((e) => e.checked)
      );
    }
  }
};
</script>
<style lang='scss' scoped>
.info_box {
  position: relative;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 100%;
  .dispatch_analysis {
    box-sizing: border-box;
    width: 391px;
    height: 100%;
    border-right: 1px solid rgba(0, 186, 255, 0.39);
    padding: 21px 0 0 32px;
    span {
      display: block;
      width: 80px;
      height: 28px;
      font-size: 20px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      color: #00baff;
      line-height: 28px;
      margin-bottom: 6px;
    }
    ::v-deep .el-checkbox-group {
      .el-checkbox {
        .el-checkbox__input {
          .el-checkbox__inner {
            width: 16px;
            height: 16px;
            border-radius: 2px;
            border: 1px solid #d9d9d9;
            background-color: transparent;
            &::after {
              border-color: #0de9ff;
              left: 5px;
              top: 2px;
            }
          }
        }
        .el-checkbox__label {
          height: 22px;
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #ffffff;
          line-height: 22px;
        }
      }
      .el-checkbox.is-checked {
        .el-checkbox__input.is-checked {
          .el-checkbox__inner {
            border: 1px solid #0de9ff;
          }
        }
        .el-checkbox__label {
          color: #0de9ff;
        }
      }
    }
  }
  .rainstorm_file_btn {
    width: 72px;
    height: 72px;
    margin-right: 32px;
    text-align: center;
    cursor: pointer;
    background-size: 100% 100%;
    &.open {
      background-image: url(~@/assets/new_images/rainstorm-file-btn-unfold.png);
    }
    &.default {
      background-image: url(~@/assets/new_images/rainstorm-file-btn-fold.png);
    }
  }
  .project_catalogue_btn {
    width: 72px;
    height: 72px;
    margin-right: 48px;
    text-align: center;
    cursor: pointer;
    background-size: 100% 100%;
    &.open {
      background-image: url(~@/assets/new_images/project-directory-btn-unfold.png);
    }
    &.default {
      background-image: url(~@/assets/new_images/project-directory-btn-fold.png);
    }
  }
  .project_catalogue_content {
    z-index: 2;
    position: absolute;
    top: 100%;
    right: 0;
    transform: translateY(3px);
    box-sizing: border-box;
    width: 216px;
    padding: 8px;
    border: 8px solid #ddd;
    border-image: url(~@/assets/new_images/project-directory-bgi.png) 8 repeat;
    background-color: rgba(2, 22, 45, 0.75);
    i {
      position: absolute;
      border-width: 4px;
      border-style: solid;
      width: 0;
      height: 0;
      &:nth-child(1) {
        left: -8px;
        top: -8px;
        border-left-color: #00c4ff;
        border-top-color: #00c4ff;
        border-right-color: transparent;
        border-bottom-color: transparent;
      }
      &:nth-child(2) {
        top: -8px;
        right: -8px;
        border-left-color: transparent;
        border-top-color: #00c4ff;
        border-right-color: #00c4ff;
        border-bottom-color: transparent;
      }
      &:nth-child(3) {
        right: -8px;
        bottom: -8px;
        border-left-color: transparent;
        border-top-color: transparent;
        border-right-color: #00c4ff;
        border-bottom-color: #00c4ff;
      }
      &:nth-child(4) {
        left: -8px;
        bottom: -8px;
        border-left-color: #00c4ff;
        border-top-color: transparent;
        border-right-color: transparent;
        border-bottom-color: #00c4ff;
      }
    }
    ::v-deep .el-input {
      box-sizing: border-box;
      height: 32px;
      box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
      border-radius: 2px;
      border: 1px solid #155bd4;
      .el-input__inner {
        height: 100%;
        text-align: left;
        padding: 0 10px;
        line-height: 30px;
      }
    }
    ul.project_catalogue_list {
      overflow: auto;
      margin-top: 7px;
      background-color: #02162c;
      li {
        height: 40px;
        padding-left: 20px;
        &:nth-child(odd) {
          background-image: linear-gradient(to right, rgba(255, 255, 255, 0.2), rgba(249, 249, 249, 0));
        }
        &.checked {
          background-image: linear-gradient(to right, rgba(44, 158, 191, 0.3), rgba(41, 147, 180, 0.1));
        }
        ::v-deep .el-checkbox {
          display: flex;
          align-items: center;
          height: 40px;
          line-height: 40px;
          .el-checkbox__input {
            .el-checkbox__inner {
              width: 16px;
              height: 16px;
              border-radius: 2px;
              border: 1px solid #d9d9d9;
              background-color: transparent;
              &::after {
                border-color: #0de9ff;
                left: 5px;
                top: 2px;
              }
            }
          }
          .el-checkbox__label {
            overflow: hidden;
            display: inline-block;
            text-overflow: ellipsis;
            white-space: nowrap;
            width: calc(100% - 16px);
            height: 18px;
            font-size: 16px;
            font-family: PingFangSC-Medium, PingFang SC;
            font-weight: 500;
            color: #ffffff;
            line-height: 18px;
            padding-left: 23px;
          }
        }
        ::v-deep .el-checkbox.is-checked {
          .el-checkbox__input.is-checked {
            .el-checkbox__inner {
              border: 1px solid #0de9ff;
            }
          }
          .el-checkbox__label {
            color: #0de9ff;
          }
        }
      }
    }
  }
}
</style>
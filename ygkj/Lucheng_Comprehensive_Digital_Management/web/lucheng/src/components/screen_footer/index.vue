<template>
  <div class="map-footer">
    <div class="map-footer__action">
      <div class="map-footer__action_item">
        <el-popover
          popperClass="footer_action-popover"
          placement="top"
          width="256"
          ref="scenePop"
          trigger="manual"
          v-model="visible"
          :visibleArrow="false"
        >
          <div class="scenes-box">
            <ul class="scenes-list">
              <li :class="{ active: !activeItemId }" @click="checkScenes('', 1)">
                <span title="鹿城一张图">鹿城一张图</span>
              </li>
              <li
                :class="{ active: activeItemId === item.id }"
                v-for="item in sceneList"
                :key="item.id"
                @click="checkScenes(item)"
              >
                <span :title="item.sceneName">{{ item.sceneName }}</span>
                <i v-if="showDelete(item.id)" @click="deleteScene(item.id)" class="el-icon-delete" />
              </li>
            </ul>
            <div class="scenes-func" v-if="activeItemId">
              <i title="添加新场景" @click="clickSetAddcardDialog" class="icon-scene-add" />
              <i title="自动对齐" @click="clickSetAligning" class="icon-auto-align" />
              <i title="c位" @click="clickSetStatisticFlag" class="icon-scene-statistic-show" />
              <i title="保存" @click="doSave" class="icon-scene-save" />
              <i title="另存为" @click="clickSetSaveAsDialog" class="icon-scene-saveas" />
            </div>
          </div>
          <template slot="reference">
            <div
              class="map-footer__action_button"
              @click="golink('/aMapOfLucheng', 3)"
              :class="{ active: activeItem === 3 }"
            >
              <i class="footer__action_item_03"></i>
              <span>鹿城一张图</span>
            </div>
          </template>
        </el-popover>
      </div>
      <div class="map-footer__action_item">
        <div
          class="map-footer__action_button"
          @click="golink('/projectScheduling', 1)"
          :class="{ active: activeItem === 1 }"
        >
          <i class="footer__action_item_01"></i>
          <span>工程调度图</span>
        </div>
      </div>
      <div class="map-footer__action_item">
        <div
          class="map-footer__action_button"
          @click="golink('/projectVisualization', 2)"
          :class="{ active: activeItem === 2 }"
        >
          <i class="footer__action_item_02"></i>
          <span>工程可视化</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import { mapActions, mapGetters } from 'vuex';
export default {
  data() {
    return {
      activeItem: '',
      visible: false,
      sceneList: [],
      activeItemId: ''
    };
  },
  computed: {
    ...mapGetters(['StatisticFlag', 'SaveAsDialog', 'ScreenMap'])
  },
  watch: {
    SaveAsDialog(val) {
      if (!val) {
        this.listScene();
      }
    }
  },
  mounted() {
    if (this.$route.path === '/projectScheduling') {
      this.activeItem = 1;
    } else if (this.$route.path === '/projectVisualization') {
      this.activeItem = 2;
    } else {
      this.activeItem = 3;
    }
    this.listScene();
  },
  methods: {
    ...mapActions([
      'SetAddcardDialog',
      'SetStatisticFlag',
      'SetSaveAsDialog',
      'SetActiveScene',
      'GetSceneComponentInfo',
      'SetDoSceneSave',
      'SetAligning'
    ]),
    showDelete(id) {
      let ids = [
        '175751117781598208',
        '175764862754881536',
        '175773070911340544',
        '175773583790833664',
        '175773583790833665'
      ];
      let showType = ids.includes(id);
      return !showType;
    },
    clickSetSaveAsDialog() {
      this.visible = false;
      this.SetSaveAsDialog(true);
    },
    clickSetStatisticFlag() {
      this.visible = false;
      this.SetStatisticFlag(!this.StatisticFlag);
    },
    clickSetAligning() {
      this.visible = false;
      this.SetAligning(true);
    },
    clickSetAddcardDialog() {
      this.visible = false;
      this.SetAddcardDialog(true);
    },
    golink(link, num) {
      if (num === this.activeItem) {
        if (num == 3) {
          this.visible = !this.visible;
        }
        return false;
      }
      this.visible = false;
      this.activeItem = num;
      this.$router.push(link);
    },
    listScene() {
      disasterPreventionApi.listScene().then((res) => {
        if (res.code == 0) {
          this.sceneList = res.data;
          if (this.$route.path === '/waterDisasterCustom') {
            this.activeItemId = this.sceneList[0].id;
          }
        }
      });
    },
    deleteScene(id) {
      disasterPreventionApi.deleteScene(id).then((res) => {
        if (res.code == 0) {
          this.listScene();
        }
      });
    },
    checkScenes(item, type) {
      this.visible = false;
      if (type) {
        this.activeItemId = '';
        this.$nextTick(() => {
          this.$router.push('/aMapOfLucheng');
        });
        return false;
      }
      this.$router.push('/waterDisasterCustom');
      this.$nextTick(() => {
        this.activeItemId = item.id;
        let { id, editable, sceneName } = item;
        // this.status = sceneName;
        const sceneObj = { name: sceneName, id, editable };
        //切换当前场景
        this.SetActiveScene(sceneObj);
        //如果是大屏场景 设置成大屏尺寸
        // const bigScreenList = ['防御态势', '防洪调度'];
        // this.SetBigScreen(bigScreenList.includes(this.status));
        //获取当前场景的组件信息
        const mode = this.qiankunModule ? 2 : 1;
        const param = { bsName: '', id, mode };
        this.GetSceneComponentInfo(param).then(() => {
          if (!this.CheckedArr.includes('台风')) {
            this.ScreenMap.clearAllTyphoon && this.ScreenMap.clearAllTyphoon();
          }
        });
      });
    },
    doSave() {
      this.visible = false;
      this.SetDoSceneSave(this.activeItemId);
    },
    async doSaveAs(id) {
      const unitList = this.getCardInfo();
      let opt = {
        bsName: this.BsName,
        unitList,
        mode: this.qiankunModule ? 2 : 1
      };
      if (id) {
        opt.id = id;
        opt.sceneName = this.ActiveScene.name;
      } else {
        if (!this.sceneName) {
          this.$message({
            type: 'warning',
            message: '请输入场景名称'
          });
          return;
        }
        opt.sceneName = this.sceneName;
      }
      let res = await disasterPreventionApi.addOrUpdateScene(opt);
      if (res.code == 0) {
        this.$message({
          type: 'success',
          message: '保存成功'
        });
        this.SetSaveAsDialog(false);
        this.sceneName = '';
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.map-footer {
  position: absolute;
  width: 100%;
  height: 95px;
  bottom: 0;
  display: flex;
  background-image: url(~assets/images/ScreenAsset/map-footer-bg.png);
  background-size: 100% 100%;
  background-repeat: no-repeat;
  justify-content: center;
  z-index: 2;
  .map-footer__action {
    display: flex;
    background-repeat: no-repeat;
    align-items: flex-end;
    justify-content: space-between;
    height: 100%;
    z-index: 3;
    width: 936px;
    margin: 0 auto;
    padding: 0 178px 0 144px;
    box-sizing: border-box;
    .map-footer__action_item {
      padding-bottom: 35px;
      .map-footer__action_button {
        cursor: pointer;
        user-select: none;
        font-size: 24px;
        color: #fff;
        display: flex;
        justify-content: center;
        align-items: center;
        box-sizing: border-box;
        outline: 0 !important;
        @include text-ellipsis;
        i {
          margin-right: 10px;
          background-size: 100% 100%;
          background-repeat: no-repeat;
        }
        .footer__action_item_01 {
          width: 20px;
          height: 20px;
          background-image: url(~assets/images/ScreenAsset/footer__action_item_01.png);
        }
        .footer__action_item_02 {
          width: 21px;
          height: 19px;

          background-image: url(~assets/images/ScreenAsset/footer__action_item_02.png);
        }
        .footer__action_item_03 {
          width: 24px;
          height: 24px;
          margin-right: 10px;
          background-image: url(~assets/images/ScreenAsset/footer__action_item_03.png);
        }
        &.active {
          color: #23e3fb;
          .footer__action_item_01 {
            background-image: url(~assets/images/ScreenAsset/footer__action_item_01_active.png);
          }
          .footer__action_item_02 {
            background-image: url(~assets/images/ScreenAsset/footer__action_item_02_active.png);
          }
          .footer__action_item_03 {
            background-image: url(~assets/images/ScreenAsset/footer__action_item_03_active.png);
          }
        }
        &:hover {
          color: #23e3fb;
          .footer__action_item_01 {
            background-image: url(~assets/images/ScreenAsset/footer__action_item_01_active.png);
          }
          .footer__action_item_02 {
            background-image: url(~assets/images/ScreenAsset/footer__action_item_02_active.png);
          }
          .footer__action_item_03 {
            background-image: url(~assets/images/ScreenAsset/footer__action_item_03_active.png);
          }
        }
      }
    }
  }
}
</style>
<style lang="scss">
.footer_action-popover {
  border: none;
  box-shadow: inset 0px 0px 40px 0px rgba(63, 150, 251, 0.24);
  border-radius: 4px;
  background: rgba(0, 0, 0, 0) url(~assets/images/custom_card/popover-bg.png) no-repeat center center;
  background-size: 100% 100%;
  .el-radio-group {
    margin-left: 10px;
    padding-bottom: 12px;
    .el-radio {
      margin-bottom: 10px;
      margin-right: 0;
      &:last-child {
        margin-bottom: 0;
      }
      .el-radio__input {
        .el-radio__inner {
          background-color: transparent;
          border: 1px solid #3085a1;
          border-radius: 0;
        }
      }
      .el-radio__label {
        color: #fff;
        font-size: 12px;
      }
    }
  }
  .scenes-box {
    color: #fff;
    .scenes-list {
      max-height: 180px;
      overflow-y: auto;
      li {
        padding: 0 40px 12px 0;
        @include text-ellipsis;
        position: relative;
        color: #ffffff;
        > span {
          cursor: pointer;
        }
        i {
          position: absolute;
          width: 14px;
          height: 14px;
          cursor: pointer;
          top: 3px;
          &:nth-child(3) {
            right: 20px;
          }
          &:nth-child(2) {
            right: 0px;
          }
        }
        &.active {
          color: #00baff;
        }
      }
    }
    .scenes-func {
      border-top: 1px solid rgba(255, 255, 255, 0.4);
      padding: 8px 12px 6px;
      @include flexbox;
      @include flexJC(space-between);
      i {
        cursor: pointer;
        &.disabled {
          opacity: 0.7;
        }
      }
    }
  }
  .toolbox {
    display: flex;
    flex-direction: column;
    .toolbox-item {
      color: #fff;
      user-select: none;
      margin-bottom: 8px;
      &:last-child {
        margin-bottom: 0;
      }
      i {
        margin-right: 3px;
      }
      .action-label {
        margin-right: 3px;
      }
      .action-list {
        a {
          cursor: pointer;
          color: #fff;
          margin-right: 3px;
          &.select-active {
            color: #56fefe;
          }
        }
      }
    }
  }
}
.action-icon {
  margin: 0 2px;
  /* height: 14px;
  width: 14px; */
}
</style>

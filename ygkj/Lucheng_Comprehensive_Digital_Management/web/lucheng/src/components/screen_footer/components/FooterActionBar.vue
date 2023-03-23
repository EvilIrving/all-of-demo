<script>
import { mapActions, mapGetters } from 'vuex';
import { weatherPredict } from '@/api/hydraulic_amap/weather';
export default {
  name: 'FooterActionBar',
  render() {
    let actionItemToolbox = this.actionItemToolbox();
    let actionItemMaptype = this.actionItemMaptype();
    let actionItemScenes = this.actionItemScenes();
    let waterDistributionMap = this.waterDistributionMap();
    return (
      <div class="map-footer__action">
        {actionItemToolbox}
        {actionItemMaptype}
        {actionItemScenes}
      </div>
    );
  },
  data() {
    return {
      basin: '全流域',
      areaList: [
        '温州市',
        '鹿城区',
        '瓯海区',
        '龙湾区',
        '洞头区',
        '瑞安市',
        '乐清市',
        '龙港市',
        '泰顺县',
        '文成县',
        '永嘉县',
        '苍南县',
        '平阳县',
        '温州市区'
      ],
      mapType: 'zhejiangSLLayer',
      activeItem: '',
      status: '选择场景',
      windFlag: false,
      waveFlag: false,
      cloundFlag: false,
      radarFlag: false,
      windTimes: [],
      windDatas: null,
      waveTimes: [],
      waveDatas: null,
      distributionMapShow: false,
      toolboxSelectedList: []
    };
  },
  computed: {
    ...mapGetters([
      'ScreenMap',
      'currentSelectArea',
      'currentSelectBasin',
      'mapChangeModule',
      'SceneList',
      'ActiveScene',
      'CheckedArr',
      'StatisticFlag',
      'BsName',
      'RadarRainList',
      'Simple',
      'BigSceneName',
      'sluiceDistributionMap'
    ]),
    //判断微应用模式还是直接访问模式
    qiankunModule() {
      return window.__POWERED_BY_QIANKUN__;
    },
    systemType() {
      // let type;
      // if(this.$microStore.state.systemType){
      //   type = this.$microStore.state.systemType;
      // }else{
      //   type = this.$localData("get","systemType");
      // }
      return this.$microStore ? this.$microStore.state.systemType : '';
    }
  },
  watch: {
    systemType() {
      this.SetBsName(val);
      const bsName = this.BsName;
      const mode = this.qiankunModule ? 2 : 1;
      const opt = { bsName, mode };
      //获取当前大屏场景列表
      this.GetSceneList(opt).then((res) => {
        let defaultScene = {
          name: res[0].sceneName,
          id: res[0].id,
          editable: res[0].editable
        };
        res.forEach((item) => {
          //PC模式下默认加载个人设置的场景
          if (item.userDefaultScene) {
            defaultScene = {
              id: item.id,
              name: item.sceneName,
              editable: item.editable
            };
          }
        });
        this.SetActiveScene(defaultScene);
        //获取当前场景组件信息列表
        const param = {
          bsName: this.BsName,
          id: defaultScene.id,
          mode
        };
        this.GetSceneComponentInfo(param).then((result) => {
          this.SetActiveCount(result[0]);
        });
      });
    }
  },
  mounted() {
    this.getWindTimes();
    this.getWaveTimes();
  },
  methods: {
    ...mapActions([
      'changeArea',
      'changeBasin',
      'SetAddcardDialog',
      'SetActiveScene',
      'SetSaveAsDialog',
      'deleteScene',
      'GetSceneComponentInfo',
      'SetStatisticFlag',
      'SetBigScreen',
      'SetBsName',
      'GetSceneList',
      'SetActiveCount',
      'SetRadarRainList'
    ]),
    actionItemBasin() {
      let actionClass = ['action-item-button', this.activeItem == 'basin' && 'active'];
      let basinCommand = (value) => {
        if (this.mapChangeModule == 'map') {
          this.changeBasin(value);
          this.$emit('basinCommand', value);
        }
      };
      let basinName = this.currentSelectBasin;
      return (
        <div class="map-footer__action--item">
          <el-popover
            popperClass="action-popover"
            placement="top"
            width="126"
            trigger="click"
            visibleArrow={false}
            on-after-leave={() => {
              this.activeItem = '';
            }}
          >
            <el-radio-group value={basinName} onInput={basinCommand}>
              {['飞云江流域', '鳌江流域', '瓯江流域', '全流域'].map((item) => (
                <el-radio label={item} />
              ))}
            </el-radio-group>

            <template slot="reference">
              <div
                class={actionClass}
                onClick={() => {
                  this.activeItem = this.activeItem == 'basin' ? '' : 'basin';
                }}
              >
                <i class={['icon-basin', 'action-icon']} />
                流域网格
              </div>
            </template>
          </el-popover>
        </div>
      );
    },
    actionItemArea() {
      let actionClass = ['action-item-button', this.activeItem == 'area' && 'active'];
      let areaChange = (value) => {
        this.changeArea(value);
        this.$emit('showAreaLine', value);
      };
      let areaName = this.currentSelectArea;
      return (
        <div class="map-footer__action--item">
          <el-popover
            popperClass="action-popover"
            placement="top"
            width="126"
            trigger="click"
            visibleArrow={false}
            on-after-leave={() => {
              this.activeItem = '';
            }}
          >
            <el-radio-group value={areaName} onInput={areaChange}>
              {this.areaList.map((item) => {
                return <el-radio label={item} />;
              })}
            </el-radio-group>

            <template slot="reference">
              <div
                class={actionClass}
                onClick={() => {
                  this.activeItem = this.activeItem == 'area' ? '' : 'area';
                }}
              >
                <i class={['icon-location', 'action-icon']} />
                {areaName}
              </div>
            </template>
          </el-popover>
        </div>
      );
    },
    //  工具箱
    actionItemToolbox() {
      let actionClass = ['action-item-button', this.activeItem == 'toolbox' && 'active'];
      let toggleSelect = (item) => {
        if (this.toolboxSelectedList.includes(item)) {
          let idx = this.toolboxSelectedList.indexOf(item);
          this.toolboxSelectedList.splice(idx, 1);
        } else {
          this.toolboxSelectedList.push(item);
        }
      };
      let actionList = [
        {
          icon: 'el-icon-edit',
          label: '测量',
          action: [
            {
              label: '测距',
              methods: () => {
                // toggleSelect('测距');
                this.$parent.distanceMeasure();
                this.$refs.toolPop.doClose();
              }
            },
            {
              label: '测面',
              methods: () => {
                // toggleSelect('测面');
                this.$parent.areaMeasure();
                this.$refs.toolPop.doClose();
              }
            }
          ]
        },
        {
          icon: 'el-icon-delete',
          label: '清除',
          action: [
            {
              label: '清除测量',
              methods: () => {
                this.$parent.clearMeasure();
                this.$refs.toolPop.doClose();
              }
            },
            {
              label: '清除图层',
              methods: () => {
                this.$parent.clearLayer();
                this.$refs.toolPop.doClose();
              }
            }
          ]
        },
        {
          icon: 'el-icon-magic-stick',
          label: '分析',
          action: [
            {
              label: '框选',
              methods: () => {
                this.$parent.boxChoose();
                this.$refs.toolPop.doClose();
              }
            },
            {
              label: '圆选',
              methods: () => {
                this.$parent.circleChoose();
                this.$refs.toolPop.doClose();
              }
            },
            {
              label: '多边形选择',
              methods: () => {
                this.$parent.areaMeasure();
                this.$refs.toolPop.doClose();
                // this.$parent.polygonChoose();
              }
            }
          ]
        },
        {
          icon: 'el-icon-s-operation',
          label: '其他',
          action: [
            {
              label: '打印',
              methods: () => {
                this.$parent.print();
                this.$refs.toolPop.doClose();
              }
            },
            {
              label: '全屏',
              methods: () => {
                this.$parent.fullScreen();
                this.$refs.toolPop.doClose();
              }
            },
            {
              label: '全图',
              methods: () => {
                this.$parent.fullMap();
                this.$refs.toolPop.doClose();
                // this.$parent.polygonChoose();
              }
            }
          ]
        },
        // {
        //   icon: 'el-icon-picture-outline-round',
        //   label: '智能对比',
        //   action: [
        //     {
        //       label: 'CAD智能对比',
        //       methods: () => {
        //         this.$parent.smartContrast();
        //         this.$refs.toolPop.doClose();
        //       }
        //     }
        //   ]
        // },
        {
          icon: 'el-icon-partly-cloudy',
          label: '气象',
          action: [
            {
              label: '风场',
              methods: () => {
                toggleSelect('风场');
                this.windFlag = !this.windFlag;
                this.controlWind(this.windFlag);
              }
            },
            {
              label: '浪场',
              methods: () => {
                toggleSelect('浪场');
                this.waveFlag = !this.waveFlag;
                this.controlWave(this.waveFlag);
              }
            },
            {
              label: '云图',
              methods: () => {
                toggleSelect('云图');
                this.cloundFlag = !this.cloundFlag;
                this.loadClound(this.cloundFlag);
              }
            },
            {
              label: '雷达',
              methods: () => {
                toggleSelect('雷达');
                this.radarFlag = !this.radarFlag;
                this.radarRain(this.radarFlag);
              }
            }
          ]
        }
      ];
      return (
        <div class="map-footer__action--item">
          <el-popover
            popperClass="action-popover"
            placement="top"
            ref="toolPop"
            trigger="click"
            visibleArrow={false}
            on-after-leave={() => {
              this.activeItem = '';
            }}
          >
            <div class="toolbox">
              {actionList.map((item) => {
                return (
                  <div class="toolbox-item">
                    <i class={item.icon} />
                    <span>
                      {item.label ? <span class="action-label">{item.label}:</span> : null}
                      <span class="action-list">
                        {item.action.map((action) => {
                          return (
                            <a
                              class={this.toolboxSelectedList.indexOf(action.label) != -1 ? 'select-active' : ''}
                              onClick={action.methods}
                            >
                              {action.label}
                            </a>
                          );
                        })}
                      </span>
                    </span>
                  </div>
                );
              })}
            </div>
            <template slot="reference">
              <div
                class={actionClass}
                onClick={() => {
                  this.activeItem = this.activeItem == 'toolbox' ? '' : 'toolbox';
                }}
              >
                <i class={['icon-toolbox', 'action-icon']} />
                工具箱
              </div>
            </template>
          </el-popover>
        </div>
      );
    },
    // 地图切换
    actionItemMaptype() {
      let actionClass = ['action-item-button', this.activeItem == 'mapType' && 'active'];
      let mapTypeChange = (value) => {
        if (this.mapChangeModule == 'map') {
          this.mapType = value;
          this.$emit('mapTypeChange', value);
        }
      };
      return (
        <div class="map-footer__action--item">
          <el-popover
            popperClass="action-popover"
            placement="top"
            width="200"
            trigger="click"
            visibleArrow={false}
            on-after-leave={() => {
              this.activeItem = '';
            }}
          >
            <el-radio-group value={this.mapType} onInput={mapTypeChange}>
              <el-radio label="tiandituVec">天地图路网图</el-radio>
              <el-radio label="tiandituImg">天地图影像图</el-radio>
              <el-radio label="zhejiangSLLayer">浙江水利底图</el-radio>
              <el-radio label="zhejiangHTLayer">浙江海塘底图</el-radio>
            </el-radio-group>
            <template slot="reference">
              <div
                class={actionClass}
                onClick={() => {
                  this.activeItem = this.activeItem == 'mapType' ? '' : 'mapType';
                }}
              >
                <i class={['icon-map-type', 'action-icon']} />
                水利地图
              </div>
            </template>
          </el-popover>
        </div>
      );
      // return (
      //   <div class="map-footer__action--item">
      //     <el-popover
      //       popperClass="action-popover"
      //       placement="top"
      //       width="200"
      //       trigger="click"
      //       visibleArrow={false}
      //       on-after-leave={() => {
      //         this.activeItem = '';
      //       }}
      //     >
      //       <el-radio-group value={this.mapType} onInput={mapTypeChange}>
      //         <el-radio label="tiandituVec">天地图路网图</el-radio>
      //         <el-radio label="tiandituImg">天地图影像图</el-radio>
      //         <el-radio label="wnzhouImgLayer">温州市影像图</el-radio>
      //         <el-radio label="zhejiangSLLayer">浙江水利底图</el-radio>
      //         <el-radio label="wenzhouVecLinght">温州水行政区划矢量-亮色</el-radio>
      //         <el-radio label="wenzhouVecDark">温州水行政区划矢量-黑色</el-radio>
      //         <el-radio label="wenzhouVecWhite">温州水行政区划矢量-白色</el-radio>
      //       </el-radio-group>

      //       <template slot="reference">
      //         <div
      //           class={actionClass}
      //           onClick={() => {
      //             this.activeItem = this.activeItem == 'mapType' ? '' : 'mapType';
      //           }}
      //         >
      //           <i class={['icon-map-type', 'action-icon']} />
      //           水利地图
      //         </div>
      //       </template>
      //     </el-popover>
      //   </div>
      // );
    },
    actionItemScenes() {
      let actionClass = ['action-item-button', this.activeItem == 'scene' && 'active'];
      const sceneList = this.getSceneList();
      return (
        <div class="map-footer__action--item">
          <el-popover
            popperClass="action-popover"
            placement="top"
            width="256"
            ref="scenePop"
            trigger="click"
            visibleArrow={false}
          >
            <div class="scenes-box">
              <ul class="scenes-list">{sceneList}</ul>
              <div class="scenes-func">
                <i onClick={() => this.SetAddcardDialog(true)} title="添加新场景" class="icon-scene-add" />
                <i title="自动对齐" class="icon-auto-align" />
                <i
                  title="c位"
                  class={this.StatisticFlag ? 'icon-scene-statistic-show' : 'icon-scene-statistic-hide'}
                  onClick={() => {
                    this.SetStatisticFlag(!this.StatisticFlag);
                  }}
                />
                <i
                  onClick={() => {
                    this.doSave();
                  }}
                  title="保存"
                  class={['icon-scene-save', this.ActiveScene.editable ? '' : 'disabled']}
                />
                <i
                  onClick={() => {
                    this.openSaveAs();
                  }}
                  title="另存为"
                  class="icon-scene-saveas"
                />
              </div>
            </div>
            <template slot="reference">
              <div
                class={actionClass}
                onClick={() => {
                  this.activeItem = this.activeItem == 'scene' ? '' : 'scene';
                }}
              >
                <i class={['icon-map-status', 'action-icon']} />
                {this.status}
              </div>
            </template>
          </el-popover>
        </div>
      );
    },
    // 水利地图
    waterDistributionMap() {
      let actionClass = ['action-item-button', this.activeItem == 'waterMap' && 'active'];
      return (
        <div class="map-footer__action--item">
          <div
            class={actionClass}
            onClick={() => {
              this.activeItem = this.activeItem == 'waterMap' ? '' : 'waterMap';
              this.distributionMapShow = !this.distributionMapShow;
              this.$store.commit('SET_DISTRIBUTION_MAP', this.distributionMapShow);
            }}
          >
            <i class={['icon-map-status', 'action-icon']} />
            水闸分布图
          </div>
        </div>
      );
    },

    onSceneListItemClick(index) {
      this.$refs.scenePop.doClose();
      //自定义场景和老场景路由切换
      if (['/index'].includes(this.$route.path)) {
        if (!this.qiankunModule) {
          this.$router.push('/waterDisasterCustom?large=true&isBigScreen=false');
        } else {
          this.$router.push('/waterDisasterCustom?large=false&isBigScreen=false&bsType=waterDisaster');
        }
      }
      if (['/scheduleMap'].includes(this.$route.path) || ['/scheduleMapProject'].includes(this.$route.path)) {
        if (!this.qiankunModule) {
          this.$router.push('/disasterPreventionScheduling');
        } else {
          this.$router.push('/disasterPreventionScheduling?large=false&isBigScreen=false&bsType=waterDisaster');
        }
      }
      let { id, editable, sceneName } = this.SceneList[index];
      this.status = sceneName;
      const sceneObj = { name: sceneName, id, editable };

      //切换当前场景
      this.SetActiveScene(sceneObj);
      //如果是大屏场景 设置成大屏尺寸
      const bigScreenList = ['防御态势', '防洪调度'];
      this.SetBigScreen(bigScreenList.includes(this.status));

      //获取当前场景的组件信息
      const mode = this.qiankunModule ? 2 : 1;
      const param = { bsName: this.BsName, id, mode };
      this.GetSceneComponentInfo(param).then(() => {
        if (!this.CheckedArr.includes('台风')) {
          this.ScreenMap.clearAllTyphoon && this.ScreenMap.clearAllTyphoon();
        }
      });
    },
    setDelType(data) {
      switch (data.sceneName) {
        case '防潮预演预报':
          return false;
        case '防潮风险预警':
          return false;
        case '防潮抢险预案':
          return false;
        case '海塘防潮态势研判':
          return false;
        default:
          return true;
      }
    },
    //获取场景列表
    getSceneList() {
      this.status = this.ActiveScene.name;
      const renderDelBtn = (index) => (
        <i
          onClick={() => {
            this.doSceneDel(this.SceneList[index].id);
          }}
          class="el-icon-delete"
        />
      );
      let sceneList = [];
      //领导一直强调要将特殊场景添加默认场景后面 加判断
      let firstFlag = true;
      //特殊场景添加
      let special1 = (
        <li
          onClick={() => {
            this.SetBigScreen(true);
            this.SetActiveScene({ id: 'fyfp', name: '防御复盘' });
            this.$router.push('/index?large=true&sceneType=防御复盘');
            this.status = '防御复盘';
          }}
          class={this.ActiveScene.name == '防御复盘' ? 'active' : ''}
        >
          <span title="防御复盘">防御复盘(场景)</span>
        </li>
      );
      let special2 = (
        <li
          onClick={() => {
            this.SetActiveScene({ id: 'sjdd', name: '三江调度' });
            this.$router.push('/scheduleMap');
            this.status = '三江调度';
          }}
          class={this.ActiveScene.name == '三江调度' ? 'active' : ''}
        >
          <span title="三江调度">三江调度(场景)</span>
        </li>
      );
      let special3 = (
        <li
          onClick={() => {
            this.SetActiveScene({ id: 'lygcdd', name: '流域工程调度' });
            this.$router.push('/scheduleMap');
            this.status = '流域工程调度';
          }}
          class={this.ActiveScene.name == '流域工程调度' ? 'active' : ''}
        >
          <span title="流域工程调度">流域工程调度</span>
        </li>
      );
      let special4 = (
        <li
          onClick={() => {
            this.SetActiveScene({ id: 'gcght', name: '工程概化图' });
            this.$router.push('/scheduleMapProject');
            this.status = '工程概化图';
          }}
          class={this.ActiveScene.name == '工程概化图' ? 'active' : ''}
        >
          <span title="工程概化图">工程概化图</span>
        </li>
      );
      for (let i = 0; i < this.SceneList.length; i++) {
        const sceneLabel = `${this.SceneList[i].sceneName} ${this.SceneList[i].defaulted ? '(场景)' : ''}`;
        let editable = this.setDelType(this.SceneList[i]);
        sceneList.push(
          <li
            onClick={() => {
              this.onSceneListItemClick(i);
            }}
            class={this.ActiveScene.id == this.SceneList[i].id ? 'active' : ''}
          >
            <span title={sceneLabel}>{sceneLabel}</span>
            {this.SceneList[i].editable && editable ? renderDelBtn(i) : null}
          </li>
        );
      }
      if (this.BigSceneName == '灾害防御调度') {
        this.$store.commit('SET_DISTRIBUTION_MAP', false);
        sceneList.unshift([special4]);
        sceneList.push([special3]);
      }
      if (this.BsName == '水灾害防御') {
        if (!this.qiankunModule) {
          sceneList.push([special1, special2]);
        } else {
          sceneList.push([special2]);
        }
      }
      return sceneList;
    },
    //删除场景
    doSceneDel(id) {
      this.$confirm('是否执行场景的删除操作?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteScene(id).then((res) => {
          if (res.code == 0) {
            this.getSceneList();
            this.$message({
              type: 'success',
              message: '删除成功'
            });
          }
        });
      });
    },
    //保存当前场景
    doSave() {
      this.$emit('doSceneSave');
    },
    //打开另存为弹框
    openSaveAs() {
      this.SetSaveAsDialog(true);
    },

    //风场时间轴
    async getWindTimes() {
      let res = await weatherPredict.windFarmsTimeList();
      this.windTimes = res.data;
    },
    //风场数据
    async getWindDatas(id) {
      //获取风场数据
      let res = await weatherPredict.getWindDataList(id);
      if (res.code == 0) {
        //解析风场数据，获取风场字符串
        // console.log(res, "===================");
        let timeDataArr = res.data.predictTime.split(' ');
        let timeStr = timeDataArr[0].replace(new RegExp('-', 'g'), '') + timeDataArr[1].slice(0, 2);
        this.windDatas = JSON.parse(res.data.dataJson)[timeStr];
      }
    },
    //浪场时间轴
    async getWaveTimes() {
      let res = await weatherPredict.waveFieldList();
      this.waveTimes = res.data;
    },
    //浪场数据
    async getWaveDatas(id) {
      //获取浪场数据
      let res = await weatherPredict.getWaveDataList(id);
      //解析浪场数据，获取浪场字符串
      // let timeDataArr = res.data.predictTime.split(" ");
      // let timeStr = timeDataArr[0].replace(new RegExp("-","g"),"") + timeDataArr[1].slice(0,2);
      // this.waveDatas = JSON.parse(res.data.dataJson)[timeStr];
      this.waveDatas = JSON.parse(res.data.dataJson).data.data;
    },
    //风场控制
    controlWind(isShow) {
      if (isShow) {
        this.getWindDatas(this.windTimes[0].id);
        let setInterF = setInterval(() => {
          if (this.windDatas != null) {
            this.ScreenMap.addWatherLayer(this.windDatas, 'wind');
            clearInterval(setInterF);
          }
        }, 2000);
      } else {
        this.ScreenMap.closeWeatherLayers('wind');
      }
    },
    controlWave(isShow) {
      if (isShow) {
        this.getWaveDatas(this.waveTimes[0].id);
        let setInterL = setInterval(() => {
          // console.log(this.windDatas, "this.windDatas");
          if (this.waveDatas != null) {
            this.ScreenMap.addWatherLayer(this.waveDatas, 'wave');
            clearInterval(setInterL);
          }
        }, 2000);
      } else {
        this.ScreenMap.closeWeatherLayers('wave');
      }
    },
    //卫星云图
    async loadClound(isShow) {
      if (isShow) {
        let res = await weatherPredict.getWenzhouCloudData();
        if (res.code == 0 && res.data.length > 0) {
          let imgUrl = res.data[res.data.length - 1].url;
          this.ScreenMap.loadCloundImg(imgUrl);
        }
      } else {
        this.ScreenMap.removeLayerByName('cloundLayer');
      }
    },
    //雷达降雨
    async radarRain(isShow) {
      if (isShow) {
        let res = await weatherPredict.getWenzhouRadarData();
        if (res.code == 0 && res.data.length > 0) {
          this.SetRadarRainList(res.data);
          let imgUrl = res.data[0].url;
          this.ScreenMap.radarLineShow = true;
          this.ScreenMap.loadRadarRainImg(imgUrl);
        }
      } else {
        this.ScreenMap.radarLineShow = false;
        this.ScreenMap.removeLayerByName('radarRainLayer');
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.map-footer__action {
  display: flex;
  background-repeat: no-repeat;
  align-items: flex-end;
  justify-content: space-between;
  height: 100%;
  z-index: 1;
  width: 936px;
  margin: 0 auto;
  padding: 0 178px 0 144px;
  box-sizing: border-box;
  .map-footer__action--item {
    padding-bottom: 20px;
    .action-item-button {
      cursor: pointer;
      user-select: none;
      font-size: 24px;
      color: #fff;
      display: flex;
      justify-content: center;
      align-items: center;
      padding-bottom: 10px;
      box-sizing: border-box;
      outline: 0 !important;
      @include text-ellipsis;
      &.active {
        color: #23e3fb;
      }
      &:hover {
        color: #23e3fb;
      }
    }
  }
}
</style>
<style lang="scss">
.action-popover {
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
      max-height: 200px;
      overflow-y: auto;
      border-bottom: 1px solid rgba(255, 255, 255, 0.4);
      li {
        padding: 0 40px 12px 0;
        @include text-ellipsis;
        position: relative;
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
          span {
            color: #00baff;
          }
        }
      }
    }
    .scenes-func {
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
<style lang="less">
@import '../../../style/spriteConfig.less';
.map-footer__action--item {
  .action-item-button:hover,
  .action-item-button.active {
    .icon-basin {
      .icon-basin-toggle();
    }
    .icon-location {
      .icon-location-toggle();
    }
    .icon-toolbox {
      .icon-toolbox-toggle();
    }
    .icon-map-type {
      .icon-map-type-toggle();
    }
    .icon-map-status {
      .icon-map-status-toggle();
    }
  }
}
</style>

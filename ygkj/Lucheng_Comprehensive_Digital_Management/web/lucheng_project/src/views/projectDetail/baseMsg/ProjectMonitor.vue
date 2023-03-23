<!--
 * @Author: hanyu
 * @LastEditTime: 2022-07-06 14:52:04
 * @Description: 工程监控
 * @FilePath: \lucheng_project\src\views\projectDetail\baseMsg\ProjectMonitor.vue
-->
<template>
  <div class="building_wrap">
    <div class="primary-view file_manage_index-page">
      <el-tabs v-model="selectTab" @tab-click="tabClick">
        <el-tab-pane
          v-for="(item, index) in tabList"
          :label="item.name"
          :name="item.name"
          :key="index"
        >
        </el-tab-pane>
      </el-tabs>
      <div class="view-content with_tab">
        <div
          class="info_option"
          style="
            width: 100%;
            padding: 25px 16px 16px 16px;
            box-sizing: border-box;
          "
        >
          <span style="font-size: 14px; margin-left: 15px">名称：</span>
          <el-input
            placeholder="请输入"
            prefix-icon="el-icon-search"
            @change="handerChangeSearch"
            v-model="stName"
            style="width: 240px; margin-right: 15px"
            clearable
          />
          <el-button type="primary" v-permission="'projectMonitor:relevance'" class="right" @click="connectStation()"
            >关联</el-button
          >
        </div>
        <div class="tb_wrapper">
          <el-table :data="siteList" border style="width: 100%">
            <el-table-column
              type="index"
              align="center"
              width="50"
              label="序号"
            ></el-table-column>
            <el-table-column
              prop="stcd"
              label="编码"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="stName"
              label="名称"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="cityAdnm"
              label="所在市"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="countryAdnm"
              label="所在区/县"
              align="center"
            ></el-table-column>
            <el-table-column
              prop="isMain"
              label="主测站"
              align="center"
              v-if="this.stType == 'TT'"
            >
              <template slot-scope="scope">
                <el-popconfirm
                  title="你确定设置该站点为主测站？"
                  icon="el-icon-info"
                  @onConfirm="confirmMain(scope.row)"
                >
                  <el-button
                    slot="reference"
                    class="config-btn"
                    :icon="mainStationIcon(scope.row)"
                    type="text"
                  />
                </el-popconfirm> </template
            ></el-table-column>
            <el-table-column align="center" label="操作" width="150">
              <template slot-scope="scope">
                <div class="deal_box">
                  <span
                    class="deal_btn deal_btn_look"
                    @click="delTable(scope.row)"
                    v-permission="'projectMonitor:relieve'"
                    >解除</span
                  >
                </div>
              </template>
            </el-table-column>
          </el-table>
          <pagination
            :total="pageTotal"
            :page.sync="pageNum"
            :limit.sync="pageSize"
            :page-sizes="[10, 20, 30, 40]"
            @pagination="getSiteList"
          />
        </div>
      </div>
    </div>
    <el-dialog
      title="关联测站"
      :visible.sync="showConnectDialog"
      @closeConnectDialog="closeConnectDialog"
      width="960px"
    >
      <div class="info_option" style="width: 100%; box-sizing: border-box">
        <span style="font-size: 14px; margin-left: 15px">名称：</span>
        <el-input
          placeholder="请输入"
          prefix-icon="el-icon-search"
          @change="handerChangeConnectSearch"
          v-model="stConnectName"
          style="width: 240px; margin-right: 15px"
          clearable
        />
      </div>
      <div class="tb_wrapper">
        <el-table :data="connectList" border style="width: 100%">
          <el-table-column
            type="index"
            align="center"
            width="50"
            label="序号"
          ></el-table-column>
          <el-table-column
            prop="stName"
            label="名称"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="cityAdnm"
            label="所在市"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="countryAdnm"
            label="所在区/县"
            align="center"
          ></el-table-column>
          <el-table-column align="center" label="关联测站" width="150">
            <template #header>
              <span>
                <slot>
                  <el-row>
                    <el-col>
                      <el-button
                        @click="checkAll()"
                        :icon="displayIcon()"
                        type="text"
                      />
                      <span style="bottom: 4px; position: relative"
                        >关联测站</span
                      >
                    </el-col>
                  </el-row>
                  <!-- <p icon="icon-checkbox"/> -->
                </slot>
              </span>
            </template>
            <template slot-scope="scope">
              <el-button
                @click="confirmthis(scope.row)"
                slot="reference"
                class="config-btn"
                :icon="participateIcon(scope.row.bandFlag)"
                type="text"
              />
              <!-- <span slot="reference" class="list-item-operate"
                    >取消关联</span
                  > -->
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="pageConnectTotal"
          :page.sync="pageConnectNum"
          :limit.sync="pageConnectSize"
          :page-sizes="[10, 20, 30, 40]"
          @pagination="getConnectList"
        />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showConnectDialog = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { basMsgGcjkApi } from '@/api/seawallPrevent'
export default {
  name: 'ProjectMonitor',
  props: {},
  components: {},
  data() {
    return {
      display: false,
      tabList: [
        {
          name: '潮位站',
          stType: 'TT',
        },
        {
          name: '雨量站',
          stType: 'PP',
        },
        {
          name: '气象站',
          stType: 'MM',
        },
        {
          name: '视频点',
          stType: 'SC',
        },
        {
          name: '安全检测',
          stType: 'SM',
        },
      ],
      stType: '',
      selectTab: '',
      stName: '',
      stConnectName: '',
      pageTotal: 0,
      pageNum: 1,
      pageSize: 10,
      pageConnectTotal: 0,
      pageConnectNum: 1,
      pageConnectSize: 10,
      siteList: [],
      connectList: [],
      showConnectDialog: false,
      reqVo: {},
    }
  },
  computed: {
    seawallDetail() {
      return this.$store.state.seawallDetail
    },
  },
  mounted() {
    this.getStationNum()
    // this.selectTab = this.tabList[0].name
    // this.tabClick(this.tabList[0])
  },
  methods: {
    tabClick(val) {
      let index = this.tabList.findIndex((item) => item.name == val.name)
      this.selectTab = val.name
      this.stType = this.tabList[index].stType
      this.getSiteList()
    },
    handerChangeSearch() {
      this.pageNum = 1
      this.getSiteList()
    },
    handerChangeConnectSearch() {
      this.pageConnectNum = 1
      this.getConnectList()
    },
    async getStationNum() {
      // 再次调用时初始化到原始数据
      this.tabList = this.$options.data().tabList
      let res = await basMsgGcjkApi.stationNum(this.seawallDetail.prcd)
      if (res.code == 0) {
        this.tabList[0].name = this.tabList[0].name + '(' + res.data.ttNum + ')'
        this.tabList[1].name = this.tabList[1].name + '(' + res.data.ppNum + ')'
        this.tabList[2].name = this.tabList[2].name + '(' + res.data.mmNum + ')'
        this.tabList[3].name = this.tabList[3].name + '(' + res.data.scNum + ')'
        this.tabList[4].name = this.tabList[4].name + '(' + res.data.smNum + ')'
        this.selectTab = this.tabList[0].name
        this.tabClick(this.tabList[0])
        // this.stationOptions = res.data
        // if (res.data.length > 0) {
        //   this.station = this.stationOptions[0].stCode
        //   this.getStormSurgeList()
        // }
      }
    },
    async getSiteList() {
      let res = await basMsgGcjkApi.list({
        pageNum: this.pageNum,
        // isRainStation: 1,
        pageSize: this.pageSize,
        projectCode: this.seawallDetail.prcd,
        stName: this.stName,
        stType: this.stType,
        projectType: '1',
        bind: true,
      })
      if (res.code == 0) {
        this.siteList = res.data.list
        this.pageTotal = res.data.totalRows
        // this.stationOptions = res.data
        // if (res.data.length > 0) {
        //   this.station = this.stationOptions[0].stCode
        //   this.getStormSurgeList()
        // }
      }
    },
    async getConnectList() {
      let res = await basMsgGcjkApi.list({
        pageNum: this.pageConnectNum,
        // isRainStation: 1,
        pageSize: this.pageConnectSize,
        projectCode: this.seawallDetail.prcd,
        stName: this.stConnectName,
        stType: this.stType,
        projectType: '1',
        bind: false,
      })
      if (res.code == 0) {
        this.connectList = res.data.list
        this.pageConnectTotal = res.data.totalRows
        res.data.list.map(function (item) {
          if (item.projectCode) {
            item.bandFlag = '1'
          } else {
            item.bandFlag = '0'
          }
        })
        // console.log(this.connectList)
        // this.stationOptions = res.data
        // if (res.data.length > 0) {
        //   this.station = this.stationOptions[0].stCode
        //   this.getStormSurgeList()
        // }
      }
    },
    mainStationIcon(row) {
      return row.isMain == '1'
        ? 'icon-radiobox is-checked'
        : 'icon-radiobox is-showColor'
    },
    // 全选图标切换
    displayIcon() {
      return this.display ? 'icon-checkbox is-checked' : 'icon-checkbox'
    },
    // 全选
    checkAll() {
      this.display = !this.display
      // console.log('orz', this.display)
      if (this.display == true) {
        this.connectList.map((item) => {
          item.bandFlag = '1'
        })
        // this.participateIcon()
        this.connectList.splice(1, 0)
        // console.log(this.connectList)
        // this.arr = []
        // // console.log(this.bandFlag,888)
        // for (let i = 0; i < this.bandFlag.length; i++) {
        //   this.tableData[i].bandFlag = '1'
        //   this.bandFlag[i] = '1'
        //   this.arr.push(this.stCode[i])
        //   console.log(this.bandFlag[i])
        // }
        // // console.log(this.arr,888)
        // // this.arr.join(",");
        // // this.participateIcon();
        // //  this.arr=this.arr.join(",");
        // console.log(this.arr, '勾着')
      } else if (this.display == false) {
        this.connectList.map((item) => {
          item.bandFlag = '0'
        })
        this.connectList.splice(1, 0)
        // console.log(this.connectList)
        // for (let i = 0; i < this.bandFlag.length; i++) {
        //   this.tableData[i].bandFlag = '0'
        //   this.arr.splice(this.arr.indexOf(i.stCode), 1)
        // }

        // console.log(this.arr, '没勾上')
      }
    },
    /** 是否绑定 */
    confirmthis(row) {
      if (row.bandFlag == '1') {
        row.bandFlag = '0'
        // console.log(this.connectList)
        let bandFlag0 = []
        this.connectList.map((item) => {
          if (item.bandFlag == '0') {
            bandFlag0.push(item.bandFlag)
          }
        })
        if (bandFlag0.length !== this.connectList.length) {
          this.display = false
        }
        // console.log(bandFlag0)
        this.connectList.splice(1, 0)
        // this.arr.splice(this.arr.indexOf(row.stCode), 1);
        // console.log(this.arr, "取消勾选");
      } else {
        row.bandFlag = '1'
        let bandFlag1 = []
        this.connectList.map((item) => {
          if (item.bandFlag == '1') {
            bandFlag1.push(item.bandFlag)
          }
        })
        if (bandFlag1.length == this.connectList.length) {
          this.display = true
        }
        this.connectList.splice(1, 0)
        // this.arr.push(row.stCode);
        // // this.arr.join(",");
        // console.log(this.arr, "勾选");
      }
    },
    // 图标变化
    participateIcon(row) {
      // console.log(row,888)
      if (row == '1') {
        return 'icon-checkbox is-checked'
      } else return 'icon-checkbox'
    },
    /** 确认主测站 */
    async confirmMain(row) {
      if (row.isMain == 1) {
        row.isMain = 0
      } else {
        row.isMain = 1
      }
      let res = await basMsgGcjkApi.updateMainSt({
        isMain: row.isMain,
        projectCode: row.projectCode,
        projectType: row.projectType,
        stType: row.stType,
        stcd: row.stcd,
      })
      if (res.code == 0) {
        this.getSiteList()
      }
    },
    connectStation() {
      this.showConnectDialog = true
      this.getConnectList()
    },
    closeConnectDialog() {},
    async save() {
      this.reqVo.projectCode = this.seawallDetail.prcd
      this.reqVo.projectType = '1'
      this.reqVo.stType = this.stType
      this.reqVo.list = []
      let listObj = {}
      this.connectList.map((item) => {
        if (item.bandFlag == '1') {
          listObj = {
            isMain: item.isMain,
            projectCode: item.projectCode,
            projectType: item.projectType,
            stType: item.stType,
            stcd: item.stcd,
          }
          this.reqVo.list.push(listObj)
        }
      })
      // console.log(this.reqVo)
      // console.log(this.connectList)
      // this.reqVo = JSON.stringify(this.reqVo);
      // console.log(JSON.stringify(this.reqVo));

      let res = await basMsgGcjkApi.updateProjRel(this.reqVo)
      if (res.code !== 0) {
        this.$message.error('编辑工程站点绑定关系失败！')
      } else {
        this.$message.success('编辑工程站点绑定关系成功！')
      }
      // console.log(res);
      // 隐藏添加目录的对话框
      this.showConnectDialog = false
      // 重新获取目录列表数据
      this.getSiteList()
      // 重新获取测站数目数据
      this.getStationNum()
    },
    delTable(row) {
      this.$confirm('确认解除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        basMsgGcjkApi
          .delProjRel({
            isMain: row.isMain,
            projectCode: row.projectCode,
            projectType: row.projectType,
            stType: row.stType,
            stcd: row.stcd,
          })
          .then((res) => {
            if (res.code == 0) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.getSiteList()
            } else {
              this.$message({
                type: 'error',
                message: '删除失败！',
              })
            }
          })
      })
    },
  },
}
</script>

<style lang='scss' scoped>
@import '@/style/theme.scss';
.building_wrap {
  @include contentBox();
  height: 100%;
  .primary-view {
    width: 100%;
    height: 100%;
    position: relative;
    &.bg_white .view-content,
    &.bg_white .el-tabs {
      background: #fff;
      box-shadow: 0 0 0.125rem 0 rgb(14 31 53 / 12%),
        0 0.0625rem 0.25rem 0 rgb(14 31 53 / 6%);
    }
    .no_padding {
      padding: 0;
    }

    .el-tabs {
      width: 100%;
      padding-top: 10px;
      font-size: 14px;
      ::v-deep .el-tabs__header {
        padding: 0 26px;
        margin: 0;
      }
      ::v-deep .el-tabs__item {
        color: rgba(48, 49, 51, 0.5);
        &.is-active {
          color: #1492ff;
        }
      }
      .el-tabs__active-bar {
        background: #1492ff;
      }
      ::v-deep .el-tabs__content {
        width: 100%;
        .el-tab-pane {
          width: 100%;
          height: 100%;
        }
      }
    }
    .view-content {
      width: 100%;
      height: 100%;
      &.with_tab {
        height: calc(100% - 50px);
      }
    }
    .tab-list {
      font-weight: bold;
      display: flex;
      padding: 6px 22px 26px 0;
      font-size: 24px;
      color: rgba(102, 102, 102, 0.7);
      span {
        display: inline-block;
        margin-right: 24px;
        cursor: pointer;
        &:hover,
        &.active {
          color: #1492ff;
        }
      }
    }
    // .technical-support {
    //   position: absolute;
    //   bottom: 16px;
    //   width: calc(100% - 32px);
    //   font-size: 14px;
    //   color: #ffffff;
    //   text-align: center;
    // }
  }
}
</style>
<style lang="scss">
.icon-checkbox {
  box-sizing: border-box;
  position: relative;
  display: block;
  width: 20px;
  height: 20px;
  border: 2px solid;
  border-color: #1492ff;
  border-radius: 2px;
  &.is-checked {
    &:after {
      content: '';
      display: block;
      box-sizing: border-box;
      position: absolute;
      left: 3px;
      top: -2px;
      width: 6px;
      height: 10px;
      border-width: 0 2px 2px 0;
      border-color: #1492ff;
      border-style: solid;
      transform-origin: bottom left;
      transform: rotate(45deg);
    }
  }
}
.icon-radiobox {
  position: relative;
  display: block;
  box-sizing: border-box;
  width: 18px;
  height: 18px;
  border: 2px solid;
  border-radius: 100px;
  border-color: #1492ff;
  &.is-checked {
    &::after {
      content: '';
      position: absolute;
      width: 10px;
      height: 10px;
      background: #1492ff;
      top: 2px;
      left: 2px;
      display: block;
      box-sizing: border-box;
      border-radius: 100px;
    }
  }
  &.is-showColor {
    border-color: #a5abb1;
    &:hover {
      border-color: #1492ff;
    }
  }
}
.config-btn {
  padding: 0;
}
</style>
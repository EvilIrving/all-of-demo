<!--
 * @Author: wqn
 * @Date: 2022-09-13 10:02:35
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2023-01-29 16:30:08
 * @Description: 鹿城一张图
-->
<template>
  <div class="a_map_of_luncheng">
    <!-- 地图容器 -->
    <div
      class="map_container"
      id="aLuchengMapContainer"
      :class="SpotsFlag ? (SpotsFlagType == 0 ? 'cursorImg' : SpotsFlagType == 1 ? 'analysisImg' : 'rainfallImg') : ''"
    ></div>
    <div ref="hoverPopup">
      <!-- 海塘落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'seawallPointsLayers'">
        <div class="popup_map_hover">
          <span class="label">名称</span><span class="value">{{ featureValues.data.seawallName || '--' }}</span
          ><br />
          <span class="label">海塘长度</span><span class="value">{{ featureValues.data.seawallLength || '--' }}m</span
          ><br />
          <span class="label">防潮标准</span
          ><span class="value">{{ featureValues.data.designTideStandard || '--' }}年</span><br />
          <span class="label">所在位置</span><span class="value">{{ featureValues.data.village || '--' }}</span>
        </div>
      </template>
      <!-- 水库落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'reservoirPointsLayers'">
        <div class="popup_map_hover">
          <span class="label">名称</span><span class="value">{{ featureValues.data.resName || '--' }}</span
          ><br />
          <span class="label">实时水位</span
          ><span class="value" style="color: eec80b">{{ featureValues.data.waterLevel || '--' }}m</span><br />
          <span class="label">汛限水位</span
          ><span class="value">{{ featureValues.data.currentLimitLevel || '--' }}m</span><br />
          <span class="label">更新时间</span><span class="value">{{ featureValues.data.dataTime || '--' }}</span
          ><br />
          <span class="label">鉴定结论</span
          ><span class="value">{{ featureValues.data.safetyConclusion || '--' }}</span>
        </div>
      </template>
      <!-- 堤防落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'dikePointsLayers'">
        <div class="popup_map_hover">
          <span class="label">名称</span><span class="value">{{ featureValues.data.dikeName || '--' }}</span
          ><br />
          <span class="label">堤防类别</span><span class="value">{{ featureValues.data.dikeType || '--' }}</span
          ><br />
          <span class="label">堤防长度</span><span class="value">{{ featureValues.data.dikeLen || '--' }}m</span>
        </div>
      </template>
      <!-- 水闸落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'sluicePointsLayers'">
        <div class="popup_map_hover">
          <span class="label">名称</span><span class="value">{{ featureValues.data.wagaName || '--' }}</span
          ><br />
          <span class="label">水闸规模</span><span class="value">{{ featureValues.data.engScal || '--' }}</span
          ><br />
          <span class="label">最大过闸流量</span
          ><span class="value">{{ featureValues.data.desLockDisc || '--' }}m³/s</span>
        </div>
      </template>
      <!-- 雨情落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'rainfallPointsLayers'">
        <div class="popup_map_hover">
          <span class="label">名称</span><span class="value">{{ featureValues.data.stationName || '--' }}</span
          ><br />
          <span class="label">降雨量</span
          ><span class="value">{{ featureValues.data.drp === 0 ? 0 : featureValues.data.drp || '--' }}mm</span>
        </div>
      </template>
      <!-- 水情落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'waterRegimePointsLayers'">
        <div class="popup_map_hover">
          <template v-if="waterTypeSelected === '水库水位' || waterTypeSelected === '河道水位'">
            <span class="label">名称</span><span class="value">{{ featureValues.data.stName || '--' }}</span
            ><br />
            <span class="label">水位</span
            ><span class="value"
              >{{ featureValues.data.waterLevel === 0 ? 0 : featureValues.data.waterLevel || '--' }}m</span
            ><br />
            <span class="label">更新时间</span><span class="value">{{ featureValues.data.time || '--' }}m</span>
          </template>
          <template v-else-if="waterTypeSelected === '堰闸水位'">
            <span class="label">名称</span><span class="value">{{ featureValues.data.stName || '--' }}</span
            ><br />
            <span class="label">闸上水位</span
            ><span class="value">{{ featureValues.data.upz === 0 ? 0 : featureValues.data.upz || '--' }}m</span><br />
            <span class="label">闸下水位</span
            ><span class="value">{{ featureValues.data.dwz === 0 ? 0 : featureValues.data.dwz || '--' }}m</span>
          </template>
          <template v-else-if="waterTypeSelected === '潮位'">
            <span class="label">名称</span><span class="value">{{ featureValues.data.stName || '--' }}</span
            ><br />
            <span class="label">潮位</span
            ><span class="value">{{ featureValues.data.tdz === 0 ? 0 : featureValues.data.tdz || '--' }}m</span><br />
            <span class="label">更新时间</span><span class="value">{{ featureValues.data.time || '--' }}m</span>
          </template>
        </div>
      </template>
      <!-- 超限水库落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'transfiniteReservoirPointsLayers'">
        <div class="popup_map_hover">
          <span class="label">水库名称</span><span class="value">{{ featureValues.data.stName || '--' }}</span
          ><br />
          <span class="label">实时水位</span
          ><span class="value"
            >{{ featureValues.data.waterLevel === 0 ? 0 : featureValues.data.waterLevel || '--' }}m</span
          ><br />
          <span class="label">汛限水位</span
          ><span class="value"
            >{{ featureValues.data.limitLevel === 0 ? 0 : featureValues.data.limitLevel || '--' }}m</span
          >
        </div>
      </template>
      <!-- 海塘风险落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'seawallRiskPointsLayers'">
        <div class="popup_map_hover">
          <span class="label">海塘名称</span><span class="value">{{ featureValues.data.name || '--' }}</span
          ><br />
          <span class="label">海塘类型</span><span class="value">{{ featureValues.data.type || '--' }}</span>
        </div>
      </template>
      <!-- 超警河网落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'overalarmRiverNetworkPointsLayers'">
        <div class="popup_map_hover">
          <span class="label">河道名称</span><span class="value">{{ featureValues.data.stName || '--' }}</span
          ><br />
          <span class="label">实时水位</span
          ><span class="value"
            >{{ featureValues.data.waterLevel === 0 ? 0 : featureValues.data.waterLevel || '--' }}m</span
          ><br />
          <span class="label">警戒水位</span
          ><span class="value"
            >{{ featureValues.data.warningLevel === 0 ? 0 : featureValues.data.warningLevel || '--' }}m</span
          >
        </div>
      </template>
      <!-- 超警堰闸落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'overalarmWeirGatePointsLayers'">
        <div class="popup_map_hover">
          <span class="label">河道名称</span><span class="value">{{ featureValues.data.stName || '--' }}</span
          ><br />
          <span class="label">实时水位</span
          ><span class="value">{{ featureValues.data.upz === 0 ? 0 : featureValues.data.upz || '--' }}m</span><br />
          <span class="label">警戒水位</span
          ><span class="value"
            >{{ featureValues.data.warningLevel === 0 ? 0 : featureValues.data.warningLevel || '--' }}m</span
          >
        </div>
      </template>
      <!-- 山洪灾害落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'mountainTorrentDisasterPointsLayers'">
        <div class="popup_map_hover">
          <span class="label">行政村</span><span class="value">{{ featureValues.data.avi || '--' }}</span
          ><br />
          <span class="label">自然村</span><span class="value">{{ featureValues.data.nvi || '--' }}</span
          ><br />
          <span class="label">总人口</span
          ><span class="value">{{ featureValues.data.tpo === 0 ? 0 : featureValues.data.tpo || '--' }}人</span><br />
          <span class="label">转移责任人</span><span class="value">{{ featureValues.data.chargerName || '--' }}</span
          ><br />
          <span class="label">电话</span><span class="value">{{ featureValues.data.chargerPhone || '--' }}</span>
        </div>
      </template>
      <!-- 海塘影响社会资源落点的悬浮窗 -->
      <template v-if="featureValues.layerName === 'seawallRiskAffectsSocialResourcesPointsLayers'">
        <div class="popup_map_hover">
          <span class="label">{{ featureValues.data.name || '--' }}</span>
        </div>
      </template>
    </div>

    <!-- 调度方案队伍悬浮框 -->
    <div v-show="schemeRanksPoint" ref="schemeRanksPopup" id="schemeRanksPopup" class="ol-popup scheme-popup">
      <div id="popup-content" class="ol-popup__wrapper">
        <div class="scheme-point_title">{{ schemeRanksInfo.name }}</div>
        <div class="scheme-point_content">
          <div v-for="(value, key) in schemeRanksInfo" :key="key">
            <template v-if="key !== 'name'">
              <div class="scheme-point_sub_box">
                <p class="scheme-point_sub_title">{{ key }}</p>
                <i class="icon-popup-divide"></i>
              </div>
              <p v-for="(item, index) in value" :key="index">
                <span>{{ item.label || item.materialName }}</span>
                <span>{{ item.value || item.num }}</span>
              </p>
            </template>
          </div>
        </div>
      </div>
    </div>
    <!-- 调度方案仓库悬浮框 -->
    <div
      v-show="schemeWarehousePoint"
      ref="schemeWarehousePopup"
      id="schemeWarehousePopup"
      class="ol-popup scheme-popup"
    >
      <div id="popup-content" class="ol-popup__wrapper">
        <div class="scheme-point_title">{{ schemeWarehouseInfo.name }}</div>
        <div class="scheme-point_content">
          <div v-for="(value, key) in schemeWarehouseInfo" :key="key">
            <template v-if="key !== 'name'">
              <div class="scheme-point_sub_box">
                <p class="scheme-point_sub_title">{{ key }}</p>
                <i class="icon-popup-divide"></i>
              </div>
              <p v-for="(item, index) in value" :key="index">
                <span>{{ item.label || item.materialName }}</span>
                <span>{{ item.value || item.num }}</span>
              </p>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- 雷达卫星 -->
    <div class="radarsat_satellite" v-if="displauRadarsatSatellite">
      <iframe src="https://smart.zj121.com/radarNew/index.html" width="100%" height="100%"></iframe>
    </div>

    <!-- 头部容器 -->
    <div class="tabbar_contanier">
      <!-- 左侧tabs标签 -->
      <div class="left_tabbar">
        <img
          v-show="leftTabbarSelected !== '工程列表'"
          src="@/assets/new_images/a_map_tabbar_gclb.png"
          alt="工程列表"
          @click="leftTabbarSelected = '工程列表'"
        />
        <img
          v-show="leftTabbarSelected === '工程列表'"
          src="@/assets/new_images/a_map_tabbar_gclb_hl.png"
          alt="工程列表"
          @click="leftTabbarSelected = '工程列表'"
        />
        <img
          v-show="leftTabbarSelected !== '雨情'"
          src="@/assets/new_images/a_map_tabbar_yq.png"
          alt="雨情"
          @click="leftTabbarSelected = '雨情'"
        />
        <img
          v-show="leftTabbarSelected === '雨情'"
          src="@/assets/new_images/a_map_tabbar_yq_hl.png"
          alt="雨情"
          @click="leftTabbarSelected = '雨情'"
        />
        <img
          v-show="leftTabbarSelected !== '水情'"
          src="@/assets/new_images/a_map_tabbar_sq.png"
          alt="水情"
          @click="leftTabbarSelected = '水情'"
        />
        <img
          v-show="leftTabbarSelected === '水情'"
          src="@/assets/new_images/a_map_tabbar_sq_hl.png"
          alt="水情"
          @click="leftTabbarSelected = '水情'"
        />
        <img
          v-show="leftTabbarSelected !== '台风分析'"
          src="@/assets/new_images/a_map_tabbar_tffx.png"
          alt="台风分析"
          @click="leftTabbarSelected = '台风分析'"
        />
        <img
          v-show="leftTabbarSelected === '台风分析'"
          src="@/assets/new_images/a_map_tabbar_tffx_hl.png"
          alt="台风分析"
          @click="leftTabbarSelected = '台风分析'"
        />
        <img
          v-show="leftTabbarSelected !== '雷达卫星'"
          src="@/assets/new_images/a_map_tabbar_ldwx.png"
          alt="雷达卫星"
          @click="leftTabbarSelected = '雷达卫星'"
        />
        <img
          v-show="leftTabbarSelected === '雷达卫星'"
          src="@/assets/new_images/a_map_tabbar_ldwx_hl.png"
          alt="雷达卫星"
          @click="leftTabbarSelected = '雷达卫星'"
        />
        <img
          v-show="leftTabbarSelected !== '风险隐患'"
          src="@/assets/new_images/a_map_tabbar_fxyh.png"
          alt="风险隐患"
          @click="leftTabbarSelected = '风险隐患'"
        />
        <img
          v-show="leftTabbarSelected === '风险隐患'"
          src="@/assets/new_images/a_map_tabbar_fxyh_hl.png"
          alt="风险隐患"
          @click="leftTabbarSelected = '风险隐患'"
        />
        <img
          v-show="leftTabbarSelected !== '山洪灾害'"
          src="@/assets/new_images/a_map_tabbar_shzh.png"
          alt="山洪灾害"
          @click="leftTabbarSelected = '山洪灾害'"
        />
        <img
          v-show="leftTabbarSelected === '山洪灾害'"
          src="@/assets/new_images/a_map_tabbar_shzh_hl.png"
          alt="山洪灾害"
          @click="leftTabbarSelected = '山洪灾害'"
        />
      </div>
      <!-- 右侧tabs标签 -->
      <div class="right_tabbar">
        <img
          :src="require('@/assets/new_images/a_map_tabbar_xy_level' + responseLevel + '.png')"
          alt="响应等级"
          @click="openResponseLevelSelectPanel = true"
        />
        <img
          src="@/assets/new_images/a_map_tabbar_fyjb.png"
          alt="防御简报"
          @click="defenseBriefingVisibleDialog = true"
        />
        <img src="@/assets/new_images/a_map_tabbar_dxfs.png" alt="短信发送" @click="smsVisibleDialog = true" />
        <img
          v-show="!openPrediction"
          src="@/assets/new_images/a_map_tabbar_jyyb.png"
          alt="降雨预报"
          @click="openPrediction = true"
        />
        <img
          v-show="openPrediction"
          src="@/assets/new_images/a_map_tabbar_jyyb_hl.png"
          alt="关闭预报"
          @click="openPrediction = false"
        />
        <img
          v-show="!openAnalysis"
          src="@/assets/new_images/a_map_tabbar_zbfx.png"
          alt="周边分析"
          @click="openAnalysis = true"
        />
        <img
          v-show="openAnalysis"
          src="@/assets/new_images/a_map_tabbar_zbfx_hl.png"
          alt="关闭分析"
          @click="openAnalysis = false"
        />
        <img
          v-show="!openRescue"
          src="@/assets/new_images/a_map_tabbar_ljqx.png"
          alt="立即抢险"
          @click="openRescue = true"
        />
        <img
          v-show="openRescue"
          src="@/assets/new_images/a_map_tabbar_ljqx_hl.png"
          alt="关闭抢险"
          @click="openRescue = false"
        />
        <img
          v-show="!open3Dmap"
          src="@/assets/new_images/a_map_tabbar_swdt.png"
          alt="三维地图"
          @click="open3Dmap = true"
        />
        <img
          v-show="open3Dmap"
          src="@/assets/new_images/a_map_tabbar_swdt_hl.png"
          alt="三维地图"
          @click="open3Dmap = false"
        />
        <div v-show="openResponseLevelSelectPanel" class="response_level_select_panel">
          <p
            v-for="item in responseLevelArr"
            :class="{ selected: responseLevel === item.id }"
            @click="responseLevelChange(item.id)"
            :key="item.id + item.name"
          >
            {{ item.name }}
          </p>
        </div>
        <!-- 防御简报 -->
        <PrimaryDialog
          :visible="defenseBriefingVisibleDialog"
          :appendToBody="true"
          main-title="水旱灾害防御简报"
          top="10vh"
          width="1296px"
          @handleClose="defenseBriefingVisibleDialog = false"
        >
          <DefenseBriefing v-if="defenseBriefingVisibleDialog" />
        </PrimaryDialog>
        <!-- 短信发送 -->
        <PrimaryDialog
          :visible="smsVisibleDialog"
          :appendToBody="true"
          main-title="发送短信"
          top="15vh"
          width="1296px"
          @handleClose="smsVisibleDialog = false"
        >
          <SendSMS v-if="smsVisibleDialog" ref="sendSMS" @handleClose="smsVisibleDialog = false" />
        </PrimaryDialog>
        <EmergencySchemeDialog
          v-if="emergencySchemeShow"
          :visible="emergencySchemeShow"
          :lat="dispatchLat"
          :lng="dispatchLng"
          @closeDialog="emergencySchemeShow = false"
          @dispatchInfo="dispatchInfo"
          @handleCloseNoThisMap="handleCloseNoThisMap"
        />
        <SelectionTipsDialog
          v-if="SelectionTipsShow"
          :visible="SelectionTipsShow"
          @closeDialog="SelectionTipsShow = false"
        />
        <SendMsgDialog v-if="SendMsgShow" :visible="SendMsgShow" @closeDialog="SendMsgShow = false" />
        <!-- 三维地图 -->
        <PrimaryMapDialog
          mainTitle="三维地图"
          ref="multipleDialog"
          :hasAnalysis="false"
          :visible="open3Dmap"
          @handleClose="HandleClose"
          :appendToBody="true"
          :modal="true"
          width="1400px"
        >
          <Map3D ref="map3D" />
        </PrimaryMapDialog>
      </div>
    </div>

    <!-- 左侧容器 -->
    <div
      class="left_contanier"
      :class="{ hidden: hiddenLeftContanier }"
      v-show="leftTabbarSelected !== '雷达卫星' && leftTabbarSelected !== '山洪灾害'"
    >
      <Title>{{ leftTabbarSelected }}</Title>
      <!-- 工程列表 -->
      <div v-if="leftTabbarSelected === '工程列表'" class="content">
        <Subtitle
          ><span>海塘 ({{ seawallList.length }})</span
          ><i
            :class="displaySeawall ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
            @click="displaySeawall = !displaySeawall"
          ></i
        ></Subtitle>
        <el-table
          v-show="displaySeawall"
          :data="seawallList"
          style="width: 100%; margin-bottom: 10px"
          key="seawallList"
          @selection-change="seawallListSelectionChange"
        >
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column type="index" label="序号" width="55"></el-table-column>
          <el-table-column property="seawallName" label="海塘名称"></el-table-column>
        </el-table>
        <Subtitle
          ><span>水库 ({{ reservoirList.length }})</span
          ><i
            :class="displayReservoir ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
            @click="displayReservoir = !displayReservoir"
          ></i
        ></Subtitle>
        <el-table
          v-show="displayReservoir"
          :data="reservoirList"
          style="width: 100%; margin-bottom: 10px"
          key="reservoirList"
          @selection-change="reservoirListSelectionChange"
        >
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column type="index" label="序号" width="55"></el-table-column>
          <el-table-column property="resName" label="水库名称"></el-table-column>
        </el-table>
        <Subtitle
          ><span>堤防 ({{ dikeList.length }})</span
          ><i :class="displayDike ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" @click="displayDike = !displayDike"></i
        ></Subtitle>
        <el-table
          v-show="displayDike"
          :data="dikeList"
          style="width: 100%; margin-bottom: 10px"
          key="dikeList"
          @selection-change="dikeListSelectionChange"
        >
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column type="index" label="序号" width="55"></el-table-column>
          <el-table-column property="dikeName" label="堤防名称"></el-table-column>
        </el-table>
        <Subtitle
          ><span>水闸 ({{ sluiceList.length }})</span
          ><i
            :class="displaySluice ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
            @click="displaySluice = !displaySluice"
          ></i
        ></Subtitle>
        <el-table
          v-show="displaySluice"
          :data="sluiceList"
          style="width: 100%; margin-bottom: 10px"
          key="sluiceList"
          @selection-change="sluiceListSelectionChange"
        >
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column type="index" label="序号" width="55"></el-table-column>
          <el-table-column property="wagaName" label="水闸名称"></el-table-column>
        </el-table>
      </div>
      <!-- 雨情 -->
      <div
        v-else-if="leftTabbarSelected === '雨情'"
        class="content"
        style="overflow: hidden"
        v-loading="rainAllListLoading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      >
        <div class="rain_button_box">
          <div class="condition_switch" @click="openRainCondition = !openRainCondition">
            <span>条件选择</span>
            <i :class="openRainCondition ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"></i>
          </div>
          <div class="condition_reset" @click="rainConditionReset">
            <i class="el-icon-refresh"></i>
            <span>重置</span>
          </div>
        </div>
        <div class="rain_condition_box" v-show="openRainCondition">
          <div class="condition_item">
            <div class="label">时间范围</div>
            <div class="condition">
              <span>从</span>
              <el-date-picker
                v-model="rainStartTime"
                type="datetime"
                placeholder="选择开始日期时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                @change="getRainAllList"
              ></el-date-picker>
              <br /><span>至</span>
              <el-date-picker
                v-model="rainEndTime"
                type="datetime"
                placeholder="选择结束日期时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                @change="getRainAllList"
              ></el-date-picker>
            </div>
          </div>
          <div class="condition_item">
            <div class="label">降雨历时</div>
            <div class="condition">
              <el-radio-group v-model="rainDuration" @change="getRainAllList">
                <el-radio :label="60">前1小时</el-radio>
                <el-radio :label="180">前3小时</el-radio>
                <el-radio :label="360">前6小时</el-radio>
                <el-radio :label="720">前12小时</el-radio>
                <el-radio :label="1440">前23小时</el-radio>
                <el-radio :label="2880">前48小时</el-radio>
              </el-radio-group>
            </div>
          </div>
          <div class="condition_item">
            <div class="label">雨量范围</div>
            <div class="condition">
              <el-select v-model="rainRange" placeholder="请选择雨量范围" @change="getRainAllList">
                <el-option
                  v-for="item in rainRangeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </div>
          </div>
          <div class="condition_item">
            <div class="label">降雨分布</div>
            <div class="condition">
              <el-checkbox v-model="isLoadingIsosurface" @change="displayLoadingIsosurface"
                >加载对应时段降雨等值面</el-checkbox
              >
            </div>
          </div>
        </div>
        <img class="split_line" src="@/assets/new_images/a_map_left_box_inner_split_line.png" alt="" />
        <div class="rain_tabbar_box">
          <ul class="tabbar">
            <li :class="{ active: tabSeleted === '全部' }" @click="tabSeleted = '全部'">
              全部 {{ rainAllList.length }}
            </li>
            <li :class="{ active: tabSeleted === '降雨' }" @click="tabSeleted = '降雨'">降雨 {{ rainList.length }}</li>
          </ul>
          <el-input v-model="rainKeyword" placeholder="请输入关键字查询" @change="getRainAllList"></el-input>
          <img
            class="menu_icon"
            src="@/assets/new_images/a_map_left_box_menu_icon.png"
            alt=""
            @click="realRainDetailVisible = true"
          />
        </div>
        <div
          class="rain_table_box"
          ref="rainTableBox"
          :style="{ height: openRainCondition ? 'calc(100% - 375px)' : 'calc(100% - 112px)' }"
        >
          <el-table
            :data="tabSeleted === '全部' ? rainAllList : rainList"
            style="width: 100%"
            :height="rainListHeight"
            key="rainAllList"
          >
            <el-table-column type="index" label="序号" width="55"></el-table-column>
            <el-table-column property="stationName" label="名称"></el-table-column>
            <el-table-column property="drp" label="降雨量(mm)" sortable>
              <template slot-scope="scope">
                {{ scope.row.drp == -1 ? '/' : scope.row.drp }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- 水情 -->
      <div
        v-else-if="leftTabbarSelected === '水情'"
        class="content"
        style="overflow: hidden"
        v-loading="waterAllListLoading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      >
        <ul class="water_type_select">
          <li :class="{ active: waterTypeSelected === '水库水位' }" @click="waterTypeSelected = '水库水位'">
            水库水位
          </li>
          <li :class="{ active: waterTypeSelected === '河道水位' }" @click="waterTypeSelected = '河道水位'">
            河道水位
          </li>
          <li :class="{ active: waterTypeSelected === '堰闸水位' }" @click="waterTypeSelected = '堰闸水位'">
            堰闸水位
          </li>
          <li :class="{ active: waterTypeSelected === '潮位' }" @click="waterTypeSelected = '潮位'">潮位</li>
        </ul>
        <div class="water_button_box">
          <div class="condition_switch" @click="openWaterCondition = !openWaterCondition">
            <span>条件选择</span>
            <i :class="openWaterCondition ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"></i>
          </div>
          <div class="condition_reset" @click="waterConditionReset">
            <i class="el-icon-refresh"></i>
            <span>重置</span>
          </div>
        </div>
        <div class="water_condition_box" v-show="openWaterCondition">
          <div class="condition_item">
            <div class="label">查询时间</div>
            <div class="condition">
              <span class="two_written">日期</span>
              <el-date-picker
                v-model="waterStartTime"
                placeholder="选择查询日期"
                value-format="yyyy-MM-dd"
                @change="waterTimeChange"
              ></el-date-picker
              ><br />
              <span class="two_written">时刻</span>
              <el-time-select
                v-model="waterEndTime"
                placeholder="选择查询时刻"
                value-format="HH:mm:ss"
                :picker-options="{ start: '00:00', step: '01:00', end: '23:00' }"
                @change="waterTimeChange"
              ></el-time-select>
            </div>
          </div>
          <div class="condition_item" v-show="waterTypeSelected === '水库水位'">
            <div class="label">水库规模</div>
            <div class="condition">
              <el-radio-group v-model="reservoirScale" @change="getWaterAllList">
                <el-radio label="">全部</el-radio>
                <el-radio label="大中型">大中型</el-radio>
                <el-radio label="小（Ⅰ）型">小（1）型</el-radio>
                <el-radio label="小（Ⅱ）型">小（2）型</el-radio>
              </el-radio-group>
            </div>
          </div>
        </div>
        <img class="split_line" src="@/assets/new_images/a_map_left_box_inner_split_line.png" alt="" />
        <div class="water_tabbar_box">
          <ul class="tabbar">
            <li :class="{ active: tabSeleted2 === '全部' }" @click="tabSeleted2 = '全部'">
              全部 {{ waterAllList.length }}
            </li>
            <li :class="{ active: tabSeleted2 === '超警' }" @click="tabSeleted2 = '超警'">
              超警 {{ waterList.length }}
            </li>
          </ul>
          <el-input v-model="waterKeyword" placeholder="请输入关键字查询" @change="getWaterAllList"></el-input>
          <img
            class="menu_icon"
            src="@/assets/new_images/a_map_left_box_menu_icon.png"
            alt=""
            @click="waterRegimeDetailVisible = true"
          />
        </div>
        <div
          class="water_table_box"
          ref="waterTableBox"
          :style="{
            height: openWaterCondition
              ? waterTypeSelected === '水库水位'
                ? 'calc(100% - 336px)'
                : 'calc(100% - 264px)'
              : 'calc(100% - 157px)'
          }"
        >
          <template v-if="waterTypeSelected === '水库水位'">
            <el-table
              :data="tabSeleted2 === '全部' ? waterAllList : waterList"
              style="width: 100%"
              :height="waterListHeight"
              key="sksw-waterAllList"
            >
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column property="stName" label="名称"></el-table-column>
              <el-table-column property="waterLevel" label="水位(m)" sortable></el-table-column>
              <el-table-column property="time" label="更新时间" width="108" sortable></el-table-column>
            </el-table>
          </template>
          <template v-else-if="waterTypeSelected === '河道水位'">
            <el-table
              :data="tabSeleted2 === '全部' ? waterAllList : waterList"
              style="width: 100%"
              :height="waterListHeight"
              key="hdsw-waterAllList"
            >
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column property="stName" label="名称"></el-table-column>
              <el-table-column property="waterLevel" label="水位(m)" sortable></el-table-column>
              <el-table-column property="time" label="更新时间" width="108" sortable></el-table-column>
            </el-table>
          </template>
          <template v-else-if="waterTypeSelected === '堰闸水位'">
            <el-table
              :data="tabSeleted2 === '全部' ? waterAllList : waterList"
              style="width: 100%"
              :height="waterListHeight"
              key="yzsw-waterAllList"
            >
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column property="stName" label="名称"></el-table-column>
              <el-table-column property="upz" label="闸上水位(m)" sortable></el-table-column>
              <el-table-column property="dwz" label="闸下水位(m)" sortable></el-table-column>
            </el-table>
          </template>
          <template v-else-if="waterTypeSelected === '潮位'">
            <el-table
              :data="tabSeleted2 === '全部' ? waterAllList : waterList"
              style="width: 100%"
              :height="waterListHeight"
              key="cw-waterAllList"
            >
              <el-table-column type="index" label="序号" width="55"></el-table-column>
              <el-table-column property="stName" label="名称"></el-table-column>
              <el-table-column property="tdz" label="潮位(m)" sortable></el-table-column>
              <el-table-column property="time" label="更新时间" width="108" sortable></el-table-column>
            </el-table>
          </template>
        </div>
      </div>

      <!-- 台风分析 -->
      <div v-else-if="leftTabbarSelected === '台风分析'" class="content" style="overflow: hidden">
        <ul class="typhoon_type_select">
          <li :class="{ active: typhoonTypeSelected === '台风分析' }" @click="typhoonTypeSelected = '台风分析'">
            台风分析
          </li>
          <li :class="{ active: typhoonTypeSelected === '当前台风' }" @click="typhoonTypeSelected = '当前台风'">
            当前台风
          </li>
        </ul>
        <!-- 台风分析 -->
        <div v-show="typhoonTypeSelected === '台风分析'" style="height: 100%">
          <ul class="typhoon_tabbar" v-show="typhoonTypeSelected === '台风分析'">
            <li :class="{ active: tabSeleted3 === '综合查询' }" @click="tabSeleted3 = '综合查询'">综合查询</li>
            <li :class="{ active: tabSeleted3 === '相似分析' }" @click="tabSeleted3 = '相似分析'">相似分析</li>
          </ul>
          <!-- 台风分析-综合查询 -->
          <div
            v-show="tabSeleted3 === '综合查询'"
            style="height: 100%"
            v-loading="typhoonListLoading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <div class="typhoon_button_box">
              <div class="condition_switch" @click="openTyphoonCondition = !openTyphoonCondition">
                <span>{{ openTyphoonCondition ? '高级查询' : '基本查询' }}</span>
                <i :class="openTyphoonCondition ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"></i>
              </div>
              <div class="condition_reset" @click="typhoonConditionReset">
                <i class="el-icon-refresh"></i>
                <span>重置</span>
              </div>
            </div>
            <div class="typhoon_condition_box">
              <div class="condition_item">
                <div class="label">影响温州</div>
                <div class="condition">
                  <el-radio-group v-model="hasImpact" @change="getTyhoonHistoryList">
                    <el-radio :label="1">全部</el-radio>
                    <el-radio :label="2">是</el-radio>
                    <el-radio :label="3">否</el-radio>
                  </el-radio-group>
                </div>
              </div>
              <div class="condition_item">
                <div class="label">台风名称</div>
                <div class="condition">
                  <el-input
                    v-model="typhoonNameKeyword"
                    placeholder="请输入台风名称"
                    @change="getTyhoonHistoryList"
                  ></el-input>
                </div>
              </div>
              <div class="condition_item">
                <div class="label">时间选择</div>
                <div class="condition" v-show="!openTyphoonCondition">
                  <el-select v-model="typhoonYear" placeholder="请选择年份" @change="getTyhoonHistoryList">
                    <el-option
                      v-for="item in typhoonYearOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </div>
                <div class="condition" v-show="openTyphoonCondition">
                  <span>从</span>
                  <el-date-picker
                    v-model="typhoonStartDate"
                    type="month"
                    placeholder="选择开始年月"
                    value-format="yyyy-MM"
                    @change="getTyhoonHistoryList"
                  ></el-date-picker>
                  <br /><span>至</span>
                  <el-date-picker
                    v-model="typhoonEndDate"
                    type="month"
                    placeholder="选择结束年月"
                    value-format="yyyy-MM"
                    @change="getTyhoonHistoryList"
                  ></el-date-picker>
                </div>
              </div>
              <div class="condition_item" v-show="openTyphoonCondition">
                <div class="label">登陆强度</div>
                <div class="condition">
                  <el-input-number
                    v-model="typhoonLevelMin"
                    :min="0"
                    :max="20"
                    @change="getTyhoonHistoryList"
                  ></el-input-number>
                  <span class="has_left_margin">—</span>
                  <el-input-number
                    v-model="typhoonLevelMax"
                    :min="0"
                    :max="20"
                    @change="getTyhoonHistoryList"
                  ></el-input-number>
                  <span class="has_left_margin">级</span>
                </div>
              </div>
              <div class="condition_item" v-show="openTyphoonCondition">
                <div class="label">风力极值</div>
                <div class="condition">
                  <el-input-number
                    v-model="typhoonSpeedMin"
                    :min="0"
                    :max="100"
                    @change="getTyhoonHistoryList"
                  ></el-input-number>
                  <span class="has_left_margin">—</span>
                  <el-input-number
                    v-model="typhoonSpeedMax"
                    :min="0"
                    :max="100"
                    @change="getTyhoonHistoryList"
                  ></el-input-number>
                  <span class="has_left_margin">米/秒</span>
                </div>
              </div>
            </div>
            <img class="split_line" src="@/assets/new_images/a_map_left_box_inner_split_line.png" alt="" />
            <div
              class="typhoon_table_box"
              ref="typhoonTableBox"
              :style="{ height: openTyphoonCondition ? 'calc(100% - 439px)' : 'calc(100% - 295px)' }"
            >
              <el-table
                ref="typhoonList"
                class="ban_selected_all"
                :data="typhoonList"
                style="width: 100%"
                :height="typhoonListHeight"
                key="typhoonList"
                @select="typhoonListRowSeletedChanged"
              >
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column property="code" label="编号"></el-table-column>
                <el-table-column property="name" label="中文名"></el-table-column>
                <el-table-column property="englishName" label="英文名"></el-table-column>
              </el-table>
              <i class="el-icon-delete" @click="clearTyphoonListSelected"></i>
            </div>
          </div>
          <!-- 台风分析-相似分析 -->
          <div
            v-show="tabSeleted3 === '相似分析'"
            style="height: 100%"
            v-loading="typhoonAnalysisListLoading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <div class="typhoon_condition_box">
              <div class="condition_item">
                <div class="label">选择台风</div>
                <div class="condition">
                  <el-cascader
                    v-model="analysisTyphoonCode"
                    :props="typhoonProps"
                    :show-all-levels="false"
                    @change="getTyphoonAnalysisList"
                  ></el-cascader>
                </div>
              </div>
              <div class="condition_item">
                <div class="label">分析类型</div>
                <div class="condition">
                  <el-radio-group v-model="analysisType" @change="getTyphoonAnalysisList">
                    <el-radio label="path">路径相似</el-radio>
                    <el-radio label="season">季节相似</el-radio>
                    <el-radio label="born_land">生成地相似</el-radio>
                  </el-radio-group>
                </div>
              </div>
            </div>
            <img class="split_line" src="@/assets/new_images/a_map_left_box_inner_split_line.png" alt="" />
            <div class="typhoon_analysis_table_box" ref="typhoonAnalysisTableBox">
              <el-table
                ref="typhoonAnalysisList"
                :data="typhoonAnalysisList"
                class="ban_selected_all"
                style="width: 100%"
                :height="typhoonAnalysisListHeight"
                key="typhoonAnalysisList"
                @select="typhoonListRowSeletedChanged"
              >
                <el-table-column type="selection" width="50"></el-table-column>
                <el-table-column property="tfbh" label="编号"></el-table-column>
                <el-table-column property="name" label="中文名"></el-table-column>
                <el-table-column property="ename" label="英文名"></el-table-column>
              </el-table>
              <i class="el-icon-delete" @click="clearTyphoonAnalysisListSelected"></i>
            </div>
          </div>
        </div>
        <!-- 当前台风 -->
        <div v-show="typhoonTypeSelected === '当前台风'">
          <!-- 台风信息 todo -->
          <!-- 台风影响 todo -->
        </div>
      </div>

      <!-- 风险隐患 -->
      <div v-else-if="leftTabbarSelected === '风险隐患'" class="content">
        <Subtitle>
          <span>超限水库 ({{ transfiniteReservoirList.length }})</span>
          <div>
            <img
              v-show="!isTransfiniteReservoirDropPoint"
              class="drop_icon on_subtitle"
              src="@/assets/new_images/a_map_left_box_drop_icon.png"
              @click="transfiniteReservoirMapDropPoint"
            />
            <img
              v-show="isTransfiniteReservoirDropPoint"
              class="drop_icon on_subtitle"
              src="@/assets/new_images/a_map_left_box_drop_icon_hl.png"
              @click="transfiniteReservoirMapRemovePoint"
            />
            <i
              :class="displayTransfiniteReservoir ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
              @click="displayTransfiniteReservoir = !displayTransfiniteReservoir"
            ></i>
          </div>
        </Subtitle>
        <el-table
          v-show="displayTransfiniteReservoir"
          :data="transfiniteReservoirList"
          style="width: 100%; margin-bottom: 10px"
          key="transfiniteReservoirList"
        >
          <el-table-column type="index" label="序号" width="55"></el-table-column>
          <el-table-column property="stName" label="水库名称"></el-table-column>
          <el-table-column property="waterLevel" label="实时水位(m)"></el-table-column>
          <el-table-column property="limitLevel" label="汛限水位(m)"></el-table-column>
        </el-table>
        <Subtitle>
          <span>海塘风险 ({{ seawallRiskList.length }})</span>
          <div>
            <img
              v-show="!isSeawallRiskDropPoint"
              class="drop_icon on_subtitle"
              src="@/assets/new_images/a_map_left_box_drop_icon.png"
              @click="seawallRiskMapDropPoint"
            />
            <img
              v-show="isSeawallRiskDropPoint"
              class="drop_icon on_subtitle"
              src="@/assets/new_images/a_map_left_box_drop_icon_hl.png"
              @click="seawallRiskMapRemovePoint"
            />
            <i
              :class="displaySeawallRisk ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
              @click="displaySeawallRisk = !displaySeawallRisk"
            ></i>
          </div>
        </Subtitle>
        <el-table
          ref="selectedRiskTable"
          v-show="displaySeawallRisk"
          :data="seawallRiskList"
          style="width: 100%; margin-bottom: 10px"
          key="seawallRiskList"
          highlight-current-row
          @current-change="seawallRiskListCurrentChange"
        >
          <el-table-column type="index" label="序号" width="55"></el-table-column>
          <el-table-column property="name" label="海塘名称"></el-table-column>
          <el-table-column property="type" label="风险类型"></el-table-column>
        </el-table>
        <Subtitle>
          <span>超警河网 ({{ overalarmRiverNetworkList.length }})</span>
          <div>
            <img
              v-show="!isOveralarmRiverNetworkDropPoint"
              class="drop_icon on_subtitle"
              src="@/assets/new_images/a_map_left_box_drop_icon.png"
              @click="overalarmRiverNetworkMapDropPoint"
            />
            <img
              v-show="isOveralarmRiverNetworkDropPoint"
              class="drop_icon on_subtitle"
              src="@/assets/new_images/a_map_left_box_drop_icon_hl.png"
              @click="overalarmRiverNetworkMapRemovePoint"
            />
            <i
              :class="displayOveralarmRiverNetwork ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
              @click="displayOveralarmRiverNetwork = !displayOveralarmRiverNetwork"
            ></i>
          </div>
        </Subtitle>
        <el-table
          v-show="displayOveralarmRiverNetwork"
          :data="overalarmRiverNetworkList"
          style="width: 100%; margin-bottom: 10px"
          key="overalarmRiverNetworkList"
        >
          <el-table-column type="index" label="序号" width="55"></el-table-column>
          <el-table-column property="stName" label="河道名称"></el-table-column>
          <el-table-column property="waterLevel" label="实时水位(m)"></el-table-column>
          <el-table-column property="warningLevel" label="警戒水位(m)"></el-table-column>
        </el-table>
        <Subtitle>
          <span>超警堰闸 ({{ overalarmWeirGateList.length }})</span>
          <div>
            <img
              v-show="!isOveralarmWeirGateDropPoint"
              class="drop_icon on_subtitle"
              src="@/assets/new_images/a_map_left_box_drop_icon.png"
              @click="overalarmWeirGateMapDropPoint"
            />
            <img
              v-show="isOveralarmWeirGateDropPoint"
              class="drop_icon on_subtitle"
              src="@/assets/new_images/a_map_left_box_drop_icon_hl.png"
              @click="overalarmWeirGateMapRemovePoint"
            />
            <i
              :class="displayOveralarmWeirGate ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"
              @click="displayOveralarmWeirGate = !displayOveralarmWeirGate"
            ></i>
          </div>
        </Subtitle>
        <el-table
          v-show="displayOveralarmWeirGate"
          :data="overalarmWeirGateList"
          style="width: 100%; margin-bottom: 10px"
          key="overalarmWeirGateList"
        >
          <el-table-column type="index" label="序号" width="55"></el-table-column>
          <el-table-column property="stName" label="堰闸名称"></el-table-column>
          <el-table-column property="upz" label="实时水位(m)"></el-table-column>
          <el-table-column property="warningLevel" label="警戒水位(m)"></el-table-column>
        </el-table>
      </div>

      <!-- 雨情的地图落点开关 -->
      <img
        v-show="!isDropPoint && leftTabbarSelected === '雨情'"
        class="drop_icon on_title"
        src="@/assets/new_images/a_map_left_box_drop_icon.png"
        @click="mapDropPoint"
      />
      <img
        v-show="isDropPoint && leftTabbarSelected === '雨情'"
        class="drop_icon on_title"
        src="@/assets/new_images/a_map_left_box_drop_icon_hl.png"
        @click="mapRemovePoint"
      />
      <!-- 水情的地图落点开关 -->
      <img
        v-show="!isWaterDropPoint && leftTabbarSelected === '水情'"
        class="drop_icon on_title"
        src="@/assets/new_images/a_map_left_box_drop_icon.png"
        @click="mapWaterDropPoint"
      />
      <img
        v-show="isWaterDropPoint && leftTabbarSelected === '水情'"
        class="drop_icon on_title"
        src="@/assets/new_images/a_map_left_box_drop_icon_hl.png"
        @click="mapWaterRemovePoint"
      />

      <!-- 隐藏按钮 -->
      <div class="display_btn" @click="hiddenLeftContanier = !hiddenLeftContanier"></div>

      <!-- 有无台风 -->
      <div
        class="no_typhoon"
        v-show="!displayCentralRole && leftTabbarSelected === '台风分析'"
        v-loading="hasTyphoonLoading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        @click="displayCentralRole = true"
      >
        <img
          :src="
            hasTyphoon
              ? require('@/assets/new_images/has_typhoon_icon.png')
              : require('@/assets/new_images/no_typhoon_icon.png')
          "
          alt=""
        />
        <span>{{ hasTyphoon ? tyhoonCentralRoleName : '暂无台风' }}</span>
      </div>
    </div>

    <!-- 右侧容器 -->
    <div
      class="right_contanier"
      :class="{ hidden: hiddenRightContanier }"
      v-show="leftTabbarSelected === '风险隐患' && showSeawallRiskProjectDetail"
    >
      <Title>{{ selectedRiskName }}</Title>

      <!-- 风险隐患 -->
      <div
        class="content"
        style="overflow: hidden"
        v-loading="companiesListLoading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
      >
        <div class="selected_risk_button_box">
          <div class="condition_switch" @click="openSelectedRiskCondition = !openSelectedRiskCondition">
            <span>条件选择</span>
            <i :class="openSelectedRiskCondition ? 'el-icon-caret-top' : 'el-icon-caret-bottom'"></i>
          </div>
          <div class="condition_reset" @click="selectedRiskConditionReset">
            <i class="el-icon-refresh"></i>
            <span>重置</span>
          </div>
        </div>
        <div class="selected_risk_condition_box" v-show="openSelectedRiskCondition">
          <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange"
            >全选</el-checkbox
          >
          <div style="margin: 15px 0"></div>
          <el-checkbox-group v-model="checkedCompanies" @change="handleCheckedCompaniesChange">
            <el-checkbox v-for="company in companies" :label="company.name" :key="company.name + company.num">{{
              company.name + '(' + company.num + ')'
            }}</el-checkbox>
          </el-checkbox-group>
          <div class="altitude_extremum_box">
            <div class="highest_spot">
              <span>海拔最高点</span>
              <span>{{ altitudeHighestSpot }}</span>
            </div>
            <div class="minimum_spot">
              <span>海拔最低点位</span>
              <span>{{ altitudeMinimumSpot }}</span>
            </div>
          </div>
        </div>
        <img class="split_line" src="@/assets/new_images/a_map_left_box_inner_split_line.png" alt="" />
        <el-input
          v-model="companiesKeyword"
          placeholder="请输入关键字查询"
          @change="getSeawallRiskListDetailData"
        ></el-input>
        <div
          class="selected_risk_table_box"
          ref="selectedRiskTableBox"
          :style="{ height: openSelectedRiskCondition ? 'calc(100% - 278px)' : 'calc(100% - 113px)' }"
        >
          <el-table :data="companiesList" style="width: 100%" :height="companiesListHeight" key="companiesList">
            <el-table-column type="index" label="序号" width="55"></el-table-column>
            <el-table-column property="name" label="名称"></el-table-column>
            <el-table-column property="type" label="类别"></el-table-column>
            <el-table-column property="elevation" label="高程(m)"></el-table-column>
          </el-table>
        </div>
      </div>

      <!-- 隐藏按钮 -->
      <div class="display_btn" @click="hiddenRightContanier = !hiddenRightContanier"></div>

      <!-- 关闭按钮 -->
      <div class="close_btn" @click="closeSeawallRiskProjectDeatilContanier">
        <i class="el-icon-close"></i>
      </div>
    </div>

    <!-- C位指标 -->
    <div
      class="central_role"
      v-show="
        (leftTabbarSelected === '台风分析' && displayCentralRole) ||
        (leftTabbarSelected === '水情' && displayWaterCentralRole)
      "
      v-loading="centralRoleLoading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
    >
      <i v-for="i in 4" :key="'centralRoleNail' + i"></i>
      <div class="title">
        <img src="@/assets/new_images/central_role_title_left.png" alt="" style="margin-right: 20px" />
        <span v-show="leftTabbarSelected === '台风分析'">
          {{ tyhoonCentralRoleName }} - {{ tyhoonCentralRoleLevel }}级
        </span>
        <span v-show="leftTabbarSelected === '水情'">{{ leftTabbarSelected }}</span>
        <img src="@/assets/new_images/central_role_title_right.png" alt="" style="margin-left: 20px" />
      </div>
      <div class="content">
        <div class="typhoon_data" v-show="leftTabbarSelected === '台风分析'">
          <p>
            距离鹿城 <span class="distance">{{ tyhoonCentralRoleDistance }}</span> km
          </p>
          <p>
            受影响工程 <span class="number">{{ tyhoonCentralRoleEffectProject }}</span> 个
          </p>
        </div>
        <div class="water_data" v-show="leftTabbarSelected === '水情'">
          <div class="water_data_item">
            <p class="label">水库水位超汛限</p>
            <p class="data">
              <span>{{ waterCentralRoleData.rrWarn }}</span> /{{ waterCentralRoleData.rrTotal }}
            </p>
          </div>
          <div class="water_data_item">
            <p class="label">河道水位超警</p>
            <p class="data">
              <span>{{ waterCentralRoleData.zzWarn }}</span> /{{ waterCentralRoleData.zzTotal }}
            </p>
          </div>
          <div class="water_data_item">
            <p class="label">堰闸水位超警</p>
            <p class="data">
              <span>{{ waterCentralRoleData.ddWarn }}</span> /{{ waterCentralRoleData.ddTotal }}
            </p>
          </div>
          <div class="water_data_item">
            <p class="label">潮位超警</p>
            <p class="data">
              <span>{{ waterCentralRoleData.ttWarn }}</span> /{{ waterCentralRoleData.ttTotal }}
            </p>
          </div>
        </div>
      </div>
      <div class="hidden_box" @click="displayCentralRole = false" v-show="leftTabbarSelected === '台风分析'">
        <img src="@/assets/new_images/hidden_central_role_icon.png" alt="" />
        <span>隐藏</span>
      </div>
      <div class="hidden_box" @click="displayWaterCentralRole = false" v-show="leftTabbarSelected === '水情'">
        <img src="@/assets/new_images/hidden_central_role_icon.png" alt="" />
        <span>隐藏</span>
      </div>
    </div>

    <!-- 图例容器 -->
    <div
      class="map_legend"
      v-show="leftTabbarSelected === '雨情' || leftTabbarSelected === '水情' || leftTabbarSelected === '山洪灾害'"
      :class="{ left: leftTabbarSelected === '山洪灾害' }"
    >
      <i v-for="i in 4" :key="'map_legend_concer_' + i"></i>
      <div class="title" @click="mapLegendIsShow = !mapLegendIsShow">
        <span>图例</span>
        <span class="legend_arrow" :class="{ rotate_arrow: mapLegendIsShow }"></span>
      </div>
      <div class="content" :class="{ hidden: !mapLegendIsShow }">
        <!-- 雨情 -->
        <div class="subtitle" v-show="leftTabbarSelected === '雨情'">实时降雨量</div>
        <div class="legend_box" v-show="leftTabbarSelected === '雨情'">
          <div class="legend_item" style="width: 60%"><i class="circle" style="background-color: #f32fdf"></i>>250</div>
          <div class="legend_item" style="width: 40%">
            <i class="circle" style="background-color: #fe323a"></i>100~250
          </div>
          <div class="legend_item" style="width: 60%">
            <i class="circle" style="background-color: #ff8a21"></i>50~100
          </div>
          <div class="legend_item" style="width: 40%">
            <i class="circle" style="background-color: #ffcc6f"></i>25~50
          </div>
          <div class="legend_item" style="width: 60%">
            <i class="circle" style="background-color: #4cc704"></i>10~25
          </div>
          <div class="legend_item" style="width: 40%">
            <i class="circle" style="background-color: #00a0ff"></i>0.1~10
          </div>
          <div class="legend_item" style="width: 60%"><i class="circle" style="background-color: #b2b2b2"></i>0</div>
        </div>
        <!-- 水情 -->
        <div class="subtitle" v-show="leftTabbarSelected === '水情'">水情</div>
        <div class="legend_box" v-show="leftTabbarSelected === '水情'">
          <div class="legend_item" style="width: 50%"><i class="circle" style="background-color: #19af47"></i>正常</div>
          <div class="legend_item" style="width: 50%"><i class="circle" style="background-color: #e99100"></i>超警</div>
        </div>
        <!-- 山洪灾害 -->
        <div class="subtitle" v-show="leftTabbarSelected === '山洪灾害'">风险等级</div>
        <div class="legend_box" v-show="leftTabbarSelected === '山洪灾害'">
          <div class="legend_item" style="width: 33.3%"><i style="background-color: #f7b500"></i>低风险</div>
          <div class="legend_item" style="width: 66.6%"><i style="background-color: #ff0000"></i>高风险</div>
        </div>
      </div>
    </div>

    <!-- 悬浮按钮容器 -->
    <div class="levitation_button">
      <div class="btn_item" v-show="leftTabbarSelected === '雨情'" @click="visibleRainDialog = true">
        <div class="text">雨量报表</div>
        <img src="@/assets/new_images/levitation_button_rain_icon.png" alt="" />
      </div>
    </div>

    <!-- 工程列表落点点击弹窗 -->
    <PrimaryDialog
      :visible="sluiceDialogForSchedule"
      :main-title="projectPointData.name"
      top="4vh"
      width="1300px"
      @handleClose="sluiceDialogForSchedule = false"
    >
      <ScheduleDialog v-if="sluiceDialogForSchedule" :pointData="projectPointData" />
    </PrimaryDialog>
    <!-- 雨情列表详情弹窗 -->
    <RealRainDetail
      v-if="realRainDetailVisible"
      :visible="realRainDetailVisible"
      @close="realRainDetailVisible = false"
    ></RealRainDetail>
    <!-- 雨情落点点击弹窗 -->
    <PrimaryDialog
      :mainTitle="rainStationPointData.stationName"
      :hasAnalysis="false"
      :visible="waterRainDialog"
      @handleClose="waterRainDialog = false"
      width="1129px"
      top="10vh"
    >
      <div class="dialog-content">
        <RainDialogContent dialogType="PP" :stationData="rainStationPointData" ref="rainDialog" />
      </div>
    </PrimaryDialog>
    <!-- 雨量报表 -->
    <PrimaryDialog
      :visible="visibleRainDialog"
      :appendToBody="true"
      main-title="雨量报表"
      top="10vh"
      width="1296px"
      @handleClose="handleRainClose"
    >
      <RainfallReportContent ref="rainfallReportContent" />
    </PrimaryDialog>
    <!-- 水情列表详情弹窗 -->
    <DetailsCommonDialog :visible="waterRegimeDetailVisible" code="水情" @close="waterRegimeDetailVisible = false" />
    <!-- 水情落点点击弹窗 -->
    <WorkWaterDialog
      v-if="waterPointDialogShow"
      :visible="waterPointDialogShow"
      title="水情"
      :pointData="waterStationPointData"
      @closeDialog="waterPointDialogShow = false"
    />
    <!-- 山洪灾害落点点击弹窗 -->
    <PrimaryDialog
      v-if="watershedDisasterRiskAssessment"
      :visible="watershedDisasterRiskAssessment"
      :main-title="mountainTorrentDisasterPointData.nvi"
      width="1000px"
      top="20vh"
      @handleClose="watershedDisasterRiskAssessment = false"
    >
      <WatershedDisasterRiskAssessment :pointData="mountainTorrentDisasterPointData" />
    </PrimaryDialog>
  </div>
</template>

<script>
import axios from 'axios';
import Title from './components/Title.vue';
import Subtitle from './components/Subtitle.vue';
import { PrimaryDialog, PrimaryMapDialog } from '@/components';
import ScheduleDialog from '@/views/water_disaster_custom/dialog/SluiceDialogForSchedule/index.vue';
import RealRainDetail from '@/views/water_disaster_custom/cards/dialog/RealRainDetail.vue';
import RainDialogContent from '@/views/hydraulic_amap/components/dialog/StationDialog';
import WorkWaterDialog from '@/views/water_disaster_custom/dialog/teamPointDialog.vue';
import DetailsCommonDialog from '@/views/water_disaster_custom/dialog/DetailsCommonDialog/index.vue';
import WatershedDisasterRiskAssessment from '@/views/water_disaster_custom/dialog/watershedDisasterRiskAssessment/WatershedDisasterRiskAssessment.vue';
import RainfallReportContent from './components/RainfallReportContent.vue';
import Map3D from './components/Map3D.vue';
import SendSMS from './components/SendSMS.vue';
import DefenseBriefing from './components/DefenseBriefing.vue';
import EmergencySchemeDialog from '@/views/water_disaster_custom/dialog/EmergencySchemeDialog.vue';
import SelectionTipsDialog from '@/views/water_disaster_custom/dialog/SelectionTipsDialog.vue';
import SendMsgDialog from '@/views/water_disaster_custom/dialog/SendMsgDialog';

import 'ol/ol.css';
import { Map, View, Overlay, Feature } from 'ol';
import { Vector as VectorLayer } from 'ol/layer';
import { Vector as VectorSource } from 'ol/source';
import { defaults as defaultControls } from 'ol/control';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import Point from 'ol/geom/Point';
import { Style, Fill, Stroke, Icon, Text, Circle } from 'ol/style';
import GeoJSON from 'ol/format/GeoJSON';
import LayerGroup from 'ol/layer/Group';
import Cluster from 'ol/source/Cluster';

import { getTyhoonList, getSimilarTyphoons, getTyphoonStat4C, getTyphoonDetail } from '@/api/typhoon.js';
import { hydraulicApi } from '@/api/hydraulic_amap/index.js';
import { rainApi, workApi } from '@/api/dataScreen/floodJudge/index.js';
import { disasterPreventionApi } from '@/api/waterDisasterPrevention/index.js';
import { getHiddenDangerList, socialResourcesCountByType, socialResourcesList } from '@/api/new_screen_api.js';

import { drawLineEx } from '@/utils/mapUtil.js';

import {
  newTyphoon,
  drawRoutePoint,
  drawLine,
  drawAnaylysiscetPoint,
  addLayerClick,
  drawCircleInMeter
} from '@/utils/mapUtil.js';
import { mapActions, mapGetters } from 'vuex';
export default {
  name: 'aMapOfLucheng',
  props: {},
  components: {
    Title,
    Subtitle,
    PrimaryDialog,
    ScheduleDialog,
    RealRainDetail,
    RainDialogContent,
    WorkWaterDialog,
    DetailsCommonDialog,
    WatershedDisasterRiskAssessment,
    RainfallReportContent,
    PrimaryMapDialog,
    Map3D,
    SendSMS,
    DefenseBriefing,
    EmergencySchemeDialog,
    SelectionTipsDialog,
    SendMsgDialog
  },
  data() {
    return {
      map: null,
      baseLayer: new TileLayer(
        {
          source: new XYZ({
            url: 'http://t0.tianditu.gov.cn/DataServer?T=img_c&X={x}&Y={y}&L={z}&tk=434967d31e3d3f9d396b2f4c4250b023',
            projection: 'EPSG:4326'
          })
        },
        { zoomOffset: 1 }
      ),
      lcBorderLayer: new TileLayer({
        id: 'lc_border',
        source: new XYZ({
          url: 'https://webgis.ygwjg.com/arcgis/rest/services/lucheng/lc_border/MapServer/tile/{z}/{y}/{x}',
          projection: 'EPSG:4326',
          crossOrigin: 'anonymous'
        }),
        preload: 1,
        visible: true
      }),
      lcRiverLayer: new TileLayer({
        id: 'lc_river',
        source: new XYZ({
          url: 'https://webgis.ygwjg.com/arcgis/rest/services/lucheng/lc_river/MapServer/tile/{z}/{y}/{x}',
          projection: 'EPSG:4326',
          crossOrigin: 'anonymous'
        }),
        preload: 1,
        visible: true
      }),
      mapLayers: {},
      hoverOverlay: {},
      featureValues: {},

      hiddenLeftContanier: false, // 隐藏左侧区域
      hiddenRightContanier: false, // 隐藏右侧区域
      mapLegendIsShow: false, // 图例是否展示

      leftTabbarSelected: '工程列表', // 左侧tabbar选中的项
      responseLevel: '5', // 选中的响应等级
      responseLevelArr: [], // 响应等级可选数据
      openResponseLevelSelectPanel: false, // 是否打开相应选择面板
      defenseBriefingVisibleDialog: false, // 是否打开防御简报的弹窗
      smsVisibleDialog: false, // 是否打开短信发送的弹窗
      openPrediction: false, // 是否打开降雨预报
      openAnalysis: false, // 是否打开周边分析
      openRescue: false, // 是否打开立即抢险
      emergencySchemeShow: false,
      dispatchLat: '',
      dispatchLng: '',
      lineLayer_team: null, //人员调度路线图层
      lineLayer_warehouse: null, //仓库调度路线图层
      pointLayer_team_start: null, //队伍起点
      pointLayer_warehouse_start: null, //仓库起点
      schemeWarehousePoint: false, // 调度方案仓库点位
      schemeWarehouseOverlay: null, // 调度方案仓库弹框
      schemeWarehouseInfo: {}, // 调度方案仓库信息
      schemeRanksPoint: false, // 调度方案队伍点位
      schemeRanksOverlay: null, // 调度方案队伍弹框
      schemeRanksInfo: {}, // 调度方案队伍信息
      analysisPointsLayer: [],
      circleLayer: null,
      SelectionTipsShow: false,
      SendMsgShow: false,
      open3Dmap: false, // 是否打开3D地图

      displaySeawall: false, // 工程列表-是否展示海塘列表
      seawallList: [], // 海塘列表数据
      displayReservoir: false, // 工程列表-是否展示水库列表
      reservoirList: [], // 水库列表数据
      displayDike: false, // 工程列表-是否展示堤防列表
      dikeList: [], // 堤防列表数据
      displaySluice: false, // 工程列表-是否展示水闸列表
      sluiceList: [], // 水闸列表数据

      openRainCondition: true, // 控制雨情筛选条件是否显示
      rainStartTime: '', // 雨情筛选条件-开始时间
      rainEndTime: '', // 雨情筛选条件-结束时间
      rainDuration: 60, // 雨情筛选条件-降雨历时
      rainRange: '', // 雨情筛选条件-雨量范围
      rainRangeOptions: [
        { value: '', label: '全部' },
        { value: '0-10', label: '<10' },
        { value: '10-25', label: '10-25' },
        { value: '25-50', label: '25-50' },
        { value: '50-100', label: '50-100' },
        { value: '100-150', label: '100-250' },
        { value: '250-999', label: '>250' }
      ], // 降雨范围选项
      isLoadingIsosurface: false, // 是否加载对应时段降雨等值面
      isosurfaceLayer: null, // 等值面图层
      tabSeleted: '全部', // 选中的tab
      rainKeyword: '', // 查询关键字
      rainAllList: [], // 雨情-全部列表数据
      rainList: [], // 雨情-降雨列表数据
      rainAllListLoading: false, // 表格是否正在加载
      rainListHeight: 0, // 表格高度
      isDropPoint: true, // 是否开启地图落点

      waterTypeSelected: '水库水位', //水情-选中的类型
      openWaterCondition: true, // 控制水情筛选条件是否显示
      waterStartTime: '', // 水情筛选条件-开始时间
      waterEndTime: '', // 水情筛选条件-结束时间
      reservoirScale: '', // 水情筛选条件-水库规模
      tabSeleted2: '全部', // 选中的tab
      waterKeyword: '', // 查询关键字
      waterAllList: [], // 雨情-全部列表数据
      waterList: [], // 雨情-降雨列表数据
      waterAllListLoading: false, // 表格是否正在加载
      waterListHeight: 0, // 表格高度
      displayWaterCentralRole: true, // 是否展示C位数据
      waterCentralRoleData: {}, // 雨情C位数据
      centralRoleLoading: false, // C位数据是否正在加载
      isWaterDropPoint: true, // 是否开启地图落点

      hasTyphoon: false, // 当前是否有台风
      displayCentralRole: false, // 是否展示C位台风信息
      hasTyphoonLoading: false, // 当前台风正在加载
      typhoonTypeSelected: '台风分析', //台风-选中的类型
      tabSeleted3: '综合查询', // 台风分析下选择的tab
      openTyphoonCondition: false, // 台风综合查询-展示基本查询还是高级查询
      hasImpact: 1, // 是否对地区有影响
      typhoonNameKeyword: '', // 台风名称
      typhoonYear: '', // 年份
      typhoonYearOptions: [
        { value: '', label: '所有年份' },
        ...new Array(100).fill(0).map((item, index) => {
          let value = new Date().getFullYear() - index + '';
          return { value, label: value };
        })
      ], // 年份选项
      typhoonStartDate: '', // 查询台风时间范围的起始时间
      typhoonEndDate: '', // 查询台风时间范围的终止时间
      typhoonLevelMin: '', // 查询台风等级范围的极小值
      typhoonLevelMax: '', // 查询台风等级范围的极大值
      typhoonSpeedMin: '', // 查询台风风速范围的极小值
      typhoonSpeedMax: '', // 查询台风风速范围的极大值
      typhoonList: [], // 台风表格数据
      typhoonListLoading: false, // 表格是否正在加载
      typhoonListHeight: 0, // 表格高度
      analysisTyphoonCode: '', // 选择需要分析的台风编码
      typhoonProps: {
        lazy: true,
        lazyLoad(node, resolve) {
          const { level } = node;
          if (level === 0) {
            const nodes = [
              ...new Array(100).fill(0).map((item, index) => {
                let value = new Date().getFullYear() - index + '';
                return { value, label: value, leaf: false };
              })
            ];
            resolve(nodes);
          } else {
            getTyhoonList({
              affectedWz: 1,
              begin: node.value + '-01',
              end: node.value + '-12'
            }).then((res) => {
              let nodes = [];
              if (res.code === 0) {
                (res.data || []).forEach((e) => {
                  nodes.push({ value: e.code, label: e.name, leaf: true });
                });
              }
              resolve(nodes);
            });
          }
        }
      },
      analysisType: 'path',
      typhoonAnalysisList: [], // 分析后的台风表格数据
      typhoonAnalysisListLoading: false, // 表格是否正在加载
      typhoonAnalysisListHeight: 0, // 表格高度
      tyhoonCentralRoleName: '', // C位台风名称
      tyhoonCentralRoleLevel: '', // C位台风等级
      tyhoonCentralRoleDistance: '', // C位台风距离鹿城km
      tyhoonCentralRoleEffectProject: '', // C位台风影响工程个数
      typhoonsMap: new Map(),
      typhoonPoint: false, //台风点位点击弹框显示标记
      pointOverlay: null, //台风点位点击弹框
      typhoonPointData: {}, //台风点位详情
      typhoonName: '',

      displauRadarsatSatellite: false, // 是否展示雷达卫星

      displayTransfiniteReservoir: false, // 风险隐患-是否展示超限水库列表
      transfiniteReservoirList: [], // 超限水库列表数据
      isTransfiniteReservoirDropPoint: true, // 是否开启超限水库的地图落点
      displaySeawallRisk: false, // 风险隐患-是否展示海塘风险列表
      seawallRiskList: [], // 海塘风险列表数据
      isSeawallRiskDropPoint: true, // 是否开启海塘风险的地图落点
      displayOveralarmRiverNetwork: false, // 风险隐患-是否展示超警河网列表
      overalarmRiverNetworkList: [], // 超警河网列表数据
      isOveralarmRiverNetworkDropPoint: true, // 是否开启超警河网的地图落点
      displayOveralarmWeirGate: false, // 风险隐患-是否展示超警堰闸列表
      overalarmWeirGateList: [], // 超警堰闸列表数据
      isOveralarmWeirGateDropPoint: true, // 是否开启超警堰闸的地图落点

      showSeawallRiskProjectDetail: false, // 是否显示海塘详细信息
      selectedRiskName: '', // 选中的风险隐患海塘工程的名称
      selectedRiskCode: '', // 选中的风险隐患海塘工程的编码
      openSelectedRiskCondition: true, // 控制海塘筛选条件是否显示
      checkAll: true, // 是否全选
      checkedCompanies: [], // 选中的项
      companies: [], // 多选框所有可选值
      isIndeterminate: true, // 负责控制选中样式
      altitudeHighestSpot: '', // 海拔最高点
      altitudeMinimumSpot: '', // 海拔最低点
      companiesKeyword: '', // 表格查询的关键字
      companiesList: [], // 表格数据
      companiesListLoading: false, // 表格数据是否正在加载
      companiesListHeight: 0, // 表格高度

      visibleRainDialog: false,
      sluiceDialogForSchedule: false,
      projectPointData: {},
      realRainDetailVisible: false,
      waterRainDialog: false,
      rainStationPointData: {},
      waterRegimeDetailVisible: false,
      waterPointDialogShow: false,
      waterStationPointData: {},
      watershedDisasterRiskAssessment: false,
      mountainTorrentDisasterPointData: {},

      lineLayer: [] //海塘线图层
    };
  },
  computed: {
    ...mapGetters(['previewModule', 'IsAction', 'SpotsFlag', 'SpotsFlagType', 'DispatchCarousel'])
  },
  watch: {
    leftTabbarSelected(val, oldVal) {
      this.leftTabbarChange(val);
      if (oldVal === '台风分析') {
        this.removeTyphoonRoute(null);
        this.map.getView().setCenter([120.54, 28.08]);
        this.map.getView().setZoom(12);
        this.lcBorderLayer.setVisible(true);
        this.lcRiverLayer.setVisible(true);
        this.map.removeOverlay(this.pointOverlay);
      } else if (oldVal === '雷达卫星') {
        this.displauRadarsatSatellite = false;
      } else if (oldVal === '雨情') {
        this.map.removeLayer(this.isosurfaceLayer);
      } else if (oldVal === '水情') {
        this.displayWaterCentralRole = true;
      }
    },
    openRainCondition() {
      this.$nextTick(() => {
        this.rainListHeight = this.$refs.rainTableBox.clientHeight;
      });
    },
    openWaterCondition() {
      this.$nextTick(() => {
        this.waterListHeight = this.$refs.waterTableBox.clientHeight;
      });
    },
    openTyphoonCondition() {
      this.$nextTick(() => {
        this.typhoonListHeight = this.$refs.typhoonTableBox.clientHeight;
      });
    },
    tabSeleted3(val) {
      this.removeTyphoonRoute(null);
      if (val === '综合查询')
        this.$nextTick(() => {
          this.typhoonListHeight = this.$refs.typhoonTableBox.clientHeight;
        });
      if (val === '相似分析')
        this.$nextTick(() => {
          this.typhoonAnalysisListHeight = this.$refs.typhoonAnalysisTableBox.clientHeight;
        });
    },
    openSelectedRiskCondition() {
      this.$nextTick(() => {
        this.companiesListHeight = this.$refs.selectedRiskTableBox.clientHeight;
      });
    },
    showSeawallRiskProjectDetail() {
      this.$nextTick(() => {
        this.companiesListHeight = this.$refs.selectedRiskTableBox.clientHeight;
      });
    },
    waterTypeSelected() {
      if (!this.waterStartTime || !this.waterEndTime) {
        this.waterStartTime = '';
        this.waterEndTime = '';
      }
      this.waterKeyword = '';
      this.$nextTick(() => {
        this.waterListHeight = this.$refs.waterTableBox.clientHeight;
        this.getWaterAllList();
      });
    },
    openPrediction(val) {
      this.SetSpotsFlag(val);
      this.SetSpotsFlagType(2);
      localStorage.setItem('tabControl', 2);
    },
    openAnalysis(val) {
      if (!val) {
        this.clearAnalysisPointsLayer();
        this.clearCirlceLayer();
      }
      this.SetSpotsFlag(val);
      this.SetSpotsFlagType(1);
      localStorage.setItem('tabControl', 1);
    },
    openRescue(val) {
      if (val) {
        this.clearDispatch();
        this.SetIsAction(!this.IsAction);
        localStorage.setItem('tabControl', 0);
        if (this.IsAction == true) {
          this.SelectionTipsShow = true;
        } else {
          this.SetSpotsFlag(false);
        }
      } else {
        this.clearDispatch();
        this.SetIsAction(!this.IsAction);                  
        localStorage.setItem('tabControl', 0);
      }
    }
  },
  created() {
    this.leftTabbarChange(this.leftTabbarSelected);
    this.getResponseLevelArr();
  },
  mounted() {
    this.initMap();
  },
  destroyed() {},
  methods: {
    ...mapActions([
      'SetTyphoonPointData',
      'SetIsAction',
      'SetSpotsFlag',
      'SetSpotsFlagType',
      'SetTeamDispatchList',
      'SetWarehouseDispatchList',
      'SetDispatchCarousel'
    ]),
    // ------------------------------------- ↓头部标签↓ -------------------------------------
    /**
     * @description 左侧tabbar选中项切换时触发
     * @param type 当前选中的项
     */
    leftTabbarChange(type) {
      this.removeLayer();
      if (type === '工程列表') {
        this.getSeawallList();
        this.getReservoirList();
        this.getDikeList();
        this.getSluiceList();
      } else if (type === '雨情') {
        this.$nextTick(() => {
          this.rainListHeight = this.$refs.rainTableBox.clientHeight;
          this.getRainAllList();
        });
      } else if (type === '水情') {
        this.getWaterCentralRoleData();
        this.$nextTick(() => {
          this.waterListHeight = this.$refs.waterTableBox.clientHeight;
          this.getWaterAllList();
        });
      } else if (type === '台风分析') {
        this.initTyphoonMap();
        this.$nextTick(() => {
          this.typhoonListHeight = this.$refs.typhoonTableBox.clientHeight;
          this.getTyhoonHistoryList();
          this.getTyhoonCentralRoleData();
        });
      } else if (type === '雷达卫星') {
        this.displauRadarsatSatellite = true;
      } else if (type === '风险隐患') {
        this.getRiskHiddenDangerList();
        this.$nextTick(() => {
          this.companiesListHeight = this.$refs.selectedRiskTableBox.clientHeight;
        });
      } else if (type === '山洪灾害') {
        this.getMountainTorrentDisasterData();
      }
      this.showSeawallRiskProjectDetail = false;
    },
    /**
     * @description 获取响应等级下拉框数据
     */
    getResponseLevelArr() {
      disasterPreventionApi.responseLevel().then((res) => {
        if (res.code === 0) {
          this.responseLevelArr = res.data || [];
          this.responseLevelArr.forEach((item) => {
            if (item.whetherOpen == 1) {
              this.responseLevel = item.id;
            }
          });
        }
      });
    },
    /**
     * @description 响应等级切换时触发
     * @param level 当前响应等级
     */
    responseLevelChange(level) {
      this.responseLevel = level;
      this.openResponseLevelSelectPanel = false;
      disasterPreventionApi.updateResponseLevel({
        id: level
      });
    },
    /**
     * @description 三维地图弹窗关闭
     */
    HandleClose() {
      this.open3Dmap = false;
      this.$refs.map3D.showPointDialog = false;
      this.$refs.map3D.tideList = [];
      this.$refs.map3D.rainList = [];
      this.$refs.map3D.cameraList = [];
    },
    // 获取调度信息
    dispatchInfo(data, stationInfo) {
      const stationPos = [stationInfo.lng, stationInfo.lat];
      //绘制队伍路线
      this.getRouterLine([data.teamInfo.longitude, data.teamInfo.latitude], stationPos, 1, data.teamInfo);
      if (data.warehouses.length > 0) {
        //绘制仓库路线
        this.getRouterLine(
          [data.warehouses[0].longitude, data.warehouses[0].latitude],
          stationPos,
          2,
          data.warehouses[0]
        );
      }
      //设置测站终点
      this.pointLayer_station_end && this.map.removeLayer(this.pointLayer_station_end);
      this.pointLayer_station_end = drawRoutePoint(stationPos, 3);
      this.pointLayer_station_end.set('name', 'pointLayer_station_end');
      this.map.addLayer(this.pointLayer_station_end);
      this.SpotsFlag && this.SetSpotsFlag(false);
      this.SendMsgShow = true;
    },
    //获取路线的方法
    async getRouterLine(startPoint, endPoint, type, pointInfo) {
      await axios({
        url: `https://restapi.amap.com/v3/direction/driving?origin=${startPoint[0]},${startPoint[1]}&destination=${endPoint[0]},${endPoint[1]}&output=xml&key=${this.$config.gaoDeKey}&output=json`,
        method: 'get'
      }).then((res) => {
        if (res.data.route.paths.length > 0) {
          const route = res.data.route;
          const steps = route.paths[0].steps;
          let routeLine = [];
          steps.map((item) => {
            const tempArr = item.polyline.split(';');
            tempArr.map((child) => {
              let tempPoint = child.split(',');
              tempPoint[0] = Number(tempPoint[0]);
              tempPoint[1] = Number(tempPoint[1]);
              routeLine.push(tempPoint);
            });
          });
          if (type == '1') {
            this.addSchemeRanksOverlay();
            //队伍展示路线
            this.map.removeLayer(this.lineLayer_team);
            this.lineLayer_team = drawLine(routeLine, {
              lineColor: '#E10000',
              lineWidth: 4
            });
            this.lineLayer_team.set('name', 'lineLayer_team');
            this.map.addLayer(this.lineLayer_team);
            //存储队伍调度路线详情
            const teamInfoList = [];
            for (let i = 0; i < route.paths.length; i++) {
              const distance = Number(route.paths[i].distance) / 1000;
              const hour = Math.floor(Number(route.paths[i].duration) / 3600);
              const minute = Math.floor((Number(route.paths[i].duration) % 3600) / 60);
              const duration = hour + '小时' + +minute + '分钟';
              const routeInfoItem = {
                taxi_cost: route.taxi_cost,
                distance: distance,
                traffic_lights: route.paths[i].traffic_lights,
                duration: duration
              };
              teamInfoList.push(routeInfoItem);
            }
            this.SetTeamDispatchList(teamInfoList);
            if (pointInfo) {
              //人员调度
              const peoplePos = [pointInfo.longitude, pointInfo.latitude];
              //设置队伍起点
              this.pointLayer_team_start && this.map.removeLayer(this.pointLayer_team_start);
              this.pointLayer_team_start = drawRoutePoint(peoplePos, 1);
              this.pointLayer_team_start.set('name', 'pointLayer_team_start');
              // 队伍起点弹框
              this.schemeRanksInfo = {
                name: pointInfo.name,
                基本信息: [
                  {
                    label: '队伍人数',
                    value: `${pointInfo.existingNumber || '-'}`
                  },
                  {
                    label: '队伍责任人',
                    value: `${pointInfo.chargePerson || '-'}`
                  },
                  {
                    label: '联系电话',
                    value: `${pointInfo.chargePhone || '-'}`
                  }
                ],
                调度信息: [
                  {
                    label: '距离',
                    value: teamInfoList.length > 0 ? `${teamInfoList[0].distance || '-'}km` : '-'
                  },
                  {
                    label: '时长',
                    value: teamInfoList.length > 0 ? `${teamInfoList[0].duration || '-'}` : '-'
                  }
                ]
              };
              this.map.addLayer(this.pointLayer_team_start);
              this.schemeRanksPoint = true;
              this.schemeRanksOverlay.setPosition(peoplePos);
            }
          } else {
            this.addSchemeWarehouseOverlay();
            //仓库展示路线
            this.map.removeLayer(this.lineLayer_warehouse);
            this.lineLayer_warehouse = drawLine(routeLine, {
              lineColor: '#079700',
              lineWidth: 4
            });
            this.lineLayer_warehouse.set('name', 'lineLayer_warehouse');
            this.map.addLayer(this.lineLayer_warehouse);
            //存储仓库调度路线详情
            const warehoureInfoList = [];
            for (let i = 0; i < route.paths.length; i++) {
              const distance = Number(route.paths[i].distance) / 1000;
              const hour = Math.floor(Number(route.paths[i].duration) / 3600);
              const minute = Math.floor((Number(route.paths[i].duration) % 3600) / 60);
              const duration = hour + '小时' + +minute + '分钟';
              const routeInfoItem = {
                taxi_cost: route.taxi_cost,
                distance: distance,
                traffic_lights: route.paths[i].traffic_lights,
                duration: duration
              };
              warehoureInfoList.push(routeInfoItem);
            }
            this.SetWarehouseDispatchList(warehoureInfoList);
            if (pointInfo) {
              //物资调度
              const wareHousePos = [pointInfo.longitude, pointInfo.latitude];
              //设置仓库起点
              this.pointLayer_warehouse_start && this.map.removeLayer(this.pointLayer_warehouse_start);
              this.pointLayer_warehouse_start = drawRoutePoint(wareHousePos, 2);
              this.pointLayer_warehouse_start.set('name', 'pointLayer_warehouse_start');
              // 仓库起点弹框
              this.schemeWarehouseInfo = {
                name: pointInfo.name,
                基本信息: [
                  {
                    label: '仓库负责人',
                    value: `${pointInfo.chargePerson || '-'}`
                  },
                  {
                    label: '联系电话',
                    value: `${pointInfo.chargePhone || '-'}`
                  }
                ],
                调度物资: this.GoodsList,
                调度信息: [
                  {
                    label: '距离',
                    value: warehoureInfoList.length > 0 ? `${warehoureInfoList[0].distance || '-'}km` : '-'
                  },
                  {
                    label: '时长',
                    value: warehoureInfoList.length > 0 ? `${warehoureInfoList[0].duration || '-'}` : '-'
                  }
                ]
              };
              this.map.addLayer(this.pointLayer_warehouse_start);
              this.schemeWarehousePoint = true;
              this.schemeWarehouseOverlay.setPosition(wareHousePos);
            }
          }
          // 调度结果轮播页面
          this.$nextTick(() => {
            this.SetDispatchCarousel(true);
          });
        }
      });
    },
    // 清除调度路线信息
    clearDispatch() {
      this.schemeWarehousePoint = false;
      this.schemeRanksPoint = false;
      this.pointLayer_team_start && this.map.removeLayer(this.pointLayer_team_start);
      this.pointLayer_warehouse_start && this.map.removeLayer(this.pointLayer_warehouse_start);
      this.pointLayer_station_end && this.map.removeLayer(this.pointLayer_station_end);
      this.lineLayer_team && this.map.removeLayer(this.lineLayer_team);
      this.lineLayer_warehouse && this.map.removeLayer(this.lineLayer_warehouse);
      this.DispatchCarousel && this.SetDispatchCarousel(false);
      this.SpotsFlag && this.SetSpotsFlag(false);
      this.IsAction && this.SetIsAction(false);
    },
    // 添加调度队伍弹框
    addSchemeRanksOverlay() {
      this.schemeRanksOverlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#schemeRanksPopup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.schemeRanksOverlay);
    },
    //添加调度仓库弹框
    addSchemeWarehouseOverlay() {
      this.schemeWarehouseOverlay = new Overlay({
        //设置弹出框的容器
        element: document.querySelector('#schemeWarehousePopup'),
        autoPan: true,
        autoPanAnimation: {
          duration: 250
        }
      });
      this.map.addOverlay(this.schemeWarehouseOverlay);
    },
    handleCloseNoThisMap(list, selectNode, center, time, zoom) {
      this.showAnalysisPoints(list, selectNode);
      this._drawCircleInMeter(center, time);
      this.flyPoint(center, zoom);
    },
    clearAnalysisPointsLayer() {
      if (this.analysisPointsLayer != null) this.map.removeLayer(this.analysisPointsLayer);
    },
    showAnalysisPoints(data) {
      this.clearAnalysisPointsLayer();
      const projectTypeArr = ['水库', '山塘', '堤防', '海塘', '泵站', '水闸', '闸站', '灌区', '农饮水', '水电站'];
      const stationTypeArr = [
        '水库水位',
        '河道水位',
        '堰闸水位',
        '潮位',
        '断面',
        '闸门开度',
        '水质监测',
        '积水',
        '视频'
      ];
      const rainTypeArr = ['雨情'];
      const imgObj = {
        水库: require('@/assets/images/map_legends/map-reservoir.png'),
        山塘: require('@/assets/images/map_legends/map-pool.png'),
        堤防: require('@/assets/images/MapPoint/point-dike-normal.png'),
        海塘: require('@/assets/images/MapPoint/point-seawall-normal.png'),
        泵站: require('@/assets/images/map_legends/map-pump.png'),
        水闸: require('@/assets/images/map_legends/map-sluice.png'),
        闸站: require('@/assets/images/map_legends/map-sluicestation.png'),
        水电站: require('@/assets/images/map_legends/map-hydropower.png'),
        农饮水: require('@/assets/images/map_legends/map-farmwater.png'),
        灌区: require('@/assets/images/map_legends/map-irrigated.png'),
        水库水位: require('@/assets/images/map_legends/map-reservoirwater.png'),
        河道水位: require('@/assets/images/map_legends/map-riverwater.png'),
        堰闸水位: require('@/assets/images/map_legends/map-checkgate.png'),
        潮位: require('@/assets/images/map_legends/map-sealevel.png'),
        闸门开度: require('@/assets/images/map_legends/map-sluicework.png'),
        水质监测: require('@/assets/images/map_legends/map-waterquality.png'),
        断面: require('@/assets/images/map_legends/map-waterquality.png'),
        积水: require('@/assets/images/map_legends/map-hydrops.png'),
        视频: require('@/assets/images/map_legends/map-video.png')
      };
      data.map((item) => {
        if (item.analysisType == '雨情') {
          let src = '';
          if (item.rainfall <= 0) {
            src = require('@/assets/images/legend/rain_0.png');
          } else if (item.rainfall > 0 && item.rainfall < 10) {
            src = require('@/assets/images/legend/rain_10.png');
          } else if (item.rainfall >= 10 && item.rainfall < 25) {
            src = require('@/assets/images/legend/rain_10_25.png');
          } else if (item.rainfall >= 25 && item.rainfall < 50) {
            src = require('@/assets/images/legend/rain_25_50.png');
          } else if (item.rainfall >= 50 && item.rainfall < 100) {
            src = require('@/assets/images/legend/rain_50_100.png');
          } else if (item.rainfall >= 100 && item.rainfall < 250) {
            src = require('@/assets/images/legend/rain_100_250.png');
          } else {
            src = require('@/assets/images/legend/rain_250.png');
          }
          item.src = src;
        } else {
          item.src = imgObj[item.analysisType];
        }
      });
      this.analysisPointsLayer = drawAnaylysiscetPoint(data);
      this.map.addLayer(this.analysisPointsLayer);
      const selectClick = addLayerClick(this.analysisPointsLayer);
      selectClick.on('select', (e) => {
        const features = e.target.getFeatures().getArray();
        if (features.length > 0) {
          const featureProperties = features[0].getProperties().featureProperties;
          const type = featureProperties.type;
          if (projectTypeArr.includes(type)) {
            console.log('showProjectDialog', featureProperties.data, featureProperties);
          } else if (stationTypeArr.includes(type) || rainTypeArr.includes(type)) {
            console.log('showDialog', featureProperties.data);
          }
        }
      });
      this.map.addInteraction(selectClick);
    },
    //根据距离画圈
    _drawCircleInMeter(center, radius) {
      this.clearCirlceLayer();
      this.circleLayer = drawCircleInMeter(center, radius, this.map);
      this.map.addLayer(this.circleLayer);
      this.flyPoint(center, 14);
    },
    clearCirlceLayer() {
      this.map.removeLayer(this.circleLayer);
      this.circleLayer = null;
      // this.flyPoint([mapConfig.lng, mapConfig.lat], mapConfig.zoom);
    },
    //定位到对应位置和层级
    flyPoint(center, zoom) {
      const view = this.map.getView();
      view.animate({
        center: center,
        duration: 300,
        zoom: zoom
      });
    },
    // ------------------------------------- ↑头部标签↑ -------------------------------------

    // ------------------------------------- ↓地图相关↓ -------------------------------------
    /**
     * @description 初始化地图
     */
    initMap() {
      // hover弹窗
      this.hoverOverlay = new Overlay({
        element: this.$refs.hoverPopup, // 弹窗标签，在html里
        autoPan: false, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250
        },
        positioning: 'bottom-center'
        // offset: [10, -4],
      });
      this.map = new Map({
        target: 'aLuchengMapContainer',
        controls: defaultControls({ zoom: false }).extend([]),
        layers: [this.baseLayer, this.lcBorderLayer, this.lcRiverLayer],
        overlays: [this.hoverOverlay],
        view: new View({
          projection: 'EPSG:4326',
          center: [120.54, 28.08],
          zoom: 12,
          maxZoom: 16,
          minZoom: 3
        })
      });
      this.clickMap(); // 地图点击事件
      this.pointerMoveInMap(); // 地图鼠标移动事件
    },
    /**
     * @description 初始化台风地图
     */
    initTyphoonMap() {
      this.lcBorderLayer.setVisible(false);
      this.lcRiverLayer.setVisible(false);
      this.map.getView().setZoom(6);
      this.addTyphoonPointOverlay();
    },
    /**
     * @description 地图点击事件
     */
    clickMap() {
      this.map.on('singleclick', (evt) => {
        // console.log([evt.coordinate[0], evt.coordinate[1]]);
        const pixel = this.map.getEventPixel(evt.originalEvent);
        const feature = this.map.forEachFeatureAtPixel(pixel, (features) => features);
        if (this.SpotsFlag) {
          this.dispatchLat = evt.coordinate[1].toFixed(3);
          this.dispatchLng = evt.coordinate[0].toFixed(3);
          this.emergencySchemeShow = true;
          this.openPrediction = false;
          return;
        }
        console.log(feature, 'featurefeaturefeature');
        if (feature) {
          if (feature.values_.layerName === 'seawallPointsLayers') {
            // 点击海塘落点
            this.projectPointData = feature.values_.data || {};
            this.projectPointData.projectType = 'seawall';
            this.projectPointData.code = this.projectPointData.seawallCode;
            this.projectPointData.name = this.projectPointData.seawallName;
            this.sluiceDialogForSchedule = true;
          } else if (feature.values_.layerName === 'reservoirPointsLayers') {
            // 点击水库落点
            this.projectPointData = feature.values_.data || {};
            this.projectPointData.projectType = 'reservoir';
            this.projectPointData.projectCode = this.projectPointData.resCode;
            this.projectPointData.name = this.projectPointData.resName;
            this.sluiceDialogForSchedule = true;
          } else if (feature.values_.layerName === 'dikePointsLayers') {
            // 点击堤防落点
            this.projectPointData = feature.values_.data || {};
            this.projectPointData.projectType = 'dike';
            this.projectPointData.projectCode = this.projectPointData.dikeCode;
            this.projectPointData.name = this.projectPointData.dikeName;
            this.sluiceDialogForSchedule = true;
          } else if (feature.values_.layerName === 'sluicePointsLayers') {
            // 点击水闸落点
            this.projectPointData = feature.values_.data || {};
            this.projectPointData.projectType = 'sluice';
            this.projectPointData.projectCode = this.projectPointData.wagaCode;
            this.projectPointData.name = this.projectPointData.wagaName;
            this.sluiceDialogForSchedule = true;
          } else if (feature.values_.layerName === 'rainfallPointsLayers') {
            // 点击雨情落点
            this.rainStationPointData = feature.values_.data || {};
            this.rainStationPointData.name = this.rainStationPointData.stationName;
            this.rainStationPointData.code = this.rainStationPointData.stcd;
            this.$nextTick(() => {
              if (this.previewModule) {
                this.$refs.rainDialog.getPreviewRainStationInfo();
              } else {
                this.$refs.rainDialog.getRainStationInfo();
              }
            });
            this.waterRainDialog = true;
          } else if (feature.values_.layerName === 'waterRegimePointsLayers') {
            // 点击水情落点
            this.waterStationPointData = feature.values_.data || {};
            this.waterStationPointData.name = this.waterStationPointData.stName;
            this.waterStationPointData.code = this.waterStationPointData.stCode;
            this.waterPointDialogShow = true;
          } else if (
            feature.values_.layerName === 'transfiniteReservoirPointsLayers' ||
            feature.values_.layerName === 'overalarmRiverNetworkPointsLayers' ||
            feature.values_.layerName === 'overalarmWeirGatePointsLayers'
          ) {
            // 点击风险隐患落点（不包括海塘风险）
            this.waterStationPointData = feature.values_.data || {};
            this.waterStationPointData.name = this.waterStationPointData.stName;
            this.waterStationPointData.code = this.waterStationPointData.stCode;
            this.waterPointDialogShow = true;
          } else if (feature.values_.layerName === 'seawallRiskPointsLayers') {
            // 点击风险隐患-海塘风险落点
            this.seawallRiskListCurrentChange(feature.values_.data || {});
            this.$refs.selectedRiskTable.setCurrentRow(feature.values_.data || {});
          } else if (feature.values_.layerName === 'mountainTorrentDisasterPointsLayers') {
            // 点击山洪灾害落点
            this.mountainTorrentDisasterPointData = feature.values_.data || {};
            this.watershedDisasterRiskAssessment = true;
          }
        }
      });
    },
    /**
     * @description 地图鼠标移入触发事件
     */
    pointerMoveInMap() {
      this.map.on('pointermove', (e) => {
        const pixel = this.map.getEventPixel(e.originalEvent);
        this.map.getTargetElement().style.cursor = this.map.hasFeatureAtPixel(pixel) ? 'pointer' : ''; // 手指效果
        const feature = this.map.forEachFeatureAtPixel(pixel, (feat) => feat);
        this.featureValues = feature ? feature.values_ : {};
        if (feature) {
          if (this.featureValues.data) {
            if (this.featureValues.layerName === 'seawallPointsLayers') {
              this.drawFloatingWindow(this.featureValues.data, e.coordinate);
            } else {
              this.drawFloatingWindow(this.featureValues.data);
            }
          }
        } else {
          this.removeFloatingWindow();
        }
      });
    },
    removeLineLayer() {
      this.lineLayer.forEach((item) => {
        this.map.removeLayer(item);
      });
    },
    /**
     * @description 海塘画线
     * @param pointsInfo 点位信息
     * @param layerName 图层名称
     */
    _drawLineEx(coordinates, styleObj = {}, layerName = 'lineLayer') {
      if (!coordinates.length > 0) return;
      let layer = drawLineEx(coordinates, styleObj, layerName);
      this.lineLayer.push(layer);
      // layer.set('name', layerName);
      layer.setZIndex(99);
      this.map.addLayer(layer);
    },

    /**
     * @description 地图打点
     * @param pointsInfo 点位信息
     * @param layerName 图层名称
     */
    drawPoint(pointsInfo, layerName) {
      const vectorSource = new VectorSource({});
      pointsInfo.forEach((e) => {
        const lat = e.latitude || e.lowLeftLat || e.upLeftLon || e.dikestartlatitude || e.startLat || e.stLat || e.lat;
        const lng =
          e.longitude || e.lowLeftLong || e.upRightLat || e.dikestartlongitude || e.startLong || e.stLong || e.lon;
        const point = [Number(lng), Number(lat)];
        const iconFeature = new Feature({
          geometry: new Point(point),
          data: e || null,
          layerName
        });
        let style = {};
        if (e.styleObj.src && e.styleObj.src !== '') {
          style = {
            image: new Icon({
              anchor: e.styleObj.anchor || [0.5, 0.5],
              anchorXUnits: e.styleObj.anchorXUnits || 'fraction',
              anchorYUnits: e.styleObj.anchorYUnits || 'fraction',
              src: e.styleObj.src,
              scale: e.styleObj.scale || 1
            })
          };
        }
        if (e.styleObj.circleIconColorArr) {
          const rainfall = e.drp || 0;
          const colorArr = e.styleObj.circleIconColorArr || [];
          style = {
            image: new Circle({
              radius: 8,
              fill: new Fill({
                color:
                  rainfall >= 250
                    ? colorArr[0]
                    : rainfall >= 100
                    ? colorArr[1]
                    : rainfall >= 50
                    ? colorArr[2]
                    : rainfall >= 25
                    ? colorArr[3]
                    : rainfall >= 10
                    ? colorArr[4]
                    : rainfall > 0
                    ? colorArr[5]
                    : colorArr[6]
              })
            })
          };
        }
        if (e.styleObj.title && e.styleObj.title !== '') {
          style.text = new Text({
            textAlign: 'center',
            textBaseline: 'middle',
            font: 'normal 12px 微软雅黑',
            text: e.title,
            fill: new Fill({
              color: '#333'
            }),
            offsetX: -18,
            offsetY: 46,
            scale: 1
          });
        }
        const iconStyle = new Style(style);
        iconFeature.setStyle(iconStyle);
        vectorSource.addFeature(iconFeature);
      });
      const vectorLayer = new VectorLayer({
        source: vectorSource
      });
      this.mapLayers[layerName] = vectorLayer;
      this.mapLayers[layerName].setZIndex(99);
      this.map.addLayer(this.mapLayers[layerName]);
    },
    /**
     * @description 移除地图图层
     * @param layerName 指定移除图层的名称,不传时移除所有添加的图层
     */
    removeLayer(layerName) {
      if (layerName) {
        if (this.mapLayers[layerName] != null) {
          this.map.removeLayer(this.mapLayers[layerName]);
          this.mapLayers[layerName] = null;
        }
      } else {
        const layerNames = Object.keys(this.mapLayers);
        layerNames.forEach((e) => {
          if (this.mapLayers[e] != null) {
            this.map.removeLayer(this.mapLayers[e]);
            this.mapLayers[e] = null;
          }
        });
      }
    },
    /**
     * @description 绘制点位浮窗
     * @param pointData 点位信息
     */
    drawFloatingWindow(pointData, points) {
      const scale = pointData.styleObj.scale || 1;
      const lng =
        pointData.longitude ||
        pointData.lowLeftLong ||
        pointData.upRightLat ||
        pointData.dikestartlongitude ||
        pointData.startLong ||
        pointData.stLong ||
        pointData.lon;
      const lat =
        pointData.latitude ||
        pointData.lowLeftLat ||
        pointData.upLeftLon ||
        pointData.dikestartlatitude ||
        pointData.startLat ||
        pointData.stLat ||
        pointData.lat;
      if (points && points.length > 0) {
        this.hoverOverlay.setPosition(points);
      } else {
        this.hoverOverlay.setPosition([Number(lng), Number(lat)]);
      }
      this.hoverOverlay.setOffset([0, -19 * scale]);
    },
    /**
     * @description 移除浮窗图层
     */
    removeFloatingWindow() {
      this.hoverOverlay.setPosition(undefined);
    },
    // ------------------------------------- ↑地图相关↑ -------------------------------------

    // ------------------------------------- ↓工程列表↓ -------------------------------------
    /**
     * @description 获取海塘列表数据
     */
    getSeawallList() {
      this.seawallList = [];
      hydraulicApi.getProjects({ type: '海塘' }).then((res) => {
        if (res.code === 0) {
          this.seawallList = res.data || [];
        }
      });
    },
    /**
     * @description 获取水库列表数据
     */
    getReservoirList() {
      this.reservoirList = [];
      hydraulicApi.getProjects({ type: '水库', withWaterLevel: true }).then((res) => {
        if (res.code === 0) {
          this.reservoirList = res.data || [];
        }
      });
    },
    /**
     * @description 获取堤防列表数据
     */
    getDikeList() {
      this.dikeList = [];
      hydraulicApi.getProjects({ type: '堤防' }).then((res) => {
        if (res.code === 0) {
          this.dikeList = res.data || [];
        }
      });
    },
    /**
     * @description 获取水闸列表数据
     */
    getSluiceList() {
      this.sluiceList = [];
      hydraulicApi.getProjects({ type: '水闸', withWaterLevel: true }).then((res) => {
        if (res.code === 0) {
          this.sluiceList = res.data || [];
        }
      });
    },
    /**
     * @description 海塘列表选择项发生变化时触发
     */
    seawallListSelectionChange(selection) {
      this.removeLineLayer();
      console.log(selection, 'dasdasdasdsadsa');
      for (let i = 0; i < selection.length; i++) {
        let seawall = selection[i];
        let color = '#E80000';
        if (seawall.designTideStandard < 10) {
          color = '#E80000';
        } else if (seawall.designTideStandard == 10) {
          color = '#DD00DB';
        } else if (seawall.designTideStandard == 20) {
          color = '#E89100';
        } else if (seawall.designTideStandard == 50) {
          color = '#EEC80B';
        } else if (seawall.designTideStandard == 100) {
          color = '#19AF47';
        } else if (seawall.designTideStandard > 100) {
          color = '#4CEBC7';
        } else if (seawall.designTideStandard == null) {
          color = '#8D0A0A';
        }
        let style = {
          lineWidth: 6,
          lineColor: color
        };
        let lineString = (seawall.coorpot && seawall.coorpot.replace(/&quot;/g, "'")) || '[]';
        let lineList = lineString && eval(lineString);
        lineList = lineList.map((line) => {
          line = line.map((item) => {
            return [item.lng, item.lat];
          });
          return {
            lineList: line,
            data: {
              styleObj: {
                src: require('@/assets/new_images/map-point-seawall.png'),
                srcPoly: require('@/assets/new_images/map-point-seawall-poly.png')
              },
              ...seawall
            }
          };
        });
        this._drawLineEx(lineList, style, 'seawallPointsLayers');
      }
      // this.drawPoint(
      //   selection.map((e) => {
      //     e.styleObj = {
      //       src: require('@/assets/new_images/map-point-seawall.png'),
      //       srcPoly: require('@/assets/new_images/map-point-seawall-poly.png')
      //     };
      //     return e;
      //   }),
      //   'seawallPointsLayers'
      // );
    },
    /**
     * @description 水库列表选择项发生变化时触发
     */
    reservoirListSelectionChange(selection) {
      this.removeLayer('reservoirPointsLayers');
      this.drawPoint(
        selection.map((e) => {
          e.styleObj = {
            src: require('@/assets/new_images/map-point-reservoir.png'),
            srcPoly: require('@/assets/new_images/map-point-reservoir-poly.png')
          };
          return e;
        }),
        'reservoirPointsLayers'
      );
    },
    /**
     * @description 堤防列表选择项发生变化时触发
     */
    dikeListSelectionChange(selection) {
      this.removeLayer('dikePointsLayers');
      this.drawPoint(
        selection.map((e) => {
          e.styleObj = {
            src: require('@/assets/new_images/map-point-dike.png'),
            srcPoly: require('@/assets/new_images/map-point-dike-poly.png')
          };
          return e;
        }),
        'dikePointsLayers'
      );
    },
    /**
     * @description 水闸列表选择项发生变化时触发
     */
    sluiceListSelectionChange(selection) {
      this.removeLayer('sluicePointsLayers');
      this.drawPoint(
        selection.map((e) => {
          e.styleObj = {
            src: require('@/assets/new_images/map-point-sluice.png'),
            srcPoly: require('@/assets/new_images/map-point-sluice-poly.png')
          };
          return e;
        }),
        'sluicePointsLayers'
      );
    },
    // ------------------------------------- ↑工程列表↑ -------------------------------------

    // ------------------------------------- ↓雨情相关↓ -------------------------------------
    /**
     * @description 获取雨情全部列表数据
     */
    getRainAllList() {
      if (this.isLoadingIsosurface) {
        this.displayLoadingIsosurface();
      }
      this.rainAllList = [];
      this.rainAllListLoading = true;
      rainApi
        .realRain({
          minutes: this.rainDuration,
          start: this.rainStartTime,
          end: this.rainEndTime,
          min: this.rainRange === '' ? undefined : this.rainRange.split('-')[0],
          max: this.rainRange === '' ? undefined : this.rainRange.split('-')[1],
          stationName: this.rainKeyword
        })
        .then((res) => {
          if (res.code === 0) {
            this.rainAllList = res.data || [];
            this.rainList = this.rainAllList.filter((e) => e.drp && Number(e.drp) > 0);
          }
          this.rainAllListLoading = false;
          if (this.isDropPoint) {
            this.mapDropPoint();
          }
        });
    },
    /**
     * @description 是否加载降雨等值面
     */
    displayLoadingIsosurface() {
      if (this.isLoadingIsosurface) {
        this.map.removeLayer(this.isosurfaceLayer);
        const SURFACE_FILL_COLOR = [
          'rgba(0, 160, 255, 0.5)',
          'rgba(75, 199, 4, 0.5)',
          'rgba(254, 203, 110, 0.5)',
          'rgba(255, 138, 33, 0.5)',
          'rgba(254, 50, 58, 0.5)',
          'rgba(243, 47, 223, 0.5)'
        ];
        const dataInterval = [10, 25, 50, 100, 250, 100000];
        const inRange = (value) => {
          return dataInterval.findIndex((item) => {
            return value < item;
          });
        };
        let startTime = this.rainStartTime;
        let endTime = this.rainEndTime;
        let nowTemp = new Date();
        if (!endTime || endTime === '') {
          endTime = nowTemp.format('yyyy-MM-dd hh:mm:ss');
        }
        if (!startTime || startTime === '') {
          startTime = new Date(nowTemp - this.rainDuration * 60 * 1000).format('yyyy-MM-dd hh:mm:ss');
        }
        rainApi
          .rainfallSurface({
            startTime,
            endTime,
            dataInterval: [-5, 0, ...dataInterval].join()
          })
          .then((res) => {
            if (res.code == 0) {
              const geojson = new GeoJSON();
              const featuresPolygon = geojson.readFeatures(res.data.geoJsonPogyLon);
              const featuresPolyline = res.data.geoJsonPolyLine
                ? geojson.readFeatures(res.data.geoJsonPolyLine)
                : undefined;
              const source = new VectorSource({ features: featuresPolygon });
              featuresPolygon.forEach((feature) => {
                const { hvalue, lvalue } = feature.getProperties();
                if (lvalue < 0) {
                  source.removeFeature(feature);
                }
              });
              const polygonLayer = new VectorLayer({
                source,
                style: (f) => {
                  const { hvalue, lvalue } = f.getProperties();
                  return new Style({
                    fill: new Fill({ color: SURFACE_FILL_COLOR[inRange(lvalue)] })
                  });
                },
                zIndex: 10
              });
              const polylineLayer = new VectorLayer({
                source: new VectorSource({ features: featuresPolyline }),
                style: (f) => {
                  const { value } = f.getProperties();
                  return new Style({
                    stroke: new Stroke({
                      color: 'red',
                      width: 2
                    }),
                    text: new Text({
                      text: `${value}`,
                      font: '12px bold Calibri,sans-serif',
                      fill: new Fill({ color: '#000' })
                    })
                  });
                },
                zIndex: 10
              });
              this.isosurfaceLayer = new LayerGroup({
                layers: [polygonLayer, polylineLayer]
              });
              this.map.addLayer(this.isosurfaceLayer);
            }
          });
      } else {
        this.map.removeLayer(this.isosurfaceLayer);
        this.isosurfaceLayer = null;
      }
    },
    /**
     * @description 重置雨情条件筛选
     */
    rainConditionReset() {
      this.rainStartTime = '';
      this.rainEndTime = '';
      this.rainDuration = 60;
      this.rainRange = '';
      this.isLoadingIsosurface = false;
      this.tabSeleted = '全部';
      this.rainKeyword = '';
      this.getRainAllList();
    },
    /**
     * @description 雨情地图落点
     */
    mapDropPoint() {
      this.isDropPoint = true;
      this.removeLayer('rainfallPointsLayers');
      this.drawPoint(
        this.rainAllList.map((e) => {
          e.styleObj = {
            circleIconColorArr: ['#F32FDF', '#FE323A', '#FF8A21', '#FFCC6F', '#4CC704', '#00A0FF', '#B2B2B2']
          };
          return e;
        }),
        'rainfallPointsLayers'
      );
    },
    /**
     * @description 移除雨情地图落点
     */
    mapRemovePoint() {
      this.isDropPoint = false;
      this.removeLayer('rainfallPointsLayers');
    },
    /**
     * @description 雨量报表弹窗关闭时触发
     */
    handleRainClose() {
      this.visibleRainDialog = false;
      this.$nextTick(() => {
        this.$refs.rainfallReportContent.rainTime = '';
        this.$refs.rainfallReportContent.minRain = '';
        this.$refs.rainfallReportContent.maxRain = '';
        this.$refs.rainfallReportContent.rainTableData = [];
      });
    },
    // ------------------------------------- ↑雨情相关↑ -------------------------------------

    // ------------------------------------- ↓水情相关↓ -------------------------------------
    /**
     * @description 重置水情条件筛选
     */
    waterConditionReset() {
      this.waterStartTime = '';
      this.waterEndTime = '';
      this.reservoirScale = '';
      this.tabSeleted2 = '全部';
      this.waterKeyword = '';
      this.getWaterAllList();
    },
    /**
     * @description 水情查询时间改变时触发
     */
    waterTimeChange() {
      // 水情日期时间条件，只有一个有值时不进行表格查询
      if (this.waterStartTime && this.waterEndTime) {
        this.getWaterAllList();
      } else if (!this.waterStartTime && !this.waterStartTime) {
        this.getWaterAllList();
      }
    },
    /**
     * @description 获取水情列表数据
     */
    getWaterAllList() {
      let params = {};
      params.dataTime =
        this.waterStartTime && this.waterEndTime ? this.waterStartTime + ' ' + this.waterEndTime + ':00' : '';
      params.stationName = this.waterKeyword;
      if (this.waterTypeSelected === '水库水位') {
        params.stationType = 'RR';
        params.engScal = this.reservoirScale ? this.reservoirScale : undefined;
        params.bindProj = '';
        params.main = '';
        params.isFocus = '';
      } else if (this.waterTypeSelected === '河道水位') {
        params.stationType = 'ZZ';
        params.riverStationType = '';
        params.isFocus = '';
      } else if (this.waterTypeSelected === '堰闸水位') {
        params.stationType = 'DD';
        params.isFocus = '';
      } else if (this.waterTypeSelected === '潮位') {
        params.stationType = 'TT';
        params.isFocus = '';
      }
      this.waterAllList = [];
      this.waterAllListLoading = true;
      workApi.mapPoint(params).then((res) => {
        if (res.code === 0) {
          this.waterAllList = res.data || [];
          this.waterList = this.waterAllList.filter((e) => e.warning);
        }
        this.waterAllListLoading = false;
        if (this.isWaterDropPoint) {
          this.mapWaterDropPoint();
        }
      });
    },
    /**
     * @description 获取水情C位展示数据
     */
    getWaterCentralRoleData() {
      this.waterCentralRoleData = {};
      this.centralRoleLoading = true;
      Promise.all([
        workApi.mapPoint({ stationType: 'RR' }),
        workApi.mapPoint({ stationType: 'ZZ' }),
        workApi.mapPoint({ stationType: 'DD' }),
        workApi.mapPoint({ stationType: 'TT' })
      ]).then(([res1, res2, res3, res4]) => {
        if (res1.code === 0) {
          this.waterCentralRoleData.rrTotal = (res1.data || []).length;
          this.waterCentralRoleData.rrWarn = (res1.data || []).filter((e) => e.warning).length;
        }
        if (res2.code === 0) {
          this.waterCentralRoleData.zzTotal = (res2.data || []).length;
          this.waterCentralRoleData.zzWarn = (res2.data || []).filter((e) => e.warning).length;
        }
        if (res3.code === 0) {
          this.waterCentralRoleData.ddTotal = (res3.data || []).length;
          this.waterCentralRoleData.ddWarn = (res3.data || []).filter((e) => e.warning).length;
        }
        if (res4.code === 0) {
          this.waterCentralRoleData.ttTotal = (res4.data || []).length;
          this.waterCentralRoleData.ttWarn = (res4.data || []).filter((e) => e.warning).length;
        }
        this.centralRoleLoading = false;
      });
    },
    /**
     * @description 水情地图落点
     */
    mapWaterDropPoint() {
      this.isWaterDropPoint = true;
      this.removeLayer('waterRegimePointsLayers');
      this.drawPoint(
        this.waterAllList.map((e) => {
          if (!e.warning) {
            if (this.waterTypeSelected === '水库水位') {
              e.styleObj = {
                src: require('@/assets/new_images/map-point-reservoir.png'),
                srcPoly: require('@/assets/new_images/map-point-reservoir-poly.png')
              };
            } else if (this.waterTypeSelected === '河道水位') {
              e.styleObj = {
                src: require('@/assets/new_images/map-point-river.png'),
                srcPoly: require('@/assets/new_images/map-point-river-poly.png')
              };
            } else if (this.waterTypeSelected === '堰闸水位') {
              e.styleObj = {
                src: require('@/assets/new_images/map-point-weirgate.png'),
                srcPoly: require('@/assets/new_images/map-point-weirgate-poly.png')
              };
            } else if (this.waterTypeSelected === '潮位') {
              e.styleObj = {
                src: require('@/assets/new_images/map-point-tidallevel.png'),
                srcPoly: require('@/assets/new_images/map-point-tidallevel-poly.png')
              };
            }
          } else {
            if (this.waterTypeSelected === '水库水位') {
              e.styleObj = {
                src: require('@/assets/new_images/map-point-reservoir-warn.png'),
                srcPoly: require('@/assets/new_images/map-point-reservoir-warn-poly.png')
              };
            } else if (this.waterTypeSelected === '河道水位') {
              e.styleObj = {
                src: require('@/assets/new_images/map-point-river-warn.png'),
                srcPoly: require('@/assets/new_images/map-point-river-warn-poly.png')
              };
            } else if (this.waterTypeSelected === '堰闸水位') {
              e.styleObj = {
                src: require('@/assets/new_images/map-point-weirgate-warn.png'),
                srcPoly: require('@/assets/new_images/map-point-weirgate-warn-poly.png')
              };
            } else if (this.waterTypeSelected === '潮位') {
              e.styleObj = {
                src: require('@/assets/new_images/map-point-tidallevel-warn.png'),
                srcPoly: require('@/assets/new_images/map-point-tidallevel-warn-poly.png')
              };
            }
          }
          return e;
        }),
        'waterRegimePointsLayers'
      );
    },
    /**
     * @description 移除水情地图落点
     */
    mapWaterRemovePoint() {
      this.isWaterDropPoint = false;
      this.removeLayer('waterRegimePointsLayers');
    },
    // ------------------------------------- ↑水情相关↑ -------------------------------------

    // ------------------------------------- ↓台风相关↓ -------------------------------------
    /**
     * @descritpion 台风检索条件重置
     */
    typhoonConditionReset() {
      this.openTyphoonCondition = false;
      this.hasImpact = 1;
      this.typhoonNameKeyword = '';
      this.typhoonYear = '';
      this.typhoonStartDate = '';
      this.typhoonEndDate = '';
      this.typhoonLevelMin = '';
      this.typhoonLevelMax = '';
      this.typhoonSpeedMin = '';
      this.typhoonSpeedMax = '';
      this.getTyhoonHistoryList();
    },
    /**
     * @description 获取台风表格数据
     */
    getTyhoonHistoryList() {
      let params = {};
      if (this.openTyphoonCondition) {
        // 台风高级查询
        if (!this.typhoonStartDate || this.typhoonStartDate === '') {
          this.$message.warning('请选择查询的起始时间');
          return;
        }
        if (!this.typhoonEndDate || this.typhoonEndDate === '') {
          this.$message.warning('请选择查询的终止时间');
          return;
        }
        params = {
          affectedWz: this.hasImpact,
          name: this.typhoonNameKeyword,
          minLandingLevel: this.typhoonLevelMin ? this.typhoonLevelMin : '',
          maxLandingLevel: this.typhoonLevelMax ? this.typhoonLevelMax : '',
          minWindExtremum: this.typhoonSpeedMin ? this.typhoonSpeedMin : '',
          maxWindExtremum: this.typhoonSpeedMax ? this.typhoonSpeedMax : '',
          begin: this.typhoonStartDate,
          end: this.typhoonEndDate
        };
      } else {
        // 台风基础查询
        params = {
          affectedWz: this.hasImpact,
          name: this.typhoonNameKeyword,
          begin: this.typhoonYear !== '' ? this.typhoonYear + '-01' : '',
          end: this.typhoonYear !== '' ? this.typhoonYear + '-12' : ''
        };
      }
      this.typhoonListLoading = true;
      this.clearTyphoonListSelected();
      getTyhoonList(params).then((res) => {
        if (res.code === 0) {
          this.typhoonList = res.data || [];
        }
        this.typhoonListLoading = false;
        if (this.typhoonList.length > 0 && this.typhoonList[0].current) {
          let row = this.typhoonList[0];
          this.$nextTick(() => {
            this.$refs.typhoonList.toggleRowSelection(row, true);
            getTyphoonDetail({ code: row.code }).then((res) => {
              if (res.code === 0) {
                this.drawTyphoonRoute(res.data || {});
              }
            });
          });
        }
      });
    },
    /**
     * @description 获取相似台风表格数据
     */
    getTyphoonAnalysisList() {
      this.typhoonAnalysisListLoading = true;
      this.clearTyphoonAnalysisListSelected();
      getSimilarTyphoons({
        code: this.analysisTyphoonCode[1] || '',
        type: this.analysisType
      }).then((res) => {
        if (res.code === 0) {
          this.typhoonAnalysisList = res.data.data || [];
        }
        this.typhoonAnalysisListLoading = false;
        if (this.analysisTyphoonCode[1]) {
          getTyphoonDetail({ code: this.analysisTyphoonCode[1] }).then((res) => {
            if (res.code === 0) {
              this.drawTyphoonRoute(res.data || {});
            }
          });
        }
      });
    },
    /**
     * @description 获取台风C位框内数据
     */
    getTyhoonCentralRoleData() {
      this.hasTyphoonLoading = true;
      getTyphoonStat4C().then((res) => {
        if (res.code === 0) {
          const data = res.data || {};
          this.hasTyphoon = !!data.code;
          this.displayCentralRole = this.hasTyphoon;
          this.tyhoonCentralRoleName = data.name || '';
          this.tyhoonCentralRoleLevel = data.power || '';
          this.tyhoonCentralRoleDistance = data.distance || '';
          this.tyhoonCentralRoleEffectProject = data.projectNum === 0 ? 0 : data.projectNum || '';
        }
        this.hasTyphoonLoading = false;
      });
    },
    /**
     * @description 台风列表勾选事件
     */
    typhoonListRowSeletedChanged(selection, row) {
      row.code = row.code || row.tfbh;
      if (selection.map((e) => e.code).includes(row.code)) {
        // 手动勾选->绘制台风路径
        getTyphoonDetail({ code: row.code }).then((res) => {
          if (res.code === 0) {
            this.drawTyphoonRoute(res.data || {});
          }
        });
      } else {
        // 取消选中->移除台风路径
        this.removeTyphoonRoute(row.code);
      }
    },
    /**
     * @description 清除台风列表的所有勾选
     */
    clearTyphoonListSelected() {
      this.$refs.typhoonList.clearSelection();
      this.removeTyphoonRoute(null);
    },
    /**
     * @description 清除分析台风列表的所有勾选
     */
    clearTyphoonAnalysisListSelected() {
      this.$refs.typhoonAnalysisList.clearSelection();
      this.removeTyphoonRoute(null);
    },
    /**
     * @description 绘制台风路径
     * @param {Object} data 台风基础数据和点位数据
     */
    drawTyphoonRoute(data) {
      const points = JSON.parse(data.points);
      const typhoon = new newTyphoon(this.map, { ...data, points }, this.typhoonsMap, this);
      typhoon.init();
    },
    addTyphoonPointOverlay() {
      this.pointOverlay = new Overlay({
        element: this.$refs.typhoonPopup, // 弹窗标签，在html里
        autoPan: true, // 如果弹窗在底图边缘时，底图会移动
        autoPanAnimation: {
          // 底图移动动画
          duration: 250
        }
      });
      this.map.addOverlay(this.pointOverlay);
    },
    /**
     * @description 移除台风路径
     * @param {String} code 台风编码，code为空时清除全部台风
     */
    removeTyphoonRoute(code) {
      if (code) {
        const layer = this.typhoonsMap.get(code + 'layer');
        const interval = this.typhoonsMap.get(code + 'interval');
        this.map.removeLayer(layer);
        clearInterval(interval);
        const nameOverLay = this.typhoonsMap.get(code + 'name');
        this.map.removeOverlay(nameOverLay);
        //清除台风点选中图标
        let picOverlay = this.map.getOverlayById('overlayWind');
        if (picOverlay) {
          this.map.removeOverlay(picOverlay);
        }
      } else {
        for (let item in this.typhoonsMap.values_) {
          if (item.indexOf('layer') != -1) {
            this.map.removeLayer(this.typhoonsMap.values_[item]);
          } else if (item.indexOf('name') != -1) {
            const nameOverLay = this.typhoonsMap.get(item);
            this.map.removeOverlay(nameOverLay);
          } else if (item.indexOf('interval') != -1) {
            clearInterval(item);
          }
        }
        //清除台风点选中图标
        let picOverlay = this.map.getOverlayById('overlayWind');
        if (picOverlay) {
          this.map.removeOverlay(picOverlay);
        }
      }
    },
    // ------------------------------------- ↑台风相关↑ -------------------------------------

    // ------------------------------------- ↓风险隐患↓ -------------------------------------
    /**
     * @description 获取风险隐患列表数据
     */
    getRiskHiddenDangerList() {
      getHiddenDangerList().then((res) => {
        if (res.code === 0) {
          const data = res.data || {};
          this.transfiniteReservoirList = data.resList || [];
          this.seawallRiskList = data.htList || [];
          // this.seawallRiskList.push({
          //   name: '江心屿景区防洪堤塘',
          //   seawallCode: '332ac745-1c7b-11ea-8760-6c92bf66b51e',
          //   latitude: '28.031788',
          //   longitude: '120.646276'
          // });
          this.overalarmRiverNetworkList = data.riveList || [];
          this.overalarmWeirGateList = data.wasList || [];
          if (this.isTransfiniteReservoirDropPoint) this.transfiniteReservoirMapDropPoint();
          if (this.isSeawallRiskDropPoint) this.seawallRiskMapDropPoint();
          if (this.isOveralarmRiverNetworkDropPoint) this.overalarmRiverNetworkMapDropPoint();
          if (this.isOveralarmWeirGateDropPoint) this.overalarmWeirGateMapDropPoint();
        }
      });
    },
    /**
     * @description 海塘风险列表点击事件
     */
    seawallRiskListCurrentChange(row) {
      if (row) {
        this.showSeawallRiskProjectDetail = true;
        this.selectedRiskName = row.name || '--';
        this.selectedRiskCode = row.seawallCode;
        this.map.getView().setCenter([row.longitude, row.latitude]);
        this.map.getView().setZoom(15);
        this.companies = [];
        this.checkedCompanies = [];
        socialResourcesCountByType(row.seawallCode).then((res) => {
          if (res.code === 0) {
            this.companies = res.data.filter((e) => e.name !== '全部') || [];
            this.checkedCompanies = (res.data || [])
              .filter((e) => e.name !== '全部' && e.num && e.num > 0)
              .map((e) => e.name);
            this.handleCheckedCompaniesChange(this.checkedCompanies);
          }
        });
      }
    },
    /**
     * @description 获取海塘风险工程详情列表数据
     */
    getSeawallRiskListDetailData() {
      this.companiesListLoading = true;
      if (this.checkedCompanies.length === 0) {
        this.altitudeHighestSpot = '--';
        this.altitudeMinimumSpot = '--';
        this.companiesList = [];
        this.seawallRiskInvolvePlaceDropPoint();
        this.companiesListLoading = false;
      } else {
        socialResourcesList(this.checkedCompanies.join(), this.selectedRiskCode, this.companiesKeyword).then((res) => {
          if (res.code === 0) {
            const data = res.data || {};
            this.altitudeHighestSpot = data.highestPoint || '--';
            this.altitudeMinimumSpot = data.lowestPoint || '--';
            this.companiesList = data.list || [];
            this.seawallRiskInvolvePlaceDropPoint();
          }
          this.companiesListLoading = false;
        });
      }
    },
    /**
     * @description 超限水库地图落点
     */
    transfiniteReservoirMapDropPoint() {
      this.isTransfiniteReservoirDropPoint = true;
      this.removeLayer('transfiniteReservoirPointsLayers');
      this.drawPoint(
        this.transfiniteReservoirList.map((e) => {
          e.styleObj = {
            src: require('@/assets/new_images/map-point-reservoir-warn.png'),
            srcPoly: require('@/assets/new_images/map-point-reservoir-warn-poly.png')
          };
          return e;
        }),
        'transfiniteReservoirPointsLayers'
      );
    },
    /**
     * @description 移除超限水库地图落点
     */
    transfiniteReservoirMapRemovePoint() {
      this.isTransfiniteReservoirDropPoint = false;
      this.removeLayer('transfiniteReservoirPointsLayers');
    },
    /**
     * @description 海塘风险地图落点
     */
    seawallRiskMapDropPoint() {
      this.isSeawallRiskDropPoint = true;
      this.removeLayer('seawallRiskPointsLayers');
      this.drawPoint(
        this.seawallRiskList.map((e) => {
          e.styleObj = {
            src: require('@/assets/new_images/map-point-seawall-warn.png'),
            srcPoly: require('@/assets/new_images/map-point-seawall-warn-poly.png')
          };
          return e;
        }),
        'seawallRiskPointsLayers'
      );
    },
    /**
     * @description 移除海塘风险地图落点
     */
    seawallRiskMapRemovePoint() {
      this.isSeawallRiskDropPoint = false;
      this.removeLayer('seawallRiskPointsLayers');
    },
    /**
     * @description 超警河网地图落点
     */
    overalarmRiverNetworkMapDropPoint() {
      this.isOveralarmRiverNetworkDropPoint = true;
      this.removeLayer('overalarmRiverNetworkPointsLayers');
      this.drawPoint(
        this.overalarmRiverNetworkList.map((e) => {
          e.styleObj = {
            src: require('@/assets/new_images/map-point-river-warn.png'),
            srcPoly: require('@/assets/new_images/map-point-river-warn-poly.png')
          };
          return e;
        }),
        'overalarmRiverNetworkPointsLayers'
      );
    },
    /**
     * @description 移除超警河网地图落点
     */
    overalarmRiverNetworkMapRemovePoint() {
      this.isOveralarmRiverNetworkDropPoint = false;
      this.removeLayer('overalarmRiverNetworkPointsLayers');
    },
    /**
     * @description 超警堰闸地图落点
     */
    overalarmWeirGateMapDropPoint() {
      this.isOveralarmWeirGateDropPoint = true;
      this.removeLayer('overalarmWeirGatePointsLayers');
      this.drawPoint(
        this.overalarmWeirGateList.map((e) => {
          e.styleObj = {
            src: require('@/assets/new_images/map-point-weirgate-warn.png'),
            srcPoly: require('@/assets/new_images/map-point-weirgate-warn-poly.png')
          };
          return e;
        }),
        'overalarmWeirGatePointsLayers'
      );
    },
    /**
     * @description 移除超警堰闸地图落点
     */
    overalarmWeirGateMapRemovePoint() {
      this.isOveralarmWeirGateDropPoint = false;
      this.removeLayer('overalarmWeirGatePointsLayers');
    },
    /**
     * @description 关闭风险隐患右侧部分
     */
    closeSeawallRiskProjectDeatilContanier() {
      this.showSeawallRiskProjectDetail = false;
      this.$refs.selectedRiskTable.setCurrentRow();
      this.seawallRiskInvolvePlaceRemovePoint();
    },
    /**
     * @descritpion 风险隐患右侧部分的条件重置
     */
    selectedRiskConditionReset() {
      this.checkedCompanies = this.companies.filter((e) => e.num && e.num > 0).map((e) => e.name);
      this.companiesKeyword = '';
      this.getSeawallRiskListDetailData();
    },
    /**
     * @description 风险隐患右侧部分-全选框状态的改变
     */
    handleCheckAllChange(val) {
      this.checkedCompanies = val ? this.companies.map((e) => e.name) : [];
      this.isIndeterminate = false;
      this.getSeawallRiskListDetailData();
    },
    /**
     * @description 风险隐患右侧部分-多选框状态的改变
     */
    handleCheckedCompaniesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.companies.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.companies.length;
      this.getSeawallRiskListDetailData();
    },
    /**
     * @description 海塘风险影响涉及的地方落点
     */
    seawallRiskInvolvePlaceDropPoint() {
      this.removeLayer('seawallRiskAffectsSocialResourcesPointsLayers');
      this.drawPoint(
        this.companiesList.map((e) => {
          if (e.type === '危化企业') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-dangerousChemicalEnterprises.png'),
              srcPoly: require('@/assets/new_images/map-point-dangerousChemicalEnterprises-poly.png')
            };
          } else if (e.type === '学校') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-school.png'),
              srcPoly: require('@/assets/new_images/map-point-school-poly.png')
            };
          } else if (e.type === '医院') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-hospital.png'),
              srcPoly: require('@/assets/new_images/map-point-hospital-poly.png')
            };
          } else if (e.type === '机关单位') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-organ.png'),
              srcPoly: require('@/assets/new_images/map-point-organ-poly.png')
            };
          } else if (e.type === '通讯基站') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-communicationBaseStation.png'),
              srcPoly: require('@/assets/new_images/map-point-communicationBaseStation-poly.png')
            };
          } else if (e.type === '下穿立交') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-undercrossingOverpass.png'),
              srcPoly: require('@/assets/new_images/map-point-undercrossingOverpass-poly.png')
            };
          } else if (e.type === '燃气站') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-gasStation.png'),
              srcPoly: require('@/assets/new_images/map-point-gasStation-poly.png')
            };
          } else if (e.type === '自然村') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-village.png'),
              srcPoly: require('@/assets/new_images/map-point-village-poly.png')
            };
          } else if (e.type === '水厂') {
            e.styleObj = {
              src: require('@/assets/new_images/map-point-waterworks.png'),
              srcPoly: require('@/assets/new_images/map-point-waterworks-poly.png')
            };
          }
          return e;
        }),
        'seawallRiskAffectsSocialResourcesPointsLayers'
      );
    },
    /**
     * @description 移除海塘风险影响涉及的地方落点
     */
    seawallRiskInvolvePlaceRemovePoint() {
      this.map.getView().setZoom(12);
      this.map.getView().setCenter([120.54, 28.08]);
      this.removeLayer('seawallRiskAffectsSocialResourcesPointsLayers');
    },
    // ------------------------------------- ↑风险隐患↑ -------------------------------------

    // ------------------------------------- ↓山洪灾害↓ -------------------------------------
    /**
     * @description 获取山洪灾害落点的数据信息
     */
    getMountainTorrentDisasterData() {
      disasterPreventionApi.listPrevObj().then((res) => {
        if (res.code === 0) {
          const data = res.data || [];
          this.removeLayer('mountainTorrentDisasterPointsLayers');
          this.drawPoint(
            data.map((e) => {
              if (e.recType === '一般村落') {
                e.styleObj = {
                  src: require('@/assets/new_images/map-point-house-warn.png')
                };
              } else {
                e.styleObj = {
                  src: require('@/assets/new_images/map-point-house-danger.png')
                };
              }
              return e;
            }),
            'mountainTorrentDisasterPointsLayers'
          );
        }
      });
    }
    // ------------------------------------- ↑山洪灾害↑ -------------------------------------
  }
};
</script>
<style lang='scss' scoped>
.a_map_of_luncheng {
  overflow: hidden;
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url('~@/assets/new_images/a_map_bgi.png');
  background-size: 100% 100%;
  .map_container {
    position: absolute;
    top: 3px;
    right: 3px;
    bottom: 3px;
    left: 3px;
    &.cursorImg {
      cursor: url('~@/assets/images/end-point.png'), pointer !important;
    }
    &.analysisImg {
      cursor: url('~@/assets/images/analysis.png'), pointer !important;
    }
    &.rainfallImg {
      cursor: url('~@/assets/images/rainfall.png'), pointer !important;
    }
  }
  .popup_map_hover {
    position: relative;
    background-image: linear-gradient(180deg, #0094c3 2%, #103567);
    border: 2px solid #00c1ff;
    border-radius: 3px;
    box-shadow: inset 0 4rem 30rem 0 rgb(35 81 239 / 25%);
    padding: 16px;
    opacity: 0.9;
    > span {
      height: 25px;
      line-height: 25px;
    }
    .label {
      color: #fff;
      font-size: 16px;
    }
    .value {
      color: #1cfff5;
      font-size: 18px;
      margin-left: 8px;
    }
    ::before,
    ::after {
      content: '';
      position: absolute;
      display: block;
      box-sizing: content-box;
      width: 0px;
      height: 0px;
      bottom: 0;
      left: 50%;
      transform: translateX(-50%) translateY(100%);
    }
    ::before {
      border: 7px solid transparent;
      border-top: 7px solid rgba(16, 53, 103, 0.9);
      border-bottom: 0;
      z-index: 6;
    }
    ::after {
      border: 10px solid transparent;
      border-top: 10px solid #00c1ff;
      border-bottom: 0;
    }
  }
  .radarsat_satellite {
    position: absolute;
    top: 3px;
    right: 3px;
    bottom: 3px;
    left: 3px;
    padding-top: 106px;
  }
  .tabbar_contanier {
    display: flex;
    position: absolute;
    top: 3px;
    left: 3px;
    right: 3px;
    height: 106px;
    background-color: rgba(2, 22, 44, 0.8);
    box-shadow: 0 4px 30px 0 rgba(35, 81, 239, 0.25) inset;
    border-bottom: 2px solid #244778;
    .left_tabbar {
      display: flex;
      align-items: center;
      flex: 1;
      padding-left: 32px;
      img {
        width: 72px;
        height: 72px;
        cursor: pointer;
        &:not(:nth-child(1), :nth-child(2)) {
          margin-left: 24px;
        }
      }
    }
    .right_tabbar {
      position: relative;
      display: flex;
      align-items: center;
      border-left: 1px solid rgba(0, 186, 255, 0.39);
      padding: 0 32px;
      img {
        width: 72px;
        height: 72px;
        cursor: pointer;
        & + img {
          margin-left: 24px;
        }
      }
      .response_level_select_panel {
        display: flex;
        flex-direction: column;
        justify-content: space-evenly;
        align-items: center;
        position: absolute;
        top: 100%;
        width: 72px;
        height: 142px;
        background-color: #0f1b25;
        p {
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #ffffff;
          cursor: pointer;
        }
        .selected {
          color: #00baff;
        }
      }
    }
  }
  .left_contanier {
    position: absolute;
    box-sizing: border-box;
    width: 400px;
    padding: 16px;
    left: 0;
    top: 109px;
    bottom: 10px;
    background-image: url('~@/assets/new_images/a_map_left_box_bgi.png');
    background-size: 100% 100%;
    transition: 0.5s linear;
    z-index: 3;
    &.hidden {
      transform: translateX(-400px);
      .display_btn {
        background-image: url('~@/assets/new_images/a_map_left_box_display_icon.png');
        transform: translateX(3px);
      }
    }
    .content {
      overflow: auto;
      height: calc(100% - 30px - 17px);
      margin-top: 17px;
      i {
        font-size: 12px;
        font-weight: bold;
        cursor: pointer;
      }
      ::v-deep .el-table {
        .el-table__header-wrapper {
          .el-table__header {
            thead {
              background-image: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
              tr {
                th {
                  font-size: 16px;
                  font-weight: 400;
                  color: #00baff;
                  text-align: left;
                  background-color: transparent;
                  padding: 8.5px 0;
                }
              }
            }
          }
        }
        .el-table__body-wrapper {
          tbody {
            tr {
              &.current-row {
                td {
                  border-top: 1px solid #00baff;
                  border-bottom: 1px solid #00baff;
                  &:first-child {
                    border-left: 1px solid #00baff;
                  }
                  &:last-child {
                    border-right: 1px solid #00baff;
                  }
                }
              }
              &:nth-child(even) {
                background-image: linear-gradient(90deg, rgba(255, 255, 255, 0.2) 0%, rgba(249, 249, 249, 0) 100%);
              }
              td {
                font-size: 16px;
                font-weight: 500;
                color: #ffffff;
                text-align: left;
                background-color: transparent;
                padding: 8.5px 0;
              }
            }
          }
        }
      }
      .rain_button_box,
      .water_button_box,
      .typhoon_button_box {
        display: flex;
        justify-content: space-between;
        .condition_switch {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 105px;
          height: 32px;
          box-sizing: border-box;
          padding: 0 12px;
          box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
          border-radius: 2px;
          border: 1px solid #155bd4;
          cursor: pointer;
          span {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
          }
          i {
            font-size: 12px;
            color: #ffffff;
          }
        }
        .condition_reset {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 68px;
          height: 32px;
          box-sizing: border-box;
          padding: 0 9px 0 6px;
          box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
          border-radius: 2px;
          border: 1px solid #155bd4;
          cursor: pointer;
          span {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #1492ff;
          }
          i {
            font-size: 16px;
            color: #1492ff;
          }
        }
      }
      .rain_condition_box,
      .water_condition_box,
      .typhoon_condition_box {
        margin-top: 11px;
        .condition_item {
          display: flex;
          .label {
            flex-shrink: 0;
            width: 76px;
            height: 20px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
            line-height: 20px;
          }
          .condition {
            > span {
              margin-right: 14px;
              height: 20px;
              font-size: 14px;
              font-family: PingFangSC-Regular, PingFang SC;
              font-weight: 400;
              color: #ffffff;
              line-height: 20px;
              &.has_left_margin {
                margin-left: 14px;
              }
              &.two_written {
                margin-right: 0;
                display: inline-block;
                transform: translateX(-5px);
              }
            }
            ::v-deep > .el-date-editor {
              width: auto;
              margin-bottom: 16px;
              .el-input__inner {
                width: 264px;
                height: 32px;
                box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
                border-radius: 2px;
                border: 1px solid #155bd4;
                text-align: left;
              }
              .el-input__icon {
                line-height: 32px;
              }
            }
            ::v-deep > .el-radio-group {
              display: flex;
              flex-wrap: wrap;
              align-content: space-between;
              height: 56px;
              margin-bottom: 16px;
              .el-radio {
                width: 33.3%;
                height: 20px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #ffffff;
                line-height: 20px;
                margin: 0;
                &.is-checked {
                  .el-radio__label {
                    color: #1492ff;
                  }
                }
                .el-radio__input {
                  .el-radio__inner {
                    border-color: #ffffff;
                    background-color: transparent;
                  }
                  &.is-checked {
                    .el-radio__inner {
                      border-color: #1492ff;
                    }
                    .el-radio__inner::after {
                      width: 8px;
                      height: 8px;
                      background-color: #1492ff;
                    }
                  }
                }
              }
            }
            ::v-deep > .el-select {
              margin-bottom: 16px;
              .el-input {
                .el-input__inner {
                  width: 292px;
                  height: 32px;
                  box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
                  border-radius: 2px;
                  border: 1px solid #155bd4;
                  text-align: left;
                }
                .el-input__icon {
                  line-height: 32px;
                }
              }
            }
            ::v-deep > .el-checkbox {
              margin-bottom: 16px;
              .el-checkbox__input {
                .el-checkbox__inner {
                  width: 16px;
                  height: 16px;
                  border-radius: 2px;
                  border: 1px solid #858d96;
                  background-color: transparent;
                  &::after {
                    border-color: #1492ff;
                    left: 5px;
                    top: 2px;
                  }
                }
              }
              .el-checkbox__label {
                height: 20px;
                font-size: 14px;
                font-family: PingFangSC-Regular, PingFang SC;
                font-weight: 400;
                color: #ffffff;
                line-height: 20px;
              }
              &.is-checked {
                .el-checkbox__input.is-checked {
                  .el-checkbox__inner {
                    border: 1px solid #1492ff;
                  }
                }
                .el-checkbox__label {
                  color: #1492ff;
                }
              }
            }
            ::v-deep > .el-input {
              margin-bottom: 16px;
              .el-input__inner {
                height: 32px;
                box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
                border-radius: 2px;
                border: 1px solid #155bd4;
                text-align: left;
              }
            }
            ::v-deep > .el-input-number {
              width: 92px;
              height: 32px;
              box-sizing: border-box;
              border-radius: 2px;
              border: 1px solid #155bd4;
              margin-bottom: 16px;
              .el-input-number__decrease,
              .el-input-number__increase {
                top: 0;
                width: 16px;
                height: 30px;
                border-color: #155bd4;
                background-color: transparent;
                line-height: 34px;
              }
              .el-input-number__decrease {
                left: 0;
              }
              .el-input-number__increase {
                right: 0;
              }
              .el-input {
                height: 30px;
                .el-input__inner {
                  display: block;
                  width: 90px;
                  height: 30px;
                  padding: 0 16px;
                  box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
                  color: #fff;
                  text-align: center;
                }
              }
            }
            ::v-deep > .el-cascader {
              margin-bottom: 16px;
              .el-input {
                .el-input__inner {
                  width: 292px;
                  height: 32px;
                  box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
                  border-radius: 2px;
                  border: 1px solid #155bd4;
                  text-align: left;
                }
              }
            }
          }
        }
      }
      .typhoon_condition_box .condition_item .condition {
        width: 292px;
        ::v-deep .el-radio-group {
          height: auto;
        }
      }
      .split_line {
        width: 368px;
        height: 20px;
        margin-bottom: 10px;
      }
      .rain_tabbar_box,
      .water_tabbar_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        ul.tabbar {
          display: flex;
          li {
            height: 32px;
            padding: 0 11px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
            line-height: 30px;
            text-align: center;
            border-left: 1px solid #858d96;
            border-top: 1px solid #858d96;
            border-bottom: 1px solid #858d96;
            cursor: pointer;
            &:first-child {
              border-top-left-radius: 2px;
              border-bottom-left-radius: 2px;
            }
            &:last-child {
              border-top-right-radius: 2px;
              border-bottom-right-radius: 2px;
              border-right: 1px solid #858d96;
              &.active {
                border-right-color: #1492ff;
              }
            }
            &.active {
              color: #1492ff;
              box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2), inset 0px 0px 7px 3px rgba(0, 186, 255, 0.44);
              border-left-color: #155bd4;
              border-top-color: #155bd4;
              border-bottom-color: #155bd4;
              & + li {
                border-left-color: #155bd4;
              }
            }
          }
        }
        ::v-deep .el-input {
          width: auto;
          .el-input__inner {
            width: 189px;
            height: 32px;
            box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
            border-radius: 2px;
            border: 1px solid #155bd4;
            text-align: left;
          }
        }
        .menu_icon {
          width: 16px;
          height: 14px;
          cursor: pointer;
        }
      }
      .water_type_select,
      .typhoon_type_select {
        display: flex;
        justify-content: space-between;
        margin-bottom: 10px;
        li {
          width: 78px;
          height: 35px;
          font-size: 16px;
          font-family: PingFangSC-Medium, PingFang SC;
          font-weight: 500;
          color: #ffffff;
          text-align: center;
          line-height: 33px;
          background-image: url('~@/assets/new_images/a_map_water_tabbar_bgi.png');
          background-repeat: no-repeat;
          cursor: pointer;
          &.active {
            color: #23e3fb;
            background-image: url('~@/assets/new_images/a_map_water_tabbar_bgi_hl.png');
          }
        }
      }
      .typhoon_type_select {
        justify-content: flex-start;
        li + li {
          margin-left: 15px;
        }
      }
      .typhoon_tabbar {
        display: flex;
        margin-bottom: 10px;
        li {
          box-sizing: border-box;
          width: 50%;
          height: 32px;
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #ffffff;
          line-height: 30px;
          text-align: center;
          border-left: 1px solid #858d96;
          border-top: 1px solid #858d96;
          border-bottom: 1px solid #858d96;
          cursor: pointer;
          &:first-child {
            border-top-left-radius: 2px;
            border-bottom-left-radius: 2px;
          }
          &:last-child {
            border-top-right-radius: 2px;
            border-bottom-right-radius: 2px;
            border-right: 1px solid #858d96;
            &.active {
              border-right-color: #1492ff;
            }
          }
          &.active {
            color: #1492ff;
            box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2), inset 0px 0px 7px 3px rgba(0, 186, 255, 0.44);
            border-left-color: #155bd4;
            border-top-color: #155bd4;
            border-bottom-color: #155bd4;
            & + li {
              border-left-color: #155bd4;
            }
          }
        }
      }
      .typhoon_analysis_table_box {
        height: calc(100% - 213px);
      }
      .typhoon_table_box,
      .typhoon_analysis_table_box {
        position: relative;
        > i {
          position: absolute;
          top: 13px;
          left: 13px;
          color: #00baff;
          font-size: 16px;
          cursor: pointer;
          font-weight: 400;
        }
      }
    }
    .display_btn {
      position: absolute;
      left: 100%;
      top: 0;
      bottom: 0;
      margin: auto;
      width: 37px;
      height: 69px;
      background-image: url('~@/assets/new_images/a_map_left_box_hidden_icon.png');
      cursor: pointer;
      transition: 0.5s linear;
    }
    .drop_icon {
      width: 16px;
      height: 18px;
      cursor: pointer;
      &.on_title {
        position: absolute;
        top: 20px;
        right: 16px;
      }
      &.on_subtitle {
        transform: translate(-13px, 3px);
      }
    }
    .no_typhoon {
      display: flex;
      flex-direction: column;
      justify-content: space-evenly;
      align-items: center;
      position: absolute;
      top: 7px;
      left: 100%;
      transform: translateX(7px);
      width: 100px;
      height: 100px;
      background: linear-gradient(180deg, rgba(0, 79, 104, 0.1) 0%, rgba(25, 60, 197, 0) 100%);
      box-shadow: inset 0px 4px 30px 0px rgba(35, 81, 239, 0.25);
      border-radius: 2px;
      border: 1px solid #00c1ff;
      cursor: pointer;
      img {
        width: 56px;
        height: 56px;
      }
      span {
        font-size: 14px;
        font-family: PingFangSC-Regular, PingFang SC;
        font-weight: 400;
        color: #00c1ff;
      }
    }
  }
  .right_contanier {
    position: absolute;
    box-sizing: border-box;
    width: 400px;
    padding: 16px;
    top: 109px;
    right: 0;
    bottom: 10px;
    background-image: url('~@/assets/new_images/a_map_left_box_bgi.png');
    background-size: 100% 100%;
    transition: 0.5s linear;
    z-index: 3;
    &.hidden {
      transform: translateX(400px);
      .display_btn {
        background-image: url('~@/assets/new_images/a_map_left_box_display_icon.png');
        transform: translateX(-3px) rotateZ(180deg);
      }
    }
    .content {
      overflow: auto;
      height: calc(100% - 30px - 17px);
      margin-top: 17px;
      .selected_risk_button_box {
        display: flex;
        justify-content: space-between;
        .condition_switch {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 105px;
          height: 32px;
          box-sizing: border-box;
          padding: 0 12px;
          box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
          border-radius: 2px;
          border: 1px solid #155bd4;
          cursor: pointer;
          span {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
          }
          i {
            font-size: 12px;
            color: #ffffff;
          }
        }
        .condition_reset {
          display: flex;
          justify-content: space-between;
          align-items: center;
          width: 68px;
          height: 32px;
          box-sizing: border-box;
          padding: 0 9px 0 6px;
          box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
          border-radius: 2px;
          border: 1px solid #155bd4;
          cursor: pointer;
          span {
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #1492ff;
          }
          i {
            font-size: 16px;
            color: #1492ff;
          }
        }
      }
      .selected_risk_condition_box {
        margin-top: 11px;
        ::v-deep .el-checkbox {
          .el-checkbox__input {
            .el-checkbox__inner {
              width: 16px;
              height: 16px;
              border-radius: 2px;
              border: 1px solid #858d96;
              background-color: transparent;
              &::after {
                border-color: #1492ff;
                left: 5px;
                top: 2px;
              }
            }
            &.is-indeterminate {
              .el-checkbox__inner {
                border: 1px solid #1492ff;
              }
              .el-checkbox__inner::before {
                background-color: #1492ff;
              }
            }
          }
          .el-checkbox__label {
            height: 20px;
            font-size: 14px;
            font-family: PingFangSC-Regular, PingFang SC;
            font-weight: 400;
            color: #ffffff;
            line-height: 20px;
          }
          &.is-checked {
            .el-checkbox__input.is-checked {
              .el-checkbox__inner {
                border: 1px solid #1492ff;
              }
            }
            .el-checkbox__label {
              color: #1492ff;
            }
          }
          + div {
            margin: 0 !important;
          }
        }
        ::v-deep .el-checkbox-group {
          display: flex;
          flex-wrap: wrap;
          margin-bottom: 16px;
          .el-checkbox {
            margin: 0;
            width: 33.3%;
          }
        }
        .altitude_extremum_box {
          display: flex;
          justify-content: space-between;
          margin-bottom: 16px;
          > div {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 175px;
            height: 40px;
            box-sizing: border-box;
            padding: 0 8px;
            > span {
              display: inline-block;
              font-size: 14px;
              font-family: PingFangSC-Semibold, PingFang SC;
              font-weight: 600;
              color: #ffffff;
            }
          }
          .highest_spot {
            background-image: url('~@/assets/new_images/altitude_highest_spot_bgi.png');
            > span:nth-child(2) {
              font-size: 16px;
              font-weight: 500;
              color: #00baff;
            }
          }
          .minimum_spot {
            background-image: url('~@/assets/new_images/altitude_minimun_spot_bgi.png');
            > span:nth-child(2) {
              font-size: 16px;
              font-weight: 500;
              color: #ff0000;
            }
          }
        }
      }
      .split_line {
        width: 368px;
        height: 20px;
        margin-bottom: 10px;
      }
      ::v-deep > .el-input {
        margin-bottom: 16px;
        .el-input__inner {
          height: 32px;
          box-shadow: 0px 0px 4px 0px rgba(10, 42, 97, 0.2);
          border-radius: 2px;
          border: 1px solid #155bd4;
          text-align: left;
        }
      }
      ::v-deep .el-table {
        .el-table__header-wrapper {
          .el-table__header {
            thead {
              background-image: linear-gradient(90deg, rgba(44, 158, 191, 0.3) 0%, rgba(41, 147, 180, 0.1) 100%);
              tr {
                th {
                  font-size: 16px;
                  font-weight: 400;
                  color: #00baff;
                  text-align: left;
                  background-color: transparent;
                  padding: 8.5px 0;
                }
              }
            }
          }
        }
        .el-table__body-wrapper {
          tbody {
            tr {
              &:nth-child(even) {
                background-image: linear-gradient(90deg, rgba(255, 255, 255, 0.2) 0%, rgba(249, 249, 249, 0) 100%);
              }
              td {
                font-size: 16px;
                font-weight: 500;
                color: #ffffff;
                text-align: left;
                background-color: transparent;
                padding: 8.5px 0;
              }
            }
          }
        }
      }
    }
    .display_btn {
      position: absolute;
      top: 0;
      right: 100%;
      bottom: 0;
      margin: auto;
      width: 37px;
      height: 69px;
      background-image: url('~@/assets/new_images/a_map_left_box_hidden_icon.png');
      cursor: pointer;
      transition: 0.5s linear;
      transform: rotateZ(180deg);
    }
    .close_btn {
      position: absolute;
      top: 21px;
      right: 16px;
      cursor: pointer;
      i {
        color: #fff;
        font-size: 16px;
      }
    }
  }
  .central_role {
    position: absolute;
    left: 0;
    right: 0;
    top: 120px;
    margin: auto;
    box-sizing: border-box;
    padding: 0 40px;
    width: 793px;
    height: 152px;
    border: 1px solid #91c8fa;
    background-color: rgba(6, 32, 61, 0.8);
    &::before,
    &::after {
      content: '';
      position: absolute;
      top: 0;
      width: 6px;
      height: 100%;
      background-color: #c7e8fa;
      box-shadow: 0px 0px 10px 2px #c7e8fa;
    }
    &::before {
      left: 0;
    }
    &::after {
      right: 0;
    }
    i {
      position: absolute;
      width: 10px;
      height: 4px;
      background: #c7e8fa;
      &:nth-child(1) {
        left: 15px;
        top: 12px;
      }
      &:nth-child(2) {
        top: 12px;
        right: 15px;
      }
      &:nth-child(3) {
        right: 15px;
        bottom: 12px;
      }
      &:nth-child(4) {
        bottom: 12px;
        left: 15px;
      }
    }
    .title {
      display: flex;
      justify-content: center;
      align-items: center;
      box-sizing: border-box;
      height: 56px;
      border-bottom: 2px solid rgba(255, 255, 255, 0.4);
      img {
        width: 21px;
        height: 40px;
      }
      span {
        font-family: ziticqtezhanti;
        color: #34d5ff;
        font-size: 32px;
        background: linear-gradient(90deg, #67fff8, #66fff8 18%, #00a1f8);
        background-clip: text;
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }
    .content {
      height: calc(100% - 56px);
      .typhoon_data {
        display: flex;
        justify-content: space-evenly;
        align-items: center;
        width: 100%;
        height: 100%;
        p {
          font-size: 22px;
          color: #fff;
          .distance {
            font-size: 33px;
            color: #ff551f;
          }
          .number {
            font-size: 33px;
            color: #eec80b;
          }
        }
      }
      .water_data {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 100%;
        height: 75%;
        .water_data_item {
          p.label {
            height: 32px;
            font-size: 16px;
            font-family: PingFangSC-Semibold, PingFang SC;
            font-weight: 600;
            color: #ffffff;
            line-height: 32px;
          }
          p.data {
            height: 20px;
            font-size: 20px;
            font-family: PingFangSC-Semibold, PingFang SC;
            font-weight: 600;
            color: #ffffff;
            line-height: 20px;
            > span {
              height: 32px;
              font-size: 32px;
              font-family: PingFangSC-Semibold, PingFang SC;
              font-weight: 600;
              color: #fcee27;
              line-height: 32px;
              vertical-align: sub;
            }
          }
        }
      }
    }
    .hidden_box {
      position: absolute;
      top: 20px;
      right: 40px;
      display: flex;
      align-items: center;
      cursor: pointer;
      > span {
        color: rgba(255, 255, 255, 0.8);
        margin-left: 5px;
      }
    }
  }
  .map_legend {
    position: absolute;
    right: 20px;
    bottom: 18px;
    background: linear-gradient(180deg, rgba(6, 29, 54, 0.7) 0%, #051832 100%);
    box-shadow: inset 0px 4px 30px 0px rgba(35, 81, 239, 0.25);
    border: 1px solid #00c1ff;
    z-index: 3;
    &.left {
      left: 20px;
      right: auto;
    }
    > i {
      position: absolute;
      width: 18px;
      height: 16px;
      border-image: linear-gradient(180deg, rgba(53, 185, 227, 1), rgba(96, 138, 255, 1)) 3 3;
      &:nth-child(1) {
        left: -3px;
        top: -3px;
        border-left: 3px solid;
        border-top: 3px solid;
      }
      &:nth-child(2) {
        top: -3px;
        right: -3px;
        border-top: 3px solid;
        border-right: 3px solid;
      }
      &:nth-child(3) {
        right: -3px;
        bottom: -3px;
        border-right: 3px solid;
        border-bottom: 3px solid;
      }
      &:nth-child(4) {
        bottom: -3px;
        left: -3px;
        border-bottom: 3px solid;
        border-left: 3px solid;
      }
    }
    .title {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 15px;
      height: 40px;
      background: linear-gradient(270deg, rgba(12, 61, 54, 0.02) 0%, rgba(7, 157, 254, 0.4) 100%);
      font-size: 16px;
      font-family: PingFangSC-Semibold, PingFang SC;
      font-weight: 600;
      color: #ffffff;
      line-height: 40px;
      cursor: pointer;
      .legend_arrow {
        width: 13px;
        height: 9px;
        background-image: url('~@/assets/new_images/projectScheduling/legend-arrow.png');
        background-size: 100% 100%;
        background-repeat: no-repeat;
        transform: rotate(180deg);
        &.rotate_arrow {
          transform: rotate(0);
        }
      }
    }
    .content {
      overflow: hidden;
      box-sizing: border-box;
      width: 280px;
      padding: 7px 21px 10px 15px;
      &.hidden {
        height: 0;
        padding: 0 21px 0 15px;
      }
      .subtitle {
        height: 24px;
        font-size: 14px;
        font-family: PingFangSC-Medium, PingFang SC;
        font-weight: 500;
        color: #00baff;
        line-height: 24px;
        margin-bottom: 3px;
        cursor: pointer;
      }
      .legend_box {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        .legend_item {
          display: flex;
          align-items: center;
          height: 20px;
          font-size: 14px;
          font-family: PingFangSC-Regular, PingFang SC;
          font-weight: 400;
          color: #ffffff;
          line-height: 20px;
          margin-bottom: 5px;
          > i {
            display: inline-block;
            width: 12px;
            height: 5px;
            margin-right: 6px;
            &.circle {
              width: 16px;
              height: 16px;
              border-radius: 50%;
            }
          }
        }
      }
    }
  }
  .levitation_button {
    position: absolute;
    top: 131px;
    right: 28px;
    .btn_item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 128px;
      height: 43px;
      background: linear-gradient(234deg, rgba(7, 165, 255, 0) 0%, rgba(30, 192, 255, 0.7) 100%);
      border-radius: 22px;
      cursor: pointer;
      .text {
        font-size: 16px;
        font-family: PingFangSC-Semibold, PingFang SC;
        font-weight: 600;
        color: #ffffff;
        padding-left: 15px;
      }
      img {
        width: 44px;
        height: 46px;
      }
    }
  }
  .ban_selected_all {
    ::v-deep .el-table__header-wrapper .el-table-column--selection {
      // border: 1px solid red;
      text-indent: -999999px;
    }
  }
  .ol-popup {
    position: absolute;
    bottom: 15px;
    left: -50%;
    transform: translateX(-50%);
    border-radius: 3px;
    min-width: 150px;
    .ol-popup__wrapper {
      background-image: linear-gradient(180deg, #0094c3 2%, #103567 100%);
      border: 2px solid #00c1ff;
      box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
      border-radius: 3px;
      height: 100%;
      width: 100%;
      padding: 16px;
      position: relative;
      opacity: 0.9;
      &:before {
        content: '';
        position: absolute;
        display: block;
        box-sizing: content-box;
        width: 0px;
        height: 0px;
        border: 7px solid transparent;
        border-top: 7px solid #103567;
        border-bottom: 0;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%) translateY(100%);
        z-index: 100;
      }
      &:after {
        content: '';
        position: absolute;
        display: block;
        box-sizing: content-box;
        width: 0px;
        height: 0px;
        border: 10px solid transparent;
        border-top: 10px solid #00c1ff;
        border-bottom: 0;
        bottom: 0;
        left: 50%;
        transform: translateX(-50%) translateY(100%);
      }
      .ol-popup__title {
        font-size: 18px;
        color: #fff;
      }
      .ol-popup__content {
        color: #fff;
        .ol-popup__content--props {
          display: flex;
          flex-direction: column;
          .props-item {
            // padding: 0 12px;
            position: relative;
            display: flex;
            align-items: center;
            min-width: 150px;
            width: max-content;
            /* &:not(:only-child) {
              flex-direction: column;
              min-width: 100px;
              &:after {
                content: "";
                position: absolute;
                height: 80%;
                right: 0;
                top: 10%;
                border-right: 1px solid #00c1ff4d;
              }
              &:first-child {
                padding-left: 0;
              }
              &:last-child {
                padding-right: 0;
                &:after {
                  border-right: 0;
                }
              }
              .item-label {
                font-size: 16px;
              }
              .item-data {
                font-size: 18px;
                color: #33ffff;
                line-height: 25px;
              }
            } */
            .item-label {
              font-size: 16px;
            }
            .item-data {
              margin-left: 8px;
              font-size: 18px;
              color: #33ffff;
              line-height: 25px;
            }
            &:first-child {
              &:not(:only-child) {
                margin-top: 8px;
              }
            }
          }
          .props-label {
            line-height: 20px;
            width: 350px;
          }
        }
      }
    }
    &.typhoon-popup {
      width: 254px;
      color: #fff;
      .typhoon-point_title {
        text-align: center;
        font-size: 18px;
        color: #ffffff;
        letter-spacing: 0;
        line-height: 24px;
        font-weight: 600;
        padding-bottom: 10px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.4);
      }
      .typhoon-point_content {
        p {
          padding: 8px 0;
          > span {
            font-size: 18px;
            color: #33ffff;
            font-weight: 600;
            padding-right: 8px;
            &:first-child {
              color: #fff;
            }
          }
        }
      }
    }
    &.scheme-popup {
      // min-width: 236px;
      font-size: 16px;
      color: #ffffff;
      line-height: 26px;
      .ol-popup__wrapper {
        padding: 8px 16px;
      }
      .scheme-point_sub_title {
        font-size: 14px;
        color: #1ef8f2;
      }
      .scheme-point_sub_box {
        display: flex;
        flex-direction: column;
        height: 32px;
      }
      p {
        span {
          display: inline-block;
        }
        span:first-child {
          width: 90px;
          text-align: left;
        }
        span:last-child {
          font-size: 18px;
          color: #33ffff;
        }
      }
    }
  }
}
</style>
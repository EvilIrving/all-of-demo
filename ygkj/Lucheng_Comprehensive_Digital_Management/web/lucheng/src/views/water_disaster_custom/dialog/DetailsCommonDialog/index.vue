<template>
  <div class="detail-table">
    <DataDetailDialog :visible.sync="visible" :loading="exportLoading" :fullscreen="fullscreen" :before-close="handleClose" :title="asyncTitle" @changeFullScreen="changeFullscreen" class="detail-table">
      <!-- 切页 -->
      <template slot="stat-tabs">
        <StatisticsTabs v-if="code == '美丽河湖'" :active.sync="activeTabBeauty" :tabs="['美丽河湖', '中小河流', '水美乡镇', '农村池塘']" @change="changeActiveTabBeauty" />

        <StatisticsTabs v-if="code == '水资源承载能力'" :active.sync="activeTabCarry" :tabs="['水量评价', '水质评价', '经济评价', '人口评价']" @change="changeActiveTabCarry" />
      </template>
      <template slot="stat">
        <!-- 水库分析  -->
        <template v-if="mediumSized == '防御复盘' && code == '水库分析'">
          <StatisticsBox title="累计拦截蓄水量" icon="icon-lan-xushui-liang">
            <span>{{ reservoirAnalysisData.totalStorage }} </span> 万m³
          </StatisticsBox>
          <StatisticsBox title="累计泄洪量" icon="icon-shuizha-paihong">
            <span>{{ reservoirAnalysisData.totalDrainage }} </span> 万m³
          </StatisticsBox>
        </template>
        <!-- 水情 -->
        <template v-if="code == '水情'">
          <StatisticsBox title="水库水位超警" icon="reservoir-icon">
            <span style="color: #fcee27; font-size: 20px"> {{ reservoirStatistical_a.value }} </span>
            <span style="color: #fff; font-size: 16px">/ {{ reservoirStatistical_a.total }} </span>
          </StatisticsBox>
          <StatisticsBox title="河道水位超警" :boxClass="count % 2 ? 'gates-open-sty' : ''" icon="riverway-icon">
            <span style="color: #fcee27; font-size: 20px"> {{ reservoirStatistical_b.value }} </span>
            <span style="color: #fff; font-size: 16px">/ {{ reservoirStatistical_b.total }} </span>
          </StatisticsBox>
          <StatisticsBox title="堰闸水位超警" :boxClass="count % 2 ? 'gates-open-sty' : ''" icon="sluice-icon">
            <span style="color: #fcee27; font-size: 20px"> {{ reservoirStatistical_c.value }} </span>
            <span style="color: #fff; font-size: 16px">/ {{ reservoirStatistical_c.total }} </span>
          </StatisticsBox>
          <StatisticsBox title="潮位超警" :boxClass="count % 2 ? 'gates-open-sty' : ''" icon="sea-level-icon">
            <span style="color: #fcee27; font-size: 20px"> {{ reservoirStatistical_d.value }} </span>
            <span style="color: #fff; font-size: 16px">/ {{ reservoirStatistical_d.total }} </span>
          </StatisticsBox>
        </template>
        <!-- 工情 -->
        <template v-if="code == '工情'">
          <StatisticsBox title="水库开启数量" icon="icon-shuizha-shuliang-tongji">
            <span style="color: #47f5a7; font-size: 20px; cursor: pointer" @click="changeActiveTabWorks('水库')">
              {{ workStatisticalData.rsvrOpenNum }}
            </span>
            <span style="color: #fff; font-size: 16px">/ {{ workStatisticalData.rsvrNum }}</span>
          </StatisticsBox>
          <StatisticsBox title="泵站开启数量" :boxClass="count % 2 ? 'gates-open-sty' : ''" icon="icon-shuizha-zhamen-kaiqi-shuliang">
            <span style="color: #47f5a7; font-size: 20px; cursor: pointer" @click="changeActiveTabWorks('泵站')">
              {{ workStatisticalData.pustOpenNum }}
            </span>
            <span style="color: #fff; font-size: 16px">/ {{ workStatisticalData.pustNum }}</span>
          </StatisticsBox>
          <StatisticsBox title="水闸开启数量" :boxClass="count % 2 ? 'gates-open-sty' : ''" icon="icon-shuizha-zhamen-kaiqi-shuliang">
            <span style="color: #47f5a7; font-size: 20px; cursor: pointer" @click="changeActiveTabWorks('水闸')">
              {{ workStatisticalData.wagaOpenNum }}
            </span>
            <span style="color: #fff; font-size: 16px">/ {{ workStatisticalData.wagaNum }}</span>
          </StatisticsBox>
        </template>
        <!-- 河网 -->
        <template v-if="code == '河网'">
          <StatisticsBox title="数量统计" icon="icon-hewang-shuliang-tongji">
            <span> {{ riverwayList.total }} </span> 个
          </StatisticsBox>
          <StatisticsBox title="超警戒统计" icon="icon-chao-xunxian">
            <span>{{ riverwayList.warn }} </span> 个
          </StatisticsBox>
        </template>
        <!-- 潮位 -->
        <template v-if="code == '潮位'">
          <StatisticsBox title="数量统计" icon="icon-chaowei-tongji">
            <span>{{ seaLevelList.totalNum == null ? '-' : seaLevelList.totalNum }}</span>个
          </StatisticsBox>
          <StatisticsBox title="超警戒统计" icon="icon-chaowei-tongji">
            <span>{{ seaLevelList.overWarnNum == null ? '-' : seaLevelList.overWarnNum }} </span>个
          </StatisticsBox>
        </template>
        <!-- 救援仓库 -->
        <template v-if="code == '救援仓库'">
          <StatisticsBox style="margin-right: 20px" title="救援仓库总数" icon="icon-warehouse">
            <span>{{ waterLoggingData.list.length }}</span>个
          </StatisticsBox>
          <StatisticsBox style="margin-right: 20px" title="仓库总面积" icon="icon-warehouse">
            <span style="color: #eec80b">{{ waterLoggingData.hasWater }}</span>㎡
          </StatisticsBox>
          <StatisticsBox title="仓库总价值" icon="icon-warehouse">
            <span style="color: #eec80b">{{ waterLoggingData.hasWater }}</span>㎡
          </StatisticsBox>
        </template>
        <!-- 报警 -->
        <template v-if="code == '报警'">
          <StatisticsBox title="海塘报警数量" icon="icon-alarm-seawall" v-if="mediumSized == 1">
            <span @click="changeActiveTabWorks('实时')">{{ alarmStatisticData.seawallWarning }}</span>个
          </StatisticsBox>
          <StatisticsBox title="海塘预警数量" icon="icon-alarm-seawall" v-if="mediumSized == 1">
            <span @click="changeActiveTabWorks('预报')">{{ alarmStatisticData.seawallGiveAnAlarm }}</span>个
          </StatisticsBox>
          <StatisticsBox title="水位报警数量" icon="icon-alarm-water" v-if="mediumSized == 2">
            <span @click="changeActiveTabWorks('水位')">{{ alarmStatisticData.waterGiveAnAlarm }}</span>个
          </StatisticsBox>
          <StatisticsBox title="降雨报警数量" icon="icon-alarm-rainfall" v-if="mediumSized == 2">
            <span @click="changeActiveTabWorks('降雨')">{{ alarmStatisticData.rainGiveAnAlarm }}</span>个
          </StatisticsBox>
          <StatisticsBox title="台风数量" icon="icon-alarm-taifeng" v-if="mediumSized == 3">
            <span @click="changeActiveTabWorks('台风')">{{ alarmStatisticData.typhoonGiveAnAlarm }}</span>个
          </StatisticsBox>
          <StatisticsBox title="运行数量" icon="icon-alarm-run" v-if="mediumSized == 1">
            <span @click="changeActiveTabWorks('运行')">{{ alarmStatisticData.runGiveAnAlarm }}</span>个
          </StatisticsBox>
        </template>
        <!-- 视频监控 -->
        <template v-if="code == '视频监控'">
          <StatisticsBox title="总数" icon="icon-camera-tongji">
            <span>{{ stats4PageCameraData.total || '-' }}</span>个
          </StatisticsBox>
          <StatisticsBox title="在线" icon="icon-camera-tongji-online">
            <span style="color: #19af47">{{ stats4PageCameraData.online || '-' }}</span>个
          </StatisticsBox>
          <StatisticsBox title="离线" icon="icon-camera-tongji-online">
            <span style="color: #bfbfbf">{{ stats4PageCameraData.offline || '-' }}</span>个
          </StatisticsBox>
        </template>
        <!-- 防汛仓库 -->
        <template v-if="code == '防汛仓库'">
          <StatisticsBox title="数量统计" icon="icon-warehouse-tongji">
            <span>{{ warehouseData.list.length }}</span>个
          </StatisticsBox>
          <StatisticsBox v-for="(item, index) in warehouseData.stats" :key="index" :icon="buildingTypeIconMap[item.key] ? buildingTypeIconMap[item.key] : 'icon-warehouse-tongji'" :title="item.key">
            <span>{{ item.value }}</span>个
          </StatisticsBox>
        </template>
        <!-- 防汛队伍 -->
        <template v-if="code == '防汛队伍'">
          <StatisticsBox title="数量统计" icon="icon-team-tongji">
            <span>{{ ranksData.list.length }}</span>个
          </StatisticsBox>
          <StatisticsBox v-for="(item, index) in ranksData.stats" :key="index" :icon="buildingTypeIconMap[item.key] ? buildingTypeIconMap[item.key] : 'icon-team-tongji'" :title="item.key">
            <span>{{ item.value }}</span>个
          </StatisticsBox>
        </template>
        <!--  -->
        <!-- 供水量统计 -->
        <template v-if="code == '供水量'">
          <StatisticsBox icon="icon-Minimum-water" title="供水量最大年份及对应供水量">
            <span>
              {{ WrbWaterSupplyData.max.year || '' }}
              <span style="color: #fff; font-size: 15px">年</span>
              {{ WrbWaterSupplyData.max.totalSupply || '' }}
            </span>
            亿m³
          </StatisticsBox>
          <StatisticsBox icon="icon-Minimum-water" title="供水量最小年份及对应供水量">
            <span>
              {{ WrbWaterSupplyData.min.year }}
              <span style="color: #fff; font-size: 15px">年</span>
              {{ WrbWaterSupplyData.min.totalSupply }}
            </span>
            亿m³
          </StatisticsBox>
        </template>

        <template v-if="code == '用水量'">
          <StatisticsBox icon="icon-Minimum-water" title="用水总量"> <span> 1000 </span>m³ </StatisticsBox>
          <StatisticsBox icon="icon-Minimum-water" title="人均综合用水量"> <span> 200 </span>m³ </StatisticsBox>
        </template>
        <!-- 降水量统计 -->
        <template v-if="code == '降水量'">
          <StatisticsBox icon="icon-precipitation" title="最大降雨年份和区域">
            <span v-if="precipitationVal.max != {}">
              {{ precipitationVal.max.adnm }}
              <span style="color: #fff">|</span> {{ precipitationVal.max.year }}
            </span>
          </StatisticsBox>
          <StatisticsBox icon="icon-precipitation" title="最小降雨年份和区域">
            <span v-if="precipitationVal.min != {}">
              {{ precipitationVal.min.adnm }}
              <span style="color: #fff">|</span> {{ precipitationVal.min.year }}
            </span>
          </StatisticsBox>
        </template>
        <!-- 水厂 -->
        <template v-if="code == '水厂'">
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-12" title="水厂总数">
            <span> {{ waterPointData.totalNum || '-' }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-13" title="城市水厂">
            <span> {{ waterPointData['城市水厂'] || '-' }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-22" title="千吨万人">
            <span style="color: #008fe8"> {{ waterPointData['千吨万人'] || '-' }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-32" title="200吨~1000吨">
            <span style="color: #6267ff"> {{ waterPointData['两百吨千人以上'] || '-' }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-422" title="200吨以下">
            <span style="color: #a000c8"> {{ waterPointData['两百吨千人以下'] || '-' }} </span>个
          </StatisticsBox>
        </template>
        <!-- 水源地 -->
        <template v-if="code == '水源地'">
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-1" title="水源地总数">
            <span> {{ wasuObjTypeData[4].num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-1" title="县级以上">
            <span> {{ wasuObjTypeData[0].num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-2" title="千吨万人">
            <span style="color: #008fe8"> {{ wasuObjTypeData[1].num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-3" title="200吨~1000吨">
            <span style="color: #6267ff"> {{ wasuObjTypeData[2].num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-4" title="200吨以下">
            <span style="color: #a000c8"> {{ wasuObjTypeData[3].num }} </span>个
          </StatisticsBox>
        </template>
        <!-- 水量预警 -->
        <template v-if="code == '水量预警'">
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-1" title="水源地总数">
            <span> {{ wasuObjTypeData[4].num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-5" title="暂无风险">
            <span style="color: #19af47"> {{ wasuObjTypeData[0].num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-6" title="黄色预警">
            <span style="color: #e89100"> {{ wasuObjTypeData[1].num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-6" title="橙色预警">
            <span style="color: #ff551f"> {{ wasuObjTypeData[2].num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi icon-shuiyuandi-8" title="橙色预警">
            <span style="color: #e80000"> {{ wasuObjTypeData[3].num }} </span>个
          </StatisticsBox>
        </template>
        <!-- 珊溪水质监测 -->
        <template v-if="code == '珊溪水质监测'">
          <StatisticsBox icon="icon-shuiyuandi sx-dialog-count-1" title="站点总数">
            <span> 246 </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sx-dialog-count-2" title="人工监测">
            <span> 123 </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sx-dialog-count-3" title="自动监测">
            <span> 123 </span>个
          </StatisticsBox>
        </template>
        <!-- 珊溪巡查统计 -->
        <template v-if="code == '巡查'">
          <StatisticsBox icon="icon-shuiyuandi sx-dialog-count-4" title="乱点总数">
            <span> {{ countList.turbTotal || '-' }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sx-dialog-count-5" title="已完成">
            <span style="color: #19af47">{{ countList.completeNum || '-' }}</span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sx-dialog-count-6" title="整改中">
            <span style="color: #e89100">{{ countList.changeNum || '-' }}</span>个
          </StatisticsBox>
        </template>
        <!-- 取水户 -->
        <template v-if="code == '取水户'">
          <StatisticsBox icon="icon-shuiyuandi qushuihu-dialog-count-1" title="取水户总数">
            <span>{{ wtaerIntakeCount.intakeSum && wtaerIntakeCount.intakeSum.toFixed(0) }}</span>户
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi qushuihu-dialog-count-2" title="正常">
            <span style="color: #19af47"> {{ wtaerIntakeCount.intakeNorm }} </span>户
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi qushuihu-dialog-count-3" title="接近许可">
            <span style="color: #008fe8">
              {{ wtaerIntakeCount.intakeNearPermit }}
            </span>
            户
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi qushuihu-dialog-count-4" title="超许可">
            <span style="color: #e89100">
              {{ wtaerIntakeCount.intakeOverPermit }}
            </span>
            户
          </StatisticsBox>
        </template>
        <!-- 批后监管 -->
        <template v-if="code == '批后监管'">
          <StatisticsBox icon="icon-shuiyuandi phjg-dialog-count-1" title="项目总数">
            <span> {{ approSupeCount.projectNum }} </span>
            个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi phjg-dialog-count-2" title="监管次数">
            <span style="color: #19af47">
              {{ approSupeCount.inspectionNum }}
            </span>
            次
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi phjg-dialog-count-3" title="发现问题数">
            <span style="color: #eec80b">
              {{ approSupeCount.findProblem }}
            </span>
            个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi phjg-dialog-count-4" title="移交执法大队数">
            <span style="color: #ef1212">
              {{ approSupeCount.hiddenDangerNum }}
            </span>
            个
          </StatisticsBox>
        </template>
        <!-- 水域变化 -->
        <template v-if="code == '水域变化'">
          <StatisticsBox icon="icon-shuiyuandi sybh-dialog-count-1" title="水域面积">
            <span>
              {{ Number(waterChangeCount.waterArea).toFixed(2) || '-' }}
            </span>
            k㎡
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sybh-dialog-count-2" title="水面率">
            <span> {{ waterChangeCount.waterRate }} </span>
            %
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sybh-dialog-count-3" title="占用面积">
            <span> {{ waterChangeCount.occupiedArea }} </span>
            k㎡
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sybh-dialog-count-4" title="补偿面积">
            <span>
              {{ Number(waterChangeCount.compensationArea).toFixed(2) || '-' }}
            </span>
            k㎡
          </StatisticsBox>
        </template>
        <!-- 清四乱 -->
        <template v-if="code == '清四乱'">
          <StatisticsBox icon="icon-shuiyuandi qsl-dialog-count-1" title="问题总数">
            <span> {{ fourChaosCount.total || '0' }} </span>
            个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi qsl-dialog-count-1" title="最多问题地区">
            <span> {{ fourChaosCount.maxRate || '-' }} </span>
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi qsl-dialog-count-1" title="最少问题地区">
            <span> {{ fourChaosCount.minRate || '-' }} </span>
          </StatisticsBox>
        </template>
        <!-- 美丽河湖 -->
        <template v-if="code == '美丽河湖'">
          <StatisticsBox :icon="['icon-shuiyuandi', getIcon]" title="项目总数">
            <span> {{ beautyCount.projectTotalNum || '0' }} </span>
            个
          </StatisticsBox>
          <StatisticsBox :icon="['icon-shuiyuandi', getIcon]" title="项目在建">
            <span> {{ beautyCount.underConstructionNum || '0' }} </span>
            个
          </StatisticsBox>
          <StatisticsBox :icon="['icon-shuiyuandi', getIcon]" title="已完工">
            <span> {{ beautyCount.completeNum || '0' }} </span>
            个
          </StatisticsBox>
        </template>
        <!-- 水电站 -->
        <template v-if="code == '水电站'">
          <StatisticsBox icon="icon-shuiyuandi sdz-dialog-count-1" title="数量统计">
            <span> {{ sdzCount.totalHyst || '-' }} </span>座
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sdz-dialog-count-1" title="最多数量">
            <span>
              {{ sdzCount.maxAreaName || '-' }} | {{ sdzCount.maxNum }}座 ({{
                sdzCount.totalHyst ? ((sdzCount.maxNum / sdzCount.totalHyst) * 100).toFixed(2) : '-'
              }}%)</span>
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sdz-dialog-count-1" title="本月生态流量达标率">
            <span>
              {{ sdzCount.standardArg ? (sdzCount.standardArg * 100).toFixed(2) : '-' }}
            </span>
            %
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi sdz-dialog-count-1" title="本月在线率">
            <span>
              {{ sdzCount.onlineArg ? (sdzCount.onlineArg * 100).toFixed(2) : '-' }}
            </span>
            %
          </StatisticsBox>
        </template>
        <!-- 海塘 -->
        <template v-if="code == '海塘'">
          <StatisticsBox icon="icon-extent-icon" title="总条数">
            <span> {{ seawallData.total || '-' }} </span> 条
          </StatisticsBox>
          <StatisticsBox icon="icon-extent-icon" title="100年以上">
            <span> {{ seawallData.gt100Year || '-' }} </span> 条
          </StatisticsBox>
          <StatisticsBox icon="icon-extent-icon" title="100年一遇">
            <span> {{ seawallData.eq100Year || '-' }} </span> 条
          </StatisticsBox>
          <StatisticsBox icon="icon-extent-icon" title="50年一遇">
            <span> {{ seawallData.eq50Year || '-' }} </span> 条
          </StatisticsBox>
          <StatisticsBox icon="icon-extent-icon" title="20年一遇">
            <span> {{ seawallData.eq20Year || '-' }} </span> 条
          </StatisticsBox>
          <StatisticsBox icon="icon-extent-icon" title="10年一遇">
            <span> {{ seawallData.eq10Year || '-' }} </span> 条
          </StatisticsBox>
          <StatisticsBox icon="icon-extent-icon" title="10年以下">
            <span> {{ seawallData.lt10Year || '-' }} </span> 条
          </StatisticsBox>
        </template>
        <!-- 河长巡查 -->
        <template v-if="code == '河长巡查'">
          <StatisticsBox icon="icon-shuiyuandi hzxc-dialog-count-1" title="问题总数">
            <span> {{ patrolCount.totalNum }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi hzxc-dialog-count-2" title="轻微问题">
            <span> {{ patrolCount.level0Num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi hzxc-dialog-count-3" title="一般问题">
            <span> {{ patrolCount.level1Num }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-shuiyuandi hzxc-dialog-count-4" title="重大问题">
            <span> {{ patrolCount.level2Num }} </span>个
          </StatisticsBox>
        </template>
        <!-- 风险隐患 -->
        <template v-if="code == '风险隐患'">
          <StatisticsBox icon="icon-risk-total" title="总数">
            <span> {{ riskCount.totalNum }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-risk-num" title="隐患点">
            <span> {{ riskCount.riskNum }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-highRisk-num" title="高风险点">
            <span> {{ riskCount.highRiskNum }} </span>个
          </StatisticsBox>
          <StatisticsBox icon="icon-weak-num" title="薄弱点">
            <span> {{ riskCount.highRiskNum }} </span>个
          </StatisticsBox>
        </template>
        <!-- 水资源量 -->
        <template v-if="code == '水资源量'">
          <StatisticsBox icon="icon-detail-stat-water-res" title="水资源总量">
            <span> {{ waterResourceStat.totalWaterResource }} </span>亿(m³)
          </StatisticsBox>
          <StatisticsBox icon="icon-detail-stat-water-res" title="水资源利用率">
            <span> {{ waterResourceStat.avgWrUseRate }} </span>%
          </StatisticsBox>
          <StatisticsBox icon="icon-detail-stat-water-res" title="人均水资源量">
            <span> {{ waterResourceStat.watResPerCapita }} </span>亿(m³)
          </StatisticsBox>
        </template>
        <!-- 救援队伍 -->
        <template v-if="code == '救援队伍'">
          <StatisticsBox icon="icon-detail-stat-water-res" style="margin-right: 20px" title="救援队伍总数">
            <!-- <span> {{ reservoirStorageStat.maxStoreIncre.year }}</span
            >个 -->
            <span>123</span>个
          </StatisticsBox>
          <StatisticsBox style="margin-right: 20px" icon="icon-detail-stat-water-res" title="XXXXXXXX队(最多)">
            <!-- <span>{{ reservoirStorageStat.maxStoreDecre.storageRate }}</span
            >人 -->
            <span>123</span>人
          </StatisticsBox>
          <StatisticsBox icon="icon-detail-stat-water-res" title="XXXXXXXX队(最少)">
            <!-- <span>{{ reservoirStorageStat.shanxiMaxStoreIncre.storageRate }}</span
            >人 -->
            <span>123</span>人
          </StatisticsBox>
        </template>
      </template>
      <!-- 条件查询区域 -->
      <template slot="query">
        <!-- 通用行政区划 -->
        <template v-if="!hideAdcdFilterCodes.includes(code)">
          <LabelFormItem label="行政区划:">
            <el-select v-model="administrationValue" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
            </el-select>
          </LabelFormItem>
        </template>
        <!-- 通用流域 -->
        <template v-if="!hideBasinFilterCodes.includes(code)">
          <LabelFormItem label="流域:">
            <el-select v-model="administrativeValue" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in administrative" :key="item.name" :label="item.name" :value="item.name" />
            </el-select>
          </LabelFormItem>
        </template>
        <template v-if="code == '风险隐患'">
          <LabelFormItem label="工程类型:">
            <el-cascader v-model="projectTypeVal" size="small" :options="projectTypeList" clearable @change="selectVal" />
          </LabelFormItem>
          <LabelFormItem label="风险类型:">
            <el-select v-model="riskTypeVal" size="small" clearable @change="selectVal" placeholder="全部">
              <el-option v-for="item in riskTypeList" :key="item.name" :label="item.name" :value="item.value" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="发现渠道:">
            <el-select v-model="discoveryChannel" size="small" clearable @change="selectVal" placeholder="全部">
              <el-option v-for="item in discoveryChannelList" :key="item.name" :label="item.name" :value="item.value" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="整改情况:">
            <el-select v-model="reorganizeSituation" size="small" clearable @change="selectVal" placeholder="全部">
              <el-option v-for="item in reorganizeSituationList" :key="item.name" :label="item.name" :value="item.name" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="发现时间:">
            <el-date-picker v-model="reportTime" format="yyyy-MM-dd" value-format="yyyy-MM-dd" type="date" placeholder="发现时间" clearable @change="selectVal" />
          </LabelFormItem>
          <LabelFormItem label="工程名称:">
            <el-input placeholder="请输入工程名称" v-model="projectNameVal" style="width: 180px" clearable @input="selectVal" />
          </LabelFormItem>
        </template>

        <template v-if="mediumSized == '防御复盘' && code == '水利设施保险'">
          <LabelFormItem label="提防名称:">
            <el-input placeholder="请输入" v-model="bewareName" clearable @input="queryVal" />
          </LabelFormItem>
          <LabelFormItem label="行政区划:">
            <el-select v-model="claimSchedule" size="small" clearable @input="selectVal" placeholder="全部">
              <el-option v-for="item in claimScheduleList" :key="item.name" :label="item.name" :value="item.name" />
            </el-select>
          </LabelFormItem>
        </template>

        <template v-if="mediumSized == '防御复盘' && code == '潮位分析'">
          <LabelFormItem label="潮位名称:">
            <el-input placeholder="请输入" v-model="seaLevelName" clearable @input="queryVal" />
          </LabelFormItem>
        </template>

        <template v-if="mediumSized == '防御复盘' && code == '河网分析'">
          <LabelFormItem label="河网名称:">
            <el-input placeholder="请输入" v-model="riverName" clearable @input="queryVal" />
          </LabelFormItem>
        </template>

        <template v-if="mediumSized == '防御复盘' && code == '水闸分析'">
          <LabelFormItem label="水闸名称:">
            <el-input placeholder="请输入" v-model="sluiceName" clearable @input="queryVal" />
          </LabelFormItem>
        </template>

        <template v-if="mediumSized == '防御复盘' && code == '水库分析'">
          <LabelFormItem label="水库名称:">
            <el-input placeholder="请输入" v-model="reservoirName" clearable @input="queryVal" />
          </LabelFormItem>
        </template>
        <!-- 设计防潮标准年限 -->
        <template v-if="code == '海塘'">
          <LabelFormItem label="设计防潮标准年限:">
            <el-select v-model="designTideStandard" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in designTideStandardData" :key="item.name" :label="item.name" :value="item.name" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="工程等别:">
            <el-select v-model="engineeringValue" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in EngineeringData" :key="item.grade" :label="item.grade" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>
        <!-- 工程规模 -->
        <template v-if="code == '水情'">
          <LabelFormItem label="站点类型:">
            <el-select v-model="siteTypeValue" size="small" placeholder="水库" clearable @change="selectVal">
              <el-option v-for="item in siteTypesData" :key="item.name" :label="item.name" :value="item.code" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="报警状态:">
            <el-select v-model="callThePoliceValue" size="small" clearable placeholder="全部" @change="selectVal">
              <el-option v-for="item in callThePoliceData" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>
        <template v-if="code == '工情'">
          <LabelFormItem label="工程规模:">
            <el-select v-model="worksProScaleValue" size="small" clearable placeholder="全部" @change="selectVal">
              <el-option v-for="item in worksProScaleData" :key="item.label" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>
        <template v-if="code == '报警'">
          <LabelFormItem v-if="this.callPoliceValue == '水位'" label="站点类型:">
            <el-select v-model="AlarmWaterLevelValue" size="small" clearable placeholder="全部" @change="selectVal">
              <el-option v-for="item in AlarmWaterLevelOptions" :key="item.label" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem v-if="this.callPoliceValue == '降雨'" label="降雨报警类型:">
            <el-select v-model="AlarmRainfallValue" size="small" clearable placeholder="全部" @change="selectVal">
              <el-option v-for="item in AlarmRainfallOption" :key="item.label" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>
        <!-- 工程类型 -->
        <template v-if="code == '病险工程' || code == '视频监控'">
          <LabelFormItem label="工程类型:">
            <el-select v-model="projType" size="small" clearable placeholder="全部" @change="selectVal">
              <el-option v-for="item in projTypeData" :key="item.name" :label="item.name" :value="item.name" />
            </el-select>
          </LabelFormItem>
        </template>
        <!-- 视频状态 -->
        <template v-if="code == '视频监控'">
          <LabelFormItem label="视频状态:">
            <el-select v-model="statusType" size="small" clearable placeholder="全部" @change="selectVal">
              <el-option label="在线" :value="1" />
              <el-option label="离线" :value="0" />
            </el-select>
          </LabelFormItem>
        </template>
        <template v-if="code == '救援仓库'">
          <LabelFormItem label="仓库类型:">
            <el-select v-model="warehouseType" size="small" clearable @input="selectVal" placeholder="全部">
              <el-option v-for="item in warehouseOptions" :key="item.id" :label="item.dictName" :value="item.id" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="名称:">
            <el-input v-model="searchVal" placeholder="请输入" @input="selectVal" />
          </LabelFormItem>
        </template>
        <!-- 仓库类型 -->
        <template v-if="code == '防汛仓库'">
          <LabelFormItem label="仓库类型:">
            <el-select v-model="warehouseType" size="small" clearable @input="selectVal" placeholder="全部">
              <el-option v-for="item in warehouseOptions" :key="item.id" :label="item.dictName" :value="item.id" />
            </el-select>
          </LabelFormItem>
        </template>

        <!-- 队伍类型 -->
        <template v-if="code == '防汛队伍'">
          <LabelFormItem label="队伍类型:">
            <el-select v-model="ranksType" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in ranksOptions" :key="item.id" :label="item.dictName" :value="item.id" />
            </el-select>
          </LabelFormItem>
        </template>

        <!-- 洪水风险图 -->
        <template v-if="code == '洪水风险图'">
          <LabelFormItem label="设计洪水标准:">
            <el-select v-model="floodLevel" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in floodLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>

        <template v-if="code == '视频监控'">
          <LabelFormItem label="名称:">
            <el-input v-model="NameVal" placeholder="请输入摄像头名称" clearable @blur="queryVal" />
          </LabelFormItem>
        </template>
        <template v-if="code == '降水量' || code == '救援队伍'">
          <template v-if="code == '降水量'">
            <LabelFormItem label="起止年份:">
              <el-date-picker v-model="startYear" format="yyyy" value-format="yyyy" type="year" placeholder="开始" clearable :picker-options="{
                  disabledDate: (time) => (endYear ? time.getFullYear() > endYear : null)
                }" @change="selectVal" />
              至
              <el-date-picker v-model="endYear" format="yyyy" value-format="yyyy" type="year" placeholder="结束" clearable :picker-options="{
                  disabledDate: (time) => (startYear ? time.getFullYear() < startYear : null)
                }" @change="selectVal" />
            </LabelFormItem>
          </template>
          <template v-if="code == '救援队伍'">
            <LabelFormItem label="抢险专业:" style="margin-right: 20px">
              <el-select v-model="earlyType" size="small" clearable @change="getEarlyDetail">
                <el-option v-for="item in earlyTypeOption" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </LabelFormItem>
            <LabelFormItem label="单位性质:" style="margin-right: 20px">
              <el-select v-model="earlyType" size="small" clearable @change="getEarlyDetail">
                <el-option v-for="item in earlyTypeOption" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </LabelFormItem>
            <LabelFormItem label="名称:">
              <el-input v-model="earlySearchVal" placeholder="请输入内容" @input="getEarlyDetail" style="width: 180px" />
            </LabelFormItem>
          </template>
        </template>
        <template v-if="['水资源量', '用水量'].includes(code) || (code == '水资源承载能力' && activeTabCarry != '用水评价')">
          <LabelFormItem label="年份:">
            <el-select v-model="checkYear" size="small" @change="selectVal" :clearable="false">
              <el-option v-for="item in yearOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>
        <!-- 预警信息 筛选项 -->
        <template v-if="code == '预警信息'">
          <LabelFormItem label="预警类型:">
            <el-select v-model="earlyType" size="small" clearable @change="getEarlyDetail">
              <el-option v-for="item in earlyTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="关键字:">
            <el-input v-model="earlySearchVal" placeholder="请输入内容" @input="getEarlyDetail" style="width: 180px" />
          </LabelFormItem>
        </template>
        <!-- 水源地 筛选项 -->
        <template v-if="code == '水源地'">
          <LabelFormItem label="行政区划:">
            <el-select v-model="administrationValue" size="small" clearable placeholder="全部" @change="getWaterSourList">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="类型:">
            <el-select v-model="waterSourType" size="small" clearable @change="getWaterSourList">
              <el-option v-for="item in waterSourTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="规模:">
            <el-select v-model="waterSourScale" size="small" clearable @change="getWaterSourList">
              <el-option v-for="item in waterSourScaleOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="关键字:">
            <el-input v-model="waterSourSearchVal" placeholder="请输入内容" @input="getWaterSourList" />
          </LabelFormItem>
        </template>
        <!-- 水厂 水厂运行状态 筛选项 -->
        <template v-if="code == '水厂' || code == '水厂运行状态'">
          <LabelFormItem label="行政区划:">
            <el-select v-model="adcdData" size="small" clearable placeholder="全部" @change="AttSwhsBaseLoadPage">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="类型:">
            <el-select v-model="engType" size="small" clearable @change="AttSwhsBaseLoadPage">
              <el-option v-for="item in waterWorksList" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="规模:">
            <el-select v-model="wasuRang" size="small" clearable @change="AttSwhsBaseLoadPage">
              <el-option v-for="item in waterWorksScaleList" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="运行状态:">
            <el-select v-model="runtimeState" size="small" clearable @change="AttSwhsBaseLoadPage">
              <el-option v-for="item in waterWorksStatusList" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="关键字:">
            <el-input v-model="cwsName" placeholder="请输入内容" @input="AttSwhsBaseLoadPage" />
          </LabelFormItem>
        </template>
        <!-- 水量预警 筛选项 -->
        <template v-if="code == '水量预警'">
          <LabelFormItem label="行政区划:">
            <el-select v-model="administrationValue" size="small" clearable placeholder="全部" @change="getWaterWarnList">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="类型:">
            <el-select v-model="waterSourType" size="small" clearable @change="getWaterWarnList">
              <el-option v-for="item in waterSourTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="水量预警等级:">
            <el-select v-model="waterSourLevel" size="small" clearable @change="getWaterSourList">
              <el-option v-for="item in waterSourLevelOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="关键字:">
            <el-input v-model="waterSourSearchVal" placeholder="请输入内容" @input="getWaterWarnList" />
          </LabelFormItem>
        </template>

        <!-- 珊溪水质监测 筛选项 -->
        <template v-if="code == '珊溪水质监测'">
          <LabelFormItem label="站点类型:">
            <el-select v-model="stationType" size="small" clearable placeholder="全部" @change="getSXWaterList">
              <el-option v-for="item in stationTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="关键字:">
            <el-input v-model="shanXiWaterSearchVal" placeholder="请输入内容" @input="getSXWaterList" />
          </LabelFormItem>
        </template>
        <!-- 珊溪巡查 筛选项 -->
        <template v-if="code == '巡查'">
          <LabelFormItem label="违法类型:">
            <el-select v-model="chaosType" size="small" clearable placeholder="全部" @change="happeningListJson">
              <el-option v-for="item in chaosTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="乱点状态:">
            <el-select v-model="chaosStatus" size="small" clearable placeholder="全部" @change="happeningListJson">
              <el-option v-for="item in chaosStatusOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
          <LabelFormItem label="乱点名称:">
            <el-input v-model="chaosName" placeholder="请输入内容" @input="happeningListJson" />
          </LabelFormItem>
        </template>

        <!-- 取水户 筛选项 -->
        <template v-if="code == '取水户'">
          <LabelFormItem label="行政区划:">
            <el-select v-model="administrationValue" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="报警:">
            <el-radio-group v-model="wtaerIntakeWarnType" @change="getWaterIntakeList">
              <el-radio class="el-radio" :label="true">是</el-radio>
              <el-radio class="el-radio" :label="false">否</el-radio>
            </el-radio-group>
          </LabelFormItem>

          <LabelFormItem label="取水类型:">
            <el-select v-model="wtaerIntakeType" size="small" clearable placeholder="全部" @change="getWaterIntakeList">
              <el-option v-for="item in wtaerIntakeTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>

        <!-- 批后监管 筛选项 -->
        <template v-if="code == '批后监管'">
          <LabelFormItem label="行政区划:">
            <el-select v-model="administrationValue" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="河道等级:">
            <el-select v-model="riverType" size="small" clearable placeholder="全部" @change="getApproSuperList">
              <el-option v-for="item in riverTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="监管状态:">
            <el-select v-model="supercisionStatu" size="small" clearable placeholder="全部" @change="getApproSuperList">
              <el-option v-for="item in supercisionStatuOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="项目阶段:">
            <el-select v-model="projStage" size="small" clearable placeholder="全部" @change="getApproSuperList">
              <el-option v-for="item in projStageOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>
        <!-- 水域变化 筛选项 -->
        <template v-if="code == '水域变化'">
          <LabelFormItem label="年份:">
            <el-select v-model="checkYear" size="small" clearable @change="selectVal">
              <el-option v-for="item in yearOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>
        <!-- 清四乱 筛选项 -->
        <template v-if="code == '清四乱'">
          <LabelFormItem label="问题状态:">
            <el-select v-model="rectiRange" size="small" clearable @change="selectVal">
              <el-option v-for="item in rectiRangeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="问题来源:">
            <el-select v-model="reasonFrom" size="small" clearable @change="selectVal">
              <el-option v-for="item in reasonFromOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="四乱类型:">
            <el-select v-model="fourChaosType" size="small" clearable @change="selectVal">
              <el-option v-for="item in fourChaosTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="严重程度:">
            <el-select v-model="severity" size="small" clearable @change="selectVal">
              <el-option v-for="item in severityOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="关键字:">
            <el-input v-model="qslSearchVal" placeholder="请输入关键字" @input="getFourChaosList" style="width: 176px" />
          </LabelFormItem>
        </template>

        <!-- 美丽河湖 筛选项 -->
        <template v-if="code == '美丽河湖'">
          <LabelFormItem label="行政区划:">
            <el-select v-model="administrationValue" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adnm" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="工程状态:">
            <el-select v-model="projStatu" size="small" clearable @change="selectVal">
              <el-option v-for="item in projStatuOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="关键字:">
            <el-input v-model="searchVal" style="width: 180px" placeholder="请输入内容" @input="selectVal" />
          </LabelFormItem>
        </template>
        <!-- 水电站 筛选项 -->
        <template v-if="code == '水电站'">
          <LabelFormItem label="行政区划:">
            <el-select v-model="administrationValue" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="水电站类型:">
            <el-select v-model="sdzType" size="small" clearable @change="selectVal">
              <el-option v-for="item in sdzTypeOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="关键字:">
            <el-input style="width: 180px" v-model="sdzSearchVal" placeholder="请输入内容" @input="getHydropowerStationList" />
          </LabelFormItem>
        </template>
        <!-- 河长巡查 筛选项 -->
        <template v-if="code == '河长巡查'">
          <LabelFormItem label="时间:">
            <el-date-picker v-model="patrolDate" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" @change="selectVal" />
          </LabelFormItem>

          <LabelFormItem label="行政区划:">
            <el-select v-model="administrationValue" size="small" clearable placeholder="全部" @input="selectVal">
              <el-option v-for="item in administration" :key="item.adcd" :label="item.adnm" :value="item.adcd" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="处置状态:">
            <el-select v-model="dealStatu" size="small" clearable @change="selectVal">
              <el-option v-for="item in dealStatuOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>

          <LabelFormItem label="问题筛选:">
            <el-select v-model="question" size="small" clearable @change="selectVal">
              <el-option v-for="item in questionOption" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </LabelFormItem>
        </template>
        <!-- 通用关键字搜索 -->
        <LabelFormItem v-if="showSearchCodes.includes(code)" label="关键字:">
          <el-input v-model="searchVal" :placeholder="calcInputPlaceholder" @input="selectVal" />
        </LabelFormItem>
        <!-- 通用导出按钮组件 -->
        <DataDetailButton v-if="hasCommonExport.includes(code)" @click="exportExcel(code)" style="margin-left: auto">导 出</DataDetailButton>
      </template>
      <div class="tb-top-panel" v-if="code == '预警信息'">
        <i class="icon-liebiao icon-sty laba" />
        <p>
          预警消息总数:<span>{{ totalPage }}</span> 次
        </p>
      </div>

      <el-table v-if="!hideTableCodes.includes(code)" v-bind="commonTableProps" v-loading="loading" :data="dynamicTableData" ref="multipleTable" @sort-change="sortChange" :row-class-name="rowClassName">
        <el-table-column type="index" label="序号" width="55" :index="indexMethod" />
        <el-table-column v-for="column in dynamicColumnSetting" show-overflow-tooltip :formatter="columnFormatter" :key="column.prop" :prop="column.prop" :label="dynamicColumnLabel(column.label)" :width="column.width" :sortable="column.sortable || false">
          <template v-if="column.slot" #default="{ row }">
            <span v-bind="column.slot(row).props">
              {{ column.slot(row).label || column.slot(row) }}
            </span>
          </template>
        </el-table-column>
      </el-table>

      <PrimaryMapTable v-if="code == '水情'" :dataList="waterRegimeData" :columnList="WRdynamicColumnSetting">
      </PrimaryMapTable>
      <PrimaryMapTable v-if="code == '工情'" :dataList="workRegimeData" :columnList="WKdynamicColumnSetting">
      </PrimaryMapTable>
      <PrimaryMapTable v-if="code == '报警'" :dataList="tableDataBJ" :columnList="BJdynamicColumnSetting">
      </PrimaryMapTable>
      <!-- <v-table v-if="code == '水情'" :tableData="waterRegimeData" :columnList="WRdynamicColumnSetting">
      </v-table>
      <v-table v-if="code == '工情'" :dataList="workRegimeData" :columnList="WKdynamicColumnSetting">
      </v-table>
      <v-table v-if="code == '报警'" :dataList="tableDataBJ" :columnList="BJdynamicColumnSetting">
      </v-table> -->
      <template #bottom>
        <div class="bottom__pagination" v-if="hasPagination.includes(code)">
          <el-pagination :current-page.sync="pageNum" :page-sizes="[10, 20, 30, 50]" :page-size="pageSize" layout="total, sizes, prev, pager, next" :total="totalPage" @size-change="onSizeChange" @current-change="onCurrentChange" />
        </div>
      </template>
    </DataDetailDialog>
  </div>
</template>

<script>
import {
  exportPageCamera,
  exportRivernet,
  exportBusRisk,
  exportSeawall,
  exportFloodPoint,
  exportWrpWagaUnitTable,
  exportFloodRiskDisasterData,
  exportWareHouses,
  exportTeams,
  exportWrabWaterSupply,
  exportWrbRainfallList,
  exportWrbWatResList,
  exportWrbStoreRsvrList
} from '@/api/micro-service/project-manage/export';
import {
  getWrbWatResList,
  getStats4WrbWatResList,
  getWrbWatConList,
  getWrbStoreRsvrList,
  getStats4WrbStoreRsvrList
} from '@/api/micro-service/wra';
import { DataDetailDialog, StatisticsBox, StatisticsTabs, LabelFormItem, DataDetailButton } from './components';
import { waterAndRain, WorkingConditionsApi, alarmPeople } from '@/api/bigScreenPart';
import VTable from './components/table/VTable.vue';
import { PrimaryMapTable } from '@/components';
import { disasterPreventionApi } from '@/api/RightSildePanel';
import { waterResourceApi } from '@/api/waterResourcesGuarantee';
import { getDict } from '@/api/system';
import { inforApi } from '@/api/inforApi';
import { tableSettings, columnSettings, commonTableProps } from './tableSettings/';
import { mapGetters } from 'vuex';

// prettier-ignore
const hideAdcdFilterCodes = ['水情', '工情', "报警", '水利设施保险', '水库分析', '水闸分析', '河网分析', '潮位分析',
  '洪水风险图', '降水量', '水资源量', '供水量', '用水量', '救援队伍',
  '水资源承载能力', '预警信息', '水源地', '水量预警', '珊溪水质监测',
  '巡查', '取水户', '批后监管', '水域变化', '清四乱', '美丽河湖', '水电站', '河长巡查', '救援仓库']

// prettier-ignore
const hideBasinFilterCodes = ['水情', '工情', "报警", '山洪', '风险隐患', '海塘', '水利设施保险', '水库分析', '水闸分析', '河网分析',
  '潮位分析', '防汛仓库', '防汛队伍', '视频监控', '救援仓库', '洪水风险图', '降水量', '水资源量', '供水量',
  '用水量', '救援队伍', '水资源承载能力', '预警信息', '水源地', '水量预警', '珊溪水质监测',
  '巡查', '取水户', '批后监管', '水域变化', '清四乱', '美丽河湖', '水电站', '河长巡查']
// prettier-ignore
const hideTableCodes = ['水情', '工情', "报警", "报警"]

// prettier-ignore
const hasPagination = ['风险隐患', '救援仓库', '水库', '海塘', '河长巡查', '水电站', '美丽河湖', '清四乱', '水域变化', '批后监管', '预警信息', '视频监控',]

// prettier-ignore
const showSearchCodes = ['水库', '水情', '工情', '河网', '潮位', '病险工程', '海塘', '防汛仓库', '防汛队伍']
// prettier-ignore
const hasCommonExport = ['水情', '工情', '救援队伍', '水资源量', '降水量', '洪水风险图',
  '视频监控', "海塘", "救援仓库", "水库",
  "防汛仓库", "防汛队伍", "河网", "用水量", "风险隐患"];

export default {
  name: 'DetailsCommonDialog',
  components: {
    DataDetailDialog,
    DataDetailButton,
    StatisticsBox,
    StatisticsTabs,
    LabelFormItem,
    VTable,
    PrimaryMapTable
  },
  props: {
    /** 组件可见性 */
    visible: { type: Boolean, default: false },
    /** 组件类型code  */
    code: { type: String, default: '' },
    /** 设计用于大中型水库判断 `基本视为冗余字段` 可以传入一些外部参数使用 */
    mediumSized: { type: String, default: '' }
  },
  data() {
    this.commonTableProps = commonTableProps;
    /** 不需要公共行政区划筛选的 */
    this.hideAdcdFilterCodes = hideAdcdFilterCodes;
    /** 不需要公共流域筛选的 */
    this.hideBasinFilterCodes = hideBasinFilterCodes;
    /** 不需要table筛选的 */
    this.hideTableCodes = hideTableCodes;
    /** 存在翻页器的*/
    this.hasPagination = hasPagination;
    /** 使用公共搜索的 */
    this.showSearchCodes = showSearchCodes;
    /** 有导出的 */
    this.hasCommonExport = hasCommonExport;
    return {
      // 导出Loading
      exportLoading: false,
      /** 水资源统计 */
      waterResourceStat: { totalWaterResource: 0, avgWrUseRate: 0, watResPerCapita: 0 },
      /** 救援队伍统计 */
      reservoirStorageStat: {},
      // 水厂 水厂运行状态
      waterPointData: {},
      waterWorksStatusList: [
        {
          label: '正常',
          value: '0'
        },
        {
          label: '供水预警',
          value: '1'
        },
        {
          label: '水质预警',
          value: '2'
        },
        {
          label: '水质供水预警',
          value: '3'
        }
      ],
      waterWorksScaleList: [
        {
          label: '全部',
          value: ''
        },
        {
          label: '两百吨千人以下',
          value: '两百吨千人以下'
        },
        {
          label: '两百吨千人以上',
          value: '两百吨千人以上'
        },
        {
          label: '千吨万人',
          value: '千吨万人'
        },
        {
          label: '城市水厂',
          value: '城市水厂'
        }
      ],
      waterWorksList: [
        {
          label: '全部',
          value: ''
        },
        {
          label: '城市',
          value: '城市'
        },
        {
          label: '乡镇',
          value: '乡镇'
        },
        {
          label: '联村',
          value: '联村'
        },
        {
          label: '单村',
          value: '单村'
        }
      ],
      adcdData: '',
      cwsName: '',
      engType: '',
      wasuRang: '',
      runtimeState: '',
      value1: '',
      stiStart: '',
      stiEnd: '',
      pickerOptions: {},
      seawallData: {},
      startYear: '',
      endYear: '',
      // 病险工程
      NameVal: '',
      radio: true,
      durationRainfall: [],
      durationRainfallList: [
        {
          value: 1,
          name: '显示前1小时'
        },
        {
          value: 3,
          name: '显示前3小时'
        },
        {
          value: 6,
          name: '显示前6小时'
        },
        {
          value: 24,
          name: '显示前24小时'
        },
        {
          value: 72,
          name: '显示前72小时'
        }
      ],
      projectNameVal: '', //工程名称
      projectTypeVal: [], //工程类型
      projectTypeList: [
        {
          value: '水利工程',
          label: '水利工程',
          children: [
            {
              value: '水库',
              label: '水库'
            },
            {
              value: '海塘',
              label: '海塘'
            },
            {
              value: '堤防',
              label: '堤防'
            },
            {
              value: '闸站',
              label: '闸站'
            },
            {
              value: '泵站',
              label: '泵站'
            },
            {
              value: '山塘',
              label: '山塘'
            }
          ]
        },
        {
          value: '在建水利工程',
          label: '在建水利工程'
        },
        {
          value: '在建涉水工程',
          label: '在建涉水工程'
        },
        {
          value: '水毁工程',
          label: '水毁工程',
          children: [
            {
              value: '水库',
              label: '水库'
            },
            {
              value: '海塘',
              label: '海塘'
            },
            {
              value: '堤防',
              label: '堤防'
            },
            {
              value: '闸站',
              label: '闸站'
            },
            {
              value: '泵站',
              label: '泵站'
            },
            {
              value: '山塘',
              label: '山塘'
            },
            {
              value: '渠道',
              label: '渠道'
            },
            {
              value: '护岸',
              label: '护岸'
            },
            {
              value: '水文测站',
              label: '水文测站'
            }
          ]
        },
        {
          value: '其他',
          label: '其他'
        }
      ], //工程类型
      riskTypeVal: '', //风险类型
      reportTime: '',
      riskTypeList: [
        {
          name: '隐患点',
          value: '251'
        },
        {
          name: '高风险点',
          value: '252'
        },
        {
          name: '薄弱点',
          value: '253'
        }
      ], //风险类型
      district: [], //区域
      districtList: [
        {
          value: '行政区划',
          label: '行政区划',
          children: [
            {
              value: '温州',
              label: '温州',
              children: [
                {
                  value: '"330302000000"',
                  label: '鹿城区'
                },
                {
                  value: '"330303000000"',
                  label: '龙湾区'
                },
                {
                  value: '"330304000000"',
                  label: '瓯海区'
                },
                {
                  value: '"330322000000"',
                  label: '洞头区'
                },
                {
                  value: '"330324000000"',
                  label: '永嘉县'
                },
                {
                  value: '"330326000000"',
                  label: '平阳县'
                },
                {
                  value: '"330327000000"',
                  label: '苍南县'
                },
                {
                  value: '"330328000000"',
                  label: '文成县'
                },
                {
                  value: '"330329000000"',
                  label: '泰顺县'
                },
                {
                  value: '330381000000',
                  label: '瑞安市'
                },
                {
                  value: '"330382000000"',
                  label: '乐清市'
                },
                {
                  value: '"330383000000"',
                  label: '龙港市'
                }
              ]
            }
          ]
        },
        {
          value: '流域',
          label: '流域',
          children: [
            {
              value: '瓯江',
              label: '瓯江'
            },
            {
              value: '飞云江',
              label: '飞云江'
            },
            {
              value: '鳌江',
              label: '鳌江'
            }
          ]
        }
      ], //区域
      reorganizeSituation: '', //整改情况
      reorganizeSituationList: [
        {
          name: '未整改'
        },
        {
          name: '正在整改'
        },
        {
          name: '已落实措施'
        },
        {
          name: '完成整改'
        }
      ], //整改情况
      discoveryChannel: '', //发现渠道
      discoveryChannelList: [
        {
          name: '县级检查',
          value: 'xjjc'
        },
        {
          name: '工程自查',
          value: 'zczc'
        },
        {
          name: '市级抽查',
          value: 'sjcc'
        },
        {
          name: '省级督查',
          value: 'sjdc'
        }
      ], //发现渠道
      // 第三场景
      bewareName: '', //提防名称
      belongName: '', //所属合同
      affiliatedUnit: '', //所属单位
      seaLevelName: '', //潮位名称
      riverName: '', //河网名称
      sluiceName: '', //水闸名称
      reservoirName: '', //水库名称
      claimSchedule: '', //理赔进度
      reservoirAnalysisData: {}, //水库数据
      lockAnalysisData: [], //水闸数据
      riverNetworkData: [], //河网数据
      waterAnalysisData: [], //潮位数据
      tideStaStart: '', //复盘起始时刻
      tideStaEnd: '', //复盘结束时刻

      claimScheduleList: [
        {
          name: '已申请'
        },
        {
          name: '已理赔'
        },
        {
          name: '主管部门确认'
        }
      ], //理赔进度

      count: 0,
      numTime: '1',
      //表格loading
      loading: false,
      //行政区划
      administration: [],
      engineeringValue: '',
      filterOpenGate: false,
      //水库流域选中值
      administrativeValue: '',
      designTideStandard: '',
      //行政区划选中值
      administrationValue: '',
      designTideStandardData: [
        {
          name: '100年以上'
        },
        {
          name: '100年一遇'
        },
        {
          name: '50年一遇'
        },
        {
          name: '20年一遇'
        },
        {
          name: '10年一遇'
        },
        {
          name: '10年以下'
        }
      ],
      //海塘工程等别
      EngineeringData: [
        {
          grade: '1级',
          value: 1,
        },
        {
          grade: '2级',
          value: 2,
        },
        {
          grade: '3级',
          value: 3,
        },
        {
          grade: '4级',
          value: 4,
        },
        {
          grade: '5级',
          value: 5,
        }
      ],
      //水库流域
      administrative: [
        {
          name: '飞云江'
        },
        {
          name: '瓯江'
        },
        {
          name: '鳌江'
        }
      ],
      //工程规模
      projectScaleData: [
        { label: '全部', value: '' },
        { label: '大中型', value: '大中型' },
        { label: '小（Ⅰ）型', value: '小（Ⅰ）型' },
        { label: '小（Ⅱ）型', value: '小（Ⅱ）型' }
      ],
      surfaceTypeStration: [
        {
          name: '行政区划'
        },
        {
          name: '流域'
        }
      ],
      projTypeData: [
        {
          name: '水库'
        },
        {
          name: '山塘'
        },
        {
          name: '海塘'
        },
        {
          name: '水闸'
        },
        {
          name: '堤防'
        },
        {
          name: '其他'
        }
      ],
      //水情列表数据
      waterRegimeData: [],
      //工情列表数据
      workRegimeData: [],
      callThePoliceValue: '',
      //水情 站点类型
      siteTypeValue: 'RR',
      WRdynamicColumnSetting: [
        {
          prop: '',
          label: '名称'
        },
        {
          prop: '',
          label: '水位'
        },
        {
          prop: '',
          label: '汛限水位'
        }
      ],
      siteTypesData: [
        {
          name: '水库',
          code: 'RR'
        },
        {
          name: '河道',
          code: 'ZZ'
        },
        {
          name: '堰闸',
          code: 'DD'
        },
        {
          name: '潮位',
          code: 'TT'
        }
      ],
      //水情 报警状态
      callThePoliceData: [
        {
          name: '是',
          value: true
        },
        {
          name: '否',
          value: false
        }
      ],
      // 水库水位表（序号、名称、水位、汛限水位）
      reservoirWLM: [
        {
          prop: 'stName',
          label: '名称'
        },
        {
          prop: 'waterLevel',
          label: '水位'
        },
        {
          prop: 'limitLevel',
          label: '汛限水位'
        }
      ],
      // 河道水位表（序号、名称、水位、警戒水位）
      weirGateWLM: [
        {
          prop: 'stName',
          label: '名称'
        },
        {
          prop: 'waterLevel',
          label: '水位'
        },
        {
          prop: 'warningLevel',
          label: '警戒水位'
        }
      ],
      // 堰闸水位表（序号、名称、水位、警戒水位）
      wadiWLM: [
        {
          prop: 'stName',
          label: '名称'
        },
        {
          prop: 'upz',
          label: '水位'
        },
        {
          prop: 'warningLevel',
          label: '警戒水位'
        }
      ],
      // 潮位表（序号、名称、水位、警戒水位）
      tidemarkWLM: [
        {
          prop: 'stName',
          label: '名称'
        },
        {
          prop: 'tdz',
          label: '水位'
        },
        {
          prop: 'warningLevel',
          label: '警戒水位'
        }
      ],
      //水情统计
      reservoirStatistical_a: {}, //水库
      reservoirStatistical_b: {}, //河道
      reservoirStatistical_c: {}, //闸门
      reservoirStatistical_d: {}, //潮位
      //工情统计数据
      workStatisticalData: [],
      workConditionValue: '水库',
      WKdynamicColumnSetting: [],
      //工程规模
      worksProScaleValue: '',
      worksProScaleData: [
        { label: '全部', value: '' },
        { label: '大（1）型', value: '大（Ⅰ）型' },
        { label: '大（2）型', value: '大（Ⅱ）型' },
        { label: '中型', value: '中型' },
        { label: '小（1）型', value: '小（Ⅰ）型' },
        { label: '小（2）型', value: '小（Ⅱ）型' }
      ],
      // 水闸工情表筛选项（工程规模——大（1）型、大（2）型、中型、小（1）型、小（2）型）
      sluiceWorksTable: [
        {
          prop: 'wagaName',
          label: '名称'
        },
        {
          prop: 'engScal',
          label: '工程规模'
        },
        {
          prop: 'upz',
          label: '闸上水位'
        },
        {
          prop: 'dwz',
          label: '闸下水位'
        },
        {
          prop: 'ovs',
          label: '闸门开度'
        },
        {
          prop: 'll',
          label: '过闸流量'
        }
      ],
      // ③水库工情表（序号、名称、工程规模、、、）+筛选项（工程规模——大（1）型、大（2）型、中型、小（1）型、小（2）型）
      reservoirWorksTable: [
        {
          prop: 'resName',
          label: '名称'
        },
        {
          prop: 'engScal',
          label: '工程规模'
        },
        {
          prop: 'waterlevel',
          label: '水库水位'
        },
        {
          prop: 'zmkd',
          label: '闸门开度'
        },
        {
          prop: 'xxll',
          label: '下泄流量'
        }
      ],
      // ②泵站工情表（序号、名称、工程规模、、、、）+筛选项（工程规模——大（1）型、大（2）型、中型、小（1）型、小（2）型）
      pumpStationWorksTable: [
        {
          prop: '',
          label: '名称'
        },
        {
          prop: 'engScal',
          label: '工程规模'
        },
        {
          prop: 'jskZ',
          label: '进水池水位'
        },
        {
          prop: 'cscZ',
          label: '出水池水位'
        },
        {
          prop: 'pustOpened',
          label: '水泵开启'
        },
        {
          prop: '',
          label: '流量'
        }
      ],
      riverwayData: [],
      riverwayList: {},
      seaLevelData: [],
      seaLevelList: {},
      dangerouslyData: [],
      projType: '',
      rainfallType: '行政区划',
      rainfalStration: [
        {
          name: '行政区划'
        },
        {
          name: '流域'
        }
      ],
      realTimeData: [],
      realTimeStration: [
        {
          name: '面雨量'
        },
        {
          name: '点雨量'
        }
      ],
      realTimeType: '点雨量',
      administrativeType: '',
      drainageType: '',
      surfaceType: '',
      inputValue: '', //输入框
      selectValue: '', //下拉值
      potentialRisksData: [],
      seawallProjData: [],
      orderColumn: '',
      ascOrDesc: '',
      options: [
        {
          label: '测试',
          value: '测试'
        }
      ],
      WaterloggingData: [],
      num: 0,
      warehouseData: {},
      ranksData: {},
      warehouseOptions: [],
      ranksOptions: [],
      warehouseType: '',
      ranksType: '',
      videoData: [],
      stats4PageCameraData: {},
      waterLoggingData: {},
      waterLoggingType: null,
      waterLoggingOptions: [
        {
          label: '救援仓库',
          value: 0
        },
        {
          label: '救援仓库',
          value: 1
        }
      ],
      //防汛队伍 仓库统计类型图标map
      buildingTypeIconMap: {
        学校: 'icon-school-tongji',
        成员单位: 'icon-member-tongji',
        工管单位: 'icon-work-tongji',
        村居: 'icon-village-tongji',
        本级: 'icon-samelevel-tongji',
        指挥部: 'icon-command-tongji',
        企业: 'icon-enterprise-tongji',
        社区: 'icon-society-tongji'
      },
      statusType: '',
      floodLevel: '',
      floodLevelOptions: [
        {
          label: '5年一遇',
          value: 5
        },
        {
          label: '10年一遇',
          value: 10
        },
        {
          label: '20年一遇',
          value: 20
        },
        {
          label: '50年一遇',
          value: 50
        }
      ],
      floodRiskData: [],
      rainSearchList: [],

      // 降水量
      yearOption: [],
      yearDropdownVisible: false,
      checkYear: '',
      precipitationVal: {},
      precipitationData: [], // 降水量
      waterResourceData: [], // 水资源量
      activeTab: '供水总量', // 供水量tab 供水总量/供水工程
      activeTabCarry: '水量评价', // 水资源承载能力tab
      WrbWaterSupplyData: {},
      waterRupplyData: [], // 供水量
      waterUseData: [], // 用水量
      reservoirStorageData: [], // 救援队伍
      waterResCarryData: [], // 水资源承载能力

      // 预警信息
      earlyType: '', // 预警信息 预警类型
      earlySearchVal: '', // 预警信息 关键字
      earlyData: [], // 预警信息
      earlyTypeOption: [], // 预警类型下拉
      pageSize: 10,
      totalPage: 1,
      pageNum: 1,
      // 水厂 水运行状态
      waterPointList: [],
      // 水源地 水量预警
      wasuObjTypeData: [{ num: 0 }, { num: 0 }, { num: 0 }, { num: 0 }, { num: 0 }],
      waterSourType: '',
      waterSourTypeOption: [
        {
          label: '水库',
          value: '1'
        },
        {
          label: '山塘',
          value: '2'
        },
        {
          label: '河流湖泊',
          value: '3'
        },
        {
          label: '溪沟堰坝',
          value: '4'
        },
        {
          label: '蓄水池',
          value: '5'
        },
        {
          label: '地下水',
          value: '6'
        },
        {
          label: '海水淡化',
          value: '7'
        },
        {
          label: '山泉水',
          value: '8'
        },
        {
          label: '其他',
          value: '9'
        }
      ],
      waterSourScale: '',
      waterSourScaleOption: [
        {
          label: '县级以上',
          value: '1'
        },
        {
          label: '千吨万人',
          value: '2'
        },
        {
          label: '200吨-1000吨',
          value: '3'
        },
        {
          label: '200吨以下',
          value: '4'
        }
      ],
      waterSourSearchVal: '',
      waterSourData: [],
      waterSourLevel: '',
      waterSourLevelOption: [
        {
          label: '暂无风险',
          value: '1'
        },
        {
          label: '黄色预警',
          value: '2'
        },
        {
          label: '橙色预警',
          value: '3'
        },
        {
          label: '红色预警',
          value: '4'
        }
      ],
      waterWarnData: [],

      // 珊溪
      countList: {},
      stationType: '',
      stationTypeOption: [],
      shanXiWaterSearchVal: '',
      sxWaterData: [],
      chaosType: '',
      chaosTypeOption: [
        { label: '全部', value: '' },
        { label: '水土流失', value: '水土流失' },
        { label: '非法采砂', value: '非法采砂' },
        { label: '倾倒垃圾', value: '倾倒垃圾' },
        { label: '违章建筑', value: '违章建筑' },
        { label: '占用水域', value: '占用水域' },
        { label: '非法排污', value: '非法排污' },
        { label: '畜禽养殖', value: '畜禽养殖' }
      ],
      chaosStatus: '',
      chaosStatusOption: [
        { label: '全部', value: '' },
        { label: '已完成', value: '已完成' },
        { label: '整改中', value: '整改中' }
      ],
      chaosName: '',
      sxCheckData: [],
      // 取水户
      wtaerIntakeType: '',
      wtaerIntakeTypeOption: [
        { label: '全部', value: '' },
        { label: '既用又取', value: '既用又取' },
        { label: '只取不用', value: '只取不用' },
        { label: '只用不取', value: '只用不取' }
      ],
      wtaerIntakeCount: {},
      wtaerIntakeData: [],
      wtaerIntakeWarnType: '',
      // wtaerIntakeWarnTypeOption: [
      //   { label: "全部", value: "" },
      //   { label: "正常", value: "正常" },
      //   { label: "接近许可", value: "接近许可" },
      //   { label: "超许可", value: "超许可" },
      // ],
      // 批后监管
      approSupeCount: {},
      riverType: '',
      riverTypeOption: [
        { label: '全部', value: '' },
        { label: '1级', value: '1' },
        { label: '2级', value: '2' },
        { label: '3级', value: '3' },
        { label: '4级', value: '4' },
        { label: '5级', value: '5' }
      ],
      supercisionStatu: '',
      supercisionStatuOption: [
        { label: '全部', value: '' },
        { label: '新项目', value: '1' },
        { label: '监管中', value: '2' },
        { label: '复核中', value: '3' },
        { label: '移交执法大队', value: '4' }
      ],
      projStage: '',
      projStageOption: [
        { label: '全部', value: '' },
        { label: '前期阶段', value: '1' },
        { label: '施工阶段', value: '2' },
        { label: '完工阶段', value: '3' }
      ],
      approSuperData: [],
      // 水域变化
      waterChangeCount: {},
      wtaerChangeData: [],
      // 清四乱
      fourChaosCount: {},
      rectiRange: '',
      rectiRangeOption: [
        { label: '全部', value: '' },
        { label: '已处理', value: '1' },
        { label: '未处理', value: '0' },
        { label: '逾期未处理', value: '2' },
        { label: '驳回', value: '3' }
      ],
      reasonFrom: '',
      reasonFromOption: [
        { label: '全部', value: '' },
        { label: '市级督查', value: '市级督查' },
        { label: '县级自查', value: '县级自查' }
      ],
      fourChaosType: '',
      fourChaosTypeOption: [
        { label: '全部', value: '' },
        { label: '乱占', value: '乱占' },
        { label: '乱采', value: '乱采' },
        { label: '乱堆', value: '乱堆' },
        { label: '乱建', value: '乱建' },
        { label: '其他', value: '其他' }
      ],
      severity: '',
      severityOption: [
        { label: '全部', value: '' },
        { label: '轻微', value: '轻微' },
        { label: '中度', value: '中度' },
        { label: '重大', value: '重大' }
      ],
      qslSearchVal: '',
      fourChaosData: [],

      // 美丽河湖
      activeTabBeauty: '美丽河湖',
      projStatu: '',
      projStatuOption: [
        { label: '全部', value: '' },
        { label: '项目在建', value: '项目在建' },
        { label: '已完工', value: '已完工' }
      ],
      beautyData: [],
      beautyCount: {},
      searchVal: '',
      // 水电站
      sdzCount: {},
      sdzType: '',
      sdzTypeOption: [
        { label: '全部', value: '' },
        { label: '饮水式', value: '2' },
        { label: '坝式(河床)', value: '1' },
        { label: '坝式(坝后)', value: '3' },
        { label: '混合式', value: '0' },
        { label: '抽水蓄能', value: '4' }
      ],
      sdzSearchVal: '',
      hydroStaData: [],
      //风险隐患
      riskCount: {
        totalNum: 0
      },
      // 河长巡查
      patrolCount: {},
      patrolDate: '',
      dealStatu: '',
      question: '',
      questionOption: [
        { label: '轻微', value: 0 },
        { label: '一般问题', value: 1 },
        { label: '重大问题', value: 2 }
      ],
      dealStatuOption: [
        { label: '全部', value: '' },
        { label: '未受理', value: 1 },
        { label: '处理中', value: 2 },
        { label: '已处理待评价', value: 3 },
        { label: '已处理已评价', value: 4 }
      ],
      patrolData: [],
      // 以下是报警数据
      alarmStatisticData: {}, //报警统计内容
      tableDataBJ: [], //报警列表
      BJdynamicColumnSetting: [],
      callPoliceValue: '',
      fullscreen: false,
      AlarmRainfallOption: [
        { label: '水库', value: '水库' },
        { label: '河道', value: '河道' },
        { label: '堰闸', value: '堰闸' },
        { label: '潮位', value: '潮位' }
      ],
      AlarmRainfallValue: '',
      AlarmWaterLevelOptions: [
        { label: '1小时超30mm', value: '1小时超30mm' },
        { label: '3小时超60mm', value: '3小时超60mm' }
      ],
      AlarmWaterLevelValue: '',
      seawallWarningsTable: [
        {
          prop: 'seawallName',
          label: '海塘名称'
        },
        {
          prop: 'seawallType',
          label: '海塘类型'
        },
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'tdz',
          label: '潮位'
        },
        {
          prop: 'tideStationBlueLevel',
          label: '蓝色警戒潮位'
        },
        {
          prop: 'tideStationYellowLevel',
          label: '黄色警戒潮位'
        },
        {
          prop: 'tideStationOrangeLevel',
          label: '橙色警戒潮位'
        },
        {
          prop: 'tideStationRedLevel',
          label: '红色警戒潮位'
        }
      ],
      waterLevelTable: [
        {
          prop: 'stTypeName',
          label: '站点类型'
        },
        {
          prop: 'stName',
          label: '名称'
        },
        {
          prop: 'waterLevel',
          label: '水位'
        },
        {
          prop: 'warningLevel',
          label: '警戒水位'
        },
        {
          prop: 'limitLevel',
          label: '汛限水位'
        }
      ],
      rainfallAlarmsTable: [
        {
          prop: 'tm',
          label: '时间'
        },
        {
          prop: 'statusName',
          label: '降雨报警类型'
        },
        {
          prop: 'stName',
          label: '名称'
        },
        {
          prop: 'drp',
          label: '降雨量'
        }
      ],
      typhoonsAlarmsTable: [
        {
          prop: 'name',
          label: '台风名称'
        },
        {
          prop: 'waterLevel',
          label: '实时风力'
        },
        {
          prop: 'limitLevel',
          label: '距离鹿城(km)'
        }
      ],
      runAlarmTable: [
        {
          prop: 'seawallName',
          label: '海塘名称'
        },
        {
          prop: 'context',
          label: '报警内容'
        }
      ]
    };
  },
  computed: {
    ...mapGetters(['formatterStartTime', 'formatterCurrentTime']),
    startTime() {
      let time = this.formatterStartTime();
      return time != 'Invalid date' ? time : '请选择时间';
    },
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != 'Invalid date' ? time : '请选择时间';
    },
    getIcon() {
      return ['mlhh-dialog-count-1', 'mlhh-dialog-count-2', 'mlhh-dialog-count-3', 'mlhh-dialog-count-4'][
        this.activeTabBeauty
      ];
    },
    /**
     * 动态表格数据
     * @returns {any[]}
     */
    dynamicTableData() {
      if (Array.isArray(tableSettings[this.code])) {
        let ret;
        tableSettings[this.code].forEach((item, index) => {
          ret = index == 0 ? this[item] : ret[item];
        });
        return ret;
      } else return this[tableSettings[this.code]];
    },
    /**
     * 动态列配置
     * @returns {import('./tableSettings/index.js').ColumnProp[]}
     */
    dynamicColumnSetting() {
      return columnSettings[this.code].filter((column) => {
        if (column.conditions) {
          const conditions = column.conditions;
          let flag = false;
          for (let i = 0; i < conditions.length; i += 3) {
            if (
              (conditions[i + 1] == 'eq' && this[conditions[i]] == conditions[i + 2]) ||
              (conditions[i + 1] == 'neq' && this[conditions[i]] != conditions[i + 2])
            ) {
              flag = true;
            }
          }
          return flag;
        } else return true;
      });
    },
    /**
     * 根据`this.code`显示placeholder
     * @returns {string}
     */
    calcInputPlaceholder() {
      const placeholderMap = {
        defualt: `请输入${this.code}名称`,
        救援仓库: `请输入救援仓库点名称`
      };
      return placeholderMap[this.code in placeholderMap ? this.code : 'defualt'];
    },
    /**
     * 标题
     * @returns {string}
     */
    asyncTitle() {
      const titleMap = {
        水闸: '温瑞平水闸'
      };
      return this.code in titleMap ? titleMap[this.code] : this.code;
    }
  },
  watch: {
    startTime: {
      immediate: true,
      handler(val) {
        if (val != '请选择时间') {
          this.tideStaStart = val + ':00';
        }
      }
    },
    currentTime: {
      immediate: true,
      handler(val) {
        if (val != '请选择时间') {
          this.tideStaEnd = val + ':00';
          this.$nextTick(() => {
            this.queryVal();
          });
        }
      }
    }
  },
  mounted() {
    if (this.code == '水情') {
      this.getOverView('RR');
      this.getOverView('ZZ');
      this.getOverView('DD');
      this.getOverView('TT');
      this.WRdynamicColumnSetting = this.reservoirWLM;
    }
    if (this.code == '工情') {
      this.WKdynamicColumnSetting = this.reservoirWorksTable;
      this.WaterRegime()
    }
    if (this.code == '报警') {
      this._waterLoggingStats();
    }
    if (this.formatterCurrentTime() != 'Invalid date') {
      this.tideStaStart = this.formatterStartTime() + ':00';
      this.tideStaEnd = this.formatterCurrentTime() + ':00';
    } else {
      this.tideStaStart = this.$moment().subtract(3, 'days').format('YYYY-MM-DD HH') + ':00:00';
      this.tideStaEnd = this.$moment().subtract(0, 'days').format('YYYY-MM-DD HH') + ':00:00';
    }
    this.getYearOption();
    // this.getEarlyType();
    this.getList();
    // this.getArea().then(() => {
    //   this.$nextTick(() => {

    //   });
    // });
  },
  methods: {
    /**
     * 根据`this.code`获取数据
     */
    getList() {
      switch (this.code) {
        case '水情':
          this.waterProjList();
          break;
        case '工情':
          this.workConditionoList();
          break;
        case '河网':
          this.riverNetworkList();
          break;
        case '潮位':
          this.tideList();
          break;
        case '病险工程':
          this.dilapProjList();
          break;
        case '风险隐患':
          this.projRiskList();
          this.riskTypeStatistics();
          break;
        case '海塘':
          this.seawallList();
          this.seawallProjStats();
          break;
        case '水库分析':
          this.reservoirAnalysisInvoking();
          break;
        case '水闸分析':
          this.lockAnalysisInvoking();
          break;
        case '河网分析':
          this.riverNetworkAnalysis();
          break;
        case '潮位分析':
          this.waterAnalysisInvoking();
          break;
        case '防汛仓库':
          this.warehouseList();
          this._getDict('warehouseType');
          break;
        case '防汛队伍':
          this.ranksList();
          this._getDict('teamType');
          break;
        case '视频监控':
          this.videoList();
          break;
        case '救援仓库':
          this.waterLoggingList();
          break;
        case '洪水风险图':
          this.floodRiskList();
          break;
        case '降水量':
          this.precipitationList();
          this.stats4WrbRainfallList();
          break;
        case '水资源量':
          this.waterResourceList();
          break;
        case '供水量':
          this.waterRupplyList();
          this.stats4WrbWaterSupplyWrbWaterSupply();
          break;
        case '用水量':
          this.waterUseList();
          break;
        case '救援队伍':
          // this.reservoirStorageList();
          break;
        case '水资源承载能力':
          this.changeActiveTabCarry(this.activeTabCarry);
          break;
        case '预警信息':
          this.getEarlyDetail();
          break;
        case '水源地':
          this.getWaterSourList();
          break;
        case '水厂':
          this.AttSwhsBaseLoadPage();
          break;
        case '水厂运行状态':
          this.AttSwhsBaseLoadPage();
          break;
        case '水量预警':
          this.getWaterWarnList();
          break;
        case '珊溪水质监测':
          this.getSXWaterList();
          break;
        case '巡查':
          this.happeningListJson();
          this.patrolInfoPatrolChaosHappStatic();
          break;
        case '取水户':
          this.stat4ListWiu();
          this.getWaterIntakeList();
          break;
        case '批后监管':
          this.homeOverView();
          this.getApproSuperList();
          break;
        case '水域变化':
          this.cPositionStatisticsWaterArea();
          this.getWaterChangeList();
          break;
        case '清四乱':
          this.statisProblemC();
          this.getFourChaosList();
          break;
        case '美丽河湖':
          this.eachProjectStatistics();
          this.getBeautifulRevierList();
          break;
        case '水电站':
          this.mainTotalNum();
          this.getHydropowerStationList();
          break;
        case '河长巡查':
          this.statsByQueryCondition();
          this.queryPatrolProblem();
          break;
        case '报警':
          console.log(this.mediumSized, '45465465');
          if (this.mediumSized == 1) {
            this.BJdynamicColumnSetting = this.seawallWarningsTable;
            this.callThePoliceList('实时报警');
          } else if (this.mediumSized == 2) {
            this.callThePoliceList('水情报警');
            this.BJdynamicColumnSetting = this.waterLevelTable;
          } else {
            this.BJdynamicColumnSetting = this.typhoonsAlarmsTable;
            this.callThePoliceList();
          }
          break;
      }
    },
    /**
     * 动态label
     * @param {string|()=>string} label
     */
    dynamicColumnLabel(label) {
      /** @description 使用call转换作用域*/
      if (typeof label == 'function') return label(this);
      return label;
    },
    /**
     * 表格列数据格式化
     * @param {number|string} value
     */
    columnFormatter(row, column, value, index) {
      if (value) {
        if (typeof value == 'number' && parseFloat(value) != parseInt(value)) {
          return value.toFixed(2);
        }
        if (typeof value == 'string' && value.split(',').length > 1) {
          return parseInt(value.split(',')[0]).toFixed(1);
        }
        return value;
      } else {
        return '-';
      }
    },
    /**
     * 表格序号项编号
     * @param {number} index
     * @returns {number}
     */
    indexMethod(index) {
      return this.pageSize * (this.pageNum - 1) + index + 1;
    },

    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$refs.multipleTable.doLayout();
    },
    // 获取预警类型
    async getEarlyType() {
      this.earlyTypeOption = [];
      await inforApi.getEarlyType().then((res) => {
        res.data.forEach((item) => {
          let obj = { value: item, label: item };
          this.earlyTypeOption.push(obj);
        });
      });
    },
    /**
     * 翻页器分页尺寸变化事件
     */
    onSizeChange(val) {
      this.pageSize = val;
      this.selectVal();
    },
    /**
     * 海塘统计接口请求
     */
    async seawallProjStats() {
      let obj = {
        adcd: this.administrationValue, // 行政区划编码
        grade: this.engineeringValue, // 1级、2级、3级、4级、5级
        designTideStandard: this.designTideStandard,
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        seawallName: this.searchVal
      };
      await disasterPreventionApi.stats4PageSeawall(obj).then((res) => {
        if (res.code == 0) {
          this.seawallData = res.data;
        }
      });
    },
    /**
     * 通用模糊查询
     */
    queryVal() {
      switch (this.code) {
        case '水库分析':
          this.reservoirAnalysisInvoking();
          break;
        case '水情':
          this.waterProjList();
          break;
        case '工情':
          this.workConditionoList();
          break;
        case '水闸分析':
          this.lockAnalysisInvoking();
          break;
        case '河网分析':
          this.riverNetworkAnalysis();
          break;
        case '潮位分析':
          this.waterAnalysisInvoking();
          break;
        case '救援仓库':
          this.waterLoggingList();
          break;
        case '防汛仓库':
          this.warehouseList();
          break;
        case '防汛队伍':
          this.ranksList();
          break;
        case '视频监控':
          this.videoList();
          break;
      }
    },
    /**
     * 第三场景 水库接口调用
     */
    async reservoirAnalysisInvoking() {
      this.loading = true;
      let obj = {
        start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
        end: this.tideStaEnd, // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
        resName: this.reservoirName
      };
      await disasterPreventionApi
        .rsvrReplayStats(obj)
        .then((res) => {
          if (res.code == 0) {
            this.reservoirAnalysisData = res.data;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 第三场景 水闸接口调用
     */
    async lockAnalysisInvoking() {
      this.loading = true;
      let obj = {
        start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
        end: this.tideStaEnd, // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
        wagaName: this.sluiceName
      };
      await disasterPreventionApi
        .wrpWagaReplayStats(obj)
        .then((res) => {
          if (res.code == 0) {
            this.lockAnalysisData = res.data.list;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 第三场景 河网接口调用
     */
    async riverNetworkAnalysis() {
      this.loading = true;
      let obj = {
        start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
        end: this.tideStaEnd, // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
        rnName: this.riverName
      };
      await disasterPreventionApi
        .rivernetReplayStats(obj)
        .then((res) => {
          if (res.code == 0) {
            this.riverNetworkData = res.data.list;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 第三场景 潮位接口调用
     */
    async waterAnalysisInvoking() {
      this.loading = true;
      let obj = {
        start: this.tideStaStart, // 复盘起始时刻 格式：yyyy-MM-dd HH:mm:ss
        end: this.tideStaEnd, // 复盘结束时刻 格式：yyyy-MM-dd HH:mm:ss
        stName: this.seaLevelName
      };
      await disasterPreventionApi
        .waterAnalysisList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.waterAnalysisData = res.data.data.list;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 弹窗关闭
     */
    handleClose() {
      this.$emit('close');
    },

    /**
     * 海塘接口调用
     */
    async seawallList() {
      this.loading = true;
      let obj = {
        adcd: this.administrationValue, // 行政区划编码
        grade: this.engineeringValue, // 1级、2级、3级、4级、5级
        designTideStandard: this.designTideStandard,
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        seawallName: this.searchVal
      };
      await disasterPreventionApi
        .pageSeawall(obj)
        .then((res) => {
          if (res.code == 0) {
            this.seawallProjData = res.data.list;
            this.totalPage = res.data.totalRows;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 行的回调函数
     */
    rowClassName({ row }) {
      return row.overLimitVal && row.overLimitVal > 0 ? 'warm-row' : '';
    },
    /**
     * 水库字段排序
     */
    sortChange(val) {
      if (val.order) {
        if (val.order == 'ascending') {
          this.orderColumn = val.prop;
          this.ascOrDesc = false;
        } else {
          this.orderColumn = val.prop;
          this.ascOrDesc = true;
        }
      } else {
        this.orderColumn = '';
        this.ascOrDesc = '';
      }
      if (this.code) {
        switch (this.code) {
          case '水情':
            this.wagaProjList();
            break;
          case '工情':
            this.workConditionoList();
            break;
          case '河网':
            this.riverNetworkList();
            break;
          case '潮位':
            this.tideList();
            break;
          case '病险工程':
            this.dilapProjList();
            break;
          case '海塘':
            this.seawallList();
            this.seawallProjStats();
            break;
          case '降水量':
            this.precipitationList();
            this.stats4WrbRainfallList();
            break;
          case '水资源量':
            this.waterResourceList();
            break;
          case '供水量':
            this.waterRupplyList();
            this.stats4WrbWaterSupplyWrbWaterSupply();
            break;
          case '用水量':
            this.waterUseList();
            break;
          case '救援队伍':
            // this.reservoirStorageList();
            break;
          case '水资源承载能力':
            this.changeActiveTabCarry(this.activeTabCarry);
            break;
        }
      }
    },
    /**
     * 风险隐患统计
     */
    async riskTypeStatistics() {
      let res = await disasterPreventionApi.riskTypeStatistics();
      this.riskCount = {
        totalNum: res.data[0].total
      };
      res.data.forEach((item) => {
        if (item.sbSignName == '隐患点') {
          this.riskCount.riskNum = item.num;
        } else if (item.sbSignName == '高风险点') {
          this.riskCount.highRiskNum = item.num;
        } else if (item.sbSignName == '薄弱点') {
          this.riskCount.weakNum = item.num;
        }
      });
    },
    /**
     * 风险隐患列表
     */
    async projRiskList() {
      let arr = '';
      // let str = "";
      if (this.projectTypeVal.length == 2) {
        arr = this.projectTypeVal[0] + '-' + this.projectTypeVal[1];
      } else {
        arr = this.projectTypeVal[0];
      }
      // if (this.district.length == 3) {
      //   str = this.district[2];
      // } else if (this.district.length == 2) {
      //   str = this.district[1];
      // } else {
      //   str = "";
      // }
      this.loading = true;
      // const startTime = this.affiliatedUnit ? this.affiliatedUnit[0] : "";
      // const endTime = this.affiliatedUnit ? this.affiliatedUnit[1] : "";
      let obj = {
        projName: this.projectNameVal, // 工程名称
        projType: arr, // 工程类型
        sbSign: this.riskTypeVal, // 风险类型
        adcdDuty: this.administrationValue, // 行政区划
        property: this.discoveryChannel, // 发现渠道
        recCondition: this.reorganizeSituation,
        reportTime: this.reportTime, // 发现日期起始点
        pageSize: this.pageSize,
        pageNum: this.pageNum
      };
      await disasterPreventionApi
        .busRiskListPage(obj)
        .then((res) => {
          if (res.code == 0) {
            this.potentialRisksData = res.data.list;
            this.totalPage = res.data.totalRows;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },

    /**
     * 病险工程
     */
    async dilapProjList() {
      this.loading = true;
      let obj = {
        adcd: this.administrationValue, // 行政区划编码
        bas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
        projType: this.projType,
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        projectName: this.searchVal
      };
      await disasterPreventionApi
        .dilapProjList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.dangerouslyData = res.data.list;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 潮位接口
     */
    async tideList() {
      this.loading = true;
      let obj = {
        adcd: this.administrationValue, // 行政区划编码
        bas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        stName: this.searchVal
      };
      await disasterPreventionApi
        .tideList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.seaLevelData = res.data.list;
            this.seaLevelList = res.data;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 河网接口
     */
    async riverNetworkList() {
      this.loading = true;
      let obj = {
        adcd: this.administrationValue, // 行政区划编码
        bas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        mainStName: this.searchVal
      };
      await disasterPreventionApi
        .riverNetworkList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.riverwayData = res.data.list;
            this.riverwayList = res.data;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    // 获取水情所有数据 统计数据自行计算
    async getOverView(stationType) {
      // RR ZZ  DD TT
      // ['水库','河道', '堰闸','潮位',],
      const opt = { stationType };
      await waterAndRain.waterStationsList(opt).then((res) => {
        if (res.code == 0) {
          let data = res.data.filter((item) => item.warning == true);
          let LevelLimit = data.length;
          let levelNumber = res.data.length;
          if (stationType == 'RR') {
            this.reservoirStatistical_a = { name: '水库水位超警', value: LevelLimit, total: levelNumber, code: 'RR' };
          } else if (stationType == 'ZZ') {
            this.reservoirStatistical_b = { name: '河道水位超警', value: LevelLimit, total: levelNumber, code: 'ZZ' };
          } else if (stationType == 'DD') {
            this.reservoirStatistical_c = { name: '堰闸水位超警', value: LevelLimit, total: levelNumber, code: 'DD' };
          } else if (stationType == 'TT') {
            this.reservoirStatistical_d = { name: '潮位超警', value: LevelLimit, total: levelNumber, code: 'TT' };
          }
          this.$forceUpdate();
        }
      });
    },
    /**
     * 工情统计数据
     */
    async WaterRegime() {
      WorkingConditionsApi.workingConditionsTotal().then((res) => {
        if (res.code == 0) {
          this.workStatisticalData = res.data;
        }
      });
    },
    // 水情列表
    async waterProjList() {
      this.loading = true;
      // RR ZZ  DD TT
      // ['水库','河道', '堰闸','潮位',],
      const opt = {
        stationType: this.siteTypeValue ? this.siteTypeValue : 'RR',
        stationName: this.searchVal ? this.searchVal : '',
        warning: this.callThePoliceValue ? !!this.callThePoliceValue : false
      };
      await waterAndRain.waterStationsList(opt).then((res) => {
        if (res.code == 0) {
          this.loading = false;
          this.waterRegimeData = res.data;
          this.$forceUpdate();
        }
      });
    },
    // 工情列表
    async workConditionoList() {
      this.loading = true;
      let obj = {
        eng_scal: this.worksProScaleValue,
        pjnm: this.searchVal,
        pjtp: this.workConditionValue,
      };
      await WorkingConditionsApi.workingConditionsList(obj)
        .then((res) => {
          if (res.code == 0) {
           
            this.workRegimeData = res.data;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    // 报警统计数据
    async _waterLoggingStats() {
      let res = await alarmPeople.giveAnAlarmCount();
      this.alarmStatisticData = res.data;
    },
    callThePoliceList(type) {
      if (this.mediumSized == 1) {
        this.getPoliceList(type);
      } else if (this.mediumSized == 2) {
        this.waterRainfallRegime(type);
      } else {
        this.typhoonList();
      }
    },
    // 海塘
    async getPoliceList(type) {
      let res;
      if (type == '实时报警') {
        res = await alarmPeople.seawallGiveAnAlarm();
        if (res.code == 0) {
          this.tableDataBJ = res.data.list;
        }
      } else if (type == '预报预警') {
        res = await alarmPeople.seawallWarning();
        if (res.code == 0) {
          this.tableDataBJ = res.data.list;
        }
      } else {
        res = await alarmPeople.runGiveAnAlarm();
        if (res.code == 0) {
          this.tableDataBJ = res.data.list;
        }
      }
    },
    //台风预警
    async typhoonList() {
      let res = await alarmPeople.typhoonGiveAnAlarm();
      if (res.code == 0) {
        if (res.data.list.length > 0) {
          this.typhoonData = res.data.list.map((item) => {
            return {
              name: item.name,
              limitLevel: item.points[item.points.length - 1].distance,
              waterLevel: item.points[item.points.length - 1].power,
              data: item
            };
          });
        }
        // this.typhoonData = res.data.list;
      } else {
        this.typhoonData = res.data.list;
      }
    },
    //水雨情
    async waterRainfallRegime(type) {
      let res;
      if (type == '水情报警') {
        res = await alarmPeople.waterGiveAnAlarm();
        if (res.code == 0) {
           res.data.list.map(i => {
              switch (i.stType) {
                case 'TT':
                  i.stTypeName = '潮位'
                  break;
                case 'DD':
                  i.stTypeName = '水闸'
                  break;
                case 'RR':
                  i.stTypeName = '水库'
                  break;
                default:
                  i.stTypeName = '河道'
                  break;
              }
            })
          this.tableDataBJ = res.data.list;
        }
      } else {
        res = await alarmPeople.rainGiveAnAlarm();
        if (res.code == 0) {
          this.tableDataBJ = res.data.list.map((item) => {
            return {
              stName: item.stName,
              drp: item.drp,
              tm: item.tm,
              status: item.status,
              statusName: item.status == 0 ? '正常' : item.status == 1 ? '1小时超30mm' : '3小时超60mm'
            };
          });
        }
      }
    },
    /**
     * 选中事件
     */
    selectVal() {
      this.numTime = '1';
      switch (this.code) {
        // 水情 切换站点类型时 更换table prop字段
        case '水情':
          // RR ZZ  DD TT
          if (this.siteTypeValue == 'RR') {
            this.WRdynamicColumnSetting = this.reservoirWLM;
          } else if (this.siteTypeValue == 'ZZ') {
            this.WRdynamicColumnSetting = this.weirGateWLM;
          } else if (this.siteTypeValue == 'DD') {
            this.WRdynamicColumnSetting = this.wadiWLM;
          } else if (this.siteTypeValue == 'TT') {
            this.WRdynamicColumnSetting = this.tidemarkWLM;
          } else {
            this.WRdynamicColumnSetting = this.reservoirWLM;
          }
          this.waterProjList();
          break;
        // 工情 切换站点类型时 更换table prop字段
        case '报警':
          let type = '';
          if (this.callPoliceValue == '台风') {
            this.BJdynamicColumnSetting = this.typhoonsAlarmsTable;
          } else if (this.callPoliceValue == '水位') {
            this.BJdynamicColumnSetting = this.waterLevelTable;
            type = '水情报警';
          } else if (this.callPoliceValue == '运行') {
            this.BJdynamicColumnSetting = this.runAlarmTable;
            type = '运行报警';
          } else if (this.callPoliceValue == '降雨') {
            this.BJdynamicColumnSetting = this.rainfallAlarmsTable;
            type = '降雨报警';
          } else if (this.callPoliceValue == '实时') {
            this.BJdynamicColumnSetting = this.seawallWarningsTable;
            type = '实时报警';
          } else {
            this.BJdynamicColumnSetting = this.seawallWarningsTable;
            type = '预报预警';
          }
          this.callThePoliceList(type);
          break;
        // 工情 切换站点类型时 更换table prop字段
        case '工情':
          if (this.workConditionValue == '水闸') {
            this.WKdynamicColumnSetting = this.sluiceWorksTable;
          } else if (this.workConditionValue == '泵站') {
            this.WKdynamicColumnSetting = this.pumpStationWorksTable;
          } else if (this.workConditionValue == '水库') {
            this.WKdynamicColumnSetting = this.reservoirWorksTable;
          }
          this.workConditionoList();
          break;
        case '河网':
          this.riverNetworkList();
          break;
        case '潮位':
          this.tideList();
          break;
        case '病险工程':
          this.dilapProjList();
          break;
        case '风险隐患':
          this.projRiskList();
          break;
        case '海塘':
          this.seawallList();
          this.seawallProjStats();
          break;

        case '防汛仓库':
          this.warehouseList();
          break;
        case '防汛队伍':
          this.ranksList();
          break;
        case '视频监控':
          this.videoList();
          break;
        case '救援仓库':
          this.waterLoggingList();
          break;
        case '洪水风险图':
          this.floodRiskList();
          break;
        case '降水量':
          this.precipitationList();
          this.stats4WrbRainfallList();
          break;
        case '水资源量':
          this.waterResourceList();
          break;
        case '供水量':
          this.waterRupplyList();
          this.stats4WrbWaterSupplyWrbWaterSupply();
          break;
        case '用水量':
          this.waterUseList();
          break;
        case '水资源承载能力':
          this.changeActiveTabCarry(this.activeTabCarry);
          break;
        case '救援队伍':
          this.reservoirStorageList();
          break;
        case '取水户':
          this.getWaterIntakeList();
          break;
        case '批后监管':
          this.getApproSuperList();
          break;
        case '水域变化':
          this.getWaterChangeList();
          break;
        case '清四乱':
          this.getFourChaosList();
          break;
        case '美丽河湖':
          this.getBeautifulRevierList();
          break;
        case '水电站':
          this.getHydropowerStationList();
          break;
        case '河长巡查':
          this.queryPatrolProblem();
          break;
      }
    },
    /**
     * 行政区划
     */
    async getArea() {
      const obj = { areaCode: 3303, level: 3 };
      await disasterPreventionApi.getArea(obj).then((res) => {
        if (res.code == 0) {
          this.administration = res.data;
        }
      });
    },
    /**
     * 防汛仓库
     */
    async warehouseList() {
      this.loading = true;
      let obj = {
        areaCode: this.administrationValue, // 行政区划编码
        typeId: this.warehouseType,
        name: this.searchVal
      };
      await disasterPreventionApi
        .warehousesList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.warehouseData = res.data;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 防汛队伍
     */
    async ranksList() {
      this.loading = true;
      let obj = {
        areaCode: this.administrationValue, // 行政区划编码
        typeId: this.ranksType,
        name: this.searchVal
      };
      await disasterPreventionApi
        .teamsList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.ranksData = res.data;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 视频监控
     */
    async videoList() {
      this.loading = true;
      let obj = {
        areaCode: this.administrationValue, // 行政区划编码
        projectType: this.projType,
        cameraName: this.NameVal,
        status: this.statusType,
        pageSize: this.pageSize,
        pageNum: this.pageNum
      };
      this.stats4PageCamera(obj);
      await disasterPreventionApi
        .listCamera(obj)
        .then((res) => {
          if (res.code == 0) {
            this.videoData = res.data.list;
            this.totalPage = res.data.totalRows;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },

    /**
     * 导出
     * @param {string} val
     */
    async exportExcel(val) {
      this.exportLoading = true;
      if (val == '视频监控') {
        const obj = {
          areaCode: this.administrationValue, // 行政区划编码
          projectType: this.projType,
          cameraName: this.NameVal,
          status: this.statusType,
          pageSize: this.pageSize,
          pageNum: this.pageNum
        };
        await exportPageCamera(obj);
      } else if (val == '河网') {
        const obj = {
          adcd: this.administrationValue, // 行政区划编码
          bas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          mainStName: this.searchVal
        };
        await exportRivernet(obj);
      } else if (val == '风险隐患') {
        let arr = '';
        let str = '';
        if (this.projectTypeVal.length == 2) {
          arr = this.projectTypeVal[0] + '-' + this.projectTypeVal[1];
        } else {
          arr = '';
        }
        if (this.district.length == 3) {
          str = this.district[2];
        } else if (this.district.length == 2) {
          str = this.district[1];
        } else {
          str = '';
        }
        const obj = {
          projName: this.projectNameVal, // 工程名称
          projType: arr, // 工程类型
          sbSign: this.riskTypeVal, // 风险类型
          adcdDuty: str ? str.substring(1, 7) : '', // 行政区划
          property: this.discoveryChannel, // 发现渠道
          recCondition: this.reorganizeSituation,
          startTime: this.affiliatedUnit[0], // 发现日期起始点
          endTime: this.affiliatedUnit[1],
          pageSize: this.pageSize,
          pageNum: this.pageNum
        };
        await exportBusRisk(obj);
      } else if (val == '海塘') {
        const obj = {
          adcd: this.administrationValue, // 行政区划编码
          grade: this.engineeringValue, // 1级、2级、3级、4级、5级
          designTideStandard: this.designTideStandard,
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          seawallName: this.searchVal
        };
        await exportSeawall(obj);
      } else if (val == '救援仓库') {
        const obj = {
          areaCode: this.administrationValue, // 行政区划编码
          ponding: this.waterLoggingType, // 是否有积水
          name: this.searchVal
        };
        await exportFloodPoint(obj);
      } else if (val == '水情') {
        const obj = {
          warning: this.callThePoliceValue ? !!this.callThePoliceValue : false,
          stationType: this.siteTypeValue,
          stationName: this.searchVal
        };
        await exportWrpWagaUnitTable(obj);
      } else if (val == '工情') {
        const obj = {
          warning: this.callThePoliceValue ? !!this.callThePoliceValue : false,
          stationType: this.siteTypeValue,
          stationName: this.searchVal
        };
        await exportWrpWagaUnitTable(obj);
      } else if (val == '洪水风险图') {
        const obj = { designStandard: this.floodLevel ? `${this.floodLevel}年一遇` : '' };
        await exportFloodRiskDisasterData(obj);
      } else if (val == '防汛仓库') {
        const obj = {
          areaCode: this.administrationValue,
          typeId: this.ranksType,
          name: this.searchVal,
          exportStrategy: 0
        };
        await exportWareHouses(obj);
      } else if (val == '防汛队伍') {
        const obj = {
          areaCode: this.administrationValue,
          typeId: this.warehouseType,
          name: this.searchVal,
          exportStrategy: 0
        };
        await exportTeams(obj);
      } else if (val == '供水量') {
        const obj = {
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc
          // year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
        };
        await exportWrabWaterSupply(obj);
      } else if (val == '用水量') {
        const obj = {
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ''
        };
        await exportWrabWaterSupply(obj);
      } else if (val == '降水量') {
        const obj = { startYear: this.startYear, endYear: this.endYear };
        await exportWrbRainfallList(obj);
      } else if (val == '水资源量') {
        const obj = { year: this.checkYear };
        await exportWrbWatResList(obj);
      } else if (val == '救援队伍') {
        const obj = {};
        await exportWrbStoreRsvrList(obj);
      }
      this.exportLoading = false;
    },
    /**
     * 视频监控统计
     */
    async stats4PageCamera(val) {
      let res = await disasterPreventionApi.stats4PageCamera(val);
      if (res.code == 0) {
        this.stats4PageCameraData = res.data;
      }
    },
    /**
     * 积水
     */
    async waterLoggingList() {
      this.loading = true;
      let obj = {
        areaCode: this.administrationValue, // 行政区划编码
        ponding: this.waterLoggingType, // 是否有积水
        name: this.searchVal
      };
      await disasterPreventionApi
        .waterLoggingList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.waterLoggingData = res.data;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    /**
     * 洪水风险图
     */
    async floodRiskList() {
      this.loading = true;
      let obj = {
        designStandard: this.floodLevel ? `${this.floodLevel}年一遇` : ''
      };
      await disasterPreventionApi
        .floodRiskList(obj)
        .then((res) => {
          if (res.code == 0) {
            this.floodRiskData = res.data.list;
          }
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    // 枚举
    async _getDict(type) {
      let res = await getDict(type);
      if (res.code == 0) {
        if (type == 'warehouseType') {
          this.warehouseOptions = res.data;
        } else if (type == 'teamType') {
          this.ranksOptions = res.data;
        }
      }
    },

    /**
     * 设置年份下拉选项
     */
    getYearOption() {
      const CURRENT_YEAR = new Date().getFullYear();
      this.yearOption = new Array(CURRENT_YEAR - 1990).fill(0).map((_, year) => ({
        label: 1990 + year + ' 年',
        value: 1990 + year + ' 年'
      }));
    },
    // 降水量接口
    async precipitationList() {
      const opt = { startYear: this.startYear, endYear: this.endYear };
      this.loading = true;
      await waterResourceApi.table4WrbRainfall(opt).then((res) => {
        this.precipitationData = res.data;
        this.loading = false;
      });
    },
    // 降水量 表头统计
    async stats4WrbRainfallList() {
      const opt = { startYear: this.startYear, endYear: this.endYear };
      await waterResourceApi.stats4WrbRainfallList(opt).then((res) => {
        this.precipitationVal = res.data;
      });
    },
    /**
     * 水资源统计
     */
    async stats4WaterResource() {
      const opt = { year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : '' };
      await getStats4WrbWatResList(opt).then((res) => {
        if (res.code == 0) {
          const { totalWaterResource, avgWrUseRate, watResPerCapita } = res.data.wzCityData;
          this.waterResourceStat = { totalWaterResource, avgWrUseRate, watResPerCapita };
        }
      });
    },
    /**
     * 水资源详表接口
     */
    async waterResourceList() {
      const opt = { year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : '' };
      this.loading = true;
      await getWrbWatResList(opt).then((res) => {
        this.waterResourceData = res.data;
        this.checkYear = res.data[0] ? res.data[0].year + ' 年' : this.checkYear;
        this.loading = false;
      });
      await this.stats4WaterResource();
    },
    // 供水量详表tab切换
    changeActiveTab(val) {
      if (val == '供水工程') return; // 供水工程暂不可点
      this.activeTab = val;
      this.waterRupplyList();
      this.stats4WrbWaterSupplyWrbWaterSupply();
    },
    // 供水量表头接口
    async stats4WrbWaterSupplyWrbWaterSupply() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc
        // year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      await waterResourceApi.stats4WrbWaterSupplyWrbWaterSupply(opt).then((res) => {
        this.WrbWaterSupplyData = res.data;
      });
    },
    // 供水量量详表接口
    async waterRupplyList() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc
        // year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      this.loading = true;
      await waterResourceApi.table4WrbWaterSupply(opt).then((res) => {
        this.waterRupplyData = res.data;
        this.loading = false;
      });
    },
    // 用水量详表接口
    async waterUseList() {
      const opt = { year: this.checkYear.substring(0, 4) ? this.checkYear.substring(0, 4) : '' };
      this.loading = true;
      await getWrbWatConList(opt).then((res) => {
        this.waterUseData = res.data;
        this.checkYear = res.data[0] ? res.data[0].year + ' 年' : this.checkYear;
        this.loading = false;
      });
    },
    //救援队伍详表接口
    // async reservoirStorageList() {
    //   const { startYear, endYear } = this;
    //   this.loading = true;
    //   await getWrbStoreRsvrList({ startYear, endYear }).then((res) => {
    //     this.reservoirStorageData = res.data;
    //     this.loading = false;
    //   });
    //   await getStats4WrbStoreRsvrList({ startYear, endYear }).then((res) => {
    //     const { maxStoreIncre, maxStoreDecre, shanxiMaxStoreIncre, shanxiMaxStoreDecre } = res.data;
    //     this.reservoirStorageStat = { maxStoreIncre, maxStoreDecre, shanxiMaxStoreIncre, shanxiMaxStoreDecre };
    //   });
    // },
    // 水资源承载能力详表tab切换
    changeActiveTabCarry(val) {
      this.activeTabCarry = val;
      switch (val) {
        case '水量评价':
          this.waterConsumeEvaluation();
          break;
        case '水质评价':
          this.waterQualityEvaluation();
          break;
        case '经济评价':
          this.ecoAppraisalTable();
          break;
        case '人口评价':
          this.popAppraisalTable();
          break;
      }
    },
    // 水资源承载能力详表接口 水量评价
    async waterConsumeEvaluation() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ''
      };
      this.loading = true;
      await waterResourceApi.waterConsumeEvaluation(opt).then((res) => {
        this.waterResCarryData = res.data.tableData;
        this.checkYear = res.data.tableData[0] ? res.data.tableData[0].year + ' 年' : this.checkYear;
        this.loading = false;
      });
    },
    // 水资源承载能力详表接口 水质评价
    async waterQualityEvaluation() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ''
      };
      this.loading = true;
      await waterResourceApi.waterQualityEvaluation(opt).then((res) => {
        this.waterResCarryData = res.data.tableData;
        this.loading = false;
      });
    },
    // 水资源承载能力详表接口 经济评价
    async ecoAppraisalTable() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ''
      };
      this.loading = true;
      await waterResourceApi.ecoAppraisalTable(opt).then((res) => {
        this.waterResCarryData = res.data;
        this.loading = false;
      });
    },
    // 水资源承载能力详表接口 人口评价
    async popAppraisalTable() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ''
      };
      this.loading = true;
      await waterResourceApi.popAppraisalTable(opt).then((res) => {
        this.waterResCarryData = res.data;
        this.loading = false;
      });
    },

    // 预警信息
    async getEarlyDetail() {
      let opt = {
        messageType: '预警',
        type: this.earlyType,
        content: this.earlySearchVal,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await inforApi.getEarlyDetail(opt).then((res) => {
        this.earlyData = res.data.datas.list;
        this.totalPage = res.data.total;
        this.loading = false;
      });
    },
    /**
     * 翻页器当前页数切换事件
     * @param {number} val
     */
    onCurrentChange(val) {
      this.pageNum = val;
      switch (this.code) {
        case '预警信息':
          this.getEarlyDetail();
          break;
        case '水域变化':
          this.getWaterChangeList();
          break;
        case '批后监管':
          this.getApproSuperList();
          break;
        case '清四乱':
          this.getFourChaosList();
          break;
        case '美丽河湖':
          this.getBeautifulRevierList();
          break;
        case '水电站':
          this.getHydropowerStationList();
          break;
        case '河长巡查':
          this.queryPatrolProblem();
          break;
        case '视频监控':
          this.videoList();
          break;
        case '风险隐患':
          this.projRiskList();
          break;
        case '海塘':
          this.seawallList();
          this.seawallProjStats();
          break;
        case '水情':
          this.waterProjList();
          break;
        case '工情':
          this.workConditionsList();
          break;
        case '报警':
          this.callThePoliceList();
          break;
      }
    },
    // 水厂表头
    async stats4listWaterWorks(opt) {
      let res = await waterResourceApi.stats4listWaterWorks(opt);
      if (res.code == 0) {
        this.waterPointData = res.data;
      }
    },
    // 水厂 水厂运行行状态
    async AttSwhsBaseLoadPage() {
      this.loading = true;
      this.waterPointList = [];
      let opt = {
        withRuntimeInfo: true,
        adcd: this.adcdData, //行政区划
        cwsName: this.cwsName, //水厂名称
        engType: this.engType, //水厂类型
        wasuRang: this.wasuRang, //水厂规模
        runtimeState: this.runtimeState //水厂运行状态
      };
      this.stats4listWaterWorks(opt);
      let res = await waterResourceApi.listWaterWorks(opt);
      if (res.code == 0) {
        this.waterPointList = res.data;
      }
      this.loading = false;
    },
    // 水源地
    async getWaterSourList() {
      let opt = {
        supplyScale: this.waterSourScale,
        wainWasoType: this.waterSourType,
        swhsName: this.waterSourSearchVal,
        swhsLoc: this.administrationValue
      };
      this.waterSuantityStatistic('1');
      let res = await waterResourceApi.AttSwhsBaseLoadPage(opt);
      if (res.code == 0) {
        this.waterSourData = res.data.list;
      }
    },
    // 水量预警
    async getWaterWarnList() {
      let opt = {
        warnType: this.waterSourLevel,
        wainWasoType: this.waterSourType,
        swhsName: this.waterSourSearchVal,
        swhsLoc: this.administrationValue
      };
      this.waterSuantityStatistic('2');
      let res = await waterResourceApi.AttSwhsBaseLoadPage(opt);
      if (res.code == 0) {
        this.waterWarnData = res.data.list;
      }
    },
    // 水源地统计
    async waterSuantityStatistic(val) {
      let opt = {
        statisticalType: val,
        warnType: this.waterSourLevel,
        wainWasoType: this.waterSourType,
        swhsName: this.waterSourSearchVal,
        swhsLoc: this.administrationValue,
        supplyScale: this.waterSourScale
      };
      let res = await waterResourceApi.waterSuantityStatistic(opt);
      if (res.code == 0) {
        this.wasuObjTypeData = res.data;
      }
    },

    // 获取珊溪水质监测列表
    getSXWaterList() { },
    // 获取珊溪统计数据
    async patrolInfoPatrolChaosHappStatic() {
      await waterResourceApi.patrolInfoPatrolChaosHappStatic().then((res) => {
        if (res.data) this.countList = res.data;
      });
    },
    // 珊溪巡查列表
    async happeningListJson() {
      let opt = {
        chaosType: this.chaosType,
        chaosStatus: this.chaosStatus,
        chaosName: this.chaosName
      };
      this.loading = true;
      await waterResourceApi.happeningListJson(opt).then((res) => {
        this.sxCheckData = res.data;
        this.loading = false;
      });
    },
    // 取水户 统计
    async stat4ListWiu() {
      if (this.code != '取水户') return;
      await waterResourceApi.stat4ListWiu().then((res) => {
        this.wtaerIntakeCount = res.data;
      });
    },
    // 取水户
    async getWaterIntakeList() {
      if (this.code != '取水户') return;
      let opt = {
        areaCode: this.administrationValue,
        warning: this.wtaerIntakeWarnType,
        wiuType: this.wtaerIntakeType
      };
      this.loading = true;
      await waterResourceApi.listWiu(opt).then((res) => {
        this.wtaerIntakeData = res.data;
        this.loading = false;
      });
    },
    // 批后监管 统计
    async homeOverView() {
      if (this.code != '批后监管') return;
      await waterResourceApi.homeOverView().then((res) => {
        this.approSupeCount = res.data;
      });
    },
    // 批后监管
    async getApproSuperList() {
      if (this.code != '批后监管') return;
      let opt = {
        areaCode: this.administrationValue,
        riverLevel: this.riverType,
        regulatoryStatus: this.supercisionStatu,
        projectStage: this.projStage,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await waterResourceApi.projectList(opt).then((res) => {
        this.approSuperData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 水域变化 统计
    async cPositionStatisticsWaterArea() {
      if (this.code != '水域变化') return;
      await waterResourceApi.cPositionStatisticsWaterArea().then((res) => {
        this.waterChangeCount = res.data;
      });
    },
    // 水域变化
    async getWaterChangeList() {
      if (this.code != '水域变化') return;
      let opt = {
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : '',
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await waterResourceApi.loadPage(opt).then((res) => {
        this.wtaerChangeData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 清四乱 统计
    async statisProblemC() {
      if (this.code != '清四乱') return;
      await waterResourceApi.statisProblemC().then((res) => {
        this.fourChaosCount = res.data;
      });
    },
    // 清四乱
    async getFourChaosList() {
      if (this.code != '清四乱') return;
      let opt = {
        status: this.rectiRange,
        problemSource: this.reasonFrom,
        riverName: this.qslSearchVal,
        fourType: this.fourChaosType,
        severity: this.severity,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await waterResourceApi.loadPageFourChaos(opt).then((res) => {
        this.fourChaosData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 美丽河湖tab切换
    changeActiveTabBeauty(val) {
      this.activeTabBeauty = val;
      this.projStatu = '';
      this.pageNum = 1;
      this.projStatu = '';
      this.administrationValue = '';
      this.eachProjectStatistics();
      this.getBeautifulRevierList();
    },
    // 工情 报警 tab切换
    changeActiveTabWorks(name) {
      if (this.code == '报警') {
        this.callPoliceValue = name;
      } else if (this.code == '工情') {
        this.workConditionValue = name;
      }
      this.selectVal();
    },
    // 美丽河湖 统计
    async eachProjectStatistics() {
      if (this.code != '美丽河湖') return;
      let opt = {
        type: this.activeTabBeauty
      };
      await waterResourceApi.eachProjectStatistics(opt).then((res) => {
        this.beautyCount = res.data;
      });
    },
    /**
     * 获取美丽河湖数据
     */
    async getBeautifulRevierList() {
      if (this.code != '美丽河湖') return;
      const AsyncFunMap = {
        美丽河湖: waterResourceApi.getBeautifulList,
        中小河流: waterResourceApi.AttSmallMediumRiver,
        水美乡镇: waterResourceApi.AttBeautVillage,
        农村池塘: waterResourceApi.AttRuralPond
      };
      const opt = {
        containWenzhou: '1',
        projectStatus: this.projStatu,
        county: this.administrationValue,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        projectName: this.searchVal
      };
      this.loading = true;

      await AsyncFunMap[this.activeTabBeauty](opt).then((res) => {
        this.beautyData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 水电站 统计
    async mainTotalNum() {
      await waterResourceApi.mainTotalNum().then((res) => {
        this.sdzCount = res.data;
      });
    },
    // 水电站 数据
    async getHydropowerStationList() {
      let opt = {
        areaCode: this.administrationValue,
        hystType: this.sdzType,
        key: this.sdzSearchVal,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await waterResourceApi.hystList(opt).then((res) => {
        this.hydroStaData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },

    // 河长巡查 统计
    async statsByQueryCondition() {
      await waterResourceApi.statsByQueryCondition().then((res) => {
        this.patrolCount = res.data;
      });
    },
    // 河长巡查 数据
    async queryPatrolProblem() {
      let opt = {
        level: this.question,
        areaCode: this.administrationValue,
        startTime: this.patrolDate && this.patrolDate.length > 0 ? this.patrolDate[0] + ' 00:00:00' : '',
        endTime: this.patrolDate && this.patrolDate.length > 0 ? this.patrolDate[1] + ' 00:00:00' : '',
        state: this.dealStatu,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await waterResourceApi.queryPatrolProblem(opt).then((res) => {
        this.patrolData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    }
  }
};
</script>

<style lang="scss">
@import './style/icon.scss';
.detail-table {
  .table-wrapper {
    .el-table__body tr:hover {
      background-color: rgba($color: #56fefe, $alpha: 0.4);
    }
    .warm-row .cell {
      color: #eec80b !important;
    }
  }
}
</style>

<style lang="scss">
.detail-table {
  .gates-open-sty {
    background: url(~assets/images/WaterDisastersImage/zhamen-kaiqi.png) no-repeat center center;
    background-size: 100% 100%;
  }
  .water-gate-sty {
    > .area-style {
      border-left: 1px solid rgba($color: #0bbbff, $alpha: 0.5);
      background: none;
      display: flex;
      justify-content: flex-start;
      > p:nth-child(1) {
        padding: 15px 15px 0 40px;
        > span {
          font-family: PingFangSC-Regular;
          font-size: 16px;
          color: #ffffff;
          font-weight: 400;
          margin-right: 10px;
        }
        > i {
          line-height: 30px;
          margin: 0 10px 0 0;
          color: #fff;
        }
        .el-input__inner {
          background: none;
          border: 0.0625rem solid #fff;
        }
        .el-input-number__decrease {
          border-left: 0.0625rem solid #fff;
          background: none;
          > i {
            color: #fff;
          }
        }
        .el-input-number__increase {
          height: 16px;
          border-left: 0.0625rem solid #fff;
          background: none;
          > i {
            color: #fff;
          }
        }
      }
      > div:nth-child(2) {
        padding: 15px 15px 0 40px;
        font-size: 16px;
        color: #fff;
        > span {
          font-family: PingFangSC-Medium;
          font-size: 24px;
          color: #1cfff5;
          letter-spacing: 0;
          text-align: right;
          line-height: 24px;
          font-weight: 500;
          padding: 0;
          margin: 0;
        }
      }
    }
  }
  .tb-top-panel {
    margin: 5px 0 10px 0;
    color: #fff;
    display: flex;
    align-items: center;
    i {
      float: left;
    }
    p {
      flex: 1;
      font-size: 15px;
      margin-left: 10px;
      background-color: rgba(255, 255, 255, 0.1);
      padding: 8px 10px;
      border-radius: 4px;
    }
    span {
      color: #7ccfff;
      font-size: 20px;
    }
    .laba {
      width: 24px;
      height: 24px;
      background: url(~assets/images/laba.png) no-repeat;
      background-size: 100%;
    }
  }
}
</style>

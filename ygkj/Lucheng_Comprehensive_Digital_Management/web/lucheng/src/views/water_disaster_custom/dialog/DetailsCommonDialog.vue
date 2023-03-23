<template>
  <div class="primary-dialog detail-table">
    <el-dialog
      top="10vh"
      width="1200px"
      destroy-on-close
      v-loading="exportLoading"
      element-loading-text="拼命导出中。。。"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
      :visible.sync="visible"
      :before-close="handleClose"
      :modal-append-to-body="true"
      :close-on-click-modal="false"
      :fullscreen="fullscreen"
    >
      <!-- 弹窗标题 -->
      <div class="primary-dialog__title" slot="title">
        <p class="primary-dialog__title--label">
          {{ code == "水闸" ? "温瑞平水闸" : code }}
        </p>
        <i
          @click="changeFullscreen"
          :class="['el-icon-full-screen', 'full-screen-icon']"
        ></i>
      </div>
      <!-- 内容区域 -->
      <div class="primary-dialog__content">
        <!-- 水库分析 -- 开始 -->
        <div
          class="statistics-sty"
          v-if="mediumSized == '防御复盘' && code == '水库分析'"
        >
          <!-- 累计拦截蓄水量 -->
          <div>
            <p><i class="icon-lan-xushui-liang"></i></p>
            <div>
              <p>累计拦截蓄水量</p>
              <p>
                <span>{{ reservoirAnalysisData.totalStorage }} </span> 万m³
              </p>
            </div>
          </div>
          <!-- 累计泄洪量 -->
          <div>
            <p><i class="icon-shuizha-paihong"></i></p>
            <div>
              <p>累计泄洪量</p>
              <p>
                <span>{{ reservoirAnalysisData.totalDrainage }} </span> 万m³
              </p>
            </div>
          </div>
        </div>
        <!-- 水库分析 -- 结束 -->
        <!-- 水库统计 -->
        <div class="statistics-sty" v-if="code == '水库'">
          <!-- 数量统计 -->
          <div>
            <p><i class="icon-shu-liang-tong-ji"></i></p>
            <div>
              <p>数量统计</p>
              <p>
                <span>{{ statisticsData.total }} </span> 座
              </p>
            </div>
          </div>
          <!-- 超汛限统计 -->
          <div>
            <p><i class="icon-chao-xunxian"></i></p>
            <div>
              <p>
                {{ projectScaleValue == "大中型" ? "超汛限统计" : "溢洪统计" }}
              </p>
              <p>
                <span>{{ statisticsData.overLimitNum }} </span> 座
              </p>
            </div>
          </div>
          <!-- 平均蓄水率 -->
          <div>
            <p><i class="icon-xu-shui-lv"></i></p>
            <div>
              <p>平均蓄水率</p>
              <p>
                <span>{{ statisticsData.avgStorageRatio }} </span> %
              </p>
            </div>
          </div>
          <!-- 至汛限平均纳蓄能力 -->
          <div v-if="projectScaleValue == '大中型'">
            <p><i class="icon-na-xu-nengli"></i></p>
            <div>
              <p>至汛限平均纳蓄能力</p>
              <p>
                <span>{{ statisticsData.avgLimitStorageAbility }} </span> mm
              </p>
            </div>
          </div>
        </div>
        <!-- 水闸统计 -->
        <div class="statistics-sty" v-if="code == '水闸'">
          <!-- 数量统计 -->
          <div>
            <p><i class="icon-shuizha-shuliang-tongji"></i></p>
            <div>
              <p>数量统计</p>
              <p>
                <span> {{ reservoirStatistical.totalNum }} </span> 个
              </p>
            </div>
          </div>
          <!-- 闸门开启数量统计 -->
          <div
            :class="count % 2 ? 'gates-open-sty' : ''"
            style="padding: 10px"
            @click="openGates"
          >
            <p><i class="icon-shuizha-zhamen-kaiqi-shuliang"></i></p>
            <div>
              <p>闸门开启数量</p>
              <p>
                <span>{{ reservoirStatistical.openGateWagaNum }} </span> 个
              </p>
            </div>
          </div>
          <!-- 水闸泄洪量 -->
          <div class="water-gate-sty">
            <div class="area-style">
              <p>
                <i class="el-icon-time icon-sty"></i><span>累计:</span>
                <el-input-number
                  v-model="numTime"
                  size="small"
                  controls-position="right"
                  :min="1"
                  :max="72"
                  @input="changeHours"
                />
                <span style="margin-left: 10px; font-size: 16px">小时</span>
              </p>
              <div>
                泄洪总量
                <span>{{ reservoirStatistical.drainageTotal }} </span> 万m³
              </div>
            </div>
          </div>
        </div>
        <!-- 河网统计 -->
        <div class="statistics-sty" v-if="code == '河网'">
          <!-- 中型水闸数量统计 -->
          <div>
            <p><i class="icon-hewang-shuliang-tongji"></i></p>
            <div>
              <p>数量统计</p>
              <p>
                <span> {{ riverwayList.total }} </span> 个
              </p>
            </div>
          </div>
          <!-- 小型水闸数量统计 -->
          <div>
            <p><i class="icon-chao-xunxian"></i></p>
            <div>
              <p>超警戒统计</p>
              <p>
                <span>{{ riverwayList.warn }} </span> 个
              </p>
            </div>
          </div>
        </div>
        <!-- 潮位统计 -->
        <div class="statistics-sty" v-if="code == '潮位'">
          <!-- 中型水闸数量统计 -->
          <div>
            <p><i class="icon-chaowei-tongji"></i></p>
            <div>
              <p>数量统计</p>
              <p>
                <span>
                  {{
                    seaLevelList.totalNum == null ? "-" : seaLevelList.totalNum
                  }}
                </span>
                个
              </p>
            </div>
          </div>
          <!-- 小型水闸数量统计 -->
          <div>
            <p><i class="icon-chao-xunxian"></i></p>
            <div>
              <p>超警戒统计</p>
              <p>
                <span
                  >{{
                    seaLevelList.overWarnNum == null
                      ? "-"
                      : seaLevelList.overWarnNum
                  }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>
        <!-- 积水统计 -->
        <div class="statistics-sty" v-if="code == '积水'">
          <div>
            <p><i class="icon-jishui-tongji"></i></p>
            <div>
              <p>总数</p>
              <p>
                <span>
                  {{ waterLoggingData.list.length }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-jishui-tongji-active"></i></p>
            <div>
              <p>有积水</p>
              <p>
                <span style="color: #eec80b">
                  {{ waterLoggingData.hasWater }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>
        <!-- 视频监控统计 -->
        <div class="statistics-sty" v-if="code == '视频监控'">
          <div>
            <p><i class="icon-camera-tongji"></i></p>
            <div>
              <p>总数</p>
              <p>
                <span>
                  {{ stats4PageCameraData.total || "-" }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-camera-tongji-online"></i></p>
            <div>
              <p>在线</p>
              <p>
                <span style="color: #19af47">
                  {{ stats4PageCameraData.online || "-" }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-camera-tongji-offline"></i></p>
            <div>
              <p>离线</p>
              <p>
                <span style="color: #bfbfbf">
                  {{ stats4PageCameraData.offline || "-" }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>
        <!-- 防汛仓库统计 -->
        <div class="statistics-sty" v-if="code == '防汛仓库'">
          <div>
            <p><i class="icon-warehouse-tongji"></i></p>
            <div>
              <p>数量统计</p>
              <p>
                <span>
                  {{ warehouseData.list.length }}
                </span>
                个
              </p>
            </div>
          </div>
          <div v-for="(item, key) in warehouseData.stats" :key="key">
            <p>
              <i
                :class="
                  buildingTypeIconMap[item.key]
                    ? buildingTypeIconMap[item.key]
                    : 'icon-warehouse-tongji'
                "
              ></i>
            </p>
            <div>
              <p>{{ item.key }}</p>
              <p>
                <span>
                  {{ item.value }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>

        <!-- 防汛队伍统计 -->
        <div class="statistics-sty" v-if="code == '防汛队伍'">
          <div>
            <p><i class="icon-team-tongji"></i></p>
            <div>
              <p>数量统计</p>
              <p>
                <span>
                  {{ ranksData.list.length }}
                </span>
                个
              </p>
            </div>
          </div>
          <div v-for="(item, key) in ranksData.stats" :key="key">
            <p>
              <i
                :class="
                  buildingTypeIconMap[item.key]
                    ? buildingTypeIconMap[item.key]
                    : 'icon-team-tongji'
                "
              />
            </p>
            <div>
              <p>{{ item.key }}</p>
              <p>
                <span>
                  {{ item.value }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>
        <!-- 规划 -->
        <div class="statistics-sty" v-if="code == '规划'">
          <div v-for="(item, index) in planningData.stats" :key="index">
            <p><i :class="item.icon" /></p>
            <div>
              <p>{{ item.label }}</p>
              <p>
                <span>
                  {{ item.num }}
                </span>
                {{ item.unit }}
              </p>
            </div>
          </div>
        </div>
        <!-- 供水量统计 -->
        <div class="statistics-sty" v-if="code == '供水量'">
          <div>
            <p><i class="icon-Minimum-water"></i></p>
            <div>
              <p>供水量最大年份及对应供水量</p>
              <p v-if="WrbWaterSupplyData.max != {}">
                <span> 
									{{WrbWaterSupplyData.max.year}} 
									<span style="color:#fff;font-size: 15px;">年</span> 
									{{WrbWaterSupplyData.max.totalSupply}} 
								</span>
                亿m³
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-Maxmum-water"></i></p>
            <div>
              <p>供水量最小年份及对应供水量</p>
              <p v-if="WrbWaterSupplyData.min != {}">
                <span> 
									{{WrbWaterSupplyData.min.year}} 
									<span style="color:#fff;font-size: 15px;">年</span>  
									{{WrbWaterSupplyData.min.totalSupply}} 
								</span>
                亿m³
              </p>
            </div>
          </div>
        </div>
        <!-- 供水量统计 -->
        <div class="statistics-sty" v-if="code == '用水量'">
          <div>
            <p><i class="icon-Minimum-water"></i></p>
            <div>
              <p>用水总量</p>
              <p>
                <span> 1000 </span>
                m3
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-Maxmum-water"></i></p>
            <div>
              <p>人均综合用水量</p>
              <p>
                <span> 200 </span>
                m3
              </p>
            </div>
          </div>
        </div>
        <!-- 降水量统计 -->
        <div class="statistics-sty" v-if="code == '降水量'">
          <div>
            <p><i class="icon-precipitation"></i></p>
            <div>
              <p>最大降雨年份和区域</p>
              <p v-if="precipitationVal.max != {}">
                <span> {{precipitationVal.max.adnm}} <span style="color:#fff;"> | </span> {{precipitationVal.max.year}} </span>
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-precipitation"></i></p>
            <div>
              <p>最小降雨年份和区域</p>
              <p v-if="precipitationVal.min != {}">
                <span> {{precipitationVal.min.adnm}} <span style="color:#fff;"> | </span> {{precipitationVal.min.year}} </span>
              </p>
            </div>
          </div>
        </div>
        <!-- 水厂统计 -->
        <div class="statistics-sty" v-if="code == '水厂'">
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-12"></i></p>
            <div>
              <p>水厂总数</p>
              <p>
                <span> {{ waterPointData.totalNum || "-" }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-13"></i></p>
            <div>
              <p>城市水厂</p>
              <p>
                <span> {{ waterPointData["城市水厂"] || "-" }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-22"></i></p>
            <div>
              <p>千吨万人</p>
              <p>
                <span style="color: #008fe8">
                  {{ waterPointData["千吨万人"] || "-" }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-32"></i></p>
            <div>
              <p>200吨~1000吨</p>
              <p>
                <span style="color: #6267ff">
                  {{ waterPointData["两百吨千人以上"] || "-" }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-42"></i></p>
            <div>
              <p>200吨以下</p>
              <p>
                <span style="color: #a000c8">
                  {{ waterPointData["两百吨千人以下"] || "-" }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>
        <!-- 水源地统计 -->
        <div class="statistics-sty" v-if="code == '水源地'">
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-1"></i></p>
            <div>
              <p>水源地总数</p>
              <p>
                <span> {{ wasuObjTypeData[4].num }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-1"></i></p>
            <div>
              <p>县级以上</p>
              <p>
                <span> {{ wasuObjTypeData[0].num }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-2"></i></p>
            <div>
              <p>千吨万人</p>
              <p>
                <span style="color: #008fe8">
                  {{ wasuObjTypeData[1].num }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-3"></i></p>
            <div>
              <p>200吨~1000吨</p>
              <p>
                <span style="color: #6267ff">
                  {{ wasuObjTypeData[2].num }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-4"></i></p>
            <div>
              <p>200吨以下</p>
              <p>
                <span style="color: #a000c8">
                  {{ wasuObjTypeData[3].num }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>
        <!-- 水量预警统计 -->
        <div class="statistics-sty" v-if="code == '水量预警'">
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-1"></i></p>
            <div>
              <p>水源地总数</p>
              <p>
                <span> {{ wasuObjTypeData[4].num }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-5"></i></p>
            <div>
              <p>暂无风险</p>
              <p>
                <span style="color: #19af47">
                  {{ wasuObjTypeData[0].num }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-6"></i></p>
            <div>
              <p>黄色预警</p>
              <p>
                <span style="color: #e89100">
                  {{ wasuObjTypeData[1].num }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-7"></i></p>
            <div>
              <p>橙色预警</p>
              <p>
                <span style="color: #ff551f">
                  {{ wasuObjTypeData[2].num }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-shuiyuandi-8"></i></p>
            <div>
              <p>红色预警</p>
              <p>
                <span style="color: #e80000">
                  {{ wasuObjTypeData[3].num }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>

        <!-- 珊溪水质监测统计 -->
        <div class="statistics-sty" v-if="code == '珊溪水质监测'">
          <div>
            <p><i class="icon-shuiyuandi sx-dialog-count-1"></i></p>
            <div>
              <p>站点总数</p>
              <p>
                <span> 123 </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi sx-dialog-count-2"></i></p>
            <div>
              <p>人工监测</p>
              <p>
                <span> 123 </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi sx-dialog-count-3"></i></p>
            <div>
              <p>自动监测</p>
              <p>
                <span> 123 </span>
                个
              </p>
            </div>
          </div>
        </div>
        <!-- 珊溪巡查统计 -->
        <div class="statistics-sty" v-if="code == '巡查'">
          <div>
            <p><i class="icon-shuiyuandi sx-dialog-count-4"></i></p>
            <div>
              <p>乱点总数</p>
              <p>
                <span> {{ countList.turbTotal || "-" }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi sx-dialog-count-5"></i></p>
            <div>
              <p>已完成</p>
              <p>
                <span style="color: #19af47">
                  {{ countList.completeNum || "-" }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi sx-dialog-count-6"></i></p>
            <div>
              <p>整改中</p>
              <p>
                <span style="color: #e89100">
                  {{ countList.changeNum || "-" }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>

        <!-- 取水户 统计 -->
        <div class="statistics-sty" v-if="code == '取水户'">
          <div>
            <p><i class="icon-shuiyuandi qushuihu-dialog-count-1"></i></p>
            <div>
              <p>取水户总数</p>
              <p>
                <span>
                  {{
                    wtaerIntakeCount.intakeSum &&
                    wtaerIntakeCount.intakeSum.toFixed(0)
                  }}
                </span>
                户
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi qushuihu-dialog-count-2"></i></p>
            <div>
              <p>正常</p>
              <p>
                <span style="color: #19af47">
                  {{ wtaerIntakeCount.intakeNorm }}
                </span>
                户
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi qushuihu-dialog-count-3"></i></p>
            <div>
              <p>接近许可</p>
              <p>
                <span style="color: #008fe8">
                  {{ wtaerIntakeCount.intakeNearPermit }}
                </span>
                户
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi qushuihu-dialog-count-4"></i></p>
            <div>
              <p>超许可</p>
              <p>
                <span style="color: #e89100">
                  {{ wtaerIntakeCount.intakeOverPermit }}
                </span>
                户
              </p>
            </div>
          </div>
        </div>

        <!-- 批后监管 统计 -->
        <div class="statistics-sty" v-if="code == '批后监管'">
          <div>
            <p><i class="icon-shuiyuandi phjg-dialog-count-1"></i></p>
            <div>
              <p>项目总数</p>
              <p>
                <span> {{ approSupeCount.projectNum }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi phjg-dialog-count-2"></i></p>
            <div>
              <p>监管次数</p>
              <p>
                <span style="color: #19af47">
                  {{ approSupeCount.inspectionNum }}
                </span>
                次
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi phjg-dialog-count-3"></i></p>
            <div>
              <p>发现问题数</p>
              <p>
                <span style="color: #eec80b">
                  {{ approSupeCount.findProblem }}
                </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi phjg-dialog-count-4"></i></p>
            <div>
              <p>移交执法大队数</p>
              <p>
                <span style="color: #ef1212">
                  {{ approSupeCount.hiddenDangerNum }}
                </span>
                个
              </p>
            </div>
          </div>
        </div>

        <!-- 水域变化 统计 -->
        <div class="statistics-sty" v-if="code == '水域变化'">
          <div>
            <p><i class="icon-shuiyuandi sybh-dialog-count-1"></i></p>
            <div>
              <p>水域面积</p>
              <p>
                <span>
                  {{ Number(waterChangeCount.waterArea).toFixed(2) || "-" }}
                </span>
                k㎡
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi sybh-dialog-count-2"></i></p>
            <div>
              <p>水面率</p>
              <p>
                <span> {{ waterChangeCount.waterRate }} </span>
                %
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi sybh-dialog-count-3"></i></p>
            <div>
              <p>占用面积</p>
              <p>
                <span> {{ waterChangeCount.occupiedArea }} </span>
                k㎡
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi sybh-dialog-count-4"></i></p>
            <div>
              <p>补偿面积</p>
              <p>
                <span>
                  {{
                    Number(waterChangeCount.compensationArea).toFixed(2) || "-"
                  }}
                </span>
                k㎡
              </p>
            </div>
          </div>
        </div>

        <!-- 清四乱 统计 -->
        <div class="statistics-sty" v-if="code == '清四乱'">
          <div>
            <p><i class="icon-shuiyuandi qsl-dialog-count-1"></i></p>
            <div>
              <p>问题总数</p>
              <p>
                <span> {{ fourChaosCount.total || "0" }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi qsl-dialog-count-1"></i></p>
            <div>
              <p>最多问题地区</p>
              <p>
                <span> {{ fourChaosCount.maxRate || "-" }} </span>
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi qsl-dialog-count-1"></i></p>
            <div>
              <p>最少问题地区</p>
              <p>
                <span> {{ fourChaosCount.minRate || "-" }} </span>
              </p>
            </div>
          </div>
        </div>

        <!-- 美丽河湖 统计 -->
        <div class="beauty-tab-panel" v-if="code == '美丽河湖'">
          <div
            :class="[
              'beauty-tab-item',
              activeTabBeauty == '美丽河湖' ? 'active' : '',
            ]"
            @click="changeActiveTabBeauty('美丽河湖')"
          >
            美丽河湖建设
          </div>
          <div
            :class="[
              'beauty-tab-item',
              activeTabBeauty == '中小河流' ? 'active' : '',
            ]"
            @click="changeActiveTabBeauty('中小河流')"
          >
            中小河流治理
          </div>
          <div
            :class="[
              'beauty-tab-item',
              activeTabBeauty == '水美乡镇' ? 'active' : '',
            ]"
            @click="changeActiveTabBeauty('水美乡镇')"
          >
            水美乡镇建设
          </div>
          <div
            :class="[
              'beauty-tab-item',
              activeTabBeauty == '农村池塘' ? 'active' : '',
            ]"
            @click="changeActiveTabBeauty('农村池塘')"
          >
            农村池塘整治
          </div>
        </div>
        <div class="statistics-sty" v-if="code == '美丽河湖'">
          <div>
            <p>
              <i :class="['icon-shuiyuandi', getIcon]"></i>
            </p>
            <div>
              <p>项目总数</p>
              <p>
                <span> {{ beautyCount.projectTotalNum || "0" }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p>
              <i :class="['icon-shuiyuandi', getIcon]"></i>
            </p>
            <div>
              <p>项目在建</p>
              <p>
                <span> {{ beautyCount.underConstructionNum || "0" }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p>
              <i :class="['icon-shuiyuandi', getIcon]"></i>
            </p>
            <div>
              <p>已完工</p>
              <p>
                <span> {{ beautyCount.completeNum || "0" }} </span>
                个
              </p>
            </div>
          </div>
        </div>

        <!-- 水电站 统计 -->
        <div class="statistics-sty" v-if="code == '水电站'">
          <div>
            <p>
              <i class="icon-shuiyuandi sdz-dialog-count-1"></i>
            </p>
            <div>
              <p>数量统计</p>
              <p>
                <span> {{ sdzCount.totalHyst || "-" }} </span>
                座
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-shuiyuandi sdz-dialog-count-1"></i>
            </p>
            <div>
              <p>最多数量</p>
              <p>
                <span>
                  {{ sdzCount.maxAreaName || "-" }} | {{ sdzCount.maxNum }}座
                  ({{
                    sdzCount.totalHyst
                      ? ((sdzCount.maxNum / sdzCount.totalHyst) * 100).toFixed(
                          2
                        )
                      : "-"
                  }}%)</span
                >
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-shuiyuandi sdz-dialog-count-1"></i>
            </p>
            <div>
              <p>本月生态流量达标率</p>
              <p>
                <span>
                  {{
                    sdzCount.standardArg
                      ? (sdzCount.standardArg * 100).toFixed(2)
                      : "-"
                  }}
                </span>
                %
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-shuiyuandi sdz-dialog-count-1"></i>
            </p>
            <div>
              <p>本月在线率</p>
              <p>
                <span>
                  {{
                    sdzCount.onlineArg
                      ? (sdzCount.onlineArg * 100).toFixed(2)
                      : "-"
                  }}
                </span>
                %
              </p>
            </div>
          </div>
        </div>
        <!-- 海塘统计 统计 -->
        <div class="statistics-sty" v-if="code == '海塘'">
          <div>
            <p>
              <i class="icon-extent-icon" />
            </p>
            <div>
              <p>总条数</p>
              <p>
                <span> {{ seawallData.total || "-" }} </span> 条
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-extent-icon" />
            </p>
            <div>
              <p>100年以上</p>
              <p>
                <span> {{ seawallData.gt100Year || "-" }} </span> 条
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-extent-icon" />
            </p>
            <div>
              <p>100年一遇</p>
              <p>
                <span> {{ seawallData.eq100Year || "-" }} </span> 条
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-extent-icon" />
            </p>
            <div>
              <p>50年一遇</p>
              <p>
                <span> {{ seawallData.eq50Year || "-" }} </span> 条
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-extent-icon" />
            </p>
            <div>
              <p>20年一遇</p>
              <p>
                <span> {{ seawallData.eq20Year || "-" }} </span> 条
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-extent-icon" />
            </p>
            <div>
              <p>10年一遇</p>
              <p>
                <span> {{ seawallData.eq10Year || "-" }} </span> 条
              </p>
            </div>
          </div>
          <div>
            <p>
              <i class="icon-extent-icon" />
            </p>
            <div>
              <p>10年以下</p>
              <p>
                <span> {{ seawallData.lt10Year || "-" }} </span> 条
              </p>
            </div>
          </div>
        </div>

        <!-- 河长巡查 统计 -->
        <div class="statistics-sty" v-if="code == '河长巡查'">
          <div>
            <p><i class="icon-shuiyuandi hzxc-dialog-count-1"></i></p>
            <div>
              <p>问题总数</p>
              <p>
                <span> {{ patrolCount.totalNum }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi hzxc-dialog-count-2"></i></p>
            <div>
              <p>轻微问题</p>
              <p>
                <span> {{ patrolCount.level0Num }} </span>个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi hzxc-dialog-count-3"></i></p>
            <div>
              <p>一般问题</p>
              <p>
                <span> {{ patrolCount.level1Num }} </span>个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi hzxc-dialog-count-4"></i></p>
            <div>
              <p>重大问题</p>
              <p>
                <span> {{ patrolCount.level2Num }} </span>个
              </p>
            </div>
          </div>
        </div>

        <!-- 风险隐患 统计 -->
        <div class="statistics-sty" v-if="code == '风险隐患'">
          <div>
            <p><i class="icon-shuiyuandi icon-risk-total"></i></p>
            <div>
              <p>总数</p>
              <p>
                <span> {{ riskCount.totalNum }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-risk-num"></i></p>
            <div>
              <p>隐患点</p>
              <p>
                <span> {{ riskCount.riskNum }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-highRisk-num"></i></p>
            <div>
              <p>高风险点</p>
              <p>
                <span> {{ riskCount.highRiskNum }} </span>
                个
              </p>
            </div>
          </div>
          <div>
            <p><i class="icon-shuiyuandi icon-weak-num"></i></p>
            <div>
              <p>薄弱点</p>
              <p>
                <span> {{ riskCount.weakNum }} </span>
                个
              </p>
            </div>
          </div>
        </div>

        <!-- 条件查询区域 -->
        <div class="query-sty">
          <!-- 风险隐患查询条件 开始 -->
          <el-row :gutter="20">
            <el-col :span="6" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">行政区划:</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>

            <el-col :span="6" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">工程类型:</p>
                <el-cascader
                  v-model="projectTypeVal"
                  :options="projectTypeList"
                  clearable
                  @change="selectVal"
                />
              </div>
            </el-col>

            <el-col :span="6" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">风险类型:</p>
                <el-select
                  v-model="riskTypeVal"
                  size="small"
                  clearable
                  @change="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in riskTypeList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>

            <el-col
              :span="6"
              v-if="code == '风险隐患'"
              style="padding-right: 0"
            >
              <div class="demo-input-suffix">
                <p style="width: 90px">发现渠道:</p>
                <el-select
                  v-model="discoveryChannel"
                  size="small"
                  clearable
                  @change="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in discoveryChannelList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>
          <el-row style="margin-top: 15px" :gutter="20">
            <el-col :span="6" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">工程名称:</p>
                <el-input
                  placeholder="请输入工程名称"
                  v-model="projectNameVal"
                  class="myClass"
                  style="width: 180px"
                  clearable
                  @change="selectVal"
                />
              </div>
            </el-col>
            <el-col :span="6" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">整改情况:</p>
                <el-select
                  v-model="reorganizeSituation"
                  size="small"
                  clearable
                  @change="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in reorganizeSituationList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6" v-if="code == '风险隐患'">
              <div class="demo-input-suffix">
                <p style="width: 90px">发现时间:</p>
                <el-date-picker
                  v-model="reportTime"
                  format="yyyy-MM-dd"
                  value-format="yyyy-MM-dd"
                  type="date"
                  placeholder="发现时间"
                  clearable
                  @change="selectVal"
                />
                <!-- <el-date-picker
                  v-model="affiliatedUnit"
                  type="daterange"
                  align="right"
                  unlink-panels
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  @change="selectVal"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                > 
                </el-date-picker>-->
              </div>
            </el-col>
            <el-col
              :span="code == '风险隐患' ? 4 : 24"
              v-if="code == '风险隐患' || code == '供水量'"
            >
              <div
                class="derive-sty mg-l16"
                style="width: 65px"
                @click="exportExcel(code)"
              >
                导 出
              </div>
            </el-col>
          </el-row>
          <!-- 风险隐患查询条件 结束 -->
          <el-row :gutter="16">
            <!-- 水利设施保险 -- 开始 -->
            <el-col
              :span="5"
              class="aa"
              v-if="mediumSized == '防御复盘' && code == '水利设施保险'"
            >
              <div class="demo-input-suffix">
                <p style="width: 100px">提防名称：</p>
                <el-input
                  placeholder="请输入"
                  v-model="bewareName"
                  class="myClass"
                  clearable
                  @input="queryVal"
                />
              </div>
              <!-- <div class="demo-input-suffix">
                <p style="width: 90px;margin-right: 9px;">堤防名称:</p>
                <el-input placeholder="请输入" v-model="projectNameVal" class="myClass" clearable @change="bewareName" />
              </div> -->
            </el-col>
            <el-col
              :span="5"
              class="aa"
              v-if="mediumSized == '防御复盘' && code == '水利设施保险'"
            >
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="claimSchedule"
                  size="small"
                  clearable
                  @input="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in claimScheduleList"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '水利设施保险'">
              <div class="demo-input-suffix">
                <p style="width: 100px">所属合同：</p>
                <el-input placeholder="请输入" v-model="belongName" class="myClass" clearable @input="queryVal" />
              </div>
            </el-col> -->
            <!-- <el-col :span="5" class="aa" v-if="mediumSized == '防御复盘' && code == '水利设施保险'">
              <div class="demo-input-suffix">
                <p style="width: 100px">所属单位：</p>
                <el-input placeholder="请输入" v-model="affiliatedUnit" class="myClass" clearable @input="queryVal" />
              </div>
            </el-col> -->
            <!-- 水利设施保险 -- 结束 -->
            <!-- 潮位分析 -- 开始 -->
            <el-col
              :span="5"
              class="aa"
              v-if="mediumSized == '防御复盘' && code == '潮位分析'"
            >
              <div class="demo-input-suffix">
                <p style="width: 100px">潮位名称：</p>
                <el-input
                  placeholder="请输入"
                  v-model="seaLevelName"
                  class="myClass"
                  clearable
                  @input="queryVal"
                />
              </div>
            </el-col>
            <!-- 潮位分析 -- 结束 -->
            <!-- 河网分析 -- 开始 -->
            <el-col
              :span="5"
              class="aa"
              v-if="mediumSized == '防御复盘' && code == '河网分析'"
            >
              <div class="demo-input-suffix">
                <p style="width: 100px">河网名称：</p>
                <el-input
                  placeholder="请输入"
                  v-model="riverName"
                  class="myClass"
                  clearable
                  @input="queryVal"
                />
              </div>
            </el-col>
            <!-- 河网分析 -- 结束 -->
            <!-- 水闸分析 -- 开始 -->
            <el-col
              :span="5"
              class="aa"
              v-if="mediumSized == '防御复盘' && code == '水闸分析'"
            >
              <div class="demo-input-suffix">
                <p style="width: 100px">水闸名称：</p>
                <el-input
                  placeholder="请输入"
                  v-model="sluiceName"
                  class="myClass"
                  clearable
                  @input="queryVal"
                />
              </div>
            </el-col>
            <!-- 水闸分析 -- 结束 -->
            <!-- 水库分析 -- 开始 -->
            <el-col
              :span="5"
              class="aa"
              v-if="mediumSized == '防御复盘' && code == '水库分析'"
            >
              <div class="demo-input-suffix">
                <p style="width: 100px">水库名称：</p>
                <el-input
                  placeholder="请输入"
                  v-model="reservoirName"
                  class="myClass"
                  clearable
                  @input="queryVal"
                />
              </div>
            </el-col>
            <!-- 水库分析 -- 结束 -->

            <!-- 短临预报 -->
            <el-col :span="6" class="aa" v-if="code == '短临预报'">
              <div class="demo-input-suffix">
                <p style="width: 100px">统计类型：</p>
                <el-select
                  v-model="rainfallType"
                  size="small"
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in rainfalStration"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="18" class="aa" v-if="code == '短临预报'">
              <div
                class="derive-sty mg-l16"
                style="width: 65px"
                @click="exportExcel('短临预报')"
              >
                导 出
              </div>
            </el-col>
            <!-- 设计防潮标准年限 -->
            <el-col :span="7" class="aa" v-if="code == '海塘'">
              <div class="demo-input-suffix">
                <p style="width: 140px">设计防潮标准年限：</p>
                <el-select
                  v-model="designTideStandard"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in designTideStandardData"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 行政区划 固定 -->
            <el-col
              :span="6"
              class="aa"
              v-if="!hideAdcdFilterCodes.includes(code)"
            >
              <div class="demo-input-suffix">
                <p style="width: 75px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 流域 -->
            <el-col
              :span="6"
              class="aa"
              v-if="!hideBasinFilterCodes.includes(code)"
            >
              <div class="demo-input-suffix">
                <p style="width: 50px">流域：</p>
                <el-select
                  v-model="administrativeValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administrative"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>

            <!-- 工程规模 -->
            <el-col :span="6" v-if="code == '水库'">
              <div class="demo-input-suffix">
                <p style="width: 100px">工程规模：</p>
                <el-select
                  v-model="projectScaleValue"
                  size="small"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in projectScaleData"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 水闸工程规模 -->
            <el-col :span="6" v-if="code == '水闸'">
              <div class="demo-input-suffix">
                <p style="width: 100px">工程规模：</p>
                <el-select
                  v-model="engScalesValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in engScalesData"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 海塘 -->
            <el-col :span="5" v-if="code == '海塘'">
              <div class="demo-input-suffix">
                <p style="width: 100px">工程等别：</p>
                <el-select
                  v-model="engineeringValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in EngineeringData"
                    :key="item.grade"
                    :label="item.grade"
                    :value="item.grade"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 工程类型 -->
            <el-col :span="6" v-if="code == '病险工程' || code == '视频监控'">
              <div class="demo-input-suffix">
                <p style="width: 75px">工程类型：</p>
                <el-select
                  v-model="projType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in projTypeData"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- 视频监控 -->
            <el-col :span="6" v-if="code == '视频监控'">
              <div class="mg-l16 demo-input-suffix">
                <p style="width: 75px">视频状态:</p>
                <el-select
                  v-model="statusType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option label="在线" :value="1" />
                  <el-option label="离线" :value="0" />
                </el-select>
              </div>
            </el-col>

            <!-- 仓库类型 -->
            <!-- <el-col :span="6" v-if="code == '防汛仓库'">
							<div class="demo-input-suffix">
								<p style="width: 100px">仓库类型：</p>
								<el-select v-model="warehouseType" size="small" clearable @input="selectVal" placeholder="全部">
									<el-option v-for="item in warehouseOptions" :key="item.id" :label="item.dictName" :value="item.id" />
								</el-select>
							</div>
						</el-col> -->
            <!-- <el-col :span="5" class="aa mg-l16 fr" v-if="code == '防汛仓库'">
              <div class="demo-input-suffix">
                <el-input
                  placeholder="名称"
                  v-model="NameVal"
                  class="myClass"
                  clearable
                  @blur="queryVal"
                />
                <div
                  class="derive-sty mg-l16"
                  style="width: 65px"
                  @click="queryVal"
                >
                  搜 索
                </div>
              </div>
            </el-col> -->
            <!-- 队伍类型 -->
            <!-- <el-col :span="6" v-if="code == '防汛队伍'">
							<div class="demo-input-suffix">
								<p style="width: 100px">队伍类型：</p>
								<el-select v-model="ranksType" size="small" clearable placeholder="全部" @input="selectVal">
									<el-option v-for="item in ranksOptions" :key="item.id" :label="item.dictName" :value="item.id" />
								</el-select>
							</div>
						</el-col> -->
            <!-- <el-col :span="5" class="aa mg-l16 fr" v-if="code == '防汛队伍'">
              <div class="demo-input-suffix">
                <el-input
                  placeholder="名称"
                  v-model="NameVal"
                  class="myClass"
                  clearable
                  @blur="queryVal"
                />
                <div
                  class="derive-sty mg-l16"
                  style="width: 65px"
                  @click="queryVal"
                >
                  搜 索
                </div>
              </div>
            </el-col> -->
            <!-- 是否积水 -->
            <el-col style="margin-right: 16px" :span="6" v-if="code == '积水'">
              <div class="demo-input-suffix">
                <p style="width: 100px">是否积水：</p>
                <el-select
                  v-model="waterLoggingType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in waterLoggingOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <!-- <el-col :span="5" class="aa fr" v-if="code == '积水'">
              <div class="demo-input-suffix">
                <el-input
                  placeholder="名称"
                  v-model="NameVal"
                  class="myClass"
                  clearable
                  @blur="queryVal"
                />
                <div
                  class="derive-sty"
                  style="margin-left: 10px; width: 65px"
                  @click="queryVal"
                >
                  搜 索
                </div>
              </div>
            </el-col> -->
            <!-- 洪水风险图 -->
            <!-- 海塘 -->
            <!-- <el-col :span="6" v-if="code == '洪水风险图'">
							<div class="demo-input-suffix">
								<p style="width: 150px">设计洪水标准：</p>
								<el-select v-model="floodLevel" size="small" clearable placeholder="全部" @input="selectVal">
									<el-option
										v-for="item in floodLevelOptions"
										:key="item.value"
										:label="item.label"
										:value="item.value"
									/>
								</el-select>
							</div>
						</el-col> -->

            <!-- 仓库类型 -->
            <el-col :span="6" v-if="code == '防汛仓库'">
              <div class="demo-input-suffix">
                <p style="width: 100px">仓库类型：</p>
                <el-select
                  v-model="warehouseType"
                  size="small"
                  clearable
                  @input="selectVal"
                  placeholder="全部"
                >
                  <el-option
                    v-for="item in warehouseOptions"
                    :key="item.id"
                    :label="item.dictName"
                    :value="item.id"
                  />
                </el-select>
              </div>
            </el-col>

            <!-- 队伍类型 -->
            <el-col :span="6" v-if="code == '防汛队伍'">
              <div class="demo-input-suffix">
                <p style="width: 100px">队伍类型：</p>
                <el-select
                  v-model="ranksType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in ranksOptions"
                    :key="item.id"
                    :label="item.dictName"
                    :value="item.id"
                  />
                </el-select>
              </div>
            </el-col>

            <!-- 是否积水 -->
            <!-- <el-col style="margin-right: 16px" :span="6" v-if="code == '积水'">
							<div class="demo-input-suffix">
								<p style="width: 100px">是否积水：</p>
								<el-select v-model="waterLoggingType" size="small" clearable placeholder="全部" @input="selectVal">
									<el-option
										v-for="item in waterLoggingOptions"
										:key="item.value"
										:label="item.label"
										:value="item.value"
									/>
								</el-select>
							</div>
						</el-col> -->
            <!-- 洪水风险图 -->
            <!-- 海塘 -->
            <el-col :span="24" v-if="code == '洪水风险图'">
              <div class="demo-input-suffix">
                <p style="width: 100px">设计洪水标准：</p>
                <el-select
                  v-model="floodLevel"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in floodLevelOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
                <div
                  class="derive-sty mg-l16"
                  style="width: 65px"
                  @click="exportExcel('洪水风险图')"
                >
                  导 出
                </div>
              </div>
            </el-col>

            <el-col v-if="code == '视频监控'" :span="6" class="aa fr">
              <div class="demo-input-suffix">
                <p style="width: 50px">名称:</p>
                <el-input
                  placeholder="请输入摄像头名称"
                  v-model="NameVal"
                  class="myClass"
                  clearable
                  style="width: 146px"
                  @blur="queryVal"
                />
                <div
                  class="derive-sty mg-l16"
                  style="width: 65px"
                  @click="exportExcel('视频监控')"
                >
                  导 出
                </div>
                <!-- <div class="derive-sty mg-l16" style="width: 65px" @click="queryVal">
									搜 索
								</div> -->
              </div>
            </el-col>

            <!-- 流域生态流量 -->

            <el-col :span="6" v-if="code == '流域生态流量'">
              <div class="demo-input-suffix">
                <p style="width: 60px">预警：</p>
                <el-select
                  v-model="checkYear"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option label="橙色预警" value="1" />
                  <el-option label="红色预警" value="2" />
                </el-select>
              </div>
            </el-col>
            <el-col :span="10" class="aa" v-if="code == '规划' || code == '降水量'">
              <div class="demo-input-suffix">
                <p style="width: 90px">起止年份：</p>
                <el-date-picker
                  v-model="planStart"
                  format="yyyy"
                  value-format="yyyy"
                  type="year"
                  placeholder="开始年份"
                  clearable
                  :picker-options="{
                    disabledDate: (time) =>
                      planEnd ? time.getFullYear() > planEnd : null,
                  }"
                  @change="selectVal"
                />
                <span style="padding: 0 10px;"> 至 </span>
                <el-date-picker
                  v-model="planEnd"
                  format="yyyy"
                  value-format="yyyy"
                  type="year"
                  placeholder="结束年份"
                  clearable
                  :picker-options="{
                    disabledDate: (time) =>
                      planStart ? time.getFullYear() < planStart : null,
                  }"
                  @change="selectVal"
                />
              </div>
            </el-col>
            <!-- 关键字搜索 -->
            <el-col :span="6" v-if="showSerachCodes.includes(code)">
              <div class="demo-input-suffix">
                <p style="width: 60px">关键字：</p>
                <el-input
                  style="width: 150px"
                  v-model="searchVal"
                  :placeholder="calcInputPlaceholder()"
                  @input="selectVal"
                />
                <div
                  v-if="
                    code == '海塘' ||
                    code == '积水' ||
                    code == '水闸' ||
                    code == '水库' ||
                    code == '防汛仓库' ||
                    code == '防汛队伍'
                  "
                  class="derive-sty mg-l16"
                  style="width: 65px"
                  @click="exportExcel(code)"
                >
                  导 出
                </div>
              </div>
            </el-col>
            <el-col :span="code == '降水量' ? 14 : 6" v-if="code == '河网' || code == '降水量'">
              <div
                class="derive-sty mg-l16"
                style="width: 65px"
                @click="exportExcel(code)"
              >
                导 出
              </div>
              <!-- <div
                v-if="code != '降水量'"
                class="derive-sty mg-l16"
                style="width: 80px"
                @click="searchVal"
              >
                查 询
              </div> -->
            </el-col>
          </el-row>

          <!-- <el-row v-if="code == '视频监控'">
						<el-col :span="12">
							<div class="demo-input-suffix" style="margin-bottom:0;">
								<p style="width: 100px">时间段查询:</p>
								<el-date-picker
									v-model="value1"
									type="daterange"
									:clearable="false"
									format="yyyy-MM-dd"
									value-format="yyyy-MM-dd"
									range-separator="至"
									:picker-options="pickerOptions"
									start-placeholder="开始日期"
									end-placeholder="结束日期"
									@change="dateChange"
								/>
							</div>
						</el-col>
						<el-col :span="12" class="aa fr">
							<div class="derive-sty mg-l16" style="width: 65px" @click="exportExcel('视频监控')">
								导 出
							</div>
						</el-col>
					</el-row> -->

          <!-- 水资源承载能力 -->
          <el-row v-if="code == '水资源承载能力'">
            <el-col :span="24" class="water-supply-select-panel">
              <div
                :class="[
                  activeTabCarry == '水量评价' ? 'active' : '',
                  'water-supply-select-btn',
                ]"
                @click="changeActiveTabCarry('水量评价')"
              >
                水量评价
              </div>
              <div
                :class="[
                  activeTabCarry == '水质评价' ? 'active' : '',
                  'water-supply-select-btn',
                ]"
                @click="changeActiveTabCarry('水质评价')"
              >
                水质评价
              </div>
              <div
                :class="[
                  activeTabCarry == '经济评价' ? 'active' : '',
                  'water-supply-select-btn',
                ]"
                @click="changeActiveTabCarry('经济评价')"
              >
                经济评价
              </div>
              <div
                :class="[
                  activeTabCarry == '人口评价' ? 'active' : '',
                  'water-supply-select-btn',
                ]"
                @click="changeActiveTabCarry('人口评价')"
              >
                人口评价
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col
              :span="5"
              v-if="
                code == '水资源量' ||
                code == '用水量' ||
                (code == '水资源承载能力' && activeTabCarry != '用水评价')
              "
            >
              <div class="demo-input-suffix">
                <p style="width: 60px">年份：</p>
                <el-select
                  v-model="checkYear"
                  size="small"
                  @change="selectVal"
                  :clearable="false"
                >
                  <el-option
                    v-for="item in yearOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="19" class="aa" v-if="code == '用水量'">
              <div
                class="derive-sty mg-l16"
                style="width: 65px"
                @click="exportExcel(code)"
              >
                导 出
              </div>
            </el-col>
          </el-row>
          <!-- 降水量，水资源量，供水量，用水量 结束 -->

          <!-- 预警信息 筛选项 -->
          <el-row :gutter="20" v-if="code == '预警信息'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">预警类型：</p>
                <el-select
                  v-model="earlyType"
                  size="small"
                  clearable
                  @change="getEarlyDetail"
                >
                  <el-option
                    v-for="item in earlyTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="demo-input-suffix">
                <p style="width: 80px">关键字：</p>
                <el-input
                  v-model="earlySearchVal"
                  placeholder="请输入内容"
                  @input="getEarlyDetail"
                  style="width: 180px"
                ></el-input>
                <div
                  class="derive-sty mg-l16"
                  style="width: 80px"
                  @click="getEarlyDetail"
                >
                  查 询
                </div>
              </div>
            </el-col>
          </el-row>
          <div class="tb-top-panel" v-if="code == '预警信息'">
            <i class="icon-liebiao icon-sty laba" />
            <p>
              预警消息总数：<span>{{ totalPage }}</span> 次
            </p>
          </div>

          <!-- 水源地 筛选项 -->
          <el-row :gutter="20" v-if="code == '水源地'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getWaterSourList"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 50px">类型：</p>
                <el-select
                  v-model="waterSourType"
                  size="small"
                  clearable
                  @change="getWaterSourList"
                >
                  <el-option
                    v-for="item in waterSourTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 50px">规模：</p>
                <el-select
                  v-model="waterSourScale"
                  size="small"
                  clearable
                  @change="getWaterSourList"
                >
                  <el-option
                    v-for="item in waterSourScaleOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">关键字：</p>
                <el-input
                  v-model="waterSourSearchVal"
                  placeholder="请输入内容"
                  @input="getWaterSourList"
                ></el-input>
              </div>
            </el-col>
          </el-row>
          <!-- 水厂 水厂运行状态 筛选项 -->
          <el-row :gutter="20" v-if="code == '水厂' || code == '水厂运行状态'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="adcdData"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="AttSwhsBaseLoadPage"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6" v-if="code == '水厂'">
              <div class="demo-input-suffix">
                <p style="width: 50px">类型：</p>
                <el-select
                  v-model="engType"
                  size="small"
                  clearable
                  @change="AttSwhsBaseLoadPage"
                >
                  <el-option
                    v-for="item in waterWorksList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6" v-if="code == '水厂'">
              <div class="demo-input-suffix">
                <p style="width: 50px">规模：</p>
                <el-select
                  v-model="wasuRang"
                  size="small"
                  clearable
                  @change="AttSwhsBaseLoadPage"
                >
                  <el-option
                    v-for="item in waterWorksScaleList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6" v-if="code == '水厂运行状态'">
              <div class="demo-input-suffix">
                <p style="width: 50px">运行状态：</p>
                <el-select
                  v-model="runtimeState"
                  size="small"
                  clearable
                  @change="AttSwhsBaseLoadPage"
                >
                  <el-option
                    v-for="item in waterWorksStatusList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">关键字：</p>
                <el-input
                  v-model="cwsName"
                  placeholder="请输入内容"
                  @input="AttSwhsBaseLoadPage"
                ></el-input>
              </div>
            </el-col>
          </el-row>

          <!-- 水量预警 筛选项 -->
          <el-row :gutter="20" v-if="code == '水量预警'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getWaterWarnList"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 50px">类型：</p>
                <el-select
                  v-model="waterSourType"
                  size="small"
                  clearable
                  @change="getWaterWarnList"
                >
                  <el-option
                    v-for="item in waterSourTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 120px">水量预警等级：</p>
                <el-select
                  v-model="waterSourLevel"
                  size="small"
                  clearable
                  @change="getWaterSourList"
                >
                  <el-option
                    v-for="item in waterSourLevelOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">关键字：</p>
                <el-input
                  v-model="waterSourSearchVal"
                  placeholder="请输入内容"
                  @input="getWaterWarnList"
                ></el-input>
              </div>
            </el-col>
          </el-row>

          <!-- 珊溪水质监测 筛选项 -->
          <el-row :gutter="20" v-if="code == '珊溪水质监测'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">站点类型：</p>
                <el-select
                  v-model="stationType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getSXWaterList"
                >
                  <el-option
                    v-for="item in stationTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">关键字：</p>
                <el-input
                  v-model="shanXiWaterSearchVal"
                  placeholder="请输入内容"
                  @input="getSXWaterList"
                ></el-input>
              </div>
            </el-col>
          </el-row>

          <!-- 珊溪巡查 筛选项 -->
          <el-row :gutter="20" v-if="code == '巡查'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">违法类型：</p>
                <el-select
                  v-model="chaosType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="happeningListJson"
                >
                  <el-option
                    v-for="item in chaosTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">乱点状态：</p>
                <el-select
                  v-model="chaosStatus"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="happeningListJson"
                >
                  <el-option
                    v-for="item in chaosStatusOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">乱点名称：</p>
                <el-input
                  v-model="chaosName"
                  placeholder="请输入内容"
                  @input="happeningListJson"
                ></el-input>
              </div>
            </el-col>
          </el-row>

          <!-- 取水户 筛选项 -->
          <el-row :gutter="20" v-if="code == '取水户'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix" style="line-height: 32px">
                <p style="width: 60px">报警：</p>
                <el-radio-group
                  v-model="wtaerIntakeWarnType"
                  @change="getWaterIntakeList"
                >
                  <el-radio class="el-radio" :label="true">是</el-radio>
                  <el-radio class="el-radio" :label="false">否</el-radio>
                </el-radio-group>
                <!-- <el-select
                  v-model="wtaerIntakeWarnType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getWaterIntakeList"
                >
                  <el-option
                    v-for="item in wtaerIntakeWarnTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select> -->
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">取水类型：</p>
                <el-select
                  v-model="wtaerIntakeType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getWaterIntakeList"
                >
                  <el-option
                    v-for="item in wtaerIntakeTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>

          <!-- 批后监管 筛选项 -->
          <el-row :gutter="20" v-if="code == '批后监管'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">河道等级：</p>
                <el-select
                  v-model="riverType"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getApproSuperList"
                >
                  <el-option
                    v-for="item in riverTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">监管状态：</p>
                <el-select
                  v-model="supercisionStatu"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getApproSuperList"
                >
                  <el-option
                    v-for="item in supercisionStatuOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">项目阶段：</p>
                <el-select
                  v-model="projStage"
                  size="small"
                  clearable
                  placeholder="全部"
                  @change="getApproSuperList"
                >
                  <el-option
                    v-for="item in projStageOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>

          <!-- 水域变化 筛选项 -->
          <el-row :gutter="20" v-if="code == '水域变化'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 60px">年份：</p>
                <el-select
                  v-model="checkYear"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in yearOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>

          <!-- 清四乱 筛选项 -->
          <el-row :gutter="20" v-if="code == '清四乱'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">问题状态：</p>
                <el-select
                  v-model="rectiRange"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in rectiRangeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">问题来源：</p>
                <el-select
                  v-model="reasonFrom"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in reasonFromOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">四乱类型：</p>
                <el-select
                  v-model="fourChaosType"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in fourChaosTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">严重程度：</p>
                <el-select
                  v-model="severity"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in severityOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="8" style="margin-top: 10px">
              <div class="demo-input-suffix">
                <p style="width: 100px">关键字：</p>
                <el-input
                  v-model="qslSearchVal"
                  placeholder="请输入关键字"
                  @input="getFourChaosList"
                  style="width: 176px"
                ></el-input>
                <div
                  class="derive-sty mg-l16"
                  style="width: 80px"
                  @click="getFourChaosList"
                >
                  查 询
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 美丽河湖 筛选项 -->
          <el-row :gutter="20" v-if="code == '美丽河湖'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adnm"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">工程状态：</p>
                <el-select
                  v-model="projStatu"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in projStatuOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">关键字：</p>
                <el-input
                  v-model="searchVal"
                  style="width: 180px"
                  placeholder="请输入内容"
                  @input="selectVal"
                ></el-input>
                <div
                  class="derive-sty mg-l16"
                  style="width: 80px"
                  @click="searchVal"
                >
                  查 询
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 水电站 筛选项 -->
          <el-row :gutter="20" v-if="code == '水电站'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 100px">水电站类型：</p>
                <el-select
                  v-model="sdzType"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in sdzTypeOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">关键字：</p>
                <el-input
                  style="width: 180px"
                  v-model="sdzSearchVal"
                  placeholder="请输入内容"
                  @input="getHydropowerStationList"
                ></el-input>
                <div
                  class="derive-sty mg-l16"
                  style="width: 80px"
                  @click="getHydropowerStationList"
                >
                  查 询
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- 河长巡查 筛选项 -->
          <el-row :gutter="20" v-if="code == '河长巡查'">
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">时间：</p>
                <el-date-picker
                  v-model="patrolDate"
                  type="daterange"
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  @change="selectVal"
                />
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">行政区划：</p>
                <el-select
                  v-model="administrationValue"
                  size="small"
                  clearable
                  placeholder="全部"
                  @input="selectVal"
                >
                  <el-option
                    v-for="item in administration"
                    :key="item.adcd"
                    :label="item.adnm"
                    :value="item.adcd"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">处置状态：</p>
                <el-select
                  v-model="dealStatu"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in dealStatuOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="demo-input-suffix">
                <p style="width: 80px">问题筛选：</p>
                <el-select
                  v-model="question"
                  size="small"
                  clearable
                  @change="selectVal"
                >
                  <el-option
                    v-for="item in questionOption"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- tab展示区域 -->
        <!-- 海塘详情列表 -->
        <div class="tab-list-sty" v-if="code == '海塘'">
          <el-table
            ref="multipleTable"
            :data="seawallProjData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{
                  pageNum == 1
                    ? scope.$index + 1
                    : (pageNum - 1) * pageSize + scope.$index + 1
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="seawallName"
              label="海塘名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="seawallLevel"
              label="海塘级别"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="village"
              label="所在乡镇"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="seawallLength"
              width="140px;"
              label="海塘长度(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="designTideStandard"
              label="设计防潮标准(年)"
            />
          </el-table>
        </div>
        <!-- 水库 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水库'">
          <el-table
            ref="multipleTable"
            :data="WaterloggingData"
            height="100%"
            stripe
            class="table-wrapper"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :row-class-name="rowClassName"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="resName"
              width="100"
              :sortable="'custom'"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              width="70"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              width="80"
              label="行政区划"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="waterLevel"
              width="100"
              label="水位(m)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="currentLimitLevel"
              :sortable="'custom'"
              width="130"
              label="汛限水位(m)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="uppLevFlco"
              width="150px;"
              label="防洪高水位(m)"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              prop="dataTime"
              width="100"
              label="更新时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :show-overflow-tooltip="true"
              prop="safetyTime"
              width="110"
              label="鉴定时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="safetyConclusion"
              width="110"
              label="鉴定结论"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="reinforce"
              width="110"
              label="有无加固"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="normalSpillwayCrestElevation"
              label="溢洪道高程(m)"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="damSizeHig"
              label="坝高(m)"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="waterLevelCapacity"
              label="实时库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="totCap"
              label="总库容(万m³)"
            />
            <el-table-column
              v-if="
                projectScaleValue == '大中型' && mediumSized == '大中型水库'
              "
              :formatter="toFixedFormatter"
              prop="drainage24H"
              width="150px;"
              label="24累计出库量(万m³)"
            />
            <el-table-column
              v-if="
                projectScaleValue == '大中型' && mediumSized == '大中型水库'
              "
              :formatter="toFixedFormatter"
              prop="drainage48H"
              width="150px;"
              label="48累计出库量(万m³)"
            />
            <el-table-column
              v-if="
                projectScaleValue == '大中型' && mediumSized == '大中型水库'
              "
              :formatter="toFixedFormatter"
              prop="drainage72H"
              width="150px;"
              label="72累计出库量(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="storageRatio"
              label="蓄水率(%)"
            />
            <el-table-column
              v-if="projectScaleValue != '大中型'"
              :formatter="toFixedFormatter"
              prop="normWatLev"
              width="175px"
              label="正常蓄水位(m)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="waterLevelCapacity"
              width="160px"
              label="实时库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="limitLevelCapacity"
              width="160px"
              label="汛限水位库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="uppLevFlcoCapacity"
              width="170px"
              label="防洪高水位库容(万m³)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="limitStorageAbility"
              width="160px"
              label="至汛限纳蓄能力(mm)"
            />
            <el-table-column
              v-if="projectScaleValue == '大中型'"
              :formatter="toFixedFormatter"
              prop="floodStorageAbility"
              width="175px"
              label="至防洪高纳蓄能力(mm)"
            />
          </el-table>
        </div>
        <!-- 水闸 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水闸'">
          <el-table
            ref="multipleTable"
            :data="waterGateData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              prop="wagaName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              label="流域"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              width="120"
              prop="areaName"
              label="行政区划"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="engScal"
              width="100"
              label="工程规模"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              width="130"
              label="闸上水位(m)"
            >
              <template #default="{ row }">
                {{ !row.upz ? "-" : row.upz > 10 ? "故障" : row.upz }}
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              width="130"
              label="闸下水位(m)"
            >
              <template #default="{ row }">
                {{ !row.dwz ? "-" : row.dwz > 10 ? "故障" : row.upz }}
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="ovs"
              width="110"
              label="闸门开度"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              width="175"
              prop="mxgtq"
              label="最大过闸流量(㎡/s)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              width="175"
              prop="ll"
              label="实际过闸流量(㎡/s)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              width="200"
              prop="drainage"
              :label="'累计' + numTime + '小时泄洪量(万m³)'"
            />
          </el-table>
        </div>
        <!-- 河网 tab列表 -->
        <div class="tab-list-sty" v-if="code == '河网'">
          <el-table
            ref="multipleTable"
            :data="riverwayData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="rnName"
              width="80"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              width="80"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              width="100"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="waterLevel"
              label="水位（m）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="mainStName"
              label="水位站名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedTwoFormatter"
              :sortable="'custom'"
              prop="warningLevel"
              label="警戒水位（m）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedTwoFormatter"
              :sortable="'custom'"
              prop="overWarn"
              label="超警戒水位（m）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedTwoFormatter"
              :sortable="'custom'"
              prop="guranteeLevel"
              label="保证水位（m）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedTwoFormatter"
              :sortable="'custom'"
              prop="overGuarantee"
              label="超保证水位（m）"
            />
          </el-table>
        </div>
        <!-- 潮位 tab列表 -->
        <div class="tab-list-sty" v-if="code == '潮位'">
          <el-table
            ref="multipleTable"
            :data="seaLevelData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="stName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              width="80"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              width="80"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="tdz"
              width="150"
              label="实时潮位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="warningLevel"
              label="警戒水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="overWarn"
              label="超警戒潮位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="astroTide"
              label="天文潮(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="subTide"
              label="增水(m)"
            />
          </el-table>
        </div>
        <!-- 病险工程 tab列表 -->
        <div class="tab-list-sty" v-if="code == '病险工程'">
          <el-table
            ref="multipleTable"
            :data="dangerouslyData"
            height="100%"
            stripe
            class="table-wrapper"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              prop="projectName"
              width="100"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              label="流域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="engScal"
              label="工程规模"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projLocName"
              label="所在乡镇"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastCheckTime"
              label="鉴定时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastCheckResult"
              label="安全鉴定结论"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="rectifyFinishTime"
              label="整改完成时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              v-if="projType == '水库'"
              prop="damType"
              width="100"
              label="坝型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              v-if="projType == '水库'"
              :formatter="toFixedFormatter"
              prop="totCap"
              width="120"
              label="总库容(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              v-if="projType == '水库'"
              :formatter="toFixedFormatter"
              prop="waterLevel"
              width="120"
              label="实时水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              v-if="projType == '水库'"
              :formatter="toFixedFormatter"
              prop="limitLevel"
              width="120"
              label="汛限水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              v-if="projType == '水库'"
              :formatter="toFixedFormatter"
              prop="floodLevel"
              width="140"
              label="防洪高水位(m)"
            />
          </el-table>
        </div>
        <!-- 风险隐患 tab列表 -->
        <div class="tab-list-sty" v-if="code == '风险隐患'">
          <el-table
            ref="multipleTable"
            :data="potentialRisksData"
            height="100%"
            class="table-wrapper"
            stripe
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{
                  pageNum == 1
                    ? scope.$index + 1
                    : (pageNum - 1) * pageSize + scope.$index + 1
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projType"
              label="工程类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="recType"
              label="风险类型"
            >
              <template slot-scope="scope">
                <span>
                  {{
                    scope.row.sbSign == "251"
                      ? "隐患点"
                      : scope.row.sbSign == "252"
                      ? "高风险点"
                      : scope.row.sbSign == "253"
                      ? "薄弱点"
                      : "-"
                  }}</span
                >
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="dangerType"
              label="隐患类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="recCondition"
              label="整改情况"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="county"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="reportPerson"
              label="上报人员"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="dutyUnit"
              label="整改责任单位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="reportTime"
              label="发现时间"
            />
            <!-- <el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="rectifyFinishTime"
							label="整改完成时间"
						/> -->
          </el-table>
        </div>
        <!-- 山洪 tab列表 -->
        <div class="tab-list-sty" v-if="code == '山洪'">
          <el-table
            ref="multipleTable"
            :data="dangerouslyData"
            height="100%"
            stripe
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectType"
              label="乡镇"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="可能性很大的村落"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="waterLevel"
              label="可能性较大的村落"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="warningLevel"
              label="影响人口"
            />
          </el-table>
        </div>
        <!-- 短临预报 tab列表 -->
        <div class="tab-list-sty" v-if="code == '短临预报'">
          <el-table
            ref="multipleTable"
            :data="inTheShortData"
            height="100%"
            stripe
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="key"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="rainfall_1"
              label="1小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="rainfall_3"
              label="3小时(mm)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="rainfall_6"
              label="6小时(mm)"
            />
          </el-table>
        </div>
        <!-- 仓库 tab列表 -->
        <div class="tab-list-sty" v-if="code == '防汛仓库'">
          <el-table
            ref="multipleTable"
            :data="warehouseData.list"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="name"
              label="物资仓库名"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="行政区划"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="type"
              label="仓库类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="address"
              label="地址"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="materialPrice"
              label="物资价值(万元)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="chargePerson"
              label="负责人"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="chargePhone"
              label="联系电话"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="acreage"
              sortable
              label="仓库面积(km²)"
            />
          </el-table>
        </div>
        <!-- 队伍 tab列表 -->
        <div class="tab-list-sty" v-if="code == '防汛队伍'">
          <el-table
            ref="multipleTable"
            :data="ranksData.list"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="name"
              label="抢险队伍名称"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="行政区划"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="typeName"
              label="队伍类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="address"
              label="地址"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="chargePerson"
              label="负责人"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="chargePhone"
              label="联系电话"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="existingNumber"
              label="人数(万人)"
              sortable
            />
          </el-table>
        </div>
        <!-- 视频监控 tab列表 -->
        <div class="tab-list-sty" v-if="code == '视频监控'">
          <el-table
            ref="multipleTable"
            :data="videoData"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{
                  pageNum == 1
                    ? scope.$index + 1
                    : (pageNum - 1) * pageSize + scope.$index + 1
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="cameraName"
              label="摄像头名称"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectName"
              label="所属工程"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectType"
              label="工程类型"
              sortable
            />
            <!-- <el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="onlineTime"
							label="在线时长(h)"
						/>
						<el-table-column
							:show-overflow-tooltip="true"
							:formatter="toFixedFormatter"
							prop="offlineTime"
							label="离线时长(h)"
						/> -->
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="status"
              label="视频状态"
              sortable
            >
              <template slot-scope="scope">
                <span>
                  {{
                    scope.row.status == 0
                      ? "离线"
                      : scope.row.status == 1
                      ? "在线"
                      : "-"
                  }}</span
                >
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="statusDuration"
              label="持续时长(h)"
            />
          </el-table>
          <!-- 分页 -->
          <el-pagination
            background
            class="pagination"
            :page-size="pageSize"
            :pager-count="5"
            layout="total, prev, pager, next"
            :total="totalPage"
            @current-change="pageChange"
          />
        </div>
        <!-- 积水 tab列表 -->
        <div class="tab-list-sty" v-if="code == '积水'">
          <el-table
            ref="multipleTable"
            :data="waterLoggingData.list"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="name"
              label="积水点名称"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="行政区划"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="streetName"
              label="地址"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="ponding"
              label="是否积水"
            >
              <template slot-scope="scope">
                <span> {{ scope.row.ponding ? "积水" : "无积水" }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="waterLevel"
              label="积水水位(cm)"
              sortable
            >
              <template slot-scope="scope">
                <span> {{ scope.row.waterLevel }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestLevel"
              label="历史最高积水位(cm)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="appearTime"
              label="最高积水位发生时间"
            />
          </el-table>
        </div>
        <!-- 洪水风险图 tab列表 -->
        <div class="tab-list-sty" v-if="code == '洪水风险图'">
          <el-table
            ref="multipleTable"
            :data="floodRiskData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="name"
              sortable
              label="流域名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="designStandard"
              label="设计洪水标准"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="floodedArea"
              label="淹没面积(km²)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="submergedPopulation"
              label="淹没人口(万人)"
              sortable
            ></el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="floodLoss"
              label="淹没区洪水损失(万元)"
              sortable
            >
            </el-table-column>
          </el-table>
        </div>
        <!-- 降水量 tab列表 -->
        <div class="tab-list-sty" v-if="code == '降水量'">
          <el-table
            ref="multipleTable"
            :data="precipitationData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="行政区域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="year"
              width="80"
              label="年份"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="rainfall"
              label="年降雨量（mm）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="yearAvgRain"
              label="多年年均降雨（mm）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="compareLastYear"
              label="较上一年(%)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="anomaly"
              label="较多年(%)"
            />
            <el-table-column :show-overflow-tooltip="true" prop="watResState" label="水资源划分"> 
              <template slot-scope="scope">
                <span class="table-order">{{ scope.row.watResState == 0 ? '平水年' : scope.row.watResState > 0 ? '丰水年' : '枯水年' }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <!-- 水资源量 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水资源量'">
          <el-table
            ref="multipleTable"
            :data="waterResourceData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="行政区域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="totalWaterResource"
              label="水资源总量(亿m³)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="surfaceWater"
              label="地表水量(亿m³)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="groundWater"
              label="地下水量(亿)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="avgWrUseRate"
              label="全市平均水资源利用率(%)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="avgWrUse"
              label="人均用水水资源量(m³)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="waterProduceFactor"
              label="产水系数"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="waterProduceMod"
              label="产水模数(万m³/k㎡)"
              sortable
            />
          </el-table>
        </div>
        <!-- 供水量 tab列表 -->
        <div class="tab-list-sty" v-if="code == '供水量'">
          <el-table
            ref="multipleTable"
            :data="waterRupplyData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="year"
              label="年份"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="totalSupply"
              label="供水总量(亿m³)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="supStoreProj"
              label="蓄水量（亿m³）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="supDiverProj"
              label="引水量（亿m³）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="supLiftProj"
              label="提水量（亿m³）"
            />
          </el-table>
        </div>
        <!-- 用水量  tab列表 -->
        <div class="tab-list-sty" v-if="code == '用水量'">
          <el-table
            ref="multipleTable"
            :data="waterUseData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="年份"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="行政区域"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="totalConsume"
              label="用水总量(亿m³)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="占全市比例%"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="人均综合用水总量"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="相较全市平均"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="相较全省平均"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="相较全国平均"
            />
          </el-table>
        </div>
        <!-- 水库蓄水量 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水库蓄水量'">
          <el-table
            ref="multipleTable"
            :data="reservoirStorageData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="resName"
              label="水库名称"
            />
            <!-- <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="currentStore"
              label="今年"
            /> -->
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="currentStore"
              label="今年末蓄水(亿m³)"
              sortable
            />
            <!-- <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastYearStore"
              label="去年"
            /> -->
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastYearStore"
              label="去年末蓄水(亿m³)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="subtract"
              label="蓄水变化(亿m³)"
              sortable
            />
          </el-table>
        </div>
        <!-- 水资源承载能力 水量评价 tab列表 -->
        <div
          class="tab-list-sty"
          v-if="code == '水资源承载能力' && activeTabCarry == '水量评价'"
        >
          <el-table
            ref="multipleTable"
            :data="waterResCarryData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedTwoFormatter"
              prop="consumeTotal"
              label="用水总量W"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedTwoFormatter"
              prop="consumeNorm"
              label="用水总量控制指标W0"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedTwoFormatter"
              prop="ratio"
              label="W/W0"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="evaDesc"
              label="评价结果"
              sortable
            />
          </el-table>
        </div>
        <!-- 水资源承载能力 水质评价 tab列表 -->
        <div
          class="tab-list-sty"
          v-if="code == '水资源承载能力' && activeTabCarry == '水质评价'"
        >
          <el-table
            ref="multipleTable"
            :data="waterResCarryData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="wfzTotalPer"
              label="水功能区水质达标率Q"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="wfzTotalPerNorm"
              label="水功能区水质达标率要求Q0"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="ratio"
              label="Q/Q0"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="evaDesc"
              label="评价等级"
              sortable
            />
          </el-table>
        </div>
        <!-- 水资源承载能力 经济评价 tab列表 -->
        <div
          class="tab-list-sty"
          v-if="code == '水资源承载能力' && activeTabCarry == '经济评价'"
        >
          <el-table
            ref="multipleTable"
            :data="waterResCarryData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="pop"
              label="人口（万人）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="totalWaterConsume"
              label="总用水量（亿m³）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="watConEachPop"
              label="人均综合用水量（m³）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="controlWaterConsume"
              label="控制用水量（亿m³）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="gdp"
              label="国内地区生产总值（亿元）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="watConPerGdp"
              label="万元GDP用水量（m³/万元）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="supportEco"
              label="可支撑的万元GDP（亿元））"
              sortable
            />
          </el-table>
        </div>
        <!-- 水资源承载能力 人口评价 tab列表 -->
        <div
          class="tab-list-sty"
          v-if="code == '水资源承载能力' && activeTabCarry == '人口评价'"
        >
          <el-table
            ref="multipleTable"
            :data="waterResCarryData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="currentPop"
              label="人口（万人）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="totalWaterConsume"
              label="总用水量（亿m³）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="watConEachPop"
              label="人均综合用水量（m³）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="controlWaterConsume"
              label="控制用水量（亿m³）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="supportPop"
              label="控制指标可支撑人口（万人））"
              sortable
            />
          </el-table>
        </div>
        <!-- 水资源承载能力 用水评价 tab列表 -->
        <div
          class="tab-list-sty"
          v-if="code == '水资源承载能力' && activeTabCarry == '用水评价'"
        >
          <el-table
            ref="multipleTable"
            :data="waterResCarryData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="resName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="currentStore"
              label="“十四五”万元GDP用水量（m³/万元）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="currentStore"
              label="“十四五”用水总量控制指标（亿m³）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastYearStore"
              label="预测用水饱和年份"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastYearStore"
              label="饱和年份用水量（亿m³））"
              sortable
            />
          </el-table>
        </div>

        <!-- 预警信息 tab列表 -->
        <div class="tab-list-sty" v-if="code == '预警信息'">
          <el-table
            ref="multipleTable"
            :data="earlyData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column type="index" label="序号" width="55">
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="type"
              label="预警类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="content"
              label="预警内容"
              width="400"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="sendTime"
              label="发送时间"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="destinationUserName"
              label="接收人"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="fromUserName"
              label="发起人"
            />
          </el-table>
          <el-pagination
            background
            class="pagination"
            :page-size="pageSize"
            :pager-count="5"
            layout="total, prev, pager, next"
            :total="totalPage"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>

        <div class="tab-list-sty" v-if="code == '流域生态流量'">
          <el-table
            ref="multipleTable"
            :data="flowData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="sectName"
              label="断面名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="bas"
              label="流域"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="flow"
              label="实时流量(m³/s)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="orangeFlow"
              label="橙色预警指标(m³/s)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="redFlow"
              label="红色预警指标(m³/s)"
              sortable
            />
          </el-table>
        </div>
        <div class="tab-list-sty" v-if="code == '规划'">
          <el-table
            ref="multipleTable"
            :data="planningData.data"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column type="index" label="序号" width="55" />

            <el-table-column
              :show-overflow-tooltip="true"
              prop="projName"
              label="项目名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projDuration"
              label="起止年份"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              prop="constructContent"
              label="主要建设内容"
            />
            <el-table-column label="总投资(万元)" prop="amountInvestTotal" />
            <el-table-column
              :label="`${mediumSized.split('|')[0]}投资(万元)`"
              prop="amountInvestDevelop"
            />
            <el-table-column label="已完成投资(万元)" prop="amountInvested" />
            <el-table-column
              label="结转总投资(万元)"
              prop="amountInvestIndirect"
            />
          </el-table>
        </div>
        <!-- 水厂 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水厂'">
          <el-table
            ref="multipleTable"
            :data="waterPointList"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="cwsName"
              label="水厂名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="cwsLoc"
              label="水厂位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="desWasuPop"
              label="人口（万人）"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="desWasuScal"
              label="规模"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="wasuRang"
              label="供水规模"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="engType"
              label="类型"
            />
            <!-- <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="cumulatAnnWain"
              label="主水源地"
              sortable
            /> -->
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="supplyFlow"
              label="实时流量（m³/h）"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="totalSupplyQuantity"
              label="日供水量（m³）"
            />
            <!-- <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="cumulatAnnWain"
              label="本月供水量（m³）"
              sortable
            /> -->
            <!-- <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="yearlyTotalWaterSupply"
              label="年供水量（m³）"
              sortable
            /> -->
          </el-table>
        </div>
        <!-- 水厂运行状态 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水厂运行状态'">
          <el-table
            ref="multipleTable"
            :data="waterPointList"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="cwsName"
              label="水厂名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="cwsLoc"
              label="水厂位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="supplyFlow"
              label="流量（m³/h）"
            />

            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              label="pH"
              sortable
            >
              <template slot-scope="scope">
                <span style="color: #ef1313">{{
                  scope.row.phValue || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="clValue"
              label="余氯"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="ntuValue"
              label="浑浊度"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="runtimeState"
              label="运行状态"
              sortable
            >
              <template slot-scope="scope">
                <span
                  :style="{
                    color:
                      scope.row.runtimeState == '0'
                        ? '#19AF47'
                        : scope.row.runtimeState == '1'
                        ? '#EF1313 '
                        : scope.row.runtimeState == '2'
                        ? '#FF551F'
                        : '#E89100',
                  }"
                  >{{
                    scope.row.runtimeState == "0"
                      ? "正常供水"
                      : scope.row.runtimeState == "1"
                      ? "供水预警"
                      : scope.row.runtimeState == "2"
                      ? "水质预警"
                      : "水质供水预警"
                  }}</span
                >
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 水源地 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水源地'">
          <el-table
            ref="multipleTable"
            :data="waterSourData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="swhsName"
              label="水源地名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="adnm"
              label="水源地位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              label="类型"
            >
              <template slot-scope="scope">
                <span>{{
                  scope.row.wainWasoType == "1"
                    ? "水库"
                    : scope.row.wainWasoType == "2"
                    ? "山塘"
                    : scope.row.wainWasoType == "3"
                    ? "河流湖泊"
                    : scope.row.wainWasoType == "4"
                    ? "溪沟堰坝"
                    : scope.row.wainWasoType == "5"
                    ? "蓄水池"
                    : scope.row.wainWasoType == "6"
                    ? "地下水"
                    : scope.row.wainWasoType == "7"
                    ? "海水淡化"
                    : scope.row.wainWasoType == "8"
                    ? "山泉水"
                    : "其他"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="destinationUserName"
              label="规模"
            >
              <template slot-scope="scope">
                <span>{{
                  scope.row.wainWasoType == "1"
                    ? "县级以上"
                    : scope.row.wainWasoType == "2"
                    ? "千吨万人"
                    : scope.row.wainWasoType == "3"
                    ? "200吨-1000吨"
                    : "200吨以下"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="desAnnWasuPop"
              label="可供水量(万m³)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="withdrawal"
              label="取水量(万m³/月)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="waterMonth"
              label="取水月份"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="cumulatAnnWain"
              label="本年累计取水量(万m³)"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="updateTime"
              label="数据更新时间"
              sortable
            />
          </el-table>
        </div>
        <!-- 水量预警 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水量预警'">
          <el-table
            ref="multipleTable"
            :data="waterWarnData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="swhsName"
              label="水源地名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="swhsName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              label="类型"
            >
              <template slot-scope="scope">
                <span>{{
                  scope.row.wainWasoType == "1"
                    ? "水库"
                    : scope.row.wainWasoType == "2"
                    ? "山塘"
                    : scope.row.wainWasoType == "3"
                    ? "河流湖泊"
                    : scope.row.wainWasoType == "4"
                    ? "溪沟堰坝"
                    : scope.row.wainWasoType == "5"
                    ? "蓄水池"
                    : scope.row.wainWasoType == "6"
                    ? "地下水"
                    : scope.row.wainWasoType == "7"
                    ? "海水淡化"
                    : scope.row.wainWasoType == "8"
                    ? "山泉水"
                    : "其他"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              label="水量预警等级"
              sortable
            >
              <template slot-scope="scope">
                <span
                  :style="{
                    color:
                      scope.row.wainWasoType == '1'
                        ? 'rgb(25, 175, 71)'
                        : scope.row.wainWasoType == '2'
                        ? 'rgb(232, 145, 0)'
                        : scope.row.wainWasoType == '3'
                        ? 'rgb(255, 85, 31)'
                        : 'rgb(232, 0, 0)',
                  }"
                  >{{
                    scope.row.wainWasoType == "1"
                      ? "暂无风险"
                      : scope.row.wainWasoType == "2"
                      ? "黄色预警"
                      : scope.row.wainWasoType == "3"
                      ? "橙色预警"
                      : "红色预警"
                  }}</span
                >
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 珊溪水质监测 tab列表 -->
        <div class="tab-list-sty" v-if="code == '珊溪水质监测'">
          <el-table
            ref="multipleTable"
            :data="sxWaterData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="type"
              label="站点名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="content"
              label="时间"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="sendTime"
              label="COD"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="destinationUserName"
              label="氨氮"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="destinationUserName"
              label="总磷"
            />
          </el-table>
        </div>

        <!-- 珊溪巡查 tab列表 -->
        <div class="tab-list-sty" v-if="code == '巡查'">
          <el-table
            ref="multipleTable"
            :data="sxCheckData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="chaosName"
              label="乱点名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="chaosType"
              label="乱点类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="chaosStatus"
              label="乱点状态"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="discoverTime"
              label="发现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="cityName"
              label="所属县/市"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="所在区域"
            />
          </el-table>
        </div>

        <!-- 取水户 tab列表 -->
        <div class="tab-list-sty" v-if="code == '取水户'">
          <el-table
            ref="multipleTable"
            :data="wtaerIntakeData"
            height="100%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="wiuName"
              label="名称"
              width="180"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="wiuType"
              label="取水类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="intake"
              label="当年取水量(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="intakePermit"
              label="许可取水量(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="legRepr"
              label="法人代表"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="legPhone"
              label="联系电话"
            />
          </el-table>
        </div>

        <!-- 批后监管 tab列表 -->
        <div class="tab-list-sty" v-if="code == '批后监管'">
          <el-table
            ref="multipleTable"
            :data="approSuperData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projectName"
              label="项目名称"
              width="200"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectCode"
              label="项目编号"
              width="150"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="adnm"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="year"
              label="年份"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="riverName"
              label="所属河道"
              width="200"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="address"
              label="地点"
              width="200"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="destinationUserName"
              label="监管次数"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="inspectionNum"
              label="监管状态"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lastInspectionTime"
              label="最后一次监管时间"
              width="190"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="unRectifyProblemNum"
              label="未整改问题数"
              width="150"
              sortable
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="unitName"
              label="单位名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="legalRepresent"
              label="法人代表"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="phone"
              label="联系电话"
            />
          </el-table>
          <el-pagination
            background
            class="pagination"
            :page-size="pageSize"
            :pager-count="5"
            layout="total, prev, pager, next"
            :total="totalPage"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>

        <!-- 水域变化 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水域变化'">
          <el-table
            ref="multipleTable"
            :data="wtaerChangeData"
            height="89%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="year"
              label="年份"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedTwoFormatter"
              prop="waterArea"
              label="水域面积(k㎡)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="waterRate"
              label="水面率(%)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="occupiedArea"
              label="占用面积(k㎡)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="compensationArea"
              label="补偿面积(k㎡)"
            />
          </el-table>
          <el-pagination
            background
            class="pagination"
            :page-size="pageSize"
            :pager-count="5"
            layout="total, prev, pager, next"
            :total="totalPage"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>

        <!-- 清四乱 tab列表 -->
        <div class="tab-list-sty" v-if="code == '清四乱'">
          <el-table
            ref="multipleTable"
            :data="fourChaosData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="riverName"
              label="问题河道"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="address"
              label="详细地址"
              width="180"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="status"
              label="问题状态"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.status == '0'" style="color: #f2a600"
                  >未处理</span
                >
                <span v-if="scope.row.status == '1'" style="color: #1ef8f2"
                  >已处理</span
                >
                <span v-if="scope.row.status == '2'" style="color: #ff0000"
                  >逾期未处理</span
                >
                <span v-if="scope.row.status == '3'" style="color: #c60000"
                  >驳回</span
                >
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="problemSource"
              label="问题来源"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="fourType"
              label="四乱类型"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="severity"
              label="严重程度"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="problemDescription"
              label="问题描述"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="createTime"
              label="上传时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="rectificationRequirement"
              label="整改范围"
            />
          </el-table>
          <el-pagination
            background
            class="pagination"
            :page-size="pageSize"
            :pager-count="5"
            layout="total, prev, pager, next"
            :total="totalPage"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>

        <!-- 美丽河湖 tab列表 -->
        <div
          class="tab-list-sty"
          v-if="code == '美丽河湖'"
          style="padding-bottom: 0"
        >
          <el-table
            v-if="activeTabBeauty == '美丽河湖'"
            ref="multipleTable"
            :data="beautyData"
            height="85%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="50">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projectName"
              label="项目名称"
              width="76"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectStatus"
              label="工程状态"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="sectionName"
              label="河段名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="startLocation"
              label="起点位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="endLocation"
              label="终点位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="planInvest"
              label="计划投资(万元)"
              width="115"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="completeInvestment"
              label="完成投资(万元)"
              width="115"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="investmentProportion"
              width="70"
              label="比例(%)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="planScale"
              label="计划规模(km)"
              width="110"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="completeScale	"
              label="完成规模(km)"
              width="110"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="scaleRatio"
              width="70"
              label="比例(%)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="overallProgress	"
              label="总体形象进度(%)"
              width="130"
            />
          </el-table>
          <el-table
            v-if="activeTabBeauty == '中小河流'"
            ref="multipleTable"
            :data="beautyData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projectName"
              label="项目名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectStatus"
              label="工程状态"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="sectionName"
              label="河段名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="startLocation"
              label="起点位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="endLocation"
              label="终点位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="totalProjectInvestment"
              label="项目总投资(万元)"
              width="140"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="completeProjectInvestment"
              label="完成总投资(万元)"
              width="140"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="investmentProportion"
              label="比例(%)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="overallProgress	"
              label="总体形象进度(%)"
              width="130"
            />
          </el-table>
          <el-table
            v-if="activeTabBeauty == '水美乡镇'"
            ref="multipleTable"
            :data="beautyData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projectName"
              label="项目名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectStatus"
              label="工程状态"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="sectionName"
              label="河段名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="startLocation"
              label="起点位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="endLocation"
              label="终点位置"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="planInvestment"
              label="计划投资(万元)"
              width="120"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="completeInvestment"
              label="完成投资(万元)"
              width="120"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="investmentProportion"
              label="比例(%)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="overallProgress	"
              label="总体形象进度(%)"
              width="130"
            />
          </el-table>
          <el-table
            v-if="activeTabBeauty == '农村池塘'"
            ref="multipleTable"
            :data="beautyData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="projectName"
              label="项目名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="projectStatus"
              label="工程状态"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="poudName"
              label="池塘名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="planInvestment"
              label="计划投资(万元)"
              width="120"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="completeInvestment"
              label="完成投资(万元)"
              width="120"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="investmentProportion"
              label="比例(%)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="overallProgress	"
              label="总体形象进度(%)"
              width="130"
            />
          </el-table>

          <el-pagination
            background
            class="pagination"
            :page-size="pageSize"
            :pager-count="5"
            layout="total, prev, pager, next"
            :total="totalPage"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>

        <!-- 水电站 tab列表 -->
        <div class="tab-list-sty" v-if="code == '水电站'">
          <el-table
            ref="multipleTable"
            :data="hydroStaData"
            height="86%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="hystName"
              label="水电站名称"
              width="150"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="hystCode"
              label="水电站编码"
              width="150"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="hystType"
              label="水电站类型"
              width="150"
            >
              <template slot-scope="scope">{{
                getHydroType(scope.row.hystType)
              }}</template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="totInsCap"
              label="总装机容量(m³)"
              sortable
              width="150"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="insCap"
              label="装机流量(m³/s)"
              sortable
              width="150"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="aprvEcoFlowYear"
              label="核定生态流量(m³/s)"
              sortable
              width="150"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="standardRate"
              label="生态流量达标率(%)"
              sortable
              width="150"
            >
              <template slot-scope="scope"
                >{{ (scope.row.standardRate * 100).toFixed(2) }}%</template
              >
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="onlineRate"
              label="设备在线率"
              sortable
              width="120"
            >
              <template slot-scope="scope"
                >{{ (scope.row.onlineRate * 100).toFixed(2) }}%</template
              >
            </el-table-column>
          </el-table>
          <el-pagination
            background
            class="pagination"
            :page-size="pageSize"
            :pager-count="5"
            layout="total, prev, pager, next"
            :total="totalPage"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>

        <!-- 河长巡查 tab列表 -->
        <div class="tab-list-sty" v-if="code == '河长巡查'">
          <el-table
            ref="multipleTable"
            :data="patrolData"
            height="90%"
            stripe
            class="table-wrapper"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            @sort-change="sortChange"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="areaName"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="riverName"
              label="河段名称"
              width="100"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="realName"
              label="发现人"
              width="70"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="address"
              label="发现地址"
              width="180"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="createDate"
              label="开始时间"
              width="150"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="deadline"
              label="截止时间"
              width="150"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              width="80"
              prop="level"
              label="问题程度"
            >
              <template slot-scope="scope">{{
                getProblemType(scope.row.level)
              }}</template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="content"
              label="问题详情"
              width="200"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="state"
              label="处置状态"
              width="80"
            >
              <template slot-scope="scope">{{
                getProblemStatu(scope.row.state)
              }}</template>
            </el-table-column>
          </el-table>
          <el-pagination
            background
            class="pagination"
            :page-size="pageSize"
            :pager-count="5"
            layout="total, prev, pager, next"
            :total="totalPage"
            @current-change="pageChange"
          >
          </el-pagination>
        </div>

        <!-- 第三场景 水库 -->
        <div
          class="tab-list-sty"
          v-if="mediumSized == '防御复盘' && code == '水库分析'"
        >
          <el-table
            ref="multipleTable"
            :data="reservoirAnalysisData.list"
            height="100%"
            stripe
            class="table-wrapper"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="resName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="lowestWaterLevel"
              label="最低水位(m)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lowestWaterLevelTm"
              width="150"
              label="最低水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestWaterLevel"
              label="最高水位(m)"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :show-overflow-tooltip="true"
              :sortable="'custom'"
              prop="dataTime"
              width="100"
              label="更新时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              :show-overflow-tooltip="true"
              prop="safetyTime"
              width="110"
              label="鉴定时间"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="safetyConclusion"
              width="110"
              label="鉴定结论"
            />
            <el-table-column
              :formatter="toFixedFormatter"
              prop="reinforce"
              width="110"
              label="有无加固"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestWaterLevelTm"
              width="150"
              label="最高水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              width="150"
              prop="capacitySub"
              label="累计拦蓄水量(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              width="150"
              prop="drainage"
              label="累计泄洪量(万m³)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              width="150"
              prop="overWarnMinutes"
              label="累计超警时间(min)"
            />
          </el-table>
        </div>
        <!-- 第三场景 水闸 -->
        <div
          class="tab-list-sty"
          v-if="mediumSized == '防御复盘' && code == '水闸分析'"
        >
          <el-table
            ref="multipleTable"
            :data="lockAnalysisData"
            height="100%"
            stripe
            class="table-wrapper"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="wagaName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="lowestUpz"
              label="最小闸上水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lowestUpzTm"
              label="最小闸上水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestUpz"
              label="最大闸上水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestUpzTm"
              label="最大闸上水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="openGateMinutes"
              label="累计开闸时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="drainage"
              label="累计排洪量"
            />
          </el-table>
        </div>
        <!-- 第三场景 河网 -->
        <div
          class="tab-list-sty"
          v-if="mediumSized == '防御复盘' && code == '河网分析'"
        >
          <el-table
            ref="multipleTable"
            :data="riverNetworkData"
            height="100%"
            stripe
            class="table-wrapper"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="rnName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              :sortable="'custom'"
              prop="mainStName"
              label="水位站名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="lowestWaterLevel"
              label="最低水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lowestWaterLevelTm"
              label="最低水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestWaterLevel"
              label="最高水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestWaterLevelTm"
              label="最高水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              prop="overWarnMinutes"
              label="累计超警时间"
            />
          </el-table>
        </div>
        <!-- 第三场景 潮位 -->
        <div
          class="tab-list-sty"
          v-if="mediumSized == '防御复盘' && code == '潮位分析'"
        >
          <el-table
            ref="multipleTable"
            :data="waterAnalysisData"
            height="100%"
            stripe
            class="table-wrapper"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="stName"
              label="名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="lowestRealTide"
              label="最低水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="lowestRealTideTm"
              label="最低水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestRealTide"
              label="最高水位"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="toFixedFormatter"
              prop="highestRealTideTm"
              label="最高水位出现时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="highestSubTide"
              label="最大增水"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="overWarnMinutes"
              label="累计超警时间"
            />
          </el-table>
        </div>
        <!-- 第三场景 水利设施保险 -->
        <div
          class="tab-list-sty"
          v-if="mediumSized == '防御复盘' && code == '水利设施保险'"
        >
          <el-table
            ref="multipleTable"
            :data="realTimeData"
            height="100%"
            stripe
            class="table-wrapper"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="name"
              label="合同编号"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="name"
              label="合同名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="newest1hour"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="1hour"
              label="堤防名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="3hour"
              label="损失图片"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="6hour"
              label="相关文件"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="主管部门(甲)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="上传时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场视频"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场音频"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场图片"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="估评理赔金额(元)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="堪核时间"
            />
          </el-table>
        </div>
        <!-- 第三场景 水利设施保险 -->
        <div
          class="tab-list-sty"
          v-if="mediumSized == '防御复盘' && code == '水利设施保险'"
        >
          <el-table
            ref="multipleTable"
            :data="realTimeData"
            height="100%"
            stripe
            class="table-wrapper"
            width="100%"
            v-loading="loading"
            element-loading-text="拼命加载中"
            element-loading-spinner="el-icon-loading"
            element-loading-background="rgba(0, 0, 0, 0.8)"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="index" label="序号" width="55">
              <template slot-scope="scope">
                <span class="table-order">{{ scope.$index + 1 }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="name"
              label="合同编号"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              sortable
              prop="name"
              label="合同名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="newest1hour"
              label="行政区划"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="1hour"
              label="堤防名称"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="3hour"
              label="损失图片"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="6hour"
              label="相关文件"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="主管部门(甲)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="上传时间"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场视频"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场音频"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="现场图片"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="估评理赔金额(元)"
            />
            <el-table-column
              :show-overflow-tooltip="true"
              :formatter="Formatter"
              sortable
              prop="24hour"
              label="堪核时间"
            />
          </el-table>
        </div>
        <!-- 风险隐患、大中型水库、海塘 分页 -->
        <div
          class="pagination-area"
          v-if="code == '风险隐患' || code == '水库' || code == '海塘'"
          style="padding-bottom: 15px; margin-top: 0"
        >
          <el-pagination
            class="right"
            @size-change="handleSizeChange"
            @current-change="pageChange"
            :current-page.sync="pageNum"
            :page-sizes="[10, 20, 30, 50]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next"
            :total="totalPage"
          />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { planDigitizationApi } from "@/api/plan_digitization/index.js";
import { disasterPreventionApi } from "@/api/RightSildePanel";
import { waterResourceApi } from "@/api/waterResourcesGuarantee";
import { getDict } from "@/api/system";
import config from "@/config/config";
import { localData } from "@/plugins/storage";
import { mapGetters } from "vuex";
import { inforApi } from "@/api/inforApi";
import axios from "axios";

export default {
  name: "DetailsCommonDialog",
  props: {
    visible: { type: Boolean, default: false },
    code: { type: String, default: "" },
    mediumSized: { type: String, default: "" }
  },
  created() {
    this.value1 = [
      new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000).format("yyyy-MM-dd"),
      new Date().format("yyyy-MM-dd")
    ];
    this.stiStart = new Date(new Date().getTime() - 7 * 24 * 60 * 60 * 1000).format("yyyy-MM-dd");
    this.stiEnd = new Date().format("yyyy-MM-dd");
    // disabledDate 为true表示不可选,false表示可选
    this.pickerOptions.disabledDate = time => {
      // 设置可选择的日期为今天之前的两个月内
      let curDate = new Date().getTime();
      // 这里算出两个月的毫秒数,这里使用60的平均值,实际中应根据具体的每个月有多少天计算
      let day = 60 * 24 * 3600 * 1000;
      let dateRegion = curDate - day;
      return time.getTime() > Date.now() || time.getTime() < dateRegion;
    };
  },
  data() {
    return {
      // 导出Loading
      exportLoading: false,
      // 水厂 水厂运行状态
      waterPointData: {},
      waterWorksStatusList: [
        {
          label: "正常",
          value: "0"
        },
        {
          label: "供水预警",
          value: "1"
        },
        {
          label: "水质预警",
          value: "2"
        },
        {
          label: "水质供水预警",
          value: "3"
        }
      ],
      waterWorksScaleList: [
        {
          label: "全部",
          value: ""
        },
        {
          label: "两百吨千人以下",
          value: "两百吨千人以下"
        },
        {
          label: "两百吨千人以上",
          value: "两百吨千人以上"
        },
        {
          label: "千吨万人",
          value: "千吨万人"
        },
        {
          label: "城市水厂",
          value: "城市水厂"
        }
      ],
      waterWorksList: [
        {
          label: "全部",
          value: ""
        },
        {
          label: "城市",
          value: "城市"
        },
        {
          label: "乡镇",
          value: "乡镇"
        },
        {
          label: "联村",
          value: "联村"
        },
        {
          label: "单村",
          value: "单村"
        }
      ],
      adcdData: "",
      cwsName: "",
      engType: "",
      wasuRang: "",
      runtimeState: "",
      //
      value1: "",
      stiStart: "",
      stiEnd: "",
      pickerOptions: {},
      seawallData: {},
      // 规划
      planningData: {
        data: [],
        stats: [
          {
            label: "项目条数",
            num: "",
            icon: "icon-planning-total",
            unit: "条"
          },
          { label: "总投资", num: "", icon: "icon-invest-total", unit: "万元" },
          {
            label: "",
            num: "",
            icon: "icon-current-invest",
            auto: true,
            unit: "万元"
          },
          {
            label: "已完成投资",
            num: "",
            icon: "icon-finish-invest",
            unit: "万元"
          },
          {
            label: "结转投资",
            num: "",
            icon: "icon-indirect-invest",
            unit: "万元"
          }
        ]
      },
      planStart: "",
      planEnd: "",
      // 病险工程
      NameVal: "",
      radio: true,
      durationRainfall: [],
      durationRainfallList: [
        {
          value: 1,
          name: "显示前1小时"
        },
        {
          value: 3,
          name: "显示前3小时"
        },
        {
          value: 6,
          name: "显示前6小时"
        },
        {
          value: 24,
          name: "显示前24小时"
        },
        {
          value: 72,
          name: "显示前72小时"
        }
      ],
      projectNameVal: "", //工程名称
      projectTypeVal: [], //工程类型
      projectTypeList: [
        {
          value: "水利工程",
          label: "水利工程",
          children: [
            {
              value: "水库",
              label: "水库"
            },
            {
              value: "海塘",
              label: "海塘"
            },
            {
              value: "堤防",
              label: "堤防"
            },
            {
              value: "闸站",
              label: "闸站"
            },
            {
              value: "泵站",
              label: "泵站"
            },
            {
              value: "山塘",
              label: "山塘"
            }
          ]
        },
        {
          value: "在建水利工程",
          label: "在建水利工程"
        },
        {
          value: "在建涉水工程",
          label: "在建涉水工程"
        },
        {
          value: "水毁工程",
          label: "水毁工程",
          children: [
            {
              value: "水库",
              label: "水库"
            },
            {
              value: "海塘",
              label: "海塘"
            },
            {
              value: "堤防",
              label: "堤防"
            },
            {
              value: "闸站",
              label: "闸站"
            },
            {
              value: "泵站",
              label: "泵站"
            },
            {
              value: "山塘",
              label: "山塘"
            },
            {
              value: "渠道",
              label: "渠道"
            },
            {
              value: "护岸",
              label: "护岸"
            },
            {
              value: "水文测站",
              label: "水文测站"
            }
          ]
        },
        {
          value: "其他",
          label: "其他"
        }
      ], //工程类型
      riskTypeVal: "", //风险类型
      reportTime: "",
      riskTypeList: [
        {
          name: "隐患点",
          value: "251"
        },
        {
          name: "高风险点",
          value: "252"
        },
        {
          name: "薄弱点",
          value: "253"
        }
      ], //风险类型
      district: [], //区域
      districtList: [
        {
          value: "行政区划",
          label: "行政区划",
          children: [
            {
              value: "温州",
              label: "温州",
              children: [
                {
                  value: '"330302000000"',
                  label: "鹿城区"
                },
                {
                  value: '"330303000000"',
                  label: "龙湾区"
                },
                {
                  value: '"330304000000"',
                  label: "瓯海区"
                },
                {
                  value: '"330322000000"',
                  label: "洞头区"
                },
                {
                  value: '"330324000000"',
                  label: "永嘉县"
                },
                {
                  value: '"330326000000"',
                  label: "平阳县"
                },
                {
                  value: '"330327000000"',
                  label: "苍南县"
                },
                {
                  value: '"330328000000"',
                  label: "文成县"
                },
                {
                  value: '"330329000000"',
                  label: "泰顺县"
                },
                {
                  value: "330381000000",
                  label: "瑞安市"
                },
                {
                  value: '"330382000000"',
                  label: "乐清市"
                },
                {
                  value: '"330383000000"',
                  label: "龙港市"
                }
              ]
            }
          ]
        },
        {
          value: "流域",
          label: "流域",
          children: [
            {
              value: "瓯江",
              label: "瓯江"
            },
            {
              value: "飞云江",
              label: "飞云江"
            },
            {
              value: "鳌江",
              label: "鳌江"
            }
          ]
        }
      ], //区域
      reorganizeSituation: "", //整改情况
      reorganizeSituationList: [
        {
          name: "未整改"
        },
        {
          name: "正在整改"
        },
        {
          name: "已落实措施"
        },
        {
          name: "完成整改"
        }
      ], //整改情况
      discoveryChannel: "", //发现渠道
      discoveryChannelList: [
        {
          name: "县级检查",
          value: "xjjc"
        },
        {
          name: "工程自查",
          value: "zczc"
        },
        {
          name: "市级抽查",
          value: "sjcc"
        },
        {
          name: "省级督查",
          value: "sjdc"
        }
      ], //发现渠道
      // 第三场景
      bewareName: "", //提防名称
      belongName: "", //所属合同
      affiliatedUnit: "", //所属单位
      seaLevelName: "", //潮位名称
      riverName: "", //河网名称
      sluiceName: "", //水闸名称
      reservoirName: "", //水库名称
      claimSchedule: "", //理赔进度
      reservoirAnalysisData: {}, //水库数据
      lockAnalysisData: [], //水闸数据
      riverNetworkData: [], //河网数据
      waterAnalysisData: [], //潮位数据
      tideStaStart: "", //复盘起始时刻
      tideStaEnd: "", //复盘结束时刻

      claimScheduleList: [
        {
          name: "已申请"
        },
        {
          name: "已理赔"
        },
        {
          name: "主管部门确认"
        }
      ], //理赔进度

      count: 0,
      numTime: "1",
      //表格loading
      loading: false,
      //行政区划
      administration: [],
      engineeringValue: "",
      filterOpenGate: false,
      //水库流域选中值
      administrativeValue: "",
      designTideStandard: "",
      //行政区划选中值
      administrationValue: "",
      designTideStandardData: [
        {
          name: "100年以上"
        },
        {
          name: "100年一遇"
        },
        {
          name: "50年一遇"
        },
        {
          name: "20年一遇"
        },
        {
          name: "10年一遇"
        },
        {
          name: "10年以下"
        }
      ],
      //海塘工程等别
      EngineeringData: [
        {
          grade: "1级"
        },
        {
          grade: "2级"
        },
        {
          grade: "3级"
        },
        {
          grade: "4级"
        },
        {
          grade: "5级"
        }
      ],
      //水库流域
      administrative: [
        {
          name: "飞云江"
        },
        {
          name: "瓯江"
        },
        {
          name: "鳌江"
        }
      ],
      //工程规模
      projectScaleData: [
        {
          name: "大中型"
        },
        {
          name: "小（Ⅰ）型"
        },
        {
          name: "小（Ⅱ）型"
        }
      ],
      surfaceTypeStration: [
        {
          name: "行政区划"
        },
        {
          name: "流域"
        }
      ],
      projTypeData: [
        {
          name: "水库"
        },
        {
          name: "山塘"
        },
        {
          name: "海塘"
        },
        {
          name: "水闸"
        },
        {
          name: "其他"
        }
      ],
      //水库工程规模选中值
      projectScaleValue: "大中型",
      statisticsData: {},
      //水闸数据
      waterGateData: [],
      //水闸工程规模选中值
      engScalesValue: "",
      //水库工程规模下拉数据
      engScalesData: [
        {
          name: "大（Ⅰ）型"
        },
        {
          name: "大（Ⅱ）型"
        },
        {
          name: "中型"
        },
        {
          name: "小（Ⅰ）型"
        },
        {
          name: "小（Ⅱ）型"
        }
      ],
      //水库统计
      reservoirStatistical: {},
      riverwayData: [],
      riverwayList: {},
      seaLevelData: [],
      seaLevelList: {},
      dangerouslyData: [],
      projType: "",
      rainfallType: "行政区划",
      rainfalStration: [
        {
          name: "行政区划"
        },
        {
          name: "流域"
        }
      ],
      inTheShortData: [],
      realTimeData: [],
      realTimeStration: [
        {
          name: "面雨量"
        },
        {
          name: "点雨量"
        }
      ],
      realTimeType: "点雨量",
      administrativeType: "",
      drainageType: "",
      surfaceType: "",
      inputValue: "", //输入框
      selectValue: "", //下拉值
      potentialRisksData: [],
      seawallProjData: [],
      orderColumn: "",
      ascOrDesc: "",
      options: [
        {
          label: "测试",
          value: "测试"
        }
      ],
      WaterloggingData: [],
      num: 0,
      warehouseData: {},
      ranksData: {},
      warehouseOptions: [],
      ranksOptions: [],
      warehouseType: "",
      ranksType: "",
      videoData: [],
      stats4PageCameraData: {},
      waterLoggingData: {},
      waterLoggingType: null,
      waterLoggingOptions: [
        {
          label: "无积水",
          value: 0
        },
        {
          label: "有积水",
          value: 1
        }
      ],
      //防汛队伍 仓库统计类型图标map
      buildingTypeIconMap: {
        学校: "icon-school-tongji",
        成员单位: "icon-member-tongji",
        工管单位: "icon-work-tongji",
        村居: "icon-village-tongji",
        本级: "icon-samelevel-tongji",
        指挥部: "icon-command-tongji",
        企业: "icon-enterprise-tongji",
        社区: "icon-society-tongji"
      },
      statusType: "",
      floodLevel: "",
      floodLevelOptions: [
        {
          label: "5年一遇",
          value: 5
        },
        {
          label: "10年一遇",
          value: 10
        },
        {
          label: "20年一遇",
          value: 20
        },
        {
          label: "50年一遇",
          value: 50
        }
      ],
      floodRiskData: [],
      rainSearchList: [],

      // 降水量
      yearOption: [],
      yearDropdownVisible: false,
      checkYear: "",
      precipitationVal: {},
      precipitationData: [], // 降水量
      waterResourceData: [], // 水资源量
      activeTab: "供水总量", // 供水量tab 供水总量/供水工程
      activeTabCarry: "水量评价", // 水资源承载能力tab
			WrbWaterSupplyData: {},
      waterRupplyData: [], // 供水量
      waterUseData: [], // 用水量
      reservoirStorageData: [], // 水库蓄水量
      waterResCarryData: [], // 水资源承载能力
      flowData: [], //流域生态流量

      // 预警信息
      earlyType: "", // 预警信息 预警类型
      earlySearchVal: "", // 预警信息 关键字
      earlyData: [], // 预警信息
      earlyTypeOption: [], // 预警类型下拉
      pageSize: 10,
      totalPage: 1,
      pageNum: 1,
      // 水厂 水运行状态
      waterPointList: [],
      // 水源地 水量预警
      wasuObjTypeData: [],
      waterSourType: "",
      waterSourTypeOption: [
        {
          label: "水库",
          value: "1"
        },
        {
          label: "山塘",
          value: "2"
        },
        {
          label: "河流湖泊",
          value: "3"
        },
        {
          label: "溪沟堰坝",
          value: "4"
        },
        {
          label: "蓄水池",
          value: "5"
        },
        {
          label: "地下水",
          value: "6"
        },
        {
          label: "海水淡化",
          value: "7"
        },
        {
          label: "山泉水",
          value: "8"
        },
        {
          label: "其他",
          value: "9"
        }
      ],
      waterSourScale: "",
      waterSourScaleOption: [
        {
          label: "县级以上",
          value: "1"
        },
        {
          label: "千吨万人",
          value: "2"
        },
        {
          label: "200吨-1000吨",
          value: "3"
        },
        {
          label: "200吨以下",
          value: "4"
        }
      ],
      waterSourSearchVal: "",
      waterSourData: [],
      waterSourLevel: "",
      waterSourLevelOption: [
        {
          label: "暂无风险",
          value: "1"
        },
        {
          label: "黄色预警",
          value: "2"
        },
        {
          label: "橙色预警",
          value: "3"
        },
        {
          label: "红色预警",
          value: "4"
        }
      ],
      waterWarnData: [],

      // 珊溪
      countList: {},
      stationType: "",
      stationTypeOption: [],
      shanXiWaterSearchVal: "",
      sxWaterData: [],
      chaosType: "",
      chaosTypeOption: [
        { label: "全部", value: "" },
        { label: "水土流失", value: "水土流失" },
        { label: "非法采砂", value: "非法采砂" },
        { label: "倾倒垃圾", value: "倾倒垃圾" },
        { label: "违章建筑", value: "违章建筑" },
        { label: "占用水域", value: "占用水域" },
        { label: "非法排污", value: "非法排污" },
        { label: "畜禽养殖", value: "畜禽养殖" }
      ],
      chaosStatus: "",
      chaosStatusOption: [
        { label: "全部", value: "" },
        { label: "已完成", value: "已完成" },
        { label: "整改中", value: "整改中" }
      ],
      chaosName: "",
      sxCheckData: [],

      // 取水户
      wtaerIntakeType: "",
      wtaerIntakeTypeOption: [
        { label: "全部", value: "" },
        { label: "既用又取", value: "既用又取" },
        { label: "只取不用", value: "只取不用" },
        { label: "只用不取", value: "只用不取" }
      ],
      wtaerIntakeCount: {},
      wtaerIntakeData: [],
      wtaerIntakeWarnType: "",
      // wtaerIntakeWarnTypeOption: [
      //   { label: "全部", value: "" },
      //   { label: "正常", value: "正常" },
      //   { label: "接近许可", value: "接近许可" },
      //   { label: "超许可", value: "超许可" },
      // ],

      // 批后监管
      approSupeCount: {},
      riverType: "",
      riverTypeOption: [
        { label: "全部", value: "" },
        { label: "1级", value: "1" },
        { label: "2级", value: "2" },
        { label: "3级", value: "3" },
        { label: "4级", value: "4" },
        { label: "5级", value: "5" }
      ],
      supercisionStatu: "",
      supercisionStatuOption: [
        { label: "全部", value: "" },
        { label: "新项目", value: "1" },
        { label: "监管中", value: "2" },
        { label: "复核中", value: "3" },
        { label: "移交执法大队", value: "4" }
      ],
      projStage: "",
      projStageOption: [
        { label: "全部", value: "" },
        { label: "前期阶段", value: "1" },
        { label: "施工阶段", value: "2" },
        { label: "完工阶段", value: "3" }
      ],
      approSuperData: [],

      // 水域变化
      waterChangeCount: {},
      wtaerChangeData: [],

      // 清四乱
      fourChaosCount: {},
      rectiRange: "",
      rectiRangeOption: [
        { label: "全部", value: "" },
        { label: "已处理", value: "1" },
        { label: "未处理", value: "0" },
        { label: "逾期未处理", value: "2" },
        { label: "驳回", value: "3" }
      ],
      reasonFrom: "",
      reasonFromOption: [
        { label: "全部", value: "" },
        { label: "市级督查", value: "市级督查" },
        { label: "县级自查", value: "县级自查" }
      ],
      fourChaosType: "",
      fourChaosTypeOption: [
        { label: "全部", value: "" },
        { label: "乱占", value: "乱占" },
        { label: "乱采", value: "乱采" },
        { label: "乱堆", value: "乱堆" },
        { label: "乱建", value: "乱建" },
        { label: "其他", value: "其他" }
      ],
      severity: "",
      severityOption: [
        { label: "全部", value: "" },
        { label: "轻微", value: "轻微" },
        { label: "中度", value: "中度" },
        { label: "重大", value: "重大" }
      ],
      qslSearchVal: "",
      fourChaosData: [],

      // 美丽河湖
      activeTabBeauty: "美丽河湖",
      projStatu: "",
      projStatuOption: [
        { label: "全部", value: "" },
        { label: "项目在建", value: "项目在建" },
        { label: "已完工", value: "已完工" }
      ],
      beautyData: [],
      beautyCount: {},
      searchVal: "",

      // 水电站
      sdzCount: {},
      sdzType: "",
      sdzTypeOption: [
        { label: "全部", value: "" },
        { label: "饮水式", value: "2" },
        { label: "坝式(河床)", value: "1" },
        { label: "坝式(坝后)", value: "3" },
        { label: "混合式", value: "0" },
        { label: "抽水蓄能", value: "4" }
      ],
      sdzSearchVal: "",
      hydroStaData: [],
      //风险隐患
      riskCount: {
        totalNum: 0
      },

      // 河长巡查
      patrolCount: {},
      patrolDate: "",
      dealStatu: "",
      question: "",
      questionOption: [
        { label: "轻微", value: 0 },
        { label: "一般问题", value: 1 },
        { label: "重大问题", value: 2 }
      ],
      dealStatuOption: [
        { label: "全部", value: "" },
        { label: "未受理", value: 1 },
        { label: "处理中", value: 2 },
        { label: "已处理待评价", value: 3 },
        { label: "已处理已评价", value: 4 }
      ],
      patrolData: [],
      fullscreen: false
    };
  },
  computed: {
    ...mapGetters(["formatterStartTime", "formatterCurrentTime"]),
    /** 没有筛选行政区划的 */
    hideAdcdFilterCodes() {
      // prettier-ignore
      return ['短临预报', '水利设施保险', '水库分析', '水闸分析', '河网分析', '潮位分析', '风险隐患', '洪水风险图', '降水量', '水资源量', '供水量', '用水量', '水库蓄水量', '水资源承载能力', '流域生态流量', '预警信息', '水源地', '水量预警', '珊溪水质监测', '巡查', '取水户', '批后监管', '水域变化', '清四乱', '美丽河湖', '水电站', '河长巡查']
    },
    /** 没有流域筛选的 */
    hideBasinFilterCodes() {
      // prettier-ignore
      return ['山洪', '短临预报', '风险隐患', '海塘', '水利设施保险', '水库分析',
        '水闸分析', '河网分析', '潮位分析', '防汛仓库', '防汛队伍', '视频监控',
        '积水', '洪水风险图', '降水量', '水资源量', '供水量',
        '用水量', '水库蓄水量', '水资源承载能力', '预警信息', '规划',
        '水源地', '水量预警', '珊溪水质监测', '巡查', '取水户', '批后监管', '水域变化',
        '清四乱', '美丽河湖', '水电站', '河长巡查']
    },
    /** 搜索的 */
    showSerachCodes() {
      // prettier-ignore
      return ['水库', '水闸', '河网', '潮位', '病险工程', '积水', '海塘', '防汛仓库', '防汛队伍', '流域生态流量', '水库蓄水量', '规划']
    },
    startTime() {
      let time = this.formatterStartTime();
      return time != "Invalid date" ? time : "请选择时间";
    },
    currentTime() {
      let time = this.formatterCurrentTime();
      return time != "Invalid date" ? time : "请选择时间";
    },
    getIcon() {
      switch (this.activeTabBeauty) {
        case "美丽河湖":
          return "mlhh-dialog-count-1";
        case "中小河流":
          return "mlhh-dialog-count-2";
        case "水美乡镇":
          return "mlhh-dialog-count-3";
        case "农村池塘":
          return "mlhh-dialog-count-4";
      }
    }
  },
  watch: {
    startTime: {
      immediate: true,
      handler(val) {
        if (val != "请选择时间") {
          this.tideStaStart = val + ":00";
        }
      }
    },
    currentTime: {
      immediate: true,
      handler(val) {
        if (val != "请选择时间") {
          this.tideStaEnd = val + ":00";
          this.$nextTick(() => {
            this.queryVal();
          });
        }
      }
    }
  },
  mounted() {
    if (this.formatterCurrentTime() != "Invalid date") {
      this.tideStaStart = this.formatterStartTime() + ":00";
      this.tideStaEnd = this.formatterCurrentTime() + ":00";
    } else {
      this.tideStaStart =
        this.$moment()
          .subtract(3, "days")
          .format("YYYY-MM-DD HH") + ":00:00";
      this.tideStaEnd =
        this.$moment()
          .subtract(0, "days")
          .format("YYYY-MM-DD HH") + ":00:00";
    }
    this.getYearOption();
    // this.getEarlyType();
    this.getArea().then(() => {
      this.$nextTick(() => {
        if (this.code) {
          switch (this.code) {
            case "水库":
              this.rsvrProjList();
              break;
            case "水闸":
              this.wagaProjList();
              break;
            case "河网":
              this.riverNetworkList();
              break;
            case "潮位":
              this.tideList();
              break;
            case "病险工程":
              this.dilapProjList();
              break;
            case "风险隐患":
              this.projRiskList();
              this.riskTypeStatistics();
              break;
            case "海塘":
              this.seawallList();
              this.seawallProjStats();
              break;
            case "短临预报":
              this.rainfallPredictGrid();
              break;
            case "水库分析":
              this.reservoirAnalysisInvoking();
              break;
            case "水闸分析":
              this.lockAnalysisInvoking();
              break;
            case "河网分析":
              this.riverNetworkAnalysis();
              break;
            case "潮位分析":
              this.waterAnalysisInvoking();
              break;
            case "防汛仓库":
              this.warehouseList();
              this._getDict("warehouseType");
              break;
            case "防汛队伍":
              this.ranksList();
              this._getDict("teamType");
              break;
            case "视频监控":
              this.videoList();
              break;
            case "积水":
              this.waterLoggingList();
              break;
            case "洪水风险图":
              this.floodRiskList();
              break;
            case "降水量":
              this.precipitationList();
              this.stats4WrbRainfallList();
              break;
            case "水资源量":
              this.waterResourceList();
              break;
            case "供水量":
              this.waterRupplyList();
							this.stats4WrbWaterSupplyWrbWaterSupply();
              break;
            case "用水量":
              this.waterUseList();
              break;
            case "水库蓄水量":
              this.reservoirStorageList();
              break;
            case "水资源承载能力":
              this.changeActiveTabCarry(this.activeTabCarry);
              break;
            case "流域生态流量":
              this.getSectFlowList();
              break;
            case "规划":
              this.getPlanningList();
              break;
            case "预警信息":
              this.getEarlyDetail();
              break;
            case "水源地":
              this.getWaterSourList();
              break;
            case "水厂":
              this.AttSwhsBaseLoadPage();
              break;
            case "水厂运行状态":
              this.AttSwhsBaseLoadPage();
              break;
            case "水量预警":
              this.getWaterWarnList();
              break;
            case "珊溪水质监测":
              this.getSXWaterList();
              break;
            case "巡查":
              this.happeningListJson();
              this.patrolInfoPatrolChaosHappStatic();
              break;
            case "取水户":
              this.stat4ListWiu();
              this.getWaterIntakeList();
              break;
            case "批后监管":
              this.homeOverView();
              this.getApproSuperList();
              break;
            case "水域变化":
              this.cPositionStatisticsWaterArea();
              this.getWaterChangeList();
              break;
            case "清四乱":
              this.statisProblemC();
              this.getFourChaosList();
              break;
            case "美丽河湖":
              this.eachProjectStatistics();
              switch (this.activeTabBeauty) {
                case "美丽河湖":
                  this.getBeautifulRevierList();
                  break;
                case "中小河流":
                  this.AttSmallMediumRiver();
                  break;
                case "水美乡镇":
                  this.AttBeautVillage();
                  break;
                case "农村池塘":
                  this.AttRuralPond();
                  break;
              }
              break;
            case "水电站":
              this.mainTotalNum();
              this.getHydropowerStationList();
              break;
            case "河长巡查":
              this.statsByQueryCondition();
              this.queryPatrolProblem();
              break;
          }
        }
      });
    });
  },
  methods: {
    calcInputPlaceholder() {
      const placeholderMap = { defualt: `请输入${this.code}名称`, 积水: `请输入积水点名称` };
      return placeholderMap[this.code in placeholderMap ? this.code : "defualt"];
    },
    changeFullscreen() {
      this.fullscreen = !this.fullscreen;
      this.$refs.multipleTable.doLayout();
    },
    // 获取预警类型
    async getEarlyType() {
      this.earlyTypeOption = [];
      await inforApi.getEarlyType().then(res => {
        res.data.forEach(item => {
          let obj = {
            value: item,
            label: item
          };
          this.earlyTypeOption.push(obj);
        });
      });
    },
    changeVal() {
      this.durationRainfall = [];
      this.realTimeData = [];
    },
    // 分页条数
    handleSizeChange(val) {
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
      await disasterPreventionApi.stats4PageSeawall(obj).then(res => {
        if (res.code == 0) {
          this.seawallData = res.data;
        }
      })
    },
    /**
     * 第三场景 模糊查询
     */
    queryVal() {
      switch (this.code) {
        case "水库分析":
          this.reservoirAnalysisInvoking();
          break;
        case "水闸分析":
          this.lockAnalysisInvoking();
          break;
        case "河网分析":
          this.riverNetworkAnalysis();
          break;
        case "潮位分析":
          this.waterAnalysisInvoking();
          break;
        case "积水":
          this.waterLoggingList();
          break;
        case "防汛仓库":
          this.warehouseList();
          break;
        case "防汛队伍":
          this.ranksList();
          break;
        case "视频监控":
          this.videoList();
          break;
        case "流域生态流量":
          this.getSectFlowList();
          break;
        case "规划":
          this.getPlanningList();
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
        .then(res => {
          if (res.code == 0) {
            this.reservoirAnalysisData = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
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
        .then(res => {
          if (res.code == 0) {
            this.lockAnalysisData = res.data.list;
          }
          this.loading = false;
        })
        .catch(error => {
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
        .then(res => {
          if (res.code == 0) {
            this.riverNetworkData = res.data.list;
          }
          this.loading = false;
        })
        .catch(error => {
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
        .then(res => {
          if (res.code == 0) {
            this.waterAnalysisData = res.data.data.list;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    /**
     * 弹窗关闭
     */
    handleClose() {
      this.$emit("close");
    },
    /**
     * 开启数
     */
    openGates() {
      this.count++;
      this.numTime = "1";
      if (this.count % 2) {
        this.filterOpenGate = true;
        this.wagaProjList();
      } else {
        this.filterOpenGate = false;
        this.wagaProjList();
      }
    },
    /**
     * 累计泄洪时间
     */
    changeHours() {
      this.wagaProjList();
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
        .then(res => {
          if (res.code == 0) {
            this.seawallProjData = res.data.list;
            this.totalPage = res.data.totalRows;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    /**
     * 行的回调函数
     */
    rowClassName({ row }) {
      return row.overLimitVal > 0 ? "warm-row" : "";
    },
    /**
     * 水库字段排序
     */
    sortChange(val) {
      if (val.order) {
        if (val.order == "ascending") {
          this.orderColumn = val.prop;
          this.ascOrDesc = false;
        } else {
          this.orderColumn = val.prop;
          this.ascOrDesc = true;
        }
      } else {
        this.orderColumn = "";
        this.ascOrDesc = "";
      }
      if (this.code) {
        switch (this.code) {
          case "水库":
            this.rsvrProjList();
            break;
          case "水闸":
            this.wagaProjList();
            break;
          case "河网":
            this.riverNetworkList();
            break;
          case "潮位":
            this.tideList();
            break;
          case "病险工程":
            this.dilapProjList();
            break;
          case "海塘":
            this.seawallList();
            this.seawallProjStats();
            break;
          case "降水量":
            this.precipitationList();
            this.stats4WrbRainfallList();
            break;
          case "水资源量":
            this.waterResourceList();
            break;
          case "供水量":
            this.waterRupplyList();
						this.stats4WrbWaterSupplyWrbWaterSupply();
            break;
          case "用水量":
            this.waterUseList();
            break;
          case "水库蓄水量":
            this.reservoirStorageList();
            break;
          case "水资源承载能力":
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
        totalNum: res.data[0].total,
      }
      res.data.forEach(item => {
        if (item.sbSignName == '隐患点') {
          this.riskCount.riskNum = item.num;
        } else if (item.sbSignName == '高风险点') {
          this.riskCount.highRiskNum = item.num;
        } else if (item.sbSignName == '薄弱点') {
          this.riskCount.weakNum = item.num;
        }
      })
    },
    /**
     * 风险隐患列表
     */
    async projRiskList() {
      let arr = "";
      // let str = "";
      if (this.projectTypeVal.length == 2) {
        arr = this.projectTypeVal[0] + "-" + this.projectTypeVal[1];
      } else {
        arr = "";
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
        .then(res => {
          if (res.code == 0) {
            this.potentialRisksData = res.data.list;
            this.totalPage = res.data.totalRows;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    /**
     * 短临预报
     */
    async rainfallPredictGrid() {
      this.loading = true;
      let obj = {
        type: this.rainfallType
      };
      await disasterPreventionApi
        .rainfallPredictGrid(obj)
        .then(res => {
          if (res.code == 0) {
            this.inTheShortData = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
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
        .then(res => {
          if (res.code == 0) {
            this.dangerouslyData = res.data.list;
          }
          this.loading = false;
        })
        .catch(error => {
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
        .then(res => {
          if (res.code == 0) {
            this.seaLevelData = res.data.list;
            this.seaLevelList = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
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
        .then(res => {
          if (res.code == 0) {
            this.riverwayData = res.data.list;
            this.riverwayList = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    /**
     * 水闸接口
     */
    async wagaProjList() {
      this.loading = true;
      let obj = {
        basin: this.administrativeValue,
        areaCode: this.administrationValue,
        filterOpenGate: this.filterOpenGate,
        engScal: this.engScalesValue,
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        hours: this.numTime,
        wagaName: this.searchVal
      };
      await disasterPreventionApi
        .wagaProjList(obj)
        .then(res => {
          if (res.code == 0) {
            this.waterGateData = res.data.list;
            this.reservoirStatistical = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    /**
     * 选中事件
     */
    selectVal() {
      this.numTime = "1";
      switch (this.code) {
        case "水库":
          this.rsvrProjList();
          break;
        case "水闸":
          this.wagaProjList();
          break;
        case "河网":
          this.riverNetworkList();
          break;
        case "潮位":
          this.tideList();
          break;
        case "病险工程":
          this.dilapProjList();
          break;
        case "风险隐患":
          this.projRiskList();
          break;
        case "海塘":
          this.seawallList();
          this.seawallProjStats();
          break;
        case "短临预报":
          this.rainfallPredictGrid();
          break;
        case "防汛仓库":
          this.warehouseList();
          break;
        case "防汛队伍":
          this.ranksList();
          break;
        case "视频监控":
          this.videoList();
          break;
        case "积水":
          this.waterLoggingList();
          break;
        case "洪水风险图":
          this.floodRiskList();
          break;
        case "降水量":
          this.precipitationList();
          this.stats4WrbRainfallList();
          break;
        case "水资源量":
          this.waterResourceList();
          break;
        case "供水量":
          this.waterRupplyList();
					this.stats4WrbWaterSupplyWrbWaterSupply();
          break;
        case "用水量":
          this.waterUseList();
          break;
        case "水资源承载能力":
          this.changeActiveTabCarry(this.activeTabCarry);
          break;
        case "水库蓄水量":
          this.reservoirStorageList();
          break;
        case "流域生态流量":
          this.getSectFlowList();
          break;
        case "规划":
          this.getPlanningList();
          break;
        case "取水户":
          this.getWaterIntakeList();
          break;
        case "批后监管":
          this.getApproSuperList();
          break;
        case "水域变化":
          this.getWaterChangeList();
          break;
        case "清四乱":
          this.getFourChaosList();
          break;
        case "美丽河湖":
          switch (this.activeTabBeauty) {
            case "美丽河湖":
              this.getBeautifulRevierList();
              break;
            case "中小河流":
              this.AttSmallMediumRiver();
              break;
            case "水美乡镇":
              this.AttBeautVillage();
              break;
            case "农村池塘":
              this.AttRuralPond();
              break;
          }
          break;
        case "水电站":
          this.getHydropowerStationList();
          break;
        case "河长巡查":
          this.queryPatrolProblem();
          break;
      }
    },
    /**
     * 短临降雨
     */
    Formatter(row, column, value, index) {
      // if (!value || value == "") return "-";
      if (value != null) {
        let valueFixed = String(value);
        if (valueFixed.split(",").length > 1) {
          valueFixed = valueFixed.split(",")[0];
        }
        valueFixed = Number(valueFixed).toFixed(1);
        return valueFixed;
      } else {
        return "-";
      }
    },
    /**
     * 表格数据格式化
     */
    toFixedFormatter(row, column, value, index) {
      if (value != null) {
        return value;
      } else {
        return "-";
      }
    },
    /**
     * 保留2位小数
     */
    toFixedTwoFormatter(row, column, value, index) {
      if (value) {
        return Number(value).toFixed(2);
      } else {
        return value;
      }
    },
    /**
     * 行政区划
     */
    async getArea() {
      let obj = {
        areaCode: 3303,
        level: 3
      };
      await disasterPreventionApi.getArea(obj).then(res => {
        if (res.code == 0) {
          this.administration = res.data;
        }
      });
    },
    /**
     * 水库详情数据
     */
    async rsvrProjList(val) {
      this.loading = true;
      let obj = {
        resAdcd: this.administrationValue, // 行政区划编码
        resBas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
        engScales: this.projectScaleValue,
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        resName: this.searchVal,
        drainage24H: this.mediumSized == "大中型水库" ? true : false,
        drainage48H: this.mediumSized == "大中型水库" ? true : false,
        drainage72H: this.mediumSized == "大中型水库" ? true : false
      };
      this.stats4RsvrUnitList(obj);
      await disasterPreventionApi
        .rsvrUnitList(obj)
        .then(res => {
          if (res.code == 0) {
            this.WaterloggingData = res.data.list;
            this.totalPage = res.data.totalRows;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    async stats4RsvrUnitList(val) {
      await disasterPreventionApi
        .stats4RsvrUnitList(val)
        .then(res => {
          if (res.code == 0) {
            this.statisticsData = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
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
        .then(res => {
          if (res.code == 0) {
            this.warehouseData = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
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
        .then(res => {
          if (res.code == 0) {
            this.ranksData = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
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
        // stiStart: this.stiStart,
        // stiEnd: this.stiEnd
      };
      this.stats4PageCamera(obj);
      await disasterPreventionApi
        .listCamera(obj)
        .then(res => {
          if (res.code == 0) {
            this.videoData = res.data.list;
            this.totalPage = res.data.totalRows;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    // 时间筛选
    dateChange() {
      this.stiStart = this.value1[0];
      this.stiEnd = this.value1[1];
      this.videoList();
    },
    /*
     *
     * 公共导出 开始
     *
     * */

    async commonExportExcel(obj, url) {
      let userInfo = localData("get", "userInfo");
      let { accessToken } = userInfo ? userInfo : { accessToken: "" };
      axios({
        url: url,
        method: "post",
        data: obj,
        responseType: "blob",
        headers: {
          authorization: accessToken
        }
      })
        .then(res => {
          const content = res.data;
          const blob = new Blob([content], { type: "application/msword" });
          const link = document.createElement("a"); // 创建a标签
          let filename = res.headers["content-disposition"];
          filename = filename ? filename.substr(filename.indexOf("=") + 1) : "";
          filename = decodeURIComponent(filename);
          link.download = filename;
          link.style.display = "none";
          link.href = URL.createObjectURL(blob);
          document.body.appendChild(link);
          link.click(); // 执行下载
          URL.revokeObjectURL(link.href); // 释放url
          document.body.removeChild(link); // 释放标签
          this.exportLoading = false;
        })
        .catch(error => {
          this.exportLoading = false;
        });
    },
    /*
     *
     * 公共导出 结束
     *
     * */

    // 导出
    exportExcel(val) {
      this.exportLoading = true;
      if (val == "视频监控") {
        let obj = {
          areaCode: this.administrationValue, // 行政区划编码
          projectType: this.projType,
          cameraName: this.NameVal,
          status: this.statusType,
          pageSize: this.pageSize,
          pageNum: this.pageNum
          // stiStart: this.stiStart,
          // stiEnd: this.stiEnd
        };
        let str = `${config.baseUrl}/project-manage/cameraOps/exportPageCamera`;
        this.commonExportExcel(obj, str);
      } else if (val == "短临预报") {
        let obj = {
          type: this.rainfallType
        };
        let str = `${config.baseUrl}/meteorological-visualization/weatherPredict/exportRainfallPredictGrid`;
        this.commonExportExcel(obj, str);
      } else if (val == "河网") {
        let obj = {
          adcd: this.administrationValue, // 行政区划编码
          bas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          mainStName: this.searchVal
        };
        let str = `${config.baseUrl}/graph/waterAndRain/exportRivernet`;
        this.commonExportExcel(obj, str);
      } else if (val == "风险隐患") {
        let arr = "";
        let str = "";
        if (this.projectTypeVal.length == 2) {
          arr = this.projectTypeVal[0] + "-" + this.projectTypeVal[1];
        } else {
          arr = "";
        }
        if (this.district.length == 3) {
          str = this.district[2];
        } else if (this.district.length == 2) {
          str = this.district[1];
        } else {
          str = "";
        }
        let obj = {
          projName: this.projectNameVal, // 工程名称
          projType: arr, // 工程类型
          sbSign: this.riskTypeVal, // 风险类型
          adcdDuty: str ? str.substring(1, 7) : "", // 行政区划
          property: this.discoveryChannel, // 发现渠道
          recCondition: this.reorganizeSituation,
          startTime: this.affiliatedUnit[0], // 发现日期起始点
          endTime: this.affiliatedUnit[1],
          pageSize: this.pageSize,
          pageNum: this.pageNum
        };
        let opt = `${config.baseUrl}/big-screen/busRiskList/exportBusRisk`;
        this.commonExportExcel(obj, opt);
      } else if (val == "海塘") {
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
        let str = `${config.baseUrl}/project-manage/seawall/exportSeawall`;
        this.commonExportExcel(obj, str);
      } else if (val == "积水") {
        let obj = {
          areaCode: this.administrationValue, // 行政区划编码
          ponding: this.waterLoggingType, // 是否有积水
          name: this.searchVal
        };
        let str = `${config.baseUrl}/flood-protect/waterLogging/exportFloodPoint`;
        this.commonExportExcel(obj, str);
      } else if (val == "水闸") {
        let obj = {
          basin: this.administrativeValue,
          areaCode: this.administrationValue,
          filterOpenGate: this.filterOpenGate,
          engScal: this.engScalesValue,
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          hours: this.numTime,
          wagaName: this.searchVal
        };
        let str = `${config.baseUrl}/project-manage/waga/exportWrpWagaUnitTable`;
        this.commonExportExcel(obj, str);
      } else if (this.mediumSized == "大中型水库") {
        let obj = {
          resAdcd: this.administrationValue, // 行政区划编码
          resBas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
          engScales: this.projectScaleValue,
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          resName: this.searchVal,
          exportStrategy: 1,
          drainage24H: this.mediumSized == "大中型水库" ? true : false,
          drainage48H: this.mediumSized == "大中型水库" ? true : false,
          drainage72H: this.mediumSized == "大中型水库" ? true : false
        };
        let str = `${config.baseUrl}/project-manage/rsvr/exportRsvr`;
        this.commonExportExcel(obj, str);
      } else if (val == "水库") {
        let obj = {
          resAdcd: this.administrationValue, // 行政区划编码
          resBas: this.administrativeValue, // 流域 飞云江、瓯江、鳌江
          engScales: this.projectScaleValue,
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          exportStrategy: 0,
          resName: this.searchVal
        };
        let str = `${config.baseUrl}/project-manage/rsvr/exportRsvr`;
        this.commonExportExcel(obj, str);
      } else if (val == "洪水风险图") {
        let obj = {
          designStandard: this.floodLevel ? `${this.floodLevel}年一遇` : ""
        };
        let str = `${config.baseUrl}/hydrology-digitization/FloodRiskDisasterData/export`;
        this.commonExportExcel(obj, str);
      } else if (val == "防汛仓库") {
        let obj = {
          areaCode: this.administrationValue, // 行政区划编码
          typeId: this.ranksType,
          name: this.searchVal,
          exportStrategy: 0
        };
        let str = `${config.baseUrl}/flood-protect/warehouse/exportWareHouses`;
        this.commonExportExcel(obj, str);
      } else if (val == "防汛队伍") {
        let obj = {
          areaCode: this.administrationValue, // 行政区划编码
          typeId: this.warehouseType,
          name: this.searchVal,
          exportStrategy: 0
        };
        let str = `${config.baseUrl}/flood-protect/team/exportTeams`;
        this.commonExportExcel(obj, str);
      } else if (val == "供水量") {
        let obj = {
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          // year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
        };
        let str = `${config.baseUrl}/wra/wrbWaterSupply/exportWrabWaterSupply`;
        this.commonExportExcel(obj, str);
      } else if (val == "用水量") {
        let obj = {
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
        };
        let str = `${config.baseUrl}/flood-protect/team/exportTeams`;
        this.commonExportExcel(obj, str);
      } else if (val == "降水量") {
        let obj = {
          orderColumn: this.orderColumn,
          ascOrDesc: this.ascOrDesc,
          startYear: this.planStart,
          endYear: this.planEnd
        };
        let str = `${config.baseUrl}/wra/wrbRainfall/exportWrbRainfallList`;
        this.commonExportExcel(obj, str);
      }
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
        .then(res => {
          if (res.code == 0) {
            this.waterLoggingData = res.data;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    /**
     * 洪水风险图
     */
    async floodRiskList() {
      this.loading = true;
      let obj = {
        designStandard: this.floodLevel ? `${this.floodLevel}年一遇` : ""
      };
      await disasterPreventionApi
        .floodRiskList(obj)
        .then(res => {
          if (res.code == 0) {
            this.floodRiskData = res.data.list;
          }
          this.loading = false;
        })
        .catch(error => {
          this.loading = false;
        });
    },
    // 枚举
    async _getDict(type) {
      let res = await getDict(type);
      if (res.code == 0) {
        if (type == "warehouseType") {
          this.warehouseOptions = res.data;
        } else if (type == "teamType") {
          this.ranksOptions = res.data;
        }
      }
    },

    /**
     * 公报
     */
    // 获取年份选择下拉内容
    getYearOption() {
      let date = new Date();
      let y = date.getFullYear();
      // this.yearOption = [{ label: "全部", value: "" }];
      for (let i = y; i >= 1990; i--) {
        this.yearOption.push({
          label: i + " 年",
          value: i + " 年"
        });
      }
    },
    // 降水量接口
    async precipitationList() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        startYear: this.planStart,
        endYear: this.planEnd
      };
      this.loading = true;
      await waterResourceApi.table4WrbRainfall(opt).then(res => {
        this.precipitationData = res.data;
        this.loading = false;
      });
    },
    // 降水量 表头统计
    async stats4WrbRainfallList() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        startYear: this.planStart,
        endYear: this.planEnd
      };
      await waterResourceApi.stats4WrbRainfallList(opt).then(res => {
        this.precipitationVal = res.data;
      });
    },
    // 水资源接口
    async waterResourceList() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      this.loading = true;
      await waterResourceApi.table4WrbWra(opt).then(res => {
        this.waterResourceData = res.data;
        this.checkYear = res.data[0] ? res.data[0].year + " 年" : this.checkYear;
        this.loading = false;
      });
    },
    // 供水量详表tab切换
    changeActiveTab(val) {
      if (val == "供水工程") return; // 供水工程暂不可点
      this.activeTab = val;
      this.waterRupplyList();
			this.stats4WrbWaterSupplyWrbWaterSupply();
    },
		// 供水量表头接口
		async stats4WrbWaterSupplyWrbWaterSupply() {
			let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        // year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      await waterResourceApi.stats4WrbWaterSupplyWrbWaterSupply(opt).then(res => {
        this.WrbWaterSupplyData = res.data;
      });
		},
    // 供水量量详表接口
    async waterRupplyList() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        // year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      this.loading = true;
      await waterResourceApi.table4WrbWaterSupply(opt).then(res => {
        this.waterRupplyData = res.data;
        this.loading = false;
      });
    },
    // 用水量详表接口
    async waterUseList() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      this.loading = true;
      await waterResourceApi.table4WrbWaterConsume(opt).then(res => {
        this.waterUseData = res.data;
        this.checkYear = res.data[0] ? res.data[0].year + " 年" : this.checkYear;
        this.loading = false;
      });
    },
    // 水库蓄水量详表接口
    async reservoirStorageList() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        resName: this.searchVal
      };
      this.loading = true;
      await waterResourceApi.table4WrbRsvrStore(opt).then(res => {
        this.reservoirStorageData = res.data;
        this.loading = false;
      });
    },
    // 水资源承载能力详表tab切换
    changeActiveTabCarry(val) {
      this.activeTabCarry = val;
      switch (val) {
        case "水量评价":
          this.waterConsumeEvaluation();
          break;
        case "水质评价":
          this.waterQualityEvaluation();
          break;
        case "经济评价":
          this.ecoAppraisalTable();
          break;
        case "人口评价":
          this.popAppraisalTable();
          break;
        case "美丽河湖":
          this.getBeautifulRevierList();
          break;
      }
    },
    // 水资源承载能力详表接口 水量评价
    async waterConsumeEvaluation() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      this.loading = true;
      await waterResourceApi.waterConsumeEvaluation(opt).then(res => {
        this.waterResCarryData = res.data.tableData;
        this.checkYear = res.data.tableData[0] ? res.data.tableData[0].year + " 年" : this.checkYear;
        this.loading = false;
      });
    },
    // 水资源承载能力详表接口 水质评价
    async waterQualityEvaluation() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      this.loading = true;
      await waterResourceApi.waterQualityEvaluation(opt).then(res => {
        this.waterResCarryData = res.data.tableData;
        this.loading = false;
      });
    },
    // 水资源承载能力详表接口 经济评价
    async ecoAppraisalTable() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      this.loading = true;
      await waterResourceApi.ecoAppraisalTable(opt).then(res => {
        this.waterResCarryData = res.data;
        this.loading = false;
      });
    },
    // 水资源承载能力详表接口 人口评价
    async popAppraisalTable() {
      let opt = {
        orderColumn: this.orderColumn,
        ascOrDesc: this.ascOrDesc,
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : ""
      };
      this.loading = true;
      await waterResourceApi.popAppraisalTable(opt).then(res => {
        this.waterResCarryData = res.data;
        this.loading = false;
      });
    },
    //流域生态流量
    async getSectFlowList() {
      let opt = {
        bas: this.administrativeValue,
        sectName: this.searchVal,
        warnType: this.checkYear
      };
      this.loading = true;
      await waterResourceApi.getSectFlowList(opt).then(res => {
        this.flowData = res.data;
        this.loading = false;
      });
    },
    // 预警信息
    async getEarlyDetail() {
      let opt = {
        messageType: "预警",
        type: this.earlyType,
        content: this.earlySearchVal,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await inforApi.getEarlyDetail(opt).then(res => {
        this.earlyData = res.data.datas.list;
        this.totalPage = res.data.total;
        this.loading = false;
      });
    },
    // 分页
    pageChange(val) {
      this.pageNum = val;
      switch (this.code) {
        case "预警信息":
          this.getEarlyDetail();
          break;
        case "水域变化":
          this.getWaterChangeList();
          break;
        case "批后监管":
          this.getApproSuperList();
          break;
        case "清四乱":
          this.getFourChaosList();
          break;
        case "美丽河湖":
          switch (this.activeTabBeauty) {
            case "美丽河湖":
              this.getBeautifulRevierList();
              break;
            case "中小河流":
              this.AttSmallMediumRiver();
              break;
            case "水美乡镇":
              this.AttBeautVillage();
              break;
            case "农村池塘":
              this.AttRuralPond();
              break;
          }
          break;
        case "水电站":
          this.getHydropowerStationList();
          break;
        case "河长巡查":
          this.queryPatrolProblem();
          break;
        case "视频监控":
          this.videoList();
          break;
        case "风险隐患":
          this.projRiskList();
          break;
        case "海塘":
          this.seawallList();
          this.seawallProjStats();
          break;
        case "水库":
          this.rsvrProjList();
          break;
      }
    },
    //规划
    async getPlanningList() {
      let [title, ptId] = this.mediumSized.split("|");
      let opt = {
        ptId,
        areaCode: this.administrationValue,
        startYear: this.planStart,
        endYear: this.planEnd,
        projName: this.searchVal
      };
      planDigitizationApi.getPageDevPlanProj(opt).then(res => {
        if (res.code == 0) {
          this.planningData.data = res.data.list;
        }
      });
      planDigitizationApi.getDevPlanProjStats(opt).then(res => {
        if (res.code == 0) {
          const data = res.data;
          this.planningData.stats = this.planningData.stats.map(item => {
            if (item.auto) {
              return {
                ...item,
                label: `${title}投资`,
                num: data.amountInvestDevelop | "-"
              };
            }
            if (item.label == "总投资") {
              return { ...item, num: data.amountInvestTotal | "-" };
            }
            if (item.label == "项目条数") {
              return { ...item, num: data.projectNum | "-" };
            }
            if (item.label == "已完成投资") {
              return { ...item, num: data.amountInvested | "-" };
            }
            if (item.label == "结转投资") {
              return { ...item, num: data.amountInvestIndirect | "-" };
            }
            return item;
          });
        }
      });
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
      this.waterSuantityStatistic("1");
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
      this.waterSuantityStatistic("2");
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
    // 设置水量预警等级颜色
    setWaterWarnColor(val) {
      switch (val) {
        case "暂无风险":
          return "#19AF47";
        case "黄色预警":
          return "#e89100";
        case "橙色预警":
          return "#ff551f";
        case "红色预警":
          return "#e80000";
      }
    },
    // 获取珊溪水质监测列表
    getSXWaterList() { },
    // 获取珊溪统计数据
    async patrolInfoPatrolChaosHappStatic() {
      await waterResourceApi.patrolInfoPatrolChaosHappStatic().then(res => {
        if (res.data) this.countList = res.data;
      });
    },
    // 珊溪巡查列表
    async happeningListJson() {
      let opt = {
        chaosType: this.chaosType,
        chaosStatus: this.chaosStatus,
        chaosName: this.chaosName
        // pageNum: 3,
        // pageSize: 10,
        // fetchAll: false,
      };
      this.loading = true;
      await waterResourceApi.happeningListJson(opt).then(res => {
        this.sxCheckData = res.data;
        this.loading = false;
      });
    },
    // 取水户 统计
    async stat4ListWiu() {
      if (this.code != "取水户") return;
      await waterResourceApi.stat4ListWiu().then(res => {
        this.wtaerIntakeCount = res.data;
      });
    },
    // 取水户
    async getWaterIntakeList() {
      if (this.code != "取水户") return;
      let opt = {
        areaCode: this.administrationValue,
        warning: this.wtaerIntakeWarnType,
        wiuType: this.wtaerIntakeType
      };
      this.loading = true;
      await waterResourceApi.listWiu(opt).then(res => {
        this.wtaerIntakeData = res.data;
        this.loading = false;
      });
    },
    // 批后监管 统计
    async homeOverView() {
      if (this.code != "批后监管") return;
      await waterResourceApi.homeOverView().then(res => {
        this.approSupeCount = res.data;
      });
    },
    // 批后监管
    async getApproSuperList() {
      if (this.code != "批后监管") return;
      let opt = {
        areaCode: this.administrationValue,
        riverLevel: this.riverType,
        regulatoryStatus: this.supercisionStatu,
        projectStage: this.projStage,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await waterResourceApi.projectList(opt).then(res => {
        this.approSuperData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 水域变化 统计
    async cPositionStatisticsWaterArea() {
      if (this.code != "水域变化") return;
      await waterResourceApi.cPositionStatisticsWaterArea().then(res => {
        this.waterChangeCount = res.data;
      });
    },
    // 水域变化
    async getWaterChangeList() {
      if (this.code != "水域变化") return;
      let opt = {
        // areaName: "",
        // waterArea: null,
        // type: "",
        year: Number(this.checkYear.substring(0, 4)) ? Number(this.checkYear.substring(0, 4)) : "",
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await waterResourceApi.loadPage(opt).then(res => {
        this.wtaerChangeData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 清四乱 统计
    async statisProblemC() {
      if (this.code != "清四乱") return;
      await waterResourceApi.statisProblemC().then(res => {
        this.fourChaosCount = res.data;
      });
    },
    // 清四乱
    async getFourChaosList() {
      if (this.code != "清四乱") return;
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
      await waterResourceApi.loadPageFourChaos(opt).then(res => {
        this.fourChaosData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 美丽河湖tab切换
    changeActiveTabBeauty(val) {
      this.activeTabBeauty = val;
      this.projStatu = "";
      this.pageNum = 1;
      this.projStatu = "";
      this.administrationValue = "";
      this.eachProjectStatistics();
      switch (val) {
        case "美丽河湖":
          this.getBeautifulRevierList();
          break;
        case "中小河流":
          this.AttSmallMediumRiver();
          break;
        case "水美乡镇":
          this.AttBeautVillage();
          break;
        case "农村池塘":
          this.AttRuralPond();
          break;
      }
    },
    // 美丽河湖 统计
    async eachProjectStatistics() {
      if (this.code != "美丽河湖") return;
      let opt = {
        type: this.activeTabBeauty
      };
      await waterResourceApi.eachProjectStatistics(opt).then(res => {
        this.beautyCount = res.data;
      });
    },
    // 美丽河湖数据 美丽河湖
    async getBeautifulRevierList() {
      if (this.code != "美丽河湖") return;
      let opt = {
        containWenzhou: "1",
        projectStatus: this.projStatu,
        county: this.administrationValue,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        projectName: this.searchVal
      };
      this.loading = true;
      await waterResourceApi.getBeautifulList(opt).then(res => {
        this.beautyData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 美丽河湖数据 中小河流
    async AttSmallMediumRiver() {
      if (this.code != "美丽河湖") return;
      let opt = {
        containWenzhou: "1",
        projectStatus: this.projStatu,
        county: this.administrationValue,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        projectName: this.searchVal
      };
      this.loading = true;
      await waterResourceApi.AttSmallMediumRiver(opt).then(res => {
        this.beautyData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 美丽河湖数据 水美乡镇
    async AttBeautVillage() {
      if (this.code != "美丽河湖") return;
      let opt = {
        containWenzhou: "1",
        projectStatus: this.projStatu,
        county: this.administrationValue,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        projectName: this.searchVal
      };
      this.loading = true;
      await waterResourceApi.AttBeautVillage(opt).then(res => {
        this.beautyData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 美丽河湖数据 农村池塘
    async AttRuralPond() {
      if (this.code != "美丽河湖") return;
      let opt = {
        containWenzhou: "1",
        projectStatus: this.projStatu,
        county: this.administrationValue,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        projectName: this.searchVal
      };
      this.loading = true;
      await waterResourceApi.AttRuralPond(opt).then(res => {
        this.beautyData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 水电站 统计
    async mainTotalNum() {
      await waterResourceApi.mainTotalNum().then(res => {
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
      await waterResourceApi.hystList(opt).then(res => {
        this.hydroStaData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 水电站类型转换
    getHydroType(val) {
      switch (val) {
        case "0":
          return "混合式";
        case "1":
          return "坝式(河床)";
        case "2":
          return "饮水式";
        case "3":
          return "坝式(坝后)";
        case "4":
          return "抽水蓄能";
      }
    },
    // 河长巡查 统计
    async statsByQueryCondition() {
      await waterResourceApi.statsByQueryCondition().then(res => {
        this.patrolCount = res.data;
      });
    },
    // 河长巡查 数据
    async queryPatrolProblem() {
      let opt = {
        level: this.question,
        areaCode: this.administrationValue,
        startTime: this.patrolDate && this.patrolDate.length > 0 ? this.patrolDate[0] + " 00:00:00" : "",
        endTime: this.patrolDate && this.patrolDate.length > 0 ? this.patrolDate[1] + " 00:00:00" : "",
        state: this.dealStatu,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      };
      this.loading = true;
      await waterResourceApi.queryPatrolProblem(opt).then(res => {
        this.patrolData = res.data.list;
        this.totalPage = res.data.totalRows;
        this.loading = false;
      });
    },
    // 河道问题程度转换
    getProblemType(val) {
      switch (val) {
        case 0:
          return "轻微";
        case 1:
          return "一般问题";
        case 2:
          return "重大问题";
      }
    },
    // 河道问题状态转换
    getProblemStatu(val) {
      switch (val) {
        case 1:
          return "未受理";
        case 2:
          return "处理中";
        case 3:
          return "已处理待评价";
        case 4:
          return "已处理已评价";
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.mg-l16 {
  margin-left: 16px;
}
.fr {
  float: right;
}
.primary-dialog {
  ::v-deep .el-dialog {
    @include flexbox;
    @include flexflow(column, nowrap);
    height: 680px;
    &.is-fullscreen {
      height: 100%;
      overflow: hidden;
    }
    .el-dialog__header {
      padding: 2px;
      .primary-dialog__title {
        position: relative;
        .primary-dialog__title--label {
          width: 80%;
          height: 32px;
          color: #fff;
          font-size: 16px;
          letter-spacing: 0;
          margin-left: 15px;
          font-weight: bold;
          line-height: 35px;
          padding-left: 56px;
        }
        .full-screen-icon {
          position: absolute;
          right: 40px;
          color: #00c1ff;
          cursor: pointer;
          top: 15px;
        }
      }
    }
    .el-dialog__body {
      padding: 2px;
      padding-top: 0;
      border-bottom: 0.0625rem solid #00c1ff;
      @include flex(1);
      .primary-dialog__content {
        @include flexbox;
        @include flexflow(column, nowrap);
        height: 100%;
        // height: 680px;
        border-top: 0;
        padding: 0 16px;
        box-shadow: inset 0 4px 30px 0 rgba(35, 81, 239, 0.25);
        background-image: linear-gradient(180deg, #1b3047 0%, #091d42 100%);

        // 统计样式
        .statistics-sty {
          padding: 10px 0;
          width: 100%;
          display: flex;
          justify-content: left;
          border-bottom: 1px solid rgba($color: #409eff, $alpha: 0.4);
          > div {
            // margin-right: 50px;
            padding: 10px 0;
            margin-right: 20px;
            display: flex;
            justify-content: space-between;
            > p {
              padding-top: 5px;
              margin-right: 15px;
            }
            > div {
              > p:nth-child(1) {
                font-family: PingFangSC-Regular;
                font-size: 15px;
                color: #ffffff;
                letter-spacing: 0;
                line-height: 24px;
                font-weight: 400;
                margin-bottom: 5px;
              }
              > p:nth-child(2) {
                font-family: PingFangSC-Medium;
                font-size: 14px;
                color: #fff;
                letter-spacing: 0;
                line-height: 24px;
                font-weight: 500;
                > span {
                  font-size: 24px;
                  color: #56fefe;
                }
              }
            }
          }
        }
        // 查询样式
        .query-sty {
          color: #fff;
          .derive-sty {
            padding: 0 15px;
            text-align: center;
            border-radius: 4px;
            line-height: 30px;
            float: right;
            color: #fff;
            cursor: pointer;
            border-color: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            ) !important;
            background: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
          }
          .aa {
            margin-right: 0px;
            margin-bottom: 10px;
          }
          .demo-input-suffix {
            display: flex;
            justify-content: left;
            align-items: center;
          }
        }

        .tab-list-sty {
          height: 100%;
          padding-bottom: 20px;
          overflow-y: auto;
        }
      }
    }
  }
  ::v-deep.el-input-number {
    width: 5.25rem;
  }
  ::v-deep.el-input__inner:hover,
  .el-select:hover .el-input__inner {
    border: 1px solid #fff;
  }
  ::v-deep.el-input__inner {
    height: 2rem;
    background: rgba(24, 47, 90, 0.1);
    color: #fff;
    border: 1px solid #fff;
  }
  ::v-deep.el-select .el-input .el-select__caret {
    color: #fff;
  }
  ::v-deep input::-webkit-input-placeholder {
    color: #fff;
  }
  .dialog-footer {
    .el-button--primary.is-plain {
      color: #fff;
      border-color: #0bbbff;
      background-color: rgba($color: #0bbbff, $alpha: 0.1);
    }
  }
  ::v-deep.el-table .warm-row .cell {
    color: #eec80b !important;
  }
  .table-wrapper ::v-deep .el-table__body tr:hover {
    background-color: rgba($color: #56fefe, $alpha: 0.4);
  }
  .gates-open-sty {
    background: url(~assets/images/WaterDisastersImage/zhamen-kaiqi.png)
      no-repeat center center;
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
        ::v-deep.el-input__inner {
          background: none;
          border: 0.0625rem solid #fff;
        }
        ::v-deep.el-input-number__decrease {
          border-left: 0.0625rem solid #fff;
          background: none;
          > i {
            color: #fff;
          }
        }
        ::v-deep.el-input-number__increase {
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
}
// 弹窗底部按钮样式
.dialog-footer {
  position: absolute;
  right: 20px;
  bottom: 15px;
  .el-button--primary.is-plain {
    color: #fff;
    border-color: #0bbbff;
    background-color: rgba($color: #0bbbff, $alpha: 0.1);
  }
}
::v-deep.primary-dialog .el-dialog .el-table th {
  color: #1cfff5;
}
::v-deep.el-table .ascending .sort-caret.ascending {
  border-bottom-color: #1cfff5;
}
::v-deep.el-table .descending .sort-caret.descending {
  border-top-color: #1cfff5;
}
::v-deep.myClass .el-input__suffix {
  margin-top: -3px;
  padding: 0;
}
::v-deep.el-date-editor .el-range-input {
  background: none;
  color: #fff;
}
::v-deep.el-date-editor .el-range-separator {
  color: #fff;
  line-height: 1.5rem;
}
::v-deep.el-date-editor .el-range__icon {
  color: #fff;
  line-height: 1.4rem;
}
::v-deep.el-date-editor .el-range__close-icon {
  color: #fff;
  line-height: 1.4rem;
}
::v-deep.el-date-editor .el-icon-date {
  line-height: 1;
}
.detail-table {
  ::v-deep .el-table__body-wrapper::-webkit-scrollbar {
    height: 6px; // 纵向滚动条 必写
    width: 6px;
  }
}
.tb-top-panel {
  margin-top: 10px;
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

::v-deep .is-checked .el-checkbox__label {
  color: #70adf8;
}
::v-deep .el-radio {
  margin-right: 15px;
}
::v-deep .el-radio__label {
  color: #fff;
}
::v-deep .is-checked .el-checkbox__inner {
  border-color: #70adf8;
  color: #70adf8;
  &::after {
    border-color: #70adf8;
  }
}
</style>

<style lang="scss" scoped>
.water-supply-select-panel,
.beauty-tab-panel {
  display: flex;
  margin-bottom: 20px;
}
.beauty-tab-panel {
  margin-bottom: 0;
}
.water-supply-select-btn,
.beauty-tab-item {
  padding: 10px 20px;
  margin-right: 20px;
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.6);
  cursor: pointer;
  &.active {
    color: #56fefe;
    background: rgba(72, 152, 251, 0.1);
    border: 1px solid #56fefe;
    box-shadow: inset 0 0 5px 0 #56fefe;
  }
}

.pagination {
  margin-top: 15px;
  text-align: right;
}
::v-deep .el-pagination {
  .el-pagination__total {
    color: #fff;
  }
  .el-pager {
    li {
      color: #fff;
      background: rgba(0, 0, 0, 0);
      &.active {
        background-color: #409eff !important;
      }
    }
  }
  .btn-prev,
  .btn-next {
    background: rgba(0, 0, 0, 0);
  }
}

.icon-shuiyuandi {
  display: inline-block;
  width: 48px;
  height: 48px;
}
.icon-shuiyuandi-1 {
  background-image: url(~assets/images/shuiyuandi-1.png);
}
.icon-shuiyuandi-2 {
  background-image: url(~assets/images/shuiyuandi-2.png);
}
.icon-shuiyuandi-3 {
  background-image: url(~assets/images/shuiyuandi-3.png);
}
.icon-shuiyuandi-4 {
  background-image: url(~assets/images/shuiyuandi-4.png);
}
.icon-shuiyuandi-5 {
  background-image: url(~assets/images/shuiyuandi-5.png);
}
.icon-shuiyuandi-6 {
  background-image: url(~assets/images/shuiyuandi-6.png);
}
.icon-shuiyuandi-7 {
  background-image: url(~assets/images/shuiyuandi-7.png);
}
.icon-shuiyuandi-8 {
  background-image: url(~assets/images/shuiyuandi-8.png);
}

.sx-dialog-count-1 {
  background-image: url(~assets/images/sx-dialog-count-1.png);
}
.sx-dialog-count-2 {
  background-image: url(~assets/images/sx-dialog-count-2.png);
}
.sx-dialog-count-3 {
  background-image: url(~assets/images/sx-dialog-count-3.png);
}
.sx-dialog-count-4 {
  background-image: url(~assets/images/sx-dialog-count-4.png);
}
.sx-dialog-count-5 {
  background-image: url(~assets/images/sx-dialog-count-5.png);
}
.sx-dialog-count-6 {
  background-image: url(~assets/images/sx-dialog-count-6.png);
}

.qushuihu-dialog-count-1 {
  background-image: url(~assets/images/qushuihu-dialog-count-1.png);
}
.qushuihu-dialog-count-2 {
  background-image: url(~assets/images/qushuihu-dialog-count-2.png);
}
.qushuihu-dialog-count-3 {
  background-image: url(~assets/images/qushuihu-dialog-count-3.png);
}
.qushuihu-dialog-count-4 {
  background-image: url(~assets/images/qushuihu-dialog-count-4.png);
}

.phjg-dialog-count-1 {
  background-image: url(~assets/images/phjg-dialog-count-1.png);
}
.phjg-dialog-count-2 {
  background-image: url(~assets/images/phjg-dialog-count-2.png);
}
.phjg-dialog-count-3 {
  background-image: url(~assets/images/phjg-dialog-count-3.png);
}
.phjg-dialog-count-4 {
  background-image: url(~assets/images/phjg-dialog-count-4.png);
}

.sybh-dialog-count-1 {
  background-image: url(~assets/images/sybh-dialog-count-1.png);
}
.sybh-dialog-count-2 {
  background-image: url(~assets/images/sybh-dialog-count-2.png);
}
.sybh-dialog-count-3 {
  background-image: url(~assets/images/sybh-dialog-count-3.png);
}
.sybh-dialog-count-4 {
  background-image: url(~assets/images/sybh-dialog-count-4.png);
}

.qsl-dialog-count-1 {
  background-image: url(~assets/images/qsl-dialog-count-1.png);
}

.mlhh-dialog-count-1 {
  background-image: url(~assets/images/mlhh-dialog-count-1.png);
}
.mlhh-dialog-count-2 {
  background-image: url(~assets/images/mlhh-dialog-count-2.png);
}
.mlhh-dialog-count-3 {
  background-image: url(~assets/images/mlhh-dialog-count-3.png);
}
.mlhh-dialog-count-4 {
  background-image: url(~assets/images/mlhh-dialog-count-4.png);
}

.sdz-dialog-count-1 {
  background-image: url(~assets/images/sdz-dialog-count-1.png);
}

.hzxc-dialog-count-1 {
  background-image: url(~assets/images/hzxc-dialog-count-1.png);
}
.hzxc-dialog-count-2 {
  background-image: url(~assets/images/hzxc-dialog-count-2.png);
}
.hzxc-dialog-count-3 {
  background-image: url(~assets/images/hzxc-dialog-count-3.png);
}
.hzxc-dialog-count-4 {
  background-image: url(~assets/images/hzxc-dialog-count-4.png);
}
.pagination-area {
  padding: 0 0 16px 0;
  color: #fff;
  ::v-deep .el-pagination {
    .el-pagination__total {
      color: #fff;
    }
    .el-pagination__jump {
      color: #fff;
    }
    .el-pager {
      > li {
        background: #0000;
        color: #fff;
        &.active {
          background-image: linear-gradient(180deg, #36c0e4 20%, #0639ff 100%);
          border-radius: 2px;
        }
      }
    }
  }
}
</style>

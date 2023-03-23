<template>
  <div :class="[leftSide == true ? 'project-dialog-wrap' : '']">
    <el-dialog
      :modal="modal"
      :visible.sync="visible"
      :width="width"
      top="10vh"
      :before-close="handleClose"
      :append-to-body="appendToBody"
      :fullscreen="fullscreen"
      :close-on-click-modal="false"
      class="primary-dialog"
    >
      <div
        class="primary-dialog__title"
        slot="title"
        :style="minimizeStatus ? 'justify-content: space-between;' : ''"
      >
        <p class="primary-dialog__title--label">
          {{ title }}
          <i
            class="peripheral-analysis"
            @click="peripheralAnalysis"
            v-if="ProgrammeData.type == '河道'"
          ></i>
        </p>
        <i
          class="minimize el-icon-minus"
          @click="minimize"
          v-if="minimizeStatus"
        />
      </div>
        <div
          class="primary-dialog__content"
          style="overflow-x: clip; overflow-y: auto; height: 700px"
        >
        <!-- 项目tab切换 -->
        <div v-if="ProgrammeData.type == '入河排污口'" style="margin-top:10px;">
          <div class="nav-bar">
            <ProjectItem title="基础信息" />
            <el-row :gutter="20">
              <el-col :span="8">
                <span class="label">入河排污口名称</span>
                <span class="value">{{ ProgrammeData.outletName || "-" }}</span>
              </el-col>
              <el-col :span="8">
                <span class="label">所在水资源分区</span>
                <span class="value">{{ ProgrammeData.wrzGdthNm || "-" }}</span>
              </el-col>
              <el-col :span="8">
                <span class="label">河湖名称</span>
                <span class="value">{{ ProgrammeData.riverLakeRsvr || "-" }}</span>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <span class="label">入河排污口规模</span>
                <span class="value">{{ ProgrammeData.outletType || "-" }}</span>
              </el-col>
              <el-col :span="8">
                <span class="label">设置时间</span>
                <span class="value">{{ "-" }}</span>
              </el-col>
              <el-col :span="8">
                <span class="label">所在地</span>
                <span class="value">{{ ProgrammeData.city || "-" }}</span>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-bottom:20px;">
              <el-col :span="8">
                <span class="label">污水入河方式</span>
                <span class="value">{{ ProgrammeData.intoRiverType || "-" }}</span>
              </el-col>
              <el-col :span="8">
                <span class="label">排放方式</span>
                <span class="value">{{ ProgrammeData.outletGrad || "-" }}</span>
              </el-col>
            </el-row>
            <ProjectItem title="存在问题" />
            <el-row :gutter="20">
              <el-col :span="8">
                <span class="label">保护区范围内</span>
                <span class="value">{{ "-" }}</span>
              </el-col>
              <el-col :span="8">
                <span class="label">距离下游最近取水口距离</span>
                <span class="value">{{ ProgrammeData.nearestWainDistance || "-" }}</span>
              </el-col>
              <el-col :span="8">
                <span class="label">所在水功能区水质达标</span>
                <span class="value">{{ "-" }}</span>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="8">
                <span class="label">符合入河排污口布局规划</span>
                <span class="value">{{ "-" }}</span>
              </el-col>
            </el-row>
          </div>
        </div>
        <!-- 项目tab切换 -->
        <div v-if="ProgrammeData.type == '项目'" style="margin-top:10px;">
          <div class="nav-bar">
            <ul>
              <li v-for="(item, index) in projectList" :key="item.name" style="padding-left:0;">
                <span
                  :class="{ active: projectActiveIndex === index }"
                  @click="projectChangeMenu(index, item.name)"
                  >{{ item.name }}</span
                >
              </li>
            </ul>
          </div>
        </div>
        <!-- 清四乱 -->
        <div v-if="ProgrammeData.type == '清四乱'" style="margin-top:10px;">
          <div class="content-panel-item">
            <div class="title-box">
              <div class="title-panel">
                <i
                  class="icon-diamonds"
                  style="margin-right: 10px; cursor: pointer"
                ></i>
                <span>整改前图像</span>
              </div>
              <i class="qsl-ld-dialog-line"></i>
            </div>
            <div class="content-box">
              <div
                class="img-panel"
                v-if="ProgrammeData.rectImgbeforeList && ProgrammeData.rectImgbeforeList.length > 0"
              >
                <img
                  v-for="(item, i) in ProgrammeData.rectImgbeforeList"
                  :src="item.fileUrl"
                  :key="i"
                />
              </div>
              <p v-else style="color: #a6a6a6">暂无图片</p>
            </div>
          </div>

          <div class="content-panel-item">
            <div class="title-box">
              <div class="title-panel">
                <i
                  class="icon-diamonds"
                  style="margin-right: 10px; cursor: pointer"
                ></i>
                <span>整改后图像</span>
              </div>
              <i class="qsl-ld-dialog-line"></i>
            </div>
            <div class="content-box">
              <div
                class="img-panel"
                v-if="ProgrammeData.rectImgAfterList && ProgrammeData.rectImgAfterList.length > 0"
              >
                <img
                  v-for="(item, i) in ProgrammeData.rectImgAfterList"
                  :src="item.fileUrl"
                  :key="i"
                />
              </div>
              <p v-else style="color: #a6a6a6">暂无图片</p>
            </div>
          </div>

          <div class="content-panel-item">
            <div class="title-box">
              <div class="title-panel">
                <i
                  class="icon-diamonds"
                  style="margin-right: 10px; cursor: pointer"
                ></i>
                <span>详细信息</span>
              </div>
              <i class="qsl-ld-dialog-line"></i>
            </div>
            <div class="content-box">
              <el-row :gutter="20">
                <el-col :span="8">
                  <span class="label">详细地址</span>
                  <span class="value">{{ ProgrammeData.address || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">所在乡镇</span>
                  <span class="value">{{ ProgrammeData.town || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">所在街道</span>
                  <span class="value">{{ ProgrammeData.street || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">地理坐标</span>
                  <span class="value"
                    >{{ Number(ProgrammeData.lng).toFixed(2) || "-" }}，{{
                      Number(ProgrammeData.lat).toFixed(2) || "-"
                    }}</span
                  >
                </el-col>
                <el-col :span="8">
                  <span class="label">问题来源</span>
                  <span class="value">{{ ProgrammeData.problemSource || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">河道等级</span>
                  <span class="value">{{ ProgrammeData.riverLevel || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">关联河湖</span>
                  <span class="value">{{ ProgrammeData.riverName || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">河长姓名</span>
                  <span class="value">{{ ProgrammeData.riverManagerName || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">四乱类型</span>
                  <span class="value">{{ ProgrammeData.fourType || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">严重程度</span>
                  <span class="value">{{ ProgrammeData.severity || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">问题描述</span>
                  <span class="value">{{ ProgrammeData.problemDescription || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">占地面积(k㎡)</span>
                  <span class="value">{{ ProgrammeData.occupySpace || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">备注说明</span>
                  <span class="value">{{ ProgrammeData.remark || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">整改要求</span>
                  <span class="value">{{
                    ProgrammeData.rectificationRequirement || "-"
                  }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">通知单位</span>
                  <span class="value">{{ ProgrammeData.deptName || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">通知人员</span>
                  <span class="value">{{ ProgrammeData.patrolUserName || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">联系方式</span>
                  <span class="value">{{ ProgrammeData.patrolUserPhone || "-" }}</span>
                </el-col>
                <el-col :span="8">
                  <span class="label">通知方式</span>
                  <span class="value">{{
                    ProgrammeData.sendType == "1" ? "短信" : "浙政钉"
                  }}</span>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
        <!-- 项目内容 -->
        <div v-if="ProgrammeData.type == '项目'" style="margin-top:10px;">
          <!-- 环境监测 -->
          <div v-show="projectActiveIndex == 0">
            <div class="titleName">今日环境监测实时数据</div>
            <div class="envStatistics">
              <div class="envStatisticsItem">
                <div class="marginBottom6">
                  <img src="@/assets/images/legend-rain9.png" alt="icon" />
                  <span class="spanVertical">实时噪音</span>
                </div>
                <span class="envName colourGreen marginRight8">{{
                  envObj.noise
                }}</span>
                <span class="colourGreen">db</span>
              </div>
              <div class="envStatisticsItem">
                <div class="marginBottom6">
                  <img
                    src="@/assets/images/legend-rain10.png"
                    alt="icon"
                  />
                  <span class="spanVertical">实时扬尘</span>
                </div>
                <span class="envName colourBlue">{{ envObj.dust }}</span>
              </div>
              <div class="envStatisticsItem">
                <div class="marginBottom6">
                  <img
                    src="@/assets/images/legend-rain100.png"
                    alt="icon"
                  />
                  <span class="spanVertical">实时PM2.5</span>
                </div>
                <span class="envName colourRed">{{ envObj.PM25 }}</span>
              </div>
              <div class="envStatisticsItem">
                <div class="marginBottom6">
                  <img
                    src="@/assets/images/legend-rain25.png"
                    alt="icon"
                  />
                  <span class="spanVertical">实时PM10</span>
                </div>
                <span class="envName colourYellow">{{ envObj.PM10 }}</span>
              </div>
            </div>
            <div class="titleName">今日环境监测趋势</div>
            <div class="marginBottom6 marginBottom12">
              <img src="@/assets/images/legend-rain9.png" alt="icon" />
              <span class="spanVertical">降雨趋势 (mm)</span>
            </div>
            <div id="barMain"></div>
            <div class="marginBottom6 marginBottom12">
              <img src="@/assets/images/legend-rain9.png" alt="icon" />
              <span class="spanVertical">积水趋势 (mm)</span>
            </div>
            <div id="lineMain">线图</div>
          </div>
          <!-- 人员 -->
          <div v-if="projectActiveIndex == 1">
            <div class="companyBox">
              <div
                class="companyItem"
                v-for="(item, index) in companyArr"
                :key="index"
                :class="companyIndex == item.dictName ? 'activeBorder' : ''"
                @click="clickCompany(index, item.dictName)"
              >
                {{ item.dictName }}
              </div>
            </div>
            <div class="companyUserBox">
              <div
                class="companyUser"
                v-for="(item, index) in selfCompanyUser"
                :key="index"
              >
                <img :src="item.personPicture" alt="img" />
                <div class="companyUserMsg">
                  <div class="colorABD nameFlex">
                    <span :title="item.personName">{{ item.personName }}</span>
                    <span :title="item.personJob">{{ item.personJob }}</span>
                  </div>
                  <div style="margin-bottom: 10px">{{ item.personPhone }}</div>
                  <div class="userPhoneMes">
                    <span>
                      <img src="@/assets/images/main-phone.png" alt="alt" />
                      电话
                    </span>
                    <span>
                      <img src="@/assets/images/main-message.png" alt="alt" />
                      短信
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 视频 -->
          <div v-if="projectActiveIndex == 2">
            <div style="font-size:24px;color:#fff;margin:25% 40%;">
            暂无视频资源
            </div>
            <div class="videoBox" v-if="false">
              <div class="videoList">
                <div class="titleName">视频列表</div>
                <div class="videoTable">
                  <div class="videoFlex">
                    <div>序号</div>
                    <div>名称</div>
                  </div>
                  <div
                    class="videoFlex"
                    :class="
                      item.cameraId == currentVideo.cameraId ? 'currentNode' : ''
                    "
                    v-for="(item, index) in videoList"
                    :key="index"
                    @click="rowClick(item)"
                  >
                    <div>{{ index + 1 }}</div>
                    <div>{{ item.cameraName }}</div>
                  </div>
                </div>
              </div>
              <div class="videoWindow">
                <div class="titleName">实时视频</div>
                <div class="videoComponent">
                  <primaryVideo
                    v-if="dialogVisible"
                    :code="currentVideo.cameraId"
                  ></primaryVideo>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 在建工程 -->
        <div v-if="ProgrammeData.type == '在建工程'" style="margin-top:10px;">
          <div class="nav-bar">
            <ul>
              <li v-for="(item, index) in tabList" :key="item.name">
                <span
                  :class="{ active: activeIndex === index }"
                  @click="changeMenu(index, item.name)"
                  >{{ item.name }}</span
                >
              </li>
            </ul>
          </div>
        </div>
        <!-- 在建工程 -->
        <div v-if="ProgrammeData.type == '在建工程'" style="margin-top:10px;">
          <div class="content-body">
            <div class="project-info" v-show="activeIndex === 0">
              <el-row :gutter="20">
                <el-col :span="8"><div class="grid-content">项目名称</div></el-col>
                <el-col :span="8"><div class="grid-content">项目地址</div></el-col>
                <el-col :span="8"><div class="grid-content">所在地区</div></el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.projectName }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.projectAddr }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.projectAddr }}
                  </div></el-col
                >
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-content">工程造价( 万元)</div></el-col
                >
                <el-col :span="8"><div class="grid-content">中央分类</div></el-col>
                <el-col :span="8"><div class="grid-content">省级分类</div></el-col>
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.projectCosts }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.centralType }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.provincialType }}
                  </div></el-col
                >
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-content">项目所在经度</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-content">项目所在纬度</div></el-col
                >
                <el-col :span="8"><div class="grid-content">业务分类</div></el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-value">{{ projectObj.longitude }}</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">{{ projectObj.latitude }}</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.businessType }}
                  </div></el-col
                >
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8"><div class="grid-content">项目级别</div></el-col>
                <el-col :span="8"
                  ><div class="grid-content">项目组织方式</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-content">工程工期&nbsp;(月)</div></el-col
                >
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.projectLevel==''? "" : projectObj.projectLevel}}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.projectOrganization }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.projectEngineeringCycle }}
                  </div></el-col
                >
              </el-row>

              <el-row :gutter="20">
                <el-col :span="16"><div class="grid-content">项目状态</div></el-col>
                <el-col :span="8"><div class="grid-content">计划类型</div></el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="16"
                  ><div class="grid-value">
                    {{ projectObj.projectStatus }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">{{ projectObj.planType }}</div></el-col
                >
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-content">计划开工日期</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-content">计划竣工日期</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-content">工程等别&nbsp;</div></el-col
                >
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.planStartTime }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.planCompletionTime }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.engineeringGrade }}
                  </div></el-col
                >
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8"><div class="grid-content">质检单位</div></el-col>
                <el-col :span="16"><div class="grid-content">项目简介</div></el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ projectObj.qualityInspectionUnit }}
                  </div></el-col
                >
                <el-col :span="16"
                  ><div class="grid-value">
                    {{ projectObj.projectDetails }}
                  </div></el-col
                >
              </el-row>
            </div>

            <!-- 批复相关 -->
            <div class="pifu-info" v-show="activeIndex === 1">
              <div class="titleMidel">
                <p>批复基本信息</p>
              </div>
              <div>
                <el-row :gutter="20">
                <el-col :span="8"><div class="grid-content">建设地址</div></el-col>
                <el-col :span="8"><div class="grid-content">项目类型</div></el-col>
                <el-col :span="8"><div class="grid-content">批复文号</div></el-col>
              </el-row>
              <el-row :gutter="20" v-if="replyObj!=null">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ replyObj.reviewDocumentNumber }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">{{ replyObj.projectType }}</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ replyObj.reviewDocumentNumber }}
                  </div></el-col
                >
              </el-row>
              <el-row :gutter="20" v-if="replyObj!=null">
                <el-col :span="8"
                  ><div class="grid-content">概算总投资&nbsp;(万元)</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-content">竣工验收主持单位</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-content">计划开工时间</div></el-col
                >
              </el-row>
              <el-row :gutter="20" v-if="replyObj!=null">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ replyObj.estimatedTotalInvestment }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ replyObj.acceptanceUnit }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ replyObj.constructionAddr }}
                  </div></el-col
                >
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8"><div class="grid-content" >工程等别</div></el-col>
                <el-col :span="8"><div class="grid-content">批复文件</div></el-col>
                <el-col :span="8"
                  ><div class="grid-content">是否为河道类项目</div></el-col
                >
              </el-row>
              <el-row :gutter="20" v-if="replyObj!=null">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ replyObj.constructionAddr }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value" >{{ replyObj.reviewFile }}</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value" >
                    {{ replyObj.isRiverProject }}
                  </div></el-col
                >
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8"><div class="grid-content">批复单位</div></el-col>
                <el-col :span="8"><div class="grid-content">批复时间</div></el-col>
                <el-col :span="8"
                  ><div class="grid-content">批复工期&nbsp;(月)</div></el-col
                >
              </el-row>
              <el-row :gutter="20" v-if="replyObj!=null">
                <el-col :span="8"
                  ><div class="grid-value">{{ replyObj.reviewUnit }}</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">{{ replyObj.reviewTime }}</div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">{{ replyObj.reviewCycle }}</div></el-col
                >
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8"><div class="grid-content">项目法人</div></el-col>
                <el-col :span="8"><div class="grid-content">建设性质</div></el-col>
                <el-col :span="8"
                  ><div class="grid-content">建设模式&nbsp;(月)</div></el-col
                >
              </el-row>
              <el-row :gutter="20" v-if="replyObj!=null">
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ replyObj.projectLegalPerson }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">
                    {{ replyObj.projectNature }}
                  </div></el-col
                >
                <el-col :span="8"
                  ><div class="grid-value">{{ replyObj.projectMode }}</div></el-col
                >
              </el-row>
              <el-row :gutter="20">
                <el-col :span="8"
                  ><div class="grid-content">工程效果图附件</div></el-col
                >
              </el-row>
              <el-row :gutter="20" v-if="replyObj!=null">
                <el-col :span="8">
                  <div class="grid-value" v-for="(file ,index) in fileList" :key="index" @click="showFileWrapper(file.id)">
                    {{file.name}}
                  </div>
                  </el-col>
              </el-row>
              </div>
            
            </div>

            <!-- 参见单位人员信息 -->
            <div class="constructer-info" v-show="activeIndex === 2">
              <div class="titleMidel">
                <p>参见单位信息及关键人员</p>
              </div>
              <el-table
                :data="constructerList"
                style="width: 100%"
                stripe
                :cell-style="cellstyle"
              >
                <el-table-column prop="companyType" label="单位类型">
                </el-table-column>
                <el-table-column label="单位名称/统一社会信用代码">
                  <template slot-scope="scope">
                    <div v-if="scope.row.companyName !== ''">
                      {{ scope.row.companyName }}
                    </div>
                    <div v-if="scope.row.unifiedCode !== ''">
                      {{ scope.row.unifiedCode }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="position" label="联系人"> </el-table-column>
                <el-table-column label="人员姓名/身份证号码" width="140">
                  <template slot-scope="scope">
                    <div v-if="scope.row.name !== ''">
                      {{ scope.row.name }}
                    </div>
                    <div v-if="scope.row.idCard">
                      {{ scope.row.idCard }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="phone"
                  label="电话号码"
                  width="120"
                >
                </el-table-column>
              </el-table>
            </div>

            <!-- 视频监控 -->
            <div class="constructer-info" v-if="activeIndex === 3">
              <div class="video-wrapper-left">
                <ul v-if="videoList.length>0">
                  <li v-for="(video,index) in videoList" :key="index" @click="changVedio(index,video )">
                    <span :class="{activeVideoBg: activeVideo===index}">{{ video.cameraName }}</span>
                  </li>
                </ul>
                <div v-else>
                  暂未同步任何监控设备
                </div>
              </div>
              <div class="video-wrapper-right" v-if="videoCode!=''">
                <div class="video-wrapper">
                  <primary-video :code="videoCode"> </primary-video>
                </div>
              </div>
            </div>
            <!-- 工程档案 -->
            <div class="project-archives" v-show="activeIndex === 4">
              <el-table
                :data="fileLists"
                style="width: 100%; height: 60%"
                row-key="templateId"
                stripe
                :cell-style="cellstyle"
                :default-expand-all="true"
                :tree-props="{
                  children: 'templateList',
                  hasChildren: 'hasChildren',
                }"
              >
                <el-table-column
                  label="序号"
                  prop=""
                  width="50"
                  align="center"
                  type="index"
                >
                </el-table-column>
                <el-table-column prop="templateName" label="档案名称" width="180">
                </el-table-column>
                <el-table-column prop="docNumber" label="附件数量">
                  <template slot-scope="scope">
                    <span
                    
                      v-if="scope.row.fileId != ''"
                      class="showFile-span"

                      >{{scope.row.docNumber}}</span
                    >
                    <span v-else></span>
                  </template>
                </el-table-column>
                <el-table-column label="附件">
                  <template slot-scope="scope">
                    <span
                      @click="showFileWrapper(scope.row.fileId)"
                      v-if="scope.row.fileId != ''"
                      class="showFile-span"

                      >查看附件</span
                    >
                    <span v-else>暂无附件</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 标化工地 -->
            <div class="constructer-info" v-show="activeIndex === 5">
              <div class="video-wrapper-left">
                <el-tree
                  :data="treeData"
                  node-key="id"
                  default-expand-all
                  :expand-on-click-node="false"
                  :props="defaultProps"
                  @node-click="nodeClick"
                >
                  <span class="custom-tree-node" slot-scope="{ node, data }">
                    <i
                      :class="
                        node.expanded ? 'el-icon-folder-opened' : 'el-icon-folder'
                      "
                    ></i>
                    <span class="file_name" :title="node.label">{{
                      node.label
                    }}</span>
                  </span>
                </el-tree>
              </div>
              <div class="video-wrapper-right">
                <div class="video-wrapper">
                  <el-table
                    :data="tableData"
                    style="width: 100%"
                    :cell-style="cellstyle"
                    stripe
                  >
                    <el-table-column
                      prop="fileType"
                      label="文件类型"
                      width="100"
                    ></el-table-column>
                    <el-table-column
                      prop="archiveTime"
                      label="归档时间"
                    ></el-table-column>
                    <el-table-column
                      prop="uploadTime"
                      label="上传时间"
                    ></el-table-column>
                    <el-table-column label="操作">
                      <template slot-scope="scope">
                        <span
                          @click="showFileWrapper(scope.row.fileId)"
                          v-if="scope.row.fileId !== null"
                          class="showFile-span"
                          >查看附件</span
                        >
                        <span v-else>暂无附件</span>
                      </template>
                    </el-table-column>
                  </el-table>
                  <!-- 分页 -->
                  <el-pagination
                    background
                    layout="prev, pager, next"
                    :current-page="pageObj.pageNum"
                    :total="pageObj.totalPages"
                    :page-size="pageObj.pageSize"
                    @current-change="currentChange"
                  ></el-pagination>
                </div>
              </div>
            </div>

            <!--工资监管  -->
            <div class="wage-ragulation" v-show="activeIndex === 6">
              <el-table
                :data="wagesList"
                style="width: 100%"
                stripe
                :cell-style="cellstyle"
              >
                <el-table-column
                  label="序号"
                  prop=""
                  width="50"
                  align="center"
                  type="index"
                ></el-table-column>
                <el-table-column prop="time" label="所属年月"> </el-table-column>
                <el-table-column label="实名制人数">
                  <template slot-scope="scope">
                    {{
                      scope.row.indentityNumber !== null
                        ? scope.row.indentityNumber
                        : 0
                    }}
                  </template>
                </el-table-column>
                <el-table-column label="发放人数">
                  <template slot-scope="scope">
                    {{ scope.row.payNumber !== null ? scope.row.payNumber : 0 }}
                  </template>
                </el-table-column>
                <el-table-column label="金额（元）">
                  <template slot-scope="scope">
                    {{ scope.row.amount !== null ? scope.row.amount : 0 }}
                  </template>
                </el-table-column>
                <el-table-column label="工资发放证明">
                  <template slot-scope="scope">
                    <span
                      @click="showFileWrapper(scope.row.fileId)"
                      v-if="scope.row.fileId !== null"
                      class="showFile-span"
                      >查看附件</span
                    >
                    <span v-else>暂无附件</span>
                  </template>
                </el-table-column>
              </el-table>
              <div class="pagenation-box">
                <el-pagination
                  background
                  layout="total, prev, pager, next"
                  @size-change="handleSizeChange"
                  @current-change="wageChangePage"
                  :total="pageObj.totalPages"
                >
                </el-pagination>
              </div>
            </div>
            <!-- 稽查管理 -->
            <div class="wage-ragulation" v-show="activeIndex === 7">
              <el-table
                :data="insepctList"
                style="width: 100%"
                stripe
                :cell-style="cellstyle"
              >
                <el-table-column
                  label="序号"
                  prop=""
                  width="50"
                  align="center"
                  type="index"
                ></el-table-column>
                <el-table-column
                  prop="inspectionActivities"
                  label="检查活动名称"
                ></el-table-column>
                <el-table-column prop="projectName" label="所属工程">
                </el-table-column>
                <el-table-column label="检查人员">
                  <template slot-scope="scope">
                    {{
                      scope.row.inspectionName !== null
                        ? scope.row.inspectionName
                        : " "
                    }}
                  </template>
                </el-table-column>
                <el-table-column label="检查时间">
                  <template slot-scope="scope">
                    {{ scope.row.checkTime !== null ? scope.row.checkTime : " " }}
                  </template>
                </el-table-column>
                <el-table-column label="整改通知书发出时间">
                  <template slot-scope="scope">
                    {{
                      scope.row.noticeSendingTime !== null
                        ? scope.row.noticeSendingTime
                        : " "
                    }}
                  </template>
                </el-table-column>
                <el-table-column label="整改反馈意见">
                  <template slot-scope="scope">
                    {{ scope.row.feedback !== null ? scope.row.feedback : " " }}
                  </template>
                </el-table-column>
                <el-table-column label="上传整改附件">
                  <template slot-scope="scope">
                    <span
                      @click="showFileWrapper(scope.row.inspectionFileId)"
                      class="showFile-span"
                      >查看文件</span
                    >
                  </template>
                </el-table-column>
              </el-table>
              <div class="pagenation-box">
                <el-pagination
                  background
                  layout="total, prev, pager, next"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :total="pageObj.totalPages"
                >
                </el-pagination>
              </div>
            </div>
            <look-file
              :visible="showFileDialog"
              :id="fileId"
              @close="closeFileDialog"
            ></look-file>
          </div>
        </div>
        <div v-if="ProgrammeData.type == '地下水位站' || ProgrammeData.type == '地下机井'">
          <el-row>
            <el-col :span="12" v-for="(item, i) in PublicHeaderList" :key="i" style="margin:10px 0;">
              <span style="color:#fff;width:145px;display: inline-block;text-align: right;">{{item.label}}</span>
              <el-input style="width:300px;margin-left:10px;" :placeholder="form[item.prop]" />
            </el-col>
          </el-row>
        </div>
        <!-- 河景全景图 -->                    
        <iframe
          v-if="ProgrammeData.type == '河景'"
          :src="panoramagram"
          frameborder="0"
          style="width: 100%; height: 680px"
        />
        <div v-if="ProgrammeData.type == '取水口' || ProgrammeData.type == '河道'">
          <!-- tab切换 -->
          <div>
            <span
              v-if="ProgrammeData.type == '取水口'"
              :class="tabSwitch == '日取水量' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('日取水量')"
              >日取水量</span
            >
            <span
              v-if="ProgrammeData.type == '取水口'"
              :class="tabSwitch == '月取水量' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('月取水量')"
              >月取水量</span
            >
            <!-- 河道 -->
            <span
              v-if="ProgrammeData.type == '河道'"
              :class="tabSwitch == '基础信息' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('基础信息')"
              >基础信息</span
            >
            <span
              v-if="ProgrammeData.type == '河道'"
              :class="tabSwitch == '健康信息' ? 'tab-switch' : 'tab-switch-sty'"
              @click="tabSwitchClick('健康信息')"
              >健康信息</span
            >
            <span
              v-if="ProgrammeData.type == '河道'"
              :class="
                tabSwitch == '巡查信息' ? 'tab-switch' : 'tab-switch-sty'
              "
              @click="tabSwitchClick('巡查信息')"
              >巡查信息</span
            >
            <i v-if="backData" style="float:right;" class="icon-get-back" @click="backtrack" />
          </div>
          <!-- 河道基础信息 -->
          <div v-if="ProgrammeData.type == '河道' && !backData" style="margin-top:15px;">
            <!-- 基础信息 -->
            <el-row v-if="tabSwitch == '基础信息'">
              <el-col :span="24">
                <ProjectItem title="河道基础信息" />
                <el-col  :span="11" class="riverway-sty" v-for="(item, i) in channelLnformationList" :key="i">
                  <span>{{item.label}}</span>
                  <span>{{rivSegInfoList[item.prop]}}</span>
                </el-col>
              </el-col>
              <el-col :span="24">
                <ProjectItem title="河长基础信息">
                <span v-for="(item, i) in riverChiefName" :key="i" v-show="riverChief" :class="riverChief == item ? 'tab-switch' : 'tab-switch-sty'" @click="riverChiefClick(item)">{{item}}</span>
                </ProjectItem>
                <el-col v-show="riverChief" style="margin:10px 38px 10px 0;" :span="11" class="riverway-sty" v-for="(item, i) in riverChiefList" :key="i">
                  <span>{{item.label}}</span>
                  <span>{{riverChiefData[item.prop]}}</span>
                </el-col>
              </el-col>
            </el-row>
            <!-- 健康信息 -->
            <el-row v-if="tabSwitch == '健康信息'" style="margin-top:10px;">
              <ProjectItem title="健康信息" />
              <el-col :span="13" style="margin-top:20px;display: flex;justify-content: space-between;">
                <div style="width:95%;">
                  <div class="health-code-sty">
                    <i v-if="rivSegInfoList.healthState == 1" class="icon-the-green-code" />
                    <i v-if="rivSegInfoList.healthState == 2" class="icon-the-yellow-code" />
                    <i v-if="rivSegInfoList.healthState == 3" class="icon-the-red-code" />
                  </div>
                  <div class="health-sty">
                    <p>河湖“四乱”清零<span>{{rivSegInfoList.noneFourChaos == 1 ? '是' : '否'}}</span></p>
                    <p>河湖管护到位 <span>{{rivSegInfoList.riverManageWell == 1 ? '是' : '否'}}</span></p>
                  </div>
                  <div class="health-sty">
                    <p>无乱点河道创建 <span>{{rivSegInfoList.noneIllegalRiverCreate == 1 ? '是' : '否'}}</span></p>
                    <p>水域岸线占补平衡 <span>{{rivSegInfoList.shorelineOccupyBalance == 1 ? '是' : '否'}}</span></p>
                  </div>
                  <div class="health-sty">
                    <p>涉河建设项目批建相符 <span>{{rivSegInfoList.riverProjTallied == 1 ? '是' : '否'}}</span></p>
                    <p>公众满意 <span>{{rivSegInfoList.publicSatisfy == 1 ? '是' : '否'}}</span></p>
                  </div>
                </div>
                <div class="boundary-sty"></div>
              </el-col>
              <el-col :span="11">
                <div id="radarMap" style="width:450px;height:500px;"></div>
              </el-col>
            </el-row>
            <!-- 巡查信息 -->
            <div v-if="tabSwitch == '巡查信息'" style="margin-top:10px;">
              <el-row class="under-frame-sty">
                <el-col :span="6" class="inspect-number-sty">
                    <p><i class="icon-amount" /></p>
                    <div>
                      <p>本月巡查总次数</p>
                      <p><span>{{stats4LoadPageList.times}}</span>次</p>
                    </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-amount" /></p>
                  <div>
                    <p>本月巡查总长度</p>
                    <p><span>{{stats4LoadPageList.inspectLength}}</span>km</p>
                  </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-amount" /></p>
                  <div>
                    <p>本月巡查总时长</p>
                    <p><span>{{stats4LoadPageList.inspectTime}}</span>h</p>
                  </div>
                </el-col>
                <el-col :span="6" class="inspect-number-sty">
                  <p><i class="icon-hong-ma" /></p>
                  <div>
                    <p>本月巡查问题总数</p>
                    <p><span style="color:red;">{{stats4LoadPageList.problemNum}}</span>个</p>
                  </div>
                </el-col>
              </el-row>
              <el-row style="margin:15px 0;">
                <el-col :span="5">
                  <p style="font-size:16px;color:#fff;margin-bottom:10px;">所属单位</p>
                  <el-select v-model="affiliatedUnitData" placeholder="请选择" style="width: 150px;">
                    <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"/>
                  </el-select>
                </el-col>
                <el-col :span="5">
                  <p style="font-size:16px;color:#fff;margin-bottom:10px;">巡查开始时间</p>
                  <el-date-picker style="width: 150px;" value-format="yyyy-MM-dd HH:mm" format="yyyy-MM-dd HH:mm" v-model="startTime" type="datetime" placeholder="巡查时间"/>
                </el-col>
                <el-col :span="5">
                  <p style="font-size:16px;color:#fff;margin-bottom:10px;">巡查结束时间</p>
                  <el-date-picker style="width: 150px;" value-format="yyyy-MM-dd HH:mm" format="yyyy-MM-dd HH:mm" v-model="endTime" type="datetime" placeholder="巡查时间"/>
                </el-col>
                <el-col :span="5">
                  <p style="font-size:16px;color:#fff;margin-bottom:10px;">巡查人员</p>
                  <el-input v-model="inspectorsData" placeholder="请输入" style="width: 150px;" />
                </el-col>
                <el-col :span="4">
                  <span class="btn-sty" @click="inquire">查询</span>
                  <span class="btn-sty" @click="empty">清空</span>
                </el-col>
              </el-row>
              <!-- 巡查列表 -->
              <div class="table-box" style="height: 380px">
                <primary-map-table :columnList="columnLists" @rowClick="rowClick" :dataList="loadPageList" />
              </div>
            </div>
          </div>
          <!-- 巡查路劲 -->
          <div v-if="backData" style="margin-top:15px; height:550px;overflow-y: auto;">
            <ProjectItem title="巡查路径" />
            <div style="padding-bottom: 10px;height: 470px">
              <ComparativeHistoryMap v-if="visible" :mapShowsList="mapShowsList" />
            </div>
            <!-- 巡查情况 -->
            <ProjectItem title="巡查情况" />
            <el-row>
              <el-col :span="11" class="riverway-sty" v-for="(item, i) in columnLists" :key="i" v-show="item.label != '巡查问题(个)'">
                <span>{{item.label}}</span>
                <span>{{inspectorsList[item.prop]}}</span>
              </el-col>
            </el-row>
            <!-- 问题记录 -->
            <div>
              <ProjectItem title="问题记录" />
              <el-collapse v-model="activeName" accordion v-if="inspectorsList.patrolProblemRespVos">
                <el-collapse-item v-for="(item, i) in inspectorsList.patrolProblemRespVos" :key="i" :title="'问题点' + `${i+1}` + ':' + item.problemDescription" :name="i">
                  <el-row style="padding:20px 0 0 20px;">
                    <el-col :span="11" class="riverway-sty">
                      <span>详细地址</span>
                      <span>{{item.address}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在乡镇</span>
                      <span>{{item.town}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在街道</span>
                      <span>{{item.street}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>地理坐标</span>
                      <span>{{item.lng}}-{{item.lat}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>问题来源</span>
                      <span>{{item.problemSource}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>所在河湖</span>
                      <span>{{item.riverName}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>河道等级</span>
                      <span>{{item.riverLevel}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>河长姓名</span>
                      <span>{{item.riverManagerName}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>四乱类型</span>
                      <span>{{item.fourType}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>严重程度</span>
                      <span>{{item.severity}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>备注说明</span>
                      <span>{{item.remark}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>占地面积</span>
                      <span>{{item.occupySpace}}</span>
                    </el-col>
                    <el-col :span="23" class="riverway-sty">
                      <span>整改要求</span>
                      <span>{{item.rectificationRequirement}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>整改前图像</span>
                      <span>{{item.rectImgbeforeList}}</span>
                    </el-col>
                    <el-col :span="11" class="riverway-sty">
                      <span>整改后图像</span>
                      <span>{{item.rectImgAfter}}</span>
                    </el-col>
                  </el-row>
                </el-collapse-item>
              </el-collapse>
              <div v-else style="text-align: center;">
                <i class="icon-no-record"></i>
              </div>
            </div>
          </div>
          <!-- 图表，列表 -->
          <el-row v-if="ProgrammeData.type == '取水口'">
            <el-col :span="16">
              <!-- 图表 -->
              <div id="histogramChart" style="width: 100%; height: 560px" />
            </el-col>
            <!-- 列表 -->
            <el-col :span="8">
              <div class="table-box" style="height: 500px">
                <primary-map-table :columnList="columnList" :dataList="dataList"/>
              </div>
            </el-col>
          </el-row>
          <!-- 在建工程 -->
          <div v-show="ProgrammeData.type == '在建工程'" class="videoWrapper" v-if="showVideo">
            <div class="dialog-title">
              <span>{{ projectObj.projectName }}</span>
              <i class="el-icon-close close-dialog" @click="closeWrapper"></i>
            </div>
            <div class="video-box">
              <primary-video :code="cameraIndexCode"> </primary-video>
            </div>
          </div>
        </div>
        <!-- 泵站工情 图表，列表 -->
        <div v-if="ProgrammeData.type == '泵站工情'">
          <el-row>
            <el-col :span="16" style="padding: 0 15px;">
              <primary-subhead :text="'水位流量曲线'" style="height: 1.5rem;line-height: 1.5rem;">
                <slot>
                  <el-select
                    v-model="time"
                    class="primary-select"
                    @change="timeHandle"
                    placeholder="请选择时间"
                    style="width: 126px"
                  >
                    <el-option
                      v-for="item in timeOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    />
                  </el-select>
                </slot>
              </primary-subhead>
              <!-- 图表 -->
              <div id="pumpStationChart" style="width: 100%; height: 600px" />
            </el-col>
            <!-- 列表 -->
            <el-col :span="8">
              <div class="table-box" style="height: 500px;padding-top: 60px;">
                  <el-table :data="waterStationsList" style="width: 100%" stripe :cell-style="cellstyle">
                  <el-table-column label="序号" width="70" align="center" type="index"/>
                  <el-table-column prop="time" label="时间"/>
                  <el-table-column prop="jskZ" label="进水口水位(m）" width="140"/>
                  <el-table-column prop="jscZ" label="出水口水位(m）" width="140"/>
                  <el-table-column prop="cscZ" label="出水池水位(m）" width="140"/>
                  <el-table-column prop="ll" label="流量(m³/s）" width="100"/>
                  <el-table-column label="水泵状态" width="160">
                    <template slot-scope="scope">
                      <span style="padding:0 5px;">
                        <i :class=" scope.row.num1 == '开' ? 'icon-switch-on' : 'icon-switch-off'" />
                      </span>
                      <span style="padding:0 5px;">
                        <i :class=" scope.row.num2 == '开' ? 'icon-switch-on' : 'icon-switch-off'" />
                      </span>
                      <span style="padding:0 5px;">
                        <i :class=" scope.row.num3 == '开' ? 'icon-switch-on' : 'icon-switch-off'" />
                      </span>
                      <span style="padding:0 5px;">
                        <i :class=" scope.row.num4 == '开' ? 'icon-switch-on' : 'icon-switch-off'" />
                      </span>
                      <span style="padding:0 5px;">
                        <i :class=" scope.row.num5 == '开' ? 'icon-switch-on' : 'icon-switch-off'" />
                      </span>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as echarts from "echarts";
  import { PrimarySubhead } from "@/components";
  import ComparativeHistoryMap from "../comparative_history_map/";
  import ProjectItem from "../screen_project_dialog/components/ProjectItem";
  import  PrimaryMapTable  from "../primary_map_table/";
  import  PrimaryVideo  from "../primary_video/VideoPlayer.vue";
  import  LookFile  from "../lookFile/lookFile.vue";
  import { hydraulicApi } from "@/api/hydraulic_amap/index.js";
  export default {
    name: "RiversInformationDialog",
    props: {
      ProgrammeData: {
        type: Object,
        default: null,
      },
      visible: {
        type: Boolean,
        default: false,
      },
      appendToBody: {
        type: Boolean,
        default: false,
      },
      width: {
        type: String,
        default: "1300px",
      },
      mainTitle: {
        type: String,
        default: "测试",
      },
      fullscreen: {
        type: Boolean,
        default: false,
      },
      leftSide: {
        type: Boolean,
        default: false,
      },
      minimizeStatus: {
        type: Boolean,
        default: false,
      },
      hasAnalysis: { type: Boolean, default: true },
      modal: {
        type: Boolean,
        default: false,
      },
    },
    data() {
      return {
        time: 2,
        timeOptions: [
          {
            value: 1,
            label: "前一天",
          },
          {
            value: 2,
            label: "前二天",
          },
          {
            value: 3,
            label: "前三天",
          },
          {
            value: 4,
            label: "前四天",
          },
          {
            value: 7,
            label: "前一周",
          },
          {
            value: 14,
            label: "前二周",
          },
        ],
        companyArr: [],
        companyIndex: "",
        envObj: {
          noise: 50,
          dust: 36,
          PM25: 18,
          PM10: 28,
        },
        projectObj: {}, //项目基本信息
        replyObj: {}, //初步批复信息
        constructerList: [], //参建单位信息
        wagesList: [], //工资监管
        insepctList: [], //稽查数据
        activeIndex: 0,
        tabList: [
        { name: "工程基本信息" },
        { name: "初步设计批复" },
        { name: "参建单位信息及参建人员" },
        { name: "视频监控" },
        { name: "工程档案" },
        { name: "标化工地" },
        { name: "工资监管" },
        { name: "稽查管理" },
      ],
      projectActiveIndex: 0,
      projectList: [
        { name: "环境监测" },
        { name: "人员" },
        { name: "视频" }
      ],
      videoCode:"",
      value: "",
      tableData: [],
      fileLists: [
        {
          id: 1,
          xuhao: "1",
          fileName: "建设前期（主目录）",
          attachmentName: "附件A",
          attachmentNum: 22,
          children: [
            {
              id: "1-1",
              fileName: "可行性研究",
              attachmentName: "附件A-1",
              attachmentNum: 2,
            },
            {
              id: "1-2",
              fileName: "项目建议书",
              attachmentName: "附件A-2",
              attachmentNum: 3,
            },
            {
              id: "1-3",
              fileName: "水土保持方案",
              attachmentName: "附件A-3",
              attachmentNum: 4,
            },
          ],
        },
        {
          id: 2,
          xuhao: "2",
          fileName: "建设过程",
          attachmentName: "附件B",
          attachmentNum: 1,
          children: [
            {
              id: "2-1",
              fileName: "质量检测",
              attachmentName: "附件B-1",
              attachmentNum: 3,
            },
            {
              id: "2-2",
              fileName: "质量评定",
              attachmentName: "附件B-2",
              attachmentNum: 4,
            },
          ],
        },
        {
          id: 3,
          xuhao: "3",
          fileName: "工程验收",
          attachmentName: "附件C",
          attachmentNum: 111,
          children: [
            {
              id: "3-1",
              fileName: "阶段验收",
              attachmentName: "附件c",
              attachmentNum: 15,
            },
            {
              id: "3-2",
              fileName: "完工验收",
              attachmentName: "附件c-2",
              attachmentNum: 14,
            },
          ],
        },
      ],
      showDialog: false,
      showProjectInfor: false,
      showVideo: false,
      pageObj: {
        pageNum: 1,
        pageSize: 10,
        totalPages: 0,
      },
      showFileDialog: false,
      fileId: "",
      fileList:[],
      videoList: [
        { vedioName: "珊溪西山水厂机房 ", vedioCode: "1" },
        { vedioName: " 珊溪西山自来水厂水利监控  ", vedioCode: "2" },
        { vedioName: " 珊溪镇西山岭头山塘  ", vedioCode: "3" },
        { vedioName: " 库区码头球机  ", vedioCode: "4" },
      ],
      treeData: [],
      defaultProps: {
        children: "childrenList",
        label: "folderName",
      },
      currentNode: "",
      activeVideo:-1,
      cameraIndexCode:"",
        mapShowsList: [],
        // 巡查人员表字段
        inspectorsList: {},
        // 河长
        riverChief: null,
        activeName: '1',
        riverChiefName: [],
        riverChiefData: {},
        riverChiefList: [
          {
            label: "河长等级",
            prop: "levelName",
          },
          {
            label: "河长身份",
            prop: "rpostName",
          },
          {
            label: "河长姓名",
            prop: "realName",
          },
          {
            label: "河长职务",
            prop: "post",
          },
          {
            label: "联系方式",
            prop: "phone",
          }
        ],
        channelLnformationList: [
          {
            label: "行政区划",
            prop: "areaName",
          },
          {
            label: "所属流域",
            prop: "watershedName",
          },
          {
            label: "河道等级",
            prop: "levelName",
          },
          {
            label: "起点名称",
            prop: "startName",
          },
          {
            label: "终点名称",
            prop: "endName",
          },
          {
            label: "河道长度",
            prop: "length",
          }
        ],
        panoramagram: "",
        title: "",
        // 巡查列表表头
        columnLists: [
          {
            width: 110,
            prop: "patrolUserName",
            label: "巡查人员",
          },
          {
            width: 160,
            prop: "deptName",
            label: "所属单位",
          },
          {
            width: 160,
            prop: "startTime",
            label: "开始时间",
          },
          {
            width: 160,
            prop: "endTime",
            label: "结束时间",
          },
          {
            width: 160,
            prop: "inspectTime",
            label: "巡查时长(h)",
            sortable: true,
          },
          {
            width: 160,
            prop: "inspectLength",
            label: "巡查长度(km)",
            sortable: true,
          },
          {
            width: 160,
            prop: "problemNum",
            label: "巡查问题(个)",
            sortable: true,
          }
        ],
        // 表头
        columnList: [
          {
            width: 160,
            prop: "tm",
            label: "时间",
          },
          {
            width: 110,
            prop: "intake",
            label: "取水量（m³）",
          },
        ],
        dataList: [],
        tabSwitch: "日取水量",
        yearData: [],
        overviewData: [],
        legendName: ["取水量", "取水累积量"],
        waterLevelArr: [
          {
            label: "编号",
            prop: "stationCode",
          },
          {
            label: "水位站名称",
            prop: "stationName",
          },
          {
            label: "sim卡卡号",
            prop: "sim",
          },
          {
            label: "表具号",
            prop: "meterNumber",
          },
          {
            label: "每日最大值",
            prop: "maxValue",
          },
          {
            label: "行政区划",
            prop: "areaName",
          },
          {
            label: "取水地点",
            prop: "waterPlace",
          },
          {
            label: "管理单位",
            prop: "manageUnit",
          },
          {
            label: "负责人",
            prop: "chargePerson",
          },
          {
            label: "负责人电话",
            prop: "chargePersonPhone",
          },
          {
            label: "日常管理单位",
            prop: "dailyChargeUnit",
          },
        ],
        pumpWellArr: [
          {
            label: "取水井代码",
            prop: "code",
          },
          {
            label: "取水井名称",
            prop: "name",
          },
          {
            label: "取水井所在位置",
            prop: "address",
          },
          {
            label: "井深",
            prop: "deep",
          },
          {
            label: "井口井管内径",
            prop: "internalDiameter",
          },
          {
            label: "井壁管材料",
            prop: "meterials",
          },
          {
            label: "成井年份",
            prop: "buildYear",
          },
          {
            label: "设计年取水量（万m³）",
            prop: "yearUseWater",
          },
          {
            label: "水源类型",
            prop: "waterResourceType",
          },
          {
            label: "备注",
            prop: "remark",
          },
        ],
        form:{},
        nameData: '工程基本信息',
        affiliatedUniData: null,
        endTime: null,
        startTime: null,
        inspectorsData: null,
        backData: false,
        rivSegInfoList: {},
        stats4LoadPageList: {},
        loadPageList: [],
        affiliatedUnitData:null,
        options: [],
        tmData: [],
        csczData: [],
        jskzData: [],
        jsczData: [],
        llData: [],
        waterStationsList: []
      };
    },
    components:{
      LookFile,
      ProjectItem,
      PrimaryVideo,
      PrimarySubhead,
      PrimaryMapTable,
      ComparativeHistoryMap
    },
    created() {
      // this.getSelectData("unitType");
    },
    mounted() {
      this.$nextTick(() => {
        this.title = this.ProgrammeData.name 
        || this.ProgrammeData.wiuName 
        || this.ProgrammeData.stationName 
        || this.ProgrammeData.projectName 
        || this.ProgrammeData.stName
        || this.ProgrammeData.outletName
        || this.ProgrammeData.address;
        if (this.ProgrammeData.type == '地下水位站') {
          this.PublicHeaderList = this.waterLevelArr;
          this.form = this.ProgrammeData;
        }else if (this.ProgrammeData.type == '地下机井') {
          this.PublicHeaderList = this.pumpWellArr;
          this.form = this.ProgrammeData;
        }else if (this.ProgrammeData.type == '泵站工情') {
          this.waterStationInfo();
        }
        if(this.ProgrammeData.type == '河道') {
          this.tabSwitch = '基础信息';
          this.rivSegInfoById(this.ProgrammeData.id);
          this.stats4LoadPage();
          this.loadPage();
        }
        if (this.ProgrammeData.type == '在建工程') {
          this.init(this.ProgrammeData);
        }
        this.panoramagram = this.ProgrammeData.browseUrl;
        // this.intakeCurve("1", this.ProgrammeData.wainCode);
      });
    },
    updated() {
      this.$nextTick(()=>{
        this.getProjectEnv();
        this.setLine();
      })
    },
    watch: {
      //监听点击的table 栏，初始化分页条件，请求数据
      activeIndex(val) {
        switch (val) {
          case 1:
            this.initPageObj();
            this.getReplyFindById();
            break;
          case 2:
            this.initPageObj();
            this.getCompanyList();
            break;
          case 3:
            this.initPageObj();
            this.getVideoList();
            break;
          case 4:
            this.initPageObj();
            this.getfileLists();
            break;
          case 5:
            this.initPageObj();
            this.getFolderList();
            break;
          case 6:
            this.initPageObj();
            this.getWageRegulationList();
            break;
          case 7:
            this.initPageObj();
            this.getAuditManagementList();
            break;
          default:
            this.activeVideo=-1;
            this.videoCode="";
        }
      },
    },
    methods: {
      timeHandle(val) {
        this.time = val;
        this.waterStationInfo();
      },
      // 泵站工情数据接口
      async waterStationInfo() {
        this.tmData = [];   //时间
        this.csczData = []; //出水池
        this.jskzData = []; //进水口
        this.jsczData = []; //进水池
        this.llData = []; //流量
        let opt = {
          days: this.time,
          stationCode: this.ProgrammeData.stCode,
          stationType: 'PT'
        };
        let res = await hydraulicApi.waterStationInfo(opt);
        if(res.code == 0) {
          this.waterStationsList = res.data.data;
          res.data.data.forEach((val)=> {
            this.tmData.push(val.tm);
            this.csczData.push(val.cscZ);
            this.jskzData.push(val.jskZ);
            this.jsczData.push(val.jscZ);
            this.llData.push(val.ll);
          });
          this.$nextTick(()=>{
            this.pumpStations();
          })
        }
      },
      // 泵站工情
      pumpStations() {
        var chartDom = document.getElementById('pumpStationChart');
        var myChart = echarts.init(chartDom);
        var option;
        option = {
          legend: {
            textStyle: {
              color: '#fff'
            }, 
            data: ['进水口水位', '进水池水位', '出水池水位', '流量']
          },
          grid: {
            left: '2%',
            right: '3%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
              type: 'category',
              axisLine: {
                show: false
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color: '#fff'
                }
              },
              axisTick: {
                show: false
              },
              data: this.tmData
          },
          yAxis: {
              type: 'value',
              axisLine: {
                show: false
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color: '#fff'
                }
              }, 
              axisTick: {
                show: false
              }
          },
          series: [{
            name: '进水口水位',
            symbol: 'none',    //去掉折线上的小圆点
            data: this.jskzData,
            type: 'line',
            color: '#1CFFF5',
            smooth: true
          },{
            name: '进水池水位',
            symbol: 'none',    //去掉折线上的小圆点
            data: this.jsczData,
            type: 'line',
            color: '#FF551F',
            smooth: true
          },{
            name: '出水池水位',
            symbol: 'none',    //去掉折线上的小圆点
            data: this.csczData,
            type: 'line',
            color: '#E80000',
            smooth: true
          },{
            name: '流量',
            symbol: 'none',    //去掉折线上的小圆点
            data: this.llData,
            type: 'line',
            color: '#FFC41C',
            smooth: true
          }]
        };
        option && myChart.setOption(option);
      },
      /**
       * methods: 初始化方法
       * params  : obj 项目对象，包含需要的一些信息
       *
       */
      init(obj) {
        this.activeIndex = 0;
        this.showDialog = true;
        this.projectObj = obj;
        if (obj.hasOwnProperty("onlineStatus")) {
          this.showProjectInfor = false;
          this.cameraIndexCode=obj.cameraIndexCode;
          this.showVideo = true;
        } else {
          this.showVideo = false;
          this.showProjectInfor = true;
          this.getProjectInfo();
        }
      },
      async getProjectEnv() {
        let data = {
          projectId: this.ProgrammeData.projectId,
          timeType: 2,
        };
        let res = await hydraulicApi.getRainDataList(data);
        if (res.code == 0) {
          this.setBar();
        }
      },
      clickCompany(i, name) {
        this.companyIndex = name;
        this.selfCompanyUser = this.personelArr[name];
      },
      // 字典查询单位信息
      async getSelectData(str) {
        let res = await hydraulicApi.dataSearch({dictCode: str});
        if (res.code == 0) {
          this.companyArr = res.data;
          this.companyIndex = res.data[0].dictName;
        }
      },
      //关闭弹框。初始化table 标签切换条件
      closeWrapper() {
        this.showDialog = false;
        this.showVideo = false;
        this.showProjectInfor = false;
        this.projectObj = null;
        this.cameraIndexCode="";
      },
      //获取点击的table 栏 index
      changeMenu(index) {
        this.activeIndex = index;
      },
      // 项目切换
      projectChangeMenu(val) {
        this.projectActiveIndex = val;
      },
      /**
       * methods:getProjectInfo 获取工程基本信息
       * params： id   项目id
       *
       */
      async getProjectInfo() {
        let data = {
          projectId: this.projectObj.projectId,
        };
        await hydraulicApi.projectFindById(data)
          .then((res) => {
            if (res.code != 0) {
              this.$message.error("查询失败！");
            } else {
              this.projectObj = res.data;
            }
          });
      },
      async getReplyFindById() {
        let data = {
          projectId: this.projectObj.projectId, //this.projectId
        };
        await hydraulicApi.replyFindById(data).then((res) => {
          if (res.code != 0) {
            this.$message.error("查询失败！");
          } else {
            this.replyObj = res.data;
            let fileIDs= this.replyObj.engineeringRenderings.split(',');
            fileIDs.forEach(id=>{
              this.getFile(id);
            })
          }
        });
      },
      // 获取图片id
      async getFile(id) {
        this.fileList = [];
        await getFileList({ ids: id }).then((res) => {
          if (res.code == 0) {
            let info = res.data.data;
            info.map((item) => {
              this.fileList.push({
                name: item.fileName,
                url: item.fileUrl,
                id: item.id,
              });
            });
          }
        });
      },
      /**
       * methods:getCompanyList 获取参建单位信息
       * params： id   项目id
       *
       */
      async getCompanyList() {
        let data = {
          projectId: this.projectObj.projectId,
        };
        await hydraulicApi.companyList(data).then((res) => {
          if (res.code != 0) {
            this.$message.error("查询失败！");
          } else {
            this.constructerList = res.data;
          }
        });
      },
      setBar() {
      // lineMain
      var chartDom = document.getElementById("barMain");
      var myChart = echarts.init(chartDom);
      var option;
      option = {
        grid: {
          top: "10",
          bottom: "30",
        },
        xAxis: {
          data: ["00:00", "00:10", "00:11", "00:20", "00:30", "00:40", "01:20"],
          axisLabel: {
            textStyle: {
              color: "#fff",
              fontSize: "12",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          z: 10,
        },
        yAxis: {
          type: "value",
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            textStyle: {
              color: "#fff",
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: ["#999"],
              width: 1,
              type: "solid",
            },
          },
        },
        dataZoom: [
          {
            type: "inside",
          },
        ],
        series: [
          {
            data: [
              Math.random() * 400 + 9,
              Math.random() * 400,
              Math.random() * 400,
              Math.random() * 400,
              Math.random() * 400,
              Math.random() * 400,
              Math.random() * 400,
            ],
            type: "bar",
            barWidth: 8,
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  1,
                  0,
                  0,
                  [
                    {
                      offset: 0,
                      color: "#42a794", // 0% 处的颜色
                    },
                    {
                      offset: 0.6,
                      color: "#98bb5f", // 60% 处的颜色
                    },
                    {
                      offset: 1,
                      color: "#B3DD6F", // 100% 处的颜色
                    },
                  ],
                  false
                ),
              },
            },
          },
        ],
      };
      option && myChart.setOption(option);
    },
     setLine(id) {
      var chartDom = document.getElementById("lineMain");
      var myChart = echarts.init(chartDom);
      var option;
      var dataNum = Math.random() * 1000;
      option = {
        grid: {
          top: "10",
          bottom: "30",
        },
        xAxis: {
          type: "category",
          data: [
            "00:00",
            "04:50",
            "08:11",
            "12:00",
            "16:10",
            "17:00",
            "20:01",
            "22:00",
          ],
          axisLabel: {
            textStyle: {
              color: "#fff",
              fontSize: "12",
            },
          },
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          z: 10,
        },
        yAxis: {
          type: "value",
          axisLine: {
            show: false,
          },
          axisTick: {
            show: false,
          },
          axisLabel: {
            textStyle: {
              color: "#fff",
            },
          },
          splitLine: {
            show: true,
            lineStyle: {
              color: ["#999"],
              width: 1,
              type: "solid",
            },
          },
        },
        dataZoom: [
          {
            type: "inside",
          },
        ],
        series: [
          {
            data: [
              Math.random() * 1000 + 9,
              Math.random() * 1000,
              Math.random() * 1000,
              Math.random() * 1000,
              Math.random() * 1000,
              Math.random() * 1000,
              Math.random() * 1000,
            ],

            type: "line",
            smooth: true,
            symbol: "none",
            lineStyle: {
              color: "#1c920c",
            },
          },
        ],
      };

      option && myChart.setOption(option);
    },








      //获取所有视频点位
      async getVideoList() {
        let data = {
          keyWord:null,
          pageNum: 1,
          pageSize: 100,
          projectId: this.projectObj.projectId,
        };
        await hydraulicApi.videoList(data).then((res) => {
          if (res.code != 0) {
            this.$message.error("查询失败！");
          } else {
            this.videoList=res.data.list;
          }
        });
      },
      //拿到点击视频点位的 视频code 嘛，为当前选中附加样式
      changVedio(index,videoCode){
        this.videoCode=videoCode.cameraIndexCode;
        this.activeVideo=index;
      },
      async getAuditManagementList() {
        let data = {
          projectId: this.projectObj.projectId,
          pageNum: this.pageObj.pageNum,
          pageSize: this.pageObj.pageSize,
        };
        await hydraulicApi.getList(data).then((res) => {
          if (res.code != 0) {
            this.$message.error("查询失败！");
          } else {
            this.insepctList = res.data.list;
            this.pageObj.pageSize = res.data.pageSize;
            this.pageObj.pageNum = res.data.pageNum;
            this.pageObj.totalPages = res.data.totalRows;
          }
        });
      },
      /**
       *  methods :   获取档案信息
       *
       */
      async getfileLists() {
        let data = {
          projectId: this.projectObj.projectId,
        };
        await hydraulicApi.getFileList(data).then((res) => {
          if (res.code != 0) {
            this.$message.error("查询失败！");
          } else {
            let arr = res.data;
            for (let i = 0; i < arr.length; i++) {
              arr[i].fileLength = 0;
              if (arr[i].templateList[0]) {
                for (let y = 0; y < arr[i].templateList.length; y++) {
                  let yitem = arr[i].templateList[y];
                  if (yitem.fileId) {
                    let fileArr = yitem.fileId.split(",");
                    arr[i].fileLength += fileArr.length;
                    yitem.fileLength = fileArr.length;
                  }
                }
              }
            }
            this.fileLists = res.data;
          }
        });
      },
      /**
       * methods:     //获取工资监管信息
       *
       */
      async getWageRegulationList() {
        let data = {
          projectId: this.projectObj.projectId,
          pageNum: this.pageObj.pageNum,
          pageSize: this.pageObj.pageSize,
        };
        await hydraulicApi.listWageRegulation(data).then((res) => {
          if (res.code != 0) {
            this.$message.error("查询失败！");
          } else {
            this.wagesList = res.data.list;
            this.pageObj.pageSize = res.data.pageSize;
            this.pageObj.pageNum = res.data.pageNum;
            this.pageObj.totalPages = res.data.totalRows;
          }
        });
      },
      //工资监管页码变化请求数据
      wageChangePage(val) {
        this.pageObj.pageNum = val;
        this.getWageRegulationList();
      },
      handleSizeChange(val) {
        console.log(val);
      },
      handleCurrentChange(val) {
        console.log(val);
      },
      //展示图片预览弹框
      showFileWrapper(fileId) {
        this.showFileDialog = true;
        this.fileId = fileId;
      },
      //初始化分页条件
      initPageObj() {
        this.pageObj = {
          pageNum: 1,
          pageSize: 10,
          totalPages: 0,
        };
      },
      //关闭图片预览弹框
      closeFileDialog() {
        this.showFileDialog = false;
        this.fileId = "";
      },
      //获取工程档案文件加
      async getFolderList() {
        let data = {
          projectId: this.projectObj.projectId,
        };
        await hydraulicApi.getListFolder(data).then((res) => {
          if (res.code != 0) {
            this.$message.error(res.message);
          } else {
            this.treeData = res.data;
          }
        });
      },
      //获取table 数据
      async getTableList() {
        let sourceData = this.currentNode;
        let arr = this.getTreeFolderIds(sourceData);
        let data = {
          folderIds: arr.toString(),
          projectId: this.projectId,
          pageNum: this.pageObj.pageNum,
          pageSize: this.pageObj.pageSize,
        };
        await hydraulicApi.listStandardFile(data).then((res) => {
          if (res.code != 0) {
            this.$message.error(res.message);
          } else {
            this.pageNum = res.data.pageNum;
            this.totalRows = res.data.totalRows;
            this.tableData = res.data.list;
          }
        });
      },
    
      getTreeFolderIds(sourceData) {
        let result = [];
        let getLeaf = (data) => {
          data.forEach((item) => {
            result.push(item.standardFileId);
            getLeaf(item.childrenList);
          });
        };
        getLeaf([sourceData]);
        return result;
      },
      //页面切换，获取数据
      currentChange(num) {
        this.pageObj.pageNum = num;
        this.getTableList();
      },
        //点击对应行，获取对应的子树状数据
      nodeClick(data, node) {
        // this.pageNum = 1;
        // this.standardFileId = data.standardFileId;
        this.currentNode = data;
        this.getTableList();
      },
      //修改样式
      cellstyle() {
        return {
          padding: "0px",
          height: "47px",
        };
      },
      // 分页查找上面的统计
      async stats4LoadPage() {
        let opt = {
          patrolUserName: this.inspectorsData, //巡查人员姓名
          deptName: this.affiliatedUnitData,   //所属单位
          startTime: this.startTime,           //巡查开始时间
          endTime: this.endTime,               //巡查开始时间
           id: this.ProgrammeData.id
         }
         await hydraulicApi.stats4LoadPage(opt).then((res) => {
          if (res.code == 0) {
            this.stats4LoadPageList = res.data;
          }
        });
      },
      // 巡查列表接口
       async loadPage() {
         let opt = {
          patrolUserName: this.inspectorsData, //巡查人员姓名
          deptName: this.affiliatedUnitData,   //所属单位
          startTime: this.startTime,           //巡查开始时间
          endTime: this.endTime,               //巡查开始时间
          //  id: this.ProgrammeData.id
         }
         await hydraulicApi.loadPage(opt).then((res) => {
          if (res.code == 0) {
            this.loadPageList = res.data.list;
          }
        });
       },
      // 河长选择
      riverChiefClick(val) {
        this.riverChief = val;
        let str = this.rivSegInfoList.riverChiefList.find((item) => {
          if (item.realName == val) {
            return item;
          }
        });
        this.riverChiefData = str;
      },
      // 河道健康码，基础信息，健康信息接口
      async rivSegInfoById(val) {
        this.riverChiefName = [];
        await hydraulicApi.rivSegInfoById({id: val,withChiefInfo:true}).then((res) => {
          if (res.code == 0) {
            this.rivSegInfoList = res.data;
            this.riverChiefData = res.data.riverChiefList[0];
            this.riverChief = res.data.riverChiefList[0].realName
            res.data.riverChiefList.forEach((item)=> {
              this.riverChiefName.push(item.realName);
            })
          }
        })
      },
      // 巡查路径返回
      backtrack() {
        this.backData = false;
      },
      // 巡查列表点击某一行事件
      rowClick(val) {
        this.inspectorsList = val;
        this.backData = true;
      },
      // 巡查查询按钮
      inquire() {
        this.loadPage();
        this.stats4LoadPage();
      },
      // 巡查筛选清楚按钮
      empty() {
        this.affiliatedUnitData = null;
        this.inspectorsData = null;
        this.startTime = null;             //巡查开始时间
        this.endTime = null;               //巡查开始时间
      },
      // 健康信息雷达图
      radarMapApi() {
        var chartDom = document.getElementById('radarMap');
        var myChart = echarts.init(chartDom);
        var option = {
            // title: {
            //     text: '基础雷达图'
            // },
            tooltip: {
                trigger: 'axis'
            },
            // legend: {
            //     data: ['无乱点河道创建', '河湖管护到位']
            // },
            radar: {
                // shape: 'circle',
                indicator: [
                    { name: '河湖“四乱”清零'},
                    { name: '无乱点河道创建'},
                    { name: '水域岸线占补平衡'},
                    { name: '涉河建设项目批建相符'},
                    { name: '公众满意'},
                    { name: '河湖管护到位'}
                ],
                // axisLine: {            // 坐标轴线
                //     show: false        // 默认显示，属性show控制显示与否
                // },
                name:{
                  textStyle:{color:'#fff'}
                 },
                splitArea : {
                    show : true,   
                    areaStyle : {
                      opacity: 0.2,
                      color: ["#050E30"]  // 图表背景网格的颜色
                    }
                },
                splitLine : {
                    show : true,
                    lineStyle : {
                        width : 1,
                        color : '#fff' // 图表背景网格线的颜色
                    }
                },
                radius: 130,
            },
            series: [{
                // name: '预算 vs 开销（Budget vs spending）',
                //  tooltip: {
                //     trigger: 'item'
                // },
                symbol: "none", // 去掉图表中各个图区域的边框线拐点
                type: 'radar',
                itemStyle: {
                    normal: {
                        color : "#33FFFF", // 图表中各个图区域的边框线拐点颜色
                        // lineStyle: {
                        //     color:"#33FFFF" // 图表中各个图区域的边框线颜色
                        // },
                        areaStyle: {
                            type: 'default'
                        }
                    }
                },
                data: [
                    {
                        value: [1, 1, 0, 0, 1, 1],
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    type: 'default',
                                    opacity: 0.8,
                                    color: "#33FFFF" // 图表中各个图区域的颜色
                                }
                            }
                        },
                        name: '无乱点河道创建'
                    },
                    {
                        value: [1, 1, 0, 0, 1, 1],
                        itemStyle: {
                            normal: {
                                areaStyle: {
                                    type: 'default',
                                    opacity: 0.8,
                                    color: "red" // 图表中各个图区域的颜色
                                }
                            }
                        },
                        name: '河湖管护到位'
                    }
                ]
            }]
        };
        option && myChart.setOption(option);
      },
      // tab切换
      tabSwitchClick(val) {
        this.tabSwitch = val;
        if (val == "日取水量") {
          this.intakeCurve("1", this.ProgrammeData.wainCode);
        } else if (val == "月取水量") {
          this.intakeCurve("2", this.ProgrammeData.wainCode);
        } else if (val == '健康信息') {
          this.$nextTick(()=>{
            this.radarMapApi();
          });
        }
      },
      // 取水口曲线接口数据
      async intakeCurve(val, type) {
        let obj = {
          wainCode: type, //取水口编码
          type: val, //1，日取水量；2，月取水量
        };
        await hydraulicApi.intakeCurve(obj).then((res) => {
          if (res.code == 0) {
            this.overviewData = [];
            this.yearData = [];
            this.dataList = res.data;
            let arr = [];
            let str = [];
            res.data.forEach((val) => {
              arr.push(val.intake);
              str.push(val.intakeSum);
              this.yearData.push(val.tm2Str);
            });
            this.overviewData.push(
              {
                name: "取水量",
                type: "bar",
                barWidth: "25",
                color: "#56FEFE",
                data: arr,
              },
              {
                name: "取水累积量",
                type: "line",
                color: "#56FEFE",
                yAxisIndex: 1,
                data: str,
              }
            );
            this.histogram();
          }
        })
      },
      // 柱状，折线图
      histogram() {
        var chartDom = document.getElementById("histogramChart");
        var myChart = echarts.init(chartDom);
        var option;
        option = {
          tooltip: {
            trigger: "axis",
            axisPointer: {
              type: "cross",
              crossStyle: {
                color: "#999",
              },
            },
          },
          legend: {
            top: "0",
            right: "20",
            data: this.legendName,
            textStyle: {
              color: "#fff", //这里用参数代替了
            },
          },
          grid: {
            top: "60",
            left: "10",
            right: "30",
            bottom: "10",
            containLabel: true,
          },
          xAxis: [
            {
              type: "category",
              data: this.yearData,
              axisPointer: {
                type: "shadow",
              },
              axisLine: {
                show: false,
              },
              axisTick: {
                show: false,
              },
              axisLabel: {
                show: true,
                textStyle: {
                  color: "#fff", //这里用参数代替了
                },
              },
            },
          ],
          yAxis: [
            {
              type: "value",
              name: "取水量（m³）",
              nameTextStyle: {
                color: "#fff",
                fontSize: 12,
              },
              axisLabel: {
                formatter: "{value}",
                show: true,
                textStyle: {
                  color: "#fff", //这里用参数代替了
                },
              },
            },
            {
              type: "value",
              name: "取水累积量（m³）",
              nameTextStyle: {
                color: "#fff",
                fontSize: 12,
              },
              axisLabel: {
                formatter: "{value}",
                show: true,
                textStyle: {
                  color: "#fff", //这里用参数代替了
                },
              },
            },
          ],
          series: this.overviewData,
        };
        option && myChart.setOption(option, true);
      },
      // 查询一项完整的规划项目
      async loadFullPlanProj(val) {
        await hydraulicApi.loadFullPlanProj({ id: val }).then((res) => {
          if (res.code == 0) {
            this.form = res.data;
          }
        });
      },
      handleClose() {
        this.$emit("handleClose");
      },
      peripheralAnalysis() {
        this.$emit("analysis", this.ProgrammeData.type);
      },
      minimize() {
        this.$emit("minimize");
      },
    },
  }
</script>

<style lang="scss">
.primary-dialog {
  .el-dialog {
    background-image: linear-gradient(
      180deg,
      rgba(8, 42, 53, 1) 2%,
      rgba(5, 14, 48, 1) 100%
    );
    .el-dialog__header {
      height: 54px;
      padding: 18px;
      border: 1px solid #00c1ff;
      // box-shadow: inset 0 4px 30px 0 #2351ef40;
      background: transparent;
      border-bottom: 0;
      margin: 2px 2px 0;
      .primary-dialog__title {
        height: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-right: 40px;
        .primary-dialog__title--label {
          background: url(~assets/images/WaterDisastersImage/title-bg.png)
            no-repeat;
          // background-size: 100% 100%;
          font-size: 16px;
          color: #fff;
          letter-spacing: 0;
          height: 32px;
          width: 80%;
          // margin-left: 16px;
          padding-left: 56px;
          font-weight: bold;
          line-height: 32px;
        }
        .primary-dialog__title--minimize {
          cursor: pointer;
          margin-right: 48px;
        }
      }
      .el-dialog__headerbtn {
        .el-dialog__close {
          color: #00c1ff;
          font-weight: bold;
        }
      }
    }
    .el-dialog__body {
      min-height: 100px;
      padding: 0 18px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      border-bottom: 0;
      margin: 0 2px;
    }
    .health-code-sty {
      text-align: center;
      margin-bottom:50px;
    }
    .health-sty {
      color: #fff;
      display: flex;
      justify-content: space-between;
      padding:15px 0;
      >p {
        font-size: 16px;
        font-weight: 600;
        >span {
          color:#33FFFF;
          font-size: 18px;
          padding-left:20px;
        }
      }
    }
    .el-collapse-item__header {
      background-color: rgba(8,42,53,0.2);
      color: #fff;
      border-bottom: 0 solid #00C1FF;
    }
    .el-collapse-item__content {
      color: #fff;
      padding-bottom: 0;
      background: rgba(1, 9, 14, 0.9);
    }
    .el-collapse {
      border-top: 0 solid #EBEEF5;
      border-bottom: 0 solid #EBEEF5;
    }
    .under-frame-sty {
      border-bottom: 1px solid rgba($color: #00C1FF, $alpha: 0.2) ;
    }
    .btn-sty {
      background-image: linear-gradient(180deg, #36C0E4 20%, #0639FF 100%);
      border-radius: 4px;
      padding: 13px 15px;
      font-size:16px;
      display: inline-block;
      color:#fff;
      margin-right: 10px;
      margin-top: 25px;
    }
    .inspect-number-sty {
      padding-bottom: 15px;
      padding-right: 50px;
      font-size: 16px;
      display: flex;
      justify-content: space-between;
      > div {
        color: #fff;
        >p:nth-child(2) {
          margin-top: 10px;
          > span {
            margin-right:10px;
            font-size: 22px;
            color: #33FFFF;
          }
        }
      }
    }
    .boundary-sty {
      width: 2px;
      height: 460px;
      float: right;
      background-image: linear-gradient(0deg, rgba(12,61,54,0.02) 0%, rgba(28,255,245,0.50) 54%, rgba(12,61,54,0.50) 100%);
    }
    .riverway-sty {
      height: 50px;
      margin: 0 38px 20px 0;
      border: 1px solid #00C1FF;
      >span:nth-child(1) {
        color: #56FEFE;
        text-align: center;
        padding: 0 15px;
        height: 48px;
        line-height: 48px;
        display: inline-block;
        border-right: 1px solid #00C1FF;
        background-image: linear-gradient(180deg, rgba(8,42,53,0.90) 2%, rgba(5,14,48,0.90) 100%);
        box-shadow: inset 0 4px 30px 0 rgba(35,81,239,0.25);
      }
      >span:nth-child(2) {
        color: #fff;
        padding-left:15px;
      }
    }
    .el-dialog__footer {
      padding: 0 18px 24px;
      background: transparent;
      border: 1px solid #00c1ff;
      border-top: 0;
      margin: 0 2px 4px;
      .dialog-footer {
        .el-button {
          padding: 6px 16px;
          &.cancel {
            color: #1ef8f2;
            border: 1px solid #1ef8f2;
            background-color: transparent;
            border-radius: 4px;
          }
          &.confirm {
            color: #fff;
            border: 0;
            background-image: linear-gradient(
              180deg,
              #36c0e4 20%,
              #0639ff 100%
            );
          }
        }
      }
    }
    .tab-switch {
      padding: 5px 10px;
      margin-right: 15px;
      color: #56fefe;
      width: 150px;
      height: 30px;
      line-height: 30px;
      border: 1px solid #56fefe;
      box-shadow: inset 0 0 5px 0 #56FEFE;
    }
    .tab-switch-sty {
      padding: 5px 10px;
      margin-right: 15px;
      color: #fff;
      width: 160px;
      height: 30px;
      line-height: 30px;
      border: 1px solid #fff;
    }
    .el-table {
      .cell {
        height: 37px;
        line-height: 37px;
      }
      .el-checkbox__inner {
        border: 0.0625rem solid #00c1ff;
        background-color: #00c1ff;
        background: rgba($color: #000000, $alpha: 0);
      }
      .el-table__body tr.current-row > td {
        background-color: rgba(41, 147, 180, 0.3);
      }
      th,
      td {
        padding: 0;
        font-size: 14px;
        color: #fff;
      }
      th {
        padding: 3px 0;
        font-size: 14px;
        background-color: rgba(41, 147, 180, 0.1);
        font-weight: bold;
        color: #00c1ff;
      }
    }
    &:after {
      content: "";
      position: absolute;
      height: calc(100% + 4px);
      width: 100%;
      top: -2px;
      left: 0;
      background: linear-gradient(to bottom, #35b9e3, #5398f6) left top
          no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) left top no-repeat,
        linear-gradient(to bottom, #35b9e3, #5398f6) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #608aff) right top no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) left bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) left bottom no-repeat,
        linear-gradient(to bottom, #5398f6, #35b9e3) right bottom no-repeat,
        linear-gradient(to bottom, #608aff, #5398f6) right bottom no-repeat;
      /*设置大小*/
      background-size: 18px 2px, 2px 18px;
      z-index: -1;
    }
  }
  &.project-dialog-wrap {
    ::v-deep .el-dialog {
      margin-left: 20px;
      margin-top: 10vh !important;
    }
  }
}
.peripheral-analysis {
  display: inline-block;
  margin-left: 10px;
  cursor: pointer;
  background: url(~assets/images/peripheral_analysis.png) no-repeat;
  background-size: 100% 100%;
  width: 15px;
  height: 15px;
}
.minimize {
  color: #00c1ff;
  font-weight: bold;
  cursor: pointer;
}
.nav-bar ul {
  // background: #fff;
  color: #fff;
  font-size: 0.875rem;
  li {
    display: inline-block;
    font-size: 0.875rem;
    padding: 1rem 1.25rem;
    cursor: pointer;
    span {
      padding: 0.25rem 1rem;
    }
  }
  .active {
    color: #fff;
    background-image: linear-gradient(180deg, #71c1a9 1%, #07bbb9 100%);
    border-radius: 0.25rem;
  }
}
.content-body {
  // background: #fff;
  color: #fff;
  padding: 20px 20px;
  width: 100%;
  height: 100%;
  .pagenation-box {
    text-align: right;
    margin: 10px 10px;
  }
  .constructer-info {
    .video-wrapper-left {
      display: inline-block;
      width: 18.75rem;
      height: 100%;
      overflow: auto;
      padding: 0.625rem 0;
      height: 100%;
      border: 0;
      font-size: 0.875rem;
      vertical-align: top;
      ul li {
        height: 1.625rem;
        line-height: 1.625rem;
        cursor: pointer;
        text-align: left;
        margin-bottom: 2px;
        span{
            padding: 3px 5px;
        }
      }
    }
    .video-wrapper-right {
      display: inline-block;
      width: calc(100% - 19rem);
      height: 500px;
      padding: 0.625rem;
      z-index: 2000;
      .video-wrapper {
        width: 100%;
        height: 100%;
      }
    }
  }
}
.grid-content {
  height: 28px;
  line-height: 28px;
  font-size: 14px;
  color: #fff;
  font-weight: 400;
}
.grid-value {
  color: #fff;
  font-size: 16px;
  font-weight: 400;
  line-height: 40px;
  padding-left: 12px;
}
.titleMidel {
  height: 3rem;
  display: flex;
  align-items: center;
  p {
    border-left: 3px solid #328173;
    padding-left: 8px;
    line-height: 20px;
    color: #fff;
    letter-spacing: 0;
    font-weight: 600;
  }
}
.showFile-span {
  color: #2a7267;
  font-size: 14px;
  cursor: pointer;
}
.videoWrapper {
  height: 100%;
}
.video-box {
  width: 650px;
  height: 450px;
  margin: 4% auto;
}
.activeVideoBg{
  background: #217aff;
  color:#fff;
}
</style>
<style lang="scss" scoped>
.tabsBox {
  background: rgba(24, 60, 54, 0.8);
  box-sizing: border-box;
  padding: 18px 28px 33px 20px;
}
.tabList {
  display: flex;
  align-items: center;
  box-sizing: border-box;
  padding: 0 0 26px 0;
  color: #fff;
  justify-content: space-between;
  position: relative;
  top: 0;
  right: 0;
}
.tabList > div:nth-child(1) {
  display: flex;
  flex: 1;
}
.tabList > div:nth-child(2) {
  cursor: pointer;
  color: #fff;
  position: absolute;
  top: -50px;
  right: 25px;
}
.tabItem {
  border-radius: 4px;
  box-sizing: border-box;
  padding: 2px 18px;
  margin-right: 24px;
  cursor: pointer;
}
.activeClass {
  background-image: linear-gradient(180deg, #6fc1aa 0%, #09bbb8 100%);
}
.titleName {
  height: auto;
  font-weight: bold;
  font-size: 16px;
  color: #fff;
  border-left: 4px solid #32bcb4;
  padding-left: 0.625rem;
  margin-bottom: 16px;
}
.content-panel-item {
  margin-bottom: 25px;
  &:last-child {
    margin-bottom: 0;
  }
}
.title-panel {
  color: #fff;
  font-size: 18px;
  display: flex;
  align-items: center;
}
.content-box {
  margin-top: 15px;
}

.img-panel {
  img {
    display: inline-block;
    width: 150px;
    height: 100px;
    margin-right: 10px;
  }
}

.el-col {
  color: #fff;
  font-size: 15px;
  line-height: 38px;
  &:last-child {
    margin-bottom: 0;
  }
  .value {
    color: #33ffff;
    font-size: 16px;
    margin-left: 20px;
  }
}

.qsl-ld-dialog-line {
  display: inline-block;
  height: 3px;
  width: 100%;
  background-image: url(~assets/images/qsl-ld-dialog-line.png);
  background-size: 100% 100%;
}
.envStatistics {
  margin-bottom: 16px;
  display: flex;
  text-align: center;
}
.envStatisticsItem {
  width: 25%;
}
.marginBottom6 {
  margin-bottom: 6px;
  font-size: 12px;
  color: #fff;
  > img {
    width: 6px;
    height: 6px;
    vertical-align: middle;
    margin-right: 8px;
  }
}
.colourGreen {
  color: #2bc6c8;
}
.colourBlue {
  color: #2da4ff;
}
.colourRed {
  color: #ff2d2d;
}
.colourYellow {
  color: #ffa751;
}
.envName {
  font-size: 24px;
}
.spanVertical {
  display: inline-block;
  vertical-align: middle;
}
.marginRight8 {
  margin-right: 8px;
}
.marginBottom12 {
  margin-bottom: 12px;
}
.companyBox {
  display: flex;
  border-bottom: 1px solid rgb(150, 148, 148);
  margin-bottom: 24px;
}
.companyItem {
  height: 24px;
  margin-right: 54px;
  cursor: pointer;
  color: #fff;
}
.activeBorder {
  color: #32bcb4;
  border-bottom: 2px solid #32bcb4;
}
.companyUserBox {
  display: flex;
  //   justify-content: space-between;
  flex-wrap: wrap;
  max-height: 20rem;
  overflow-y: auto;
}
.companyUser {
  width: 45%;
  display: flex;
  font-size: 12px;
  margin-right: 26px;
  margin-bottom: 12px;
  > img {
    width: 90px;
    height: 100px;
    margin-right: 18px;
  }
}
.font16 {
  font-size: 16px;
}
.font14 {
  font-size: 14px;
}
.colorABD {
  color: #2abdb4;
}
.companyUserMsg {
  color: #fff;
  width: calc(100% - 108px);
  > div:first-child {
    font-size: 18px;
    margin-bottom: 20px;
  }
  > div:last-child {
    background: rgba(48, 189, 178, 0.16);
    box-sizing: border-box;
    padding: 4px 12px;
    display: flex;
    justify-content: space-between;
  }
}
.nameFlex {
  display: flex;
  justify-content: space-between;
  span {
    max-width: 50%;
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
  }
  > span:first-child {
    font-size: 14px;
  }
  > span:last-child {
    background: rgba(48, 189, 178, 0.16);
    box-sizing: border-box;
    padding: 4px 12px;
    font-size: 12px;
  }
}
.videoBox {
  display: flex;
}
.videoList {
  width: 160px;
  margin-right: 14px;
  background: rgba(38, 118, 113, 0.56);
  border: 1px solid #3ab378;
  border-radius: 0 0 4px 4px;
  box-sizing: border-box;
  padding: 12px;
}
.videoTable {
  height: 20rem;
  overflow-y: auto;
  color: #fff;
  // color: rgba(255,255,255,0.88);
}
.videoWindow {
  width: calc(100% - 174px);
}
.videoComponent {
  border-radius: 5px;
  height: 20rem;
}
#barMain,
#lineMain {
  height: 180px;
  width: 100%;
  color: #1c920c;
}
.videoFlex {
  display: flex;
  margin-bottom: 6px;
  cursor: pointer;
  padding: 4px 0;
  > div:first-child {
    width: 40%;
    text-align: center;
  }
  > div:last-child {
    width: 60%;
  }
}
.currentNode {
  background-color: rgba(48, 189, 178, 0.16);
}
.userPhoneMes {
  span {
    > img {
      width: 14px;
      height: 14px;
      vertical-align: bottom;
    }
  }
}
</style>
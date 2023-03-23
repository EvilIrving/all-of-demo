<!--
 * @Date: 2022-04-13 16:03:48
 * @Author: 
 * @Description: 
 * @LastEditors: dtb
 * @LastEditTime: 2022-04-27 11:00:49
 * @FilePath: \lucheng_project\src\views\propertyManage\threeChargePeople\ChargePeopleDetail.vue
-->
<template>
  <div>
    <div class="header">
      <div class="imgArea">
        <!-- <img style="width:100%;height: 100%;cursor:pointer" src="../../../assets/images/no_image.png" alt="" class="ispic"> -->
        <img style="width:100%;height: 100%;cursor:pointer" :src="src" alt="" class="ispic">
      </div>
      <div class="right">
        <h3 class="title">{{detail.projectName}}</h3>
        <section class="info" v-if="detail.projectScale === '1' ||detail.projectScale === '2' ||detail.projectScale === '3'  ">
          <span class="shortspan">所在地：<span style="color:#000;">{{detail.cityAdnm}}-{{detail.countryAdnm}}-{{detail.townAdnm}}</span> </span>
          <span class="longspan">有无管理单位：<span style="color:#000;">{{detail.ifDepartment}}</span> </span>
          <span class="shortspan">更新时间：<span style="color:#000;">{{detail.updateTime.substring(0,10)}}</span> </span>
          <span class="longspan">责任人公示文件：
            <i v-show="detail.personPublishFile" class="links"></i>
            <span class="link" @click="lookFile(detail.personPublishFile)"> {{detail.personPublishFilnm}}
            </span>
          </span>
        </section>
        <section class="info" v-else>
          <span class="shortspan">所在地：<span style="color:#000;">{{detail.cityAdnm}}-{{detail.countryAdnm}}-{{detail.townAdnm}}</span> </span>
          <span class="longspan">有无管理单位：<span style="color:#000;">{{detail.ifDepartment}}</span> </span>
          <span class="shortspan">更新时间：<span style="color:#000;">{{detail.updateTime.substring(0,10)}}</span> </span>
          <span class="longspan">主管部门：<span style="color:#000;">{{detail.mUninm}}</span></span>
          <span class="longspan">责任人公示文件：
            <i v-show="detail.personPublishFile" class="links"></i>
            <span class="link" @click="lookFile(detail.personPublishFile)"> {{detail.personPublishFilnm}}
            </span>

          </span>
        </section>
      </div>
    </div>
    <div class="margin_bottom3">
    </div>
    <ul class="main_box">
      <!-- <%--        行政负责人-政府责任人--%> -->
      <li>
        <div>
          <h3 class="little_title">行政负责人</h3>
          <h2 class="big_title" style="font-size: 18px;font-weight: 600">政府责任人</h2>
        </div>
        <section>
          <div><span class="name">{{detail.govermentChargeName }}</span> <span class="">{{detail.govermentChargeJob }}</span>
          </div>
          <div><span class="phone">{{detail.govermentChargePhone }}</span></div>
          <div><span class="addr">单位：{{detail.govermentChargeDept }}</span></div>
        </section>
      </li>
      <!-- <%--        行政负责人-水行政主管部门责任人--%> -->
      <li>
        <div>
          <h3 class="little_title">行政负责人</h3>
          <h3 class="big_title" style="font-size: 18px;font-weight: 600">水行政主管部门责任人</h3>
        </div>
        <section>
          <div>
            <span class="name">{{detail.waterDepartmentChargeName }}</span>
            <span class="">{{detail.waterDepartmentChargeJob }}</span>
          </div>
          <div><span class="phone">{{detail.waterDepartmentChargePhone }}</span></div>
          <div><span class="addr">单位：{{detail.waterDepartmentChargeDept }}</span></div>
        </section>
      </li>
      <!-- <%--        行政负责人-主管部门（产权人）责任人--%> -->
      <li>
        <div>
          <h3 class="little_title">行政负责人</h3>
          <h3 class="big_title" style="font-size: 18px;font-weight: 600">主管部门（产权人）责任人</h3>
        </div>
        <section>
          <div>
            <span class="name">{{detail.competentDepartmentChargeName }}</span>
            <span class="">{{detail.competentDepartmentChargeJob }}</span>
          </div>
          <div><span class="phone">{{detail.competentDepartmentChargePhone }}</span></div>
          <div><span class="addr">单位：{{detail.competentDepartmentChargeDept }}</span></div>
        </section>
      </li>
      <!-- <%--        行政负责人-管理单位责任人--%> -->
      <li v-if="detail.ifDepartment !='无' ">
        <div>
          <h3 class="little_title">行政负责人</h3>
          <h3 class="big_title" style="font-size: 18px;font-weight: 600">管理单位责任人</h3>
        </div>
        <section>
          <div>
            <span class="name">{{detail.manageDepartmentChargeName }}</span>
            <span class="">{{detail.manageDepartmentChargeJob }}</span>
          </div>
          <div><span class="phone">{{detail.manageDepartmentChargePhone }}</span></div>
          <div><span class="addr">单位：{{detail.manageDepartmentChargeDept }}</span></div>
        </section>
      </li>
      <!-- <%--        物业单位负责人--%> -->
      <li v-if="detail.ifDepartment =='无' ">
        <div>
          <h3 class="big_title" style="font-size: 18px;font-weight: 600">物业单位负责人</h3>
        </div>
        <section>
          <div>
            <span class="name">{{detail.propertyManageName }}</span>
            <span class="">{{detail.propertyManagePosition }}</span>
          </div>
          <div><span class="phone">{{detail.propertyManagePhone }}</span></div>
          <div><span class="addr">单位：{{detail.propertyManageUnit }}</span></div>
        </section>
      </li>
      <!-- <%--        技术负责人--%> -->
      <li>
        <div>
          <h3 class="big_title" style="font-size: 18px;font-weight: 600">技术责任人</h3>
        </div>
        <section>
          <div>
            <span class="name">{{detail.technologyChargeName }}</span>
            <span class="">{{detail.technologyChargeJob }}</span>
          </div>
          <div><span class="phone">{{detail.technologyChargePhone }}</span></div>
          <div><span class="addr">单位：{{detail.technologyChargeDept }}</span></div>
        </section>
      </li>
      <!-- <%--        巡查负责人--%> -->
      <li>
        <div>
          <h3 class="big_title" style="font-size: 18px;font-weight: 600">巡查责任人</h3>
        </div>
        <section>
          <div>
            <span class="name">{{detail.patrolChargeName }}</span>
            <span class="">{{detail.patrolChargeJob }}</span>
          </div>
          <div><span class="phone">{{detail.patrolChargePhone }}</span></div>
          <div><span class="addr">单位：{{detail.patrolChargeDept }}</span></div>
        </section>
      </li>
    </ul>
  </div>
</template>

<script>
import { chargePeopleApi } from '@/api/propertyManage'
export default {
  name: 'ChargePeopleDetail',
  props: {
    id: [String, Number],
  },
  components: {

  },
  data() {
    return {
      detail: {},
      src: '../../../assets/images/no_image.png',
    };
  },
  computed: {

  },
  watch: {

  },
  created() {

  },
  mounted() {
    this.getList()
  },
  methods: {
    async getList() {
      let res = await chargePeopleApi.query({
        id: this.id,
      })
      if (res.code == 0) {
        this.detail = res.data
        if (res.data.reservoirPhoto) {
          let photo = res.data.reservoirPhoto || ''
          if (photo.indexOf("group1") === -1) {
            this.src = "/fileUpds/" + photo;
          } else {
            this.src = photo;
          }
        } else {
          this.src = "https://file.ygwjg.com/2754c2dd556ea6b7c9dccb6249ab142480f4dbae.png";
        }
      }
    },
    lookFile(url){
      window.open(url)
    }

    // $(".phone").each(function () {
    //     var _txt = $(this).text();
    //     if (_txt.length == 11) {
    //         _txt = _txt.substr(0, 4) + "****" + _txt.substr(7, 10);
    //         $(this).html(_txt);
    //     }
    // });
  },
};
</script>

<style scoped lang="scss">
div.margin_bottom3 {
  border-bottom: 1px solid rgba(0, 0, 0, 0.09);
  padding: 10px 20px 10px;
}

.header {
  width: 100%;
  height: 150px;
  display: flex;
}

.imgArea {
  width: 260px;
  height: 100%;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}
img {
  cursor: pointer;
  background-size: cover;
  /*max-width: 100%;*/
}

.right {
  margin: 2% 0 0 20px;
}
.title {
  font-weight: 600;
  color: #000;
}
.right .info {
  margin: 15px 0;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-wrap: wrap;
  width: 100%;
}
.info span {
  padding: 3px 0;
}
.info span.shortspan {
  width: 40%;
}
.info span.longspan {
  width: 60%;
  display: flex;
  align-items: center;
}
.info i.links {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin: 0 5px;
  background: url("../../../assets/images/c_path.png") no-repeat center/100%;
}
.info span.link {
  color: #193cc5;
  cursor: pointer;
}
.main_box {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
}
ul:after,
li:after {
  content: "";
  display: none;
}
ul:before,
li:before {
  content: "";
  display: none;
}
li {
  color: #fff;
  width: 25%;
  height: 150px;
  padding: 15px 2%;
  border-radius: 16px;
  background: url("../../../assets/images/bg-respon.png") no-repeat center/100%;
  margin: 1% 2% 0 3%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  flex-wrap: wrap;
  h3 {
    margin: 3px 0;
  }
  .name {
    margin-right: 5px;
  }
}
li:nth-child(n + 2),
li:nth-child(1) {
  margin-left: 0;
}
</style>

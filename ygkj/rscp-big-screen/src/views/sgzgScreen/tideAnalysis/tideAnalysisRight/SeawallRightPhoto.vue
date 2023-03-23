<!--
 * @Author: hanyu
 * @LastEditTime: 2021-07-23 15:21:56
 * @Description: 地图右侧-工程照片
 * @FilePath: /rscp-big-screen/src/views/bigScreen/seawall/seawallRight/SeawallRightPhoto.vue
-->
<template>
  <div class='wrap'>
    <progress-list v-loading="progressLoading" title="工程照片分布情况" class="progress" :progressData="progressData"></progress-list>
    <section class="photo_list">
      <div class="chart_title">
        <img src="../../../../assets/images/chart_item_icon.png" alt="">
        工程列表
      </div>
      <div class="table_list" ref="tableContainer">
        <el-table :height="tableHeight" v-loading="tableLoading" :data="tableData" stripe style="width: 100%" @row-click="clickPhoto">
          <template slot="empty">
            <div class="table_nodata">
              <img src="../../../../assets/images/noData.png" alt="" />
            </div>
          </template>
          <el-table-column prop="NAME" label="海塘名称"></el-table-column>
          <el-table-column prop="SEAWALL_LENGTH" width="120" label="海塘长度（m）" align="center"></el-table-column>
          <el-table-column label="海塘等级" align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.project_scale == 1">一级</div>
              <div v-else-if="scope.row.project_scale == 2">二级</div>
              <div v-else-if="scope.row.project_scale == 3">三级</div>
              <div v-else-if="scope.row.project_scale == 4">四级</div>
              <div v-else-if="scope.row.project_scale == 5">五级</div>
              <div v-else-if="scope.row.project_scale == 6">六级</div>
              <div v-else>-</div>
            </template>
          </el-table-column>
          <el-table-column label="照片数量" align="center">
            <template slot-scope="scope">
              <div style="color: #003ec7; cursor: pointer;" @click.stop="lookPhotos(scope.row)">{{scope.row.photoCnt}}</div>
            </template>
          </el-table-column>
        </el-table> 
      </div>
    </section>
  </div>
</template>

<script>
import { seawallLeftStatisticApi, seawallPhotoListApi, getFile } from '@/api/api_seawall'
import ProgressList from '@/components/ProgressList'
export default {
  name: '',
  props: {
    
  },
  components: {
    ProgressList
  },
  data () {
    return {
      progressLoading: true,
      progressData: [],
      tableData: [],
      tableLoading: true,
      tableHeight: 0,
      opt: {
        statistic: 'mapList',
        itemCategory: 'photo',
        itemValue: 1,
        adcd: this.leftSelectAdcd ? this.leftSelectAdcd : this.$localData('get', 'userInfo').adcd
      }
    };
  },
  computed: {
    leftSelectAdcd(){
      return this.$store.state.leftSelectAdcd
    },
    leftSelectPreventLevel(){
      return this.$store.state.leftSelectPreventLevel
    },
    leftSelectProjectScale(){
      return this.$store.state.leftSelectProjectScale
    }
  },
  watch: {
    leftSelectAdcd(val){
      this.opt.adcd = val ? val : this.$localData('get', 'userInfo').adcd
      if(val == '33'){
        this.opt.superviseLevel = 1
      }
      this.getPhotoListData()
    },
    leftSelectPreventLevel(val){
      this.setOptions('preventLevel', val)
      this.getPhotoListData()
    },
    leftSelectProjectScale(val){
      this.setOptions('scale', val)
      this.getPhotoListData()
    }
  },
  mounted() {
    this.$nextTick(function () {
      const height = this.$refs.tableContainer.clientHeight;
      this.tableHeight = height - 20
      let _this = this;
      window.onresize = function() {
        const height = this.$refs.tableContainer.clientHeight;
        this.tableHeight = height - 20
      }
    })
    if(this.leftSelectAdcd == '33'){
      this.opt.superviseLevel = 1
    }
    if(this.leftSelectProjectScale){
      this.setOptions('scale', this.leftSelectProjectScale)
    }
    this.getPhotoStatisticData()
    this.getPhotoListData()
  },
  methods: {
    setOptions(type, val){
      if(val){ // val不为空
        if(this.opt.itemCategory){
          if(this.opt.itemCategory.indexOf(type) == -1){ // 不存在该参数
            this.opt.itemCategory = this.opt.itemCategory + ',' + type
            this.opt.itemValue = this.opt.itemValue + ',' + val
          }else{ // 已存在该参数 更新参数
            let itemArr = this.opt.itemCategory.split(',')
            let index = itemArr.indexValue(type)
            let valueArr = this.opt.itemValue.toString().split(',')
            valueArr[index] = val
            this.opt.itemValue = valueArr.join(',')
          }
        }else{ // 没有参数
          this.opt.itemCategory = type
          this.opt.itemValue = val
        }
      }else{ // 为空
        if(this.opt.itemCategory){
          if(this.opt.itemCategory.indexOf(type) != -1){ // 存在该参数 清空参数
            let itemArr = this.opt.itemCategory.split(',')
            let valueArr = this.opt.itemValue.toString().split(',')
            let index = itemArr.indexValue(type)
            itemArr.splice(index, 1)
            valueArr.splice(index, 1)
            this.opt.itemCategory = itemArr.join(',')
            this.opt.itemValue = valueArr.join(',')
          }
        }
      }
    },
    async getPhotoStatisticData(){
      this.progressLoading = true
      this.progressData = []
      let res = await seawallLeftStatisticApi({
        statistic: 'adcd',
        itemCategory: 'photo',
        itemValue: 1,
        adcd: this.$localData('get', 'userInfo').adcd
      })
      if(res.success){
        let maxarr = []
        for (let i = 0; i < res.data.length; i++) {
          maxarr.push(res.data[i].photoCnt)
        }
        let max = Math.max(...maxarr)
        for (let i = 0; i < res.data.length; i++) {
          let percentage = ( res.data[i].photoCnt / max * 100 ).toFixed(0)
          this.progressData.push({
            title: res.data[i].adnm,
            allNum: max,
            nowNum: res.data[i].photoCnt,
            percentage: Number(percentage)
          })
        }
      }
      this.progressLoading = false
    },
    async getPhotoListData(){
      this.$parent.$refs.seawallMap.removeAllLayer();
      this.$parent.$refs.seawallMap.closePopup();
      this.$parent.$refs.seawallMap.initView();
      this.tableLoading = true
      this.tableData = []
      this.$store.commit('SET_SEAWALL_RIGTH_SELECT_FLAG', false)
      let res = await seawallLeftStatisticApi(this.opt)
      if(res.success){
        if(res.data.length){
          this.tableData = res.data
          let pointArr = [];
          let lineArr = [];
          res.data.map(v => {
            v.lineArr = []
            pointArr.push({
              lat: v.LTTD,
              lng: v.LGTD,
              NAME: v.NAME,
              rightType: '4',
              PRCD: v.PRCD,
              tm: v.tm,
              tide: v.tide,
              DESIGN_TIDE_LEVEL: v.DESIGN_TIDE_LEVEL,
              DESIGN_TIDE_STANDARD: v.DESIGN_TIDE_STANDARD,
              photoCnt: v.photoCnt,
              src: require("@/assets/images/map/seawall.png")
            })
            lineArr.push({
              line: v.COORPOT ? JSON.parse(v.COORPOT.replace(new RegExp('&quot;',"gm"),'"'))[0] : '',
              level: v.project_scale
            })
            v.lineArr.push({
              line: v.COORPOT ? JSON.parse(v.COORPOT.replace(new RegExp('&quot;',"gm"),'"'))[0] : '',
              level: v.project_scale
            })
          });
          
          this.$parent.showMapPointJh(pointArr, 'photo');
          this.$parent.showMapLines(lineArr);

        }
      }
      this.tableLoading = false
      this.$store.commit('SET_SEAWALL_RIGTH_SELECT_FLAG', true)
    },
    async getPhotoDetailList(prcd, seawallName){
      let res = await seawallPhotoListApi({
        prcd: prcd,
        fetchAll: true
      })
      if(res.success){
        let arr = []
        if(res.rows.length){
          res.rows.map(v => {
            arr.push(v.extphoto6)
          })
          this.getFileList(arr.join(','), seawallName)
        }else{
          this.$message.error('该工程暂无照片！')
        }
      }else{
        this.$message.error('获取照片失败！')
      }
    },
    async getFileList(ids, seawallName){
      let res = await getFile({
        dataId: ids,
      });
      let photoList = [];
      if (res.list.length) {
        res.list.map((v) => {
          let url = v.filurl
          if(v.filurl && v.filurl.indexOf('https://file.ygwjg.com/') == -1 && v.filurl.indexOf('/fileUpds/') != -1){
            url = 'https://sk.zjwater.com' + v.filurl
          }
          photoList.push({
            name: v.filnm,
            url: url
          });
        });
        this.$parent.showPhotosList(photoList, seawallName)
      }
    },
    clickPhoto(row){
      this.$parent.setView(row.LGTD, row.LTTD);
      row.lat = row.LTTD
      row.lng = row.LGTD
      row.rightType = '4'
      this.$parent.showOneSelectPoint([row]);
      setTimeout(()=>{
        this.$parent.setPopup(row)
      },500)
    },
    lookPhotos(row){
      // console.log(row,222);
      this.$parent.openLookPhotosPopup()
      this.getPhotoDetailList(row.PRCD, row.NAME)
    }
  }
}
</script>

<style lang='scss' scoped>
  .wrap{
    height: 100%;
  }
  .progress{
    height: 260px;
  }
  .photo_list{
    height: calc(100% - 260px);
    .table_list{
      height: calc(100% - 42px);
      box-sizing: border-box;
      padding-bottom: 16px;
      // height: 500px;
      overflow: auto;
      
    }
  }
</style>
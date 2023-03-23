<!--
 * @Author: dengzhao
 * @LastEditTime: 2021-11-13 16:52:12
 * @Description: 
 * @FilePath: \lucheng\src\views\water_disaster_custom\components\simple\components\TyphoonList.vue
-->
<template>
  <div class="warp-box-table">
    <div class="filtrate-box">
      <div>台风列表</div>
    </div>
    <div class="map-table-box" v-if="!checkTable">
      <primary-map-table :columnList="columnList" :dataList="dataList" @rowClick="rowClick"></primary-map-table>
    </div>
    <div class="map-table-box" v-else>
      <CapsuleTabs :tabs="['返回']" @change="handleTabChange" />
      <div class="primary-map-table">
        <primary-map-table :columnList="columnList_a" :dataList="seawallDataList"></primary-map-table>
      </div>
    </div>
  </div>
</template>

<script>
import { PrimaryMapTable } from '@/components';
import CapsuleTabs from '@/views/WaterDisasterPreventionScreen/components/CapsuleTabs.vue';
import { mapGetters } from 'vuex';
import { alarmPeople } from '@/api/bigScreenPart';
export default {
  data() {
    return {
      checkTable: false,
      columnList: [
        {
          prop: 'name',
          label: '台风名称'
        },
        {
          prop: 'power',
          label: '风力'
        },
        {
          prop: 'distance',
          label: '距离鹿城(km)'
        }
      ],
      dataList: [],
      columnList_a: [
        {
          prop: 'seawallName',
          label: '海塘名称'
        },
        {
          prop: 'distance',
          label: '台风距离(km)'
        },
        {
          prop: 'isInfluence',
          label: '台风影响'
        }
      ],
      seawallDataList: [],
      id: ''
    };
  },
  components: {
    PrimaryMapTable,
    CapsuleTabs
  },
  computed: {
    ...mapGetters(['simplePotenData', 'simpleRangTime']),
    map() {
      return this.$store.state.screen.mapComponent;
    }
  },
  watch: {
    simplePotenData: {
      handler: function (val) {
        this.checkTable = false;
        this.map.clearAllTyphoon();
        if (val) {
          this.typhoonList(val);
        }
      },
      deep: true
    }
  },
  mounted() {
    this.typhoonList(this.simplePotenData);
  },
  beforeDestroy() {
    this.map.clearAllTyphoon();
  },
  methods: {
    handleTabChange() {
      this.checkTable = !this.checkTable;
    },
    typhoonList(arr) {
      this.dataList = arr.map((item) => {
        let points = JSON.parse(item.points);
        item['distance'] = points[points.length - 1].distance;
        item['power'] = points[points.length - 1].power;
        return item;
      });
      this.dataList.forEach((element) => {
        let points = eval(element.points);
        const data = {
          ...element,
          points
        };
        console.log(element, '11111111111111111')
        this.map.showTyphoon(data);
      });
    },
    async getSeawallList() {
      let res = await alarmPeople.seawallTyphoon({ id: this.id, date: this.simpleRangTime });
      console.log(res, 'asdasdsadsadsad');
      if (res.code == 0) {
        this.seawallDataList = res.data;
      }
    },
    rowClick(row) {
      this.map.clearAllTyphoon();
      let points = eval(row.points);
      const data = {
        ...row,
        points
      };
      this.map.showTyphoon(data);
      this.checkTable = true;
      if (this.id != row.id) {
        this.id = row.id;
        this.getSeawallList();
      }
    },
  }
};
</script>

<style lang="scss" scoped>
.warp-box-table {
  height: 100%;
}
.filtrate-box {
  padding-bottom: 10px;
  position: relative;
  margin-bottom: 20px;
  &::after {
    content: '';
    width: 100%;
    height: 4px;
    position: absolute;
    left: 0;
    bottom: 0;
    background: url(~assets/images/WaterDisastersImage/seawall-title-icon-overview.png) no-repeat;
    background-size: 100% 100%;
  }
  > div {
    padding-left: 26px;
    position: relative;
    &::after {
      content: '';
      width: 20px;
      height: 20px;
      position: absolute;
      left: 0;
      top: -3px;
      background: url(~assets/images/WaterDisastersImage/seawall-title-icon.png) no-repeat;
      background-size: 100% 100%;
    }
  }
}
.map-table-box {
  height: calc(100% - 58px);
  width: 100%;
  .primary-map-table {
    margin-top: 16px;
    height: calc(100% - 30px);
  }
}
</style>

<style>
.map-table-box .capsule-tabs,
.map-table-box .capsule-tabs .capsule-tabs__item {
  margin: 0;
}
</style>
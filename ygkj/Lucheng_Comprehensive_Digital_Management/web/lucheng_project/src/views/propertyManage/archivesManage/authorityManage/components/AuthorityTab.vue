<!--
 * @Author: zhongg
 * @LastEditTime: 2020-12-02 11:11:16
 * @LastEditors: Please set LastEditors
 * @Descripttion: 
 * @FilePath: /ruiAnWaterPlatform/src/views/perceptual_converge/equipment_manage/EquipmentManageForm.vue
-->
<template>
  <div class="user-form">
    <div class="el-form"
      ref="authorityTab"
    >
      <div class="type_box">
        <div class="type_item" v-for="(item, index) in typeList" :key="index" :class="typeIndex == index ? 'type_active' : ''" @click="typeClick(index)">
          <span>{{item.title}}</span>
        </div>
      </div>

      <div class="warnConfigBox">
         <!-- 人员选择 -->
        <template v-if="type == 'people'">
          <people-form
            ref="peopleForm"
            @cancelDialog="$emit('cancelDialog')"
          ></people-form>
        </template>

        <!-- 权限配置 -->
        <template v-if="type == 'config'">
          <config-form
            ref="configForm"
            @cancelDialog="handleClose"
          ></config-form>
        </template>
      </div>
    
      
    </div>
  </div>
</template>

<script>
import PeopleForm from "./PeopleForm";
import ConfigForm from "./ConfigForm";
export default {
  name: "AuthorityTab",
  props: {

  },
  components: {
    PeopleForm,
    ConfigForm
  },
  data() {
    return {
      directoryId: "",
      typeIndex: 0,
      type: 'people',
      typeList: [
        { 
          title: '人员选择', 
          value: 'people'
        },
        { 
          title: '权限配置', 
          value: 'config'
        }
      ]
    };
  },
  mounted() {
 
  },
  computed: {
    headers: function() {
      const userInfo = this.$sessionData("get", "userInfo");
      const opt = {
        authorization: userInfo.accessToken,
      };
      return opt;
    },
  },
  methods: {
    typeClick(index){
      this.typeIndex = index;
      this.type = this.typeList[index].value;
      this.$nextTick().then(() => {
        if(this.type == "people"){
          this.$refs.peopleForm.getFormData(this.directoryId);
        }else if(this.type == "config"){
          this.$refs.configForm.getFormData(this.directoryId);
        }
      })
    },
    resetForm() {
      if(this.type == "people"){
        this.$refs.peopleForm.resetForm();
      }else if(this.type == "config"){
        this.$refs.configForm.resetForm();
      }
      this.typeIndex = 0;
      this.type = 'people';
    },
    handleClose() {  
      this.typeIndex = 0;
      this.type = 'people';
      this.$emit('cancelDialog');
    },
    getFormId(id) {
      this.directoryId = id;
      this.$nextTick().then(() => {
        if(this.type == "people"){
          this.$refs.peopleForm.getFormData(this.directoryId);
        }else if(this.type == "config"){
          this.$refs.configForm.getFormData(this.directoryId);
        }
      })
    }
  },
};
</script>

<style lang="scss" scoped>
.user-form {
  width: 100%;
  padding-top: 10px;
  .el-form{
    width: 100%;
    background: #fff;
    .type_box{
      width: 100%;
      height: 40px;
      line-height: 37px;
      margin-bottom: 15px;
      border-bottom: 1px solid #DCDFE6;
      .type_item{
        color: #000;
        padding: 0 8px;
        margin: 0 8px;
        float: left;
        cursor: pointer;
      }
      .type_active{
        color: #01A59C;
        border-bottom: 3px solid #01A59C;
      }
    }
    .warnConfigBox{
      width: 100%;
    }
    .basicInfo{
      padding: 0 10px;
    }
  }
  
}
</style>

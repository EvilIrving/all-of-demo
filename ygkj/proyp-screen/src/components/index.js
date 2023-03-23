/*
 * @Author: wqn
 * @Date: 2022-11-14 10:12:44
 * @LastEditors: wqn
 * @LastEditTime: 2023-02-15 11:09:45
 */
import Vue from 'vue'

import ListDialogExportButton from "common/button/ListDialogExportButton.vue"
import ListDetailsDialog from "common/dialog/ListDetailsDialog.vue"
import ListDialogRiskTypeRadio from "common/radio/ListDialogRiskTypeRadio.vue"
import AdmnBasinSelect from "common/select/AdmnBasinSelect.vue"
import ListDialogAdmnSelect from "common/select/ListDialogAdmnSelect.vue"
import MainHeader from "page/mainHeader/Index.vue"
import MapLegend from "page/mapLegend/Index.vue"
import ModuleLayout from "page/moduleLayout/Index.vue"
import OlMap from "page/olMap/Index.vue"
import ProjectDetail from "page/projectDetail/ProjectDetail.vue"

Vue.component('list-details-export-button', ListDialogExportButton)
Vue.component('list-details-dialog', ListDetailsDialog)
Vue.component('list-dialog-risk-type-radio', ListDialogRiskTypeRadio)
Vue.component('admn-basin-select', AdmnBasinSelect)
Vue.component('list-dialog-admn-select', ListDialogAdmnSelect)
Vue.component('MainHeader', MainHeader)
Vue.component('MapLegend', MapLegend)
Vue.component('ModuleLayout', ModuleLayout)
Vue.component('OlMap', OlMap)
Vue.component('ProjectDetail', ProjectDetail)
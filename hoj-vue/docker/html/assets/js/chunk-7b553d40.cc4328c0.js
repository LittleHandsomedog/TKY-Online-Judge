(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-7b553d40"],{"4b73":function(t,i,s){"use strict";s.r(i),s("b0c0");var e=s("5530"),a=s("3228"),n=s("5880");n={name:"SubmissionStatistics",props:{title:{type:String,required:!0}},data:function(){return{loading:!1,options:{tooltip:{trigger:"axis",axisPointer:{type:"cross",label:{backgroundColor:"#6a7985"}}},legend:{data:[this.$i18n.t("m.AC"),this.$i18n.t("m.Total")]},toolbox:{feature:{saveAsImage:{show:!0,title:this.$i18n.t("m.save_as_image")}}},grid:{left:"3%",right:"4%",bottom:"3%",containLabel:!0},xAxis:[{type:"category",boundaryGap:!1,data:[]}],yAxis:[{type:"value"}],series:[{name:this.$i18n.t("m.AC"),type:"line",stack:"Total",areaStyle:{},emphasis:{focus:"series"},color:"#91cc75",data:[0,0,0,0,0,0,0]},{name:this.$i18n.t("m.Total"),type:"line",stack:"Total",label:{show:!0,position:"top"},areaStyle:{},emphasis:{focus:"series"},color:"#73c0de",data:[0,0,0,0,0,0,0]}]}}},mounted:function(){this.getLastWeekSubmissionStatistics(!1)},methods:{getLastWeekSubmissionStatistics:function(t){var i=this;this.loading=!0,a.a.getLastWeekSubmissionStatistics(t).then((function(t){i.options.xAxis[0].data=t.data.data.dateStrList,i.options.series[0].data=t.data.data.acCountList,i.options.series[1].data=t.data.data.totalCountList,i.loading=!1}),(function(t){i.loading=!1}))}},computed:Object(e.a)({},Object(n.mapGetters)(["isSuperAdmin","webLanguage"])),watch:{webLanguage:function(t,i){this.options.legend.data=[this.$i18n.t("m.AC"),this.$i18n.t("m.Total")],null!=this.options.series&&2==this.options.series.length&&(this.options.series[0].name=this.$i18n.t("m.AC"),this.options.series[1].name=this.$i18n.t("m.Total"))}}},s("c097"),s=s("2877"),n=Object(s.a)(n,(function(){var t=this,i=t.$createElement;i=t._self._c||i;return i("el-card",[i("div",{attrs:{slot:"header",shadow:"",padding:10},slot:"header"},[i("span",{staticClass:"home-title panel-title"},[i("i",{staticClass:"el-icon-data-line"}),t._v(" "+t._s(t.$t("m.Statistics_Submissions_In_The_Last_Week")))]),t.isSuperAdmin?i("span",[i("el-button",{staticStyle:{float:"right"},attrs:{type:"primary",icon:"el-icon-refresh",size:"small",loading:t.loading},on:{click:function(i){return t.getLastWeekSubmissionStatistics(!0)}}},[t._v(t._s(t.$t("m.Refresh")))])],1):t._e()]),i("div",{directives:[{name:"loading",rawName:"v-loading",value:t.loading,expression:"loading"}],staticClass:"echarts"},[i("ECharts",{ref:"chart",attrs:{options:t.options,autoresize:!0}})],1)])}),[],!1,null,"44d08df3",null);i.default=n.exports},"8e98":function(t,i,s){},c097:function(t,i,s){"use strict";s("8e98")}}]);
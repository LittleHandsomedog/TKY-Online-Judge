(window.webpackJsonp=window.webpackJsonp||[]).push([["chunk-ddd7c1e0"],{"9a3b":function(t,e,s){"use strict";var a=s("c975"),n=s("a434"),o=(a=s("498a"),n=s("5530"),s("3228")),r=(a=s("5880"),s("9a36")),i=s("8935");e.a={methods:{initConcernedList:function(){var t=Object(r.r)(this.$route.params.contestID);this.concernedList=i.a.get(t)||[]},getContestRankData:function(){var t=this,e=0<arguments.length&&void 0!==arguments[0]?arguments[0]:1,s=1<arguments.length&&void 0!==arguments[1]&&arguments[1];this.showChart&&!s&&this.$refs.chart.showLoading({maskColor:"rgba(250, 250, 250, 0.8)"});var a={currentPage:e,limit:this.limit,cid:this.$route.params.contestID,forceRefresh:!!this.forceUpdate,removeStar:!this.showStarUser,concernedList:this.concernedList,keyword:null==this.keyword?null:this.keyword.trim(),containsEnd:this.isContainsAfterContestJudge};o.a.getContestRank(a).then((function(a){t.showChart&&!s&&t.$refs.chart.hideLoading(),t.total=a.data.data.total,1===e&&t.applyToChart(a.data.data.records),t.applyToTable(a.data.data.records)}))},handleAutoRefresh:function(t){var e=this;1==t?this.refreshFunc=setInterval((function(){e.$store.dispatch("getContestProblems"),e.getContestRankData(e.page,!0)}),1e4):clearInterval(this.refreshFunc)},updateConcernedList:function(t,e){e?this.concernedList.push(t):-1<(s=this.concernedList.indexOf(t))&&this.concernedList.splice(s,1);var s=Object(r.r)(this.contestID);i.a.set(s,this.concernedList),this.getContestRankData(this.page,!0)},getRankShowName:function(t,e){var s=t;return null==t||""==t||0==t.trim().length?e:s}},computed:Object(n.a)(Object(n.a)(Object(n.a)({},Object(a.mapGetters)(["isContestAdmin","userInfo","isContainsAfterContestJudge"])),Object(a.mapState)({contest:function(t){return t.contest.contest},contestProblems:function(t){return t.contest.contestProblems}})),{},{showChart:{get:function(){return this.$store.state.contest.itemVisible.chart},set:function(t){this.$store.commit("changeContestItemVisible",{chart:t})}},showStarUser:{get:function(){return!this.$store.state.contest.removeStar},set:function(t){this.$store.commit("changeRankRemoveStar",{value:!t})}},showTable:{get:function(){return this.$store.state.contest.itemVisible.table},set:function(t){this.$store.commit("changeContestItemVisible",{table:t})}},forceUpdate:{get:function(){return this.$store.state.contest.forceUpdate},set:function(t){this.$store.commit("changeRankForceUpdate",{value:t})}},concernedList:{get:function(){return this.$store.state.contest.concernedList},set:function(t){this.$store.commit("changeConcernedList",{value:t})}},refreshDisabled:function(){return this.contest.status==r.a.ENDED}}),beforeDestroy:function(){clearInterval(this.refreshFunc)}}},"9d33":function(t,e,s){"use strict";s.r(e),s("99af"),s("4160"),s("c975"),s("b64b"),s("d3b7"),s("159b");var a=s("5530"),n=s("4a89"),o=s.n(n),r=s("5880"),i=(n=s("9a3b"),s("90b9"));a={name:"OIContestRank",components:{Pagination:function(){return Promise.resolve().then(s.bind(null,"5072"))},RankBox:function(){return s.e("chunk-3ef84ff5").then(s.bind(null,"f5bc"))},Avatar:o.a},mixins:[n.a],data:function(){return{total:0,page:1,limit:50,contestID:"",dataRank:[],keyword:null,autoRefresh:!1,options:{title:{text:this.$i18n.t("m.Top_10_Teams"),left:"center"},tooltip:{trigger:"axis"},toolbox:{show:!0,feature:{dataView:{show:!0,readOnly:!0},magicType:{show:!0,type:["line","bar"]},saveAsImage:{show:!0,title:this.$i18n.t("m.save_as_image")}},right:"10%",top:"5%"},calculable:!0,xAxis:[{type:"category",data:["root"],boundaryGap:!0,axisLabel:{interval:0,showMinLabel:!0,showMaxLabel:!0,align:"center",formatter:function(t,e){return i.a.breakLongWords(t,14)}},axisTick:{alignWithLabel:!0}}],yAxis:[{type:"value"}],grid:{left:"11%"},series:[{name:this.$i18n.t("m.Score"),type:"bar",barMaxWidth:"80",data:[0],markPoint:{data:[{type:"max",name:"max"}]}}]}}},created:function(){this.initConcernedList()},mounted:function(){this.contestID=this.$route.params.contestID,this.getContestRankData(1),this.refreshDisabled||(this.autoRefresh=!0,this.handleAutoRefresh(!0))},computed:{contest:function(){return this.$store.state.contest.contest},isMobileView:function(){return window.screen.width<768}},watch:{isContainsAfterContestJudge:function(t,e){this.getContestRankData(this.page)}},methods:Object(a.a)(Object(a.a)({},Object(r.mapActions)(["getContestProblems"])),{},{cellClassName:function(t){var e=t.row,s=(t.rowIndex,t.column);t=t.columnIndex;return e.username!=this.userInfo.username||"rank"!=s.property&&"totalScore"!=s.property&&"username"!=s.property&&"realname"!=s.property?"username"===s.property&&e.userCellClassName?e.userCellClassName:"rank"!==s.property&&"totalScore"!==s.property&&"username"!==s.property&&"realname"!==s.property?this.isContestAdmin?e.cellClassName[[this.contestProblems[t-4].displayId]]:e.cellClassName[[this.contestProblems[t-3].displayId]]:e.isConcerned&&"username"!==s.property?"bg-concerned":void 0:"own-submit-row"},getUserTotalSubmit:function(t){this.$router.push({name:"ContestSubmissionList",query:{username:t}})},getUserHomeByUsername:function(t,e){this.$router.push({name:"UserHome",query:{username:e,uid:t}})},getContestProblemById:function(t){this.$router.push({name:"ContestProblemDetails",params:{contestID:this.contestID,problemID:t}})},getUserProblemSubmission:function(t){var e=t.row;t=t.column;"rank"!==t.property&&"totalScore"!==t.property&&"username"!==t.property&&"realname"!==t.property&&this.$router.push({name:"ContestSubmissionList",query:{username:e.username,problemID:t.property,completeProblemID:!0}})},applyToChart:function(t){var e=[],s=[],a=t.length,n=this.concernedList.length||0;0<t.length&&t[0].uid==this.userInfo.uid&&n++;for(var o=n;o<a&&o<n+10;o++){var r=t[o];e.push(this.getRankShowName(r[this.contest.rankShowName],r.username)),s.push(r.totalScore)}this.options.xAxis[0].data=e,this.options.series[0].data=s},applyToTable:function(t){var e=this;t.forEach((function(t,s){var a=t.submissionInfo,n=t.timeInfo,o={};-1!=e.concernedList.indexOf(t.uid)&&(t.isConcerned=!0),Object.keys(a).forEach((function(e){t[e]=a[e];var s=a[e];null!=n&&null!=n[e]?o[e]="oi-100":0==s?o[e]="oi-0":null!=s&&(o[e]="oi-between")})),t.cellClassName=o,-1==t.rank&&(t.userCellClassName="bg-star"),"female"==t.gender&&(t.userCellClassName="bg-female"),t.rankShowName=e.getRankShowName(t[e.contest.rankShowName],t.username)})),this.dataRank=t},downloadRankCSV:function(){i.a.downloadFile("/api/file/download-contest-rank?cid=".concat(this.$route.params.contestID,"&forceRefresh=").concat(!!this.forceUpdate,"&containEnd=").concat(this.isContainsAfterContestJudge))}})},s("f7c7"),r=s("2877"),a=Object(r.a)(a,(function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("el-card",{attrs:{shadow:""}},[s("div",{attrs:{slot:"header"},slot:"header"},[s("span",{staticClass:"panel-title"},[t._v(t._s(t.$t("m.Contest_Rank"))+"（"+t._s("Recent"==t.contest.oiRankScoreType?t.$t("m.Based_on_The_Recent_Score_Submitted_Of_Each_Problem"):t.$t("m.Based_on_The_Highest_Score_Submitted_For_Each_Problem"))+"）")])]),s("div",{directives:[{name:"show",rawName:"v-show",value:t.showChart,expression:"showChart"}],staticClass:"echarts"},[s("ECharts",{ref:"chart",attrs:{options:t.options,autoresize:!0}})],1),s("el-row",[s("el-col",{attrs:{xs:24,md:8}},[s("div",{staticClass:"contest-rank-search contest-rank-filter"},[s("el-input",{attrs:{placeholder:t.$t("m.Contest_Rank_Search_Placeholder")},nativeOn:{keyup:function(e){return!e.type.indexOf("key")&&t._k(e.keyCode,"enter",13,e.key,"Enter")?null:t.getContestRankData(t.page)}},model:{value:t.keyword,callback:function(e){t.keyword=e},expression:"keyword"}},[s("el-button",{staticClass:"search-btn",attrs:{slot:"append",icon:"el-icon-search"},on:{click:function(e){return t.getContestRankData(t.page)}},slot:"append"})],1)],1)]),s("el-col",{attrs:{xs:24,md:16}},[s("div",{staticClass:"contest-rank-config"},[s("el-popover",{attrs:{trigger:"hover",placement:"left-start"}},[s("el-button",{attrs:{slot:"reference",round:"",size:"small"},slot:"reference"},[t._v(" "+t._s(t.$t("m.Contest_Rank_Setting"))+" ")]),s("div",{attrs:{id:"switches"}},[s("p",[s("span",[t._v(t._s(t.$t("m.Chart")))]),s("el-switch",{model:{value:t.showChart,callback:function(e){t.showChart=e},expression:"showChart"}})],1),s("p",[s("span",[t._v(t._s(t.$t("m.Table")))]),s("el-switch",{model:{value:t.showTable,callback:function(e){t.showTable=e},expression:"showTable"}})],1),s("p",[s("span",[t._v(t._s(t.$t("m.Star_User")))]),s("el-switch",{on:{change:function(e){return t.getContestRankData(t.page)}},model:{value:t.showStarUser,callback:function(e){t.showStarUser=e},expression:"showStarUser"}})],1),s("p",[s("span",[t._v(t._s(t.$t("m.Auto_Refresh"))+"(10s)")]),s("el-switch",{attrs:{disabled:t.refreshDisabled},on:{change:t.handleAutoRefresh},model:{value:t.autoRefresh,callback:function(e){t.autoRefresh=e},expression:"autoRefresh"}})],1),t.isContestAdmin?[s("p",[s("span",[t._v(t._s(t.$t("m.Force_Update")))]),s("el-switch",{on:{change:function(e){return t.getContestRankData(t.page)}},model:{value:t.forceUpdate,callback:function(e){t.forceUpdate=e},expression:"forceUpdate"}})],1)]:t._e(),t.isContestAdmin?[s("el-button",{attrs:{type:"primary",size:"small"},on:{click:t.downloadRankCSV}},[t._v(t._s(t.$t("m.Download_as_CSV")))])]:t._e()],2)],1)],1)])],1),s("div",{directives:[{name:"show",rawName:"v-show",value:t.showTable,expression:"showTable"}]},[s("vxe-table",{ref:"OIContestRank",attrs:{round:"",border:"","auto-resize":"",size:"medium",align:"center",data:t.dataRank,"cell-class-name":t.cellClassName},on:{"cell-click":t.getUserProblemSubmission}},[s("vxe-table-column",{attrs:{field:"rank",width:"50",fixed:"left",title:t.$t("m.Contest_Rank_Seq")},scopedSlots:t._u([{key:"default",fn:function(e){return e=e.row,[-1==e.rank?[s("span",[t._v("*")])]:[e.isWinAward?[s("RankBox",{attrs:{num:e.rank,background:e.awardBackground,color:e.awardColor,name:e.awardName}})]:[s("RankBox",{attrs:{num:e.rank}})]]]}}])}),t.isMobileView?s("vxe-table-column",{attrs:{field:"username","min-width":"300",title:t.$t("m.User"),"header-align":"center",align:"left"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[s("div",{staticClass:"contest-rank-user-box"},[s("span",[s("avatar",{attrs:{username:a.rankShowName,inline:!0,size:37,color:"#FFF",src:a.avatar,title:a.rankShowName}})],1),s("el-tooltip",{attrs:{placement:"top"}},[s("div",{attrs:{slot:"content"},slot:"content"},[t._v(" "+t._s(a.isConcerned?t.$t("m.Unfollow"):t.$t("m.Top_And_Follow"))+" ")]),s("span",{staticClass:"contest-rank-concerned",on:{click:function(e){return t.updateConcernedList(a.uid,!a.isConcerned)}}},[a.isConcerned?s("i",{staticClass:"fa fa-star",staticStyle:{color:"red"}}):s("i",{staticClass:"el-icon-star-off"})])]),s("span",{staticClass:"contest-rank-user-info"},[s("a",{on:{click:function(e){return t.getUserHomeByUsername(a.uid,a.username)}}},[s("span",{staticClass:"contest-username",attrs:{title:a.rankShowName}},[a.uid==t.userInfo.uid?s("span",{staticClass:"contest-rank-flag"},[t._v("Own")]):t._e(),-1==a.rank?s("span",{staticClass:"contest-rank-flag"},[t._v("Star")]):t._e(),"female"==a.gender?s("span",{staticClass:"contest-rank-flag"},[t._v("Girl")]):t._e(),t._v(" "+t._s(a.rankShowName))]),a.school?s("span",{staticClass:"contest-school",attrs:{title:a.school}},[t._v(t._s(a.school))]):t._e()])])],1)]}}])}):s("vxe-table-column",{attrs:{field:"username",fixed:"left","min-width":"300",title:t.$t("m.User"),"header-align":"center",align:"left"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[s("div",{staticClass:"contest-rank-user-box"},[s("span",[s("avatar",{attrs:{username:a.rankShowName,inline:!0,size:37,color:"#FFF",src:a.avatar,title:a.rankShowName}})],1),s("el-tooltip",{attrs:{placement:"top"}},[s("div",{attrs:{slot:"content"},slot:"content"},[t._v(" "+t._s(a.isConcerned?t.$t("m.Unfollow"):t.$t("m.Top_And_Follow"))+" ")]),s("span",{staticClass:"contest-rank-concerned",on:{click:function(e){return t.updateConcernedList(a.uid,!a.isConcerned)}}},[a.isConcerned?s("i",{staticClass:"fa fa-star",staticStyle:{color:"red"}}):s("i",{staticClass:"el-icon-star-off"})])]),s("span",{staticClass:"contest-rank-user-info"},[s("a",{on:{click:function(e){return t.getUserHomeByUsername(a.uid,a.username)}}},[s("span",{staticClass:"contest-username",attrs:{title:a.rankShowName}},[a.uid==t.userInfo.uid?s("span",{staticClass:"contest-rank-flag"},[t._v("Own")]):t._e(),-1==a.rank?s("span",{staticClass:"contest-rank-flag"},[t._v("Star")]):t._e(),"female"==a.gender?s("span",{staticClass:"contest-rank-flag"},[t._v("Girl")]):t._e(),t._v(" "+t._s(a.rankShowName))]),a.school?s("span",{staticClass:"contest-school",attrs:{title:a.school}},[t._v(t._s(a.school))]):t._e()])])],1)]}}],null,!1,3657049288)}),t.isContestAdmin?s("vxe-table-column",{attrs:{field:"realname","min-width":"96",title:t.$t("m.RealName"),"show-overflow":""}}):t._e(),s("vxe-table-column",{attrs:{field:"totalScore",title:t.$t("m.Total_Score"),"min-width":"90"},scopedSlots:t._u([{key:"default",fn:function(e){var a=e.row;return[s("span",[s("a",{staticStyle:{color:"rgb(87, 163, 243)"},on:{click:function(e){return t.getUserTotalSubmit(a.username)}}},[t._v(t._s(a.totalScore))]),s("br"),s("span",{staticClass:"problem-time"},[t._v("("+t._s(a.totalTime)+"ms)")])])]}}])}),t._l(t.contestProblems,(function(e){return s("vxe-table-column",{key:e.displayId,attrs:{"min-width":"80",field:e.displayId},scopedSlots:t._u([{key:"header",fn:function(){return[e.color?s("span",{staticClass:"contest-rank-balloon"},[s("svg",{staticClass:"icon",attrs:{t:"1633685184463",viewBox:"0 0 1088 1024",version:"1.1",xmlns:"http://www.w3.org/2000/svg","p-id":"5840",width:"25",height:"25"}},[s("path",{attrs:{d:"M575.872 849.408c-104.576 0-117.632-26.56-119.232-31.808-6.528-22.528 32.896-70.592 63.744-96.768l-1.728-2.624c137.6-42.688 243.648-290.112 243.648-433.472A284.544 284.544 0 0 0 478.016 0a284.544 284.544 0 0 0-284.288 284.736c0 150.4 116.352 415.104 263.744 438.336-25.152 29.568-50.368 70.784-39.104 108.928 12.608 43.136 62.72 63.232 157.632 63.232 7.872 0 11.52 9.408 4.352 19.52-21.248 29.248-77.888 63.424-167.68 63.424V1024c138.944 0 215.936-74.816 215.936-126.528a46.72 46.72 0 0 0-16.32-36.608 56.32 56.32 0 0 0-36.416-11.456zM297.152 297.472c0 44.032-38.144 25.344-38.144-38.656 0-108.032 85.248-195.712 190.592-195.712 62.592 0 81.216 39.232 38.08 39.232-105.152 0.064-190.528 87.04-190.528 195.136z",fill:e.color,"p-id":"5841"}})])]):t._e(),s("span",[s("a",{staticClass:"emphasis",staticStyle:{color:"#495060"},on:{click:function(s){return t.getContestProblemById(e.displayId)}}},[t._v(" "+t._s(e.displayId)+" ")])]),s("br"),s("span",[s("el-tooltip",{attrs:{effect:"dark",placement:"top"}},[s("div",{attrs:{slot:"content"},slot:"content"},[t._v(" "+t._s(e.displayId+". "+e.displayTitle)+" "),s("br"),t._v(" "+t._s("Accepted: "+e.ac)+" "),s("br"),t._v(" "+t._s("Rejected: "+(e.total-e.ac))+" ")]),s("span",[t._v("("+t._s(e.ac)+"/"+t._s(e.total)+") ")])])],1)]},proxy:!0},{key:"default",fn:function(a){return a=a.row,[a.submissionInfo[e.displayId]?s("div",{staticClass:"submission-hover"},[s("span",[t._v(t._s(a.submissionInfo[e.displayId]))]),s("br"),a.timeInfo&&null!=a.timeInfo[e.displayId]?s("span",{staticStyle:{"font-size":"12px"}},[t._v("("+t._s(a.timeInfo[e.displayId])+"ms)")]):t._e()]):t._e()]}}],null,!0)})}))],2)],1),s("Pagination",{attrs:{total:t.total,"page-size":t.limit,current:t.page,"page-sizes":[10,30,50,100,300,500],layout:"prev, pager, next, sizes"},on:{"update:pageSize":function(e){t.limit=e},"update:page-size":function(e){t.limit=e},"update:current":function(e){t.page=e},"on-change":t.getContestRankData,"on-page-size-change":function(e){return t.getContestRankData(1)}}})],1)}),[],!1,null,"477b41ea",null);e.default=a.exports},a6b8:function(t,e,s){},f7c7:function(t,e,s){"use strict";s("a6b8")}}]);
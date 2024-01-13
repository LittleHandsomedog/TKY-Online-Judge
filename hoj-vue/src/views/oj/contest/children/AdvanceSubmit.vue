<template>
  <div>
    <el-card class="box-card"
             v-if="displayStatus==0">
      <div slot="header"
           class="header">
        <span class="title">{{$t('m.Advance_Submit')}}</span>
        <el-button type="danger"
                   @click="advanceSubmit">{{$t('m.Advance_Submit')}}</el-button>
      </div>
      <div class="body">
        <p>{{$t('m.Agreement')}}</p>
      </div>
    </el-card>
    <el-card class="box-card"
             v-else-if="displayStatus==1">
      <div slot="header"
           class="header">
        <p class="title">{{$t('m.Already_Advance_Submit')}}</p>
      </div>
      <div class="body">
        <p class="thanks">{{$t('m.Thanks_for_Paticipate')}}</p>
      </div>
    </el-card>
    <el-card v-else>
      <div slot="header"
           class="header">
        <span class="title">{{$t('m.Submission_List')}}</span>
        <el-button type="info"
                   @click="getAdvanceSubmit">{{$t('m.Refresh_Information')}}</el-button>
      </div>
      <vxe-table :data="list" align="center">
        <vxe-table-column field="username"
                         :title="$t('m.Username')"
                         width="180">
        </vxe-table-column>
        <vxe-table-column field="nickname"
                         :title="$t('m.Nickname')"
                         width="180">
        </vxe-table-column>
        <vxe-table-column field="realname"
                         :title="$t('m.RealName')">
        </vxe-table-column>
        <vxe-table-column field="gmtCreate"
                         :title="$t('m.Enter_Contest_Time')">
        </vxe-table-column>
        <vxe-table-column field="gmtModified"
                         :title="$t('m.Submit_Time')">
        </vxe-table-column>
        <vxe-table-column field="status"
                         :title="$t('m.Status')">
        </vxe-table-column>
      </vxe-table>
    </el-card>
  </div>
</template>

<script>
import api from '@/common/api';
import myMessage from '@/common/message';
export default {
  data () {
    return {
      input: '',
      displayStatus: 0,
      list: [],
    }
  },
  mounted () {
    this.getAdvanceSubmit()
  },
  methods: {
    advanceSubmit () {
      if(this.$i18n.t('m.Advance_Submit')=='提前结束答题'){
        this.$confirm('确定要提前结束答题吗', '再次提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(() => {
          let cid = this.$route.params.contestID
          api.advanceSubmit(cid).then(() => {
            myMessage.success(this.$i18n.t('m.Submit_Success'))
            this.getAdvanceSubmit()
          })
        })
      }else{
        this.$confirm('Are you sure you want to advance submit early?', 'Reminder again', {
          confirmButtonText: 'Sure',
          cancelButtonText: 'Cancle',
        }).then(() => {
          let cid = this.$route.params.contestID
          api.advanceSubmit(cid).then(() => {
            myMessage.success(this.$i18n.t('m.Submit_Success'))
            this.getAdvanceSubmit()
          })
        })
      }
      
    },
    getAdvanceSubmit () {
      let cid = this.$route.params.contestID
      api.getAdvanceSubmit(cid).then((res) => {
        this.list = res.data.data
        if (this.list.length == 1 && this.list[0].status == 2) {//还没交卷
          this.displayStatus = 0
          return
        }
        if (this.list.length == 1 && this.list[0].status == 3) {//交了
          this.displayStatus = 1
          return
        }
        this.displayStatus = 2//管理员模式
        this.list.forEach((item) => {
          item.gmtModified = this.formattedDate(item.gmtModified)
          item.gmtCreate = this.formattedDate(item.gmtCreate)
          if (item.status == 0) {
            item.gmtModified = ''
            item.status = ''
          } else {
            item.status = this.$i18n.t('m.Submit_status')
          }
        })
      })
    },
    formattedDate (dateString) {
      // 创建一个Date对象
      var date = new Date(dateString)
      // 获取年、月、日、小时、分钟、秒
      var year = date.getFullYear()
      var month = (date.getMonth() + 1).toString().padStart(2, '0')
      var day = date.getDate().toString().padStart(2, '0')
      var hours = date.getHours().toString().padStart(2, '0')
      var minutes = date.getMinutes().toString().padStart(2, '0')
      var seconds = date.getSeconds().toString().padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.body {
  display: flex;
  flex-direction: column;
  height: 250px;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  font-size: 16px;
}
.title {
  font-size: 21px;
  display: flex;
  align-items: center;
}
.thanks {
  font-size: 16px;
}
.thanks {
  text-align: center;
}
.box-card {
  height: 400px;
}

.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
</style>
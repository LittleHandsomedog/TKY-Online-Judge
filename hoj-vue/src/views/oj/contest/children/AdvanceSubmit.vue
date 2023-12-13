<template>
  <div>
    <el-card class="box-card"
             v-if="displayStatus==0">
      <div slot="header"
           class="header">
        <span class="title">提前结束答题</span>
        <el-button type="danger"
                   @click="advanceSubmit"
                   :disabled="input!==`失去`">提前结束答题</el-button>
      </div>
      <div class="body">
        <p class="content">本人明确理解并同意：点击右上角“提前结束答题”按钮意味着我将不可恢复地</p>
        <el-input v-model="input"
                  placeholder="失去"></el-input>
        <p class="content">在本场比赛提交答案的权利。</p>
      </div>
    </el-card>
    <el-card class="box-card"
             v-else-if="displayStatus==1">
      <div slot="header"
           class="header">
        <p class="title">你已经提前交卷</p>
      </div>
      <div class="body">
        <p class="thanks">感谢你的参与</p>
      </div>
    </el-card>
    <el-card v-else>
      <div slot="header"
           class="header">
        <span class="title">交卷名单</span>
        <el-button type="info"
                   @click="getAdvanceSubmit">刷新信息</el-button>
      </div>
      <el-table :data="list"
                style="width: 100%">
        <el-table-column prop="username"
                         label="用户名"
                         width="180">
        </el-table-column>
        <el-table-column prop="nickname"
                         label="昵称"
                         width="180">
        </el-table-column>
        <el-table-column prop="realname"
                         label="真实姓名">
        </el-table-column>
        <el-table-column prop="gmtCreate"
                         label="注册时间">
        </el-table-column>
        <el-table-column prop="gmtModified"
                         label="提交时间">
        </el-table-column>
        <el-table-column prop="status"
                         label="状态">
        </el-table-column>
      </el-table>
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
      let cid = this.$route.params.contestID
      api.advanceSubmit(cid).then(() => {
        myMessage.success("交卷成功")
        this.getAdvanceSubmit()
      })
    },
    getAdvanceSubmit () {
      let cid = this.$route.params.contestID
      api.getAdvanceSubmit(cid).then((res) => {
        this.list = res.data.data
        console.log(this.list);
        if (this.list.length == 0) {//还没交卷
          this.displayStatus = 0
          return
        }
        if (this.list[0].status == 3) {//交了
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
            item.status = '已交卷'
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
}
.title {
  font-size: 21px;
  display: flex;
  align-items: center;
}
.content,
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
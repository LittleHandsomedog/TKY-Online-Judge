<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="header">
        <span class="title">你确定要提前结束答题吗</span>
        <el-button class="button" type="danger" @click="advanceSubmit" :disabled="input!==`失去`">提前结束答题</el-button>
      </div>
      <div class="body">
        <span class="title">本人明确理解并同意：点击右上角“提前结束答题”按钮意味着我将不可恢复地
          <el-input class="input" v-model="input" placeholder="失去"></el-input>
        在本场考试提交答案的权利。</span>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data(){
    return{
      input:''
    }
  },
  methods:{
    advanceSubmit(){
      console.log("正在研发中");
    }
  }
}
</script>

<style scoped>
  .header{
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .title{
    font-size: 21px;
    display: flex;
    align-items: center;
  }
  .body{
    display: flex;
    align-items: center;
    justify-content: center;
    height: 250px;
  }
  .box-card {
    height: 400px;
  }
  .input{
    width: 60px;
  }
</style>
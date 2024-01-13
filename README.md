# Hcode Online Judge（HOJ）

![logo](./logo.png)

## 一、总概

- 基于HOJ二次开发的平台
- 在HOJ的基础功能上新增：IP限制功能，比赛成员提前交卷，题目中切换上一题下一题功能

|               在线Demo               |                   在线文档                   |             Github&Gitee仓库地址             |           QQ群           |
| :--------------------------------: | :--------------------------------------: | :--------------------------------------: | :---------------------: |
| [https://hdoi.cn](https://hdoi.cn) | [https://docs.hdoi.cn](https://docs.hdoi.cn) | [https://github.com/HimitZH/HOJ](https://github.com/HimitZH/HOJ)  [https://gitee.com/himitzh0730/hoj](https://gitee.com/himitzh0730/hoj) | 598587305（已满）、743568562 |

**注意：**

1. **建议使用Centos8以上或Ubuntu16.04以上的操作系统！！！不然判题机（judgeserver）可能无法正常启动**
2. **若一定要用Centos7系统，部署请先看文档说明：[https://docs.hdoi.cn/deploy/faq/](https://docs.hdoi.cn/deploy/faq/)**
3. **服务器配置尽可能使用2核4G以上，保证服务的正常启动与运行。**
4. **尽量不要使用突发性能或共享型的云服务器实例，有可能造成评测时间计量不准确。**
5. **有任何部署问题或项目bug请发issue或者加QQ群。**
6. **如果要对本项目进行商业化，请在页面底部的Powered by指向HOJ本仓库地址，顺便点上star收藏本项目对开发者的支持，谢谢。**

## 二、原版HOJ部署

部署文档：[基于docker-compose部署](https://docs.hdoi.cn/deploy/docker)

部署仓库：https://gitee.com/himitzh0730/hoj-deploy

## 三、TKYOJ部署

部署仓库：

## 四、如何添加二次开发的功能

- 前端文件为hoj-vue，后端文件为hoj-springboot
- 该文档讲述了如何自定义前端[自定义前端 | HOJ (hdoi.cn)](https://docs.hdoi.cn/use/update-fe/#一、完全自定义前端)
- 如果需要本地启动后端并对后端进行新增修改，请前往教程学习[onlineJudge搭建教程，HOJ搭建教程，HOJ二开教程 - longkui - 博客园 (cnblogs.com)](https://www.cnblogs.com/longkui-site/p/16747636.html)

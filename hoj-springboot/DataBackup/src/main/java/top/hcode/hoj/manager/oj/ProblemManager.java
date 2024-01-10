package top.hcode.hoj.manager.oj;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.SecurityUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.hcode.hoj.annotation.HOJAccessEnum;
import top.hcode.hoj.common.exception.StatusAccessDeniedException;
import top.hcode.hoj.common.exception.StatusFailException;
import top.hcode.hoj.common.exception.StatusForbiddenException;
import top.hcode.hoj.common.exception.StatusNotFoundException;
import top.hcode.hoj.dao.contest.ContestEntityService;
import top.hcode.hoj.dao.judge.JudgeEntityService;
import top.hcode.hoj.dao.problem.*;
import top.hcode.hoj.exception.AccessException;
import top.hcode.hoj.mapper.ContestProblemMapper;
import top.hcode.hoj.mapper.ProblemMapper;
import top.hcode.hoj.pojo.dto.LastAcceptedCodeVO;
import top.hcode.hoj.pojo.dto.PidListDTO;
import top.hcode.hoj.pojo.entity.contest.Contest;
import top.hcode.hoj.pojo.entity.contest.ContestProblem;
import top.hcode.hoj.pojo.entity.judge.Judge;
import top.hcode.hoj.pojo.entity.problem.*;
import top.hcode.hoj.pojo.vo.*;
import top.hcode.hoj.shiro.AccountProfile;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.validator.AccessValidator;
import top.hcode.hoj.validator.ContestValidator;
import top.hcode.hoj.validator.GroupValidator;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Himit_ZH
 * @Date: 2022/3/11 10:37
 * @Description:
 */
@Component
public class ProblemManager {
    @Autowired
    private ProblemEntityService problemEntityService;

    @Autowired
    private ProblemTagEntityService problemTagEntityService;

    @Autowired
    private JudgeEntityService judgeEntityService;

    @Autowired
    private TagEntityService tagEntityService;

    @Autowired
    private LanguageEntityService languageEntityService;

    @Autowired
    private ContestEntityService contestEntityService;

    @Autowired
    private ProblemLanguageEntityService problemLanguageEntityService;

    @Autowired
    private CodeTemplateEntityService codeTemplateEntityService;

    @Autowired
    private ContestValidator contestValidator;

    @Autowired
    private GroupValidator groupValidator;

    @Autowired
    private AccessValidator accessValidator;

    @Autowired
    private TrainingManager trainingManager;

    @Autowired
    private ContestManager contestManager;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private ContestProblemMapper contestProblemMapper;

    /**
     * @MethodName getProblemList
     * @Params * @param null
     * @Description 获取题目列表分页
     * @Since 2020/10/27
     */
    public Page<ProblemVO> getProblemList(Integer limit, Integer currentPage,
                                          String keyword, List<Long> tagId, Integer difficulty, String oj) {
        // 页数，每页题数若为空，设置默认值
        if (currentPage == null || currentPage < 1) currentPage = 1;
        if (limit == null || limit < 1) limit = 10;

        // 关键词查询不为空
        if (!StringUtils.isEmpty(keyword)) {
            keyword = keyword.trim();
        }
        if (oj != null && !Constants.RemoteOJ.isRemoteOJ(oj)) {
            oj = "Mine";
        }
        return problemEntityService.getProblemList(limit, currentPage, null, keyword,
                difficulty, tagId, oj);
    }

    /**
     * @MethodName getRandomProblem
     * @Description 随机选取一道题目
     * @Since 2020/10/27
     */
    public RandomProblemVO getRandomProblem() throws StatusFailException {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        // 必须是公开题目
        queryWrapper.select("problem_id").eq("auth", 1)
                .eq("is_group", false);
        List<Problem> list = problemEntityService.list(queryWrapper);
        if (list.size() == 0) {
            throw new StatusFailException("获取随机题目失败，题库暂无公开题目！");
        }
        Random random = new Random();
        int index = random.nextInt(list.size());
        RandomProblemVO randomProblemVo = new RandomProblemVO();
        randomProblemVo.setProblemId(list.get(index).getProblemId());
        return randomProblemVo;
    }

    /**
     * @MethodName getUserProblemStatus
     * @Description 获取用户对应该题目列表中各个题目的做题情况
     * @Since 2020/12/29
     */
    public HashMap<Long, Object> getUserProblemStatus(PidListDTO pidListDto) throws StatusNotFoundException {

        // 需要获取一下该token对应用户的数据
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        HashMap<Long, Object> result = new HashMap<>();
        // 先查询判断该用户对于这些题是否已经通过，若已通过，则无论后续再提交结果如何，该题都标记为通过
        QueryWrapper<Judge> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("distinct pid,status,submit_time,score")
                .in("pid", pidListDto.getPidList())
                .eq("uid", userRolesVo.getUid())
                .orderByDesc("submit_time");

        if (pidListDto.getIsContestProblemList()) {
            // 如果是比赛的提交记录需要判断cid
            queryWrapper.eq("cid", pidListDto.getCid());
        } else {
            queryWrapper.eq("cid", 0);
            if (pidListDto.getGid() != null) {
                queryWrapper.eq("gid", pidListDto.getGid());
            } else {
                queryWrapper.isNull("gid");
            }
        }
        List<Judge> judges = judgeEntityService.list(queryWrapper);

        boolean isACMContest = true;
        boolean isContainsContestEndJudge = false;
        long contestEndTime = 0L;
        Contest contest = null;
        if (pidListDto.getIsContestProblemList()) {
            contest = contestEntityService.getById(pidListDto.getCid());
            if (contest == null) {
                throw new StatusNotFoundException("错误：该比赛不存在！");
            }
            isACMContest = contest.getType().intValue() == Constants.Contest.TYPE_ACM.getCode();
            isContainsContestEndJudge = Objects.equals(contest.getAllowEndSubmit(), true)
                    && Objects.equals(pidListDto.getContainsEnd(), true);
            contestEndTime = contest.getEndTime().getTime();
        }
        boolean isSealRank = false;
        if (!isACMContest && CollectionUtil.isNotEmpty(judges)) {
            isSealRank = contestValidator.isSealRank(userRolesVo.getUid(), contest, false,
                    SecurityUtils.getSubject().hasRole("root"));
        }
        for (Judge judge : judges) {

            HashMap<String, Object> temp = new HashMap<>();
            if (pidListDto.getIsContestProblemList()) { // 如果是比赛的题目列表状态

                // 如果是隐藏比赛后的提交，需要判断提交时间进行过滤
                if (!isContainsContestEndJudge && judge.getSubmitTime().getTime() >= contestEndTime) {
                    continue;
                }

                if (!isACMContest) {
                    if (!result.containsKey(judge.getPid())) {
                        // IO比赛的，如果还未写入，则使用最新一次提交的结果
                        // 判断该提交是否为封榜之后的提交,OI赛制封榜后的提交看不到提交结果，
                        // 只有比赛结束可以看到,比赛管理员与超级管理员的提交除外
                        if (isSealRank) {
                            temp.put("status", Constants.Judge.STATUS_SUBMITTED_UNKNOWN_RESULT.getStatus());
                            temp.put("score", null);
                        } else {
                            temp.put("status", judge.getStatus());
                            temp.put("score", judge.getScore());
                        }
                        result.put(judge.getPid(), temp);
                    }
                } else {
                    if (judge.getStatus().intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus()) {
                        // 如果该题目已通过，且同时是为不封榜前提交的，则强制写为通过（0）
                        temp.put("status", Constants.Judge.STATUS_ACCEPTED.getStatus());
                        temp.put("score", null);
                        result.put(judge.getPid(), temp);
                    } else if (!result.containsKey(judge.getPid())) {
                        // 还未写入，则使用最新一次提交的结果
                        temp.put("status", judge.getStatus());
                        temp.put("score", null);
                        result.put(judge.getPid(), temp);
                    }
                }

            } else { // 不是比赛题目
                if (judge.getStatus().intValue() == Constants.Judge.STATUS_ACCEPTED.getStatus()) {
                    // 如果该题目已通过，则强制写为通过（0）
                    temp.put("status", Constants.Judge.STATUS_ACCEPTED.getStatus());
                    result.put(judge.getPid(), temp);
                } else if (!result.containsKey(judge.getPid())) {
                    // 还未写入，则使用最新一次提交的结果
                    temp.put("status", judge.getStatus());
                    result.put(judge.getPid(), temp);
                }
            }
        }

        // 再次检查，应该可能从未提交过该题，则状态写为-10
        for (Long pid : pidListDto.getPidList()) {
            // 如果是比赛的题目列表状态
            if (pidListDto.getIsContestProblemList()) {
                if (!result.containsKey(pid)) {
                    HashMap<String, Object> temp = new HashMap<>();
                    temp.put("score", null);
                    temp.put("status", Constants.Judge.STATUS_NOT_SUBMITTED.getStatus());
                    result.put(pid, temp);
                }
            } else {
                if (!result.containsKey(pid)) {
                    HashMap<String, Object> temp = new HashMap<>();
                    temp.put("status", Constants.Judge.STATUS_NOT_SUBMITTED.getStatus());
                    result.put(pid, temp);
                }
            }
        }
        return result;

    }

    /**
     * @MethodName getProblemInfo
     * @Description 获取指定题目的详情信息，标签，所支持语言，做题情况（只能查询公开题目 也就是auth为1）
     * @Since 2020/10/27
     */
    public ProblemInfoVO getProblemInfo(String problemId, Long gid) throws StatusNotFoundException, StatusForbiddenException {
        QueryWrapper<Problem> wrapper = new QueryWrapper<Problem>().eq("problem_id", problemId);
        //查询题目详情，题目标签，题目语言，题目做题情况
        Problem problem = problemEntityService.getOne(wrapper, false);
        if (problem == null) {
            throw new StatusNotFoundException("该题号对应的题目不存在");
        }
        if (problem.getAuth() != 1) {
            throw new StatusForbiddenException("该题号对应题目并非公开题目，不支持访问！");
        }

        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if (problem.getIsGroup() && !isRoot) {
            if (gid == null) {
                throw new StatusForbiddenException("题目为团队所属，此处不支持访问，请前往团队查看！");
            }
            if (!groupValidator.isGroupMember(userRolesVo.getUid(), problem.getGid())) {
                throw new StatusForbiddenException("对不起，您并非该题目所属的团队内成员，无权查看题目！");
            }
        }

        QueryWrapper<ProblemTag> problemTagQueryWrapper = new QueryWrapper<>();
        problemTagQueryWrapper.eq("pid", problem.getId());

        // 获取该题号对应的标签id
        List<Long> tidList = new LinkedList<>();
        problemTagEntityService.list(problemTagQueryWrapper).forEach(problemTag -> {
            tidList.add(problemTag.getTid());
        });
        List<Tag> tags = new ArrayList<>();
        if (tidList.size() > 0) {
            tags = (List<Tag>) tagEntityService.listByIds(tidList);
        }

        // 记录 languageId对应的name
        HashMap<Long, String> tmpMap = new HashMap<>();
        // 获取题目提交的代码支持的语言
        List<String> languagesStr = new LinkedList<>();
        QueryWrapper<ProblemLanguage> problemLanguageQueryWrapper = new QueryWrapper<>();
        problemLanguageQueryWrapper.eq("pid", problem.getId()).select("lid");
        List<Long> lidList = problemLanguageEntityService.list(problemLanguageQueryWrapper)
                .stream().map(ProblemLanguage::getLid).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(lidList)) {
            Collection<Language> languages = languageEntityService.listByIds(lidList);
            languages = languages.stream().sorted(Comparator.comparing(Language::getSeq, Comparator.reverseOrder())
                            .thenComparing(Language::getId))
                    .collect(Collectors.toList());
            languages.forEach(language -> {
                languagesStr.add(language.getName());
                tmpMap.put(language.getId(), language.getName());
            });
        }
        // 获取题目的提交记录
        ProblemCountVO problemCount = judgeEntityService.getProblemCount(problem.getId(), gid);

        // 获取题目的代码模板
        QueryWrapper<CodeTemplate> codeTemplateQueryWrapper = new QueryWrapper<>();
        codeTemplateQueryWrapper.eq("pid", problem.getId()).eq("status", true);
        List<CodeTemplate> codeTemplates = codeTemplateEntityService.list(codeTemplateQueryWrapper);
        HashMap<String, String> LangNameAndCode = new HashMap<>();
        if (CollectionUtil.isNotEmpty(codeTemplates)) {
            for (CodeTemplate codeTemplate : codeTemplates) {
                LangNameAndCode.put(tmpMap.get(codeTemplate.getLid()), codeTemplate.getCode());
            }
        }
        // 屏蔽一些题目参数
        problem.setJudgeExtraFile(null)
                .setSpjCode(null)
                .setSpjLanguage(null);

        // 将数据统一写入到一个Vo返回数据实体类中
        return new ProblemInfoVO(problem, tags, languagesStr, problemCount, LangNameAndCode);
    }

    public LastAcceptedCodeVO getUserLastAcceptedCode(Long pid, Long cid) {
        AccountProfile userRolesVo = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if (cid == null) {
            cid = 0L;
        }
        QueryWrapper<Judge> judgeQueryWrapper = new QueryWrapper<>();
        judgeQueryWrapper.select("submit_id", "cid", "code", "username", "submit_time", "language")
                .eq("uid", userRolesVo.getUid())
                .eq("pid", pid)
                .eq("cid", cid)
                .eq("status", 0)
                .orderByDesc("submit_id")
                .last("limit 1");
        List<Judge> judgeList = judgeEntityService.list(judgeQueryWrapper);
        LastAcceptedCodeVO lastAcceptedCodeVO = new LastAcceptedCodeVO();
        if (CollectionUtil.isNotEmpty(judgeList)) {
            Judge judge = judgeList.get(0);
            lastAcceptedCodeVO.setSubmitId(judge.getSubmitId());
            lastAcceptedCodeVO.setLanguage(judge.getLanguage());
            lastAcceptedCodeVO.setCode(buildCode(judge));
        } else {
            lastAcceptedCodeVO.setCode("");
        }
        return lastAcceptedCodeVO;
    }

    private String buildCode(Judge judge) {
        if (judge.getCid() == 0) {
            // 比赛外的提交代码 如果不是超管或题目管理员，需要检查网站是否开启隐藏代码功能
            boolean isRoot = SecurityUtils.getSubject().hasRole("root"); // 是否为超级管理员
            boolean isProblemAdmin = SecurityUtils.getSubject().hasRole("problem_admin");// 是否为题目管理员
            if (!isRoot && !isProblemAdmin) {
                try {
                    accessValidator.validateAccess(HOJAccessEnum.HIDE_NON_CONTEST_SUBMISSION_CODE);
                } catch (AccessException e) {
                    return "Because the super administrator has enabled " +
                            "the function of not viewing the submitted code outside the contest of master station, \n" +
                            "the code of this submission details has been hidden.";
                }
            }
        }
        if (judge.getLanguage().toLowerCase().contains("py")) {
            return judge.getCode() + "\n\n" +
                    "'''\n" +
                    "    @runId: " + judge.getSubmitId() + "\n" +
                    "    @language: " + judge.getLanguage() + "\n" +
                    "    @author: " + judge.getUsername() + "\n" +
                    "    @submitTime: " + DateUtil.format(judge.getSubmitTime(), "yyyy-MM-dd HH:mm:ss") + "\n" +
                    "'''";
        } else if (judge.getLanguage().toLowerCase().contains("ruby")) {
            return judge.getCode() + "\n\n" +
                    "=begin\n" +
                    "* @runId: " + judge.getSubmitId() + "\n" +
                    "* @language: " + judge.getLanguage() + "\n" +
                    "* @author: " + judge.getUsername() + "\n" +
                    "* @submitTime: " + DateUtil.format(judge.getSubmitTime(), "yyyy-MM-dd HH:mm:ss") + "\n" +
                    "=end";
        } else {
            return judge.getCode() + "\n\n" +
                    "/**\n" +
                    "* @runId: " + judge.getSubmitId() + "\n" +
                    "* @language: " + judge.getLanguage() + "\n" +
                    "* @author: " + judge.getUsername() + "\n" +
                    "* @submitTime: " + DateUtil.format(judge.getSubmitTime(), "yyyy-MM-dd HH:mm:ss") + "\n" +
                    "*/";
        }
    }

    public List<ProblemFullScreenListVO> getFullScreenProblemList(Long tid, Long cid)
            throws StatusFailException, StatusForbiddenException, StatusAccessDeniedException {
        if (tid != null) {
            return trainingManager.getProblemFullScreenList(tid);
        } else if (cid != null && cid != 0) {
            return contestManager.getContestFullScreenProblemList(cid);
        } else {
            throw new StatusFailException("请求参数错误：tid或cid不能为空");
        }
    }

    public void updateProblemDifficulty(String pid, Long cid, Integer difficulty) throws StatusForbiddenException, StatusFailException {
        // 是否为超级管理员
        boolean isRoot = SecurityUtils.getSubject().hasRole("root");
        // 只有超级管理员和比赛拥有者才能操作
        if (!isRoot) {
            throw new StatusForbiddenException("对不起，你无权限操作！");
        }
        if (pid == null) {
            throw new StatusFailException("请求参数错误：pid不能为空");
        }
        if (!(difficulty >= 0 && difficulty <= 2)) {
            throw new StatusFailException("请求参数错误：difficulty参数无效");
        }
        UpdateWrapper<Problem> updateWrapper = new UpdateWrapper<>();
        if (cid != null && cid != 0) {
            QueryWrapper<ContestProblem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("display_id",pid);
            ContestProblem contestProblem = contestProblemMapper.selectOne(queryWrapper);
            updateWrapper.eq("id",contestProblem.getPid());
        }else{
            updateWrapper.eq("problem_id", pid);
        }
        Problem problem = new Problem();
        problem.setDifficulty(difficulty);
        int isOk = problemMapper.update(problem, updateWrapper);
        if (isOk == 0) {
            throw new StatusFailException("更新难度失败");
        }
    }


    public PrevOrNextProblemVO getPrevProblem(String pid, Long cid, Long tid, Boolean isRemote) throws StatusFailException {
        PrevOrNextProblemVO prevOrNextProblemVO;
        if (cid != null && cid != 0) {
            prevOrNextProblemVO = problemMapper.getContestPrevProblem(pid, cid);
        } else if (tid != null) {
            prevOrNextProblemVO = problemMapper.getTrainingPrevProblem(pid, tid);
        } else {
            prevOrNextProblemVO = problemMapper.getHomePrevProblem(pid, isRemote);
        }
        if (prevOrNextProblemVO == null) {
            throw new StatusFailException("获取上一题失败！题目已经到顶了！");
        }
        return prevOrNextProblemVO;
    }

    public PrevOrNextProblemVO getNextProblem(String pid, Long cid, Long tid, Boolean isRemote) throws StatusFailException {
        PrevOrNextProblemVO prevOrNextProblemVO;
        if (cid != null && cid != 0) {
            prevOrNextProblemVO = problemMapper.getContestNextProblem(pid, cid);
        } else if (tid != null) {
            prevOrNextProblemVO = problemMapper.getTrainingNextProblem(pid, tid);
        } else {
            prevOrNextProblemVO = problemMapper.getHomeNextProblem(pid, isRemote);
        }
        if (prevOrNextProblemVO == null) {
            throw new StatusFailException("获取下一题失败！题目已经到底了！");
        }
        return prevOrNextProblemVO;
    }
}
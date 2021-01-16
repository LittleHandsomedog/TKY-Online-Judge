package top.hcode.hoj.service;

import top.hcode.hoj.pojo.entity.ContestProblem;
import com.baomidou.mybatisplus.extension.service.IService;
import top.hcode.hoj.pojo.vo.ContestProblemVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-10-23
 */
public interface ContestProblemService extends IService<ContestProblem> {
    List<ContestProblemVo> getContestProblemList(Long cid);
}

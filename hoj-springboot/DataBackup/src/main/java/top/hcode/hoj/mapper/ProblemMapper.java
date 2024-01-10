package top.hcode.hoj.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.hcode.hoj.pojo.vo.PrevOrNextProblemVO;
import top.hcode.hoj.pojo.vo.ProblemVO;
import top.hcode.hoj.pojo.entity.problem.Problem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-10-23
 */
@Mapper
@Repository
public interface ProblemMapper extends BaseMapper<Problem> {
    List<ProblemVO> getProblemList(IPage page,
                                   @Param("pid") Long pid,
                                   @Param("keyword") String keyword,
                                   @Param("difficulty") Integer difficulty,
                                   @Param("tid") List<Long> tid,
                                   @Param("tagListSize") Integer tagListSize,
                                   @Param("oj") String oj);
    PrevOrNextProblemVO getContestPrevProblem(@Param("pid") String pid,
                                                    @Param("cid") Long cid);
    PrevOrNextProblemVO getContestNextProblem(@Param("pid") String pid,
                                                    @Param("cid") Long cid);
    PrevOrNextProblemVO getTrainingPrevProblem(@Param("pid") String pid,
                                                    @Param("tid") Long tid);
    PrevOrNextProblemVO getTrainingNextProblem(@Param("pid") String pid,
                                                     @Param("tid") Long tid);
    PrevOrNextProblemVO getHomePrevProblem(@Param("pid") String pid,
                                                     @Param("isRemote") Boolean isRemote);
    PrevOrNextProblemVO getHomeNextProblem(@Param("pid") String pid,
                                                 @Param("isRemote") Boolean isRemote);

}

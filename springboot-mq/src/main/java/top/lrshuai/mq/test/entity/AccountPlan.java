package top.lrshuai.mq.test.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 入账计划表
 * </p>
 *
 * @author jobob
 * @since 2018-11-24
 */
@Data
@Accessors(chain = true)
public class AccountPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 对应业务数据主键ID
     */
    private Long objectId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 和哪个用户交易的
     */
    private Long tranUserId;

    /**
     * 操作类型：1 -- 收入，0 -- 支出
     */
    private Integer operateState;

    /**
     * 操作的金额
     */
    private BigDecimal operateAmount;

    /**
     * 账户类型：free -- 自由子链，lock--锁定子链
     */
    private String operateCode;

    /**
     * 数据来源：1 -- 转账，2 -- 复投，3 -- 采集，4 -- 系统释放，5 -- 充值,6--众筹
     */
    private Integer operateSource;

    /**
     * 备注信息
     */
    private String description;

    /**
     * 是否入账：0 -- 待入账，1 -- 已入账
     */
    private Integer intoAccount;

    /**
     * 什么时候处理这条数据，当当前时间大于这个时间时执行
     */
    private Date responseTime;

    /**
     * 是否是平台收益，1/是  0/否
     */
    private Integer isAdmin;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    private Long modifyBy;

    private Date modifyTime;


}

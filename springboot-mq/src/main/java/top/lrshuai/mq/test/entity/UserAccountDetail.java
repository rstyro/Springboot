package top.lrshuai.mq.test.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 活期积分流水表
 * </p>
 *
 * @author jobob
 * @since 2018-11-24
 */
@Data
@Accessors(chain = true)
public class UserAccountDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;

    /**
     * 与其交易的用户ID
     */
    private Long transUserId;

    /**
     * 币种
     */
    private String code;

    /**
     * 交易数量
     */
    private BigDecimal amount;

    /**
     * 对应的详细记录id
     */
    private Long detailId;

    /**
     * 交易类型，0转出，1转入
     */
    private Integer state;

    /**
     * 交易类型：1 -- 转账，2 -- 复投，3 -- 采集，4 -- 释放，5 -- 加速释放,6 -- 充值,7--众筹,8---伞下收益
     */
    private Integer detailType;

    /**
     * 转出之前余额
     */
    private BigDecimal beforMoney;

    /**
     * 此笔账单完成之后的活期余额
     */
    private BigDecimal afterMoney;

    private String isDeleted;

    private Integer isHandle;

    /**
     * 时间
     */
    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;


}

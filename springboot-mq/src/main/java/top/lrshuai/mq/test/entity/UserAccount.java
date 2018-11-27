package top.lrshuai.mq.test.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户账户余额表
 * </p>
 *
 * @author jobob
 * @since 2018-11-24
 */
@Data
@Accessors(chain = true)
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 币种
     */
    private String code;

    /**
     * token ,以后可能用到
     */
    private String token;

    /**
     * 地址，以后可能用到
     */
    private String address;

    /**
     * 可用余额
     */
    private BigDecimal amount;

    /**
     * 冻结余额
     */
    private BigDecimal lockAmount;

    private String isDeleted;

    private LocalDateTime createTime;

    private Long createBy;

    private LocalDateTime modifyTime;

    private Long modifyBy;


}

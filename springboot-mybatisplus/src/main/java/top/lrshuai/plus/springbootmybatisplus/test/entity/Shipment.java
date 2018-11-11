package top.lrshuai.plus.springbootmybatisplus.test.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2018-11-07
 */
@Data
@Accessors(chain = true)
public class Shipment  {

    private static final long serialVersionUID = 1L;

    /**
     * 物流单号
     */
    private String orderNumber;

    /**
     * 物流状态：1 -- 已揽件，2 -- 正在路上，3 --- 已签收
     */
    private Integer status;

    /**
     * 物流地址
     */
    private String address;

    /**
     * 接收时间
     */
    private LocalDateTime receiveDate;

    /**
     * 是否已删除
     */
    private String isDel;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}

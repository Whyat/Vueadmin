package com.whyat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TODO
 *
 * @Author Whyat
 * @Date 2021/8/12 15:43
 */
@Data
public class BaseEntity implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private LocalDateTime created;
    private LocalDateTime updated;

    @NotNull
    private Integer status;

}

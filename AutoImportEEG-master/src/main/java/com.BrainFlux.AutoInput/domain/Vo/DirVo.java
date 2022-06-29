package com.BrainFlux.AutoInput.domain.Vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/5/27-16:55
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DirVo {
    String dir;
    Date startTimer;
}

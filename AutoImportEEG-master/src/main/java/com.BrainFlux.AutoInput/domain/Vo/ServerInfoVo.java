package com.BrainFlux.AutoInput.domain.Vo;

import lombok.Data;

/**
 * @Author：geliyang
 * @Version：1.0
 * @Date：2022/6/6-23:44
 * @Since:jdk1.8
 * @Description:TODO
 */
@Data
public class ServerInfoVo {
    private String server;
    private String status;
    private String location;
    private String thread;
    private String type;
    private String sleepTime;
}

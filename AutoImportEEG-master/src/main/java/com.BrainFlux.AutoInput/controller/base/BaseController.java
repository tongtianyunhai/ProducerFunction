package com.BrainFlux.AutoInput.controller.base;


import com.BrainFlux.AutoInput.common.http.AxiosResult;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author：whl
 * @Version：1.0
 * @Date：2021/7/3-10:33
 * @Since:jdk1.8
 */
@RestController
public class BaseController {

    public AxiosResult<Integer> toAxiosResult(int row){
        return row > 0 ?  AxiosResult.success(row):AxiosResult.error(row);
    }

    /**
     * 重载 boolean方法
     * @author chy
     * @param row
     * @return axiosResult
     */
    public AxiosResult<Boolean> toAxiosResult(boolean row){
        return row ?  AxiosResult.success(row):AxiosResult.error(row);
    }
}

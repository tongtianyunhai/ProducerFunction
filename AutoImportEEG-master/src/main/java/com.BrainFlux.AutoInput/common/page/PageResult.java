package com.BrainFlux.AutoInput.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor //所有带参数构造方法
@NoArgsConstructor //无参数构造方法
public class PageResult<T> {

    private long total;

    private List<T> data;

}

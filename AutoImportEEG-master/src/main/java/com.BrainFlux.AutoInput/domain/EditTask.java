package com.BrainFlux.AutoInput.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author whl
 * @since 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EditTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tid;

    private String taskType;

    private Integer status;

    private String additionOne;

    private String additionTwo;

}

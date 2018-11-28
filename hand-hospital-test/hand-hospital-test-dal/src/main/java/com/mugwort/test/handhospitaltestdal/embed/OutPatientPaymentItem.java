package com.mugwort.test.handhospitaltestdal.embed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 门诊缴费模型模型-具体项目模型
 * <p>
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author hugua@myweimai.com
 * @since 2018年06月06日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutPatientPaymentItem {
    private String medicalName;             // 药品名称
    private int quantity;                   // 数量(考虑药品的特殊性，故用String类型记录)
    private String unit;                    // 单位
    private BigDecimal totalAmount;         // 具体项目总价
}

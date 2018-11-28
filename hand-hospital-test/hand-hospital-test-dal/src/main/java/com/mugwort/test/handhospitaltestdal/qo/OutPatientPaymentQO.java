package com.mugwort.test.handhospitaltestdal.qo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 在线缴费查询对象
 * <p>
 * Copyright (c) 2018 Choice, Inc.
 * All Rights Reserved.
 * Choice Proprietary and Confidential.
 *
 * @author baiqian@myweimai.com
 * @since 2018年08月03日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutPatientPaymentQO {
    private String orgId;               // 医院ID
    private String patientName;         // 就诊人名称
    private String relationSelf;        // 和本人关系
    private String cardType;            // 就诊卡类型
    private String certificateNumber;   // 证件号码
    private String cardNumber;          // 卡号
    private String groupId;             // 集团ID
    private String patientInnerNo;      // 患者编号
    private Integer sex;                // 性别
    private Integer page;               // 页数
    private Integer pageSize;           // 每页大小
}

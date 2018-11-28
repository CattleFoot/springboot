package com.mugwort.test.handhospitaltestdal.doc;

import com.hand.hospital.common.bean.GeneralField;
import com.mugwort.test.handhospitaltestdal.embed.OutPatientPaymentItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 门诊缴费模型模型
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
@Document(collection = "out_patient_payment")
public class OutPatientPayment implements Serializable {
    @Id
    private String id;                              // 主键ID
    private String payUserId;                       // 实际缴费人ID
    private String patientInnerNo;                  // 患者院内编号
    private String patientName;                     // 实际就诊人姓名
    private String relationSelf;                    // 关系        参照数据字典
    private String cardType;                        // 就诊卡类型   参照数据字典
    private Integer sex;                            // 性别
    private String certificateNumber;               // 身份证号
    private String cardNumber;                      // 就诊卡号
    private Integer payStatus;                      // 支付状态
    private String prescriptionNumber;              // 处方号
    private BigDecimal totalPrice;                  // 应支付总价
    private String orderNumber;                     // 订单编号(支付完成后回填)
    private BigDecimal payPrice;                    // 实际支付总价
    private String payType;                        // 支付方式    参照 HisPayTypeEnum 枚举
    private String payDate;                         // 支付时间
    private List<OutPatientPaymentItem> payItems;   // 门诊缴费具体项目
    private String doctorId;                        // 医生ID
    private String doctorInnerNumber;               // 医生HIS编号
    private String doctorName;                      // 医生姓名
    private String clinicalDate;	                // 就诊日期
    private String sectionId;                       // 科室ID
    private String sectionInnerNumber;              // 科室HIS院内ID
    private String sectionName;                     // 科室名称
    private String orgId;                           // 机构ID
    private String groupId;                         // 集团ID

    /**
     * 从未缴费数据中获取
     */
    private String treatmentNo;                     // 就诊流水号
    private String patientVoucherNo;                // 患者凭证号

    /**
     * 从支付成功回调中获取
     */
    private String requestThirdNumber;              // 请求第三方结算编号
    private String payThirdNumber;                  // 第三方结算编号
    private String payRechargeNumber;               // 平台充值编号
    private String payOrderNumber;                  // 平台支付单号

    /**
     * 从门诊结算中获取
     */
    private String payBalanceNumber;                // 平台结算编号
    private String payMedicalNumber;                // 医保结算编号
    private String payInnerNo;                      // 院内收费编号

    private GeneralField generalField;              // 通用字段
}

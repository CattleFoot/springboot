package com.mugwort.test.service.handhospitaltestser;

import com.hand.hospital.common.bean.Pager;
import com.mugwort.test.handhospitaltestdal.doc.OutPatientPayment;
import com.mugwort.test.handhospitaltestdal.qo.OutPatientPaymentQO;

public interface OutPatientPaymentService {
    /**
     * 分页查询门诊缴费记录
     *
     * @param qo
     * @param pager
     * @return
     */
    boolean listPaidPagerByParams(OutPatientPaymentQO qo, Pager<OutPatientPayment> pager);
}

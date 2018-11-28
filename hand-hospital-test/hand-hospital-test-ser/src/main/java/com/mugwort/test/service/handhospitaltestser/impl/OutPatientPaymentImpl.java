package com.mugwort.test.service.handhospitaltestser.impl;

import com.hand.hospital.common.bean.Pager;
import com.hand.hospital.common.bean.converter.BeanConverter;
import com.mugwort.test.handhospitaltestdal.doc.OutPatientPayment;
import com.mugwort.test.handhospitaltestdal.qo.OutPatientPaymentQO;
import com.mugwort.test.handhospitaltestdal.repo.OutPatientPaymentRepo;
import com.mugwort.test.service.handhospitaltestser.OutPatientPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OutPatientPaymentImpl implements OutPatientPaymentService {
    @Autowired
    private OutPatientPaymentRepo outPatientPaymentRepo;

    @Override
    public boolean listPaidPagerByParams(OutPatientPaymentQO qo, Pager<OutPatientPayment> pager) {
        Pager<OutPatientPayment> repoPager = new Pager<OutPatientPayment>(pager.getPage(), pager.getPageSize());
        List<OutPatientPayment> list = outPatientPaymentRepo.listPaidPagerByParams(qo, repoPager);
        BeanConverter.copyProp(repoPager, pager);
        log.info("查询成功，list isNull = {}, size = {}", null == list, null != list ? list.size() : 0);
        if (null != list && list.size() > 0) {
            pager.setDataSet(list);
            return true;
        }
        return false;
    }
}

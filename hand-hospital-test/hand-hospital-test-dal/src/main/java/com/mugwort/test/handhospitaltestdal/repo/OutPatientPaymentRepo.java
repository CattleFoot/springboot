package com.mugwort.test.handhospitaltestdal.repo;

import com.hand.hospital.common.bean.DeleteFlagEnum;
import com.hand.hospital.common.bean.Pager;
import com.mugwort.test.handhospitaltestdal.doc.OutPatientPayment;
import com.mugwort.test.handhospitaltestdal.qo.OutPatientPaymentQO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Desc:
 */
@Repository
public class OutPatientPaymentRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<OutPatientPayment> listPaidPagerByParams(OutPatientPaymentQO qo, Pager<OutPatientPayment> pager) {

        Query query = new Query(Criteria.where("generalField.deleteFlag").is(DeleteFlagEnum.OK.value()));
        query.addCriteria(Criteria.where("payStatus").gt(0));
        buildQuery(query, qo);
        int totalCount = (int) mongoTemplate.count(query, OutPatientPaymentQO.class);
        pager.setTotalCount(totalCount);
        query.with(new Sort(Sort.Direction.DESC, ""));
        query.skip(pager.getSkip()).limit(pager.getPageSize());
        return mongoTemplate.find(query, OutPatientPayment.class);

    }

    private void buildQuery(Query query, OutPatientPaymentQO qo) {
        if (StringUtils.isNotEmpty(qo.getCertificateNumber())) {
            query.addCriteria(Criteria.where("certificateNumber").is(qo.getCertificateNumber()));
        }
        if (StringUtils.isNotEmpty(qo.getOrgId())) {
            query.addCriteria(Criteria.where("orgId").is(qo.getOrgId()));
        }
        if (StringUtils.isNotEmpty(qo.getGroupId())) {
            query.addCriteria(Criteria.where("groupId").is(qo.getGroupId()));
        }
        if (StringUtils.isNotEmpty(qo.getPatientName())) {
            query.addCriteria(Criteria.where("patientName").is(qo.getPatientName()));
        }
        if (StringUtils.isNotEmpty(qo.getRelationSelf())) {
            query.addCriteria(Criteria.where("relationSelf").is(qo.getRelationSelf()));
        }
        if (StringUtils.isNotEmpty(qo.getCardType())) {
            query.addCriteria(Criteria.where("cardType").is(qo.getCardType()));
        }
        if (StringUtils.isNotEmpty(qo.getCardNumber())) {
            query.addCriteria(Criteria.where("cardNumber").is(qo.getCardNumber()));
        }
        if (StringUtils.isNotEmpty(qo.getPatientInnerNo())) {
            query.addCriteria(Criteria.where("patientInnerNo").is(qo.getPatientInnerNo()));
        }
        if (qo.getSex() != null) {
            query.addCriteria(Criteria.where("sex").is(qo.getSex()));
        }
    }
}

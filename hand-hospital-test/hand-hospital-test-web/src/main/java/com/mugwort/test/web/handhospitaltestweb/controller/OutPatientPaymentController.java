package com.mugwort.test.web.handhospitaltestweb.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/out_patient_payment")
public class OutPatientPaymentController {
    @GetMapping("/list_paid")
    public void listPaid() {

        /*ApiResponse apiResponse = ApiResponseBuilder.buildSuccess();
        Pager<OutPatientPayment> pager = new Pager(param.getPage() == null ?
                1 : param.getPage(), param.getPageSize() == null ? 20 : param.getPageSize());

        // 分页查询数据
        OutPatientPaymentQO qo = new OutPatientPaymentQO();
        BeanConverter.copyProp(param, qo);
        qo.setGroupId(loginUser.getCurrentGroupId());
        OutPatientPaymentRepo outPatientPaymentService;
        outPatientPaymentService.listPaidPagerByParams(qo, pager);

        // 转换结果,去除明细数据
        List<OutPatientPaymentVO> data = Lists.newArrayList();
        List<String> orgIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(pager.getDataSet())) {
            pager.getDataSet().forEach(item -> {
                OutPatientPaymentVO vo = new OutPatientPaymentVO();
                BeanConverter.copyProp(item, vo);
                vo.setPayStatusName(OrderStatusEnum.getAliasByType(vo.getPayStatus()));
                vo.setPayTypeName(OrgAppSettingEnum.PayTypeEnum.getAliasByType(vo.getPayType()));
                vo.setPayItems(null);
                data.add(vo);
                if (StringUtils.isNotEmpty(item.getOrgId())) {
                    orgIds.add(item.getOrgId());
                }
            });
        }

        // 查询机构名称信息
        if (CollectionUtils.isNotEmpty(orgIds)) {
            ApiResponse<List<OrganizationDTO>> organizationApiResponse = httpOrganizationFacadeClient.listOrganizationByIds(orgIds);
            if (organizationApiResponse.getCode() != 200) {
                log.error("调用 httpOrganizationFacadeClient.listOrganizationByIds 接口失败，参数 orgIds = {}", orgIds);
            } else {
                List<OrganizationDTO> organizations = organizationApiResponse.getData();
                if (CollectionUtils.isNotEmpty(organizations)) {
                    Map<String, String> organizationMap = Maps.newHashMap();
                    organizations.forEach(item -> organizationMap.put(item.getId(), item.getName()));
                    data.forEach(item -> {
                        if (StringUtils.isNotEmpty(item.getOrgId())) {
                            item.setOrgName(organizationMap.get(item.getOrgId()));
                        }
                    });
                }
            }
        }

        Pager<OutPatientPaymentVO> pagerVO = new Pager(param.getPage() == null ? 1 : param.getPage(),
                param.getPageSize() == null ? 20 : param.getPageSize());
        pagerVO.setTotalCount(pager.getTotalCount());
        pager.setSkip(pagerVO.getSkip());
        pagerVO.setDataSet(data);

        Map<String, Pager<OutPatientPaymentVO>> resultMap = Maps.newHashMap();
        resultMap.put("data", pagerVO);
        apiResponse.data(resultMap);
        return apiResponse;*/
    }
}

package com.hq.service;

import com.hq.model.vo.DeploymentVo;
import org.activiti.engine.repository.Deployment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @program: SpringBootLearn
 * @description:
 * @author: Mr.Huang
 * @create: 2019-05-13 17:14
 **/
public interface IWorkFlowService {
    /**
     * 部署工作流
     * @param file
     * @param name
     */
    void deploy(MultipartFile file, String name);

    /**
     * 删除工作流
     * @param id
     * @param cascade
     */
    void deleteDepoly(String id, boolean cascade);

    /**
     * 查询部署的工作流
     * @param deploymentVo
     * @return
     */
    List<Deployment> find(DeploymentVo deploymentVo);
}

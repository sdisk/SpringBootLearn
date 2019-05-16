package com.hq.service;

import com.hq.model.vo.DeploymentVo;
import com.hq.model.vo.ProcessDefinitionVo;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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
    List<Deployment> findDeployment(DeploymentVo deploymentVo);

    /**
     * 查询资源图片的输入流
     * @param deploymentId
     * @param diagramResourceName
     * @return
     */
    InputStream findImageInputStream(String deploymentId, String diagramResourceName);

    /**
     * 查询流程定义
     * @param process
     * @return
     */
    List<ProcessDefinition> findProcessDefinition(ProcessDefinitionVo process);

}

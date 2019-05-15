package com.hq.service.impl;

import com.hq.model.vo.DeploymentVo;
import com.hq.service.IWorkFlowService;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * @description:
 * @create: 2019-05-13 17:41
 **/
@Service
public class WorkFlowServiceImpl implements IWorkFlowService {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private IdentityService identityService;


    @Override
    public void deploy(MultipartFile file, String name) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
            //部署对象
            DeploymentBuilder builder = repositoryService.createDeployment();
            builder.name(name);
            builder.addZipInputStream(zipInputStream);
            builder.deploy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDepoly(String id, boolean cascade) {
        repositoryService.deleteDeployment(id, cascade);
    }

    @Override
    public List<Deployment> find(DeploymentVo deploymentVo) {

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        deploymentQuery.deploymentNameLike("%" + deploymentVo.getName() + "%");
        deploymentQuery.orderByDeploymentName().asc();
        List<Deployment> list = deploymentQuery.list();
        return list;
    }
}

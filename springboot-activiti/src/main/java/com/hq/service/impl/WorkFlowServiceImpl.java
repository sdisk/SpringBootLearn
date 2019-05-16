package com.hq.service.impl;

import com.hq.model.vo.DeploymentVo;
import com.hq.model.vo.ProcessDefinitionVo;
import com.hq.service.IWorkFlowService;
import org.activiti.engine.*;
import org.activiti.engine.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    public List<Deployment> findDeployment(DeploymentVo deploymentVo) {

        DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
        deploymentQuery.deploymentNameLike("%" + deploymentVo.getName() + "%");
        deploymentQuery.orderByDeploymentName().asc();
        List<Deployment> list = deploymentQuery.list();
        return list;
    }

    @Override
    public InputStream findImageInputStream(String deploymentId, String diagramResourceName) {
        return repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
    }

    @Override
    public List<ProcessDefinition> findProcessDefinition(ProcessDefinitionVo process) {
        List<ProcessDefinition> list = new ArrayList<>();
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        if (null != process.getKey() && !"".equals(process.getKey())){
            query.processDefinitionKeyLike("%" + process.getKey() + "%");
        }
        if (null != process.getName() && !"".equals(process.getName())){
            query.processDefinitionNameLike("%" + process.getName() + "%");
        }
        if (null != process.getResourceName() && !"".equals(process.getResourceName())){
            query.processDefinitionResourceNameLike("%" + process.getResourceName() + "%");
        }
        if (null != process.getVersion() && !"".equals(process.getVersion())){
            query.processDefinitionVersion(Integer.valueOf(process.getVersion()));
        }

        list = query.orderByProcessDefinitionId().asc().list();
        return list;
    }
}

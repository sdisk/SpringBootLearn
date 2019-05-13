package com.hq.service.impl;

import com.hq.service.IWorkFlowService;
import org.activiti.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    }
}

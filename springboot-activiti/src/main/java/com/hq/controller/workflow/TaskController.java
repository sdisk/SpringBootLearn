package com.hq.controller.workflow;

import com.hq.base.BaseController;
import com.hq.service.IUserService;
import com.hq.service.IWorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 任务
 * @create: 2019-05-16 17:21
 **/
@Controller
@RequestMapping("/system/task")
public class TaskController extends BaseController {

    @Autowired
    private IWorkFlowService workFlowService;
    @Autowired
    private IUserService userService;



}

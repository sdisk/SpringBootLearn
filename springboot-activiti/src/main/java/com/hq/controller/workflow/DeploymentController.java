package com.hq.controller.workflow;

import com.hq.base.BaseController;
import com.hq.model.Result;
import com.hq.model.vo.DeploymentVo;
import com.hq.service.IWorkFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description: 部署
 * @create: 2019-05-13 17:13
 **/
@Controller
@RequestMapping("/deploy")
public class DeploymentController extends BaseController {

    @Autowired
    private IWorkFlowService workFlowService;

    @GetMapping
    @ResponseBody
    public Result<List<DeploymentVo>> find(@ModelAttribute("deployment") DeploymentVo deploymentVo){
        return Result.success();
    }

    @PostMapping
    public String add(@RequestParam("name")String name, @RequestParam("file")MultipartFile file){
        workFlowService.deploy(file, name);
        return "workflow/deployManage";
    }


}

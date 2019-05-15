package com.hq.controller.workflow;

import com.hq.base.BaseController;
import com.hq.model.Result;
import com.hq.model.vo.DeploymentVo;
import com.hq.service.IWorkFlowService;
import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Deployment> list = workFlowService.find(deploymentVo);
        List<DeploymentVo> data = new ArrayList<>(list.size());
        data = list.stream().map(de -> {
            DeploymentVo vo = new DeploymentVo();
            vo.transfor(de);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(data);
    }

    @PostMapping
    public String add(@RequestParam("name")String name, @RequestParam("file")MultipartFile file){
        workFlowService.deploy(file, name);
        return "workflow/deployManage";
    }
    @DeleteMapping("/{id}")
    @ResponseBody
    public Result<String> delete(@PathVariable("id")String id){
        workFlowService.deleteDepoly(id, true);
        return Result.success("删除成功");
    }

}

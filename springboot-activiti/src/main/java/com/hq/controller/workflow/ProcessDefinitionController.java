package com.hq.controller.workflow;

import com.hq.base.BaseController;
import com.hq.model.Result;
import com.hq.model.vo.ProcessDefinitionVo;
import com.hq.service.IWorkFlowService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 流程定义
 * @create: 2019-05-16 09:30
 **/
@Controller
@RequestMapping("/process")
public class ProcessDefinitionController extends BaseController {

    @Autowired
    private IWorkFlowService workFlowService;

    @GetMapping
    @ResponseBody
    public Result<List<ProcessDefinitionVo>> find(@ModelAttribute("process") ProcessDefinitionVo process){
       List<ProcessDefinition> list =  workFlowService.findProcessDefinition(process);
       List<ProcessDefinitionVo> processDefinitionVos = new ArrayList<>(list.size());
       //map 1 -> 1
       processDefinitionVos = list.stream().map(
               pd ->{
                   ProcessDefinitionVo vo =  new ProcessDefinitionVo();
                   vo.transfor(pd);
                   return vo;
               }).collect(Collectors.toList());
       return Result.success(processDefinitionVos);
    }

    @RequestMapping("/lookFlowChart")
    public void lookFlowChart(@RequestParam("deploymentId")String deploymentId,@RequestParam("diagramResourceName")String diagramResourceName) throws IOException {
        //获取资源文件表（act_ge_bytearray）中的资源图片输入流
        InputStream is = workFlowService.findImageInputStream(deploymentId, diagramResourceName);
        //从response对象取输出流
        OutputStream os = getResponse().getOutputStream();
        //输出到输出流中
        for (int i = -1;(i = is.read())!=-1;){
            os.write(i);
        }
        os.close();
        is.close();
    }

}

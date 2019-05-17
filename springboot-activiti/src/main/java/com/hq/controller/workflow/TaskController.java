package com.hq.controller.workflow;

import com.hq.base.BaseController;
import com.hq.common.WebConstant;
import com.hq.model.Result;
import com.hq.model.entity.SysUserEntity;
import com.hq.model.vo.CommentVo;
import com.hq.model.vo.TaskVo;
import com.hq.model.vo.WorkFlowVo;
import com.hq.service.IUserService;
import com.hq.service.IWorkFlowService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @GetMapping
    @ResponseBody
    public Result<List<TaskVo>> find(HttpServletRequest request){
        SysUserEntity user = (SysUserEntity) getSession().getAttribute(WebConstant.LOGIN_USER);
        List<Task> list = workFlowService.findTask(user.getId());
        List<TaskVo> taskVos = new ArrayList<>(list.size());
        taskVos = list.stream().map( task ->{
                TaskVo vo = new TaskVo();
                vo.transfor(task);
                return vo;
        }).collect(Collectors.toList());
        return Result.success(taskVos);
    }

    @RequestMapping("/findComment")
    @ResponseBody
    public Result<List<CommentVo>> findCommentByTaskId(@RequestParam("id")String id){
        List<Comment> list = workFlowService.findCommentByTaskId(id);
        List<CommentVo> commentVos = new ArrayList<CommentVo>(list.size());
        commentVos = list.stream().map( comment -> {
            CommentVo vo = new CommentVo();
            vo.transfor(comment);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(commentVos);
    }

    @RequestMapping("/submit")
    @ResponseBody
    public Result<String> submit(@ModelAttribute("workflow") WorkFlowVo workFlowVo){
        workFlowService.compeleteTask(workFlowVo);
        return Result.success("审核成功");
    }
    @RequestMapping("/getFlowChart")
    public String getFlowChart(@RequestParam("taskId")String taskId, ModelMap map){
        Map<String, Object> address = workFlowService.finfCoordingByTask(taskId);
        ProcessDefinition pd = workFlowService.findProcessDefinitionByTaskId(taskId);
        map.put("address", address);
        map.put("deploymentId", pd.getDeploymentId());
        map.put("diagramResourceName", pd.getDiagramResourceName());
        return "workflow/flowChart";
    }
}

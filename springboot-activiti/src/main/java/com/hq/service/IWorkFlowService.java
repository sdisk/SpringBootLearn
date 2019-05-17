package com.hq.service;

import com.hq.model.vo.DeploymentVo;
import com.hq.model.vo.ProcessDefinitionVo;
import com.hq.model.vo.WorkFlowVo;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

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

    /**
     * 查询当前人的流程
     * @param id
     * @return
     */
    List<Task> findTask(long id);

    /**
     * 根据任务id查询当前实例的所有流程批注
     * @param id
     * @return
     */
    List<Comment> findCommentByTaskId(String id);

    /**
     * 审核工作流
     * @param workFlowVo
     */
    boolean compeleteTask(WorkFlowVo workFlowVo);

    /**
     * 查看当前活动，获取档期活动对应的x,y,width,height，将4个值放入map中
     * @param taskId
     * @return
     */
    Map<String,Object> finfCoordingByTask(String taskId);

    ProcessDefinition findProcessDefinitionByTaskId(String taskId);
}

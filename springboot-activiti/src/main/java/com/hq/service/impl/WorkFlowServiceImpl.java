package com.hq.service.impl;

import com.hq.common.BpmsActivityTypeEnum;
import com.hq.common.WebConstant;
import com.hq.model.entity.SysUserEntity;
import com.hq.model.vo.DeploymentVo;
import com.hq.model.vo.ProcessDefinitionVo;
import com.hq.model.vo.WorkFlowVo;
import com.hq.service.IWorkFlowService;
import com.hq.util.CommonUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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

    @Override
    public List<Task> findTask(long id) {
        List<Task> list = taskService.createTaskQuery().taskAssignee(String.valueOf(id)).
                orderByTaskCreateTime().desc()
                .list();
        return list;
    }

    @Override
    public List<Comment> findCommentByTaskId(String id) {
        List<Comment> list = new ArrayList<>();
        Task task  = taskService.createTaskQuery().taskId(id).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        list = taskService.getProcessInstanceComments(processInstanceId);
        return list;


    }

    @Override
    public boolean compeleteTask(WorkFlowVo workFlowVo) {
        //获取任务ID
        String taskId = workFlowVo.getTaskId();
        //获取连线名称
        String outcome = workFlowVo.getOutcome();
        //批注信息
        String comment = workFlowVo.getComment();
        /**
         * 在完成之前，添加一个批注信息，添加一条数据到act_hi_comment，
         * 记录对当前审核人的审核信息
         */
        //获取任务对象
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //获取流程实例id
        String processInstanceId = task.getProcessInstanceId();
        /**
         * 1.添加批注的时候，activity是使用
         * String userId = Authentication.getAuthenticatedUserId();
         * CommentEntity comment = new CommentEntity();
         * comment.setUserId(userId);
         * 所以需要从session中获取当前登录者，作为任务的审核人，
         * 对应act_hi_comment中的user_id字段，所以要求添加配置执行使用
         * Authentication.setAuthenticatedUserId();添加当前任务的审核人
         */
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute(WebConstant.LOGIN_USER);
        Authentication.setAuthenticatedUserId(String.valueOf(user.getId()));
        taskService.addComment(taskId, processInstanceId, comment);
        /**
         * 2.如果连续是 “默认提交”,就不需要设置，否则需要设置流程变量
         * 在完成任务之前，设置流程变量，安装流线的名称，去完成任务
         *          流程变量的名称: outcome
         *          流程变量的值: 连续的名称
         */
        Map<String, Object> variables = new HashMap<>();
        if (null != outcome && !"默认提交".equals(outcome))
            variables.put("outcome", outcome);
        /**
         * 3.使用任务id，完成当前人的个人任务
         */
        taskService.complete(taskId, variables);
        /**
         * 4.任务完成后，需要指定下一个任务的办理人
         * 可以通过指定变量setAssignee,修改办理人
         */

        /**
         * 5.在完成任务后，判断流程是否结束
         * 若结束，更新表单的状态 1 变成 2 （审核中-->审核完成）
         */
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionId(processInstanceId).singleResult();
        if (null == pi){
            // todo
            // 1 --> 2
        }
        return true;
    }

    @Override
    public Map<String, Object> finfCoordingByTask(String taskId) {
        Map<String, Object> map = new HashMap<>(4);
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        //获取流程定义的id
        String processInstanceId = task.getProcessInstanceId();
        //获取流程定义的实体对象（.bpmn文件中的数据）
        ProcessDefinitionEntity pdEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processInstanceId);
        //流程定义id,查看正在执行的对象表，获取当前活动对应的实例对象
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult();
        //获取当前活动对象
        String activityId = pi.getActivityId();

     /*
        5.x版本中可以使用历史流程节点查找ActivityImpl,  6已经移除
        ActivityImpl activityImpl = pdEntity.findActivity(activityId);//活动ID
        //获取坐标
        map.put("x", activityImpl.getX());
        map.put("y", activityImpl.getY());
        map.put("width", activityImpl.getWidth());
        map.put("height", activityImpl.getHeight());
        return map;*/
        return null;
    }

    @Override
    public ProcessDefinition findProcessDefinitionByTaskId(String taskId) {
        return null;
    }

    public InputStream getResourceDiagramInputStream(String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processInstanceId = task.getProcessInstanceId();
        //获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取流程中已经执行的节点，安装先后顺序进行排序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processDefinitionId(processInstanceId)
                .orderByHistoricActivityInstanceId().asc().list();
        //构建已执行的节点id集合
        List<String>  executedActivityIdList = new ArrayList<>();
        for(HistoricActivityInstance instance : historicActivityInstanceList){
            executedActivityIdList.add(instance.getActivityId());
        }
        //获取bpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        //获取流程已发送的线id集合
        List<String> flowIds = this.getExecutedFlows(bpmnModel, historicActivityInstanceList);
        //使用默认配置获取流程图标生成器
        ProcessDiagramGenerator generator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
        InputStream is = generator.generateDiagram(bpmnModel,"png", executedActivityIdList, flowIds,"宋体","微软雅黑","黑体",null,2.0);
        return is;
    }

    private List<String> getExecutedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstanceList) {
        /**
         * 由于并行网关与包含网关可能存在多个流转，所以要找到全部符合要求的Flow，
         * 其它类型的节点只查找历史活动实例中的第一个节点：
         */
        List<String> flowIdList = new ArrayList<>();

        List<FlowNode> flowNodeList = new ArrayList<>();
        //全部活动实例
        List<HistoricActivityInstance> finshedInstanceList = new LinkedList<>();
        //已完成的历史活动节点
        for(HistoricActivityInstance instance : historicActivityInstanceList){
            flowNodeList.add((FlowNode) bpmnModel.getMainProcess().getFlowElement(instance.getActivityId(), true));
            if (instance.getEndTime() != null){
                finshedInstanceList.add(instance);
            }
        }
        //遍历已完成的实例，从每个实例的outgoingFlow中找到已执行的
        FlowNode curFlowNode = null;
        for (HistoricActivityInstance instance: finshedInstanceList){
            curFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(instance.getActivityId(),true);
            List<SequenceFlow> sequenceFlowList = curFlowNode.getOutgoingFlows();
            //遍历outgoingFlow并找到已经流转的
            /**
             * 满足两个条件：
             * 1.当前节点是并行网关或包含网关，这通过outgoingFolw能够在历史活动中找到的都是已流转
             * 2.当前节点是以上两种之外的，通过outgoingFolw查询时间最近的流程节点是有效流转
             */
            FlowNode targetFlowNode = null;
            if (BpmsActivityTypeEnum.PARALLEL_GATEWAY.getType().equals(instance.getActivityType()) ||
                    BpmsActivityTypeEnum.INCLUSIVE_GATEWAY.getType().equals(instance.getActivityType()) ){
                //遍历历史活动节点
                for (SequenceFlow sequenceFlow : sequenceFlowList){
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (flowNodeList.contains(targetFlowNode)){
                        flowIdList.add(sequenceFlow.getId());
                    }
                }
            } else {
                List<Map<String, String>> tempMapList = new LinkedList<>();
                //遍历历史活动节点，找到匹配flow的节点
                for (SequenceFlow sequenceFlow : sequenceFlowList){
                    for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList){
                        if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())){
                            tempMapList.add(CommonUtil.toMap("flowId", sequenceFlow.getId(),"activityStartTime",
                                    String.valueOf(historicActivityInstance.getStartTime().getTime())));
                        }
                    }
                }
                long earliestStamp = 0L;
                String flowId = null;
                for (Map<String, String> map: tempMapList){
                    long activityStartTime = Long.valueOf(map.get("activityStartTime"));
                    if (earliestStamp == 0  || earliestStamp >= activityStartTime){
                        earliestStamp = activityStartTime;
                        flowId = map.get("flowId");
                    }
                }
                flowIdList.add(flowId);
            }
        }
        return flowIdList;
    }
}

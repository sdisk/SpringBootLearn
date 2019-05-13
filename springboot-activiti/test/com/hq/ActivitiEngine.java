package com.hq;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @create: 2019-05-10 13:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiEngine {

    @Autowired
    private ProcessEngine processEngine;
    /**
     * 用代码生成数据库信息,使用ProcessEngineConfiguration对象
     */
    @Test
    public void createActivitiEngine(){
        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        engineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        engineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/huang_acti?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true");
        engineConfiguration.setJdbcUsername("root");
        engineConfiguration.setJdbcPassword("123456");
        engineConfiguration.setDatabaseSchemaUpdate("true");
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
        System.out.println("引擎创建成功!");
    }

    /**
     * 部署工作流
     */
    @Test
    public void deploy(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //创建一个部署的构建器
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
        //bpmn文件放在了resources下面的processes文件夹下
        deploymentBuilder.addClasspathResource("processes/borrowMoney.bpmn");
        deploymentBuilder.name("请求单流程");
        deploymentBuilder.category("办公类别");
        // act_re_deployment表就有相关的数据
        Deployment deployment = deploymentBuilder.deploy();
        System.out.println("部署的id："+deployment.getId());
        System.out.println("部署的名称："+deployment.getName());
        System.out.println("部署的key："+deployment.getKey());

    }

    /**
     * 执行工作流
     */
    @Test
    public void startProcess(){
        //指定执行刚刚部署的工作流程
        String processDefikey = "borrowMoney";
        //取运行时的runtimeService
        RuntimeService runtimeService  = processEngine.getRuntimeService();
        //取得流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefikey);
        System.out.println("流程实例id："+processInstance.getId());
        System.out.println("流程定义id："+processInstance.getProcessDefinitionId());
    }

    /**
     * 查询任务信息
     */
    @Test
    public void queryTask(){
        //String assignee =  "小米";
        //String assignee =  "中米";
        String assignee =  "大米";
        //取得任务服务
        TaskService taskService = processEngine.getTaskService();
        //创建一个任务查询对象
        TaskQuery taskQuery = taskService.createTaskQuery();
        List<Task> taskList =  taskQuery.taskAssignee(assignee).list();
        //遍历任务列表
        if (taskList != null && taskList.size() > 0 ){
            for(Task task : taskList){
                System.out.println(task.getAssignee());
                System.out.println(task.getId());
                System.out.println(task.getName());
            }
        }
    }

    /**
     * 处理任务
     */
    @Test
    public void compileTask(){
        //String taskId = "15005";
        String taskId = "17502";
        TaskService taskService = processEngine.getTaskService();
        taskService.complete(taskId);
        System.out.println("当前任务执行完毕");
    }

}

package com.hq.model.vo;

import lombok.Data;
import org.activiti.engine.repository.ProcessDefinition;


@Data
public class ProcessDefinitionVo {
    private String id;
    /**
     * 流程定义名称
     */
    private String name;
    /**
     * 流程定义KEY
     */
    private String key;
    /**
     * 流程定义版本
     */
    private String version;
    /**
     * 流程定义的规则文件名称
     */
    private String resourceName;
    /**
     * 流程定义的规则图片名称
     */
    private String diagramResourceName;
    /**
     * 部署ID
     */
    private String deploymentId;

    public void transfor(ProcessDefinition processDefinition){
        this.id = processDefinition.getId();
        this.name = processDefinition.getName();
        this.key = processDefinition.getKey();
        this.version = String.valueOf(processDefinition.getVersion());
        this.resourceName = processDefinition.getResourceName();
        this.diagramResourceName = processDefinition.getDiagramResourceName();
        this.deploymentId = processDefinition.getDeploymentId();
    }
}

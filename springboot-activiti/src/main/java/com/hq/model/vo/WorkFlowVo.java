package com.hq.model.vo;

import lombok.Data;

@Data
public class WorkFlowVo {

    /**
     * 申请单id
     */
    private Long id;

    /**
     * 部署对象id
     */
    private String deploymentId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 连线名称
     */
    private String outcome;

    /**
     * 备注
     */
    private String comment;

}

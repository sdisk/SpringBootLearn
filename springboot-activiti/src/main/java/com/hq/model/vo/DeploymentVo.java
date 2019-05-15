package com.hq.model.vo;

import com.hq.util.DateUtil;
import lombok.Data;
import org.activiti.engine.repository.Deployment;

/**
 * @create: 2019-05-13 17:36
 **/
@Data
public class DeploymentVo {

    private String id;
    private String name;
    private String deploymentTime;

    public void transfor(Deployment deploy){
        this.id = deploy.getId();
        this.name = deploy.getName();
        this.deploymentTime = DateUtil.format(deploy.getDeploymentTime());
    }
}

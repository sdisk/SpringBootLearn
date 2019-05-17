package com.hq.model.vo;

import com.hq.model.entity.SysUserEntity;
import com.hq.util.DateUtil;
import lombok.Data;
import org.activiti.engine.task.Task;

/**
 * @description: 任务
 * @create: 2019-05-17 10:45
 **/
@Data
public class TaskVo {

    private String id;
    private String name;
    private String createTime;
    private SysUserEntity assign;

    public void transfor(Task task){
        this.id = task.getId();
        this.name = task.getName();
        this.createTime = DateUtil.format(task.getCreateTime());
    }
}

package com.hq;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @create: 2019-05-10 13:58
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiEngine {

    /**
     * 用代码生成数据库信息,使用ProcessEngineConfiguration对象
     */
    @Test
    public void createActivitiEngine(){
        ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        engineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        engineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/huang?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai");
        engineConfiguration.setJdbcUsername("root");
        engineConfiguration.setJdbcPassword("123456");
        engineConfiguration.setDatabaseSchemaUpdate("true");
        ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
        System.out.println("引擎创建成功!");
    }

}

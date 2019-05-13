package com.hq.service;

import org.springframework.web.multipart.MultipartFile;

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
}

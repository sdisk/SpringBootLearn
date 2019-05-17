package com.hq.model.vo;

import com.hq.util.DateUtil;
import lombok.Data;
import org.activiti.engine.task.Comment;


@Data
public class CommentVo
{
    private String id;
    private String time;
    private String message;

    public void transfor(Comment comment){
        this.id = comment.getId();
        this.message = comment.getFullMessage();
        this.time = DateUtil.format(comment.getTime());
    }
}

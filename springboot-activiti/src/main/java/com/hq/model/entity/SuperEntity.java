package com.hq.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableLogic;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class SuperEntity<T extends Model<?>> extends Model<T> implements Serializable{


    private static final long serialVersionUID = -2715445862171737570L;

    protected long id;

    protected String createDate;

    protected long creater;

    protected String updateDate;

    protected long updater;

    // 是否删除
    @TableLogic
    protected Integer n_deleted;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}

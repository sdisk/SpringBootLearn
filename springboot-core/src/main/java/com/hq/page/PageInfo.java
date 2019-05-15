package com.hq.page;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 前端table展示对象
 * @create: 2019-05-14 14:51
 **/
@Getter
@Setter
public class PageInfo<T> {
    /**
     * 结果集
     */
    private List<T> rows;

    /**
     * 总数
     */
    private long total;

    public PageInfo(Page<T> page){
        this.rows = page.getRecords();
        this.total = page.getTotal();
    }
}

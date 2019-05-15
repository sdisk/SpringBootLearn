package com.hq.page;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 分页参数查询
 * @create: 2019-05-14 14:56
 **/
@Setter
@Getter
public class PageQuery {

    /**
     * 每页显示个数
     */
    private int limit;

    /**
     * 偏移量
     * 查询的页数 = offset/limit + 1
     * JPA Pageable pageable = PageRequest.of(offset/ limit, limit, sort);
     */
    private int offset;

    /**
     * 排序方式
     * ASC DESC
     */
    private String sortOrder;

    /**
     * 排序列
     */
    private String sortName;

    public PageQuery() {
        super();
    }

    public PageQuery(int limit, int offset) {
        super();
        this.limit = limit;
        this.offset = offset;
    }
    public int getPageSize() {
        return this.limit;
    }

    public int getPageNumber() {
        return this.offset / this.limit + 1;
    }

    @Override
    public String toString() {
        return "PageQuery [limit=" + limit + ", offset=" + offset + ", sortOrder=" + sortOrder + ", sortName=" + sortName + "]";
    }
}

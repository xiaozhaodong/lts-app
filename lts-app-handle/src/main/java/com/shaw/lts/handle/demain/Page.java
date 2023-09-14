package com.shaw.lts.handle.demain;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

/**
 * Page
 * 分页参数
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/28 15:55
 **/
public class Page {

    /**
     * 页码
     */
    @Schema(description = "页码")
    @NotNull(message = "页码不能为空")
    private int pageNum;

    /**
     * 每页条数
     */
    @Schema(description = "每页条数")
    @NotNull(message = "每页条数不能为空")
    private int pageSize;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

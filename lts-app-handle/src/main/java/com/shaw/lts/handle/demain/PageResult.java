package com.shaw.lts.handle.demain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * PageResult
 * 分页
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/28 15:52
 **/
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 7669959186018240853L;

    /**
     * 当前第几页
     */
    @Schema(description = "当前第几页")
    private int pageNum;

    /**
     * 总数量
     */
    @Schema(description = "总数量")
    private long total;

    /**
     * 分页数据
     */
    @Schema(description = "分页数据")
    private List<T> dataList;

    public static <T> PageResult<T> build(int pageNum, long total, List<T> dataList) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPageNum(pageNum);
        pageResult.setTotal(total);
        pageResult.setDataList(dataList);
        return pageResult;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}

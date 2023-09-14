package com.shaw.lts.handle.demain.doc;

import com.shaw.lts.handle.demain.BaseRespDoc;
import com.shaw.lts.handle.demain.PageResult;
import com.shaw.lts.handle.demain.vo.UserCareVo;

/**
 * GetCareUserDoc
 * 获取关心的人 doc
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/26 10:34
 **/
public class GetCareUserDoc extends BaseRespDoc {

    private PageResult<UserCareVo> data;

    public PageResult<UserCareVo> getData() {
        return data;
    }

    public void setData(PageResult<UserCareVo> data) {
        this.data = data;
    }
}

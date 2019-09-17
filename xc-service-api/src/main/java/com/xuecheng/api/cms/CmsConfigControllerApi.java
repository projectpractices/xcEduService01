package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms配置类管理接口", description = "cms配置类管理接口，提供页面的增、删、改、查")
public interface CmsConfigControllerApi {
    /**
     * @param id 当前页
     * @return QueryResponseResult
     */
    @ApiOperation("根据id查询模板文件信息")
    @ApiImplicitParam(name = "id")
    CmsConfig getModel(String id);
}

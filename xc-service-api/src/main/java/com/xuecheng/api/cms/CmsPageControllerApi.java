package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    /**
     * @param page             当前页
     * @param size             每页多少数据
     * @param queryPageRequest 结果集
     * @return QueryResponseResult
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult findPageList(int page, int size, QueryPageRequest queryPageRequest);

    /**
     * 查询所有
     *
     * @return QueryResponseResult
     */
    @ApiOperation("查询所有")
    @ApiImplicitParams({})
    QueryResponseResult findList();
}

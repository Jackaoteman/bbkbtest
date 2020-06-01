package com.liyue.liyuetest.Service;

import com.liyue.liyuetest.Common.BaseCRUDInterface;
import com.liyue.liyuetest.Domain.Company;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

public interface IBookInterface  extends BaseCRUDInterface<Long,Company> {

      @ApiOperation("根据id查询")
      List<Company>   findById(Long id);

}

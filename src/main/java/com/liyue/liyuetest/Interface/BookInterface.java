package com.liyue.liyuetest.Interface;

import com.liyue.liyuetest.Common.BaseCRUDInterface;
import com.liyue.liyuetest.Domain.Company;

/**
 * 对Controllerbao暴露接口
 */
public interface BookInterface extends BaseCRUDInterface<Long,Company> {
    /**
     * 插入一个
     * @param company
     */

}

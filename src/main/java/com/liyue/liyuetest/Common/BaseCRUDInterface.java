package com.liyue.liyuetest.Common;

import com.github.pagehelper.PageInfo;

/**
 * 增删改查基础接口
 * @param <P>
 * @param <T>
 */
public interface BaseCRUDInterface<P,T> {
    /**
     * 插入一个对象
     *
     * @param t
     */
    int insert(T t);

    /**
     * 根据id删除一个对象
     *
     * @param id
     */
    void delete(P id);

    /**
     * 根据主键ID修改一个对象信息
     *
     * @param t
     */
    void update(T t);

    /**
     * 根据id查询一个对象
     *
     * @param id
     * @return
     */
    T getOne(P id);

    /**
     * 查询数据分页
     *
     * @param pageNo        页码
     * @param pageSize      每页数据
     * @param orderBy       排序字段 缺省: create_time desc  learningtime_all  选项：start_time,create_time
     * @param t 查询条件(选填)
     * @return
     */
    PageInfo<T> page(Integer pageNo, Integer pageSize, String orderBy, T t);
}

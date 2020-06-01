package com.liyue.liyuetest.Service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liyue.liyuetest.Dao.CompanyMapper;
import com.liyue.liyuetest.Domain.Company;
import com.liyue.liyuetest.Domain.CompanyExample;
import com.liyue.liyuetest.Service.IBookInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IBookServiceImpl implements IBookInterface {

    //注入mapper接口
    @Autowired
    CompanyMapper companyMapper;

    @Override
    public int insert(Company company) {
      return companyMapper.insert(company);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Company company) {

    }

    @Override
    public Company getOne(Long id) {
        return null;
    }



    @Override
    public PageInfo page(Integer pageNo, Integer pageSize, String orderBy, Company company) {

        System.out.println("分页查询开始拉");
        PageHelper.startPage(pageNo,pageSize);

        //根绝搜索结果将返回值插入PageInfo的构造函数中
     //   CompanyExample example=new CompanyExample();
       // CompanyExample.Criteria criteria = example.createCriteria();
        //criteria.andCompanyIdNotBetween(1,500);
        List<Company> companies = companyMapper.selectAll();
        // List<Company> companies = companyMapper.selectByExample(example);
        PageInfo<Company> pageInfo=new PageInfo(companies);

        return pageInfo;
    }

    @Override
    public List<Company> findById(Long id) {
        return null;
    }
}

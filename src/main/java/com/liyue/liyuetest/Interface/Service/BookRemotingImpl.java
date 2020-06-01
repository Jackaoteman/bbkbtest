package com.liyue.liyuetest.Interface.Service;

import com.github.pagehelper.PageInfo;
import com.liyue.liyuetest.Domain.Company;
import com.liyue.liyuetest.Interface.BookInterface;
import com.liyue.liyuetest.Service.IBookInterface;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookRemotingImpl implements BookInterface {

    @Autowired
    IBookInterface iBookInterface;


    @Override
    public int insert(Company company) {
        try {

            int insert = iBookInterface.insert(company);
        }catch (Exception e){
            if(e.getCause() instanceof MySQLIntegrityConstraintViolationException){

            }
        }

        return   0 ;
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
    public PageInfo<Company> page(Integer pageNo, Integer pageSize, String orderBy, Company company) {
        return iBookInterface.page(pageNo,pageSize,orderBy,company);
    }


}

package com.liyue.liyuetest.Dao;

import com.liyue.liyuetest.Domain.Company;
import com.liyue.liyuetest.Domain.CompanyExample;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CompanyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cw_topic
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cw_topic
     *
     * @mbggenerated
     */
    int insert(Company record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cw_topic
     *
     * @mbggenerated
     */
    int insertSelective(Company record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cw_topic
     *
     * @mbggenerated
     */
    List<Company> selectByExample(CompanyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cw_topic
     *
     * @mbggenerated
     */
    Company selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cw_topic
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Company record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_cw_topic
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Company record);

    List<Company> selectAll();
}
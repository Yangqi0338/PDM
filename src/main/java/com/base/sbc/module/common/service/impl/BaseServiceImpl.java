package com.base.sbc.module.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.base.sbc.config.common.base.BaseEntity;
import com.base.sbc.config.common.base.UserCompany;
import com.base.sbc.config.utils.StringUtils;
import com.base.sbc.config.utils.UserUtils;
import com.base.sbc.module.common.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author 卞康
 * @date 2023/4/13 11:50:06
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M,T> implements BaseService<T> {

    @Resource
    private UserUtils userUtils;

    public static  final String COMPANY_CODE="company_code";
    public static  final String DEL_FLAG="del_flag";

    /**
     * 获取企业编码
     * @return
     */
    public String getCompanyCode(){
        return userUtils.getCompanyCode();
    }

    public String getUserId(){
        return userUtils.getUserCompany().getUserId();
    }
    /**
     * 批量提交修改，逻辑删除新增修改
     * @param entityList 实体列表
     * @param queryWrapper 构造器
     * @return 传入实体列表的总长度
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addAndUpdateAndDelList(List<T> entityList, QueryWrapper<T> queryWrapper) {
        String companyCode = userUtils.getCompanyCode();
        //分类
        // 新增的
        Collection<T> addList = new ArrayList<>();
        // 修改的
        Collection<T> updateList = new ArrayList<>();

        Collection<String> ids=new ArrayList<>();

        for (T entity : entityList) {
            if (StringUtils.isEmpty(entity.getId()) || entity.getId().contains("-")) {
                //说明是新增的
                entity.setId(null);
                addList.add(entity);
            } else {
                //说明是修改的
                updateList.add(entity);
                ids.add(entity.getId());
            }
        }
        queryWrapper.eq("company_code",companyCode);
        //逻辑删除传进来不存在的
        if (ids.size()>0){
            queryWrapper.notIn("id", ids);
        }
        this.remove(queryWrapper);
        //新增
        this.saveBatch(addList);
        //修改
        this.updateBatchById(updateList);

        return entityList.size();
    }

    public void setUpdateInfo(UpdateWrapper uw) {
        UserCompany userCompany = userUtils.getUserCompany();
        uw.set("update_id", userCompany.getId());
        uw.set("update_name", userCompany.getAliasUserName());
        uw.set("update_date", new Date());
    }
}
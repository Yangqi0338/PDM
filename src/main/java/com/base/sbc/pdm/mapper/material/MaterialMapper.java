package com.base.sbc.pdm.mapper.material;

import com.base.sbc.pdm.dao.material.MaterialAllDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 卞康
 * @date 2023/3/24 19:07:58
 */
@Mapper
public interface MaterialMapper {
    List<MaterialAllDto> listQuery(MaterialAllDto materialAllDto);


}

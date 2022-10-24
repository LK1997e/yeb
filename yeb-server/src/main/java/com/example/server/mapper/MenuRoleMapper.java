package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author like
 * @since 2022-09-20
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {
    /**
     * 更新角色菜单
     * @param rid
     * @param mids
     * @return Integer
     */
    Integer insertRecord(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}

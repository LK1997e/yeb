package com.example.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.server.mapper.MenuRoleMapper;
import com.example.server.pojo.Admin;
import com.example.server.pojo.MenuRole;
import com.example.server.pojo.RespBean;
import com.example.server.service.IMenuRoleService;
import com.example.server.util.AdminUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author like
 * @since 2022-09-20
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {
    @Autowired
    private MenuRoleMapper menuRoleMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 更新角色菜单
     *
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        if (null == mids || 0 == mids.length) {
            return RespBean.success("更新成功");
        }
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        if (result == mids.length) {
            ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
            Integer adminId = AdminUtils.getCurrentAdmin().getId();
            valueOperations.set("menu_" + adminId, null);
            return RespBean.success("更新成功！");
        }
        return RespBean.success("更新失败！");
    }
}

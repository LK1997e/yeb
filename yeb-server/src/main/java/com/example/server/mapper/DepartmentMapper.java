package com.example.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.server.pojo.Department;
import com.example.server.pojo.RespBean;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author like
 * @since 2022-09-12
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有部门
     * @return
     * @param parentId
     */
    List<Department> getAllDepartments(Integer parentId);

    /**
     * 添加部门
     * @param department
     * @return
     */
    RespBean addDepartment(Department department);

    /**
     * 删除部门
     * @param department
     */
    void deleteDepartment(Department department);
}

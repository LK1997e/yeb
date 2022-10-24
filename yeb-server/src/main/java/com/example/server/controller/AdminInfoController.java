package com.example.server.controller;

import com.example.server.pojo.Admin;
import com.example.server.pojo.RespBean;
import com.example.server.service.IAdminService;
import com.example.server.util.OssUploadUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class AdminInfoController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "更新当前用户信息")
    @PutMapping("/api/admin/info")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication) {
        admin.setPassword(null);
        if (adminService.updateById(admin)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(admin, null,
                    authentication.getAuthorities()));
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/api/admin/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updateAdminPassword(oldPass, pass, adminId);
    }

    @ApiOperation(value = "更新用户头像")
    @PostMapping("/api/admin/userface")
    public RespBean updateAdminUserFace(MultipartFile file, Integer id, Authentication authentication) {
        String url = OssUploadUtil.uploadFile(file);
        if (StringUtils.isEmpty(url)) {
            return RespBean.error("上传头像文件失败");
        }
        return adminService.updateAdminUserFace(url, id, authentication);

    }
}

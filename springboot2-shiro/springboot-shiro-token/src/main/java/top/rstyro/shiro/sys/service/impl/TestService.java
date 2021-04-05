//package top.rstyro.shiro.sys.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import top.rstyro.shiro.sys.entity.Role;
//import top.rstyro.shiro.sys.entity.User;
//import top.rstyro.shiro.sys.service.IRoleService;
//import top.rstyro.shiro.sys.service.ITestService;
//import top.rstyro.shiro.sys.service.IUserService;
//
//import java.util.UUID;
//
//@Service
//public class TestService implements ITestService {
//
//    @Autowired
//    private IRoleService roleService;
//
//    @Autowired
//    private IUserService userService;
//
//    @Override
//    @Transactional
//    public void testTransactional() {
//        // 测试shior 是否和事务冲突
//        roleService.save(new Role().setRoleName(UUID.randomUUID().toString().replaceAll("-","")).setRemark("随便测试"));
//        System.out.println("===========");
//        System.out.println(1/0);
//        userService.save(new User().setNickName("rstyro").setUsername("rstyro").setPassword("abc"));
//    }
//}

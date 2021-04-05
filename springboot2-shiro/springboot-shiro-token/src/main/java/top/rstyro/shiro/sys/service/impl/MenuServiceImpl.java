package top.rstyro.shiro.sys.service.impl;

import top.rstyro.shiro.sys.entity.Menu;
import top.rstyro.shiro.sys.mapper.MenuMapper;
import top.rstyro.shiro.sys.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author rstyro
 * @since 2021-04-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}

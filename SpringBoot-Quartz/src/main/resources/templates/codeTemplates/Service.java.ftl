package ${package.Service};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
import com.lrs.common.constant.ResponseModel;
import com.lrs.common.dto.PageDTO;
import javax.servlet.http.HttpSession;

/**
 * <p>
 *  ${table.comment!} 服务类
 * </p>
 *
 * @author rstyro
 * @since ${.now?date}
 */
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {
    public ResponseModel getList(PageDTO dto) throws  Exception;
    public ResponseModel add(${entity} item, HttpSession session) throws  Exception;
    public ResponseModel edit(${entity} item, HttpSession session) throws  Exception;
    public ResponseModel del(Long id, HttpSession session) throws  Exception;
    public ResponseModel getDetail(Long id) throws  Exception;
}

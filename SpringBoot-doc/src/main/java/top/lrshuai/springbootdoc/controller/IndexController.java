package top.lrshuai.springbootdoc.controller;

import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/home")
@Api(tags = "首页", description = "首页相关接口")
public class IndexController {


    @GetMapping(path = {"/","/index"})
    @ApiOperation(value ="首页数据",notes = "首页相关")
    public Object index(){
        Map<String,Object> result = new HashMap<>();
        result.put("status","200");
        result.put("msg","ok");
        result.put("data", Lists.newArrayList());
        return result;
    }


    @GetMapping(path ="/hot")
    @ApiOperation(value ="首页热门数据",notes = "首页相关")
    public Object hot(){
        Map<String,Object> result = new HashMap<>();
        result.put("status","200");
        result.put("msg","ok");
        result.put("data", Lists.newArrayList());
        return result;
    }
}

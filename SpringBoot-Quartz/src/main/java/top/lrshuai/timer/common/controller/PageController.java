package top.lrshuai.timer.common.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.timer.common.constant.Result;
import top.lrshuai.timer.task.entity.QuartzDTO;
import top.lrshuai.timer.task.service.IQuartzService;

@Controller
public class PageController {

    @GetMapping("/include/{pageName}")
    public String include(@PathVariable("pageName") String pageName){
        System.out.println("/include/"+pageName);
        return "include/"+pageName;
    }

    @GetMapping("/error/{pageNumber}")
    public String error(@PathVariable("pageNumber") String pageNumber){
        return "error/"+pageNumber;
    }


    @GetMapping(value={"/","/toLogin"})
    public String toLogin(){
        System.out.println("??????");
        return "login";
    }

    @GetMapping("/index")
    public Object index(){
        return "index";
    }

    @Autowired
    private IQuartzService quartzService;

    @GetMapping("/task/quartz/page")
    public Object quartzPage(Model model, QuartzDTO dto){
        model.addAttribute("list",quartzService.getQuartzPage(dto));
        model.addAttribute("keyword",dto.getKeyword());
        return "page/task/quartz_list";
    }

    @RequestMapping(value={"/login"},method=RequestMethod.POST)
    @ResponseBody
    public Result login() throws Exception {
        //TODO
        return Result.ok();
    }

    /**
     * 用户注销
     * @return
     */
    @GetMapping("/logout")
    public String logout() throws Exception {
        return "login";
    }
}

package top.lrshuai.helloword.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
	
	private Logger log = Logger.getLogger(this.getClass());

	@RequestMapping(value={"/index","/"})
	public String index(Model model){
		log.info("来打印log 了");
		model.addAttribute("title", "测试");
		model.addAttribute("atext", "这个冬天不太Cool");
		return "index";
	}
	
}

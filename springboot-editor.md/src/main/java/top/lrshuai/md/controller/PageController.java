package top.lrshuai.md.controller;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import top.lrshuai.md.util.Utils;

@Controller
public class PageController {
	
	String MD_CODE = "MD_CODE";
	
	//为了获取图片资源
	private final ResourceLoader resourceLoader;
	
	@Value("${upload.root.folder}")
	public String root_fold;
	
	@Autowired 
    public PageController(ResourceLoader resourceLoader) {  
        this.resourceLoader = resourceLoader;  
    }  

	/**
	 * 首页
	 * @return
	 */
	@GetMapping(path={"/","/index"})
	public String index(){
		return "index";
	}
	
	/**
	 * 语法
	 * @return
	 */
	@GetMapping(path="/markdown")
	public String markdown(){
		return "markdown";
	}
	
	/**
	 * 去新增页面
	 * @return
	 */
	@GetMapping(path="/goAdd")
	public String add(){
		return "add";
	}
	
	/**
	 * 详情
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping(path="/detail")
	public String detail(Model model,HttpSession session){
		String code = (String) session.getAttribute(MD_CODE);
		if(code != null){
			model.addAttribute(MD_CODE, code);
		}else{
			model.addAttribute(MD_CODE, "编辑点内容吧");
		}
		return "detail";
	}
	
	/**
	 * 去编辑页面
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping(path="/goEdit")
	public String edit(Model model,HttpSession session){
		String code = (String) session.getAttribute(MD_CODE);
		if(code != null){
			model.addAttribute(MD_CODE, code);
		}else{
			model.addAttribute(MD_CODE, "#编辑点内容吧");
		}
		return "edit";
	}
	
	/**
	 * 更新
	 * @param mdcode
	 * @param session
	 * @return
	 */
	@PostMapping(path="/edit")
	public String update(@RequestParam(value="mdcode",required=false) String mdcode,HttpSession session){
		if(!mdcode.isEmpty()){
			session.setAttribute(MD_CODE, mdcode);
		}
		return "success";
	}
	
	/**
	 * 保存
	 * @param session
	 * @param mdcode
	 * @param mdhtml
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String test(HttpSession session, @RequestParam(value="mdcode",required=false) String mdcode,
			@RequestParam(value="mdhtml",required=false) String mdhtml ){
		if(!mdcode.isEmpty()){
			session.setAttribute(MD_CODE, mdcode);
		}
		return "success";
	}
	
	/**
	 * 清除session
	 * @param session
	 * @return
	 */
	@GetMapping(path="/clean")
	public String cleanSession(HttpSession session){
		session.removeAttribute(MD_CODE);
		return "success";
	}
	
	/**
	 * 上传图片
	 * @param request
	 * @param response
	 * @param file
	 */
	@RequestMapping(value="/uploadImg",method=RequestMethod.POST)
	public void uploadImg(HttpServletRequest request,HttpServletResponse response,@RequestParam(value = "editormd-image-file", required = false) MultipartFile file){
		try {
			String filePath = "/images/"+Utils.random(5)+".png";
			String resultPath = Utils.uploadImg(root_fold, filePath, file.getInputStream());
			response.getWriter().write( "{\"success\": 1, \"message\":\"上传成功\",\"url\":\"" + resultPath + "\"}" );
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().write( "{\"success\": 0, \"message\":\"上传失败\",\"url\":\""+ "\"}" );
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	/**
     * 显示上传的图片
     * @param folderName
     * @param date
     * @param filename
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/upload/{folderName}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getImg(@PathVariable("folderName") String folderName,@PathVariable("filename") String filename) {
    	try {
    		return ResponseEntity.ok(resourceLoader.getResource("file:" + Paths.get(root_fold+"/"+folderName, filename).toString()));
    	} catch (Exception e) {
    		return ResponseEntity.notFound().build();
    	}
    }
	
}

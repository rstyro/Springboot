package top.lrshuai.es.entity;

import java.util.HashMap;


public class RespnseModel{

	private static HashMap<String,Object> model=null;
	
	private RespnseModel() {}
	
	public static HashMap<String, Object> getModel(String msg,String status,Object data){
		if(model == null){
			synchronized (HashMap.class) {
                if(model==null)
                model = new HashMap<>();
            }
		}
		if(!msg.isEmpty()){
			model.put("msg", msg);
		}
		if(!status.isEmpty()){
			model.put("status", status);
		}
		if(data != null){
			model.put("data", data);
		}
		return model;
	}
	
	public static HashMap<String, Object> getModel(String msg,int code,Object data){
		if(model == null){
			synchronized (HashMap.class) {
				if(model==null)
					model = new HashMap<>();
			}
		}
		model.put("status", code);
		if(!msg.isEmpty()){
			model.put("msg", msg);
		}
		if(data != null){
			model.put("data", data);
		}
		return model;
	}

	
	public static HashMap<String, Object> getErrorModel(){
		if(model == null){
			synchronized (HashMap.class) {
                if(model==null)
                model = new HashMap<>();
            }
		}
		model.put("status", 500);
		model.put("msg", "请求错误");
		return model;
	}
	
	public static HashMap<String, Object> getNotAuthModel(){
		if(model == null){
			synchronized (HashMap.class) {
                if(model==null)
                model = new HashMap<>();
            }
		}
		model.put("status", 403);
		model.put("msg", "你权限不足");
		return model;
	}
}

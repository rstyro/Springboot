$(function () {
	//表格分页
    $('#menuList').DataTable({
    	"scrollX"	  : true,
  		'paging'      : true,
  	    'lengthChange': true,
  	    'searching'   : true,
  	    'ordering'    : true,
  	    'info'        : true,
  	    'autoWidth'   : false,
  	  	"pagingType"  : "full_numbers",
  	  	"pageLength"  : 10
    });
    //提示框
    $("[data-toggle='tooltip']").tooltip();
    
    //添加一级菜单
    $("#addFirstMenu").click(function(){
    	reloadMenuModel("添加菜单","",0,"","","2","","");
    	reloadActionBtn(_ctx+"/admin/menu/add","添加");
	  	$("#menuModal").modal("show");
    });
    
    //展开菜单
    $(".spreadSubm").click(function(){
    	var parentId = $(this).attr("data-id");
    	var text = $(this).find("font").text();
    	var el = $(this).parent().parent();
    	if("展开" == text){
    		$(this).find("font").text("折叠");
    		$.ajax({
				type:"GET",
		        url:_ctx+"/admin/menu/getSubMenu",
		        data:{parentId:parentId,time:new Date().getTime()},
		        dataType:"json",
		        cache:false,
		        success: function(data){
		       	 if("200" == data.status){
					showSubMeunList(data.data,el,parentId)
		       	 }else{
					alert(data.message);
		       	 }
		        }
			})
    	}else{
    		$(this).find("font").text("展开");
    		$(".submenu"+parentId).remove();
    	}
    });
    
    $("#submitBtn").click(function(){
    	var menuId = $("input[name='menu_id']").val();
    	var actionUrl = $("input[name='actionurl']").val();
    	var parentId = $("input[name='parent_id']").val();
    	var menuName = $("input[name='menu_name']").val();
    	var menuUrl = $("input[name='menu_url']").val();
    	var menuType = $("[name='menu_type']").val();
    	var menuIcon = $("input[name='menu_icon']").val();
    	var menuOrder = $("input[name='menu_order']").val();
    	$.ajax({
    		type:"POST",
    		url:actionUrl,
    		data:{menuId:menuId,parentId:parentId,menuName:menuName,menuUrl:menuUrl,menuType:menuType,menuIcon:menuIcon,sortNum:menuOrder},
    		dataType:"json",
    		cache:false,
    		success:function(data){
    			if(data.status == '200'){
    				$("#menuModal").modal("hide");
    				//刷新
    				window.location.href=window.location.href;
    			}else{
    				alert(data.message);
    			}
    		}
    	});
    	
    });
  })
  
  //添加子菜单
  function addMenu(parentId){
	  reloadMenuModel("添加菜单","",parentId,"","","2","","");
	  reloadActionBtn(_ctx+"/admin/menu/add","添加");
	  $("#menuModal").modal("show");
  }
  
  //删除菜单
  function delMenu(menuId){
	  if(confirm("你确定要删除此菜单吗？")){
		  $.ajax({
			  type:"GET",		  
			  url:_ctx+"/admin/menu/del/"+menuId,
			  data:{time:new Date()},
			  dataType:"json",
			  cache:false,
			  success:function(data){
				  if(data.status == "200"){
						window.location.href=window.location.href;
				  }else{
					  alert(data.message);
				  }
			  }
		  });
	  }
  }
  //编辑菜单
  function editMenu(menuId){
	  $.ajax({
		  type:"GET",		  
		  url:_ctx+"/admin/menu/query/"+menuId,
		  data:{time:new Date()},
		  dataType:"json",
		  cache:false,
		  success:function(data){
			  if(data.status == "200"){
				  var obj = data.data;
				  console.log("obj:",obj);
				  reloadMenuModel("编辑菜单",obj.menuId,obj.parentId,obj.menuName,obj.menuUrl,obj.menuType,obj.menuIcon,obj.sortNum);
				  reloadActionBtn(_ctx+"/admin/menu/edit","更新");
				  $("#menuModal").modal("show");
			  }else{
				  alert(data.message);
			  }
		  }
	  });
  }
  
  /*
  	添加二级菜单
  */
  function showSubMeunList(data,el,parentId){
	  var subStr="";
	  for(var i=0;i<data.length;i++){
		  var submenu = data[i];
		  var menuType = "业务";
		  if(submenu.menuType == 1){
			  menuType = "系统"
		  }
		  var subm = "<tr class='submenu"+parentId+"'>"
			+"<td align='right'><i class='fa fa-angle-double-right'></i></td>"
			+"<td>"+submenu.menuName+"</td>"
			+"<td>"+submenu.menuUrl+"</td>"
			+"<td>"+menuType+"</td>"
			+"<td>#</td>"
			+"<td>"+submenu.sortNum+"</td>"
			+"<td><span class='btn btn-xs btn-info' onclick='editMenu("+submenu.menuId+")'><i class='fa fa-edit'></i> 修改</span> <span class='btn btn-xs btn-danger' onclick='delMenu("+submenu.menuId+")'><i class='fa fa-trash-o'></i> 删除</span></td>"
			+"</tr>"
		  subStr += subm;
	  }
	  el.after(subStr);
  }
  
  //加载模态框的内容
  function reloadMenuModel(title,menu_id,parent_id,menu_name,menu_url,menu_type,menu_icon,sort_num){
	$("#menuModal #menumodelHead").text(title);
	$("#menuModal input[name='menu_id']").val(menu_id);
	$("#menuModal input[name='parent_id']").val(parent_id);
  	$("#menuModal input[name='menu_name']").val(menu_name);
  	$("#menuModal input[name='menu_url']").val(menu_url);
  	$("#menuModal [name='menu_type']").val(menu_type);
  	$("#menuModal input[name='menu_icon']").val(menu_icon);
  	$("#menuModal input[name='menu_order']").val(sort_num);
  }
  function reloadActionBtn(actionUrl,btnText){
	  $("#menuModal input[name='actionurl']").val(actionUrl);
	  $("#submitBtn").text(btnText);
  }
//树 的配置，具体请看:http://www.treejs.cn/v3/main.php#_zTreeInfo
var setting = {
	check: {
		enable: true
	},
	data: {
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "pId",
			rootPId: 0
		}
	}
};

//树 对象
function menuTree(id,pId,name,checked){
	this.id=id;
	this.pId=pId;
	this.name=name;
	this.checked=checked;
}

//权限处理
function roleMenu(rid,qx,title){
	var zNodes =[];
	reloadQXModal(rid,qx,title);
	$.ajax({
		type:"POST",
		url:_ctx+"/admin/role/qx",
		cache:false,
		data:{time:new Date().getTime(),qx:qx,roleId:rid},
		dataType:"json",
		success:function(data){
			if(data.status == '200'){
				var rultData = data.data;
				for(var i=0;i<rultData.length;i++){
					var childNode = [];
					var obj = rultData[i];
					zNodes.push(new menuTree(obj.menuId,obj.parentId,obj.menuName,obj.hasMenu));
					if(obj.subMenu.length > 0){
						for(var j = 0;j<obj.subMenu.length;j++){
							var subm = obj.subMenu[j];
							zNodes.push(new menuTree(subm.menuId,subm.parentId,subm.menuName,subm.hasMenu));
						}
					}
				}
				//初始化 数
				$.fn.zTree.init($("#roleTree"), setting,zNodes);
			}else{
				alert(data.message);
			}
		}
	});
	$("#qxModal").modal("show");
}

//删除按钮
function delRole(role_id){
	if(confirm("你确定要删除吗？如果删除，拥有该角色的用户将失去此角色的权限。")){
		$.ajax({
			type:"GET",
			url:_ctx+"/admin/role/del/"+role_id,
			cache:false,
			dataType:"json",
			data:{time:new Date().getTime()},
			success:function(data){
				if(data.status == '200'){
					window.location.href=window.location.href;
				}else{
					alert(data.message);
				}
			}
		});
	}
}

//编辑角色信息
function editRole(roleId,role_name,role_desc){
	reloadRoleModal(roleId,_ctx+"/admin/role/edit",role_name,role_desc,"编辑角色","更改");
	$("#roleModal").modal("show");
}

//修改模态框的数据
function reloadQXModal(roleId,qx,title){
	$("input[name='qx']").val(qx);
	$("input[name='role_id']").val(roleId);
	$("#modelHead").text(title);
}
function reloadRoleModal(roleId,url,role_name,role_desc,title,btnText){
	$("input[name='url']").val(url);
	$("input[name='role_id']").val(roleId);
	$("input[name='role_name']").val(role_name);
	$("textarea[name='role_desc']").val(role_desc);
	$("#rolemodelHead").text(title);
	$("#submit-roleBtn").text(btnText);
}

//更改权限
function updateRole(rid,ids,qx){
	$.ajax({
		type:"POST",
		url:_ctx+"/admin/role/edit",
		cache:false,
		dataType:"json",
		data:{roleId:rid,ids:ids,qx:qx},
		success:function(data){
			if(data.status == '200'){
				$("#qxModal").modal("hide");
			}else{
				alert(data.message);
			}
		}
	});
}

$(function(){
	 //提示框
    $("[data-toggle='tooltip']").tooltip();
	//表格分页
    $('#roleList').DataTable({
  		'paging'      : true,
  	    'lengthChange': true,
  	    'searching'   : true,
  	    'ordering'    : true,
  	    'info'        : true,
  	    'autoWidth'   : false,
  	  	"scrollX"	  : true,
  	  	"pagingType"  : "full_numbers",
  	  	"pageLength"  : 10
    });
	
	//点击模态框更改按钮
	$("#submit-qxBtn").click(function(){
		var treeObj = $.fn.zTree.getZTreeObj("roleTree");
		var nodes = treeObj.getCheckedNodes(true);
		var ids = "";
		for(var i=0;i<nodes.length;i++){
			var node = nodes[i];
			if(i != nodes.length -1){
				ids =ids+node.id+",";
			}else{
				ids = ids +node.id;
			}
		}
		var roleId = $("input[name='role_id']").val();
		var qx = $("input[name='qx']").val();
		updateRole(roleId,ids,qx)
	});
	
	$("#addRole").click(function(){
		reloadRoleModal("",_ctx+"/admin/role/add","","","新增角色","新增");
		$("#roleModal").modal("show");
	});
	
	//角色模态框的提交按钮
	$("#submit-roleBtn").click(function(){
		var url = $("input[name='url']").val();
		var roleId = $("input[name='role_id']").val();
		var roleName = $("input[name='role_name']").val();
		//回车或换行再次编辑的时候出现问题，怀疑是editRole方法的单引号问题。双引号转义老失败,
		var roleDesc = $("textarea[name='role_desc']").val().replace(/\n/g,"、").replace(/\s/g,"、");
		$.ajax({
			type:"POST",
			cache:false,
			url:url,
			dataType:"json",
			data:{roleId:roleId,roleName:roleName,roleDesc:roleDesc,time:new Date().getTime()},
			success:function(data){
				if(data.status == '200'){
					window.location.href=window.location.href;
					$("#roloModal").modal("hide");
				}else{
					alert(data.message);
				}
			}
		});
	});
})
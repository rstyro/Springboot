<#macro gemTH  name>
	<@compress single_line=true>
		${r"${item."}${name} }
	</@compress> 
</#macro>

<#macro notTran  name>
	<@compress single_line=true>
		${r"${"}${name} }
	</@compress> 
</#macro>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head th:include="include/css-head :: css-head">
</head>
<style>
    textarea{
        width:100%;
        height:100px;
    }
</style>
<body>
<!-- Content Header (Page header) -->
<section class="content-header">
    <h1>
        <small></small>
    </h1>
    <ol class="breadcrumb">
        <li><a href="#"><i class="fa fa-dashboard"></i> 系统管理</a></li>
        <li class="active">列表管理</li>
    </ol>
</section>

<!-- Main content -->
<section class="content">
    <div class="box">
        <div class="box-header">
            <h3 class="box-title">列表页</h3>
            <br><br>
            <div class="search">
                <div class="input-group" style="float: right;width: 300px;">
                    <input type="text" class="form-control" id="keyword" th:value="<@notTran name="keyword" />" placeholder="请输入关键字">
                    <span class="input-group-addon searcher" style="cursor:pointer;"><i class="fa fa-search"></i> 搜素</span>
                </div>
            </div>
            <button th:onclick="add();" th:if="<@notTran name="QX.add == '1' && QX.query == '1'" />" class="btn btn-success btn-sm" ><i class="fa fa-plus"></i> &nbsp;&nbsp;新增</button>
        </div>
        <!-- /.box-header -->
        <div class="box-body">
            <table id="roleList" class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th>序号</th>
	                <#list table.fields as column>
                        <#if column.comment == ''>
                            <th>${column.propertyName}</th>
                        <#else>
                          <th>${column.comment}</th>
                        </#if>
                    </#list>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr th:if="<@notTran name="QX.query == '1'" />" th:each="item,stat :<@notTran name="list.data.records" />" >
                    <td th:text="${r'${stat.count}'}">1</td>
                  <#list table.fields as column>
                      <#if column.propertyType == 'LocalDateTime'>
                        <td><div th:text="<@notTran name="#temporals.format(item.${column.propertyName}, 'yyyy-MM-dd HH:mm:ss')"/>" >${column.comment}</div></td>
                      <#else>
                          <td th:text="<@gemTH name=column.propertyName />">${column.comment}</td>
                      </#if>
                  </#list>
                    <td>
                        <span th:unless="<@notTran name="QX.add == '1' || QX.del == '1' || QX.edit == '1' || QX.query == '1'"/>" ><i class="fa fa-lock"></i> 无权限</span>
                        <span data-toggle="tooltip" data-placement="left" title="修改" th:if="<@notTran name="QX.edit == '1'"/>"  th:data-id="<@gemTH name='id'/>" class="btn btn-xs btn-info" th:onclick="edit(this.getAttribute('data-id'));"><i class="fa fa-edit"></i></span>
                        <span data-toggle="tooltip" data-placement="right" title="删除" th:if="<@notTran name="QX.del == '1'"/>" class="btn btn-xs btn-danger"  th:data-id="<@gemTH name='id'/>" th:onclick="del(this.getAttribute('data-id'));"><i class="fa fa-trash-o"></i></span>
                    </td>
                </tr>
                <tr th:unless="<@notTran name="QX.query == '1'" />" >
                    <td colspan="${table.fields?size+2}" rowspan="1" align="center">
                        <h2>您无权限查看该页面</h2>
                    </td>
                </tr>
                </tbody>
            </table>

        </div>
    </div>
    <div style="max-width:800px;margin:0 auto;">
        <div id="kkpager"></div>
    </div>
</section>

<div class="modal fade" id="itemModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="itemModelHead">添加</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <#list table.fields as column>
                        <#if column.keyFlag >
                         <input type="hidden" name="${column.propertyName}" id="${column.propertyName}" value="0"/>
                        </#if>
                    </#list>
                    <input type="hidden" name="actionurl" id="actionurl" value="/"/>
                    <#list table.fields as column>
                        <#if column.propertyName != 'isDeleted' && !column.keyFlag>
                            <div class="form-group">
                                <label for="${column.propertyName}" class="col-sm-2 control-label"> <#if column.comment == ''>${column.propertyName}<#else>${column.comment}</#if></label>
                                <div class="col-sm-10">
                                    <input type="text" class="form-control" name="${column.propertyName}" value="" id="${column.propertyName}" placeholder="请输入<#if column.comment == ''>${column.propertyName}<#else>${column.comment}</#if>">
                                </div>
                            </div>
                        </#if>
                    </#list>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-success" id="submitBtn" >添加</button>
            </div>
        </div>
    </div>
</div>


<script th:src="@{/static/bower_components/jquery/dist/jquery.min.js}"></script>
<script th:src="@{/static/bower_components/bootstrap/dist/js/bootstrap.min.js}"></script>
<!-- AdminLTE App -->
<script th:src="@{/static/dist/js/adminlte.min.js}"></script>
<script th:src="@{/static/js/common.js}"></script>
<script th:src="@{/static/page/src/kkpager.js}"></script>
<script th:inline="javascript">
    //init
    $(function(){
        var pageNo = getParameter('pageNo');
        if(!pageNo){
            pageNo = 1;
        }
        //生成分页
        kkpager.generPageHtml({
            pno : [[<@notTran name="list.data.current"></@notTran>]],
            //总页码
            total : [[<@notTran name="list.data.pages"></@notTran>]],
            //总数据条数
            totalRecords : [[<@notTran name="list.data.total"></@notTran>]],
            mode : 'click',//默认值是link，可选link或者click
            click : function(n){
                //手动选中按钮
                this.selectPage(n);
                skipPage(n);
                return false;
            }
        });

        //提交按钮
        $("#submitBtn").click(function(){
            var url = $("input[name='actionurl']").val();
            <#list table.fields as column>
            var ${column.propertyName} = $("#${column.propertyName}").val();
            </#list>
            if(checkData(<#list table.fields as column><#if !column.keyFlag>${column.propertyName}<#if column_has_next>,</#if></#if></#list>)){
                $.ajax({
                    type:"POST",
                    url:_ctx+url,
                    cache:false,
                    data:{<#list table.fields as column>${column.propertyName}:${column.propertyName},</#list> time:new Date().getTime()},
                    dataType:"json",
                     success:function(data){
                    if(data.status == "200"){
                        window.location.href=window.location.href;
                    }else{
                        alert(data.message);
                    }
                }
            });
                $("#itemModal").modal("hide");
            }

        });
    });
    var listUrl = "/${package.ModuleName}/${table.entityPath}/list";
    var editUrl = "/${package.ModuleName}/${table.entityPath}/edit";
    var addUrl = "/${package.ModuleName}/${table.entityPath}/add";
    var delUrl = "/${package.ModuleName}/${table.entityPath}/del";
    var queryUrl = "/${package.ModuleName}/${table.entityPath}/query";

    //跳转页面
    function  skipPage(pageNo) {
        var keyword = $("#keyword").val();
        window.location.href=_ctx+listUrl+"?keyword="+keyword+"&pageNo="+pageNo;
    }

    /**
     * 新增
     * */
    function add(){
        $("input[name='actionurl']").val(addUrl);
        $("#modelHead").text("新增");
        $("#submitBtn").text("新增");
        setData(<#list table.fields as column>""<#if column_has_next>,</#if></#list>);
        $("#itemModal").modal("show");
    }
    /**
     * 编辑
     *
     * */
    function edit(id){
        $("#modelHead").text("编辑");
        $("#submitBtn").text("编辑");
        $("input[name='actionurl']").val(editUrl);
        $.get(_ctx+queryUrl,{t:new Date().getTime(),id:id},function(data){
            console.log("data:",data);
            if(data.status == "200"){
                var item = data.data;
                console.log("item:",item);
                setData(<#list table.fields as column> item.${column.propertyName} <#if column_has_next>,</#if></#list>)
            }else{
                alert(data.message);
            }
        })
        $("#itemModal").modal("show");
    }

    /**
     * 删除
     * */
    function del(id){
        if(confirm("您确定要删除这条记录吗？"))
        $.post(_ctx+delUrl,{t:new Date().getTime(),id:id},function(data){
            if(data.status == "200"){
                window.location.href=window.location.href;
            }else{
                alert(data.message);
            }
        });
    }

    /**
     * 校验参数
     * @returns {boolean}
     */
    function checkData(<#list table.fields as column><#if !column.keyFlag>${column.propertyName}<#if column_has_next>,</#if></#if></#list>){
        <#list table.fields as column>
            <#if !column.keyFlag && column.propertyType != 'LocalDateTime' && column.propertyName != 'isDeleted'>
            if(${column.propertyName} == ""){
                alert("<#if column.comment == ''>${column.propertyName}<#else>${column.comment}</#if>不能为空");
                $("input[name='${column.propertyName}']").focus();
                return false;
             }
             </#if>
        </#list>

        return true;
    }

    /**
     *  赋值
      */
    function setData( <#list table.fields as column> ${column.propertyName} <#if column_has_next>,</#if></#list>){
        <#list table.fields as column>
         $("#${column.propertyName}").val(${column.propertyName});
        </#list>
    }
</script>
</body>
</html>

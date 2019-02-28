var _ctx = $("meta[name='_ctx']").attr("content");
_ctx = _ctx.substr(0, _ctx.length - 1);

/* 获取地址参数*/
function getParameter(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r!=null) return unescape(r[2]); return null;
}

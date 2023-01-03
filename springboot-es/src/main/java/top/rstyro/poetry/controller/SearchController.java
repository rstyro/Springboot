package top.rstyro.poetry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.rstyro.poetry.commons.R;
import top.rstyro.poetry.dto.SearchDto;
import top.rstyro.poetry.es.vo.EsSearchResultVo;
import top.rstyro.poetry.service.IPoetryService;
import top.rstyro.poetry.vo.SearchVo;
import javax.validation.Valid;

/**
 * 检索相关
 *
 * @author rstyro
 */
@Validated
@RestController
@RequestMapping("/search")
public class SearchController {

    private IPoetryService poetryService;

    @Autowired
    public void setPoetryService(IPoetryService poetryService) {
        this.poetryService = poetryService;
    }

    /**
     * 搜索
     */
    @PostMapping("/list")
    public R<EsSearchResultVo<SearchVo>> list(@RequestBody @Valid SearchDto dto){
        return R.success(poetryService.getList(dto));
    }

    /**
     * 详情
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") String id){
        return R.success(poetryService.getDetail(id));
    }

    /**
     * 飞花令
     */
    @GetMapping("/flyFlower")
    public R getFlyFlower(String text){
        return R.success(poetryService.getFlyFlower(text));
    }

}

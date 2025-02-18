package top.rstyro.poetry.service;

import top.rstyro.poetry.dto.SearchDto;
import top.rstyro.poetry.es.vo.EsSearchResultVo;
import top.rstyro.poetry.vo.FlyFlowerVo;
import top.rstyro.poetry.vo.SearchDetailVo;
import top.rstyro.poetry.vo.SearchVo;
import top.rstyro.poetry.vo.SuggestVo;

import java.util.List;

public interface IPoetryService {
    public EsSearchResultVo<SearchVo> getList(SearchDto dto);

    public SearchDetailVo getDetail(String id);
    public EsSearchResultVo<FlyFlowerVo> getFlyFlower(String text);
}

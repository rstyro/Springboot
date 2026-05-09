package top.lrshuai.playwright.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.lrshuai.playwright.base.R;
import top.lrshuai.playwright.controller.dto.TestLoginDto;
import top.lrshuai.playwright.service.ITestService;

@Slf4j
@RestController
@RequestMapping("/playwright")
@RequiredArgsConstructor
public class TestController {

    private final ITestService testService;

    /**
     * 举例
     */
    @GetMapping("/example")
    public R example(){
        return R.ok(testService.example());
    }

    /**
     * 测试动态脚本
     * @param dto 参数
     * @return 结果
     */
    @PostMapping("/testGroovyLogin")
    public R testGroovyLogin(@RequestBody TestLoginDto dto){
        return R.ok(testService.testGroovyLogin(dto));
    }

}

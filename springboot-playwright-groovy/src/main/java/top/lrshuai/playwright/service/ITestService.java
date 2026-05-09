package top.lrshuai.playwright.service;

import top.lrshuai.playwright.controller.dto.TestLoginDto;

public interface ITestService {

    Object example();

    boolean testGroovyLogin(TestLoginDto dto);
}

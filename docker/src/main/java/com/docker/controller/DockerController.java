package com.docker.controller;

import com.docker.common.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class DockerController {

    @GetMapping("docker")
    public Result<String> helloDocker() {
        log.info("hello docker!!");
        return Result.success("hello docker!!");
    }
}

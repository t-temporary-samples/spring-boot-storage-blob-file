package com.samples.springboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
@RestController
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @RequestMapping("/")
    public @ResponseBody String root() {
        return "<body>" +
                "<p><a href=\"write\">/write?content=***</a>: Write *** to file</p>" +
                "<p><a href=\"read\">/read</a>: Read file content</p>" +
                "</body>";
    }

    @Value("azure-file://test/test.txt")
    private Resource file;

    @RequestMapping("/read")
    public @ResponseBody String read() throws IOException {
        return StreamUtils.copyToString(file.getInputStream(), StandardCharsets.UTF_8);
    }

    @RequestMapping("/write")
    public @ResponseBody String write(@RequestParam String content) throws IOException {
        try (OutputStream os = ((WritableResource) file).getOutputStream()) {
            os.write(content.getBytes(StandardCharsets.UTF_8));
        }
        return "Written: " + content;
    }
}
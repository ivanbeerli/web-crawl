package com.challenge.crawler.components;

import com.challenge.crawler.dtos.Page;
import com.challenge.crawler.dtos.Root;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ResourceComponent {

    @Value("classpath:static/internet.json")
    private Resource resource;


    public List<Page> getPages(){
        String text = null;
        try {
            text = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Root root = JsonbBuilder.create().fromJson(text, Root.class);

        return root.getPages();

    }
}

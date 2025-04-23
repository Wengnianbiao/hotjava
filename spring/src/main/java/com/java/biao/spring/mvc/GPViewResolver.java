package com.java.biao.spring.mvc;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.Locale;

@Data
@NoArgsConstructor
public class GPViewResolver {

    private final String DEFAULT_TEMPLATE_SUFFIX = ".html";

    private File templateRootDir;
    private String viewName;

    public GPViewResolver(String templateRoot) {
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.templateRootDir = new File(templateRootPath);
    }

    // 视图本质上是文件流中的html文件
    public GPView resolveViewName(String viewName, Locale locale) throws Exception {
        this.viewName = viewName;
        if (viewName == null || viewName.trim().isEmpty()) {
            return null;
        }
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFIX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFIX);
        File templateFile = new File((templateRootDir.getPath() + "/" + viewName).replaceAll("/+", "/"));
        return new GPView(templateFile);
    }

    public String getViewName() {
        return viewName;
    }
}

package com.pokereco.pokereco.Security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ForceCredentialsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request,
      ServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {

    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // 強制的にレスポンスに付与
    httpResponse.setHeader(""
        + "", "true");

    // 必要に応じて Access-Control-Allow-Origin も設定
    // 例: https://localhost:3001 など
     httpResponse.setHeader("Access-Control-Allow-Origin", "https://localhost:3000");

    chain.doFilter(request, response);
  }
}

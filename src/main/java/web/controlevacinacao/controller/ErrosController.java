package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrosController implements ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrosController.class);

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String url = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI).toString();
        String retorno = "error";
        if (status != null) {
            HttpStatus httpStatus = HttpStatus.resolve((int) status);
            if (httpStatus == HttpStatus.NOT_FOUND) {
                logger.warn("A URL {} foi acessada mas não existe.", url);
                retorno = "error/404";
            } else if (httpStatus == HttpStatus.FORBIDDEN) {
                logger.warn("Tentaram acessar a URL {} sem permissão.", url);
                retorno = "error/403";
            } else if (httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
                logger.error("Ocorreu um erro interno no servidor.");
                retorno = "error/500";
            }
        }
        return request.getHeader("HX-Request") == null ? retorno : retorno +  " :: conteudo";
    }

}

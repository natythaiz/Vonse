package web.controlevacinacao.controller.advice;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

//Vai afetar todos os controladores
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(HttpServletRequest request, Exception exception) throws Exception {
        // ESQUEMA DO ID DO ERRO
        HttpStatusCode statusCode = (exception instanceof ErrorResponse) ? ((ErrorResponse) exception).getStatusCode()
                : HttpStatus.INTERNAL_SERVER_ERROR;
        logger.error("A requisição {} lançou uma {}, com a falha de código {}", request.getRequestURL(), exception,
                statusCode);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        logger.error("Stack trace da exceção: {}", sw.toString());
        if (request.getHeader("HX-Request") != null) {
            return new ModelAndView("error :: conteudo");
        }
        return new ModelAndView("error", statusCode);
    }

    // Qdo ocorre um 404 o Spring lanca uma excecao NoResourceFoundException
    // que seria capturada pelo nosso handleAllExceptions, e não é isso que queremos,
    // portanto empurramos para frente para deixar o ErrosController tratar.
    @ExceptionHandler(NoResourceFoundException.class)
    public String handleError404(HttpServletRequest request, Exception exception) throws Exception {
        throw exception;
    }

}

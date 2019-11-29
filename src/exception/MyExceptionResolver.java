package exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MyExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView mad = new ModelAndView();
        if (e instanceof ParamErrorException){
            mad.addObject("msg",e.toString());
        }else{
            mad.addObject("msg","未知异常"+e.toString());
        }

        mad.addObject("error");
        return mad;
    }
}

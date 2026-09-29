package hello.servlet.web.frontcontroller.v5.adapter;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v4.ControllerV4;
import hello.servlet.web.frontcontroller.v5.MyHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV4 controller = (ControllerV4) handler;

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();


        String viewName = controller.process(paramMap, model); // process() 리턴 -> modelView 반환

        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        // http://localhost:8080/member?name=주찬&age=28
        // 요청으로 들어온 파라미터들을 저장할 Map 생성
        // 예: name=주찬, age=28
        // => {"name"="주찬", "age"="28"}
        Map<String, String> paramMap = new HashMap<>();

        // request에 들어있는 모든 파라미터의 이름을 가져옴
        // 예: ["name", "age"]
        request.getParameterNames()

                // 파라미터 이름들을 하나씩 꺼낼 수 있도록 Iterator로 변환
                .asIterator()
                // 파라미터가 남아있는 동안 하나씩 반복
                .forEachRemaining(paramName ->  // paramName이라는 값을 하나 받아서 → 뒤에 있는 코드를 실행해라
                        // 파라미터 이름과 그 값을 Map에 저장
                        // paramName = "name"
                        // request.getParameter("name") = "주찬"
                        //
                        // 결과:
                        // {"name"="주찬"}
                        //
                        // 다음 반복:
                        // {"name"="주찬", "age"="28"}
                        paramMap.put(paramName, request.getParameter(paramName)));

        return paramMap;
    }
}

import domain.Ball;
import domain.Lotto;
import domain.LottoMachine;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class HelloWorld {

    public static void main(String[] args) {
        port(8080);
        staticFiles.location("/static");

        post("/buyLotto", ((request, response) -> {
            String[] manualNumbers = request.queryParams("manualNumber").split("\r\n");
            List<Lotto> lottos = new ArrayList<>();

            for (int i = 0; i < manualNumbers.length; i++) {
                Set<Ball> balls = Arrays.stream(manualNumbers[i].split(",")).map(Integer::parseInt).map(Ball::new).collect(Collectors.toSet());
                lottos.add(new Lotto(balls));
            }

            int count = Integer.parseInt(request.queryParams("inputMoney")) / 1000;
            for (int i = 0; i < count - manualNumbers.length; i++) {
                lottos.add(LottoMachine.createRandomLotto());
            }

            HashMap<String, Object> model = new HashMap<>();
            model.put("lottosSize", count);
            model.put("lottos", lottos);
            return render(model, "show.html");
        }));

    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}

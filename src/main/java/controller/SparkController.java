package controller;

import domain.*;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import view.OutputView;

import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class SparkController {

    private User user;
    private WinningLotto winningLotto;

    public void run() {
        port(8080);
        init();
        buyLotto();
        showResult();
    }

    private void init() {
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });
    }

    private void buyLotto() {
        post("/buyLotto", ((request, response) -> {
            String[] manualNumbers = request.queryParams("manualNumber").split("\r\n");
            List<Lotto> lottos = new ArrayList<>();

            for (int i = 0; i < manualNumbers.length; i++) {
                Set<Ball> balls = Arrays.stream(manualNumbers[i].split(","))
                        .map(Integer::parseInt)
                        .map(Ball::new)
                        .collect(Collectors.toSet());
                lottos.add(new Lotto(balls));
            }

            int inputMoney = Integer.parseInt(request.queryParams("inputMoney"));
            int count = inputMoney / 1000;
            for (int i = 0; i < count - manualNumbers.length; i++) {
                lottos.add(LottoMachine.createRandomLotto());
            }

            user = new User(inputMoney, lottos);

            Map<String, Object> model = new HashMap<>();
            model.put("lottosSize", count);
            model.put("lottos", lottos);
            return render(model, "show.html");
        }));
    }

    private void showResult() {
        post("/matchLotto", ((request, response) -> {
            String[] strings = request.queryParams("winningNumber").split(",");
            Set<Ball> balls = Arrays.stream(strings).map(Integer::parseInt).map(Ball::new).collect(Collectors.toSet());
            int bonusBallNumber = Integer.parseInt(request.queryParams("bonusNumber"));
            winningLotto = new WinningLotto(new Lotto(balls), new Ball(bonusBallNumber));
            user.matchWinningLotto(winningLotto);
            List<String> result = OutputView.getResult(user);
            Map<String, Object> model = new HashMap<>();
            model.put("lottosResult", new LottosResult(result, user.calculateRateOfReturn()));
            return render(model, "result.html");
        }));
    }

    public static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

}

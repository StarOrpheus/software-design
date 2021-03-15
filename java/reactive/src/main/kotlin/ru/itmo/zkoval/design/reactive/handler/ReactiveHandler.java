package ru.itmo.zkoval.design.reactive.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import ru.itmo.zkoval.design.reactive.entities.Product;
import ru.itmo.zkoval.design.reactive.entities.User;
import ru.itmo.zkoval.design.reactive.enums.Currency;
import ru.itmo.zkoval.design.reactive.repositories.ProductRepository;
import ru.itmo.zkoval.design.reactive.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static ru.itmo.zkoval.design.reactive.utils.UtilsKt.dummyErrorView;
import static ru.itmo.zkoval.design.reactive.utils.UtilsKt.dummyView;

@Component
public class ReactiveHandler {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReactiveHandler(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Mono<ServerResponse> home(ServerRequest request) {
        return request.session().flatMap(session -> {
            Map<String, Object> attributes = session.getAttributes();
            String id = (String) attributes.get("id");
            if (id != null) {
                return userRepository
                        .findById(id)
                        .flatMap(user -> {
                            Map<String, Object> mp = new HashMap<>();
                            mp.put("login", user.getLogin());
                            return dummyView("index", mp);
                        })
                        .switchIfEmpty(Mono.error(new IllegalStateException("No such user")));
            } else {
                return dummyView("index", new HashMap<>());
            }
        });
    }

    public Mono<ServerResponse> saveView(ServerRequest request) {
        return ok().render("saveProduct", Map.of("newProduct", new Product()));
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        return request.formData().flatMap(formData -> {
                    var product = formData.toSingleValueMap();
                    return request.session().flatMap(session -> {
                        var id = (String) Objects.requireNonNull(session.getAttribute("id"));
                        return userRepository.findById(id);
                    }).switchIfEmpty(
                            Mono.error(new IllegalStateException("unregistered users can not add new products"))
                    ).flatMap(user ->
                            productRepository.save(new Product(
                                    null, product.get("productName"), user.getId(), Double.parseDouble(product.get("price")), Currency.valueOf(product.get("currency"))
                            ))
                    ).flatMap(savedProduct ->
                            ok().render("products")
                    ).switchIfEmpty(Mono.error(new IllegalStateException("Can not save product")));
                }
        ).onErrorResume(
                e -> dummyErrorView(e.getMessage())
        );
    }

    public Mono<ServerResponse> listAll(ServerRequest request) {
        return request.session().flatMap(session -> {
                    var id = (String) Objects.requireNonNull(session.getAttribute("id"));
                    return userRepository.findById(id);
                }
        ).flatMap(user -> {
            var products = productRepository.findAll().map(product -> {
                        var preferredCurrency = Objects.requireNonNull(user.getPreferredCurrency());
                        var oldPrice = Objects.requireNonNull(product.getPrice());
                        var oldCurrency = Objects.requireNonNull(product.getCurrency());
                        var price = oldCurrency.convertToPreferred(oldPrice, preferredCurrency);
                        return new Product(null, product.getProductName(), null, price, preferredCurrency);
                    }
            ).collectList();

            return ok().render("products", Map.of("products", products, "login", Objects.requireNonNull(user.getLogin())));
        }).switchIfEmpty(
                Mono.error(new IllegalStateException("Only authorized users can view product list"))
        ).onErrorResume(
                e -> dummyErrorView(e.getMessage())
        );
    }

    public Mono<ServerResponse> registerView(ServerRequest request) {
        return ok().render("register", Map.of("newUser", new User()));
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.formData().flatMap(formData -> {
            var newUser = formData.toSingleValueMap();
            var name = newUser.get("name");
            var login = newUser.get("login");
            return userRepository.findByLogin(login).flatMap(u ->
                    Mono.error(new IllegalStateException("User with login" + u.getLogin() + " already exists"))
            ).switchIfEmpty(
                    userRepository.save(new User(null, login, name, Currency.valueOf(newUser.get("currency"))))
            ).flatMap(u -> ok().render("index"));
        }).onErrorResume(
                e -> dummyErrorView(e.getMessage())
        );
    }

    public Mono<ServerResponse> loginView(ServerRequest request) {
        return ok().render("login", Map.of("user", new User()));
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.formData().flatMap(formData -> {
            var userData = formData.toSingleValueMap();
            String login = Objects.requireNonNull(userData.get("login"));
            return userRepository.findByLogin(login)
                    .switchIfEmpty(
                            Mono.error(new IllegalStateException("Invalid login!"))
                    ).flatMap(user -> {
                        return request.session().flatMap(session -> {
                            session.getAttributes().put("id", user.getId());
                            return ok().render("index", Map.of("login", Objects.requireNonNull(user.getLogin())));
                        });
                    });
        }).onErrorResume(e -> dummyErrorView(e.getMessage()));
    }

    public Mono<ServerResponse> logout(ServerRequest request) {
        return request.session().flatMap(session -> {
                    clearSession(session);
                    return ok().render("index");
                }
        );
    }

    private void clearSession(WebSession session) {
        session.getAttributes().remove("id");
    }
}

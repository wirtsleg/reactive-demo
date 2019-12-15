package com.example.reactivedemo;

import com.example.reactivedemo.domain.Post;
import com.example.reactivedemo.domain.User;
import com.example.reactivedemo.domain.UserPost;
import com.example.reactivedemo.repository.PostRepository;
import io.r2dbc.postgresql.api.Notification;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class ReactiveDemoApplication {
    private final WebClient dataClient;
    private final PostRepository postRepository;
    private final PostgresqlConnection postgresqlConnection;

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(ReactiveDemoApplication.class, args);
    }

    @GetMapping(value = "/post-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CharSequence> getStream() {
        return postgresqlConnection.getNotifications().map(Notification::getParameter);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runScheduler() {
        Flux.interval(Duration.ofMillis(1000), Schedulers.newSingle("123"))
                .flatMap(i -> getUsers())
                .map(users -> users.stream().collect(Collectors.toMap(User::getId, u -> u)))
                .flatMap(usersMap -> {
                    return Flux.from(getPosts())
                            .map(userPost -> {
                                User user = usersMap.get(userPost.getUserId());

                                return Post.builder()
                                        .id(userPost.getId())
                                        .userId(user.getId())
                                        .organizationId(user.getOrganization().getId())
                                        .subject(userPost.getSubject())
                                        .body(userPost.getBody())
                                        .build();
                            });
                })
                .flatMap(postRepository::save)
                .log()
                .subscribe();
    }

    private Mono<List<User>> getUsers() {
        return dataClient.get()
                .uri("/users")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    private Flux<UserPost> getPosts() {
        return dataClient.get()
                .uri("/posts")
                .retrieve()
                .bodyToFlux(UserPost.class);
    }
}

package com.example.reactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toList;

@SpringBootApplication
public class ReactorApplication {

	public static void main(String[] args) {

		List<Integer> elements = new ArrayList<>();

		//Flux<Integer> just = Flux.just(1, 2, 3, 4);
		//Mono<Integer> just2 = Mono.just(1);

		Flux.just(1 , 2, 3, 4)
				.log()
				.subscribe();

        //java 8 streams
		List<Integer> collected = Stream.of(1, 2, 3, 4)
				.collect(toList());

		Flux.just(1, 2, 3, 4)
				.log()
				.map(i -> i * 2)
				.subscribe(elements::add);

		//flujo caliente
		ConnectableFlux<Object> publish = Flux.create(fluxSink -> {
					while(true) {
						fluxSink.next(System.currentTimeMillis());
					}
				})
				.publish();
		publish.subscribe(System.out::println);
		/*publish.connect();*/


		//flujo caliente envia datos cada dos segundos
		ConnectableFlux<Object> publish2 = Flux.create(fluxSink -> {
					while(true) {
						fluxSink.next(System.currentTimeMillis());
					}
				})
				.sample(ofSeconds(2))
				.publish();
		publish2.subscribe(System.out::println);
		//publish2.connect();

		//suscribirse a un hilo diferente al principal
		Flux.just(1, 2, 3, 4)
				.log()
				.map(i -> i * 2)
				.subscribeOn(Schedulers.parallel())
				.subscribe(elements::add);




		//SpringApplication.run(ReactorApplication.class, args);
	}

}

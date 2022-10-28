package site.metacoding.white.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

// @Component
// public class BeanInit extends ObjectMapper{
// // 상속함으로서 이 클래스는 이제부터 objectMapper가 됨
// // om구현체 => 이런 이름으로 클래스를 만들어야 됨
// // component로 ioc에 띄어줌

// // 필요한 거 전부 다 띄우는 방법
// }

// @Configuration
// public class BeanInit {

// // 컨피그레이션 어노테이션
// // 일단 이 클래스를 ioc에 new하고
// // 클래스 내부를 런타임시 리플렉션해서 x 리플랙션이 아니라 그냥 내부를 스캔해서 !
// // bean이 있으면 ioc에 띄워줌 => 컨피그래이션 특

// // 이런식으로 ioc에 등록을 해줌
// // 내가 만든 클래스가 아닌 경우 이런 식으로 ioc에 등록함
// // 내가 만들었으면 그냥 어노테이션 붙이면 됨

// @Bean
// public ObjectMapper omInit(){
// return new ObjectMapper();
// }

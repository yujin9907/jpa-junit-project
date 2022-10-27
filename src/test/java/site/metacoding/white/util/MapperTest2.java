package site.metacoding.white.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
class Dog {
    private Integer id;
    private String name;
}

@NoArgsConstructor
@Setter
@Getter
class DogDto {
    private Integer id;
    private String name;

    public DogDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "DogDto [id=" + id + ", name=" + name + "]";
    }

}

public class MapperTest2 {

    @Test
    public void convert_test() {
        List<Dog> dogList = new ArrayList<>();
        dogList.add(new Dog(1, "강아지"));
        dogList.add(new Dog(2, "고양이"));

        // List<Dog> -> List<DogDto>
        List<DogDto> dogDtoList = new ArrayList<>();
        for (Dog dog : dogList) {
            DogDto dogDto = new DogDto();
            dogDto.setId(dog.getId());
            dogDto.setName(dog.getName());
            dogDtoList.add(dogDto);
        }

        System.out.println(dogDtoList);
    }

    @Test
    public void convert_test2() {
        List<Dog> dogList = new ArrayList<>();
        dogList.add(new Dog(1, "강아지"));
        dogList.add(new Dog(2, "고양이"));

        // List<Dog> -> List<DogDto> : 포문로직 대신 스트림
        List<DogDto> dogDtoList = dogList.stream() // 최상위의 타입을 벗겨냄 = 꺼내기, 스트림(파이프같은것)안에 리스트 안에 있는 것들이 들어옴(컬렉션이 아니라 스트림임)
                .map((dog) -> new DogDto(dog.getId(), dog.getName())) // .foreach도 가능 : map = 변환해서 리턴, foreach = 먼가 하겟다
                                                                      // 리턴없이?, fillter = 스트림 안 필터 if 같은 느낌
                .collect(Collectors.toList()); // 다시 스트림을 컬렉션함

        System.out.println(dogDtoList);
    }
}
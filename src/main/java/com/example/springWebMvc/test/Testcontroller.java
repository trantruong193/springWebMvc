package com.example.springWebMvc.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/test")
public class Testcontroller {
    @Autowired
    ProRepo proRepo;
    @Autowired
    Type1Repo type1Repo;
    @Autowired
    Type2Repo type2Repo;
    @GetMapping("add")
    private String add(){
        return "test/add";
    }
    @GetMapping("save")
    private String save(
                        @RequestParam("proId") int id,
                        @RequestParam("proName") String name,
                        @RequestParam("type1") String type1,
                        @RequestParam("type2") String type2,
                        @RequestParam("type1Name") String type1Name,
                        @RequestParam("type2Name") String type2Name){
        TestProduct testProduct = TestProduct.builder()
                .id((long) id).name(name).type1Name(type1).type2Name(type2)
                .build();
        proRepo.save(testProduct);
        List<String> type1N = List.of(type1Name.trim().replace(" ","").split(","));
        type1N.forEach(s -> {
            new TestType1();
            TestType1 testType1;
            testType1 = TestType1.builder()
                    .proId((long) id).name(s)
                    .build();
            type1Repo.save(testType1);
        });
        List<String> type2N = List.of(type2Name.trim().replace(" ","").split(","));
        type2N.forEach(s -> {
            TestType2 testType2;
            testType2 = TestType2.builder()
                    .proId((long) id).name(s)
                    .build();
            type2Repo.save(testType2);
        });
        return "test/add";
    }
}

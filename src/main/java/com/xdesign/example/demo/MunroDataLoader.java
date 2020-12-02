package com.xdesign.example.demo;

import com.opencsv.bean.CsvToBeanBuilder;
import com.xdesign.example.demo.entities.Summit;
import com.xdesign.example.demo.repositories.SummitRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MunroDataLoader {
    @Autowired
    private SummitRepository summitRepository;

    public void loadData() throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("munrotab_v6.2-stripped.csv").getInputStream()));

        List<Summit> summits = new CsvToBeanBuilder<Summit>(reader)
                .withType(Summit.class)
                .build().parse().stream()
                .filter(s -> Strings.isNotBlank(s.getCategory()))
                .collect(Collectors.toList());

        summitRepository.saveAll(summits);
    }
}

package com.zaitsev.bankapi.config;

import com.zaitsev.bankapi.entity.Category;
import com.zaitsev.bankapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {

        createIfNotExists("Еда");
        createIfNotExists("Развлечения");
        createIfNotExists("Зарплата");
        createIfNotExists("Инвестиции");
        createIfNotExists("Переводы");
        createIfNotExists("Прочее");

        log.info("Категории успешно инициализированы");
    }

    private void createIfNotExists(String name){

        if(!categoryRepository.existsByNameIgnoreCase(name)){

            categoryRepository.save(
                    Category.builder()
                            .name(name)
                            .build()
            );
        }
    }
}
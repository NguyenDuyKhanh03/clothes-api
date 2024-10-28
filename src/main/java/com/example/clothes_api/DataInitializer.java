package com.example.clothes_api;

import com.example.clothes_api.entity.Category;
import com.example.clothes_api.entity.Color;
import com.example.clothes_api.entity.Role;
import com.example.clothes_api.repository.CategoryRepository;
import com.example.clothes_api.repository.ColorRepository;
import com.example.clothes_api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;

    @Override
    public void run(String... args) throws Exception {
        if(roleRepository.count()==0){
            Role role1 = new Role();
            role1.setName("ROLE_USER");
            roleRepository.save(role1);

            Role role2 = new Role();
            role2.setName("ROLE_ADMIN");
            roleRepository.save(role2);
        }

        if(categoryRepository.count()==0){
            Category category1 = new Category();
            category1.setName("Áo sơ mi nam");
            categoryRepository.save(category1);
        }

        if(colorRepository.count()==0){
            Color color1 = new Color();
            color1.setName("Đen");
            colorRepository.save(color1);

            Color color2 = new Color();
            color2.setName("Trắng");

            colorRepository.save(color2);

        }
    }
}

package org.web.projekat.security;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.web.projekat.models.Role;
import org.web.projekat.models.User;
import org.web.projekat.repositories.UserRepository;

@Component
public class AdminUserInitializer  implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... arg) throws Exception{
        //provjeriti da li postoji neki admin

        if(userRepository.findByUsername("admin").isEmpty()){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@gmail.com");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user je kreiran sa sljedecim pristupnim podacima: 'admin' and password: 'admin123'.");
        }else{
            System.out.println("Admin user vec postoji.");
        }
    }
}

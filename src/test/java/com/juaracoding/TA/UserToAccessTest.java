package com.juaracoding.TA;


import com.juaracoding.TA.repo.AuthorizeAccessModulRepo;
import com.juaracoding.TA.repo.UserToAccessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserToAccessTest {

    @Autowired private UserToAccessRepo userToAccessRepo;
    @Autowired private AuthorizeAccessModulRepo authorizeAccessModulRepo;

//    @Test
//    public void saveUser()
//    {
//        List<AuthorizeAccessModul> authorizeAccessModulList = authorizeAccessModulRepo.findByAccessId(1);
//        Assertions.assertThat(authorizeAccessModulList).isNotNull();
//    }


}

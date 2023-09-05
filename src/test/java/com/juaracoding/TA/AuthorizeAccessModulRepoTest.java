package com.juaracoding.TA;

import com.juaracoding.TA.model.AuthorizeAccessModul;
import com.juaracoding.TA.repo.AccessRepo;
import com.juaracoding.TA.repo.AuthorizeAccessModulRepo;
import com.juaracoding.TA.repo.MenuRepo;
import com.juaracoding.TA.repo.ModulRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AuthorizeAccessModulRepoTest {

    @Autowired private AuthorizeAccessModulRepo authorizeAccessModulRepo;
    @Autowired private AccessRepo accessRepo;
    @Autowired private MenuRepo menuRepo;
    @Autowired
    private ModulRepo modulRepo;

    @Test
    public void testSaveNew() {
//        for (int i = 1; i <= 10; i++)
//        {
//            Access access = new Access();
//            access.setAccessName("accessTest-"+i);
//            access.setCreatedBy(1);
//            access.setCreatedDate(new Date());
//            accessRepo.save(access);
//
//
//            Modul modul = new Modul();
//            modul.setModulName("modulTest-"+i);
//            modul.setCreatedBy(1);
//            modul.setCreatedDate(new Date());
//            modulRepo.save(modul);
//        }

//        for (Integer i=1;i<=10;i++)
//        {
//            Access access = new Access();
//            access.setAccessId(3L);
//
//
//            Modul modul = new Modul();
//            modul.setModulId(i.longValue());
//
//            AuthorizeAccessModulKeys id = new AuthorizeAccessModulKeys();
//            id.setAccessId(access.getAccessId());
//            id.setModulId(modul.getModulId());
//
//            AuthorizeAccessModul accessModul = new AuthorizeAccessModul();
//            accessModul.setId(id);
//            accessModul.setAccess(access);
//            accessModul.setModul(modul);
//            accessModul.setCanAdd((byte)1);
//            accessModul.setCanEdit((byte)1);
//            accessModul.setCanDelete((byte)1);
//            accessModul.setCanPrint((byte)1);
//            accessModul.setCreatedBy(1);
//            accessModul.setCreatedDate(new Date());
//
//            AuthorizeAccessModul saveAccessModul = authorizeAccessModulRepo.save(accessModul);
//            Assertions.assertThat(saveAccessModul).isNotNull();
//        }

    }

//    @Test
//    public void testListAll() {
//        List<AuthorizeAccessModul> orderDetails = authorizeAccessModulRepo.findAll();
//
//        assertThat(orderDetails).isNotEmpty();
//        orderDetails.forEach(System.out::println);
//    }

    @Test
    public void testFindBy() {
        List<AuthorizeAccessModul> authorizeAccessModulList = authorizeAccessModulRepo.findByAccessAccessId(1L);
        assertThat(authorizeAccessModulList).isNotEmpty();
        authorizeAccessModulList.forEach(System.out::println);
    }

//    @Test
//    public void delete()
//    {
//        Access access = new Access();
//        Modul modul = new Modul();
//
//        access.setAccessId(1L);
//        modul.setModulId(1L);
//        AuthorizeAccessModulKeys id = new AuthorizeAccessModulKeys();
//        id.setAccessId(access.getAccessId());
//        id.setModulId(modul.getModulId());
//        AuthorizeAccessModul accessModul = new AuthorizeAccessModul();
//        accessModul.setId(id);
//        authorizeAccessModulRepo.deleteById(id);
//    }
}
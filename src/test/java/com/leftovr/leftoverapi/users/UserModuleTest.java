package com.leftovr.leftoverapi.users;

import com.leftovr.leftoverapi.LeftoverapiApplication;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.test.ApplicationModuleTest;

@ApplicationModuleTest
public class UserModuleTest {

    @Test
    void moduleDependenciesAreValid() {
       var modules = ApplicationModules.of(LeftoverapiApplication.class);

         modules.verify();
    }
}

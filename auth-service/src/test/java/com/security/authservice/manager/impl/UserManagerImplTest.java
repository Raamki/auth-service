package com.security.authservice.manager.impl;

import com.security.authservice.dto.response.UserResponse;
import com.security.authservice.entity.UserDetail;
import com.security.authservice.manager.UserManager;
import com.security.authservice.repository.UserDetailRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserManagerImplTest {
    @Mock
    private UserDetailRepository userDetailRepository;

    @Autowired
    private UserManagerImpl userManager;

    @Test
    public void getUserTest() {
        UserDetail user = new UserDetail("id", "raam@abc.com", "Raam", "test", true, "", null, "", null, null);
        when(userDetailRepository.findTopByUsernameIgnoreCase(Mockito.anyString())).thenReturn(user);
        UserResponse result = userManager.getUser("admin");

        assertEquals("id", result.getUserId());
    }

}

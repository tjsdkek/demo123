package kroryi.demo.Service;


import kroryi.demo.domain.User;
import kroryi.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow( ()->
                    new RuntimeException("찾는 사용자가 없습니다.")
                );
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());
        existingUser.setAddress(user.getAddress());
        existingUser.setPhone(user.getPassword());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("사용자가 존재 하지 않습니다."));

        userRepository.delete(existingUser);
    }

//    public List<User> findByName(String name){
//
//        return userRepository.findByUsername(name);
//
//    }

    public void saveDummyUser() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            User user = User.builder()
                    .username("사용자" + i)
                    .email("user" + i + "@yi.or.kr")
                    .password("11111")
                    .address("대구광역시 내당 " + (i % 10) + "동")
                    .phone("0101111000" + i)
                    .build();
            User result = userRepository.save(user);
            log.info("UserName: {}", result.getUsername());

        });
    }

    public List<User> findByUsernameOrEmail(String username, String email){

        return userRepository.findByUsernameOrEmailOrderByIdDesc(username, email);
    }

    public List<User> findByUsernameLikeOrderByIdDesc(
            String username,
            Pageable pageable){

        return userRepository.findByUsernameLikeOrderByIdDesc(username, pageable);
    }


    @Transactional
    public void saveUsers(List<User> users) {

        userRepository.saveAll(users);
//        for(User user : users) {
//            userRepository.save(user);
//        }
    }

    @Transactional
    public void createUsersWithTransaction(){
        User user1 = new User();
        user1.setUsername("유관순");
        user1.setPassword("123456");
        user1.setEmail("kkkkk@abc.co.kr");
        user1.setAddress("부산 특별시");

        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("강감찬");
        user2.setPassword("123456");
        user2.setEmail("cccc@abc.co.kr");
        user2.setAddress("서울 특별시");
        userRepository.save(user2);

        if(true){
            throw  new RuntimeException("강제로 트랜잭션을 롤백합니다.");
        }

        User user3 = new User();
        user3.setUsername("윤봉길");
        user3.setPassword("11111");
        user3.setEmail("yyyy@abc.co.kr");
        user3.setAddress("강릉시 ");
        userRepository.save(user3);

    }

    @Transactional(propagation=Propagation.REQUIRED,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = {Exception.class}, readOnly = true
    )
    public void createUsersWithoutTransaction(){
        User user1 = new User();
        user1.setUsername("유관순1");
        user1.setPassword("123456");
        user1.setEmail("kkkkk@abc.co.kr");
        user1.setAddress("부산 특별시");

        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("강감찬1");
        user2.setPassword("123456");
        user2.setEmail("cccc@abc.co.kr");
        user2.setAddress("서울 특별시");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("윤봉길1");
        user3.setPassword("11111");
        user3.setEmail("yyyy@abc.co.kr");
        user3.setAddress("강릉시 ");
        userRepository.save(user3);

    }

}

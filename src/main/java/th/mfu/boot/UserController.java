package th.mfu.boot;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    public UserRepository repo;

    // POST /users - ลงทะเบียนผู้ใช้ใหม่
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (repo.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exists", HttpStatus.CONFLICT);
        }

        repo.save(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    // GET /users - ดึงรายชื่อผู้ใช้ทั้งหมด
    @GetMapping
    public ResponseEntity<List<User>> list() {
        List<User> users = new ArrayList<>();
        repo.findAll().forEach(users::add);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // GET /users/{username} - ดึงข้อมูลผู้ใช้ตาม username
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        User user = repo.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // DELETE /users/{id} - ลบผู้ใช้ตาม id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        repo.deleteById(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
}
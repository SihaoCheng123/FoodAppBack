package com.app.foodapp.services;

import com.app.foodapp.dto.ApiDelivery;
import com.app.foodapp.dto.LoginResponse;
import com.app.foodapp.models.Roles;
import com.app.foodapp.models.Users;
import com.app.foodapp.repositories.RolRepository;
import com.app.foodapp.repositories.UserRepository;
import com.app.foodapp.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwt;

    public List<Users> getAllUsers(){
        return this.userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id){
        return this.userRepository.findById(id);
    }

    public Optional<Users> getUserByEmail(String email){
        return this.userRepository.findUserByEmail(email);
    }

    public void deleteUserById(Long id){
        Users userOptional = this.userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado con ID:" + id));
        this.userRepository.delete(userOptional);
    }

    public Users updateUser(Users user, Long id){
        Users userOptional = this.userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado"));

        userOptional.setFirstName(user.getFirstName());
        userOptional.setLastName(user.getLastName());
        userOptional.setEmail(user.getEmail());
        userOptional.setPhone(user.getPhone());
        userOptional.setImage(user.getImage());

        if (user.getPassword() != null && user.getPassword().isEmpty()){
            userOptional.setPassword(this.passwordEncoder.encode(user.getPassword()));
        }

        return this.userRepository.save(userOptional);
    }

    public Users createUser(Users user){
        if (this.userRepository.findUserByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Usuario ya existe");
        }
        //En caso de no existir, creamos un objeto de tipo usuario en el back
        //Le asignamos los valores de los datos del usuario del front
        Users newUser = new Users();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(this.passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        newUser.setImage("");

        //Creamos un array vacío de roles
        Set<Roles> roles = new HashSet<>();
        //Si del front no asginamos un rol por defecto, asignamos el que queramos nosotros
        if (user.getRoles() == null || user.getRoles().isEmpty()){
            //Seleccionamos el rol de la base de datos que tenga el nombre "Admin"
            Roles defaultRol = this.rolRepository.findRolByName("Admin");
            if (defaultRol != null){ //Si obtenemos un valor dee la base de datos que coincida, lo añadimos al array roles
                roles.add(defaultRol);
            }else { //Si no encontramos el rol, paramos la creación del usuario
                throw new RuntimeException("No se puede agregar el rol");
                }
        }else {
            //En caso de que el front nos llegue un usuario con un array con un mínimo rol asignado
            //recorremos el array y buscamos en la bd por nombre cada objeto de rool y lo guardamos en el array
            for (Roles rol : user.getRoles()){
                Roles newRol = this.rolRepository.findRolByName(rol.getName());
                roles.add(newRol);
            }
        }
        newUser.setRoles(roles); //Asignamos al neuvo usuario los roles establecidos
        return this.userRepository.save(newUser); //Guardamos el usuario
    }

    public ApiDelivery<LoginResponse> login(String email, String password){
       // Users optionalUser = this.userRepository.findUserByEmail(email).orElseThrow(()-> new RuntimeException("Usuario no ha sido encontrado con id"));
        Optional<Users> optionalUser = this.userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()){
            return new ApiDelivery<>("Usuario no encontrado", false, 404, null, "No encontrado");
        }

        Users user = optionalUser.get();
        if (!this.passwordEncoder.matches(password, user.getPassword())){
            return new ApiDelivery<>("Contraseña incorrecta", false, 400, null, "Contraseña incorrecta");
        }
        String token = this.createToken(email);
        LoginResponse login = new LoginResponse(user, token);
        return new ApiDelivery<>("Login exitosos", true, 200, login, "Login exitoso");

    }

    public String createToken(String email){
        return  this.jwt.generateToken(email);
    }

}

package com.app.ecommere.service;

import com.app.ecommere.entity.User;
import com.app.ecommere.exception.ResourceNotFoundException;
import com.app.ecommere.model.UserDTO;
import com.app.ecommere.payload.response.UserResponse;
import com.app.ecommere.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;


    public UserResponse getAllUser (int pageNo ,int pageSize , String sortBy , String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo-1,pageSize, sort);

        Page<User> users = userRepository.findAll(pageable);

        List<User> listOfUser = users.getContent();

        List<UserDTO> content = listOfUser.stream().map(this::mapToDTO).collect(Collectors.toList());

        UserResponse userResponse = new UserResponse();
        userResponse.setUsers(content);
        userResponse.setPageNo(users.getNumber()+1);
        userResponse.setPageSize(users.getSize());
        userResponse.setTotalElements(users.getTotalElements());
        userResponse.setTotalPages(users.getTotalPages());
        userResponse.setLast(users.isLast());

        return userResponse;
    }

    public UserDTO getUserById(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",id));
        return mapToDTO(user);
    }

    public UserDTO updateUser (UserDTO userDTO ,Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",id));

        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        User updatedUser = userRepository.save(user);

        return mapToDTO(updatedUser);
    }

    public void deleteUserById (Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User","id",id));
        userRepository.delete(user);
    }


    private UserDTO mapToDTO (User user){
        return mapper.map(user,UserDTO.class);
    }

    private User mapToEntity (UserDTO userDTO){
        return  mapper.map(userDTO,User.class);
    }

}

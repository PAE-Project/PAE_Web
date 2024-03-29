package com.example.demo.springbootdeveloper.service;

import com.example.demo.springbootdeveloper.DTO.AddShareRequest;
import com.example.demo.springbootdeveloper.DTO.UpdateShareRequest;
import com.example.demo.springbootdeveloper.domain.Share;
import com.example.demo.springbootdeveloper.domain.User;
import com.example.demo.springbootdeveloper.repository.ShareRepository;
import com.example.demo.springbootdeveloper.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShareService {

    private final ShareRepository shareRepository;

    private final UserRepository userRepository;

    public Share save(AddShareRequest request) {

        User user = userRepository.findByNickname(request.getNickname());
        if(user != null) {
            return shareRepository.save(request.toEntity());
        } else {
            throw new IllegalArgumentException("not found " + request.getNickname());
        }
    }

    public List<Share> findAll() {
        return shareRepository.findAll();
    }

    public Share findById(long id) {
        return shareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found " + id));
    }

    public List<Share> findByAddress(String address) {
        return shareRepository.findByAddress(address);
    }

    public List<Share> findByNickname(String nickname) {
        return shareRepository.findByNickname(nickname);
    }

    public List<Share> findSale() {
        return shareRepository.findByState("판매 중");
    }

    public List<Share> findSaleEnd() {
        return shareRepository.findByState("판매 완료");
    }

    public List<Share> findSharing() {
        return shareRepository.findByState("나눔 중");
    }

    public List<Share> findSharingEnd() {
        return shareRepository.findByState("나눔 완료");
    }

    public List<Share> findClothes() {
        return shareRepository.findByCategory("의류");
    }

    public List<Share> findSupplies() {
        return shareRepository.findByCategory("용품");
    }

    public void delete(long id) {
        shareRepository.deleteById(id);
    }

    @Transactional
    public Share update(long id, UpdateShareRequest request) {

        Share share = shareRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found " + id));

        User user = userRepository.findByNickname(request.getNickname());
        if (user == null) {
            throw new IllegalArgumentException("User not found with nickname: " + request.getNickname());
        }

        if (share.getNickname().equals(user.getNickname())) {
            shareRepository.update(id, request);
            return share;
        } else {
            throw new IllegalArgumentException();
        }
    }
}

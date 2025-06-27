package com.fptu.hk7.basicolawer.service;

import com.fptu.hk7.basicolawer.dto.Request.RequestDTO;
import com.fptu.hk7.basicolawer.pojo.Request;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RequestService {
    List<Request> findAll();
    Optional<Request> findById(UUID id);
    Request save(Request request);
    Request update(UUID id, Request request);
    void deleteById(UUID id);
    List<RequestDTO> findByUserId();
    Optional<Request> findByTxnRef(String txnRef);

}

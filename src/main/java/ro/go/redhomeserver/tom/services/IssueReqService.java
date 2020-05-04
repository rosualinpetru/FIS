package ro.go.redhomeserver.tom.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.models.IssueReq;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.IssueReqRepository;

import java.util.Map;

@Service
public class IssueReqService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    IssueReqRepository issueReqRepository;

    public void reportIssueWithData(Map<String, String> params) {

        issueReqRepository.save(new IssueReq(params.get("description" ),accountRepository.findById(Integer.parseInt(params.get("myId")))));   // to save the issue req in the data base

    }
}

package ro.go.redhomeserver.tom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.go.redhomeserver.tom.dtos.PendingIssue;
import ro.go.redhomeserver.tom.exceptions.UserNotFoundException;
import ro.go.redhomeserver.tom.models.Account;
import ro.go.redhomeserver.tom.models.IssueRequest;
import ro.go.redhomeserver.tom.repositories.AccountRepository;
import ro.go.redhomeserver.tom.repositories.IssueRequestRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IssueRequestService {

    private final AccountRepository accountRepository;
    private final IssueRequestRepository issueRequestRepository;

    @Autowired
    public IssueRequestService(AccountRepository accountRepository, IssueRequestRepository issueRequestRepository) {
        this.accountRepository = accountRepository;
        this.issueRequestRepository = issueRequestRepository;
    }

    public void addIssueRequest(String username, Map<String, String> params) throws UserNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByUsername(username);
        if(accountOptional.isPresent()) {
            issueRequestRepository.save(new IssueRequest(params.get("description"), accountOptional.get()));
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<PendingIssue> loadAllPendingIssueRequests() {
        Comparator<IssueRequest> compareByIssueReq = Comparator.comparingInt(i -> i.getAccount().getEmployee().getDepartment().getId());
        List<IssueRequest> withoutNull = (List<IssueRequest>) issueRequestRepository.findAll();
        List<IssueRequest> onlyNull = (List<IssueRequest>) issueRequestRepository.findAll();

        withoutNull.removeIf(issueRequest -> issueRequest.getAccount()==null);
        onlyNull.removeIf(issueRequest -> issueRequest.getAccount()!=null);

        withoutNull.sort(Comparator.nullsFirst(compareByIssueReq));

        List<PendingIssue> result = onlyNull.stream().map((s -> new PendingIssue(s.getId(), "IT", "IT-SERVICE", s.getDescription()))).collect(Collectors.toList());
        result.addAll(withoutNull.stream().map(s -> new PendingIssue(s.getId(), s.getAccount().getEmployee().getDepartment().getName(), s.getAccount().getEmployee().getName(), s.getDescription())).collect(Collectors.toList()));
        return result;
    }

    public void deleteIssueRequestById(int issueRequestId) {
        issueRequestRepository.deleteById(issueRequestId);
    }

}
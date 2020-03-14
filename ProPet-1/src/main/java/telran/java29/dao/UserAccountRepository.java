package telran.java29.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.java29.domain.UserAccount;

public interface UserAccountRepository extends MongoRepository<UserAccount, String>{
	

}
